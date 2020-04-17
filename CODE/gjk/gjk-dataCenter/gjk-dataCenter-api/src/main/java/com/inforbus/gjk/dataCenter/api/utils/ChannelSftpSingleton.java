package com.inforbus.gjk.dataCenter.api.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * ChannelSftpSingleton
 *
 * @author wang
 * @date 2020/4/8
 * @Description sftp连接linux服务器
 */
public class ChannelSftpSingleton {

    private static final Logger logger = LoggerFactory.getLogger(ChannelSftpSingleton.class);

    private static ChannelSftpSingleton instance;

    private static ChannelSftp channelSftp;

    private Session session;

    private ChannelSftpSingleton() {
    }

    public static ChannelSftpSingleton getInstance() {
        if (instance == null) {
            instance = new ChannelSftpSingleton();
        }
        return instance;//返回连接对象
    }

    public static void channelSftpNull() {
        if (channelSftp != null) {
            channelSftp = null;
        }
    }

    public ChannelSftp getChannelSftp(String ip, String userName, String passWord) throws JSchException {
        if (channelSftp != null) {
            return channelSftp;
        }
        channelSftp = getChannel(ip, userName, passWord);
        return channelSftp;
    }

    /**
     * @Author wang
     * @Description: 断开SFTP Channel、Session连接
     * @Param: []
     * @Return: void
     * @Create: 2020/4/8
     */
    public void closeChannel() throws Exception {
        logger.debug("closeChannel方法开始运行，开始断开Channel，Session连接");
        try {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
            logger.debug("disconnected SFTP successfully!");
        } catch (Exception e) {
            logger.error("Channel,Session断开连接失败");
        }
        logger.debug("closeChannel方法运行结束");
    }

    /**
     * @Author wang
     * @Description: 获得SFTP Channel
     * @Param: [ip, userName, passWord]
     * @Return: com.jcraft.jsch.ChannelSftp
     * @Create: 2020/4/8
     */
    private ChannelSftp getChannel(String ip, String userName, String passWord) throws JSchException {
        logger.debug("getChannel方法开始运行");
        // 创建JSch对象
        JSch jsch = new JSch();
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(userName, ip, 22);
        // 设置密码
        session.setPassword(passWord);
        Properties configTemp = new Properties();
        configTemp.put("StrictHostKeyChecking", "no");
        // 为Session对象设置properties
        session.setConfig(configTemp);
        // 设置timeout时间
        session.setTimeout(60000);
        session.connect();
        // 通过Session建立链接
        // 打开SFTP通道
        Channel channel = session.openChannel("sftp");
        // 建立SFTP通道的连接
        channel.connect();
        logger.debug("Connected successfully to ftpHost = " + ip + ",as ftpUserName = " + userName + ", returning: " + channel);
        logger.debug("getChannel方法运行结束");
        return (ChannelSftp) channel;
    }
}
