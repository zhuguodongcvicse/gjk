package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/ExternalInfServer")
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE)
public interface ExternalInfInvokeService {


    @RequestMapping("/getCmpSysConfig")
    public R<Map<String, List<String>>> getCmpSysConfig(@RequestParam("netWorkConfigFileName") String netWorkConfigFileName,
                                                        @RequestParam("packinfoPath") String packinfoPath,
                                                        @RequestParam("workModeFileName") String workModeFileName);
	/**
	 * 自定义配置调用客户接口
	 * @param flowFilePath
	 * @param ThemeFilePath
	 * @param filePath
	 * @return
	 */
	@PostMapping("/createUserDefineTopic")
	public R<Boolean> createUserDefineTopic(@RequestParam("flowFilePath")String flowFilePath, 
			@RequestParam("ThemeFilePath")String ThemeFilePath, @RequestParam("filePath")String filePath);
	

	/**
	 * 注册
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @param packinfoPath    客户自存自取路径
	 * @param cmpResFilePath  客户自存自取路径
	 * @param appProPath      APP工程文件夹路径
	 * @return
	 */
	@PostMapping("/appInstall")
	public boolean appInstall(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName,
			@RequestParam("appID") int appID, @RequestParam("appName") String appName,
			@RequestParam("packinfoPath") String packinfoPath, @RequestParam("cmpResFilePath") String cmpResFilePath,
			@RequestParam("appProPath") String appProPath);

	/**
	 * 加载、更新加载
	 *
	 * @param cmpNameToHwType   APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID             APP对应的流程ID
	 * @param appName           APP名称
	 * @param existDeployConfig APP组件工程配置
	 * @param sysconfigPath     系统配置模块XML路径
	 * @param appProPath        APP工程文件夹路径
	 * @return
	 */
	@PostMapping("/appLoad")
	public boolean appLoad(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName,
			@RequestParam("appID") int appID, @RequestParam("appName") String appName,
			@RequestParam("existDeployConfig") boolean existDeployConfig,
			@RequestParam("sysconfigPath") String sysconfigPath, @RequestParam("appProPath") String appProPath);

	/**
	 * 注销
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @param packinfoPath    客户自存自取路径
	 * @return
	 */
	@PostMapping("/appUnInstall")
	public boolean appUnInstall(@RequestBody Map<String, String> cmpNameToHwType,
			@RequestParam("userName") String userName, @RequestParam("appID") int appID,
			@RequestParam("appName") String appName, @RequestParam("packinfoPath") String packinfoPath);

	/**
	 * 导出
	 *
	 * @param appId                 APP对应的流程ID
	 * @param appName               APP名称
	 * @param appPath               APP工程文件夹路径
	 * @param sysconfigPath         系统配置模块XML路径
	 * @param packinfoPath          客户自存自取路径
	 * @param cmpDeployPlanFilePath 客户自存自取路径
	 */
	@PostMapping("/appTaskExport")
	public void appTaskExport(@RequestParam("userName") String userName, @RequestParam("appId") int appId,
			@RequestParam("appName") String appName, @RequestParam("appPath") String appPath,
			@RequestParam("sysconfigPath") String sysconfigPath, @RequestParam("packinfoPath") String packinfoPath,
			@RequestParam("cmpDeployPlanFilePath") String cmpDeployPlanFilePath);

	/**
     * 软硬件映射的接口，生成缩略方案
     *
     * @param schemeFileList
     */
	@RequestMapping("/simplePlan")
    public void simplePlan(@RequestParam("str") String[] str,@RequestParam("simplePlanFile")  String simplePlanFile) ;
	
	/**
     * 调回写部署方案接口
     *
     * @param schemeFileList
     */
	@RequestMapping("/writeBackDeployScheme")
    public void writeBackDeployScheme(@RequestParam("workModeFilePath") String workModeFilePath, @RequestParam("schemeFile") String schemeFile );

}
