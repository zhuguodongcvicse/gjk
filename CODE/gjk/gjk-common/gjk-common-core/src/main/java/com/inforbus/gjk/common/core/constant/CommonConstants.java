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

package com.inforbus.gjk.common.core.constant;

/**
 * @author lengleng
 * @date 2019/2/1
 */
public interface CommonConstants {
	/**
	 * 删除
	 */
	String STATUS_DEL = "1";
	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单
	 */
	String MENU = "0";

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * JSON 资源
	 */
	String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "gjk-ui";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "gjk";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;
	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * 验证码前缀
	 */
	String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

	/**
	 * 树节点
	 */
	String STATUS_TREE = "-1";
	
	/**
	 * 程序文本编辑器的odecd ：utf-8
	 */
	String CODE_UTF8 = "UTF-8";
	
	/**
	 * 程序文本编辑器的CODE_UNICODE
	 */
	String CODE_UNICODE = "Unicode";
	
	/**
	 * 程序文本编辑器的CODE_UTF16
	 */
	String CODE_UTF16 = "UTF-16BE";
	
	/**
	 * 程序文本编辑器的CODE_GBK
	 */
	String CODE_GBK = "GBK";
	
	/**
	 *  APP下载路径
	 */
	String APPDOWNLOAD_FILEPATH = "APPDownload";
}
