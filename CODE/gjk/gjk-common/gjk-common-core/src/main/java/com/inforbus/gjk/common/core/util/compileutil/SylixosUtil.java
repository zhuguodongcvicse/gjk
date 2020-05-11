package com.inforbus.gjk.common.core.util.compileutil;

import com.inforbus.gjk.common.core.util.FileUtil;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.*;

/**
 * SylixosUtil
 *
 * @author wang
 * @date 2020/5/8
 * @Description sylixos平台makefile文件修改工具类
 */
@UtilityClass
public class SylixosUtil {

    /**
     * @Author wang
     * @Description: 修改 sylixos文件,.cproject文件,.reproject.project文件都不需要更改,只修改config.mk文件,makefile文件,716.mk文件
     * @Param: [sylixosPath, bspFilePath, historyName],sylixosPath 为 文件 根目录 项目根目录,historyName 项目以前名称 后续 可能会 把716 改成 活得 2019年11月27日 14:43:24 更改
     * @Return: boolean
     * @Create: 2020/5/8
     */
	public boolean updateSylixos(String sylixosPath, String bspFilePath, String historyName) {
		// 修改sylixos相关文件
		// 遍历sylixos文件夹
		File sylixosFile = new File(sylixosPath);
		File[] childFiles = sylixosFile.listFiles();
		for (File childFile : childFiles) {
			if (childFile.getName().equals(historyName + ".mk")) {// 修改historyName.mk文件
				List<String> selectFileExtensionList = new ArrayList<String>();
				selectFileExtensionList.add(".c");
				Set<String> setPaths = new HashSet<String>();
				FileUtil.getSelectStrFilePathList(setPaths, sylixosPath, selectFileExtensionList);// 查找.c文件
				List<String> listPaths = new ArrayList<String>();
				listPaths.addAll(setPaths);
				replaceFileNull(childFile.getAbsolutePath(), "LOCAL_SRCS", listPaths, null,childFile.getParentFile().getName());// 修改文件中.c路径

			} else if (childFile.getName().equals("config.mk")) {
				String parent = sylixosFile.getParent();
                HashMap<String, String> map = new HashMap<>();
                findBaseAndBspFile(bspFilePath, "SYLIXOS_BASE_PATH.xml",map);// 获取到依赖工程base与bsphr2的绝对路径
				findBaseAndBspFile(bspFilePath, "SYLIXOS_BSP_HR2_PATH.xml",map);// 获取到依赖工程base与bsphr2的绝对路径
				modifyConfigMk(childFile.getAbsolutePath(),map);// 修改config.mk文件
			} else if (childFile.getName().equals("Makefile")) {
				// 2019年11月27日 18:45:14 暂时不需要更改
				// replaceFileStr(childFile.getAbsolutePath(), historyName,
				// sylixosFile.getName());
			}
		}
		return true;
	}

    /**
     * @Author wang
     * @Description: 替换指定文件中的指定内容
     * @Param: [filepath, sourceStr, targetStr],filepath 文件路径,sourceStr 文件需要替换的内容,targetStr 替换后的内容
     * @Return: boolean
     * @Create: 2020/5/8
     */
    public static boolean replaceFileStr(String filepath, String sourceStr, String targetStr) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filepath), "GBK");
            BufferedReader fis = new BufferedReader(isr);// 创建文件输入流
            char[] data = new char[1024]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            //fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);//把数组转换成字符串
                sb.append(str);
            }
            fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            String str = sb.toString().replace(sourceStr, targetStr);
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
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
    public static boolean replaceFileNull(String filepath, String sourceStr, List<String> list, String softwareName, String projectName) {
        InputStreamReader isr = null;
        BufferedReader fis = null;
        OutputStreamWriter fout = null;
        try {
            isr = new InputStreamReader(new FileInputStream(filepath), "GBK");
            fis = new BufferedReader(isr);// 创建文件输入流
            char[] data = new char[1024]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            //fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);//把数组转换成字符串
                sb.append(str);
            }
            if (fis != null){
                fis.close();// 关闭输入流
            }
            // 从构建器中生成字符串，并替换搜索文本
            //判断修改的参数名 修改不同内容
            if (sourceStr.equals("LOCAL_SRCS")) {
                String subStr = sb.toString().substring(sb.indexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("#*******************"));
                //System.out.println(subStr);
                String paths = "";
                for (int i = 0; i < list.size(); i++) {
                    int index = list.get(i).indexOf("\\" + projectName + "\\");
                    int length = projectName.length() + 2;
                    if (list.size() - i == 1) {
                        //由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
                        paths += list.get(i).substring(index + length, list.get(i).length()).replace("\\", "/") + " \r\r";
                    } else {
                        paths += list.get(i).substring(index + length, list.get(i).length()).replace("\\", "/") + " \\ \r";
                    }
                }
                String str = sb.toString().replace(subStr, sourceStr + " :=  \\ \r" + paths);
                fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                if (fout != null){
                    fout.close();// 关闭输出流
                }
            } else if (sourceStr.equals("LOCAL_INC_PATH")) {
                String subStr = sb.toString().substring(sb.indexOf(sourceStr + " :=  \\"), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("#*******************"));
                //此集合用来存储排重后的路径运用到 set集合查重机制
                Set<String> hSet = new HashSet<String>();
                String paths = "";
                //将加工后的 路径存到集合中
                for (int i = 0; i < list.size(); i++) {
                    hSet.add("-I\"$(WORKSPACE_" + softwareName + ")/" + list.get(i).substring(list.get(i).indexOf("src"), list.get(i).lastIndexOf("\\")).replace("\\", "/"));
                }
                //清空list 将 hSet添加到list中 方便后续执行
                list.clear();
                list.addAll(hSet);
                for (int i = 0; i < list.size(); i++) {
                    //将加工后的 路径存到集合中
                    if (hSet.size() - i == 1) {
                        //由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
                        //.h文件路径 为文件夹路径  所以截取到最后一个 /
                        paths += list.get(i) + "\" \r\r";
                    } else {
                        paths += list.get(i) + "\" \\ \r";
                    }
                }
                String str = sb.toString().replace(subStr, sourceStr + " :=  \\ \r" + paths);
                fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                if (fout != null){
                    fout.close();// 关闭输出流
                }
            } else if (sourceStr.equals("LOCAL_DEPEND_LIB")) {
                String subStr = sb.toString().substring(sb.indexOf(sourceStr + " :=  \\"), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("#*******************"));
                //System.out.println(subStr);
                String paths = "";
                //此for循环 负责取 *.a文件名称
                for (int i = 0; i < list.size(); i++) {
                    if (list.size() - i == 1) {
                        //由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
                        //.a文件路径 为文件夹路径  所以截取到最后一个 /
                        paths += "-l:" + list.get(i).substring(list.get(i).lastIndexOf("\\") + 1, list.get(i).length()) + " \r";
                    } else {
                        paths += "-l:" + list.get(i).substring(list.get(i).lastIndexOf("\\") + 1, list.get(i).length()) + " \\ \r";
                    }
                }
                paths += "LOCAL_DEPEND_LIB_PATH :=  \\ \r";
                for (int i = 0; i < list.size(); i++) {
                    if (list.size() - i == 1) {
                        //由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
                        //.a文件路径 为文件夹路径  所以截取到最后一个 /
                        paths += "-L\"$(WORKSPACE_" + softwareName + ")/" + list.get(i).substring(list.get(i).indexOf("src"), list.get(i).lastIndexOf("\\")).replace("\\", "/") + "\" \r\r";
                    } else {
                        paths += "-L\"$(WORKSPACE_" + softwareName + ")/" + list.get(i).substring(list.get(i).indexOf("src"), list.get(i).lastIndexOf("\\")).replace("\\", "/") + "\" \\ \r";
                    }
                }
                String str = sb.toString().replace(subStr, sourceStr + " :=  \\ \r" + paths);
                fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                if (fout != null){
                    fout.close();// 关闭输出流
                }
            } else if (sourceStr.equals("LOCAL_DEPEND_TARGET")) {
                String subStr = sb.toString().substring(sb.indexOf(sourceStr + " :=  \\"), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("include $(APPLICATION_MK)"));
                String paths = "";
                //此for循环 负责取 *.a文件名称路径
                for (int i = 0; i < list.size(); i++) {
                    if (list.size() - i == 1) {
                        //由于list内容为绝对路径 则进行截取 目前是写死的 src路径 后续 看情况修改
                        //.a文件路径 为文件夹路径  所以截取到最后一个 /
                        paths += "$(WORKSPACE_" + softwareName + ")/" + list.get(i).substring(list.get(i).indexOf("src"), list.get(i).length()).replace("\\", "/") + " \r\r";
                    } else {
                        paths += "$(WORKSPACE_" + softwareName + ")/" + list.get(i).substring(list.get(i).indexOf("src"), list.get(i).length()).replace("\\", "/") + " \\ \r";
                    }
                }
                String str = sb.toString().replace(subStr, sourceStr + " :=  \\ \r" + paths);
                fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                if (fout != null){
                    fout.close();// 关闭输出流
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * @Author wang
     * @Description: 查找base与bsp工程下的特定xml文件,参数为bsp文件夹的绝对路径
     * @Param: [bspPath, fileName, map],参数1:查找文件的父路径,参数2:文件名,参数3:map
     * @Return: void
     * @Create: 2020/5/8
     */
    public void findBaseAndBspFile(String bspPath, String fileName,Map<String,String> map) {
        File bspFile = new File(bspPath);
        File[] files = bspFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.getName() != null && file.getName().equals(fileName)) {
                    map.put(fileName, file.getParentFile().getAbsolutePath());//找到文件的绝对路径,添加到map中
                }
                if (file.listFiles() != null && file.listFiles().length > 0) {
                    findBaseAndBspFile(file.getAbsolutePath(), fileName,map);
                }
            }
        }
    }

    /**
     * @Author wang
     * @Description: 修改.config.mk文件方法
     * @Param: [filepath, map]
     * @Return: boolean
     * @Create: 2020/5/8
     */
    public boolean modifyConfigMk(String filepath, Map<String, String> map) {

        InputStreamReader isr = null;
        BufferedReader fis = null;
        OutputStreamWriter fout = null;
        try {
            isr = new InputStreamReader(new FileInputStream(filepath), "GBK");
            fis = new BufferedReader(isr);// 创建文件输入流
            char[] data = new char[1024]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            //fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);//把数组转换成字符串
                sb.append(str);
            }
            if (fis != null){
                fis.close();// 关闭输入流
            }
            // 从构建器中生成字符串，并替换搜索文本
            //判断修改的参数名 修改不同内容
            String subStr = sb.toString().substring(sb.indexOf("SYLIXOS_BASE_PATH"), sb.length());//截取出子串
            subStr = subStr.substring(0, subStr.indexOf("#*******************"));
            String basePath = map.get("SYLIXOS_BASE_PATH.xml");
            String bsphr2Path = map.get("SYLIXOS_BSP_HR2_PATH.xml");
            String newStr = "SYLIXOS_BASE_PATH := " + basePath + "\r\n\r\n" + "SYLIXOS_BSP_HR2_PATH := " + bsphr2Path + "\r\n\r\n";//替换成新的内容
            String str = sb.toString().replace(subStr, newStr);//替换成新的内容
            fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
            fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
            if (fout != null){
                fout.close();// 关闭输出流
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();//关流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();//关流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fout != null) {
                try {
                    fout.close();//关流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
