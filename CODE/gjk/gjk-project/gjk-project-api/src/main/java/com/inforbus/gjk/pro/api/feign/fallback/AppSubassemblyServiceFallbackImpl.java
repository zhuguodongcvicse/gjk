
package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.dto.CopySoftwareAndBspDTO;
import com.inforbus.gjk.pro.api.dto.ModifyAssemblyDirDTO;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.api.feign.AppSubassemblyServiceFeign;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author sunchao
 * @date 2019/5/11
 * feign token  fallback
 */
@Slf4j
@Component
public class AppSubassemblyServiceFallbackImpl implements AppSubassemblyServiceFeign {
    @Setter
    private Throwable cause;

    @Override
    public R judgeFileExist(@RequestParam("judgeFileExist") String filePath) {
        log.error("调用数据中心的feign接口judgeFileExist方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R getFileProperty(@RequestParam("filePath") String filePath, @RequestParam("fileProperty") String fileProperty) {
        log.error("调用数据中心的feign接口getFileProperty方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R getAppPath(@RequestParam("filePath") String filePath, @RequestParam("selectFileName") String selectFileName) {
        log.error("调用数据中心的feign接口getAppPath方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R modifyAssemblyDir(@RequestBody ModifyAssemblyDirDTO modifyAssemblyDirDTO) {
        log.error("调用数据中心的feign接口modifyAssemblyDir方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R copySoftwareAndBsp(@RequestBody CopySoftwareAndBspDTO copySoftwareAndBspDTO) {
        log.error("调用数据中心的feign接口copySoftwareAndBsp方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R<String> transferFileToDestination(@RequestParam("appDirPath") String appDirPath, @RequestPart("file")  MultipartFile file) throws IOException {
        log.error("调用数据中心的feign接口transferFileToDestination方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R<List<ProjectFileVO>> createAppTree(@RequestParam("appPath") String appPath, @RequestParam("processId") String processId) {
        log.error("调用数据中心的feign接口createAppTree方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R getHardwareNodeList(@RequestParam("proDetailPath") String proDetailPath, @RequestBody List<ProjectFile> projectFiles) {
        log.error("调用数据中心的feign接口getHardwareNodeList方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R<Map<String, List<String>>> getCmpSysConfigMap(String customizeFileName, String packinfoFileName, String processFileName) {
        log.error("调用数据中心的feign接口getCmpSysConfigMap方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

}
