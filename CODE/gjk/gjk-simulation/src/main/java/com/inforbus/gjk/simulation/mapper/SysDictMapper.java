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

package com.inforbus.gjk.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.admin.api.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @Description:    字典表 Mapper 接口
* @Author:         ZhangHongXu
* @CreateDate:     2020/4/9 9:18
* @UpdateUser:     ZhangHongXu
* @UpdateDate:     2020/4/9 9:18
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * @Description      * @Description
     * @Author ZhangHongXu
     * @Return
     * @Exception
     * @Date 2020/4/9 9:17
     */

    List<SysDict> getDictTypes();

}
