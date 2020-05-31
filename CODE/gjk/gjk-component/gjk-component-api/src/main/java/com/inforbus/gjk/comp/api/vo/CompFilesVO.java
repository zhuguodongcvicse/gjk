package com.inforbus.gjk.comp.api.vo;

import java.io.File;

import com.inforbus.gjk.comp.api.entity.CompImg;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: CompFilesVO
 * @Description: 文件vo
 * @Author xiaohe
 * @DateTime 2019年7月18日 上午10:21:55
 */
@Data
public class CompFilesVO {
	private String name;
	private String size;
	private String relativePath;
	private CompImg compImg;

	public CompFilesVO(String name,  String relativePath) {
		super();
		this.name = name;
		this.relativePath = relativePath;
	}

	public CompFilesVO() {
		super();
	}
}
