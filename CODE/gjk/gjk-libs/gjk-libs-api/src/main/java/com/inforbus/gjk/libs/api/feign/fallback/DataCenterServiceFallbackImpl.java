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

package com.inforbus.gjk.libs.api.feign.fallback;

import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.feign.DataCenterServiceFeign;
import com.inforbus.gjk.libs.api.feign.RemoteTokenService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author lengleng
 * @date 2019/2/1
 * feign token  fallback
 */
@Slf4j
@Component
public class DataCenterServiceFallbackImpl implements DataCenterServiceFeign {
	@Setter
	private Throwable cause;
	
	/**
	 * 三个库读取程序文本编辑器文件
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	@Override
	public R readFiles( ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		log.error("调用数据中心的feign接口readFiles方法失败", cause);
		R r = new R();
		r.setCode(CommonConstants.FAIL);
		r.setMsg(ServiceNameConstants.DATACENDER_SERVICE+"服务器异常，请联系管理员");
		return r;
	}
	
	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 * @param filePath 文件路径
	 * @param textContext 文本内容
	 */
	@Override
	public R saveFileContext( String filePath, String textContext) {
		log.error("调用数据中心的feign接口saveFileContext方法失败", cause);
		R r = new R();
		r.setCode(CommonConstants.FAIL);
		r.setMsg(ServiceNameConstants.DATACENDER_SERVICE+"服务器异常，请联系管理员");
		return r;
	}
}
