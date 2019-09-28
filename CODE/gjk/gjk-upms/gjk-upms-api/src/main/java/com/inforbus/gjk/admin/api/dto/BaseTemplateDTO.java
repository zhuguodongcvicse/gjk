package com.inforbus.gjk.admin.api.dto;

import com.inforbus.gjk.admin.api.entity.BaseTemplate;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import lombok.Data;

@Data
public class BaseTemplateDTO {
    private BaseTemplate baseTemplate;
    private XmlEntityMap xmlEntityMap;
}
