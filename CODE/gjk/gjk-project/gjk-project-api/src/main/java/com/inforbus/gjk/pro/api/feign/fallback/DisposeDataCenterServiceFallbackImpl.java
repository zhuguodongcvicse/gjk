
package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;

import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author lengleng
 * @date 2019/2/1
 * feign token  fallback
 */
@Slf4j
@Component
public class DisposeDataCenterServiceFallbackImpl implements DisposeDataCenterServiceFeign {
    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 解析xml文件为xmlEntity对象
     * @Param: [localPath] 文件的绝对路径
     * @Return: boolean
     * @Create: 2020/4/13
     */
    @Override
    public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(String localPath) {
        log.error("调用数据中心的feign接口analysisXmlFileToXMLEntityMap方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @Author wang
     * @Description: 在指定位置生成xml文件
     * @Param: [xMlEntityMapVO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
    @Override
    public R<Boolean> createXMLFile(XMlEntityMapVO xMlEntityMapVO) {
        log.error("调用数据中心的feign接口createXMLFile方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @Author wang
     * @Description: 多文件上传熔断方法
     * @Param: [ufile, localPath]
     * @Return: com.inforbus.gjk.common.core.util.R<?>
     * @Create: 2020/5/7
     */
    @Override
    public R<?> uploadLocalFiles(MultipartFile[] ufile, String localPath) {
        log.error("调用数据中心的feign接口uploadLocalFiles方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    /**
     * @Author wang
     * @Description: 拷贝文件熔断方法
     * @Param: [source, destin]
     * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
     * @Create: 2020/5/7
     */
    @Override
    public R<Boolean> copylocalFile(String source, String destin) {
        log.error("调用数据中心的feign接口copylocalFile方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

	/**
	 * @Title: downloadStreamFiles
	 * @Desc 多文件下载（feign）
	 * @Author cvics
	 * @DateTime 2020年4月15日
	 * @param filePaths
	 * @return Response 其中包含 文件流
	 *
	 *         <pre>
	 *         Response.Body body = response.body();
	 *         InputStream inputStream = body.asInputStream();
	 *         </pre>
	 */
	@Override
	public Response downloadStreamFiles(String[] filePaths) {
		log.error("file-service 服务异常", cause);
		return null;
	}

    /**
     * @Author wang
     * @Description: 删除文件熔断方法
     * @Param: [sourcePath]
     * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
     * @Create: 2020/5/7
     */
    @Override
    public R<Boolean> delAllFile(String sourcePath) {
        log.error("调用数据中心的feign接口delAllFile方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }


}
