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

package com.inforbus.gjk.admin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.dto.DictType;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.admin.api.vo.DictVO;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysDictService extends IService<SysDict> {
	/**
	 * 分页查询角色信息
	 *
	 * @param page    分页对象
	 * @param roleDTO 参数列表
	 * @return
	 */
	IPage getDictPage(Page page, SysDict sysDict);

	/**
	 * 查询标志字典
	 */
	List<DictVO> getDictValue(String type);


    List<DictVO> getDicts(SysDict sysDict);

	List<DictType> getDictTypes();

	List<DictVO> getDictsByTypeAndRemarks(DictVO dictVO);
}
