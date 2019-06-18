package com.ruoyi.project.system.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.project.system.notice.domain.Notice;
import com.ruoyi.project.system.notice.mapper.NoticeMapper;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 * @date 2018-06-25
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements INoticeService {

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public Notice selectNoticeById(Long noticeId) {
        return baseMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<Notice> selectNoticeList(Notice notice) {
        return baseMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(Notice notice) {
        notice.setCreateBy(ShiroUtils.getLoginName());
        return baseMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(Notice notice) {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        return baseMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids) {
        return baseMapper.deleteNoticeByIds(Convert.toStrArray(ids));
    }
}
