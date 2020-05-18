package com.inforbus.gjk.simulation.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.simulation.feign.factory.SimulationExternalInfServiceFallbackFactory;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 第三方接口
 * @Author: zhanghongxu
 * @CreateDate: 2020/4/21$ 9:58$
 * @UpdateUser:
 * @UpdateDate: 2020/4/21$ 9:58$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE,url = "localhost:8080/dataCenter/fileServe" )
public interface SimulationExternalInfService {
    /**
     * @Author wang
     * @Description: 解析xml文件为xmlEntity对象
     * @Param: [localPath] 文件的绝对路径
     * @Return: boolean
     * @Create: 2020/4/13
     */
    @PostMapping("/analysisXmlFile")
    public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath);

    /**
     * @Author wang
     * @Description: 在指定位置生成xml文件
     * @Param: [xMlEntityMapVO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
    @PostMapping("/createXMLFile")
    public R<Boolean> createXMLFile(@RequestBody XMlEntityMapVO xMlEntityMapVO);

    /**
     * @param filePaths 文件全路径
     * @Title: downloadFile
     * @Desc 下载多文件返回流
     * @Author xiaohe
     * @DateTime 2020年4月15日
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/downloadStreamFiles")
    public Response downloadFile(@RequestParam("filePaths") String[] filePaths);


}
