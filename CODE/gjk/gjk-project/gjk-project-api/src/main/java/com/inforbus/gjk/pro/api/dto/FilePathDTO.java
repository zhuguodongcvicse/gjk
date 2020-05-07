package com.inforbus.gjk.pro.api.dto;

import lombok.Data;
/**
 * FilePathDTO
 *
 * @author wang
 * @date 2020/5/6
 * @Description 文件路径包装类
 */
@Data
public class FilePathDTO {

    //原文件路径
    private String oldFilePath;

    //增加位置路径
    private String newFilePath;

    //文件名
    private String fileName;
}
