package org.jeecg.common.exception;

/**
 * @Description: jeecg-boot自定义异常
 * @author: jeecg-boot
 */
public class EchoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EchoException(String message){
		super(message);
	}
	
	public EchoException(Throwable cause)
	{
		super(cause);
	}
	
	public EchoException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
