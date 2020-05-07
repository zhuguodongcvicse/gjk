package com.inforbus.gjk.pro.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * FolderPathDTO
 *
 * @author wang
 * @date 2020/5/6
 * @Description 上传文件夹包装类
 */
@Data
public class FolderPathDTO {

    //目标地址
    private String amisPath;

    //文件地址集合
    private List<String> filePaths = new ArrayList<>();
}
