package com.inforbus.gjk.simulation.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.simulation.feign.SimulationExternalInfService;
import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Description: 熔断器实现类
 * @Author: zhanghongxu
 * @CreateDate: 2020/4/21$ 10:03$
 * @UpdateUser:
 * @UpdateDate: 2020/4/21$ 10:03$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class SimulationExternalInfServiceImpl implements SimulationExternalInfService {

    @Setter
    private Throwable cause;

    @Override
    public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(String localPath) {
        log.error("调用数据中心的feign接口analysisXmlFileToXMLEntityMap方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R<Boolean> createXMLFile(XMlEntityMapVO xMlEntityMapVO) {
        log.error("调用数据中心的feign接口createXMLFile方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @param response  返回zip流
     * @param filePaths 文件全路径
     * @Title: downloadFile
     * @Desc 下载多文件返回流
     * @Author xiaohe
     * @DateTime 2020年4月15日
     * @return
     */
    @Override
    @ResponseBody
    @PostMapping(value = "/downloadStreamFiles")
    public Response downloadFile(@RequestParam("filePaths") String[] filePaths) {
        log.error("调用数据中心的feign接口downloadFile方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return null;
    }

}
