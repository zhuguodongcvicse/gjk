package com.inforbus.gjk.dataCenter.service;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.api.entity.Part;
import com.inforbus.gjk.dataCenter.api.vo.ProjectFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AppSubassemblyService {

    /**
     * 判断文件是否存在
     * @param filePath
     * @auther sunchao
     * @return Boolean
     */
    Boolean judgeFileExist(String filePath);

    /**
     * 获取文件属性
     * @param filePath
     * @auther sunchao
     * @return String
     */
    String getFileProperty(String filePath, String fileProperty);

    /**
     * 查找App路径
     * @param filePath
     * @param selectFileName
     * @auther sunchao
     * @return
     * @throws IOException
     */
    String getAppPath(String filePath, String selectFileName) throws IOException;

    /**
     * 修改组件文件夹
     * @auther sunchao
     * @param r
     * @param assemblyName
     * @param part
     * @param makefileType
     * @param integerCodeFilePath
     * @param bspFilePath
     * @param sylixosProjectName
     * @param proDetailPath
     */
    void modifyAssemblyDir(R r, String assemblyName, Part part, String makefileType, String integerCodeFilePath,
                           String bspFilePath, String sylixosProjectName, String proDetailPath);

    /**
     *复制软件工程和bsp
     * @auther sunchao
     * @param r
     * @param appFilePath
     * @param partName
     * @param libsType
     * @param platformType
     * @param softwareFilePath
     * @param softwareName
     * @param bspFilePath
     * @param proDetailPath
     */
    void copySoftwareAndBsp(R r, String appFilePath, String partName, String libsType, String platformType,
                            String softwareFilePath, String softwareName, String bspFilePath, String proDetailPath);

    /**
     * 转移文件到指定目录
     * @auther sunchao
     * @param appDirPath
     * @param multipartFile
     * @throws IOException
     */
    void transferFileToDestination(String appDirPath, MultipartFile multipartFile) throws IOException;

    /**
     *创建App组件工程树
     * @auther sunchao
     * @param appPath
     * @param processId
     * @return
     */
    List<ProjectFileVO> createAppTree(String appPath, String processId);
}
