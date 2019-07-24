package org.crown.project.system.user.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.crown.framework.responses.ApiResponses;
import org.crown.framework.web.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录验证
 *
 * @author Crown
 */
@Controller
@Slf4j
public class LoginController extends WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiResponses<Void> ajaxLogin(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return success();
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }
}
