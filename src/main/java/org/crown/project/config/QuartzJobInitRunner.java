package org.crown.project.config;

import org.crown.project.monitor.quartz.common.QuartzManage;
import org.crown.project.monitor.quartz.domain.Job;
import org.crown.project.monitor.quartz.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jie
 * @date 2019-01-07
 */
@Component
@Slf4j
public class QuartzJobInitRunner implements ApplicationRunner {

    @Autowired
    private IJobService jobService;

    @Autowired
    private QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        log.info("--------------------注入定时任务---------------------");
        jobService.query().eq(Job::getPaused, false).list().forEach(quartzJob -> quartzManage.addJob(quartzJob));
        log.info("--------------------定时任务注入完成---------------------");
    }
}
