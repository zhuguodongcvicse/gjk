/*
 *    Copyright (c) 2018-2025, inforbus All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the inforbus.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: inforbus
 */
package com.inforbus.gjk.comp.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片
 *
 * @author xiaohe
 * @date 2019-08-02 16:32:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_comp_img")
public class CompImg extends Model<CompImg> {
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	@TableId
	private String id;
	/**
	 * 构建明细编号
	 */
	private String compDetid;
	/**
	 * 构建明细编号
	 */
	private String imgPath;
	/**
	 * 显示名字
	 */
	private String imgShowName;
	/**
	 * 图片高度
	 */
	private String imgHeight;
	/**
	 * 图片宽度
	 */
	private String imgWidth;
	/**
	 * 背景颜色
	 */
	private String imgBackcolor;
	/**
	 * 边框像素
	 */
	private String imgBorderpx;
	/**
	 * 边框粗细
	 */
	private String imgBorderbl;
	/**
	 * 边框样式
	 */
	private String imgBorderso;
	/**
	 * 边框圆角
	 */
	private String imgBorderradius;
	/**
	 * 图片html
	 */
	private String imgHtml;

}
