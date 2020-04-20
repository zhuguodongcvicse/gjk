package com.inforbus.gjk.dataCenter.service.impl;


import com.inforbus.gjk.dataCenter.service.CodeGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
