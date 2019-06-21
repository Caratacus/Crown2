package com.ruoyi.project.monitor.operlog.service;

import java.util.List;

import com.ruoyi.framework.service.BaseService;
import com.ruoyi.project.monitor.operlog.domain.OperLog;

/**
 * 操作日志 服务层
 *
 * @author ruoyi
 */
public interface IOperLogService extends BaseService<OperLog> {

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    List<OperLog> selectOperLogList(OperLog operLog);

}
