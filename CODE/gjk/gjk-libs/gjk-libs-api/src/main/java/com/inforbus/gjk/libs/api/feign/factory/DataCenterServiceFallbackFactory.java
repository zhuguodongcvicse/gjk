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

package com.inforbus.gjk.libs.api.feign.factory;

import com.inforbus.gjk.libs.api.feign.DataCenterServiceFeign;
import com.inforbus.gjk.libs.api.feign.fallback.DataCenterServiceFallbackImpl;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Component
public class DataCenterServiceFallbackFactory implements FallbackFactory<DataCenterServiceFeign> {

	@Override
	public DataCenterServiceFeign create(Throwable throwable) {
		DataCenterServiceFallbackImpl dataCenterServiceFallback = new DataCenterServiceFallbackImpl();
		dataCenterServiceFallback.setCause(throwable);
		return dataCenterServiceFallback;
	}
}
