package com.inforbus.gjk.dataCenter.service;

import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import flowModel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ExternalInfService {
    /**
     * 构件头文件解析
     *
     * @param headerPath 头文件
     * @return 输出对象
     */
    public HeaderFileTransVO parseHeaderFile(String headerPath);

    /**
     * 解析性能测试表
     *
     * @Title: parsePerformanceTable
     * @Description:
     * @Author CVICSE-COMMON
     * @DateTime 2019年6月3日 上午10:33:54
     * @param excelPath 性能测试表路径
     * @return 通过率
     */
    public Map<Integer, Float> parsePerformanceTable(String excelPath);

    /**
     * 软硬件映射的接口，生成缩略方案
     *
     * @param schemeFileList
     */
    public void simplePlan(ArrayList<String> schemeFileList, String simplePlanFile);

    /**
     * 调回写部署方案接口
     *
     * @param schemeFileList
     */
    public void writeBackDeployScheme(String workModeFilePath, String schemeFile );

    /**
     * 解析结构体（通用：包括系统表、内部表等）
     *
     * @param filePath 结构体文件路径
     * @return <结构体类型名，list<变量类型+空格+变量名>>
     */
    public Map<String, List<String>> parseStruct(String filePath);

    public Map<String, List<String>> getCmpSysConfig(String netWorkConfigFileName, String packinfoPath,
                                                            String workModeFileName);

    /**
     * 自定义配置调用接口
     */
    public void createUserDefineTopic(String flowFilePath, String ThemeFilePath, String filePath);

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
    public boolean appInstall(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
                                     String packinfoPath, String cmpResFilePath, String appProPath);

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

    public boolean appLoad(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
                                  boolean existDeployConfig, String sysconfigPath, String appProPath);

    /**
     * 卸载
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    public boolean appUnload(Map<String, String> cmpNameToHwType, String userName, int appID, String appName);

    /**
     * 启动
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    public boolean appRestart(Map<String, String> cmpNameToHwType, String userName, int appID, String appName);

    /**
     * 停止
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    public boolean appStop(Map<String, String> cmpNameToHwType, String userName, int appID, String appName);

    /**
     * 暂停
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    public boolean appPause(Map<String, String> cmpNameToHwType, String userName, int appID, String appName);

    /**
     * 注销
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @param packinfoPath    客户自存自取路径
     * @return
     */
    public boolean appUnInstall(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
                                       String packinfoPath);


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
    public void appTaskExport(String userName, int appId, String appName, String appPath, String sysconfigPath,
                                     String packinfoPath, String cmpDeployPlanFilePath);

    /**
     * 解析系统参数波形
     */
    public void parseSystemPara(String flowName, String str, String xmlPath);

    public void modifySpbInclude(List<String> compFuncNameList, String cmpAppFilePath) throws IOException;

    /**
     * @Title: createSpbFrameFile
     * @Description: 生成构件框架
     * @Author xiaohe
     * @DateTime 2019年11月13日 下午2:53:28
     * @param spbModelXmlFile 构件模型XML文件
     * @param spbModelDir     保存路径(可以是平台文件路径)
     */
    public void createSpbFrameFile(String spbModelXmlFile, String spbModelDir);

    /**
     * 完备性检查
     * @param flowFilePath
     * @return
     */
    public CheckResult completeCheck(String flowFilePath);

    /**
     * 仿真数据
     */
    public MoniRecvDataThread startMoniRecvDataThread(String redisIP, String channelName,List<String> arrowList, String flowFilePath, String ctrlTabFilePath );

    /**
     * 停止推送仿真数据
     */
    public void stopMoniRecvDataThread(MoniRecvDataThread thread);

    /**
     * @Title: ParseMoniData
     * @Description: 生成构件框架
     * @Author xu
     * @DateTime 2020年1月16日
     * @param flowFilePath 流程建模文件路径
     * @param packinfoFilePath     packinfo文件路径
     * @param packDataMap 数据包+页面配置项
     * @param arrowInfo     起始构件ID+“:”+变量名+“|”+结束构件ID+“:”+变量名
     */
    public Map<String, Object> parseMoniData(String flowFilePath, String packinfoFilePath, Map<String,Object> packDataMap,
                                                    String arrowInfo);

    /**
     * @Title: ParseMoniData
     * @Description: 生成构件框架
     * @Author xu
     * @DateTime 2020年1月16日
     * @param flowFilePath 流程建模文件路径
     * @param packinfoFilePath     packinfo文件路径
     * @param packDataMap 数据包+页面配置项
     * @param arrowInfo     起始构件ID+“:”+变量名+“|”+结束构件ID+“:”+变量名
     */
    public Map<String, String> getMaxXYZ(String flowFilePath, String packinfoFilePath,Map<String,Object> packDataMap,String arrowInfo);
}
