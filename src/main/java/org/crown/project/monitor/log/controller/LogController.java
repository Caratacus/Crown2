package org.crown.project.monitor.log.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.crown.framework.web.controller.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 日志监控
 *
 * @author Crown
 */
@Controller
@RequestMapping("/monitor/log")
public class LogController extends WebController {

    private final String prefix = "monitor/log";

    @RequiresPermissions("monitor:log:view")
    @GetMapping
    public String server(ModelMap mmap) {
        return prefix + "/log";
    }
}
