package com.inforbus.gjk.common.core.util.vo;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * XMlEntityMapVO
 *
 * @author wang
 * @date 2020/4/14
 * @Description xiaohe 2020年5月6日10:44:02 添加构造
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XMlEntityMapVO {

    private String localPath;

    private XmlEntityMap xmlEntityMap;
}
