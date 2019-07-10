package org.crown.project.monitor.quartz.service;

import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.project.monitor.quartz.common.QuartzManage;
import org.crown.project.monitor.quartz.domain.Job;
import org.crown.project.monitor.quartz.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Service
public class JobServiceImpl extends BaseServiceImpl<JobMapper, Job> implements IJobService {

    @Autowired
    private QuartzManage quartzManage;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Job create(Job resources) {
        save(resources);
        quartzManage.addJob(resources);
        return resources;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        updateById(resources);
        quartzManage.updateJobCron(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaused(Job quartzJob) {
        if (quartzJob.getPaused()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setPaused(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setPaused(true);
        }
        updateById(quartzJob);
    }

    @Override
    public void execute(Job quartzJob) {
        quartzManage.runAJobNow(quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Job quartzJob) {
        quartzManage.deleteJob(quartzJob);
        removeById(quartzJob);
    }
}
