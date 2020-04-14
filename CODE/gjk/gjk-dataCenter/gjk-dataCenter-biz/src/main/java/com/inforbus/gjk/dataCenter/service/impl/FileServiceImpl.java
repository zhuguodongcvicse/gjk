package com.inforbus.gjk.dataCenter.service.impl;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.constant.enums.FileExtensionEnum;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.dataCenter.api.entity.FileCenter;
import com.inforbus.gjk.dataCenter.service.FileService;

/**
 * @ClassName: FileServiceImpl
 * @Desc
 * @Author xiaohe
 * @DateTime 2020年4月7日
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Value("${git.local.path}")
    private String localBasePath;

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * @param localPath 保存的本地文件路劲
     * @param localFile 要保存的文件列表
     * @return
     * @throws Exception
     * @Title: uploadLocalFile
     * @Desc 多文件上传
     * @Author xiaohe
     * @DateTime 2020年4月1日
     * @see com.inforbus.gjk.dataCenter.service.FileService#uploadLocalFile(java.lang.String,
     * java.util.List)
     */
    @Override
    public Boolean uploadLocalFile(String localPath, List<FileCenter> localFile) throws Exception {
        if (localFile.isEmpty()) {
            return false;
        }
        // 创建文件
        UploadFilesUtils.createFile(localBasePath + File.separator + localPath);
        localFile.stream().forEach(fc -> {
            FileChannel in = null;
            FileChannel out = null;
            try {
                in = fc.getInputStream().getChannel();
                FileOutputStream fos = new FileOutputStream(
                        localBasePath + File.separator + localPath + File.separator + fc.getAbsolutePath());
                out = fos.getChannel();
                // 连接两个通道，并从in通道读取，写入out中
                in.transferTo(0, in.size(), out);
            } catch (FileNotFoundException e) {
                logger.debug("下载本地文件,{}", e);
            } catch (IOException e) {
                logger.debug("下载本地文件,{}", e);
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    logger.debug("下载本地文件,{}", e);
                }
            }
        });
        return true;
    }

    /**
     * @param sourcePath 文件/文件夹路径 相对路径
     * @param localPath  保存文件/文件夹根路径 相对路径
     * @param fileName   文件名字
     * @return
     * @throws Exception
     * @Title: downloadFile
     * @Desc 下载文件
     * @Author cvics
     * @DateTime 2020年4月1日
     * @see com.inforbus.gjk.dataCenter.service.FileService#downloadFile(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Boolean downloadFile(String sourcePath, String localPath, String fileName) throws Exception {
        File file = new File(localBasePath + File.separator + sourcePath);
        // 判断是不是目录
        try {
            if (file.isDirectory()) {
                FileUtil.copyFile(file.getPath(), localBasePath + File.separator + localPath);
                logger.info("下载文件夹 {} 中文件到 {} 中", sourcePath, localPath);
            } else {
                FileUtil.copyFile(file.getPath(), localBasePath + File.separator + localPath, fileName);
                logger.info("下载文件夹 {} 中文件到 {} 中 ；名字为{}", sourcePath, localPath);
            }
        } catch (IOException e) {
            logger.debug("下载文件夹,{}", e);
            return false;
        }
        return true;

    }

    /**
     * @param sourcePath 文件路径
     * @return
     * @throws Exception
     * @Title: delAllFile
     * @Description: 删除指定文件夹下的所有文件
     * @Author xiaohe
     * @DateTime 2020年4月1日
     * @see com.inforbus.gjk.dataCenter.service.FileService#delAllFile(java.lang.String)
     */
    @Override
    public Boolean delAllFile(String sourcePath) throws Exception {
        String allPath = localBasePath + File.separator + sourcePath;
        return UploadFilesUtils.delAllFile(allPath);
    }

    /**
     * @param folderPath 文件夹路径
     * @return
     * @throws Exception
     * @Title: delFolder
     * @Description: 删除文件夹
     * @Author xiaohe
     * @DateTime 2020年4月1日
     * @see com.inforbus.gjk.dataCenter.service.FileService#delFolder(java.lang.String)
     */
    @Override
    public Boolean delFolder(String folderPath) throws Exception {
        String allPath = localBasePath + File.separator + folderPath;
        //// 删除完里面所有内容
        if (delAllFile(allPath)) {
            String filePath = allPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            // 删除空文件夹
            return myFilePath.delete();
        } else {
            return false;
        }

    }

    /**
     *
     * @return
     * @throws Exception
     */
    /**
     * @param source 源文件路径
     * @param destin 拷贝文件路径
     * @throws Exception
     * @Title: copylocalFile
     * @Description: 拷贝文件
     * @Author xiaohe
     * @DateTime 2019年11月27日 下午8:46:20
     * @see com.inforbus.gjk.dataCenter.service.FileService#copylocalFile(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void copylocalFile(String source, String destin) throws Exception {
        UploadFilesUtils.NioToCopyFile(source, destin);
    }

    /**
     * @param localPath 文件路径
     * @return 字符编码和文件内容 <'charset',charset>,<textContext,'xxxxx'>
     * @throws Exception
     * @Title: readFile
     * @Desc 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
     * @Author xiaohe
     * @DateTime 2020年4月1日
     * @see com.inforbus.gjk.dataCenter.service.FileService#readFile(java.lang.String)
     */
    @Override
    public Map<String, String> readFile(String localPath) throws Exception {
        Map<String, String> maps = Maps.newHashMap();
        String filepath = localBasePath + File.separator + localPath;
        File file = UploadFilesUtils.createFile(filepath);
        String charset = getFilecharset(file);
        maps.put("charset", charset);
        StringBuilder readStr = new StringBuilder();
        // 获取文件后缀名
        String fileType = localPath.substring(localPath.lastIndexOf("."));
        // 根据文件后缀名判断
        FileExtensionEnum fileEnum = FileExtensionEnum.containsDocFileType(fileType);
        switch (fileEnum) {
            // WORD 文件
            case WORD_DOC:
                readStr.append(readWord(filepath));
                break;
            // EXCEL 文件
            case EXCEL_DOC:

                break;
            // txt 文件
            case TXT_DOC:
                readStr.append(readTxt(filepath, charset));
                break;
            // 默认时用txt 文件 的读取方式
            default:
                readStr.append(readTxt(filepath, charset));
                break;
        }
        maps.put("textContext", readStr.toString());
        return maps;
    }

    /**
     * @param localPath   文件路径
     * @param charset     编码格式
     * @param textContext 文件内容
     * @return
     * @throws Exception
     * @Title: writeFile
     * @Desc 编辑文件 更改文件内容
     * @Author cvics
     * @DateTime 2020年4月1日
     * @see com.inforbus.gjk.dataCenter.service.FileService#writeFile(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Boolean writeFile(String localPath, String charset, String textContext) {
        boolean boo = false;
        String filepath = localBasePath + File.separator + localPath;
        // 获取文件后缀名
        String fileType = filepath.substring(filepath.lastIndexOf("."));
        // 根据文件后缀名判断
        FileExtensionEnum fileEnum = FileExtensionEnum.containsDocFileType(fileType);
        switch (fileEnum) {
            // WORD 文件
            case WORD_DOC:
                break;
            // EXCEL 文件
            case EXCEL_DOC:

                break;
            // txt 文件
            case TXT_DOC:
                boo = writeTxt(filepath, textContext, charset);
                break;
            default:
                // 默认时用txt 文件 的读取方式
                boo = writeTxt(filepath, textContext, charset);
                break;
        }
        return boo;
    }

    /**
     * @Author wang
     * @Description: 解析xml文件为xmlEntity对象
     * @Param: [localPath]
     * @Return: boolean
     * @Create: 2020/4/13
     */
    @Override
    public XmlEntityMap analysisXmlFileToXMLEntityMap(String localPath) throws FileNotFoundException {
        File localFile = new File(localPath);
        if (!localFile.exists()) {
            //如果xml文件不存在,返回null
            logger.error(localFile.getName() + "文件不存在");
            throw new FileNotFoundException(localFile.getName() + "文件不存在");
        }
        //通过工具类按照路径解析xml文件,返回XmlEntityMap对象
        return XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(localFile);
    }

    /**
     * @Author wang
     * @Description: 在指定位置生成xml文件
     * @Param: [xMlEntityMapVO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
    @Override
    public boolean createXMLFile(XMlEntityMapVO xMlEntityMapVO) {
        File localPathFile = new File(xMlEntityMapVO.getLocalPath());
        if (!localPathFile.exists()) {
            try {
                //文件如果不存在,创建文件
                File parentFile = localPathFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                localPathFile.createNewFile();
            } catch (IOException e) {
                logger.error("文件创建失败");
                return false;
            }
        }
        return XmlFileHandleUtil.createXmlFile(xMlEntityMapVO.getXmlEntityMap(), localPathFile);

    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @Title: readWord
     * @Desc POI 读取 word
     * @Author xiaohe
     * @DateTime 2020年4月1日
     */
    private static String readWord(String filePath) throws Exception {
        logger.info("POI 开始读取 word文档。。。。。");
        String buffer = "";
        String fileType = filePath.substring(filePath.lastIndexOf("."));
        FileExtensionEnum typeEnum = FileExtensionEnum.containsFileType(fileType);
        try {
            switch (typeEnum) {
                // 当值时doc文档时
                case DOC_EXTENSION:
                    InputStream is = new FileInputStream(new File(filePath));
                    WordExtractor ex = new WordExtractor(is);
                    buffer = ex.getText();
                    ex.close();
                    logger.info("POI 开始读取 word(DOC)文档。。。。。");
                    break;
                // 当值时docx文档
                case DOCX_EXTENSION:
                    OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
                    POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                    buffer = extractor.getText();
                    extractor.close();
                    logger.info("POI 开始读取 word(DOCX)文档。。。。。");
                    break;
                default:
                    buffer = null;
                    break;
            }
        } catch (Exception e) {
            logger.error("POI 开始读取 文档失败。。{}，{}", e.getMessage(), e);
        }
        logger.info("POI 开始读取 word文档。。。。。");
        return buffer;
    }

    public static void main(String[] args) {
        try {
            String filePath = "D:\\输入输出参数赋值说明.docx";
//			System.out.println(getFilecharset(new File("D:\\输入输出参数赋值说明.docx")));
//			;
            String fileType = filePath.substring(filePath.lastIndexOf("."));
//			FileExtensionEnum.DOC_EXTENSION
            FileExtensionEnum typeEnum = FileExtensionEnum.containsFileType(fileType);
            System.out.println(typeEnum);
            String linList = readWord("D:\\输入输出参数赋值说明.docx");
//			writeTxt("D:\\123.txt", "GBK");
//			writeTxt("D:\\1234.txt", "UTF8");
//			System.out.println(getFilecharset(new File("D:\\123.txt")));
            System.out.println("fileType:" + fileType + "    " + FileExtensionEnum.DOC_EXTENSION);
//			for (String string : linList) {
            System.out.println("string:::::::" + linList);
//			}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param sourceFile
     * @return
     * @Title: getFilecharset
     * @Desc 判断编码格式方法
     * @Author xiaohe
     * @DateTime 2020年4月1日
     */
    private static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte _xff = (byte) 0xFF;
        byte _xfe = (byte) 0xFE;
        byte _xef = (byte) 0xEF;
        byte _xbf = (byte) 0xBF;
        byte _xbb = (byte) 0xBB;
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                // 文件编码为 ANSI
                return charset;
            } else if (first3Bytes[0] == _xff && first3Bytes[1] == _xfe) {
                // 文件编码为 Unicode
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == _xfe && first3Bytes[1] == _xff) {
                // 文件编码为 Unicode big endian
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == _xef && first3Bytes[1] == _xbb && first3Bytes[2] == _xbf) {
                // 文件编码为 UTF-8
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0) {
                        break;
                    }
                    // 单独出现BF以下的，也算是GBK
                    if (0x80 <= read && read <= 0xBF) {
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        // 双字节 (0xC0 - 0xDF)
                        if (0x80 <= read && read <= 0xBF) {
                            continue;
                        } else {
                            break;
                        }

                    } else
                        // 也有可能出错，但是几率较小
                        if (0xE0 <= read && read <= 0xEF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                read = bis.read();
                                if (0x80 <= read && read <= 0xBF) {
                                    charset = "UTF-8";
                                    break;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                }
            }
            bis.close();
        } catch (Exception e) {
            logger.debug("下载文件夹,{}", e);
        }
        return charset;
    }

    /**
     * @param localPath
     * @param charset
     * @return
     * @Title: readTxt
     * @Desc 读取txt文件
     * @Author xiaohe
     * @DateTime 2020年4月1日
     */
    @SuppressWarnings("resource")
    public static String readTxt(String localPath, String charset) {
        StringBuffer sbt = new StringBuffer();
        try {
            // 定义缓冲区对象
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 通过文件输入流获取文件通道流对象
            FileChannel inFc = new FileInputStream(localPath).getChannel();
            // 读取数据
            buffer.clear();
            int length = inFc.read(buffer);
            sbt.append(new String(buffer.array(), 0, length, charset));
            inFc.close();
        } catch (Exception e) {
            logger.debug("下载文件夹,{}", e);
        }
        return sbt.toString();
    }

    /**
     * @param localPath   文件路径
     * @param textContext 文件内容
     * @param charset     字符编码
     * @Title: writeTxt
     * @Desc 写入txt 文件
     * @Author cvics
     * @DateTime 2020年4月1日
     */
    @SuppressWarnings("resource")
    public static boolean writeTxt(String localPath, String textContext, String charset) {
        logger.info("开始往文件{}里写数据", localPath);
        try {
            FileChannel outFc = new FileOutputStream(localPath, true).getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(textContext.getBytes(charset));
//			ByteBuffer byteBuffer = ByteBuffer.wrap("\r\n李白乘舟将欲行".getBytes(charset));
            outFc.write(byteBuffer);
            outFc.close();
        } catch (Exception e) {
            logger.error("开始往文件", e);
        }
        return true;
    }

    /**
     * @param sourcePath 下载的文件路径
     * @return
     * @throws Exception
     * @Title: downloadFile
     * @Desc 下载文件本地
     * @Author cvics
     * @DateTime 2020年4月2日
     * @see com.inforbus.gjk.dataCenter.service.FileService#downloadFile(java.lang.String)
     */
    @Override
    public List<FileCenter> downloadFile(String sourcePath) throws Exception {
        List<FileCenter> fileCenters = Lists.newArrayList();
        // 循环遍历所有文件
        readfile(fileCenters, sourcePath);
        return fileCenters;
    }

    /**
     * @param fileCenters 文件对象的集合
     * @param filepath    文件路径
     * @throws FileNotFoundException
     * @throws IOException
     * @Title: readfile
     * @Desc 读取文件夹下的所有文件
     * @Author cvics
     * @DateTime 2020年4月7日
     */
    private void readfile(List<FileCenter> fileCenters, String filepath) throws FileNotFoundException, IOException {
        File file = UploadFilesUtils.createFile(filepath);
        // 判断是否是文件
        if (!file.isDirectory()) {
            FileCenter center = new FileCenter(file.getName(), file.length(), file.getPath(), file.getAbsolutePath(),
                    new FileInputStream(file));
            // 添加文件
            fileCenters.add(center);
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String path : filelist) {
                // 根据路径创建文件
                File readfile = UploadFilesUtils.createFile(filepath + File.separator + path);
                // 判断是否是文件
                if (!readfile.isDirectory()) {
                    FileCenter center = new FileCenter(readfile.getName(), readfile.length(), readfile.getPath(),
                            file.getAbsolutePath(), new FileInputStream(file));
                    fileCenters.add(center);
                } else
                    // 判断是否是文件 如果时文件夹递归遍历文件
                    if (readfile.isDirectory()) {
                        readfile(fileCenters, filepath + File.separator + path);
                    }
            }
        }
    }

}
