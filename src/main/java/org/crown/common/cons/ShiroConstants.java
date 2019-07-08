package org.crown.common.cons;

/**
 * Shiro通用常量
 *
 * @author ruoyi
 */
public interface ShiroConstants {

    /**
     * 用户名
     */
    String CURRENT_USERNAME = "username";

    /**
     * 当前在线会话
     */
    String ONLINE_SESSION = "online_session";

    /**
     * 验证码key
     */
    String CURRENT_CAPTCHA = "captcha";

    /**
     * 验证码开关
     */
    String CURRENT_ENABLED = "captchaEnabled";

    /**
     * 验证码类型
     */
    String CURRENT_TYPE = "captchaType";

    /**
     * 验证码
     */
    String CURRENT_VALIDATECODE = "validateCode";

    /**
     * 验证码错误
     */
    String CAPTCHA_ERROR = "captchaError";

    /**
     * 登录记录缓存
     */
    String LOGINRECORDCACHE = "loginRecordCache";

    /**
     * 系统活跃用户缓存
     */
    String SYS_USERCACHE = "sys-userCache";
}