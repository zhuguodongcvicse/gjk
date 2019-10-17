package com.inforbus.gjk.pro.api.dto;

import lombok.Data;
/*
项目树增加文件对象
 * */
@Data
public class FilePathDTO {
    private String oldFilePath;//原文件路径
    private String newFilePath;//增加位置路径
    private String fileName;//文件名
}
