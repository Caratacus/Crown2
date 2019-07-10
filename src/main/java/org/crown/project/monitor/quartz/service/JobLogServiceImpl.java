package org.crown.project.monitor.quartz.service;

import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.project.monitor.quartz.domain.JobLog;
import org.crown.project.monitor.quartz.mapper.JobLogMapper;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 定时任务日志 服务实现类
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Service
public class JobLogServiceImpl extends BaseServiceImpl<JobLogMapper, JobLog> implements IJobLogService {

}
