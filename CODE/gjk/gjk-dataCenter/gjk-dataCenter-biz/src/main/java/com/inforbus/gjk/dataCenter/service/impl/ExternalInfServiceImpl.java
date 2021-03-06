package com.inforbus.gjk.dataCenter.service.impl;

import SpbModel.HeadFile;
import SpbModel.Performance;
import SpbModel.Struc;
import appcontrol.AppControl;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.dataCenter.service.ExternalInfService;
import flowModel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: gjk
 * @description:
 * @author: SunChao
 * @create: 2020-04-17 14:25
 **/
@Service
public class ExternalInfServiceImpl implements ExternalInfService {
    /**
     * 构件头文件解析
     *
     * @param headerPath 头文件
     * @return 输出对象
     */
    @Override
    public HeaderFileTransVO parseHeaderFile(String headerPath) {
        HeaderFileTransVO headerFileTransVO = new HeaderFileTransVO();
        HeadFile.parseHeaderFile(headerPath, headerFileTransVO.getInputParaNameToType(),
                headerFileTransVO.getOutputParaNameToType(), headerFileTransVO.getStructTypeToMember(),
                headerFileTransVO.getIndexToParaName());
        return headerFileTransVO;
    }

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
    @Override
    public Map<Integer, Float> parsePerformanceTable(String excelPath) {
        Map<Integer, Float> performanceTable = Maps.newHashMap();
        System.out.println("excelPath" + excelPath);
        try {
            Performance.parsePerformanceTable(excelPath, performanceTable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return performanceTable;
    }

    /**
     * 软硬件映射的接口，生成缩略方案
     *
     * @param schemeFileList
     */
    @Override
    public void simplePlan(ArrayList<String> schemeFileList, String simplePlanFile) {
        try {
            SimpleScheme.createSimpleScheme(schemeFileList, simplePlanFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 调回写部署方案接口
     *
     * @param schemeFileList
     */
    @Override
    public void writeBackDeployScheme(String workModeFilePath, String schemeFile ) {
        try {
            BackNodeInfoForSpb.writeBackDeployScheme(workModeFilePath, schemeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析结构体（通用：包括系统表、内部表等）
     *
     * @param filePath 结构体文件路径
     * @return <结构体类型名，list<变量类型+空格+变量名>>
     */
    @Override
    public Map<String, List<String>> parseStruct(String filePath) {
        Map<String, List<String>> structTypeToMember = Maps.newHashMap();
        Struc.parseStruct(filePath, structTypeToMember);
        return structTypeToMember;
    }

    @Override
    public Map<String, List<String>> getCmpSysConfig(String netWorkConfigFileName, String packinfoPath,
                                                     String workModeFileName) {

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        // 调用客户api
        SystemConfig.getCmpSysConfig(netWorkConfigFileName, packinfoPath, workModeFileName, map);

        return map;
    }

    /**
     * 自定义配置调用接口
     */
    @Override
    public void createUserDefineTopic(String flowFilePath, String ThemeFilePath, String filePath) {
        try {
            TopicConfig.createUserDefineTopic(flowFilePath, ThemeFilePath, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    @Override
    public boolean appInstall(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
                              String packinfoPath, String cmpResFilePath, String appProPath) {
//		AppControl appControl = new AppControl();
////		appControl.appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath, appProPath);
//		return appControl.appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath,
//				appProPath);
        return AppControl.getAppControl().appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath, appProPath);
    }

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

    @Override
    public boolean appLoad(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
                           boolean existDeployConfig, String sysconfigPath, String appProPath) {
        return AppControl.getAppControl().appLoad(cmpNameToHwType, userName, appID, appName, existDeployConfig, sysconfigPath, appProPath);

    }

    /**
     * 卸载
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    @Override
    public boolean appUnload(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
        return AppControl.getAppControl().appUnload(cmpNameToHwType, userName, appID, appName);
    }

    /**
     * 启动
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    @Override
    public boolean appRestart(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
        return AppControl.getAppControl().appRestart(cmpNameToHwType, userName, appID, appName);
    }

    /**
     * 停止
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    @Override
    public boolean appStop(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
        return AppControl.getAppControl().appStop(cmpNameToHwType, userName, appID, appName);
    }

    /**
     * 暂停
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @return
     */
    @Override
    public boolean appPause(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
        return AppControl.getAppControl().appPause(cmpNameToHwType, userName, appID, appName);
    }

    /**
     * 注销
     *
     * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
     * @param appID           APP对应的流程ID
     * @param appName         APP名称
     * @param packinfoPath    客户自存自取路径
     * @return
     */
    @Override
    public boolean appUnInstall(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
                                String packinfoPath) {
        return AppControl.getAppControl().appUnInstall(cmpNameToHwType, userName, appID, appName, packinfoPath);
    }


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
    @Override
    public void appTaskExport(String userName, int appId, String appName, String appPath, String sysconfigPath,
                              String packinfoPath, String cmpDeployPlanFilePath) {
        AppControl.getAppControl().appTaskExport(userName,appId, appName, appPath, sysconfigPath, packinfoPath, cmpDeployPlanFilePath);
    }

    /**
     * 解析系统参数波形
     */
    @Override
    public void parseSystemPara(String flowName, String str, String xmlPath) {
        try {
            SystemPara.parseSystempPara(flowName, str, xmlPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifySpbInclude(List<String> compFuncNameList, String cmpAppFilePath) throws IOException {
        cmpProCreate.modifySpbInclude(compFuncNameList, cmpAppFilePath);
    }

    /**
     * @Title: createSpbFrameFile
     * @Description: 生成构件框架
     * @Author xiaohe
     * @DateTime 2019年11月13日 下午2:53:28
     * @param spbModelXmlFile 构件模型XML文件
     * @param spbModelDir     保存路径(可以是平台文件路径)
     */
    @Override
    public void createSpbFrameFile(String spbModelXmlFile, String spbModelDir) {
        new SpbFrameAutoGen().createSpbFrameFile(spbModelXmlFile, spbModelDir);
//		new SpbFrameAutoGen().createSpbFrameFile(spbModelXmlFile, headerTemplateFile, srcTemplateFile, saveDir);
    }

    /**
     * 完备性检查
     * @param flowFilePath
     * @return
     */
    @Override
    public CheckResult completeCheck(String flowFilePath) {
        SanityCheck sanityCheck = new SanityCheck();
        return sanityCheck.sanityChecking(flowFilePath);
    }

    /**
     * 仿真数据
     */
    @Override
    public MoniRecvDataThread startMoniRecvDataThread(String redisIP, String channelName, List<String> arrowList, String flowFilePath, String ctrlTabFilePath ){
        MoniRecvDataThread moniRecvDataThread = new MoniRecvDataThread(redisIP,channelName,arrowList,flowFilePath,ctrlTabFilePath);
        moniRecvDataThread.start();
        return moniRecvDataThread;
    }

    /**
     * 停止推送仿真数据
     */
    @Override
    public void stopMoniRecvDataThread(MoniRecvDataThread thread){
        thread.stopRunning();
    }

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
    @Override
    public Map<String, Object> parseMoniData(String flowFilePath, String packinfoFilePath, Map<String,Object> packDataMap,
                                             String arrowInfo) {
        return new ParseMoniData(flowFilePath,packinfoFilePath).parseMoniData(packDataMap, arrowInfo);

    }

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
    @Override
    public Map<String, String> getMaxXYZ(String flowFilePath, String packinfoFilePath, Map<String,Object> packDataMap, String arrowInfo) {

        return new ParseMoniData(flowFilePath,packinfoFilePath).getMaxXYZ(packDataMap, arrowInfo);
    }
}
