package org.crown.project.monitor.online.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.crown.common.utils.security.ShiroUtils;
import org.crown.framework.aspectj.lang.annotation.Log;
import org.crown.framework.aspectj.lang.enums.BusinessType;
import org.crown.framework.shiro.session.OnlineSessionDAO;
import org.crown.framework.web.controller.WebController;
import org.crown.framework.web.domain.AjaxResult;
import org.crown.framework.web.page.TableDataInfo;
import org.crown.project.monitor.online.service.IUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.crown.project.monitor.online.domain.OnlineSession;
import org.crown.project.monitor.online.domain.OnlineSession.OnlineStatus;
import org.crown.project.monitor.online.domain.UserOnline;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/online")
public class UserOnlineController extends WebController {

    private final String prefix = "monitor/online";

    @Autowired
    private IUserOnlineService userOnlineService;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online() {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserOnline userOnline) {
        startPage();
        List<UserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:online:batchForceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(@RequestParam("ids[]") String[] ids) {
        for (String sessionId : ids) {
            UserOnline online = userOnlineService.selectOnlineById(sessionId);
            if (online == null) {
                return error("用户已下线");
            }
            OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
            if (onlineSession == null) {
                return error("用户已下线");
            }
            if (sessionId.equals(ShiroUtils.getSessionId())) {
                return error("当前登陆用户无法强退");
            }
            onlineSession.setStatus(OnlineStatus.off_line);
            onlineSessionDAO.update(onlineSession);
            online.setStatus(OnlineStatus.off_line);
            userOnlineService.updateById(online);
        }
        return success();
    }

    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/forceLogout")
    @ResponseBody
    public AjaxResult forceLogout(String sessionId) {
        UserOnline online = userOnlineService.selectOnlineById(sessionId);
        if (sessionId.equals(ShiroUtils.getSessionId())) {
            return error("当前登陆用户无法强退");
        }
        if (online == null) {
            return error("用户已下线");
        }
        OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
        if (onlineSession == null) {
            return error("用户已下线");
        }
        onlineSession.setStatus(OnlineStatus.off_line);
        onlineSessionDAO.update(onlineSession);
        online.setStatus(OnlineStatus.off_line);
        userOnlineService.updateById(online);
        return success();
    }
}
