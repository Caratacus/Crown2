package org.crown.project.monitor.quartz.service;

import java.util.List;

import org.crown.common.utils.TypeUtils;
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

    @Override
    public List<JobLog> selectJobLogList(JobLog jobLog) {

        String beginTime = TypeUtils.castToString(jobLog.getParams().get("beginTime"));
        String endTime = TypeUtils.castToString(jobLog.getParams().get("endTime"));
     /*   return query().like(StringUtils.isNotEmpty(jobLog.getTitle()), OperLog::getTitle, operLog.getTitle())
                .eq(Objects.nonNull(jobLog.getBusinessType()), OperLog::getBusinessType, operLog.getBusinessType())
                .in(CollectionUtils.isNotEmpty(jobLog.getBusinessTypes()), OperLog::getBusinessType, operLog.getBusinessTypes())
                .eq(Objects.nonNull(jobLog.getStatus()), OperLog::getStatus, operLog.getStatus())
                .like(StringUtils.isNotEmpty(jobLog.getOperName()), OperLog::getOperName, operLog.getOperName())
                .gt(StringUtils.isNotEmpty(beginTime), OperLog::getOperTime, beginTime)
                .lt(StringUtils.isNotEmpty(endTime), OperLog::getOperTime, endTime)
                .list();*/
        return list();
    }
}
