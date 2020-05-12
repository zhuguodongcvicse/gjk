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

package com.inforbus.gjk.pro.api.feign.factory;


import com.inforbus.gjk.pro.api.feign.AppSubassemblyServiceFeign;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;
import com.inforbus.gjk.pro.api.feign.fallback.AppSubassemblyServiceFallbackImpl;
import com.inforbus.gjk.pro.api.feign.fallback.DisposeDataCenterServiceFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author sunchao
 * @date 2019/5/11
 */
@Component
public class AppSubassemblyServiceFallbackFactory implements FallbackFactory<AppSubassemblyServiceFeign> {
	@Override
	public AppSubassemblyServiceFeign create(Throwable cause) {
		AppSubassemblyServiceFallbackImpl serviceFallback = new AppSubassemblyServiceFallbackImpl();
		serviceFallback.setCause(cause);
		return serviceFallback;
	}
}
