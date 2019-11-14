package com.inforbus.gjk.comp.api.vo;

import java.io.File;

import com.inforbus.gjk.comp.api.entity.CompImg;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: CompFilesVO
 * @Description: 文件vo
 * @Author xiaohe
 * @DateTime 2019年7月18日 上午10:21:55
 */
public class CompFilesVO {
	@Getter
	@Setter
	private String name;
	@Getter
	private String size;
	@Getter
	private String relativePath;
	@Getter
	@Setter
	private CompImg compImg;

	public CompFilesVO(String name,  String relativePath) {
		super();
		File file = new File(relativePath);
		if (file.exists() && file.isFile()) {
			this.size = Long.toString(file.length());
		} else {
			this.size = "";
		}
		this.name = name;
		this.relativePath = relativePath;
	}

	public CompFilesVO() {
		super();
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
		File file = new File(relativePath);
		if (file.exists() && file.isFile()) {
			this.size = Long.toString(file.length());
		} else {
			this.size = "";
		}

	}

}
