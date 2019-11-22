package io.apj.common.utils;

import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonElement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 返回数据
 * @author lixinan
 *
 */
public class RD<J extends JsonElement> extends HashMap<String, Object>{
	private static final long serialVersionUID = 1L;
	
	public RD() {};
	
	// 设置状态
	public RD(Integer status) {
		put("status", status);
	}
	
	// 设置状态、data
	public RD(Integer status, Object data) {
		put("status", status);
		put("data", data);
	}
	
	// 设置状态、code、message
	public RD(Integer status, String code, String message) {
		put("status", status);
		put("code", code);
		put("message", message);
	}
	
	// 正常返回、修改成功
	public static ResponseEntity<Object> ok(Object data) {
		return new ResponseEntity<Object>(build().put("status", 200).put("page", data), HttpStatus.OK);
	}
	
	// 正常返回、修改成功
	public static ResponseEntity<Object> success(Object data) {
		return new ResponseEntity<Object>(data, HttpStatus.OK);
	}
	
	// 设置列表
	public RD(int status, Object list, int total) {
		put("status", status);
		put("total", total);
		put("data", list);
	}
	
	// 列表返回
	public static ResponseEntity<Object> listReturn(List<?> list,int total) {
		if(list.size() > 0) {
			// 正常返回
			return new ResponseEntity<Object>(new RD<JsonElement>(200, list, total), HttpStatus.OK);
		} else {
			// 空列表
			return new ResponseEntity<Object>(new RD<JsonElement>(404, list, total), HttpStatus.OK);
		}
	}
	
	// 删除成功(204)
	public static ResponseEntity<Object> NO_CONTENT(Object data) {
		return new ResponseEntity<Object>(new RD<JsonElement>(204,data), HttpStatus.NO_CONTENT);
	}
	
	// 找不到数据(404)
	public static ResponseEntity<Object> NOT_FOUND() {
		return new ResponseEntity<Object>(new RD<JsonElement>(404), HttpStatus.NOT_FOUND);
	}
	
	// 对象不存在(400)
	public static ResponseEntity<Object> BAD_REQUEST() {
		return new ResponseEntity<Object>(new RD<JsonElement>(400), HttpStatus.BAD_REQUEST);
	}
	
	// 权限，认证(401)
	public static ResponseEntity<Object> UNAUTHORIZED(String code, String message) {
		return new ResponseEntity<Object>(new RD<JsonElement>(401, code, message),HttpStatus.UNAUTHORIZED);
	}
	
	// 数据不可用(403)
	public static ResponseEntity<Object> FORBIDDEN(String code, String message) {
		return new ResponseEntity<Object>(new RD<JsonElement>(403, code, message),HttpStatus.FORBIDDEN);
	}	
	
	// 服务器未知错误(500)
	public static ResponseEntity<Object> error() {
		return new ResponseEntity<Object>(new RD<JsonElement>(500), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// 服务器未知错误(500)
	public static ResponseEntity<Object> INTERNAL_SERVER_ERROR(String message) {
		return new ResponseEntity<Object>(new RD<JsonElement>(500).put("message",message), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// 重写    put() 方法
	public RD<JsonElement> put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	/**
	 *  获取    RD 实体
	 * @return RD object
	 */
	public static RD<JsonElement> build() {
		return new RD<JsonElement>();
	}
}
