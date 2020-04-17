/*
 * Copyright 2018 Artem Labazin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inforbus.gjk.common.core.config;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import static feign.form.ContentType.MULTIPART;

/**
 * 
 * @auther xiaodao
 * 
 * @date 2019/9/18 17:08
 * 
 */

public class FeignSpringFormEncoder extends FormEncoder {

	/**
	 * 
	 * Constructor with the default Feign's encoder as a delegate.
	 * 
	 */
	public FeignSpringFormEncoder() {
		this(new Encoder.Default());
	}

	/**
	 * Constructor with specified delegate encoder.
	 * 
	 * @param delegate delegate encoder, if this encoder couldn't encode object.
	 * 
	 */
	public FeignSpringFormEncoder(Encoder delegate) {
		super(delegate);
		val processor = (MultipartFormContentProcessor) getContentProcessor(MULTIPART);
		processor.addFirstWriter(new SpringSingleMultipartFileWriter());
		processor.addFirstWriter(new SpringManyMultipartFilesWriter());
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
		if (bodyType.equals(MultipartFile.class)) {
			MultipartFile file = (MultipartFile) object;
			Map<String, Object> data = Collections.singletonMap(file.getName(), object);
			super.encode(data, MAP_STRING_WILDCARD, template);
			return;
		} else if (bodyType.equals(MultipartFile[].class)) {
			MultipartFile[] file = (MultipartFile[]) object;
			if (file != null) {
				Map<String, Object> data = Collections.singletonMap(file.length == 0 ? "" : file[0].getName(), object);
				super.encode(data, MAP_STRING_WILDCARD, template);
				return;
			}
		}
		super.encode(object, bodyType, template);
	}
}