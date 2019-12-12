package com.inforbus.gjk.pro.api.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.inforbus.gjk.common.core.util.FileUtil;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinuxUtil {
    /*
     * 修改 Linux文件
     * @param linuxPath 为  文件 根目录  项目根目录
     */
    public boolean updateLinux(List<String> list, String linuxPath, String type) {
        //查找.mk文件
        Set<String> setPaths = new HashSet<String>();
        FileUtil.getSelectStrFilePathList(setPaths, linuxPath, ".mk");
        List<String> listPaths = new ArrayList<String>();
        listPaths.addAll(setPaths);
        for (String path : listPaths) {
            if (path.indexOf("App\\Src\\Spb\\subdir.mk") > -1 && type.equals(".c")) {
                if (list != null) {
                    replaceFileC(path, list);
                }
            }
        }
        return true;
    }

    //修改.c
    public static boolean replaceFileC(String path, List<String> listPath) {
        InputStreamReader isr = null;
        BufferedReader fis = null;
        try {
            isr = new InputStreamReader(new FileInputStream(path), "GBK");
            fis = new BufferedReader(isr);// 创建文件输入流
            char[] data = new char[1024]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            //fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);//把数组转换成字符串
                sb.append(str);
            }
            if (fis != null)
                fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            //截取C_SRCS
            String subStr = sb.toString().substring(sb.indexOf("C_SRCS += \\"), sb.length());
            String replaceSubStr = subStr.substring(0, subStr.indexOf("OBJS += \\"));
            //subStr = subStr.substring(0,subStr.lastIndexOf(".c")+2);
            if (replaceSubStr.contains("\n")) {
                subStr = replaceSubStr.substring(0, replaceSubStr.indexOf("\n"));
            }
            System.out.println(subStr);
            //加工后续
            String subStr1 = subStr;
            subStr1 += "\r";
            for (int i = 0; i < listPath.size(); i++) {
                if (i == listPath.size() - 1) {
                    //截取获取文件名称
                    subStr1 += "../App/Src/Spb/" + listPath.get(i).substring(listPath.get(i).lastIndexOf("\\") + 1, listPath.get(i).length()) + " \r";
                } else {
                    //截取获取文件名称
                    subStr1 += "../App/Src/Spb/" + listPath.get(i).substring(listPath.get(i).lastIndexOf("\\") + 1, listPath.get(i).length()) + " \\ \r";
                }
            }


            //截取OBJS
            String subOBJS = sb.toString().substring(sb.indexOf("OBJS += \\"), sb.length());
            String replaceSubOBJS = subOBJS.substring(0, subOBJS.indexOf("C_DEPS += \\"));
            //subOBJS = subOBJS.substring(0,subOBJS.lastIndexOf(".o")+2);
            if (replaceSubOBJS.contains("\n")) {
                subOBJS = replaceSubOBJS.substring(0, replaceSubOBJS.indexOf("\n"));
            }
            System.out.println(subOBJS);
            //加工后续
            String subOBJS1 = subOBJS;
            subOBJS1 += "\r";
            for (int i = 0; i < listPath.size(); i++) {
                if (i == listPath.size() - 1) {
                    //截取获取文件名称
                    subOBJS1 += "./App/Src/Spb/" + listPath.get(i).substring(listPath.get(i).lastIndexOf("\\") + 1, listPath.get(i).length() - 2) + ".o \r";
                } else {
                    //截取获取文件名称
                    subOBJS1 += "./App/Src/Spb/" + listPath.get(i).substring(listPath.get(i).lastIndexOf("\\") + 1, listPath.get(i).length() - 2) + ".o \\ \r";
                }
            }


            //截取C_DEPS
            String subC_DEPS = sb.toString().substring(sb.indexOf("C_DEPS += \\"), sb.length());
            String replaceSubC_DEPS = subC_DEPS.substring(0, subC_DEPS.indexOf("# Each subdirectory"));
            //subC_DEPS = subC_DEPS.substring(0,subC_DEPS.lastIndexOf(".d")+2);
            if (replaceSubC_DEPS.contains("\n")) {
                subC_DEPS = replaceSubC_DEPS.substring(0, replaceSubC_DEPS.indexOf("\n"));
            }
            System.out.println(subC_DEPS);
            //加工后续
            String subC_DEPS1 = subC_DEPS;
            subC_DEPS1 += "\r";
            for (int i = 0; i < listPath.size(); i++) {
                if (i == listPath.size() - 1) {
                    //截取获取文件名称
                    subC_DEPS1 += "./App/Src/Spb/" + listPath.get(i).substring(listPath.get(i).lastIndexOf("\\") + 1, listPath.get(i).length() - 2) + ".d \r";
                } else {
                    //截取获取文件名称
                    subC_DEPS1 += "./App/Src/Spb/" + listPath.get(i).substring(listPath.get(i).lastIndexOf("\\") + 1, listPath.get(i).length() - 2) + ".d \\ \r";
                }
            }

            String str = sb.toString().replace(replaceSubStr, subStr1 + "\r");
            str = str.replace(replaceSubOBJS, subOBJS1 + "\r");
            str = str.replace(replaceSubC_DEPS, subC_DEPS1 + "\r");
            OutputStreamWriter fout = null;
            try {
                fout = new OutputStreamWriter(new FileOutputStream(path), "GBK");
                fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fout != null)
                    fout.close();// 关闭输出流
            }
            return true;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("D:\\asd\\asd\\asd\\123.c");
        list.add("D:\\asd\\asd\\asd\\234.c");
        list.add("D:\\asd\\asd\\asd\\345.c");
        list.add("D:\\asd\\asd\\asd\\456.c");
        updateLinux(list, "D:\\doudou\\linux\\srFrame_Mul_Linux\\srFrame_Mul", ".c");
    }
}
