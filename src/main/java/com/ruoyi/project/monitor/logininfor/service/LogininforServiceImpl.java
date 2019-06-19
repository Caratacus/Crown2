package com.ruoyi.project.monitor.logininfor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.common.utils.TypeUtils;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.project.monitor.logininfor.domain.Logininfor;
import com.ruoyi.project.monitor.logininfor.mapper.LogininforMapper;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class LogininforServiceImpl extends BaseServiceImpl<LogininforMapper, Logininfor> implements ILogininforService {

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor) {
        String beginTime = TypeUtils.castToString(logininfor.getParams().get("beginTime"));
        String endTime = TypeUtils.castToString(logininfor.getParams().get("endTime"));
        return query().select()
                .like(StringUtils.isNotEmpty(logininfor.getIpaddr()), Logininfor::getIpaddr, logininfor.getIpaddr())
                .eq(StringUtils.isNotEmpty(logininfor.getStatus()), Logininfor::getStatus, logininfor.getStatus())
                .like(StringUtils.isNotEmpty(logininfor.getLoginName()), Logininfor::getLoginName, logininfor.getLoginName())
                .ge(StringUtils.isNotEmpty(beginTime), Logininfor::getLoginTime, beginTime)
                .le(StringUtils.isNotEmpty(endTime), Logininfor::getLoginTime, logininfor.getParams().get("endTime"))
                .list();
    }

}
