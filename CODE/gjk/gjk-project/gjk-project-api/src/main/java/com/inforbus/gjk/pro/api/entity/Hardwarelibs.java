package com.inforbus.gjk.pro.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Hardwarelibs {

    /**
     * id
     */
    //@TableId(value = "id", type = IdType.ID_WORKER_STR)
    @TableId
    private String id;
    /**
     * 项目名
     */
    private String hardwareName;
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 模型id
     */
    private String modelId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 连线
     */
    private String link;
    /**
     * 说明
     */
    private String description;
    /**
     * 逻辑删除
     */
    private String delFlag;
    /**
     * 版本
     */
    private String version;

    /**
     * 正面json
     */
    private String frontJson;

    /**
     * 反面json
     */
    private String backJson;

    /**
     * 连线关系
     */
    private String linkRelation;

    /**
     * 部署图需要json
     */
    private String frontCaseForDeployment;
}
