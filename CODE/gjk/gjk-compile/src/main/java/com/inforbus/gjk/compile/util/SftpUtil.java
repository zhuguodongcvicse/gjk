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
 
public class SftpUtil {
 
    private SftpUtil() {
    }
 
    public static void uploadFilesToServer(String srcPath, String dst,String ip,String userName,String passWord, SftpProgressMonitor monitor) throws Exception {
        ChannelSftp sftp = upload(srcPath, dst,ip,userName,passWord, monitor);
        if (sftp != null) {
            sftp.quit();
            sftp.disconnect();
            System.out.println(" SFTP disconnect successfully!");
        }
        ChannelSftpSingleton.getInstance().closeChannel();
    }
 
    //上传
    private static ChannelSftp upload(String path, String dst,String ip,String userName,String passWord, SftpProgressMonitor monitor) throws SftpException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        ChannelSftp chSftp = null;
        try {
            chSftp = ChannelSftpSingleton.getInstance().getChannelSftp(ip,userName,passWord);
        } catch (JSchException e) {
            e.printStackTrace();
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
                    upload(fp, mkdir,ip,userName,passWord, monitor);
                } else {
                    chSftp.put(fp, dst, monitor, ChannelSftp.OVERWRITE);
                }
            }
        } else {
            String fp = file.getAbsolutePath();
            chSftp.put(fp, dst, monitor, ChannelSftp.OVERWRITE);
        }
        return chSftp;
    }
    
    
    //下载
    public static void download(String dir,String src,String saveFile,ChannelSftp sftp) throws UnsupportedEncodingException{
    	Vector conts = null;
    	try {
    		conts = sftp.ls(src);
    	}catch(SftpException e) {
    		e.printStackTrace();
    	}
    	if(saveFile.indexOf(".") > -1) {
    		
    	}else {
    		File file = new File(saveFile);
        	if(!file.exists()) file.mkdir();
    	}
    	//文件
    	if(src.indexOf(".") > -1) {
    		try {
    			sftp.get(src,saveFile);
    		}catch(SftpException e) {
    			e.printStackTrace();
    		}
    	}else {
    		//文件夹
    		for(Iterator iterator = conts.iterator(); iterator.hasNext();) {
    			LsEntry obj =(LsEntry) iterator.next();
    			String filename = new String(obj.getFilename().getBytes(),"UTF-8");
    			if(!(filename.indexOf(".") > -1)) {
    				src = FileUtil.normalize(dir + "/" + filename);
    			}else {
    				//扫描到文件名为“..” 这样的直接跳过
    				String[] arrs = filename.split("\\.");
    				if(!filename.equals(".") && !filename.equals("..")) {
    					src = FileUtil.normalize(dir + "/" + filename);
    				}else {
    					continue;
    				}
    			}
    			download(src,src,FileUtil.normalize(saveFile + "/" + filename),sftp);
    		}
    	}
    }
    
    
    public static void main(String[] args) {
    	String wPath = "D:\\doudou\\compile\\compile\\syliox";
    	String lPath = "/gjk/project";
    	try {
    		SftpUtil.uploadFilesToServer(wPath, lPath,"192.168.234.129","root1","root", new SftpProgressMonitor() {
				
				@Override
				public void init(int op, String src, String dest, long max) {
					// TODO Auto-generated method stub
					System.out.println(" 正在上传 " + src + " 到 " + dest + " , 文件大小： " + (double) (max / 1024) + "kb");
				}
				
				@Override
				public void end() {
					// TODO Auto-generated method stub
					System.out.println("上传成功");
				}
				
				@Override
				public boolean count(long count) {
					// TODO Auto-generated method stub
					return true;
				}
			});
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	ChannelSftpSingleton.channelSftpNull();
    	ChannelSftp chSftp = null;
        try {
            chSftp = ChannelSftpSingleton.getInstance().getChannelSftp("192.168.234.129","root1","root");
        } catch (JSchException e) {
            e.printStackTrace();
        }
    	try {
			download("/gjk/project/srFrame_Mul.zip","/gjk/project/srFrame_Mul.zip","D:\\doudou_\\test",chSftp);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			ChannelSftpSingleton.getInstance().closeChannel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ChannelSftpSingleton.channelSftpNull();
    }
 
}

