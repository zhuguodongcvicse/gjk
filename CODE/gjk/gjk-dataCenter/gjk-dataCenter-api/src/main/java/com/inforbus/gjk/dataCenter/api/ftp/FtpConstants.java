package com.inforbus.gjk.dataCenter.api.ftp;

/**
 * 
 * 说明：ftp配置类
 * 
 * Created by luojie on 2019/04/16.
 * 
 */

public class FtpConstants {

	public static final String root = "/";

	public static int MaxTotal = 20;

	public static final int MinIdel = 1;

	public static final int MaxIdle = 8;

	public static final int MaxWaitMillis = 10000;

	/**
	 * 
	 * 本地字符编码
	 * 
	 */

	public static final String LOCAL_CHARSET = "UTF-8";

	// FTP协议里面，规定文件名编码为iso-8859-1

	public static final String SERVER_CHARSET = "iso-8859-1";

	/**
	 * 
	 * 各类文件存放规划路径目录名
	 * 
	 */

	public static final String WORKDIR_PAT = "/upload/pat";// 专利文件主目录

	public static final String WORKDIR_PATDATAS = "datas"; // 专利资料存放路径

	public static final String WORKDIR_PATIMGS = "imgs"; // 专利图片存放路径

	public static final String WORKDIR_PATCONTRACT = "contract"; // 专利合同存放路径

	public static final String WORKDIR_PATPAY_ORDER_C = "payorderforc"; // 专利付款单 （对公司）

	public static final String WORKDIR_PATPAY_ORDER_P = "payorderforp"; // 专利付款单（对平台）

	public static final String WORKDIR_PAT_PROTOCOL_S = "protocols"; // 卖方专利转让协议文件

	public static final String WORKDIR_PAT_PROTOCOL_B = "protocolb"; // 买方专利转让协议文件

	/**
	 * 
	 * 专利上传下载分类
	 * 
	 */

	public static final String TYPE_PATDATAS = "1"; // 专利资料上传下载

	public static final String TYPE_PATIMGS = "2"; // 专利图片上传下载

	public static final String TYPE_PATCONTRACT = "3"; // 专利合同上传下载

	public static final String TYPE_PATPAY_ORDER_C = "4"; // 专利付款单 （对公司）上传下载

	public static final String TYPE_PATPAY_ORDER_P = "5"; // 专利付款单（对平台）上传下载

	public static final String TYPE_PAT_PROTOCOL_S = "6"; // 卖方专利转让协议文件上传下载

	public static final String TYPE_PAT_PROTOCOL_B = "7"; // 买方专利转让协议文件上传下载

	/****
	 * 
	 * ftp.Host=192.168.xx.xx 本机ip
	 * 
	 * ftp.Port=21
	 * 
	 * ftp.UserName=root
	 * 
	 * ftp.PassWord=root
	 * 
	 * ftp.workDir=/images
	 * 
	 * ftp.encoding=utf-8
	 * 
	 * ftp.root=/
	 * 
	 * ftp.MaxTotal=100
	 * 
	 * ftp.MinIdel=2
	 * 
	 * ftp.MaxIdle=5
	 * 
	 * ftp.MaxWaitMillis=3000
	 * 
	 **/

}
