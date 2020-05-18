package com.inforbus.gjk.dataCenter.service.impl;



import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.service.CodeGenerationService;
import com.inforbus.gjk.dataCenter.taskThread.StreamManage2;
import com.inforbus.gjk.pro.api.util.HttpClientUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * CodeGenerationServiceImpl
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成实现功能实现类
 */
@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

    /**
     * generatecode.exe所在路径
     * "D:\\14S_GJK_GIT\\gjk\\gjk\\generateCodeExe\\generatecode.exe"
     */
    @Value("${git.local.generateCodePath}")
    private String generateCodePath;

    /**
     * @Author wang
     * @Description: 集成代码生成功能方法
     * @Param: [map]
     * @Return: boolean
     * @Create: 2020/4/20
     */
    @Override
    public boolean codeGeneration(Map<String, String> map) throws IOException {
        // 获取流程下generateCodeResult文件夹路径
        String tmpGenerateCodeResult = map.get("TmpGenerateCodeResult");
        //获取流程图id
        String workModeId = map.get("WorkModeId");
        //流程模型文件的绝对路径
        String workModeFilePath = map.get("WorkModeFilePath");
        //packinfo文件路径（打包解包）
        String packinfoPath = map.get("PackinfoPath");
        //自定义主题XML文件（经过处理的路径）
        String userDefineTopicFilePath = map.get("UserDefineTopicFilePath");
        //用户名
        String username = map.get("Username");
        File file = new File(tmpGenerateCodeResult);
        if (!file.exists()) {
            file.mkdirs();
        }
        String[] strArray = new String[]{this.generateCodePath, username, workModeId, workModeFilePath, packinfoPath,
                userDefineTopicFilePath, tmpGenerateCodeResult};
        try {
            //执行generatecode.exe文件
            Process process = Runtime.getRuntime().exec(strArray);
            InputStreamReader reader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                //缓存执行exe文件后的日志数据
                stringBuffer.append(str);
            }
            if (stringBuffer == null || stringBuffer.length() == 0) {
                return false;
            }
            return true;
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * @Author wang
     * @Description: 静态检查功能
     * @Param: [filePath, fileName]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/5/18
     */
    @Override
    public R staticInspect(String filePath, String fileName) {
        Map<String, String> params = Maps.newHashMap();
        String projectKey = "s" + fileName.hashCode();
        params.put("name", fileName);
        params.put("project", projectKey);
        String url = "http://127.0.0.1:9000/api/projects/create";
        HttpResponse httpResponse = HttpClientUtil.toPost(url, params);
        if (httpResponse == null) {
            return new R<>(CommonConstants.FAIL, "sonar工具未启动", null);
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        try {
            String s = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 执行sonar-scanner命令
        // 文件所在盘符
        String diskCharacter = filePath.split(":")[0] + ":";
        String execCommand = "cmd.exe /c cd " + filePath + " && " + diskCharacter + " && "
                + JGitUtil.getSONAR_SCANNER_PATH() + "\\sonar-scanner.bat -D\"sonar.projectKey=" + projectKey
                + "\" -D\"sonar.sources=.\" -D\"sonar.host.url=http://localhost:9000\"";
        try {
            Process execResult = Runtime.getRuntime().exec(execCommand);
            // 出现error时 单个线程会阻塞
            StreamManage2 errorStream = new StreamManage2(execResult.getErrorStream(), "Error");
            StreamManage2 outputStream = new StreamManage2(execResult.getInputStream(), "Output");
            errorStream.start();
            outputStream.start();
        } catch (Exception e) {
            e.printStackTrace();
            return new R<>(CommonConstants.FAIL, "执行sonar-scanner扫描项目失败", null);
        }
        File file = new File(filePath + "/.sonar/" + fileName.hashCode() + ".pdf");
        if (file.exists()) {
            file.renameTo(new File(filePath + "/.sonar/" + fileName + "检查报告.pdf"));
        }
        return new R<>(CommonConstants.SUCCESS, "filePath + \"/.sonar/\" + fileName + \".pdf\"", projectKey);
    }
}
