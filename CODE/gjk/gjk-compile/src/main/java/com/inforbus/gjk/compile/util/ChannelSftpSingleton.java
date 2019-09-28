package com.inforbus.gjk.compile.util;
import com.jcraft.jsch.*;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
 
public class ChannelSftpSingleton {
 
    private static ChannelSftpSingleton instance;
    private static ChannelSftp channelSftp;
    private Session session;
    
    private ChannelSftpSingleton() {
    }
 
    public static ChannelSftpSingleton getInstance() {
        if (instance == null) {
            instance = new ChannelSftpSingleton();
        }
        return instance;
    }
    public static void channelSftpNull() {
        if (channelSftp != null) {
        	channelSftp = null;
        }
    }
 
    public ChannelSftp getChannelSftp(String ip,String userName,String passWord) throws JSchException {
        if (channelSftp != null) {
            return channelSftp;
        }
        channelSftp = getChannel(ip,userName,passWord);
        return channelSftp;
    }
 
    /**
     * 断开SFTP Channel、Session连接
     *
     * @throws Exception
     */
    public void closeChannel() throws Exception {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
        System.out.println("disconnected SFTP successfully!");
    }
 
    /**
     * 获得SFTP Channel
     *
     * @return ChannelSftp Instance
     * @throws JSchException
     */
    private ChannelSftp getChannel(String ip,String userName,String passWord) throws JSchException {
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
        System.out.println("Connected successfully to ftpHost = " + ip + ",as ftpUserName = " + userName + ", returning: " + channel);
        return (ChannelSftp) channel;
    }
}
