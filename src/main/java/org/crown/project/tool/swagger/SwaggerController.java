package org.crown.project.tool.swagger;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.crown.framework.web.controller.WebController;

/**
 * swagger 接口
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends WebController {

    @RequiresPermissions("tool:swagger:view")
    @GetMapping
    public String index() {
        return redirect("/swagger-ui.html");
    }
}
