package com.inforbus.gjk.dataCenter.controller;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.api.dto.CopySoftwareAndBspDTO;
import com.inforbus.gjk.dataCenter.api.dto.ModifyAssemblyDirDTO;
import com.inforbus.gjk.dataCenter.api.entity.ProjectFile;
import com.inforbus.gjk.dataCenter.api.vo.ProjectFileVO;
import com.inforbus.gjk.dataCenter.service.AppSubassemblyService;
import com.inforbus.gjk.pro.api.entity.HardwareNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appSubassemblyServer")
public class AppSubassemblyController {

    private static final Logger logger = LoggerFactory.getLogger(AppSubassemblyController.class);

    @Autowired
    private AppSubassemblyService appSubassemblyService;
    /**
     * 判断文件是否存在
     * @param filePath
     * @auther sunchao
     * @return
     */
    @PostMapping("/judgeFileExist")
    @ResponseBody
    public R judgeFileExist(@RequestParam("filePath") String filePath) {
        R<Boolean> r = new R<>();
        try {
            r.setData(appSubassemblyService.judgeFileExist(filePath));
        } catch (Exception e) {
            r.setMsg("\"判断文件是否存在失败：\" + e.getMessage()");
            logger.error("判断文件是否存在失败：" + e.getMessage());
        }
        return r;
    }

    /**
     * 获取文件属性
     * @param filePath
     * @auther sunchao
     * @return
     */
    @PostMapping("/getFileProperty")
    @ResponseBody
    public R getFileProperty(@RequestParam("filePath") String filePath, @RequestParam("fileProperty") String fileProperty) {
        R<String> r = new R<>();
        try {
            r.setData(appSubassemblyService.getFileProperty(filePath, fileProperty));
        } catch (Exception e) {
            r.setMsg("\"获取文件属性失败：\" + e.getMessage()");
            logger.error("获取文件属性失败：" + e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    /**
     *获取app文件路径
     * @auther sunchao
     * @param filePath
     * @param selectFileName
     * @return
     */
    @PostMapping("/getAppPath")
    @ResponseBody
    public R getAppPath(@RequestParam("filePath") String filePath, @RequestParam("selectFileName") String selectFileName) {
        R<String> r = new R<>();
        try {
            r.setData(appSubassemblyService.getAppPath(filePath, selectFileName));
        } catch (Exception e) {
            r.setMsg("\"获取APP路径失败：\" + e.getMessage()");
            logger.error("获取APP路径失败：" + e.getMessage());
        }
        return r;
    }

    /**
     *修改组件文件夹
     * @auther sunchao
     * @param modifyAssemblyDirDTO
     * @return
     */
    @PostMapping("/modifyAssemblyDir")
    @ResponseBody
    public R modifyAssemblyDir(@RequestBody ModifyAssemblyDirDTO modifyAssemblyDirDTO) {
        System.out.println(modifyAssemblyDirDTO);
        R<String> r = new R<>();
        appSubassemblyService.modifyAssemblyDir(r, modifyAssemblyDirDTO.getAssemblyName(), modifyAssemblyDirDTO.getPart(),
                modifyAssemblyDirDTO.getMakefileType(), modifyAssemblyDirDTO.getIntegerCodeFilePath(), modifyAssemblyDirDTO.getBspFilePath(),
                modifyAssemblyDirDTO.getSylixosProjectName(), modifyAssemblyDirDTO.getProDetailPath());
        return r;
    }

    /**
     *复制软件工程和bsp
     * @auther sunchao
     * @param copySoftwareAndBspDTO
     * @return
     */
    @PostMapping("/copySoftwareAndBsp")
    @ResponseBody
    public R copySoftwareAndBsp(@RequestBody CopySoftwareAndBspDTO copySoftwareAndBspDTO) {
        System.out.println(copySoftwareAndBspDTO);
        R<String> r = new R<>();
        appSubassemblyService.copySoftwareAndBsp(r, copySoftwareAndBspDTO.getAppFilePath(), copySoftwareAndBspDTO.getPartName(),
                copySoftwareAndBspDTO.getLibsType(), copySoftwareAndBspDTO.getSoftwareFilePath(),
                copySoftwareAndBspDTO.getSoftwareName(), copySoftwareAndBspDTO.getBspFilePath(), copySoftwareAndBspDTO.getProDetailPath());
        return r;
    }

    /**
     * 转移文件到指定目录
     * @auther sunchao
     * @param appDirPath
     * @param file
     * @return
     */
    @PostMapping(value = "/transferFileToDestination", produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public R transferFileToDestination(@RequestParam("appDirPath") String appDirPath, @RequestPart("file")  MultipartFile file) {
        R<String> r = new R<>();
        try {
            appSubassemblyService.transferFileToDestination(appDirPath, file);
        } catch (IOException e) {
            r.setMsg("\"转移文件失败：\" + e.getMessage()");
            logger.error("转移文件失败：" + e.getMessage());
        }
        return r;
    }

    /**
     *创建App组件工程树
     * @auther sunchao
     * @param appPath
     * @param processId
     * @return
     */
    @PostMapping("/createAppTree")
    @ResponseBody
    public R createAppTree(@RequestParam("appPath") String appPath, @RequestParam("processId") String processId) {
        R<List<ProjectFileVO>> r = new R<>();
        List<ProjectFileVO> appTree = appSubassemblyService.createAppTree(appPath, processId);
        r.setData(appTree);
        return r;
    }

    /**
     * 获取硬件节点列表
     * @param proDetailPath
     * @param projectFiles
     * @return
     */
    @PostMapping("/getHardwareNodeList")
    @ResponseBody
    public R<List<HardwareNode>> getHardwareNodeList(@RequestParam("proDetailPath") String proDetailPath, @RequestBody List<ProjectFile> projectFiles) {
        R<List<HardwareNode>> r = new R<>();
        try {
            List<HardwareNode> hardwareNodeList = appSubassemblyService.getHardwareNodeList(proDetailPath, projectFiles);
            r.setData(hardwareNodeList);
            r.setCode(CommonConstants.SUCCESS);
        } catch (FileNotFoundException e) {
            r.setMsg(e.getMessage());
            r.setCode(CommonConstants.FAIL);
            return r;
        }
        return r;
    }

    /**
     * 获取客户api的返回值
     * @param customizeFileName
     * @param packinfoFileName
     * @param processFileName
     * @return
     */
    @PostMapping("/getCmpSysConfigMap")
    @ResponseBody
    public R<Map<String, List<String>>> getCmpSysConfigMap(@RequestParam("customizeFileName") String customizeFileName,
                                                           @RequestParam("packinfoFileName") String packinfoFileName,
                                                           @RequestParam("processFileName") String processFileName) {
        R<Map<String, List<String>>> r = new R<>();
        try {
            Map<String, List<String>> cmpSysConfigMap = appSubassemblyService.getCmpSysConfigMap(customizeFileName, packinfoFileName, processFileName);
            r.setData(cmpSysConfigMap);
        } catch (Exception e) {
            r.setException(e);
        }
        return r;
    }
}
