package com.inforbus.gjk.pro.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.inforbus.gjk.common.core.util.FileUtil;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SylixosUtil {
	/*
	 * 修改 sylixos文件
	 * @param sylixosPath 为  文件 根目录  项目根目录
	 * @param historyName 项目以前名称
	 * 后续 可能会 把716 改成 活得
	 */
	public boolean updateSylixos(String sylixosPath,String historyName) {
		//修改sylixos相关文件
		//遍历sylixos文件夹
		File sylixosFile = new File(sylixosPath);
		File[] childFiles = sylixosFile.listFiles();
		for (File childFile : childFiles) {
			//判断是否是.project
			if (childFile.getName().endsWith(".project")) {
				replaceFileStr(childFile.getAbsolutePath(), historyName,sylixosFile.getName());
				//判断是否是.reproject
			}else if(childFile.getName().endsWith(".reproject")) {
				replaceFileStr(childFile.getAbsolutePath(), historyName,sylixosFile.getName());
				//判断是否是 Makefile
			}else if(childFile.getName().equals("Makefile")) {
				replaceFileStr(childFile.getAbsolutePath(), historyName,sylixosFile.getName());
				//判断是否是716.mk
			}else if(childFile.getName().equals(historyName+".mk")) {
				
				replaceFileStr(childFile.getAbsolutePath(), historyName,sylixosFile.getName());
				
				//修改 mk文件中 的.cpp .c 路径
				List<String> selectFileExtensionList = new ArrayList<String>();
				selectFileExtensionList.add(".c");
				selectFileExtensionList.add(".cpp");
				Set<String> setPaths = new HashSet<String>();
				FileUtil.getSelectStrFilePathList(setPaths,sylixosPath,selectFileExtensionList);
				List<String> listPaths = new ArrayList<String>();
				listPaths.addAll(setPaths);
				replaceFileNull(childFile.getAbsolutePath(),"LOCAL_SRCS",listPaths,null);
				
				//执行完清空listPaths setPaths
				listPaths.clear();
				setPaths.clear();
				
				//修改 mk文件中的 .h 路径
				FileUtil.getSelectStrFilePathList(setPaths,sylixosPath,".h");
				listPaths.addAll(setPaths);
				replaceFileNull(childFile.getAbsolutePath(),"LOCAL_INC_PATH",listPaths,sylixosFile.getName());
				
				//执行完清空listPaths setPaths
				listPaths.clear();
				setPaths.clear();
				
				//修改mk文件中的.a路径
				FileUtil.getSelectStrFilePathList(setPaths,sylixosPath,".a");
				listPaths.addAll(setPaths);
				replaceFileNull(childFile.getAbsolutePath(),"LOCAL_DEPEND_LIB",listPaths,sylixosFile.getName());
				replaceFileNull(childFile.getAbsolutePath(),"LOCAL_DEPEND_TARGET",listPaths,sylixosFile.getName());
				
				//执行完清空listPaths setPaths
				listPaths.clear();
				setPaths.clear();
				
				//修改.mk文件名为目标文件名
				childFile.renameTo(new File(sylixosFile.getAbsolutePath()+"\\"+sylixosFile.getName()+".mk"));
			}
		}
		return true;
	}
	/***
	 * 替换指定文件中的指定内容
	 * @param filepath 文件路径
	 * @param sourceStr 文件需要替换的内容
	 * @param targetStr 替换后的内容
	 * @return 替换成功返回true，否则返回false
	 */
	public static boolean replaceFileStr(String filepath,String sourceStr,String targetStr){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filepath),"GBK");
			BufferedReader fis = new BufferedReader(isr);// 创建文件输入流
			char[] data = new char[1024]; // 创建缓冲字符数组
			int rn = 0;
			StringBuilder sb=new StringBuilder(); // 创建字符串构建器
			//fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
			while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
				String str=String.valueOf(data,0,rn);//把数组转换成字符串
				sb.append(str);
			}
			fis.close();// 关闭输入流
			// 从构建器中生成字符串，并替换搜索文本
			String str = sb.toString().replace(sourceStr, targetStr);
			OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(filepath),"GBK");
			fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
			fout.close();// 关闭输出流

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/***
	 * 替换指定文件中的指定内容为空
	 * 目的清空已有数据放入新增数据
	 * @param filepath 文件路径
	 * @param sourceStr 文件需要替换的内容
	 * @param list .c .h .cpp集合
	 * @param softwareName 项目名称
	 * @return 替换成功返回true，否则返回false
	 */
	public static boolean replaceFileNull(String filepath,String sourceStr,List<String> list,String softwareName){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filepath),"GBK");
			BufferedReader fis = new BufferedReader(isr);// 创建文件输入流
			char[] data = new char[1024]; // 创建缓冲字符数组
			int rn = 0;
			StringBuilder sb=new StringBuilder(); // 创建字符串构建器
			//fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
			while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
				String str=String.valueOf(data,0,rn);//把数组转换成字符串
				sb.append(str);
			}
			fis.close();// 关闭输入流
			// 从构建器中生成字符串，并替换搜索文本
			//判断修改的参数名 修改不同内容
			if(sourceStr.equals("LOCAL_SRCS")) {
				String subStr = sb.toString().substring(sb.indexOf(sourceStr),sb.length());
				subStr = subStr.substring(0,subStr.indexOf("#*******************"));
				//System.out.println(subStr);
				String paths = "";
				for(int i=0;i<list.size();i++) {
					if(list.size()-i==1) {
						//由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
						paths += list.get(i).substring(list.get(i).indexOf("src"),list.get(i).length()).replace("\\", "/") + " \r\r";
					}else {
						paths += list.get(i).substring(list.get(i).indexOf("src"),list.get(i).length()).replace("\\", "/") + " \\ \r";
					}
				}
				String str = sb.toString().replace(subStr,sourceStr+" :=  \\ \r"+paths);
				OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(filepath),"GBK");
				fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
				fout.close();// 关闭输出流
			}else if(sourceStr.equals("LOCAL_INC_PATH")) {
				String subStr = sb.toString().substring(sb.indexOf(sourceStr+" :=  \\"),sb.length());
				subStr = subStr.substring(0,subStr.indexOf("#*******************"));
				//System.out.println(subStr);
				
				//此集合用来存储排重后的路径运用到 set集合查重机制
				Set<String> hSet = new HashSet<String>();
				String paths = "";
				
				//将加工后的 路径存到集合中
				for(int i=0;i<list.size();i++) {
					hSet.add("-I\"$(WORKSPACE_"+softwareName+")/"+list.get(i).substring(list.get(i).indexOf("src"),list.get(i).lastIndexOf("\\")).replace("\\", "/"));
				}
				//清空list 将 hSet添加到list中 方便后续执行
				list.clear();
				list.addAll(hSet);
				for(int i=0;i<list.size();i++) {
					//将加工后的 路径存到集合中
					if(hSet.size()-i==1) {
						//由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
						//.h文件路径 为文件夹路径  所以截取到最后一个 /
						paths += list.get(i) + "\" \r\r";
					}else {
						paths += list.get(i) + "\" \\ \r";
					}
				}
				String str = sb.toString().replace(subStr,sourceStr+" :=  \\ \r"+paths);
				OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(filepath),"GBK");
				fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
				fout.close();// 关闭输出流
			}else if(sourceStr.equals("LOCAL_DEPEND_LIB")) {
				String subStr = sb.toString().substring(sb.indexOf(sourceStr+" :=  \\"),sb.length());
				subStr = subStr.substring(0,subStr.indexOf("#*******************"));
				//System.out.println(subStr);
				String paths = "";
				
				//此for循环 负责取 *.a文件名称
				for(int i=0;i<list.size();i++) {
					if(list.size()-i==1) {
						//由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
						//.a文件路径 为文件夹路径  所以截取到最后一个 /
						paths += "-l:"+list.get(i).substring(list.get(i).lastIndexOf("\\")+1,list.get(i).length()) + " \r";
					}else {
						paths += "-l:"+list.get(i).substring(list.get(i).lastIndexOf("\\")+1,list.get(i).length()) + " \\ \r";
					}
				}
				paths += "LOCAL_DEPEND_LIB_PATH :=  \\ \r";
				for(int i=0;i<list.size();i++) {
					if(list.size()-i==1) {
						//由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
						//.a文件路径 为文件夹路径  所以截取到最后一个 /
						paths += "-L\"$(WORKSPACE_"+softwareName+")/"+list.get(i).substring(list.get(i).indexOf("src"),list.get(i).lastIndexOf("\\")).replace("\\", "/") + "\" \r\r";
					}else {
						paths += "-L\"$(WORKSPACE_"+softwareName+")/"+list.get(i).substring(list.get(i).indexOf("src"),list.get(i).lastIndexOf("\\")).replace("\\", "/") + "\" \\ \r";
					}
				}
				String str = sb.toString().replace(subStr,sourceStr+" :=  \\ \r"+paths);
				OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(filepath),"GBK");
				fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
				fout.close();// 关闭输出流
			}else if(sourceStr.equals("LOCAL_DEPEND_TARGET")) {
				String subStr = sb.toString().substring(sb.indexOf(sourceStr+" :=  \\"),sb.length());
				subStr = subStr.substring(0,subStr.indexOf("include $(APPLICATION_MK)"));
				//System.out.println(subStr);
				String paths = "";
				
				//此for循环 负责取 *.a文件名称路径
				for(int i=0;i<list.size();i++) {
					if(list.size()-i==1) {
						//由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
						//.a文件路径 为文件夹路径  所以截取到最后一个 /
						paths += "$(WORKSPACE_"+softwareName+")/"+list.get(i).substring(list.get(i).indexOf("src"),list.get(i).length()).replace("\\", "/") + " \r\r";
					}else {
						paths += "$(WORKSPACE_"+softwareName+")/"+list.get(i).substring(list.get(i).indexOf("src"),list.get(i).length()).replace("\\", "/") + " \\ \r";
					}
				}
				String str = sb.toString().replace(subStr,sourceStr+" :=  \\ \r"+paths);
				OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(filepath),"GBK");
				fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
				fout.close();// 关闭输出流
			}
			

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		updateSylixos("D:\\doudou\\compile\\compile\\syliox\\716_tq","716");
	}
}
