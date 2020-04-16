package com.inforbus.gjk.admin.api.feign;

import com.inforbus.gjk.admin.api.feign.factory.RemoteBaseTemplateServiceFallbackFactory;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * RemoteBaseTemplateService
 *
 * @author wang
 * @date 2020/4/14
 * @Description fegin调用dataCenter提供的公共接口
 */
@FeignClient(name = ServiceNameConstants.DATACENDER_SERVICE,url = "localhost:8080/dataCenter/fileServe",fallbackFactory = RemoteBaseTemplateServiceFallbackFactory.class)
public interface RemoteBaseTemplateService {
    /**
     * @Author wang
     * @Description: 解析xml文件为xmlEntity对象
     * @Param: [localPath]
     * @Return: boolean
     * @Create: 2020/4/13
     */

    @PostMapping("/analysisXmlFile")
    public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath);

    /**
     * @Author wang
     * @Description: 早指定路径下生成xml文件
     * @Param: [xmlEntityMap, localPath]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/14
     */
    @PostMapping("/createXMLFile")
    public R<Boolean> createXMLFile(@RequestBody XMlEntityMapVO xMlEntityMapVO);

    /**
     * @Author wang
     * @Description: 根据据对路径删除文件
     * @Param: [absolutePath] 文件的绝对路径
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @PostMapping("/delFile")
    public R<Boolean> delFile(@RequestParam("absolutePath") String absolutePath);
}
