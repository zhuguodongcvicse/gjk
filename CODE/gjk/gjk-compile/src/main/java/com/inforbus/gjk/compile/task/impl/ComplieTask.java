package com.inforbus.gjk.compile.task.impl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.compile.exception.FileNotFoundException;
import com.inforbus.gjk.compile.task.Task;
import com.inforbus.gjk.compile.util.ChannelSftpSingleton;
import com.inforbus.gjk.compile.util.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpProgressMonitor;
import org.eclipse.jgit.transport.JschSession;
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

/*
* 2019年10月30日 11:19:08
* wang创建
* 编译任务类,编译功能
*
* */
@Component("complieTask")
@Scope("prototype")
public class ComplieTask implements Task {
    private static final Logger logger = LoggerFactory.getLogger(ComplieTask.class);

    //配置文件读取路径
    @Value("${VS2010.path}")
    private String vsPath;

    @Value("${Sylixos.path}")
    private String syPath;

    @Value("${Workbench.path}")
    private String wbPath;


    @Value("${Linux.path}")
    private String lPath;

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

    @Override
    public String Command() {
        Set<String> filePathList = new TreeSet<String>();
        //VS编译
        String fileEndName = ".sln";
        File file = new File(this.getPath1());
        String str = "";
        //判断是否存在sln文件
        boolean isFile = false;
        if (file.exists()) {
            File[] childFiles = file.listFiles();
            if (this.getPlatformType().equals("VS2010")) {
                //判断是否是VS2010
                for (File childFile : childFiles) {
                    //判断是否是VS平台
                    if (childFile.getName().endsWith(fileEndName)) {
                        //filePathList.add(file.getAbsolutePath());
                        isFile = true;
                        str = devenv(childFile.getAbsolutePath(), this.getFileName(), this.getToken());
                    }
                }
            }else if (platformType.equals("Sylixos")) {
                //判断是否是Sylixos平台
                isFile = true;
                str = sylixos(file.getAbsolutePath(), fileName, token);
            } else if (platformType.equals("Workbench")) {
                //判断是否是Workbench平台
                isFile = true;
                Set<String> set = new HashSet<String>();
                List<String> list = new ArrayList<String>();
                FileUtil.getSelectStrFilePathList(set, file.getAbsolutePath(), "Makefile");
                list.addAll(set);
                str = workbench(list.get(0), fileName, token);
            } else if (platformType.equals("Linux")) {
                //判断是否是 Linux平台
                isFile = true;
                str = linux(file.getAbsolutePath(), fileName, token);

            }
        } else {
            logger.error("传入路径错误,"+this.getPath1()+"文件路径不存在");
            //throw new FileNotFoundException("传入路径错误,"+file.getName()+"不存在");
        }

        if (!isFile) {
            FileUtil.getSelectStrFilePathList(filePathList, this.getPath1(), fileEndName);
            if (filePathList.size() > 0) {
                for (String slnPath : filePathList) {
                    str = devenv(slnPath, this.getFileName(), this.getToken());
                }
            }
        }
        return str;
    }
    //编译windows平台vs2010
    private String devenv(String slnpath, String fileName, String token) {
        //String strCommand = "";
        //String path = "D:\\Program Files (x86)\\Microsoft Visual Studio\\2019\\Community\\Common7\\IDE";
        File dir = new File(vsPath);
        //编译解决方案
        //String str = "devenv D:\\Build\\CCode\\ConsoleApplication1\\ConsoleApplication1.sln /Rebuild";
        String str = "devenv " + slnpath + " /Rebuild";
        //编译单个项目
        String str1 = "devenv D:\\CCode\\ConsoleApplication1\\ConsoleApplication1\\ConsoleApplication1.vcxproj /Build 'Release|Win32'/project D:\\CCode\\ConsoleApplication1\\ConsoleApplication1\\ConsoleApplication1.vcxproj";
        String[] cmd = new String[]{"cmd", "/c", str};
        Runtime rt = Runtime.getRuntime();
        LineNumberReader input = null;
        Process p = null;
        try {
            p = rt.exec(cmd, null, dir);
            InputStreamReader ir = new InputStreamReader(p.getInputStream(), "GBK");
            input = new LineNumberReader(ir);
            String line = "";
            while ((line = input.readLine()) != null) {
                //System.out.println(line);
                //strCommand += "\n"+line;
                //推送消息到rabbitmq中
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n" + line);
            }
        } catch (IOException e) {
            logger.error("IO读取失败，请检查组件工程文件。");
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
        }
        return "编译成功";
    }

    //编译 sylixos
    private String sylixos(String sylixosPath, String fileName, String token) {
        String strCommand = "";
        //File dir = new File(syPath);
        File dir = new File(sylixosPath);
        //拼接 cmd命令
        String str = "make all";
        String[] cmd = new String[]{"cmd", "/c", str};
        Runtime rt = Runtime.getRuntime();
        LineNumberReader input = null;
        Process p = null;
        try {
            p = rt.exec(cmd, null, dir);
            //关闭流释放资源

            InputStreamReader ir = new InputStreamReader(p.getInputStream(), "GBK");
            input = new LineNumberReader(ir);
            String line = "";
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                //strCommand += "\n"+line;
                //推送消息到rabbitmq中
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n" + line);
            }
            //System.out.println("111111"+strCommand);
        } catch (IOException e) {
            logger.error("IO读取失败，请检查组件工程文件。");
            e.printStackTrace();
        }finally {
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
            if (p != null) {
                try {
                    p.getOutputStream().close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
        }
        return strCommand;
    }

    //编译 workbench
    private String workbench(String slnpath, String fileName, String token) {
        String strCommand = "";
        //String path = "D:\\Program Files (x86)\\Microsoft Visual Studio\\2019\\Community\\Common7\\IDE";
        File dir = new File(wbPath);
        //拼接 cmd命令
        String str = "wrenv -p vxworks-6.8 make -C " + slnpath + " --no-print-directory BUILD_SPEC= MIPSI64disable_SMP DEBUG_MODE=1 TRACE=1 clean";
        String[] cmd = new String[]{"cmd", "/c", str};
        Runtime rt = Runtime.getRuntime();
        LineNumberReader input = null;
        InputStreamReader ir = null;
        Process p = null;
        try {
            p = rt.exec(cmd, null, dir);
            ir = new InputStreamReader(p.getInputStream(), "GBK");
            input = new LineNumberReader(ir);
            String line = "";
            while ((line = input.readLine()) != null) {
                //System.out.println(line);
                //strCommand += "\n"+line;
                //推送消息到rabbitmq中
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n" + line);
            }
            //System.out.println("111111"+strCommand);
        } catch (IOException e) {
            logger.error("IO读取失败，请检查组件工程文件。");
            e.printStackTrace();
        }finally {
            if (input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
            if (ir!=null){
                try {
                    ir.close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
            if (p!=null){
                try {
                    p.getInputStream().close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
        }
        return strCommand;
    }

    //编译 linux
    private String linux(String filePath, String fileName, String token) {
        //通过filePath 截取流程名称 用来创建父级目录
        String ProcessName = filePath.substring(0, filePath.lastIndexOf("APP"));
        ProcessName = ProcessName.substring(ProcessName.lastIndexOf(File.separator) + 1, ProcessName.length());
        System.out.println("流程名称====" + ProcessName);

        Session session = null;
        //执行linux命令编译 项目
        try {
            Connection connection = new Connection(ip);
            connection.connect();
            connection.authenticateWithPassword(username, password);
            session = connection.openSession();

            //先创建流程文件夹
            session.execCommand("mkdir " + lPath + "/" + ProcessName);

            //再创建工程文件夹
            session = connection.openSession();
            session.execCommand("mkdir " + lPath + "/" + ProcessName + "/" + fileName);

        } catch (IOException e) {
            logger.error("Linux服务器连接失败,请检查IP,账号,密码是否正确");
            e.getStackTrace();
        }finally {
            if (session != null) {
                session.close();
            }
        }


        //将Windows 项目文件上传到linux服务器中
        try {
            SftpUtil.uploadFilesToServer(filePath, lPath + "/" + ProcessName + "/" + fileName, ip, username, password, new SftpProgressMonitor() {

                @Override
                public void init(int op, String src, String dest, long max) {
                    logger.info(" 正在上传 " + src + " 到 " + dest + " , 文件大小： " + (double) (max / 1024) + "kb");
                    System.out.println(" 正在上传 " + src + " 到 " + dest + " , 文件大小： " + (double) (max / 1024) + "kb");
                }

                @Override
                public void end() {
                    logger.info("上传成功");
                    System.out.println("上传成功");
                }

                @Override
                public boolean count(long count) {
                    return true;
                }
            });
        } catch (Exception e) {
            logger.error("上传失败,请检查ip地址,账号,密码是否正确");
            e.printStackTrace();
        }


        //执行linux命令编译 项目
        //makefile文件夹路径
        String makeFilePath = "";
        InputStream is = null;
        BufferedReader brs = null;
        try {
            Connection connection = new Connection(ip);
            connection.connect();
            connection.authenticateWithPassword(username, password);
            session = connection.openSession();

            //先执行查找makefile命令
            session.execCommand("find " + lPath + "/" + ProcessName + "/" + fileName + " -name makefile");

            //获取输出结果 为 makefile 的绝对路径
            is = new StreamGobbler(session.getStdout());
            brs = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String temp = "";
            while ((temp = brs.readLine()) != null) {
                makeFilePath = temp.substring(0, temp.lastIndexOf("/"));
            }

            //找到路径后  执行make命令
            session = connection.openSession();
            session.execCommand("cd " + makeFilePath + " ; make clean ; make");

            //成功返回
            is = new StreamGobbler(session.getStdout());
            brs = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((temp = brs.readLine()) != null) {
                //推送消息到rabbitmq中
                //this.rabbitmqTemplate.convertAndSend(token , token+"===@@@==="+fileName+"===@@@===\n"+temp);
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n" + temp);
            }

            //失败返回
            is = new StreamGobbler(session.getStderr());
            brs = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((temp = brs.readLine()) != null) {
                //推送消息到rabbitmq中
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n" + temp);
            }

            //执行打包命令
            session = connection.openSession();
            session.execCommand("cd " + makeFilePath + " ; zip -r " + fileName + ".zip " + "*");

            //成功返回
            is = new StreamGobbler(session.getStdout());
            brs = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((temp = brs.readLine()) != null) {
                //推送消息到rabbitmq中
                this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n" + temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (session != null) {
                session.close();
            }
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
            if (brs != null){
                try {
                    brs.close();
                } catch (IOException e) {
                    logger.error("IO关闭失败，请联系管理员。");
                    e.printStackTrace();
                }
            }
        }

        //将连接状态设置成null
        ChannelSftpSingleton.channelSftpNull();

        //下载压缩包
        ChannelSftp chSftp = null;
        try {
            chSftp = ChannelSftpSingleton.getInstance().getChannelSftp(ip, username, password);
        } catch (JSchException e) {
            logger.error("下载失败，请联系管理员。");
            e.printStackTrace();
        }
        try {
            SftpUtil.download(makeFilePath + "/" + fileName + ".zip", makeFilePath + "/" + fileName + ".zip", dPath, chSftp);

            //todo,需取得一个下载成功或失败的标志
            //推送消息到rabbitmq中
            this.rabbitmqTemplate.convertAndSend(token, fileName + "===@@@===\n下载完毕");

            //解压到工程Debug目录下
            try {
                unZipFiles(dPath + "\\" + fileName + ".zip", filePath + "\\Debug");
                //todo ,解压成功或失败的判断以及处理
            } catch (IOException e) {
                logger.error("解压失败");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //解压完删除压缩包
            File rmFile = new File(dPath + "\\" + fileName + ".zip");
            rmFile.delete();
        } catch (UnsupportedEncodingException e) {
            logger.error("下载失败");
            e.printStackTrace();
        }
        try {
            ChannelSftpSingleton.getInstance().closeChannel();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //将连接状态设置成null
        ChannelSftpSingleton.channelSftpNull();

        //执行完毕 删除 linux数据
        try {
            Connection connection = new Connection(ip);
            connection.connect();
            connection.authenticateWithPassword(username, password);
            session = connection.openSession();

            //执行删除文件夹命令
            session.execCommand("rm -rf " + lPath + "/" + ProcessName);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (session!=null){
                session.close();
            }
        }

        return "编译成功";
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipPath 压缩文件地址
     * @param descDir 指定目录
     * @throws IOException
     */
    private static void unZipFiles(String zipPath, String descDir) throws IOException {
        InputStream in = null;
        ZipFile zip = null;
        OutputStream out = null;
        try {
            File zipFile = new File(zipPath);
            if (!zipFile.exists()) {
                throw new IOException("需解压文件不存在.");
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
        }finally {
            if (in!=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
            if (zip!=null){
                zip.close();
            }
        }
    }

}
