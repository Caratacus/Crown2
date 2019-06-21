package com.ruoyi.project.system.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.StringUtils;
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

    @Override
    public List<Notice> selectNoticeList(Notice notice) {
        return query().like(StringUtils.isNotEmpty(notice.getNoticeTitle()), Notice::getNoticeTitle, notice.getNoticeTitle())
                .eq(StringUtils.isNotEmpty(notice.getNoticeType()), Notice::getNoticeType, notice.getNoticeType())
                .like(StringUtils.isNotEmpty(notice.getCreateBy()), Notice::getCreateBy, notice.getCreateBy())
                .list();
    }

}
