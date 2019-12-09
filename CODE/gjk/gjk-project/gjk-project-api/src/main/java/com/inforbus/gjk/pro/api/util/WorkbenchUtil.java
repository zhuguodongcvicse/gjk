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
import org.apache.tools.ant.taskdefs.EchoXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.x509.CRLNumberExtension;
import sun.util.resources.es.CurrencyNames_es_UY;

@UtilityClass
public class WorkbenchUtil {
    private File makeFile;
    private static final Logger logger = LoggerFactory.getLogger(WorkbenchUtil.class);

    /*
     * 修改 workbench文件
     * @param workbenchPath 为  文件 根目录  项目根目录
     */
    public boolean updateWorkbench(String workbenchPath) {
        File workbenchFile = new File(workbenchPath);
        //查找Makefile文件
        getFile(workbenchPath, "Makefile");
        if (makeFile != null) {
            //修改 WS_ROOT_DIR 的值
            replaceFileNull(makeFile.getAbsolutePath(), "WS_ROOT_DIR", null, workbenchFile.getParentFile().getAbsolutePath());

            //修改 PRJ_ROOT_DIR 的值
            replaceFileNull(makeFile.getAbsolutePath(), "PRJ_ROOT_DIR", null, workbenchFile.getName());

            //修改 IDE_LIBRARIES 的值
            Set<String> setPaths = new HashSet<String>();
            FileUtil.getSelectStrFilePathList(setPaths, workbenchPath, ".a");
            List<String> listPaths = new ArrayList<String>();
            listPaths.addAll(setPaths);
            replaceFileNull(makeFile.getAbsolutePath(), "IDE_LIBRARIES", listPaths, workbenchFile.getName());

            //清空list set
            setPaths.clear();
            listPaths.clear();

            //修改.c 转换.o
            List<String> selectFileExtensionList = new ArrayList<String>();
            selectFileExtensionList.add(".c");
            selectFileExtensionList.add(".cpp");
            FileUtil.getSelectStrFilePathList(setPaths, workbenchPath, selectFileExtensionList);
            listPaths.addAll(setPaths);
            replaceFileNull(makeFile.getAbsolutePath(), "OBJ_DIR", listPaths, workbenchFile.getName());

            //添加.o路径
            replaceFileNull(makeFile.getAbsolutePath(), "OBJECTS_srFrame_Mul_partialImage", listPaths, workbenchFile.getName());

            //清空list set
            setPaths.clear();
            listPaths.clear();

            //添加.d路径
            //此处按照文档格式走的
            FileUtil.getSelectStrFilePathList(setPaths, workbenchPath, ".c");
            listPaths.addAll(setPaths);
            replaceFileNull(makeFile.getAbsolutePath(), "DEP_FILES", listPaths, workbenchFile.getName());

            //清空list set
            setPaths.clear();
            listPaths.clear();
            return true;
        }
        return false;
    }

    /***
     * 替换指定文件中的指定内容为空
     * 目的清空已有数据放入新增数据
     * @param filepath 文件路径
     * @param sourceStr 文件需要替换的内容
     * @param list 集合
     * @param other 其他参数
     * @return 替换成功返回true，否则返回false
     */
    public static boolean replaceFileNull(String filepath, String sourceStr, List<String> list, String other) {
        InputStreamReader isr = null;
        BufferedReader fis = null;
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
            if (fis != null)
                fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            //判断修改的参数名 修改不同内容
            if (sourceStr.equals("WS_ROOT_DIR")) {
                String subStr = sb.toString().substring(sb.indexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("PRJ_ROOT_DIR :="));
                //System.out.println(subStr);
                String str = sb.toString().replace(subStr, sourceStr + " := " + other.replace("\\", "/") + "\r");
                OutputStreamWriter fout = null;
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                    fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                } catch (Exception e) {
                    logger.error("修改makefile文件中WS_ROOT_DIR部分失败");
                } finally {
                    if (fout != null) {
                        fout.close();// 关闭输出流
                    }
                }
            } else if (sourceStr.equals("PRJ_ROOT_DIR")) {
                String subStr = sb.toString().substring(sb.indexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("#Global"));
                //System.out.println(subStr);
                String str = sb.toString().replace(subStr, sourceStr + " := $(WS_ROOT_DIR)/" + other + "\r\r\r\r");
                OutputStreamWriter fout = null;
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                    fout.write(str.toCharArray());// 把替换完成的字符串写入文件内

                } catch (Exception e) {
                    logger.error("修改makefile文件中PRJ_ROOT_DIR部分失败");
                } finally {
                    if (fout != null) {
                        fout.close();// 关闭输出流
                    }
                }
            } else if (sourceStr.equals("IDE_LIBRARIES")) {
                //找第一个IDE_LIBRARIES
                String subStr = sb.toString().substring(sb.indexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("IDE_DEFINES = "));

                //找到第二个IDE_LIBRARIES
                String subStr2 = sb.toString().substring(sb.indexOf("srFrame_Mul/$(MODE_DIR)/% : " + sourceStr), sb.length());
                subStr2 = subStr2.substring(0, subStr2.indexOf("srFrame_Mul/$(MODE_DIR)/% : IDE_DEFINES = "));
                //找到第三个IDE_LIBRARIES
                String subStr3 = sb.toString().substring(sb.indexOf("srFrame_Mul_partialImage/$(MODE_DIR)/% : " + sourceStr), sb.length());
                subStr3 = subStr3.substring(0, subStr3.indexOf("srFrame_Mul_partialImage/$(MODE_DIR)/% : IDE_DEFINES = "));

                //遍历加工.a路径
                String paths = "";
                for (String path : list) {
                    paths += "$(PRJ_ROOT_DIR)" + path.substring(path.indexOf("\\"+other+"\\") + other.length(), path.length()).replace("\\", "/") + " ";
                }
                //重新组装数据 写入文件中
                String str = sb.toString()
                        .replace(subStr, sourceStr + " = " + paths + "\r\r")
                        .replace(subStr2, "srFrame_Mul/$(MODE_DIR)/% : " + sourceStr + " = " + paths + "\r")
                        .replace(subStr3, "srFrame_Mul_partialImage/$(MODE_DIR)/% : " + sourceStr + " = " + paths + "\r");
                OutputStreamWriter fout = null;
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                    fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                } catch (Exception e) {
                    logger.error("修改makefile文件中IDE_LIBRARIES部分失败");
                } finally {
                    if (fout != null) {
                        fout.close();// 关闭输出流
                    }
                }
            } else if (sourceStr.equals("OBJ_DIR")) {
                //找最后一个OBJ_DIR
                String subStr = sb.toString().substring(sb.lastIndexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("OBJECTS_srFrame_Mul_partialImage"));
                //加工.c/.cpp路径
                String paths = "OBJ_DIR := srFrame_Mul_partialImage/$(MODE_DIR) \r\r";
                for (String path : list) {
                    //paths += "srFrame_Mul_partialImage/$(MODE_DIR)/Objects/srFrame_Mul" + path.substring(path.indexOf(other) + other.length(), path.lastIndexOf(".")).replace("\\", "/") + ".o";
                    paths += "srFrame_Mul_partialImage/$(MODE_DIR)/Objects" + path.substring(path.indexOf("\\" + other + "\\"), path.lastIndexOf(".")).replace("\\", "/") + ".o";
                    paths += " : $(PRJ_ROOT_DIR)" + path.substring(path.indexOf("\\"+other+"\\") + other.length(), path.length()).replace("\\", "/") + " $(FORCE_FILE_BUILD) \r";
                    paths += "	$(TRACE_FLAG)if [ ! -d \"`dirname \"$@\"`\" ]; then mkdir -p \"`dirname \"$@\"`\"; fi;echo \"building $@\"; $(TOOL_PATH)dcc $(DEBUGFLAGS_C-Compiler) $(CC_ARCH_SPEC) -W:c:,-Xclib-optim-off -Xansi -Xlocal-data-area-static-only -Xforce-declarations   -Xmake-dependency=0xd $(ADDED_CFLAGS) $(IDE_INCLUDES) $(ADDED_INCLUDES) -DCPU=$(CPU) -DTOOL_FAMILY=$(TOOL_FAMILY) -DTOOL=$(TOOL) -D_WRS_KERNEL -D_WRS_VX_SMP -D_WRS_CONFIG_SMP -D_VSB_CONFIG_FILE=\\\"$(VSB_CONFIG_FILE)\\\" -D_WRS_MIPS_N32_ABI -DMIPSEL  $(DEFINES) -o \"$@\" -c \"$<\"";
                    paths += "\r\r\r";
                }

                String str = sb.toString().replace(subStr, paths);
                OutputStreamWriter fout = null;
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                    fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                } catch (Exception e) {
                    logger.error("修改makefile文件中OBJ_DIR部分失败");
                } finally {
                    if (fout != null) {
                        fout.close();// 关闭输出流
                    }
                }

            } else if (sourceStr.equals("OBJECTS_srFrame_Mul_partialImage")) {
                //找第一个OBJECTS_srFrame_Mul_partialImage
                String subStr = sb.toString().substring(sb.indexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("ifeq"));
                //加工.o路径
                String paths = "OBJECTS_srFrame_Mul_partialImage = ";
                for (int i = 0; i < list.size(); i++) {
                    if (list.size() - i == 1) {
                        //.o文件路径
                        paths += "	srFrame_Mul_partialImage/$(MODE_DIR)/Objects" + list.get(i).substring(list.get(i).indexOf("\\" + other + "\\"), list.get(i).lastIndexOf(".")).replace("\\", "/") + ".o  \r\r";
                    } else if (i == 0) {
                        paths += "srFrame_Mul_partialImage/$(MODE_DIR)/Objects" + list.get(i).substring(list.get(i).indexOf("\\" + other + "\\"), list.get(i).lastIndexOf(".")).replace("\\", "/") + ".o \\ \r";
                    } else {
                        paths += "	srFrame_Mul_partialImage/$(MODE_DIR)/Objects" + list.get(i).substring(list.get(i).indexOf("\\" + other + "\\"), list.get(i).lastIndexOf(".")).replace("\\", "/") + ".o \\ \r";
                    }
                }

                String str = sb.toString().replace(subStr, paths);
                OutputStreamWriter fout = null;
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                    fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                } catch (Exception e) {
                    logger.error("修改makefile文件中OBJECTS_srFrame_Mul_partialImage部分失败");
                } finally {
                    if (fout != null) {
                        fout.close();// 关闭输出流
                    }
                }
            } else if (sourceStr.equals("DEP_FILES")) {
                //此处按照文档格式走的
                //找第一个DEP_FILES
                String subStr = sb.toString().substring(sb.indexOf(sourceStr), sb.length());
                subStr = subStr.substring(0, subStr.indexOf("-include"));
                //加工.d路径
                String paths = "DEP_FILES := ";
                //判断list是否为空
                if (list.size() == 0) {
                    paths += "\r";
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            //paths += "srFrame_Mul_partialImage/$(MODE_DIR)/Objects/srFrame_Mul"+list.get(i).substring(list.get(i).indexOf(other)+other.length(),list.get(i).length()).replace("\\", "/")+"  \r";
                            String str = list.get(i).substring(list.get(i).indexOf("\\" + other + "\\"), list.get(i).length()).replace("\\", "/");
                            str = str.replace(".c", ".d");
                            paths += "srFrame_Mul_partialImage/$(MODE_DIR)/Objects" + str + "\r";

                        } else {
                            //paths += "	srFrame_Mul_partialImage/$(MODE_DIR)/Objects/srFrame_Mul"+list.get(i).substring(list.get(i).indexOf(other)+other.length(),list.get(i).length()).replace("\\", "/")+"  \r";
                            String str = list.get(i).substring(list.get(i).indexOf("\\" + other + "\\"), list.get(i).length()).replace("\\", "/");
                            str = str.replace(".c", ".d");
                            paths += "srFrame_Mul_partialImage/$(MODE_DIR)/Objects" + str + " \\ \r";
                        }
                    }
                }

                String str = sb.toString().replace(subStr, paths);
                OutputStreamWriter fout = null;
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(filepath), "GBK");
                    fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
                } catch (Exception e) {
                    logger.error("修改makefile文件中DEP_FILES部分失败");
                } finally {
                    if (fout != null) {
                        fout.close();// 关闭输出流
                    }
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    logger.error("IO流关闭失败");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("IO流关闭失败");
                }
            }
        }
    }

    /**
     * 查找文件
     *
     * @param strPath  文件夹路径
     * @param fileName 查找的文件
     * @return
     */
    public static void getFile(String strPath, String fileName) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                //是文件夹的话就是要递归再深入查找文件
                if (files[i].isDirectory()) {
                    //判断是文件夹还是文件
                    getFile(files[i].getAbsolutePath(), fileName);
                } else {
                    //判断是否是Makefile文件
                    if (files[i].getName().equals(fileName)) {
                        makeFile = files[i];
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        updateWorkbench("D:\\14S_GJK_GIT\\gjk\\gjk\\project\\测试workbench生成makefile文件项目\\测试1115\\admin_测试workbench生成makefile文件项目_测试1115APP\\wb1");
    }
}
