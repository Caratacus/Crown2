package com.ruoyi.project.system.notice.service;

import java.util.List;

import com.ruoyi.framework.service.BaseService;
import com.ruoyi.project.system.notice.domain.Notice;

/**
 * 公告 服务层
 *
 * @author ruoyi
 */
public interface INoticeService extends BaseService<Notice> {

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    List<Notice> selectNoticeList(Notice notice);
}