package org.crown.framework.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.crown.common.exception.BusinessException;
import org.crown.common.exception.DemoModeException;
import org.crown.common.utils.ServletUtils;
import org.crown.framework.enums.ErrorCodeEnum;
import org.crown.framework.exception.ApiException;
import org.crown.framework.model.ErrorCode;
import org.crown.framework.responses.ApiResponses;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义 REST 业务异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    public ApiResponses<Void> handleBadRequest(ApiException exception) {
        ErrorCode code = exception.getErrorCode();
        if (code.getHttpCode() < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            log.info("Info: error: {} ,httpCode: {} ,msg: {}", code.getError(), code.getHttpCode(), code.getMsg());
        } else {
            log.warn("Warn: error: {} ,httpCode: {} ,msg: {}", code.getError(), code.getHttpCode(), code.getMsg());
        }
        return ApiResponses.failure(code);
    }

    /**
     * 权限校验失败 如果请求为ajax返回json，普通请求跳转页面
     */
    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(HttpServletRequest request, AuthorizationException e) {
        log.error(e.getMessage(), e);
        if (ServletUtils.isAjaxRequest(request)) {
            return ApiResponses.failure(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert());
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error/unauth");
            return modelAndView;
        }
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ApiResponses<Void> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ApiResponses.failure(ErrorCodeEnum.METHOD_NOT_ALLOWED.convert());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponses<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponses.failure(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Object businessException(HttpServletRequest request, BusinessException e) {
        log.error(e.getMessage(), e);
        if (ServletUtils.isAjaxRequest(request)) {
            return ApiResponses.failure(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert());
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("error/business");
            return modelAndView;
        }
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public ApiResponses<Void> demoModeException(DemoModeException e) {
        return ApiResponses.failure(ErrorCodeEnum.DEMO_SYSTEM_CANNOT_DO.convert());
    }
}
