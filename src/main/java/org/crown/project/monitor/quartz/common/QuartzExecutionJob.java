package org.crown.project.monitor.quartz.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.crown.common.cons.Constants;
import org.crown.common.cons.QuartzCons;
import org.crown.common.utils.JacksonUtils;
import org.crown.framework.spring.ApplicationUtils;
import org.crown.project.monitor.quartz.domain.Job;
import org.crown.project.monitor.quartz.domain.JobLog;
import org.crown.project.monitor.quartz.service.IJobLogService;
import org.crown.project.monitor.quartz.service.IJobService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 *
 * @author Caratacus
 */
@Async
public class QuartzExecutionJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Job quartzJob = (Job) context.getMergedJobDataMap().get(QuartzCons.JOB_KEY_PREFIX);
        /* 获取spring bean */
        IJobService jobService = ApplicationUtils.getBean(IJobService.class);
        IJobLogService jobLogService = ApplicationUtils.getBean(IJobLogService.class);
        QuartzManage quartzManage = ApplicationUtils.getBean(QuartzManage.class);

        String jobParams = quartzJob.getJobParams();
        JobLog log = new JobLog();
        log.setJobName(quartzJob.getJobName());
        log.setClassName(quartzJob.getClassName());
        log.setJobParams(jobParams);
        long startTime = System.currentTimeMillis();
        log.setCron(quartzJob.getCron());
        try {
            JSONObject jsonObject = JacksonUtils.parseObject(jobParams);
            // 执行任务
            logger.info("任务准备执行，任务名称：{}", quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getClassName(), quartzJob.getJobId(),
                    jsonObject);
            Future<?> future = executorService.submit(task);
            future.get();
            String runTime = System.currentTimeMillis() - startTime + "ms";
            log.setRunTime(runTime);
            // 任务状态
            log.setStatus(Constants.SUCCESS);
            logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.getJobName(), runTime);
        } catch (Exception e) {
            logger.error("任务执行失败，任务名称：{}" + quartzJob.getJobName(), e);
            log.setRunTime(System.currentTimeMillis() - startTime + "ms");
            // 任务状态 0：成功 1：失败
            log.setStatus(Constants.FAIL);
            log.setException(Throwables.getStackTraceAsString(e));
            //出错就暂停任务
            quartzManage.pauseJob(quartzJob);
            //更新状态
            jobService.updatePaused(quartzJob);
        } finally {
            jobLogService.save(log);
        }
    }
}
