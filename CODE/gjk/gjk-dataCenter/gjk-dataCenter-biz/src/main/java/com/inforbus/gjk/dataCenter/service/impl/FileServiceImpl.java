package com.inforbus.gjk.dataCenter.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.inforbus.gjk.comp.api.dto.ComponentDTO;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.entity.Components;
import com.inforbus.gjk.comp.api.util.XmlAnalysisUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.constant.enums.FileExtensionEnum;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.dataCenter.api.dto.ThreeLibsDTO;
import com.inforbus.gjk.dataCenter.api.dto.ThreeLibsFilePathDTO;
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
	@Value("${gjk.code.encodeing}")
	private String defaultEncoding;

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
	 *      java.util.List)
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
			FileOutputStream fos=null;
			try {
				in = fc.getInputStream().getChannel();
				fos = new FileOutputStream(
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
					fos.close();
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
	 *      java.lang.String, java.lang.String)
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
		return UploadFilesUtils.delAllFile(sourcePath);
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
	public Boolean delFolder(String[] folderPath) throws Exception {
		boolean isDel = true;
		for (String spath : folderPath) {
			// delAllFile ==>删除完里面所有内容
			if (delAllFile(spath)) {
				File file = new File(spath);
				// myFilePath 删除空文件夹
				file.delete();
			} else {
				isDel = false;
				break;
			}
		}
		return isDel;
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
	 *      java.lang.String)
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
	 *      java.lang.String, java.lang.String)
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
			// 如果xml文件不存在,返回null
			logger.error(localFile.getName() + "文件不存在");
			throw new FileNotFoundException(localFile.getName() + "文件不存在");
		}
		// 通过工具类按照路径解析xml文件,返回XmlEntityMap对象
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
				// 文件如果不存在,创建文件
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
	 * @Author wang
	 * @Description: 根据据对路径删除文件
	 * @Param: [absolutePath]
	 * @Return: boolean
	 * @Create: 2020/4/15
	 */
	@Override
	public boolean delFile(String absolutePath) {
		File file = new File(absolutePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
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

	/**
	 * @param sourceFile
	 * @return
	 * @Title: getFilecharset
	 * @Desc 判断编码格式方法
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 */
	@SuppressWarnings("resource")
	private static String getFilecharset(File sourceFile) {
		String charset = "GBK";
		byte xff = (byte) 0xFF;
		byte xfe = (byte) 0xFE;
		byte xef = (byte) 0xEF;
		byte xbf = (byte) 0xBF;
		byte xbb = (byte) 0xBB;
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				// 文件编码为 ANSI
				return charset;
			} else if (first3Bytes[0] == xff && first3Bytes[1] == xfe) {
				// 文件编码为 Unicode
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == xfe && first3Bytes[1] == xff) {
				// 文件编码为 Unicode big endian
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == xef && first3Bytes[1] == xbb && first3Bytes[2] == xbf) {
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
		readfileStream(fileCenters, sourcePath);
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
	private void readfileStream(List<FileCenter> fileCenters, String filepath)
			throws FileNotFoundException, IOException {
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
					readfileStream(fileCenters, filepath + File.separator + path);
				}
			}
		}
	}

	/**
	 * 程序文本编辑器的文件展示
	 * 
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	@Override
	public R fileReads(ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		String fileName = threeLibsFilePathDTO.getFilePathName();
		// 获取文件编码格式
		String code = "";
		if (StringUtils.isEmpty(threeLibsFilePathDTO.getCode())) {
			code = defaultEncoding;
		} else {
			code = threeLibsFilePathDTO.getCode();
		}
		if (StringUtils.isEmpty(fileName)) {
			return new R<>();
		}
		File isFile = new File(fileName);
		// 获取文件后缀名
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String str = "";
		ThreeLibsDTO dto = new ThreeLibsDTO();
		if (isFile.exists() && isFile.isFile()) {

			// 读取excel文件
			if ("xlsx".equals(prefix) || "xls".equals(prefix)) {
				try {

					XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileName);
					// 循环工作表sheet
					XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
					int firstRowIndex = xssfSheet.getFirstRowNum(); // 第一行是列名，所以不读
					int lastRowIndex = xssfSheet.getLastRowNum();
					// 循环行row
					XSSFRow xssfRow = xssfSheet.getRow(0);
					// 循环列cell
					// 用stringbuffer得到excel表格一行的内容并用逗号分隔
					StringBuffer sbs = new StringBuffer();
					for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {// 遍历行
						Row row = xssfSheet.getRow(rIndex);
						if (row != null) {
							int firstCellIndex = row.getFirstCellNum();
							int lastCellIndex = row.getLastCellNum();
							for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {// 遍历列
								Cell cell = row.getCell(cIndex);
								sbs.append(cell);
								if (cell != null) {
									sbs.append(",");
								}
							}
						}

					}
					str = sbs.toString();
				} catch (Exception e) {
					e.printStackTrace();
					return new R<>(new Exception("读取" + fileName + "文件内容出错"));
				}
				// 读取word文件
			} else if ("doc".equals(prefix) || "docx".equals(prefix)) {
				WordExtractor extractor = null;
				try {

					FileInputStream in = new FileInputStream(fileName);
					extractor = new WordExtractor(in);
					str = extractor.getText();
//				dto.setTextContext(str);
				} catch (Exception e) {
					e.printStackTrace();
					return new R<>(new Exception("读取" + fileName + "文件内容出错"));
				} finally {
					// 关闭 WordExtractor
					try {
						extractor.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// 读取//.h .m .c .o等客户 文件
			else {
				try {
					str = FileUtils.readFileToString(new File(fileName), code);
				} catch (Exception e) {
					e.printStackTrace();
					return new R<>(new Exception("读取" + fileName + "文件内容出错"));
				}
			}
			dto.setTextContext(prefix + "@%#@*+-+@" + str + "@%#@*+-+@" + code);
		} else {
			return new R<>(new Exception("找不到指定的文件"));
		}
		return new R<>(dto);
	}

	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 *
	 * @param filePath    文件路径
	 * @param textContext 文本内容
	 */
	@Override
	public void saveFileContext(String filePath, String textContext) {
		String fileName = filePath;
		File file = new File(fileName);
		OutputStream outputStream = null;
		if (file.exists()) {
			try {
				// 如果文件找不到，就new一个
				file.delete();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// 定义输出流，写入文件的流
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 定义将要写入文件的数据
		// 把string转换成byte型的，并存放在数组中
		byte[] bs = textContext.getBytes();
		try {
			// 写入bs中的数据到file中
			outputStream.write(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: getUploadFilePaths
	 * @Desc 多文件上传
	 * @Author cvics
	 * @DateTime 2020年4月16日
	 * @param files 文件列表
	 * @param paths 列表全路径
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @see com.inforbus.gjk.dataCenter.service.FileService#getUploadFilePaths(org.springframework.web.multipart.MultipartFile[],
	 *      java.lang.String)
	 */
	@Override
	public Boolean getUploadFilePaths(MultipartFile[] files, String paths) throws IllegalStateException, IOException {
		String url = "";
		Boolean bool = true;
		try {
			for (MultipartFile file : files) {
				// fileName==>获取上传文件名,包含后缀
				String fileName = file.getOriginalFilename();
				url = new String(paths + File.separator + fileName);
				File uploadFile = UploadFilesUtils.createFile(url);
				// uploadFile.exists()==>判断文件是否存在
				if (uploadFile.exists()) {
					uploadFile.delete();
				}
				// transferTo==>将文件上传上传至uploadFile中
				file.transferTo(uploadFile);
			}
		} catch (IllegalStateException e) {
			bool = false;
			throw new IllegalStateException("上传文件返回路径出错", e);
		} catch (IOException e) {
			bool = false;
			throw new IOException("上传文件返回路径出错", e);
		}
		return bool;
	}

	/**
	 * @Title: decompression
	 * @Desc 解压文件
	 * @Author xiaohe
	 * @DateTime 2020年5月8日
	 * @param file  文件
	 * @param paths 文件路径
	 * @return
	 * @see com.inforbus.gjk.dataCenter.service.FileService#decompression(org.springframework.web.multipart.MultipartFile,
	 *      java.lang.String)
	 */
	@Override
	public R decompression(MultipartFile file, String paths) {
		logger.debug("开始解压文件{}", file.getOriginalFilename());
		long startTime = System.currentTimeMillis(); // 获取开始时间
		if (ObjectUtils.isNotEmpty(file)) {
			File uploadFile = null;
			if (StringUtils.isNotEmpty(paths)) {
				uploadFile = new File(paths);
				if (!uploadFile.getParentFile().exists()) {
					uploadFile.getParentFile().mkdirs();
				}
			}
		}
		// 创建FutureTask
		R ret = new R<Boolean>();
		try {
			FutureTask<R> futureTask = new FutureTask<>(new Callable<R>() {
				@Override
				public R call() {
					// 线程执行体
					try {
						UploadFilesUtils.decompression(file.getInputStream(), paths);
						// 返回值
						return new R<Boolean>(true);
					} catch (IOException e) {
						logger.error("解压文件出错", e.getMessage());
						// 返回值
						return new R<>(e);
					}

				}
			});
			futureTask.run();
			ret = futureTask.get();
			logger.debug("结束解压文件{}", file.getOriginalFilename());
			long endTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
			logger.info("程序运行时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
		} catch (InterruptedException e) {
			ret = new R<>(e);
			logger.error("解压文件出错", e.getMessage());
		} catch (ExecutionException e) {
			ret = new R<>(e);
			logger.error("解压文件出错", e.getMessage());
		}
		return ret;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 * @auther sunchao
	 * @return
	 * @throws IOException
	 */
	@Override
	public Boolean judgeFileExist(String filePath) {
		return new File(filePath).exists();
	}

	/**
	 * 查找App路径
	 * 
	 * @param filePath
	 * @param selectFileName
	 * @auther sunchao
	 * @return
	 * @throws IOException
	 */
	@Override
	public String getAppPath(String filePath, String selectFileName) throws IOException {
		// 要返回的app路径
		String selectPath = null;
		// 拿到目标路径的path对象
		Path path = Paths.get(filePath);
		// 拿到匹配器
		PathMatcher matcher = FileSystems.getDefault()
				.getPathMatcher("glob:**" + FileSystems.getDefault().getSeparator() + selectFileName);
		// 查找匹配的文件，转成list
		List<Path> collect = Files.walk(path).filter(matcher::matches).collect(Collectors.toList());
		// 找到符合条件的路径
		for (Path pathEl : collect) {
			if (pathEl.toString().contains(selectFileName) && !pathEl.toString().contains("debug")) {
				selectPath = pathEl.toString();
			}
		}
		return selectPath;
	}
	
	/**
	 * 保存流程建模json
	 */
	@Override
	public void editProJSON(StringRef strRef, String filePath) {
		String jsonString = strRef.getVal();
		File file = new File(filePath);
		if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
			file.getParentFile().mkdirs();
		}
		if (file.exists()) { // 如果已存在,删除旧文件
			file.delete();
		}
		Writer write = null;
		try {
			write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
		} catch (Exception e) {
			logger.error("保存json文件失败");
		} finally {
			if(write != null) {
				try {
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	/**
	 * 解析流程建模json
	 */
	@Override
	public String findJson(String jsonPath) {
		File jsonFile = new File(jsonPath);
		FileReader fileReader;
		String jsonStr = "";
		if(jsonFile.exists()) {
			try {
				fileReader = new FileReader(jsonFile);
				Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
				int ch = 0;
				StringBuffer sb = new StringBuffer();
				while ((ch = reader.read()) != -1) {
					sb.append((char) ch);
				}
				fileReader.close();
				reader.close();
				jsonStr = sb.toString();
			} catch (Exception e) {
				logger.error("json文件解析失败");
			}
			
		}
		return jsonStr;
	}

	/**
	 * @Author wang
	 * @Description: 根据绝对路径判断是否是文件
	 * @Param: [filePath]
	 * @Return: boolean
	 * @Create: 2020/5/21
	 */
    @Override
    public boolean isFile(String filePath) {
		File file = new File(filePath);
		return !file.isFile();
//		return file.isDirectory();
    }

	/**
	 * 循环解析构件xml并返回
	 * @param filePathMap
	 * @return
	 * @throws FileNotFoundException
	 */
	@Override
	public Map<String, XmlEntityMap> getCompData(Map<String, String> filePathMap) throws FileNotFoundException {
		Map<String, XmlEntityMap> xmlEntityMapMap = new HashMap<String, XmlEntityMap>();
		for (Map.Entry<String,String > entrys :filePathMap.entrySet()) {
			File localFile = new File(entrys.getValue());
			if (!localFile.exists()) {
				// 如果xml文件不存在,返回null
				logger.error(localFile.getName() + "文件不存在");
				throw new FileNotFoundException(localFile.getName() + "文件不存在");
			}
			XmlEntityMap xmlMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(localFile);
			xmlEntityMapMap.put(entrys.getKey(),xmlMap);
		}
		return xmlEntityMapMap;
	}

	@Override
	public List<ComponentDTO> fileService(List<Components> componentsList) {
		List<ComponentDTO> dtos = Lists.newArrayList();
		for(Components components : componentsList){
			String compFilePth = this.localBasePath + File.separator + components.getComponentDetail().getFilePath() + File.separator
					+ components.getComponentDetail().getFileName();
			dtos.add(XmlAnalysisUtil.xmlFileToComponentDTO(components.getComponent(), components.getComponentDetail(), new File(compFilePth)));
		}
		return dtos;
	}
}
