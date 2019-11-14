package com.inforbus.gjk.common.core.util;

import java.io.File;
import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

/**
 * SimpleXML转换类
 * 
 * @author yin_fei
 *
 */
public class SimpleXmlUtils {

	//private static final Logger logger = LoggerFactory.getLogger(ParseXml.class);

	/**
	 * 
	 * 反序列化
	 * @param type 
	 *      	类型
	 * @param path 
	 * 			文件路径
	 * @return  
	 * 			对象
	 * @throws Exception
	 * 			转化失败抛出异常
	 */
	public static <T> T  parse(Class<? extends T> type , String path) throws Exception{
    	Serializer serializer = new Persister();
    	return serializer.read(type, new File(path));
    }
    
	/**
	 * 
	 * @param type
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static <T> T  parseText(Class<? extends T> type , String text) throws Exception{
    	Serializer serializer = new Persister();
    	return serializer.read(type, text);
    }
    
	/**
	 * 
	 * @param type
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public static <T> T  parseText(Class<? extends T> type , InputStream stream) throws Exception{
    	Serializer serializer = new Persister();
    	return serializer.read(type, stream);
    }
    
    /**
     * 反序列化
     * @param obj
     * @param xmlPath
     * @return
     * @throws Exception
     */
	public static Object paser(Object obj ,String xmlPath) throws Exception{
    	Serializer serializer = new Persister();
    	Object object = serializer.read(obj.getClass(), new File(xmlPath));
    	return object;
    }
	
	/**
	 * 序列化，将对象转化为xml
	 * 
	 * @param obj
	 * @param path
	 * @throws Exception
	 */
	public static void serialize (Object obj , String path) throws Exception{
	 	 Format format = new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
         Serializer serializer = new Persister(format); 
         File result = new File(path);
         serializer.write(obj, result);
	}
}
