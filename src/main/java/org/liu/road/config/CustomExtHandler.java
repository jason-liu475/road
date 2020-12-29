package org.liu.road.config;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExtHandler {
	/**
	 * 捕获全局异常，处理所有不可知的异常
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public Object handleException(Exception e, HttpServletRequest request) {
		log.error("exception {}", e);
		log.error("url {}, msg {}", request.getRequestURL(), e.getMessage());
		return JSONUtil.parseObj("error:{" + e.getMessage() + "}");
	}
}