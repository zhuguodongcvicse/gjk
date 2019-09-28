package com.inforbus.gjk.admin.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by wang on 2019/8/19.
 * 字典表服务类用于形成字典表类型的键值对对象
 */
@Data
public class DictType {
    private String value;//字典表中的type
    private  String label;//字典表中的type

    public DictType() {
    }

    public DictType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
