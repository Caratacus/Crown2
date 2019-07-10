package org.crown.project.monitor.quartz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.crown.framework.mapper.BaseMapper;
import org.crown.project.monitor.quartz.domain.Job;

/**
 * <p>
 * 定时任务 Mapper 接口
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {

}
