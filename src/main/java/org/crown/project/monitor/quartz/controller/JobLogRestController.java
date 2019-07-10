package org.crown.project.monitor.quartz.controller;

import java.util.Objects;

import org.crown.common.utils.StringUtils;
import org.crown.common.utils.TypeUtils;
import org.crown.framework.responses.ApiResponses;
import org.crown.framework.web.controller.WebController;
import org.crown.project.monitor.quartz.domain.JobLog;
import org.crown.project.monitor.quartz.service.IJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 定时任务日志 前端控制器
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Api(tags = {"JobLog"}, description = "定时任务日志相关接口")
@RestController
@RequestMapping("/v1/job-log")
public class JobLogRestController extends WebController {

    @Autowired
    private IJobLogService jobLogService;

    @ApiOperation(value = "查询定时任务日志", notes = "查询定时任务日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchStr", value = "任务名称", paramType = "query"),
            @ApiImplicitParam(name = "isSuccess", value = "是否成功", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<JobLog>> getJobLogs(@RequestParam(required = false, value = "searchStr") String searchStr,
                                                  @RequestParam(required = false, value = "isSuccess") Boolean isSuccess) {
        IPage<JobLog> jobLogPage = jobLogService.page(this.<JobLog>getPage(), Wrappers.<JobLog>lambdaQuery()
                .eq(Objects.nonNull(isSuccess), JobLog::getSuccessed, isSuccess)
                .like(StringUtils.isNotBlank(searchStr), JobLog::getTaskName, searchStr)
                .orderByAsc(JobLog::getId));
        return success(jobLogPage);
    }

    @ApiOperation(value = "根据id查看异常信息", notes = "根据id查看异常信息")
    @GetMapping("/{id}/exception")
    public ApiResponses<String> getJobLogException(@PathVariable("id") Long id) {
        String exception = jobLogService.getObj(
                Wrappers.<JobLog>lambdaQuery().select(JobLog::getException).eq(JobLog::getId, id)
                , TypeUtils::castToString
        );
        return success(exception);
    }
}
