package org.crown.project.monitor.exceLog.domain;

import org.crown.framework.web.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 异常日志表 sys_exce_log
 *
 * @author Caratacus
 */
@Setter
@Getter
public class ExceLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long id;
	/**
	 * 用户ID
	 */
	private Integer uid;
	/**
	 * Query参数
	 */
	private String parameterMap;
	/**
	 * 请求体
	 */
	private String requestbody;
	/**
	 * 请求路径
	 */
	private String url;
	/**
	 * 控制器方法
	 */
	private String actionMethod;
	/**
	 * 请求方法
	 */
	private String method;
	/**
	 * 接口运行时间 单位:ms
	 */
	private String runTime;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * IP地址
	 */
	private String ipAddr;
	/**
	 * 日志需要打印的json字符串
	 */
	private String result;

}
