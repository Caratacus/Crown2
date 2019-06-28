package org.crown.project.system.user.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.crown.common.utils.ServletUtils;
import org.crown.framework.enums.ErrorCodeEnum;
import org.crown.framework.responses.ApiResponses;
import org.crown.framework.utils.ApiAssert;
import org.crown.framework.web.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
@Slf4j
public class LoginController extends WebController {

    @GetMapping("/login")
    public String login() {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiResponses<Void> ajaxLogin(String username, String password, Boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            log.warn("用户登陆失败 {}", e.getMessage());
            ApiAssert.failure(ErrorCodeEnum.USER_USERNAME_OR_PASSWORD_IS_WRONG);
        }
        return success();
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }
}
