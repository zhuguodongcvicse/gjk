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

package com.inforbus.gjk.comp.api.feign.fallback;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.comp.api.dto.ComponentDTO;
import com.inforbus.gjk.comp.api.entity.Components;
import com.inforbus.gjk.comp.api.feign.RemoteDataCenterService;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;

import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RemoteDataCenterServiceFallbackImpl
 * @Desc
 * @Author xiaohe feign token fallback
 * @DateTime 2020年4月13日
 */
@Slf4j
@Component
public class RemoteDataCenterServiceFallbackImpl implements RemoteDataCenterService {
	@Setter
	private Throwable cause;

	@Override
	public R uploadLocalFile(MultipartFile ufile, String filePath) {
		R ret = new R<>();
		ret.setException(cause);
		ret.setMsg("file-service 服务异常 ");
		log.error("file-service 服务异常", cause);
		return ret;
	}

	@Override
	public R uploadLocalFiles(MultipartFile[] ufile, String localPath) {
		R ret = new R<>();
		ret.setException(cause);
		ret.setMsg("file-service 服务异常 ");
		log.error("file-service 服务异常", cause);
		return ret;
	}

	/**
	 * @Title: downloadStreamFiles
	 * @Desc 多文件下载（fegin）
	 * @Author cvics
	 * @DateTime 2020年4月15日
	 * @param filePaths
	 * @return Response 其中包含 文件流
	 * 
	 *         <pre>
	 *         Response.Body body = response.body();
	 *         InputStream inputStream = body.asInputStream();
	 * 
	 *         </pre>
	 * 
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#downloadStreamFiles(java.lang.String[])
	 */
	@Override
	public Response downloadStreamFiles(String[] filePaths) {
		log.error("file-service 服务异常", cause);
		return null;
	}

	/**
	 * @Title: downloadStreamFile
	 * @Desc 单文件下载（fegin）
	 * @Author cvics
	 * @DateTime 2020年4月15日
	 * @param filePath
	 * @return Response 其中包含 文件流
	 * 
	 *         <pre>
	 *         Response.Body body = response.body();
	 *         InputStream inputStream = body.asInputStream();
	 * 
	 *         </pre>
	 * 
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#downloadStreamFile(java.lang.String)
	 */
	@Override
	public Response downloadStreamFile(String filePath) {
		log.error("file-service 服务异常", cause);
		return null;
	}

	/**
	 * @Title: analysisXmlFileToXMLEntityMap
	 * @Desc 解析xml文件为xmlEntity对象
	 * @Author xiaohe
	 * @DateTime 2020年4月16日
	 * @param localPath
	 * @return
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#analysisXmlFileToXMLEntityMap(java.lang.String)
	 */
	@Override
	public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(String localPath) {
		R ret = new R<>();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常---解析XML文件 ");
		log.error("数据中心服务异常---解析XML文件", cause);
		return ret;
	}

	/**
	 * @Title: createXMLFile
	 * @Desc 在指定位置生成xml文件
	 * @Author xiaohe
	 * @DateTime 2020年4月16日
	 * @param xMlEntityMapVO
	 * @return
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#createXMLFile(com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO)
	 */
	@Override
	public R<Boolean> createXMLFile(XMlEntityMapVO xMlEntityMapVO) {
		R<Boolean> ret = new R<Boolean>();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常---生成XML文件 ");
		log.error("数据中心服务异常---生成XML文件", cause);
		return ret;
	}

	@Override
	public R delFolder(String[] folderPath) {
		R<Boolean> ret = new R<Boolean>();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常---删除文件错误 ");
		log.error("数据中心服务异常---删除文件错误", cause);
		return ret;
	}

	/**
	 * @Title: getHeader
	 * @Desc 构件头文件解析
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param maps
	 * @return
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#getHeader(java.util.Map)
	 */
	@Override
	public R<HeaderFileTransVO> getHeader(String excelPath) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常---解析头文件出错 ");
		log.error("数据中心服务异常---解析头文件错误，文件路劲为{}", excelPath, cause);
		return ret;
	}

	/**
	 * @Title: getPerformanceTable
	 * @Desc 解析性能测试表
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param maps
	 * @return
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#getPerformanceTable(java.util.Map)
	 */
	@Override
	public R<?> getPerformanceTable(String excelPath) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常---解析性能测试表格出错 ");
		log.error("数据中心服务异常---解析性能测试表格错误,文件路劲为{}", excelPath, cause);
		return ret;
	}

	@Override
	public Response downloadStreamFiles(MultipartFile ufile, String fileTarget, String filePaths) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R<Map<String, XmlEntityMap>> getCompData(Map<String, String> filePathMap) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常");
		log.error("数据中心服务异常---xml解析失败", cause);
		return ret;
	}

	@Override
	public R<List<ComponentDTO>> getCompDetailData(List<Components> componentsList) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常");
		log.error("数据中心服务异常---获取构件明细失败", cause);
		return ret;
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件
	 * 
	 * @param path 当前遍历文件或目录的路径
	 * @return 文件列表
	 * @return
	 * @see com.inforbus.gjk.comp.api.feign.RemoteDataCenterService#loopFiles(java.lang.String)
	 */
	@Override
	public R<List<CompFilesVO>> loopFiles(String filePath) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常");
		log.error("数据中心服务异常---获取构件明细失败", cause);
		return ret;
	}

	@Override
	public Response getImgFile(String path) {
		
		return null;
	}

	@Override
	public R<Boolean> delAllFile(String absolutePath) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心服务异常-删除文件失败");
		log.error("数据中心服务异常---获取构件明细失败", cause);
		return ret;
	}

	@Override
	public R<Boolean> copylocalFile(String source, String destin) {
		// TODO Auto-generated method stub
		return null;
	}

}
