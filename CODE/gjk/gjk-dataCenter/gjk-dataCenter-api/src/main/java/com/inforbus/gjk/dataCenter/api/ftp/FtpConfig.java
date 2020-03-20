package com.inforbus.gjk.dataCenter.api.ftp;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 
 * 说明：ftp配置
 * 
 * Created by luojie on 2019/04/16.
 * 
 */
@Component
@Data
public class FtpConfig {
	@Value("${ftp.host}") // ftp地址
    private String host;
	
    @Value("${ftp.port}")// 端口号
    private Integer port = 21;
    
    @Value("${ftp.username}")// 登录用户
    private String userName;
    
    @Value("${ftp.password}")// 登录密码
    private String passWord;
    
    @Value("${ftp.passiveMode}")// 被动模式
    private boolean passiveMode = false;

    @Value("${ftp.connectTimeout}")// 连接超时时间(秒)
    private Integer connectTimeout;
    
//    @Value("${ftp.bufferSize}")// 缓冲大小
//    private Integer bufferSize = 1024;
//    
//    @Value("${ftp.transferFileType}")// 传输文件类型
//    private Integer transferFileType;
    @Value("${ftp.ftpPath}")
    private String ftpPath;
    
    @Value("${ftp.dataTimeout}")
    private String dataTimeout;
    
    @Value("${ftp.keepAliveTimeout}")
    private String keepAliveTimeout;



}
