
package com.inforbus.gjk.admin.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author geng_hxian
 * @date 2019/4/16
 * 算法树
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlgorithmTree extends TreeNodeAl {
	private String name;
	private String label;
	private String filePath;
}
