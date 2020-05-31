package com.inforbus.gjk.admin.api.feign.fallback;

import com.inforbus.gjk.admin.api.feign.RemoteBaseTemplateService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RemoteBaseTemplateServiceFallbackImpl
 *
 * @author wang
 * @date 2020/4/14
 * @Description fegin失败回调实现类
 */
@Slf4j
@Component
public class RemoteBaseTemplateServiceFallbackImpl implements RemoteBaseTemplateService {

    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: fegin调用dataCenter中analysisXmlFileToXMLEntityMap方法，失败回调
     * @Param: [localPath]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/14
     */
    @Override
    public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(String localPath) {
        log.error("Fegin调用数据中心解析xml文件失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @Author wang
     * @Description: fegin调用dataCenter中createXMLFile方法，失败回调
     * @Param: [xMlEntityMapVO]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/14
     */
    @Override
    public R<Boolean> createXMLFile(XMlEntityMapVO xMlEntityMapVO) {
        log.error("Fegin调用数据中心生成xml文件失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @Author wang
     * @Description: fegin调用数据中心中delFile方法删除文件
     * @Param: [absolutePath]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @Override
    public R<Boolean> delFile(String absolutePath) {
        log.error("Fegin调用数据中心删除文件失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @Author wang
     * @Description: 判断文件是否存在熔断器
     * @Param: [filePath]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/5/31
     */
    @Override
    public R judgeFileExist(String filePath) {
        log.error("Fegin调用数据中心判断文件是否存在", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
