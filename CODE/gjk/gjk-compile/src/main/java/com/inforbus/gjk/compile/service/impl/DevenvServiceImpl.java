package com.inforbus.gjk.compile.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.compile.service.DevenvService;
import com.inforbus.gjk.compile.util.ChannelSftpSingleton;
import com.inforbus.gjk.compile.util.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpProgressMonitor;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Service("appService")
public class DevenvServiceImpl implements DevenvService{

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
	
	@Override
	public String Command(String path1,String fileName,String platformType) {
		Set<String> filePathList = new TreeSet<String>();
		//VS编译
		String fileEndName = ".sln";

		File file = new File(path1);
		String str = "";
		//判断是否存在sln文件
		boolean isFile = false;
		if(file.exists()) {
			File[] childFiles = file.listFiles();
			if(platformType.equals("VS2010")) {
				//判断是否是VS2010
				for (File childFile : childFiles) {
					//判断是否是VS平台
					if (childFile.getName().endsWith(fileEndName)) {
						//filePathList.add(file.getAbsolutePath());
						isFile = true;
						str = devenv(childFile.getAbsolutePath(),fileName);
					} 
				}
			}else if(platformType.equals("Sylixos")){
				//判断是否是Sylixos平台
				isFile = true;
				str = sylixos(file.getAbsolutePath(),fileName);
			}else if(platformType.equals("Workbench")) {
				//判断是否是Workbench平台
				isFile = true;
				Set<String> set = new HashSet<String>();
				List<String> list = new ArrayList<String>();
				FileUtil.getSelectStrFilePathList(set,file.getAbsolutePath(),"Makefile");
				list.addAll(set);
				str = workbench(list.get(0),fileName);
			}else if(platformType.equals("Linux")) {
				//判断是否是 Linux平台
				isFile = true;
				linux(file.getAbsolutePath(),fileName);
			}
		}else {
			System.out.println("传入路径错误，请联系管理员。");
		}

		if(!isFile) {
			FileUtil.getSelectStrFilePathList(filePathList,path1,fileEndName);
			if(filePathList.size()>0) {
				for(String slnPath : filePathList) {
					str = devenv(slnPath,fileName);
				}
			}
		}
		return str;
	}
	private String devenv(String slnpath,String fileName) {
		String strCommand = "";
		//String path = "D:\\Program Files (x86)\\Microsoft Visual Studio\\2019\\Community\\Common7\\IDE";
		File dir = new File(vsPath);
		//编译解决方案
		//String str = "devenv D:\\Build\\CCode\\ConsoleApplication1\\ConsoleApplication1.sln /Rebuild";
		String str = "devenv "+slnpath+" /Rebuild";
		//编译单个项目
		String str1 = "devenv D:\\CCode\\ConsoleApplication1\\ConsoleApplication1\\ConsoleApplication1.vcxproj /Build 'Release|Win32'/project D:\\CCode\\ConsoleApplication1\\ConsoleApplication1\\ConsoleApplication1.vcxproj";
		String[] cmd = new String[] {"cmd","/c",str}; 
		Runtime rt = Runtime.getRuntime();
		LineNumberReader input = null;

		try {
			Process p = rt.exec(cmd,null,dir);
			InputStreamReader ir = new InputStreamReader(p.getInputStream(),"GBK");
			input = new LineNumberReader(ir);
			String line = "";
			while((line=input.readLine())!=null) {
				//System.out.println(line);
				//strCommand += "\n"+line; 
				//推送消息到rabbitmq中
				this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n"+line);
			}
			//System.out.println("111111"+strCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strCommand;
	}

	//编译 sylixos 
	private String sylixos(String sylixosPath,String fileName) {
		String strCommand = "";
		//File dir = new File(syPath);
		File dir = new File(sylixosPath);
		//拼接 cmd命令
		String str = "make all";
		String[] cmd = new String[] {"cmd","/c",str}; 
		Runtime rt = Runtime.getRuntime();
		LineNumberReader input = null;

		try {
			Process p = rt.exec(cmd,null,dir);
			//关闭流释放资源
			if(p != null){
				p.getOutputStream().close();
			}
			InputStreamReader ir = new InputStreamReader(p.getInputStream(),"GBK");
			input = new LineNumberReader(ir);
			String line = "";
			while((line=input.readLine())!=null) {
				System.out.println(line);
				//strCommand += "\n"+line; 
				//推送消息到rabbitmq中
				this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n"+line);
			}
			//System.out.println("111111"+strCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strCommand;
	}

	//编译 workbench 
	private String workbench(String slnpath,String fileName) {
		String strCommand = "";
		//String path = "D:\\Program Files (x86)\\Microsoft Visual Studio\\2019\\Community\\Common7\\IDE";
		File dir = new File(wbPath);
		//拼接 cmd命令
		String str = "wrenv -p vxworks-6.8 make -C "+slnpath+" --no-print-directory BUILD_SPEC= MIPSI64disable_SMP DEBUG_MODE=1 TRACE=1 clean";
		String[] cmd = new String[] {"cmd","/c",str}; 
		Runtime rt = Runtime.getRuntime();
		LineNumberReader input = null;

		try {
			Process p = rt.exec(cmd,null,dir);
			InputStreamReader ir = new InputStreamReader(p.getInputStream(),"GBK");
			input = new LineNumberReader(ir);
			String line = "";
			while((line=input.readLine())!=null) {
				//System.out.println(line);
				//strCommand += "\n"+line; 
				//推送消息到rabbitmq中
				this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n"+line);
			}
			//System.out.println("111111"+strCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strCommand;
	}

	//编译 linux 
	private String linux(String filePath,String fileName) {
		//通过filePath 截取流程名称 用来创建父级目录
		String ProcessName = filePath.substring(0,filePath.lastIndexOf("APP"));
		ProcessName = ProcessName.substring(ProcessName.lastIndexOf(File.separator)+1,ProcessName.length());
		System.out.println("流程名称===="+ProcessName);
		//手动创建目录
		//执行linux命令编译 项目
		try {
			Connection connection = new Connection(ip);
			connection.connect();
			connection.authenticateWithPassword(username, password);
			Session session = connection.openSession();
			
			//先创建流程文件夹
			session.execCommand("mkdir "+lPath+"/"+ProcessName);
			
			//再创建工程文件夹
			session = connection.openSession();
			session.execCommand("mkdir "+lPath+"/"+ProcessName+"/"+fileName);
			if(session != null) {
				session.close();
			}
			session.close();
		}catch(Exception e) {	
			e.getStackTrace();
		}



		//将Windows 项目文件上传到linux服务器中
		try {
			SftpUtil.uploadFilesToServer(filePath, lPath+"/"+ProcessName+"/"+fileName,ip,username,password, new SftpProgressMonitor() {

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


		//执行linux命令编译 项目
		//makefile文件夹路径
		String makeFilePath = "";
		try {
			Connection connection = new Connection(ip);
			connection.connect();
			connection.authenticateWithPassword(username, password);
			Session session = connection.openSession();
			
			//先执行查找makefile命令
			session.execCommand("find "+lPath+"/"+ProcessName+"/"+fileName+" -name makefile");
			
			//获取输出结果 为 makefile 的绝对路径
			InputStream is = new StreamGobbler(session.getStdout());
			BufferedReader brs = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
			String temp = "";
			while ((temp = brs.readLine()) != null) {
				makeFilePath = temp.substring(0,temp.lastIndexOf("/"));
			}

			//找到路径后  执行make命令
			session = connection.openSession();
			session.execCommand("cd "+makeFilePath+" ; make clean ; make");

			//成功返回
			is = new StreamGobbler(session.getStdout());
			brs = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
			while ((temp = brs.readLine()) != null) {
				System.out.println(temp);
				
				//推送消息到rabbitmq中
				this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n"+temp);
			}

			//失败返回
			is = new StreamGobbler(session.getStderr());
			brs = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
			while ((temp = brs.readLine()) != null) {
				System.out.println(temp);
				
				//推送消息到rabbitmq中
				this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n"+temp);
			}

			//执行打包命令
			session = connection.openSession();
			session.execCommand("cd "+makeFilePath+" ; zip -r "+fileName+".zip "+"*");
			
			//成功返回
			is = new StreamGobbler(session.getStdout());
			brs = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
			while ((temp = brs.readLine()) != null) {
				System.out.println(temp);
				
				//推送消息到rabbitmq中
				this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n"+temp);
			}

			if(session != null) {
				session.close();
			}
			session.close();
			brs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//将连接状态设置成null
		ChannelSftpSingleton.channelSftpNull();
		
		//下载压缩包
		ChannelSftp chSftp = null;
		try {
			chSftp = ChannelSftpSingleton.getInstance().getChannelSftp(ip,username,password);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		try {
			SftpUtil.download(makeFilePath+"/"+fileName+".zip",makeFilePath+"/"+fileName+".zip",dPath,chSftp);
			//todo,需取得一个下载成功或失败的标志
			//推送消息到rabbitmq中
			this.rabbitmqTemplate.convertAndSend("gjkmq" , fileName+"===@@@===\n下载完毕");
			
			//解压到工程Debug目录下
			try {
				unZipFiles(dPath+"\\"+fileName+".zip",filePath+"\\Debug");
				//todo ,解压成功或失败的判断以及处理
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//解压完删除压缩包
			File rmFile = new File(dPath+"\\"+fileName+".zip");
			rmFile.delete();
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
		//将连接状态设置成null
		ChannelSftpSingleton.channelSftpNull();
		
		//执行完毕 删除 linux数据
    	try {
	    	Connection connection = new Connection(ip);
			connection.connect();
			connection.authenticateWithPassword(username, password);
			Session session = connection.openSession();
			
			//执行删除文件夹命令
			session.execCommand("rm -rf "+lPath+"/"+ProcessName);
			if(session != null) {
				session.close();
			}
			session.close();
		}catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipPath 压缩文件地址
	 * @param descDir 指定目录
	 * @throws IOException
	 */
	private static void unZipFiles(String zipPath, String descDir) throws IOException {
		try {
			File zipFile = new File(zipPath);
			if (!zipFile.exists()) {
				throw new IOException("需解压文件不存在.");
			}
			File pathFile = new File(descDir);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			ZipFile zip = new ZipFile(zipFile);
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
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
				InputStream in = zip.getInputStream(entry);
				// 输出文件路径信息
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			zip.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
}
