package com.ruoyi.project.monitor.online.service;

import java.util.Date;
import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.framework.shiro.session.OnlineSessionDAO;
import com.ruoyi.project.monitor.online.domain.UserOnline;
import com.ruoyi.project.monitor.online.mapper.UserOnlineMapper;

/**
 * 在线用户 服务层处理
 *
 * @author ruoyi
 */
@Service
public class UserOnlineServiceImpl extends BaseServiceImpl<UserOnlineMapper, UserOnline> implements IUserOnlineService {

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * 通过会话序号查询信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public UserOnline selectOnlineById(String sessionId) {
        return baseMapper.selectOnlineById(sessionId);
    }

    /**
     * 查询会话集合
     *
     * @param userOnline
     */
    @Override
    public List<UserOnline> selectUserOnlineList(UserOnline userOnline) {
        return baseMapper.selectUserOnlineList(userOnline);
    }

    /**
     * 强退用户
     *
     * @param sessionId 会话ID
     */
    @Override
    public void forceLogout(String sessionId) {
        Session session = onlineSessionDAO.readSession(sessionId);
        if (session == null) {
            return;
        }
        session.setTimeout(1000);
        removeById(sessionId);
    }

    /**
     * 查询会话集合
     *
     * @param expiredDate
     */
    @Override
    public List<UserOnline> selectOnlineByExpired(Date expiredDate) {
        String lastAccessTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        return baseMapper.selectOnlineByExpired(lastAccessTime);
    }
}
