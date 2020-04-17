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

package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;

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
public class DisposeDataCenterServiceFallbackImpl implements DisposeDataCenterServiceFeign {
	@Setter
	private Throwable cause;
	
	 /**
     * @Author wang
     * @Description: 解析xml文件为xmlEntity对象
     * @Param: [localPath] 文件的绝对路径
     * @Return: boolean
     * @Create: 2020/4/13
     */
	@Override
	public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(String localPath) {
		log.error("调用数据中心的feign接口analysisXmlFileToXMLEntityMap方法失败", cause);
		R r = new R();
		r.setCode(CommonConstants.FAIL);
		r.setMsg(ServiceNameConstants.DATACENDER_SERVICE+"服务器异常，请联系管理员");
		return r;
	}

	 /**
     * @Author wang
     * @Description: 在指定位置生成xml文件
     * @Param: [xMlEntityMapVO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
	@Override
	public R<Boolean> createXMLFile(XMlEntityMapVO xMlEntityMapVO) {
		log.error("调用数据中心的feign接口createXMLFile方法失败", cause);
		R r = new R();
		r.setCode(CommonConstants.FAIL);
		r.setMsg(ServiceNameConstants.DATACENDER_SERVICE+"服务器异常，请联系管理员");
		return r;
	}
}
