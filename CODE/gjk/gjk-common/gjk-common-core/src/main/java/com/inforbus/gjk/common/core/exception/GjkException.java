package com.inforbus.gjk.common.core.exception;

/**
 * 平台自定义异常类
 * 
 * @author wang_cmin
 * 
 */
public class GjkException extends Exception {

	private static final long serialVersionUID = 1L;
	String message = "";

	public GjkException(Throwable ex) {
		super(ex);
		String tempmessage = ex.getMessage();
		if (tempmessage != null) {
			String temp = tempmessage.replaceAll("\"", "'");
			String message = temp.replaceAll("\r|\n", "");
			this.message = message;
		} else {
			String tempcause = ex.getCause().toString();
			if (tempcause != null) {
				String temp = tempcause.replaceAll("\"", "'");
				String message = temp.replaceAll("\r|\n", "");
				this.message = message;
			}
		}
	}

	public GjkException(String message, Throwable ex) {
		super(message, ex);
		String temp = (ex.getMessage()).replaceAll("\"", "'");
		String message1 = temp.replaceAll("\r|\n", "");
		this.message = message1;
	}

	public GjkException(String message) {
		super(message);
		String temp = message.replaceAll("\"", "'");
		String message1 = temp.replaceAll("\r|\n", "");
		this.message = message1;
	}

	public String toString() {
		return message;
	}
}