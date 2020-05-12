package com.inforbus.gjk.dataCenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.compileutil.LinuxUtil;
import com.inforbus.gjk.common.core.util.compileutil.MakeFileUtil;
import com.inforbus.gjk.common.core.util.compileutil.SylixosUtil;
import com.inforbus.gjk.common.core.util.compileutil.WorkbenchUtil;
import com.inforbus.gjk.dataCenter.api.entity.*;
import com.inforbus.gjk.dataCenter.api.vo.ProjectFileVO;
import com.inforbus.gjk.dataCenter.controller.AppSubassemblyController;
import com.inforbus.gjk.dataCenter.service.AppSubassemblyService;
import com.inforbus.gjk.dataCenter.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service("appSubassemblyServer")
public class AppSubassemblyServiceImpl implements AppSubassemblyService {

    private static final Logger logger = LoggerFactory.getLogger(AppSubassemblyController.class);

    @Autowired
    private FileService fileService;
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
     * 获取文件属性
     * @param filePath
     * @auther sunchao
     * @return String
     */
    @Override
    public String getFileProperty(String filePath, String fileProperty) {
        File file = new File(filePath);
        List<String> pathList = new ArrayList<>();
        pathList.add(file.getName());
        pathList.add(file.getParent());

        return JSON.toJSON(pathList).toString();
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

    private void getCompCHFileAndSave(R r, Part part, String assemblyName, String includeFilePath, String srcFilePath,
                                      Set<String> hFilePathSet, Set<String> hMakeFilePathSet, Set<String> cFilePathSet,
                                      Set<String> cMakeFilePathSet, Set<String> apiNeedStringSet, List<String> compFuncNameList,
                                      List<String> selectFileExtensionList, String proDetailPath) {
        // 遍历组件中所有构件，在构件文件夹中获取所有.h .c .cpp文件路径集合
        List<Component> compList = part.getComponents();
        for (Component comp : compList) {
            compFuncNameList.add(comp.getFunctionName());
            // 查找构件文件夹中所有的.h文件
            FileUtil.getSelectStrFilePathList(hFilePathSet, proDetailPath + comp.getFunctionPath(), ".h");
            // 查找构件文件夹中所有的.c和.cpp文件
            FileUtil.getSelectStrFilePathList(cFilePathSet, proDetailPath + comp.getFunctionPath(), selectFileExtensionList);
        }

        // 将.h文件拷贝到指定路径下，并将拷贝后的文件路径按照makeFile文件路径切割并存入集合
        for (String hFilePath : hFilePathSet) {
            try {
                // TODO:makeFile文件路径待修改
                String hMakeFilePath = "." + File.separator
                        + new File(FileUtil.copyFile(hFilePath, includeFilePath, new File(hFilePath).getName()))
                        .getAbsolutePath().substring(assemblyName.length() + 1);
                hMakeFilePathSet.add(hMakeFilePath);

                // 调客户Api需要的不带后缀的文件名
                String hFileName = new File(hFilePath).getName();
                apiNeedStringSet.add(hFileName.substring(0, hFileName.lastIndexOf(".")));
            } catch (Exception e) {
                logger.error("复制.h文件错误，请联系管理员。");
                e.printStackTrace();
                r.setException(new Exception("复制" + hFilePath + "文件到" + includeFilePath + "路径下错误，请联系管理员。"));
                return;
            }
        }

        // 将.c .cpp文件拷贝到指定路径下，并将拷贝后的文件路径按照makeFile文件路径切割并存入集合
        for (String cFilePath : cFilePathSet) {
            try {
                String cMakeFilePath = "." + File.separator
                        + new File(FileUtil.copyFile(cFilePath, srcFilePath, new File(cFilePath).getName()))
                        .getAbsolutePath().substring(assemblyName.length() + 1);
                cMakeFilePathSet.add(cMakeFilePath);

                // 调客户Api需要的不带后缀的文件名
                String cFileName = new File(cFilePath).getName();
                apiNeedStringSet.add(cFileName.substring(0, cFileName.lastIndexOf(".")));
            } catch (IOException e) {
                logger.error("复制.c文件错误，请联系管理员。");
                r.setException(new Exception("复制" + cFilePath + "文件到" + srcFilePath + "路径下错误，请联系管理员。"));
                return;
            }
        }
    }

    private List<?> getListBySet(Collection c) {
        List<?> list = new ArrayList<>();
        list.addAll(c);
        return list;
    }

    private void modifyVs2010MakeFile(R r, String assemblyName, List<String> hMakeFilePathList,
                                      List<String> cMakeFilePathList) {
        // 获取makeFile文件,vs2010中的.filters文件
        Set<String> makeFileList = new HashSet<String>();
        String filtersFileName = null;
        FileUtil.getSelectStrFilePathList(makeFileList, assemblyName, ".filters");
        if (makeFileList.size() > 0) {
            List<String> list = new ArrayList<>();
            list.addAll(makeFileList);
            filtersFileName = list.get(0);
        } else {
            r.setException(
                    new Exception("查找" + new File(assemblyName).getName() + "路径下.filters文件失败，请确认选择的软件框架正确及其他配置正确。"));
            return;
        }
        // 获取makeFile文件,vs2010中的. vcxproj文件
        String vcxprojFileName = null;
        makeFileList = new HashSet<String>();
        FileUtil.getSelectStrFilePathList(makeFileList, assemblyName, ".vcxproj");
        if (makeFileList.size() > 0) {
            List<String> list = new ArrayList<>();
            list.addAll(makeFileList);
            vcxprojFileName = list.get(0);
        } else {
            r.setException(
                    new Exception("查找" + new File(assemblyName).getName() + "路径下.vcxproj文件失败，请确认选择的软件框架正确及其他配置正确。"));
            return;
        }

        // 调用makeFile工具类，传入参数makeFile文件路径、需要修改的文件路径集合、文件类型
        if (filtersFileName != null) {
            MakeFileUtil.updateVcxprojFiltersFile(filtersFileName, hMakeFilePathList, MakeFileUtil.hFile);
            MakeFileUtil.updateVcxprojFiltersFile(filtersFileName, cMakeFilePathList, MakeFileUtil.cFile);
        }
        if (vcxprojFileName != null) {
            MakeFileUtil.updateVcxprojFiltersFile(vcxprojFileName, hMakeFilePathList, MakeFileUtil.hFile);
            MakeFileUtil.updateVcxprojFiltersFile(vcxprojFileName, cMakeFilePathList, MakeFileUtil.cFile);
        }
    }

    /**
     * 修改组件文件夹
     * @auther sunchao
     * @param r
     * @param assemblyName
     * @param part
     * @param makefileType
     * @param integerCodeFilePath
     * @param bspFilePath
     * @param sylixosProjectName
     * @param proDetailPath
     */
    @Override
    public void modifyAssemblyDir(R r, String assemblyName, Part part, String makefileType, String integerCodeFilePath,
                                  String bspFilePath, String sylixosProjectName, String proDetailPath) {
        // 创建空集合，存储所有.h文件路径
        Set<String> hFilePathSet = new HashSet<String>();
        // 创建空集合，存储所有.h文件的makeFile路径
        Set<String> hMakeFilePathSet = new HashSet<String>();
        // 创建空集合，存储所有.c和.cpp文件路径
        Set<String> cFilePathSet = new HashSet<String>();
        // 创建空集合，存储所有.c和.cpp文件的makeFile路径
        Set<String> cMakeFilePathSet = new HashSet<String>();
        // 调接口时使用,储存调用客户接口所需要的数据
        Set<String> apiNeedStringSet = new HashSet<>();
        // 获取Linux编译需要的所有APP/src文件路径下的所有.c文件
        Set<String> linuxCFilePathSet = new HashSet<>();
        // 调接口时使用,存构件的函数名
        List<String> compFuncNameList = new ArrayList<String>();
        // 存储需要查找的文件的后缀集合
        List<String> selectFileExtensionList = new ArrayList<String>();
        selectFileExtensionList.add(".c");
        selectFileExtensionList.add(".cpp");

        Thread copyFile = new Thread(() -> {
            // 查找组件文件夹下文件夹名为App的文件夹路径
//			String appFilePathName = FileUtil.getSelectStrFilePath(assemblyName, "App");
            String appFilePathName = null;
            try {
//                appFilePathName = FileUtil.getAppPath(assemblyName, "App");
                appFilePathName = getAppPath(assemblyName, "App");
            } catch (Exception e) {
                logger.error("查找App路径失败，请联系管理员。");
                r.setException(new Exception("查找App路径失败，请联系管理员。"));
                return;
            }
            if (appFilePathName == null) {
                // 如果未找到App文件夹路径，在组件文件夹下创建App文件夹
                appFilePathName = assemblyName + File.separator + "App";
            }
            // 获取集成代码文件夹 文件夹路径规则:../App/Src/
            String partIntegerCodeFilePath = appFilePathName + File.separator + "Src" + File.separator;
            // 获取include文件夹 文件夹路径规则:../App/Include/Spb/
            String includeFilePath = appFilePathName + File.separator + "Include" + File.separator + "Spb"
                    + File.separator;
            // 获取src文件夹 文件夹路径规则:../App/Src/Spb/
            String srcFilePath = appFilePathName + File.separator + "Src" + File.separator + "Spb" + File.separator;

            // 复制集成代码
            Set<String> integerCodeSet = new HashSet<String>();

            FileUtil.getSelectStrFilePathList(integerCodeSet, integerCodeFilePath, "Cmp" + part.getPartName(),".c");
            try {
                for (String filepath : integerCodeSet) {
                    FileUtil.copyFile(filepath, partIntegerCodeFilePath, "CmpSpbIntg.c");
                }
            } catch (IOException e) {
                logger.error("复制集成代码失败，请联系管理员。");
                r.setException(new Exception("复制集成代码失败，请联系管理员。"));
                return;
            }

            getCompCHFileAndSave(r, part, assemblyName, includeFilePath, srcFilePath, hFilePathSet,
                    hMakeFilePathSet, cFilePathSet, cMakeFilePathSet, apiNeedStringSet, compFuncNameList,
                    selectFileExtensionList, proDetailPath);
            if (CommonConstants.FAIL.equals(r.getCode())) {
                return;
            }
            FileUtil.getSelectStrFilePathList(linuxCFilePathSet, partIntegerCodeFilePath, selectFileExtensionList);
        });
        copyFile.start();

        Thread modifyFile = new Thread(() -> {
            while (copyFile.isAlive()) {
                try {
                    Thread.sleep(500);
                    logger.info("等待文件拷贝中");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将set集合转成List集合
            List<String> hMakeFilePathList = (List<String>) getListBySet(hMakeFilePathSet);
            List<String> cMakeFilePathList = (List<String>) getListBySet(cMakeFilePathSet);
            List<String> cFilePathList = (List<String>) getListBySet(cFilePathSet);
            List<String> linuxCFilePath = (List<String>) getListBySet(linuxCFilePathSet);

            try {
                if (makefileType.trim().toLowerCase().equals("VS2010".toLowerCase())) {
                    modifyVs2010MakeFile(r, assemblyName, hMakeFilePathList, cMakeFilePathList);
                } else if (makefileType.trim().toLowerCase().equals("Workbench".toLowerCase())) {
                    WorkbenchUtil.updateWorkbench(assemblyName);
                } else if (makefileType.trim().toLowerCase().equals("Sylixos".toLowerCase())) {
                    SylixosUtil.updateSylixos(assemblyName, bspFilePath, sylixosProjectName);
                } else if (makefileType.trim().toLowerCase().startsWith("Linux".toLowerCase())) {
                    LinuxUtil.updateLinux(cFilePathList, assemblyName, ".c");
                    // LinuxUtil.updateLinux(linuxCFilePath, assemblyName, ".c");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("调用" + makefileType + "工具类修改MakeFile文件失败，请联系管理员。");
                r.setException(new Exception("调用" + makefileType + "工具类修改MakeFile文件失败，请联系管理员。"));
                return;
            }
        });
        modifyFile.start();
    }

    /**
     *复制软件工程和bsp
     * @auther sunchao
     * @param r
     * @param appFilePath
     * @param partName
     * @param libsType
     * @param platformType
     * @param softwareFilePath
     * @param softwareName
     * @param bspFilePath
     * @param proDetailPath
     */
    @Override
    public void copySoftwareAndBsp(R r, String appFilePath, String partName, String libsType, String platformType, String softwareFilePath, String softwareName, String bspFilePath, String proDetailPath) {
        if ("".equals(bspFilePath)) {
            if (libsType.equals("Sylixos") || libsType.equals("Workbench")) {
                logger.error("请配置" + libsType + "对应的bsp," + "部件" + partName + "缺少对应的BSP。");
                r.setException(new Exception("请配置" + libsType + "对应的BSP," + "部件" + partName + "缺少对应的BSP。"));
                return;
            }
        }
        Thread thread = new Thread(() -> {
            String bspFilePathStr = appFilePath + "bsp" + File.separator + platformType + File.separator + new File(bspFilePath).getName();
            File file = new File(bspFilePathStr);
            if (!file.exists()) {
                // 拷贝bsp对应的文件夹到app组件工程目录下
                file.mkdirs();
                try {
                    FileUtil.copyFile(proDetailPath + bspFilePath, file.getAbsolutePath());
                } catch (IOException e) {
                    logger.error("复制BSP文件夹错误，请联系系统管理员");
                    r.setException(new Exception("复制BSP文件夹错误，请联系系统管理员"));
                    return;
                }
            }
        });
        if (libsType.equals("Sylixos") || libsType.equals("Workbench")) {
            thread.start();
        }

        if ("".equals(softwareFilePath)) {
            logger.error(partName + "寻找软件框架错误，请配置" + libsType + "对应的软件框架。");
            r.setException(new Exception("请配置" + libsType + "对应的软件框架," + "部件" + partName + "缺少对应的软件框架。"));
            return;
        }
        try {
            // 创建根组件文件夹的名称及文件路径
            String assemblyName = appFilePath + partName;
            // 将软件框架所有子文件及文件夹拷贝到根组件文件夹中
//            FileUtil.copyFile(proDetailPath + softwareFilePath, assemblyName);
            fileService.copylocalFile(proDetailPath + softwareFilePath, assemblyName);
        } catch (Exception e) {
            logger.error("复制软件框架" + softwareName + "文件夹错误，请联系系统管理员。");
            r.setException(new Exception("复制软件框架" + softwareName + "文件夹错误，请联系系统管理员。"));
            return;
        }
    }

    /**
     * 转移文件到指定目录
     * @auther sunchao
     * @param appDirPath
     * @param multipartFile
     * @throws IOException
     */
    @Override
    public void transferFileToDestination(String appDirPath, MultipartFile multipartFile) throws IOException {
        File targetFile = new File(appDirPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        appDirPath += IdGenerate.uuid()
                + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        targetFile = new File(appDirPath);
        multipartFile.transferTo(targetFile);
    }

    /**
     *创建App组件工程树
     * @auther sunchao
     * @param appPath
     * @param processId
     * @return
     */
    @Override
    public List<ProjectFileVO> createAppTree(String appPath, String processId) {
        File appFile = new File(appPath);
        List<ProjectFileVO> tree = Lists.newArrayList();
        if (!appFile.exists()) {
            return Lists.newArrayList();
        }

        String id = IdGenerate.uuid();
        tree.add(new ProjectFileVO(id, appFile.getName(), "App组件工程", "app",
                appFile.getParentFile().getAbsolutePath(), processId, processId, "0"));
        File[] fileList = appFile.listFiles();
        for (File file : fileList) {
            if (file.getName().equals("bsp") && appFile.getName().equals("AppPro")) {
                continue;
            }
            addAppFileTree(tree, id, file, processId);
        }
        return tree;
    }

    private void addAppFileTree(List<ProjectFileVO> tree, String parentId, File file, String processId) {
        String fileId = IdGenerate.uuid();

        if (file.isDirectory()) {
            tree.add(new ProjectFileVO(fileId, file.getName(), file.getName(), "app",
                    file.getParentFile().getAbsolutePath(), parentId, processId, "0"));
            File[] childFileList = file.listFiles();
            for (File childFile : childFileList) {
                addAppFileTree(tree, fileId, childFile, processId);
            }
        } else {
            tree.add(new ProjectFileVO(fileId, file.getName(), file.getName(), "app",
                    file.getParentFile().getAbsolutePath(), parentId, processId, "1"));
        }
    }

}
