package org.crown.project.monitor.quartz.service;

import java.util.List;

import org.crown.framework.service.BaseService;
import org.crown.project.monitor.quartz.domain.JobLog;

/**
 * <p>
 * 定时任务日志 服务类
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
public interface IJobLogService extends BaseService<JobLog> {

    List<JobLog> selectJobLogList(JobLog jobLog);
}
