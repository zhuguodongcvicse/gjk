package com.inforbus.gjk.dataCenter.service;

import java.io.IOException;

public interface AppSubassemblyService {

    /**
     * 判断文件是否存在
     * @param filePath
     * @auther sunchao
     * @return
     * @throws IOException
     */
    Boolean judgeFileExist(String filePath);

    /**
     * 查找App路径
     * @param filePath
     * @param selectFileName
     * @auther sunchao
     * @return
     * @throws IOException
     */
    String getAppPath(String filePath, String selectFileName) throws IOException;
}
