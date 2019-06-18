package com.ruoyi.project.monitor.online.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.project.monitor.online.domain.OnlineSession.OnlineStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 当前在线会话 sys_user_online
 *
 * @author ruoyi
 */
@Setter
@Getter
public class UserOnline extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户会话id
     */
    @TableId
    private String sessionId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * session创建时间
     */
    private Date startTimestamp;

    /**
     * session最后访问时间
     */
    private Date lastAccessTime;

    /**
     * 超时时间，单位为分钟
     */
    private Long expireTime;

    /**
     * 在线状态
     */
    private OnlineStatus status = OnlineStatus.on_line;

    /**
     * 备份的当前用户会话
     */
    @TableField(exist = false)
    private OnlineSession session;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("sessionId", getSessionId())
                .append("loginName", getLoginName())
                .append("deptName", getDeptName())
                .append("ipaddr", getIpaddr())
                .append("loginLocation", getLoginLocation())
                .append("browser", getBrowser())
                .append("os", getOs())
                .append("status", getStatus())
                .append("startTimestamp", getStartTimestamp())
                .append("lastAccessTime", getLastAccessTime())
                .append("expireTime", getExpireTime())
                .toString();
    }
}
