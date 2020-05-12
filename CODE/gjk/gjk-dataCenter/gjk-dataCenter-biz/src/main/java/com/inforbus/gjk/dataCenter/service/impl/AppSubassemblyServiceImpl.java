package com.inforbus.gjk.dataCenter.service.impl;

import com.inforbus.gjk.dataCenter.service.AppSubassemblyService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service("appSubassemblyServer")
public class AppSubassemblyServiceImpl implements AppSubassemblyService {

    /**
     * 判断文件是否存在
     * @param filePath
     * @auther sunchao
     * @return
     * @throws IOException
     */
    @Override
    public Boolean judgeFileExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 查找App路径
     * @param filePath
     * @param selectFileName
     * @auther sunchao
     * @return
     * @throws IOException
     */
    @Override
    public String getAppPath(String filePath, String selectFileName) throws IOException {
        //要返回的app路径
        String selectPath = null;
        //拿到目标路径的path对象
        Path path = Paths.get(filePath);
        //拿到匹配器
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher("glob:**" + FileSystems.getDefault().getSeparator() + selectFileName);
        //查找匹配的文件，转成list
        List<Path> collect = Files.walk(path)
                .filter(matcher::matches)
                .collect(Collectors.toList());
        //找到符合条件的路径
        for (Path pathEl : collect) {
            if (pathEl.toString().contains(selectFileName) && !pathEl.toString().contains("debug")) {
                selectPath = pathEl.toString();
            }
        }
        return selectPath;
    }
}
