package com.ruoyi.project.monitor.operlog.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.common.utils.TypeUtils;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.project.monitor.operlog.domain.OperLog;
import com.ruoyi.project.monitor.operlog.mapper.OperLogMapper;

/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
public class OperLogServiceImpl extends BaseServiceImpl<OperLogMapper, OperLog> implements IOperLogService {

    @Override
    public List<OperLog> selectOperLogList(OperLog operLog) {
        String beginTime = TypeUtils.castToString(operLog.getParams().get("beginTime"));
        String endTime = TypeUtils.castToString(operLog.getParams().get("endTime"));
        return query().like(StringUtils.isNotEmpty(operLog.getTitle()), OperLog::getTitle, operLog.getTitle())
                .eq(Objects.nonNull(operLog.getBusinessType()), OperLog::getBusinessType, operLog.getBusinessType())
                .in(CollectionUtils.isNotEmpty(operLog.getBusinessTypes()), OperLog::getBusinessType, operLog.getBusinessTypes())
                .eq(Objects.nonNull(operLog.getStatus()), OperLog::getStatus, operLog.getStatus())
                .like(StringUtils.isNotEmpty(operLog.getOperName()), OperLog::getOperName, operLog.getOperName())
                .gt(StringUtils.isNotEmpty(beginTime), OperLog::getOperTime, beginTime)
                .lt(StringUtils.isNotEmpty(endTime), OperLog::getOperTime, endTime)
                .list();
    }

}
