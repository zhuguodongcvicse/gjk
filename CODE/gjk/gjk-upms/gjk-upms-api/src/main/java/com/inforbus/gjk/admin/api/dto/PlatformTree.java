
package com.inforbus.gjk.admin.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author geng_hxian
 * @since 2019/4/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformTree extends TreeNodePl {
	private String name;
	private String label;
	private String filePath;

}
