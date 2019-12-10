
package com.inforbus.gjk.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.entity.*;
import com.inforbus.gjk.admin.api.vo.MenuVO;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.admin.api.vo.TestVO;
import com.inforbus.gjk.admin.mapper.*;
import com.inforbus.gjk.admin.service.GjkAlgorithmService;
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.admin.service.GjkTestService;
import com.inforbus.gjk.admin.service.SysMenuService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 测试库权限表 服务实现类
 * </p>
 *
* @author geng_hxian
 * @since 2019/4/17
 */
@Service
@AllArgsConstructor
public class GjkTestServiceImpl extends ServiceImpl<GjkTestMapper, GjkTest> implements GjkTestService {
	private static final String gitFilePath = JGitUtil.getLOCAL_REPO_PATH();

	@Autowired
	GjkPlatformMapper gjkPlatformMapper;
	@Autowired
	GjkAlgorithmMapper gjkAlgorithmMapper;

	@Override
	@Cacheable(value = "menu_details", key = "#roleId  + '_menu'")
	public List<TestVO> getTestByRoleId() {
		return baseMapper.listTestsByRoleId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "menu_details", allEntries = true)
	public R removeTestById(String id) {
		// 查询父节点为当前节点的节点
		List<GjkTest> testList = this.list(Wrappers.<GjkTest>query()
			.lambda().eq(GjkTest::getParentId, id));
		if (CollUtil.isNotEmpty(testList)) {
			return R.builder()
				.code(CommonConstants.FAIL)
				.msg("测试含有下级不能删除").build();
		}

		//删除当前测试库及其子测试
		return new R(this.removeById(id));
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean updateTestById(GjkTest gjkTest) {
		return this.updateById(gjkTest);
	}

	@Override
	public byte[] createZip(List<String> libs) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		createExcelFileToZipIO(libs, zip);

		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	@Override
	public int analysisZipFile(MultipartFile ufile, String importType) {
		String filePath = gitFilePath + "gjk" + File.separator + "zipFile" + File.separator
				+ (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + "-" + ufile.getOriginalFilename();
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			ufile.transferTo(file);

			String descDirPath = filePath.substring(0, filePath.lastIndexOf("."));
			// 解压zip文件夹
			unZipFiles(filePath, descDirPath);

			int tag = 0;
			if ("libs".equals(new File(descDirPath).listFiles()[0].getName())) {
				String excelFilePath = descDirPath + File.separator + "libs" + File.separator + "MySQL.xls";
				tag = readExcel(descDirPath, excelFilePath, importType);
				// 删除压缩包
				cn.hutool.core.io.FileUtil.del(filePath);
				// 删除解压包
				cn.hutool.core.io.FileUtil.del(descDirPath);
			} else {
				return -1;
			}
			return tag;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 解析excel文件
	 * @param unZipFilePath
	 * @param path
	 * @param importType
	 * @return
	 * @throws Exception
	 */
	public int readExcel(String unZipFilePath, String path, String importType) throws Exception {
		// 获取整个表格文件对象
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));

		List<GjkTest> testList = new ArrayList<>();
		List<GjkAlgorithm> algorithmList = new ArrayList<>();
		List<GjkPlatform> platformList = new ArrayList<>();
		int testTag = 0, algorithmTag = 0, platformTag = 0;

		// 获取所有sheet对象
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			String tableName = sheet.getSheetName();
			String className = null;
			if ("gjk_test".equals(tableName)) {
				testTag = 1;
				className = "com.inforbus.gjk.admin.api.entity.GjkTest";
				for (Object obj : parseSheet(sheet, className)) {
					testList.add((GjkTest) obj);
				}
			} else if ("gjk_algorithm".equals(tableName)) {
				algorithmTag = 1;
				className = "com.inforbus.gjk.admin.api.entity.GjkAlgorithm";
				for (Object obj : parseSheet(sheet, className)) {
					algorithmList.add((GjkAlgorithm) obj);
				}
			} else if ("gjk_platform".equals(tableName)) {
				platformTag = 1;
				className = "com.inforbus.gjk.admin.api.entity.GjkPlatform";
				for (Object obj : parseSheet(sheet, className)) {
					platformList.add((GjkPlatform) obj);
				}
			}
		}
		workbook.close();

		// 判断导入类型
		// 覆盖导入
		if(importType.equals("allImport")){
			if(testTag == 1){
				// 测试库表清空
				baseMapper.deleteAll();
				// 测试库数据入库
				for (GjkTest gjkTest : testList) {
					baseMapper.insert(gjkTest);
				}
			}

			if(platformTag == 1){
				// 平台库表清空
				gjkPlatformMapper.deleteAll();
				// 平台库数据入库
				for (GjkPlatform gjkPlatform : platformList) {
					gjkPlatformMapper.insert(gjkPlatform);
				}
			}

			if(algorithmTag == 1){
				// 算法库表清空
				gjkAlgorithmMapper.deleteAll();
				// 算法库数据入库
				for (GjkAlgorithm gjkAlgorithm : algorithmList) {
					gjkAlgorithmMapper.insert(gjkAlgorithm);
				}
			}
		}

		// 增量导入
		else if(importType.equals("addImport")){
			// 测试库数据入库
			for (GjkTest gjkTest : testList) {
				// 先根据name判断数据是否存在
				QueryWrapper<GjkTest> wrapper = new QueryWrapper<>();
				wrapper.eq("name", gjkTest.getName());
				Integer num = baseMapper.selectCount(wrapper);
				if(num == 0){
					// 判断id 如果存在则重置id
//					wrapper = new QueryWrapper<>();
//					wrapper.eq("test_id", gjkTest.getTestId());
//					wrapper.in("del_flag", "0", "1");
					num = baseMapper.selectCountById(gjkTest.getTestId());
					if(num > 0){
						gjkTest.setTestId(IdGenerate.uuid());
					}
					baseMapper.insert(gjkTest);
				}
			}

			// 平台库数据入库
			for (GjkPlatform gjkPlatform : platformList) {
				// 先根据name判断数据是否存在
				QueryWrapper<GjkPlatform> wrapper = new QueryWrapper<>();
				wrapper.eq("name", gjkPlatform.getName());
				Integer num = gjkPlatformMapper.selectCount(wrapper);
				if(num == 0){
					// 判断id 如果存在则重置id
//					wrapper = new QueryWrapper<>();
//					wrapper.eq("platform_id", gjkPlatform.getPlatformId());
//					wrapper.in("del_flag", "0", "1");
					num = gjkPlatformMapper.selectCountById(gjkPlatform.getPlatformId());
					if(num > 0){
						gjkPlatform.setPlatformId(IdGenerate.uuid());
					}
					gjkPlatformMapper.insert(gjkPlatform);
				}
			}

			// 算法库数据入库
			for (GjkAlgorithm gjkAlgorithm : algorithmList) {
				// 先根据name判断数据是否存在
				QueryWrapper<GjkAlgorithm> wrapper = new QueryWrapper<>();
				wrapper.eq("name", gjkAlgorithm.getName());
				Integer num = gjkAlgorithmMapper.selectCount(wrapper);
				if(num == 0){
					// 判断id 如果存在则重置id
//					wrapper = new QueryWrapper<>();
//					wrapper.eq("algorithm_id", gjkAlgorithm.getAlgorithmId());
					num = gjkAlgorithmMapper.selectCountById(gjkAlgorithm.getAlgorithmId());
					if(num > 0){
						gjkAlgorithm.setAlgorithmId(IdGenerate.uuid());
					}
					gjkAlgorithmMapper.insert(gjkAlgorithm);
				}
			}
		}
		return 0;
	}

	/**
	 * 解析工作簿
	 *
	 * @param sheet
	 * @throws Exception
	 */
	private List<Object> parseSheet(Sheet sheet, String className) throws Exception {
		List<Object> objects = new ArrayList<Object>();

		// 获取所有Row对象
		Iterator<Row> iterator = sheet.iterator();
		int index = 0;
		// 存列信息
		List<String> columnList = new ArrayList<>();
		while (iterator.hasNext()) {
			if (index == 0) {
				columnList = parseRow(iterator.next());
			} else {
				objects.add(parseRow(iterator.next(), className, columnList));
			}
			index++;
		}
		return objects;
	}

	/**
	 * 解析列
	 *
	 * @param next
	 */
	private List<String> parseRow(Row next) {
		List<String> list = new ArrayList<String>();
		// 获取所有Cell对象取值
		Iterator<Cell> cellIterator = next.cellIterator();
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			cell.setCellType(CellType.STRING);
			list.add(cell.getStringCellValue());
		}
		return list;
	}

	/**
	 * 解析每行数据
	 *
	 * @param next
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private Object parseRow(Row next, String calssName, List<String> columnList) throws Exception {
		Class<?> cla = Class.forName(calssName);
		Object obj = cla.newInstance();

		// 获取所有Cell对象取值
		Iterator<Cell> cellIterator = next.cellIterator();
		int index = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			cell.setCellType(CellType.STRING);
			if (!"".equals(cell.getStringCellValue())) {
				if ("CreateTime".equals(columnList.get(index)) || "UpdateTime".equals(columnList.get(index))) {
					LocalDateTime parse = LocalDateTime.parse(cell.getStringCellValue());
					setFieldValueByName(columnList.get(index), cla, obj, parse);
				} else {
					setFieldValueByName(columnList.get(index), cla, obj, cell.getStringCellValue());
				}
			}
			index++;
		}
		return obj;
	}

	/**
	 * set属性值
	 *
	 * @param fieldName 字段名称
	 * @param o         对象
	 * @return Object
	 */
	private void setFieldValueByName(String fieldName, Class<?> clazz, Object o, Object value) {
		try {
			String firstLetter = fieldName.substring(0, 1).toLowerCase();
			fieldName = firstLetter + fieldName.substring(1); // 获取方法名
			PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
			Method wM = pd.getWriteMethod();// 获得写方法

			// 判断字段类型 转换类型
			if(pd.getPropertyType().getName().indexOf("Double") > -1){
				value = Double.valueOf((String)value);
			}else if(pd.getPropertyType().getName().indexOf("Integer") > -1){
				value = Integer.valueOf((String)value);
			}

			wM.invoke(o, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 生成Excel文件并添加到压缩文件流中
	 * @param libs
	 * @param zip
	 * @throws Exception
	 * @throws IOException
	 */
	private void createExcelFileToZipIO(List<String> libs, ZipOutputStream zip) throws Exception, IOException {
		// 创建表格工作空间
		XSSFWorkbook workbook = new XSSFWorkbook();

		for (String item : libs) {
			// 测试库
			if(item.equals("testLib")){
				List<GjkTest> testList = baseMapper.selectList(null);
				createSheet(workbook, testList, "gjk_test");
			}
			// 算法库
			else if(item.equals("algorithmLib")){
				List<GjkAlgorithm> algorithmList = gjkAlgorithmMapper.selectList(null);
				createSheet(workbook, algorithmList, "gjk_algorithm");
			}
			// 平台库
			else if(item.equals("platformLib")){
				List<GjkPlatform> platformList = gjkPlatformMapper.selectList(null);
				createSheet(workbook, platformList, "gjk_platform");
			}
		}

		// 创建Excel文件保存的临时地址
		File file = new File(gitFilePath + "gjk" + File.separator + "testExcel" + File.separator + "MySQL.xls");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		// 创建Excel文件压缩目录
		ZipEntry entry = new ZipEntry("libs" + File.separator + "MySQL.xls");
		zip.putNextEntry(entry);
		// 将Excel文件内容写入临时文件
		OutputStream op = new FileOutputStream(file);
		workbook.write(op);
		// 将Excel文件读入压缩文件流
		zip.write(FileUtils.readFileToByteArray(file));
		// 将临时文件删除
		file.delete();
		zip.flush();
		zip.closeEntry();
	}

	/**
	 * 创建一个sheep标签 并处理数据
	 * @param workbook
	 * @param dataList
	 * @param tableName
	 * @throws Exception
	 */
	private void createSheet(XSSFWorkbook workbook, List<?> dataList, String tableName) throws Exception {
		XSSFSheet compDetailSheet = workbook.createSheet(tableName);
		// set Sheet页头部
		List<String> compDetailColumnList = setSheetHeader(workbook, compDetailSheet, tableName);
		// set Sheet页内容
		List<Object> list = new ArrayList<Object>();
		list.addAll(dataList);
		setSheetContent(workbook, compDetailSheet, list, compDetailColumnList);
	}

	/**
	 * 配置Excel表格的顶部信息，如：学号 姓名 年龄 出生年月
	 *
	 * @param xWorkbook
	 * @param xSheet
	 */
	private List<String> setSheetHeader(XSSFWorkbook xWorkbook, XSSFSheet xSheet, String tableName) {
		List<String> columnList = new ArrayList<>();

		// 设置表格的宽度 xSheet.setColumnWidth(0, 20 * 256); 中的数字 20 自行设置为自己适用的
		xSheet.setColumnWidth(0, 20 * 256);
		xSheet.setColumnWidth(1, 15 * 256);
		xSheet.setColumnWidth(2, 15 * 256);
		xSheet.setColumnWidth(3, 20 * 256);

		// 创建表格的样式
		XSSFCellStyle cs = xWorkbook.createCellStyle();
		// 设置居中
		cs.setAlignment(HorizontalAlignment.CENTER);
		// 设置字体
		Font headerFont = xWorkbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);
//		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("宋体");
		cs.setFont(headerFont);
		cs.setWrapText(true);// 是否自动换行

		// 创建一行
		XSSFRow row = xSheet.createRow(0);

		int columnNum = 0;
		// 设置每一列
		for (Map<String, String> column : queryColumns(tableName)) {
			XSSFCell cell = row.createCell(columnNum);
			cell.setCellStyle(cs);
			cell.setCellValue(columnToJava(column.get("columnName")));
			columnList.add(columnToJava(column.get("columnName")));
			columnNum++;
		}
		return columnList;
	}

	/**
	 * 配置(赋值)表格内容部分
	 *
	 * @param xWorkbook
	 * @param xSheet
	 * @throws Exception
	 */
	private void setSheetContent(XSSFWorkbook xWorkbook, XSSFSheet xSheet, List<Object> objList,
								 List<String> columnList) throws Exception {
		// 创建内容样式（头部以下的样式）
		CellStyle cs = xWorkbook.createCellStyle();
		cs.setWrapText(true);
		cs.setAlignment(HorizontalAlignment.CENTER);

		int rowIndex = 1;
		if (null != objList && objList.size() > 0) {
			for (Object obj : objList) {
				XSSFRow xRow = xSheet.createRow(rowIndex);
				int columnIndex = 0;
				for (String key : columnList) {
					Object temp = getFieldValueByName(key, obj);
					String strTemp = "";
					if (temp != null) {
						strTemp = temp.toString();
					}
					XSSFCell cell = xRow.createCell(columnIndex);
					cell.setCellStyle(cs);
					// 把每个对象此字段的属性写入这一列excel中
					cell.setCellValue(strTemp);

					columnIndex++;
				}
				rowIndex++;
			}
		}
	}

	/**
	 * 获取属性值
	 *
	 * @param fieldName 字段名称
	 * @param o         对象
	 * @return Object
	 */
	private static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1); // 获取方法名
			Method method = o.getClass().getMethod(getter, new Class[] {}); // 获取方法对象
			Object value = method.invoke(o, new Object[] {}); // 用invoke调用此对象的get字段方法
			return value; // 返回值
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取表中的字段名
	 *
	 * @param tableName
	 * @return
	 */
	private List<Map<String, String>> queryColumns(String tableName) {
		return baseMapper.queryColumns(tableName);
	}

	/**
	 * 列名转换成Java属性名
	 */
	private String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
	}
}
