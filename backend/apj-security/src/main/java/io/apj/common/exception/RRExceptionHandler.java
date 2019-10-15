package io.apj.common.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.apj.common.utils.RD;

/**
 * 异常处理器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public ResponseEntity<Object> handleRRException(RRException e){
//		R r = new R();
//		r.put("code", e.getCode());
//		r.put("msg", e.getMessage());
		return RD.INTERNAL_SERVER_ERROR(e.getMessage());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return RD.success(RD.build().put("status","404"));
//		return R.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return RD.FORBIDDEN("", "数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<Object> handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return RD.UNAUTHORIZED("UNAUTHORIZED", "没有权限，请联系管理员授权");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception e){
		logger.error(e.getMessage(), e);
		return RD.error();
	}
}
