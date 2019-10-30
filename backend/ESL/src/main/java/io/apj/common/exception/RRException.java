package io.apj.common.exception;

/**
 * 自定义异常
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:11:27
 */
public class RRException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String message;
    private Integer status = 500;
    
    public RRException(String msg) {
		super(msg);
		this.message = msg;
	}
    
    public RRException(String msg, Integer status) {
		super(msg);
		this.message = msg;
		this.status = status;
	}
	
	public RRException(String msg, Throwable e) {
		super(msg, e);
		this.message = msg;
	}
	
	public RRException(String msg, int code) {
		super(msg);
		this.message = msg;
		this.status = code;
	}
	
	public RRException(String msg, int code, Throwable e) {
		super(msg, e);
		this.message = msg;
		this.status = code;
	}

	public String getMsg() {
		return message;
	}

	public void setMsg(String msg) {
		this.message = msg;
	}

	public int getCode() {
		return status;
	}

	public void setCode(int code) {
		this.status = code;
	}
	
	
}
