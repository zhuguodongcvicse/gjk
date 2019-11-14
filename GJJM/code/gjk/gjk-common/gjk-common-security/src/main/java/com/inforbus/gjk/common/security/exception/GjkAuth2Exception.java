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

package com.inforbus.gjk.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inforbus.gjk.common.security.component.GjkAuth2ExceptionSerializer;

import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author lengleng
 * @date 2019/2/1
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = GjkAuth2ExceptionSerializer.class)
public class GjkAuth2Exception extends OAuth2Exception {
	@Getter
	private String errorCode;

	public GjkAuth2Exception(String msg) {
		super(msg);
	}

	public GjkAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
