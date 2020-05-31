/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inforbus.gjk.admin.mapper;

import java.util.List;

import com.inforbus.gjk.admin.api.dto.DictType;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.admin.api.vo.DictVO;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
	/**
	 * 分页查询字典信息
	 *
	 * @param page    分页
	 * @param dictDTO 查询参数
	 * @return list
	 */
	IPage<List<DictVO>> getDictVosPage(Page page, @Param("query") SysDict sysDict);
	
	/**
	 * 查询标志字典
	 */
	List<DictVO> getDictValue(String type);

    List<DictVO> getDictsByNameAndType(@Param("query")SysDict sysDict);

    List<SysDict> getDictTypes();

    List<DictVO> getDictsByTypeAndRemarks(DictVO dictVO);

	/**
	 * 修改平台库目录时同步修改字典表
	 * @param dictVO
	 * @return
	 */
	int syncModifyDict(DictVO dictVO);
}
