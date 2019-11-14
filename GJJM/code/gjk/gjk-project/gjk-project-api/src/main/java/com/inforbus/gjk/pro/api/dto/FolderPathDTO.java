package com.inforbus.gjk.pro.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
* 上传文件夹使用
* */
@Data
public class FolderPathDTO {
    private String amisPath;//目标地址
    private List<String> filePaths = new ArrayList<>();//文件地址集合
}
