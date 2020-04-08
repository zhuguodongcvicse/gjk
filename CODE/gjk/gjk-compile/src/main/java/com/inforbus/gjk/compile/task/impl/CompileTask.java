package com.inforbus.gjk.compile.task.impl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.compile.constant.PlatformType;
import com.inforbus.gjk.compile.task.Task;
import com.inforbus.gjk.compile.taskThread.StreamManage;
import com.inforbus.gjk.compile.util.ChannelSftpSingleton;
import com.inforbus.gjk.compile.util.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpProgressMonitor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * CompileTask
 *
 * @author wang
 * @date 2020/4/8
 * @Description 编译功能任务实现类
 */
@Component("compileTask")
@Scope("prototype")
public class CompileTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(CompileTask.class);

    //配置文件读取路径
    @Value("${VS2010.path}")
    private String vsPath;

    @Value("${Workbench.path}")
    private String wbPath;

    @Value("${Linux.path}")
    private String linuxPath;

    //读取配置信息
    @Value("${Linux.ip}")
    private String ip;

    @Value("${Linux.username}")
    private String username;

    @Value("${Linux.password}")
    private String password;

    @Value("${downPath.path}")
    private String dPath;
    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    private Connection connection;//linux服务器连接对象

    private String path1;//被编译文件地址

    private String fileName;//被编译文件名

    private String platformType;//平台

    private String token;//登录票据

    public String getPath1() {
        return path1;
    }

    public void setPath1(String path1) {
        this.path1 = path1;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @Author wang
     * @Description: 编译功能方法，方法中根据平台类型调用对应平台的编译方法
     * @Param:
     * @Return: boolean
     * @Create: 2020/4/8
     */
    @Override
    public boolean command() {
        logger.debug("command方法开始运行");
        Set<String> filePathList = new TreeSet<String>();
        //VS编译
        String fileEndName = ".sln";
        File file = new File(this.getPath1());
        String str = "";
        //判断是否存在sln文件
        boolean isFile = false;
        if (file.exists()) {
            File[] childFiles = file.listFiles();
            if (this.platformType.equals(PlatformType.VS2010)) {
                //判断是否是VS2010
                for (File childFile : childFiles) {
                    //判断是否是VS平台
                    if (childFile.getName().endsWith(fileEndName)) {
                        isFile = true;
                        return devenv(childFile.getAbsolutePath(), this.fileName, this.token);
                    }
                }
            } else if (this.platformType.equals(PlatformType.SYLIXOS)) {
                //判断是否是Sylixos平台
                isFile = true;
                return sylixos(file.getAbsolutePath(), this.fileName, this.token);
            } else if (this.platformType.equals(PlatformType.WORKBENCH)) {
                //判断是否是Workbench平台
                isFile = true;
                Set<String> set = new HashSet<String>();
                List<String> list = new ArrayList<String>();
                FileUtil.getSelectStrFilePathList(set, file.getAbsolutePath(), "Makefile");
                list.addAll(set);
                return workbench(list.get(0), this.fileName, this.token);
            } else if (this.platformType.equals(PlatformType.LINUX)) {
                //判断是否是 Linux平台
                isFile = true;
                return linux(file.getAbsolutePath(), this.fileName, this.token);

            }
        } else {
            logger.error("传入路径错误," + this.path1 + "文件路径不存在");
        }

        if (!isFile) {
            FileUtil.getSelectStrFilePathList(filePathList, this.getPath1(), fileEndName);
            if (filePathList.size() > 0) {
                for (String slnPath : filePathList) {
                    return devenv(slnPath, this.fileName, this.token);
                }
            }
        }
        logger.debug("command方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: 编译windows平台vs2010
     * @Param: [slnpath, fileName, token]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    private boolean devenv(String slnpath, String fileName, String token) {
        logger.debug("devenv方法开始运行，Windows平台项目开始编译");
        File dir = new File(this.vsPath);
        //编译单个项目
        String str = "devenv " + slnpath + " /Rebuild";
        String[] cmd = new String[]{"cmd", "/c", str};
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            p = rt.exec(cmd, null, dir);//打开cmd执行make命令
            StreamManage errorThread = new StreamManage(p.getErrorStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译错误控制台信息
            StreamManage successThread = new StreamManage(p.getInputStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译正常控制台信息
            errorThread.start();//开启编译错误流线程
            successThread.start();//开启编译正常流线程
            return true;
        } catch (Exception e) {
            logger.error("vs2010平台编译失败，请检查编译进程。" + e.getMessage());
        } finally {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                logger.error("vs2010平台编译失败，请检查编译进程。" + e.getMessage());
            }
            if (p != null)
                p.destroy();
        }
        logger.debug("devenv方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: sylixos平台编译功能实现方法
     * @Param: [sylixosPath, fileName, token]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    private boolean sylixos(String sylixosPath, String fileName, String token) {
        logger.debug("sylixos方法开始运行，sylixos平台项目开始编译");
        File dir = new File(sylixosPath);
        //拼接 cmd命令
        String str = "make clean";
        String[] cmd = new String[]{"cmd", "/c", str};
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            p = rt.exec(cmd, null, dir);//打开cmd执行make命令
            StreamManage errorThread = new StreamManage(p.getErrorStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译错误控制台信息
            StreamManage successThread = new StreamManage(p.getInputStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译正常控制台信息
            errorThread.start();//开启编译错误流线程
            successThread.start();//开启编译正常流线程
            errorThread.join();//等待线程结束
            successThread.join();//等待线程结束
            str = "make all";
            cmd = new String[]{"cmd", "/c", str};
            p = rt.exec(cmd, null, dir);//打开cmd执行make命令
            StreamManage errorThread1 = new StreamManage(p.getErrorStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译错误控制台信息
            StreamManage successThread1 = new StreamManage(p.getInputStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译正常控制台信息
            errorThread1.start();//开启编译错误流线程
            successThread1.start();//开启编译正常流线程
            return true;
        } catch (Exception e) {
            logger.error("sylixos平台编译失败，请检查编译进程是否阻塞。" + e.getMessage());
            str = "taskkill /f /im make";
            cmd = new String[]{"cmd", "/c", str};
            try {
                p = rt.exec(cmd, null, dir);
            } catch (IOException e1) {
                logger.error("sylixos平台编译失败，请检查IO。" + e.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                logger.error("sylixos平台编译失败，请检查IO。" + e.getMessage());
            }
            if (p != null)
                p.destroy();
        }
        logger.debug("sylixos方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: workbench平台编译功能实现方法
     * @Param: [sylixosPath, fileName, token]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    private boolean workbench(String workbenchPath, String fileName, String token) {
        logger.debug("workbench方法开始运行，workbench平台项目开始编译");
        File dir = new File(this.wbPath);
        //拼接 cmd命令
        String str = "wrenv -p vxworks-6.8 make -C " + workbenchPath + " --no-print-directory BUILD_SPEC= MIPSI64disable_SMP DEBUG_MODE=1 TRACE=1 clean";
        String[] cmd = new String[]{"cmd", "/c", str};
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            p = rt.exec(cmd, null, dir);//打开cmd执行make命令
            StreamManage errorThread = new StreamManage(p.getErrorStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译错误控制台信息
            StreamManage successThread = new StreamManage(p.getInputStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译正常控制台信息
            errorThread.start();//开启编译错误流线程
            successThread.start();//开启编译正常流线程
            errorThread.join();
            successThread.join();
            str = "wrenv -p vxworks-6.8 make -C " + workbenchPath + " --no-print-directory BUILD_SPEC= MIPSI64disable_SMP DEBUG_MODE=1 TRACE=1";
            cmd = new String[]{"cmd", "/c", str};
            p = rt.exec(cmd, null, dir);
            StreamManage errorThread2 = new StreamManage(p.getErrorStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译错误控制台信息
            StreamManage successThread2 = new StreamManage(p.getInputStream(), "GBK", this.rabbitmqTemplate, fileName, token);//编译正常控制台信息
            errorThread2.start();//开启编译错误流线程
            successThread2.start();//开启编译正常流线程
            return true;
        } catch (Exception e) {
            logger.error("workbench平台编译失败，请检查编译进程。" + e.getMessage());
        } finally {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                logger.error("workbench平台编译失败，请检查编译进程。" + e.getMessage());
            }
            if (p != null)
                p.destroy();
        }
        logger.debug("workbench方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: linux平台编译功能实现方法
     * @Param: [filePath, fileName, token]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    private boolean linux(String filePath, String fileName, String token) {
        logger.debug("linux方法开始运行，linux平台项目开始编译");
        //执行linux命令编译 项目
        String cmd = null;
        cmd = "mkdir -p " + this.linuxPath + "/AppPro/" + fileName;
        //再创建工程文件夹
        boolean b = executeCommand(cmd, false);
        int i = 0;
        while (true) {
            i++;
            if (i == 500)
                break;
        }
        if (b) {
            String linuxAimsPath = this.linuxPath + "/AppPro/" + fileName;
            //将Windows 项目文件上传到linux服务器中
            boolean isUploadSuccess = uploadFileToLinux(filePath, linuxAimsPath);
            if (isUploadSuccess) {
                cmd = "find " + this.linuxPath + "/AppPro/" + fileName + " -name makefile";
                //寻找makefile文件所在绝对路径
                String makeFilePath = executeCommand2(cmd);
                if (makeFilePath != null && !makeFilePath.equals("")) {
                    cmd = "cd " + makeFilePath + " ; make clean ; make";
                    boolean isMakeSuccess = executeCommand3(cmd);//执行make命令
                    if (isMakeSuccess) {
                        cmd = "cd " + makeFilePath + " ; zip -r " + fileName + ".zip " + "*";
                        boolean isZipSuccess = executeCommand(cmd, true);//执行打包命令
                        if (isZipSuccess) {
                            //将连接状态设置成null
                            ChannelSftpSingleton.channelSftpNull();
                            //下载压缩包
                            ChannelSftp chSftp = null;
                            try {
                                chSftp = ChannelSftpSingleton.getInstance().getChannelSftp(this.ip, this.username, this.password);
                                try {
                                    File downFile = new File(this.dPath);
                                    if (!downFile.exists()) {
                                        downFile.mkdirs();
                                    }
                                    SftpUtil.download(makeFilePath + "/" + fileName + ".zip", makeFilePath + "/" + fileName + ".zip", this.dPath, chSftp);
                                    File rmFile = new File(this.dPath + "\\" + fileName + ".zip");
                                    if (!rmFile.exists()) {
                                        this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n压缩包文件下载失败");
                                        return false;
                                    }
                                    //推送消息到rabbitmq中
                                    this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n压缩包文件下载完毕");
                                    //解压到工程Debug目录下
                                    unZipFiles(this.dPath + "\\" + fileName + ".zip", filePath + "\\Debug");
                                    //解压完删除压缩包
                                    rmFile.delete();
                                } catch (UnsupportedEncodingException e) {
                                    logger.error("下载失败" + e.getMessage());
                                } catch (IOException e) {
                                    logger.error("解压失败" + e.getMessage());
                                }
                            } catch (JSchException e) {
                                logger.error("下载失败，请联系管理员。" + e.getMessage());
                            }

                            try {
                                ChannelSftpSingleton.getInstance().closeChannel();
                            } catch (Exception e) {
                                logger.error("下载失败，请联系管理员。" + e.getMessage());
                            }
                            //将连接状态设置成null
                            ChannelSftpSingleton.channelSftpNull();
                        }
                    }
                }
                cmd = "rm -rf " + this.linuxPath + "/AppPro";
                executeCommand(cmd, false);//删除流程文件夹
                return true;
            } else {
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n 文件上传至linux系统失败,请联系管理员");
            }
        }
        cmd = "rm -rf " + this.linuxPath + "/AppPro";
        executeCommand(cmd, false);//删除流程文件夹
        logger.debug("linux方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: 解压文件到指定目录
     * @Param: [zipPath, descDir]
     * @Return: void
     * @Create: 2020/4/8
     */
    private static void unZipFiles(String zipPath, String descDir) throws IOException {
        logger.debug("unZipFiles方法开始运行");
        InputStream in = null;
        ZipFile zip = null;
        OutputStream out = null;
        try {
            File zipFile = new File(zipPath);
            if (!zipFile.exists()) {
                throw new IOException("需解压文件不存在");
            }
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            zip = new ZipFile(zipFile);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                String outPath = descDir + File.separator + zipEntryName;
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (entry.isDirectory()) {
                    continue;
                }
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                in = zip.getInputStream(entry);
                // 输出文件路径信息
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (zip != null) {
                zip.close();
            }
        }
        logger.debug("unZipFiles方法运行结束");
    }

    /**
     * @Author wang
     * @Description: linux系统连接方法
     * @Param: []
     * @Return: boolean
     * @Create: 2020/4/8
     */
    public boolean linuxLogin() {
        logger.debug("linuxLogin方法开始运行");
        boolean flag = false;
        try {
            this.connection = new Connection(this.ip);
            this.connection.connect();
            flag = this.connection.authenticateWithPassword(this.username, this.password);
        } catch (IOException e) {
            logger.error("连接linux失败,请检查ip,账户,密码是否正确" + e.getMessage());
        }
        logger.debug("linuxLogin方法运行结束");
        return flag;
    }

    /**
     * @Author wang
     * @Description: linux 系统执行cmd命令行方法,flag 为是否把执行命令失败的控制台信息推送到mq中
     * @Param: [cmd, flag]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    public boolean executeCommand(String cmd, boolean flag) {
        logger.debug("executeCommand方法开始运行");
        Session session = null;
        try {
            if (linuxLogin()) {//连接linux服务器
                session = this.connection.openSession();
                session.execCommand(cmd);//执行cmd命令
                String str = processStdout(session.getStdout(), Charset.forName("UTF-8"));
                if (flag) {
                    if (StringUtils.isBlank(str)) {
                        processStdout(session.getStderr(), Charset.forName("UTF-8"));
                    }
                }
                return true;
            } else {
                logger.error("连接linux系统失败,请检查ip,账号,密码");
            }
        } catch (IOException e) {
            logger.error("IO异常" + e.getMessage());
        } finally {
            if (this.connection != null) {
                this.connection.close();
            }
            if (session != null) {
                session.close();
            }
        }
        logger.debug("executeCommand方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: rabbitmq推送消息
     * @Param: [in, charset]
     * @Return: java.lang.String
     * @Create: 2020/4/8
     */
    public String processStdout(InputStream in, Charset charset) {
        logger.debug("processStdout方法开始运行");
        InputStream stdout = new StreamGobbler(in);
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result = line;
                this.rabbitmqTemplate.convertAndSend(this.token, this.fileName + "===@@@===\n" + line);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("获取流对象失败" + e.getMessage());
        } catch (IOException e) {
            logger.error("linux系统IO异常" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("流关闭失败" + e.getMessage());
                }
            }
            if (stdout != null) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    logger.error("流关闭失败" + e.getMessage());
                }
            }
        }
        logger.debug("processStdout方法运行结束");
        return result;
    }

    /**
     * @Author wang
     * @Description: linux 系统执行cmd命令行方法
     * @Param: [cmd]
     * @Return: java.lang.String
     * @Create: 2020/4/8
     */
    public String executeCommand2(String cmd) {
        logger.debug("executeCommand2方法开始运行");
        String str = "";
        Session session = null;
        try {
            if (linuxLogin()) {
                session = this.connection.openSession();
                session.execCommand(cmd);
                str = processStdout2(session.getStdout(), Charset.forName("UTF-8"));
                if (StringUtils.isBlank(str)) {
                    str = processStdout2(session.getStderr(), Charset.forName("UTF-8"));
                }
            } else {
                logger.error("连接linux系统失败,请检查ip,账号,密码");
            }
        } catch (IOException e) {
            logger.error("IO异常" + e.getMessage());
        } finally {
            if (this.connection != null) {
                this.connection.close();
            }
            if (session != null) {
                session.close();
            }
        }
        logger.debug("executeCommand2方法运行结束");
        return str;
    }

    /**
     * @Author wang
     * @Description: rabbitmq推送消息
     * @Param: [in, charset]
     * @Return: java.lang.String
     * @Create: 2020/4/8
     */
    public String processStdout2(InputStream in, Charset charset) {
        logger.debug("processStdout2方法开始运行");
        InputStream stdout = new StreamGobbler(in);
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result = line.substring(0, line.lastIndexOf("/"));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("获取流对象失败" + e.getMessage());
        } catch (IOException e) {
            logger.error("linux系统IO异常" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("流关闭失败" + e.getMessage());
                }
            }
            if (stdout != null) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    logger.error("流关闭失败" + e.getMessage());
                }
            }
        }
        logger.debug("processStdout2方法运行结束");
        return result;
    }

    /**
     * @Author wang
     * @Description: linux 系统执行cmd命令行方法
     * @Param: [cmd]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    public boolean executeCommand3(String cmd) {
        logger.debug("executeCommand3方法开始运行");
        Session session = null;
        try {
            if (linuxLogin()) {//连接linux服务器
                session = this.connection.openSession();
                session.execCommand(cmd);//执行cmd命令
                StreamManage successStream = new StreamManage(session.getStdout(), "UTF-8", this.rabbitmqTemplate, this.fileName, this.token);
                StreamManage errorStream = new StreamManage(session.getStderr(), "UTF-8", this.rabbitmqTemplate, this.fileName, this.token);
                successStream.start();
                errorStream.start();
                successStream.join();
                errorStream.join();
                return true;
            } else {
                logger.error("连接linux系统失败,请检查ip,账号,密码");
            }
        } catch (IOException e) {
            logger.error("IO异常" + e.getMessage());
        } catch (InterruptedException e) {
            logger.error("线程被中断异常" + e.getMessage());
        } finally {
            if (this.connection != null) {
                this.connection.close();
            }
            if (session != null) {
                session.close();
            }
        }
        logger.debug("executeCommand3方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: 上传文件至linux系统中, 参数1为被上穿文件地址, 参数2为linux系统中上传的目标地址
     * @Param: [localFilePath, linuxAmisPath]
     * @Return: boolean
     * @Create: 2020/4/8
     */
    public boolean uploadFileToLinux(String localFilePath, String linuxAmisPath) {
        try {
            SftpUtil.uploadFilesToServer(localFilePath, linuxAmisPath, this.ip, this.username, this.password, new SftpProgressMonitor() {
                @Override
                public void init(int op, String src, String dest, long max) {
                    logger.info(" 正在上传 " + src + " 到 " + dest + " , 文件大小： " + (double) (max / 1024) + "kb");
                    System.out.println(" 正在上传 " + src + " 到 " + dest + " , 文件大小： " + (double) (max / 1024) + "kb");
                }

                @Override
                public void end() {
                    logger.info("上传成功");
                }

                @Override
                public boolean count(long count) {
                    return true;
                }
            });
            return true;
        } catch (Exception e) {
            logger.error("上传失败,请检查ip地址,账号,密码是否正确" + e.getMessage());
        }
        return false;
    }

}
