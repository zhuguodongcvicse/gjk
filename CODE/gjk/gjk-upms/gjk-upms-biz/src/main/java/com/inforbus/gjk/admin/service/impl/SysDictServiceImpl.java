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

package com.inforbus.gjk.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.dto.DictType;
import com.inforbus.gjk.admin.api.dto.RoleDTO;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.admin.api.vo.DictVO;
import com.inforbus.gjk.admin.mapper.SysDictMapper;
import com.inforbus.gjk.admin.service.SysDictService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
	@Autowired
	private SysDictMapper sysDictMapper;

	/**
	 * 分页查询字典信息
	 *
	 * @param page    分页对象
	 * @param sysDict 参数列表
	 * @return
	 */
	@Override
	public IPage getDictPage(Page page, SysDict sysDict) {

		IPage IPage = baseMapper.getDictVosPage(page, sysDict);
		return IPage;
	}

	/**
	 * 查询标志字典
	 */
	@Override
	public List<DictVO> getDictValue(String type) {
		System.out.println("9999999");
		return sysDictMapper.getDictValue(type);
	}

	@Override
	public List<DictVO> getDicts(SysDict sysDict) {
		return sysDictMapper.getDictsByNameAndType(sysDict);
	}

	@Override
	public List<DictType> getDictTypes() {
		List<SysDict> types = sysDictMapper.getDictTypes();
		List<DictType> dictTypes = new ArrayList<>();
		if (types.size() > 0 && types != null) {
			for (SysDict dict : types) {
				dictTypes.add(new DictType(dict.getType(), dict.getDescription()));
			}
		}
		return dictTypes;
	}

	/**
	 * 通过字典类型与 remarks字段查找字典
	 *
	 * @param DictVO
	 * @return List<DictVO>
	 */
	@Override
	public List<DictVO> getDictsByTypeAndRemarks(DictVO dictVO) {
		return sysDictMapper.getDictsByTypeAndRemarks(dictVO);
	}

}
