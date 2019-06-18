package com.ruoyi.project.monitor.online.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ruoyi.framework.mapper.BaseMapper;
import com.ruoyi.project.monitor.online.domain.UserOnline;

/**
 * 在线用户 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface UserOnlineMapper extends BaseMapper<UserOnline> {

    /**
     * 通过会话序号查询信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    UserOnline selectOnlineById(String sessionId);

    /**
     * 通过会话序号删除信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    int deleteOnlineById(String sessionId);

    /**
     * 保存会话信息
     *
     * @param online 会话信息
     * @return 结果
     */
    int saveOnline(UserOnline online);

    /**
     * 查询会话集合
     *
     * @param userOnline 会话参数
     * @return 会话集合
     */
    List<UserOnline> selectUserOnlineList(UserOnline userOnline);

    /**
     * 查询过期会话集合
     *
     * @param lastAccessTime 过期时间
     * @return 会话集合
     */
    List<UserOnline> selectOnlineByExpired(String lastAccessTime);
}
