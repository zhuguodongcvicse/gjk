package com.inforbus.gjk.compile.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Vector;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SftpUtil
 *
 * @author wang
 * @date 2020/4/8
 * @Description 上传文件至Linux系统，下载工具类
 */
public class SftpUtil {

    private static final Logger logger = LoggerFactory.getLogger(SftpUtil.class);

    private SftpUtil() {
    }

    /**
     * @Author wang
     * @Description: 上传文件至服务器
     * @Param: [srcPath, dst, ip, userName, passWord, monitor]
     * @Return: void
     * @Create: 2020/4/8
     */
    public static void uploadFilesToServer(String srcPath, String dst, String ip, String userName, String passWord, SftpProgressMonitor monitor) throws Exception {
        logger.debug("uploadFilesToServer方法开始运行");
        ChannelSftp sftp = upload(srcPath, dst, ip, userName, passWord, monitor);
        if (sftp != null) {
            sftp.quit();
            sftp.disconnect();
            logger.debug(" SFTP disconnect successfully!");
        }
        ChannelSftpSingleton.getInstance().closeChannel();
        logger.debug("uploadFilesToServer方法运行结束");
    }

    /**
     * @Author wang
     * @Description: 上传文件方法
     * @Param: [path, dst, ip, userName, passWord, monitor]
     * @Return: com.jcraft.jsch.ChannelSftp
     * @Create: 2020/4/8
     */
    private static ChannelSftp upload(String path, String dst, String ip, String userName, String passWord, SftpProgressMonitor monitor) throws SftpException {
        logger.debug("upload方法开始运行");
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        ChannelSftp chSftp = null;
        try {
            chSftp = ChannelSftpSingleton.getInstance().getChannelSftp(ip, userName, passWord);
        } catch (JSchException e) {
            logger.error("链接linux服务器出现异常" + e.getMessage());
        }
        if (chSftp == null) {
            return null;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
                return null;
            }
            for (File f : files) {
                String fp = f.getAbsolutePath();
                if (f.isDirectory()) {
                    String mkdir = dst + "/" + f.getName();
                    try {
                        chSftp.cd(mkdir);
                    } catch (Exception e) {
                        chSftp.mkdir(mkdir);
                    }
                    upload(fp, mkdir, ip, userName, passWord, monitor);
                } else {
                    chSftp.put(fp, dst, monitor, ChannelSftp.OVERWRITE);
                }
            }
        } else {
            String fp = file.getAbsolutePath();
            chSftp.put(fp, dst, monitor, ChannelSftp.OVERWRITE);
        }
        logger.debug("upload方法运行结束");
        return chSftp;
    }

    /**
     * @Author wang
     * @Description: 下载方法
     * @Param: [dir, src, saveFile, sftp]
     * @Return: void
     * @Create: 2020/4/8
     */
    public static void download(String dir, String src, String saveFile, ChannelSftp sftp) throws UnsupportedEncodingException {
        logger.debug("download方法开始运行");
        Vector conts = null;
        try {
            conts = sftp.ls(src);
        } catch (SftpException e) {
            logger.error("链接linux服务器失败" + e.getMessage());
        }
        if (saveFile.indexOf(".") > -1) {

        } else {
            File file = new File(saveFile);
            if (!file.exists()) file.mkdir();
        }
        //文件
        if (src.indexOf(".") > -1) {
            try {
                sftp.get(src, saveFile);
            } catch (SftpException e) {
                logger.error("Sftp异常" + e.getMessage());
            }
        } else {
            //文件夹
            for (Iterator iterator = conts.iterator(); iterator.hasNext(); ) {
                LsEntry obj = (LsEntry) iterator.next();
                String filename = new String(obj.getFilename().getBytes(), "UTF-8");
                if (!(filename.indexOf(".") > -1)) {
                    src = FileUtil.normalize(dir + "/" + filename);
                } else {
                    //扫描到文件名为“..” 这样的直接跳过
                    String[] arrs = filename.split("\\.");
                    if (!filename.equals(".") && !filename.equals("..")) {
                        src = FileUtil.normalize(dir + "/" + filename);
                    } else {
                        continue;
                    }
                }
                download(src, src, FileUtil.normalize(saveFile + "/" + filename), sftp);
            }
        }
        logger.debug("download方法运行结束");
    }
}

