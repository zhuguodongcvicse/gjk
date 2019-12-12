package com.inforbus.gjk.pro.api.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.entity.XmlHasParentEntity;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MakeFileUtil {

    public static final String cFile = ".c";
    public static final String cppFile = ".cpp";
    public static final String hFile = ".h";

    private static final Logger logger = LoggerFactory.getLogger(MakeFileUtil.class);

    /**
     * @param filePath     修改文件的路径
     * @param filePathList c/h/cpp/.a文件路径集合
     * @param fileType     类型
     */
    public void updateVcxprojFiltersFile(String filePath, List<String> filePathList, String fileType) {
        File file = new File(filePath);
        if (file.exists()) {
            String textStr = null;
            String lableName = null;
            if (fileType.equals(cFile)) {
                textStr = "源文件";
                lableName = "ClCompile";
            } else if (fileType.equals(hFile)) {
                textStr = "头文件";
                lableName = "ClInclude";
            }

            // 解析文件
            XmlEntityMap xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);
            XmlHasParentEntity xmlHasParentEntity = new XmlHasParentEntity(xmlEntityMap, null);
            // 获取目标节点
            XmlHasParentEntity targetXmlMap = getTargetXmlEntityMap(xmlHasParentEntity, fileType, lableName);

            // 如果查不到储存c文件的节点，查找储存cpp文件的节点
            if (targetXmlMap == null && fileType.equals(cFile)) {
                targetXmlMap = getTargetXmlEntityMap(xmlHasParentEntity, cppFile, lableName);
            }

            // 如果查不到目标节点，查找空节点
            if (targetXmlMap == null) {
                targetXmlMap = getItemGroupHasNoChild(xmlHasParentEntity);
            }

            // 如果查不到空节点，新建节点
            if (targetXmlMap == null) {
                XmlHasParentEntity projectXmlentityMap = getProjectNode(xmlHasParentEntity);
                targetXmlMap = new XmlHasParentEntity();
                targetXmlMap.setLableName("ItemGroup");
                projectXmlentityMap.getXmlHasParentEntities().add(targetXmlMap);
            }

            for (String fileName : filePathList) {
                // 判断文件扩展名
                String fileExtension = file.getName();
                if (fileExtension.endsWith(".filters")) {
                    saveFiltersFileNode(targetXmlMap, fileName, lableName, textStr);
                } else if (fileExtension.endsWith(".vcxproj")) {
                    saveVcxprojFileNode(targetXmlMap, fileName, lableName);
                }
            }

            XmlEntityMap entityMap = new XmlEntityMap(xmlHasParentEntity);

            XmlFileHandleUtil.createXmlFile(entityMap, file);

        } else {
            logger.error("需要修改的文件不存在，请检查文件路径是否正确");
        }
    }

    private XmlHasParentEntity getTargetXmlEntityMap(XmlHasParentEntity entityMap, String fileType, String lableName) {
        XmlHasParentEntity targetEntityMap = null;
        if (entityMap.getLableName().equals(lableName)) {
            if (entityMap.getAttributeMap() != null) {
                if (entityMap.getAttributeMap().containsKey("Include")) {
                    if (entityMap.getAttributeMap().get("Include").endsWith(fileType)) {
                        targetEntityMap = entityMap.getParentEntity();
                    }
                }
            }
        } else {
            if (entityMap.getXmlHasParentEntities() != null) {
                for (XmlHasParentEntity xmlEntityMap : entityMap.getXmlHasParentEntities()) {
                    targetEntityMap = getTargetXmlEntityMap(xmlEntityMap, fileType, lableName);
                    if (targetEntityMap != null) {
                        break;
                    }
                }
            }
        }
        return targetEntityMap;
    }

    /**
     * 查找project节点下的ItemGroup空节点
     *
     * @param entityMap
     * @return
     */
    private XmlHasParentEntity getItemGroupHasNoChild(XmlHasParentEntity entityMap) {
        XmlHasParentEntity targetEntityMap = null;
        if (entityMap.getLableName().equals("ItemGroup")) {
            if (entityMap.getParentEntity() != null && entityMap.getParentEntity().getLableName().equals("Project")) {
                if (entityMap.getXmlHasParentEntities() == null || entityMap.getXmlHasParentEntities().size() == 0) {
                    targetEntityMap = entityMap;
                }
            }
        } else {
            if (entityMap.getXmlHasParentEntities() != null) {
                for (XmlHasParentEntity xmlEntityMap : entityMap.getXmlHasParentEntities()) {
                    targetEntityMap = getItemGroupHasNoChild(xmlEntityMap);
                    if (targetEntityMap != null) {
                        break;
                    }
                }
            }
        }
        return targetEntityMap;
    }

    /**
     * 查找project节点
     *
     * @param entityMap
     * @return
     */
    private XmlHasParentEntity getProjectNode(XmlHasParentEntity entityMap) {
        XmlHasParentEntity targetEntityMap = null;
        if (entityMap.getLableName().equals("Project") && entityMap.getParentEntity() == null) {
            targetEntityMap = entityMap;
        } else {
            if (entityMap.getXmlHasParentEntities() != null) {
                for (XmlHasParentEntity xmlEntityMap : entityMap.getXmlHasParentEntities()) {
                    targetEntityMap = getProjectNode(xmlEntityMap);
                    if (targetEntityMap != null) {
                        break;
                    }
                }
            }
        }
        return targetEntityMap;
    }

    /**
     * <ClCompile Include="DspFrame\Src\Monitor.c"> <Filter>源文件</Filter>
     * </ClCompile>
     * <p>
     * <ClInclude Include="App\Include\AppCommon.h"> <Filter>头文件</Filter>
     * </ClInclude>
     */
    private void saveFiltersFileNode(XmlHasParentEntity entityMap, String filePath, String lableName, String textStr) {
        XmlHasParentEntity xmlEntityMap = new XmlHasParentEntity();
        xmlEntityMap.setLableName(lableName);
        Map<String, String> map = new HashMap<>();
        map.put("Include", filePath);
        xmlEntityMap.setAttributeMap(map);
        xmlEntityMap.setParentEntity(entityMap);

        List<XmlHasParentEntity> childList = new ArrayList<XmlHasParentEntity>();
        XmlHasParentEntity childXmlMap = new XmlHasParentEntity();
        childXmlMap.setLableName("Filter");
        childXmlMap.setTextContent(textStr);
        childXmlMap.setParentEntity(xmlEntityMap);
        childList.add(childXmlMap);

        xmlEntityMap.setXmlHasParentEntities(childList);
        if (entityMap.getXmlHasParentEntities() == null) {
            List<XmlHasParentEntity> list = new ArrayList<XmlHasParentEntity>();
            entityMap.setXmlHasParentEntities(list);
        }
        entityMap.getXmlHasParentEntities().add(xmlEntityMap);
    }

    /**
     * <ClCompile Include="App\Src\CmpSpbIntg.c" />
     * <p>
     * <ClInclude Include="App\Include\AppCommon.h" />
     */
    private void saveVcxprojFileNode(XmlHasParentEntity entityMap, String filePath, String lableName) {
        XmlHasParentEntity xmlEntityMap = new XmlHasParentEntity();
        xmlEntityMap.setLableName(lableName);
        Map<String, String> map = new HashMap<>();
        map.put("Include", filePath);
        xmlEntityMap.setAttributeMap(map);
        xmlEntityMap.setParentEntity(entityMap);

        if (entityMap.getXmlHasParentEntities() == null) {
            List<XmlHasParentEntity> list = new ArrayList<XmlHasParentEntity>();
            entityMap.setXmlHasParentEntities(list);
        }
        entityMap.getXmlHasParentEntities().add(xmlEntityMap);
    }

}
