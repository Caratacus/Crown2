package org.crown.project.monitor.quartz.task;

import org.crown.project.monitor.quartz.common.IExecuteQuartzJob;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时删除过期未支付订单
 */
@Slf4j
@Component
public class SystemOutWelcomeTask implements IExecuteQuartzJob {

    @Override
    public void execute(Long jobId, JSONObject params) {
        log.info("欢迎来到Crown.");
    }
}
