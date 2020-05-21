package com.inforbus.gjk.libs.api.feign;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.feign.factory.DataCenterServiceFallbackFactory;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, fallbackFactory = DataCenterServiceFallbackFactory.class)
public interface DataCenterServiceFeign {

	/**
	 * 三个库读取程序文本编辑器文件
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	@PostMapping("/fileServe/readFiles")
	public R readFiles(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) ;
	
	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 * @param filePath 文件路径
	 * @param textContext 文本内容
	 */
	@PostMapping("/fileServe/saveFileContext")
	public R saveFileContext(@RequestParam("filePath") String filePath,@RequestParam("textContext") String textContext);

	/**
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @return
	 * @throws Exception
	 * @Title: copylocalFile
	 * @Description: 拷贝文件
	 * @Author xiaohe
	 * @DateTime 2019年11月27日 下午8:46:20
	 */
	@PostMapping("/fileServe/copylocalFile")
	public R<Boolean> copylocalFile(@RequestParam("source") String source, @RequestParam("destin") String destin);

	/**
	 * @Author wang
	 * @Description: 根据绝对路径判断是否是文件
	 * @Param: [filePath]
	 * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
	 * @Create: 2020/5/21
	 */
	@PostMapping("/fileServe/isFile")
	@ResponseBody
	public R<Boolean> isFile(@RequestParam("filePath") String filePath);

	/**
	 * @Author wang
	 * @Description: 解析xml文件为xmlEntity对象
	 * @Param: [localPath] 文件的绝对路径
	 * @Return: boolean
	 * @Create: 2020/4/13
	 */
	@PostMapping("/fileServe/analysisXmlFile")
	public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath);

	/**
	 * @Author wang
	 * @Description: 在指定位置生成xml文件
	 * @Param: [xMlEntityMapVO]
	 * @Return: boolean
	 * @Create: 2020/4/14
	 */
	@PostMapping("/fileServe/createXMLFile")
	public R<Boolean> createXMLFile(@RequestBody XMlEntityMapVO xMlEntityMapVO);
}
