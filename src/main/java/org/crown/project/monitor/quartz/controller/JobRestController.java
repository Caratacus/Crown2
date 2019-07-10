package org.crown.project.monitor.quartz.controller;

import org.crown.common.utils.StringUtils;
import org.crown.framework.model.BaseModel;
import org.crown.framework.responses.ApiResponses;
import org.crown.framework.web.controller.WebController;
import org.crown.project.monitor.quartz.domain.Job;
import org.crown.project.monitor.quartz.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
 * 定时任务 前端控制器
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Api(tags = {"Job"}, description = "定时任务相关接口")
@RestController
@RequestMapping("/v1/job")
public class JobRestController extends WebController {

    @Autowired
    private IJobService jobService;

    @ApiOperation(value = "查询定时任务", notes = "查询定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchStr", value = "任务名称", paramType = "query")
    })
    @GetMapping
    public ApiResponses<IPage<Job>> getJobs(@RequestParam(required = false, value = "searchStr") String searchStr) {
        IPage page = jobService.page(this.<Job>getPage(), Wrappers.<Job>lambdaQuery()
                .like(StringUtils.isNotBlank(searchStr), Job::getTaskName, searchStr));
        return success(page);
    }

    @ApiOperation(value = "新增定时任务", notes = "新增定时任务")
    @PostMapping
    public ApiResponses<BaseModel> create() {
       /* Job job = jobService.create(jobPARM.convert(Job.class));
        return success(job.convert(BaseModel.class));*/
        return success(new BaseModel());
    }

    @ApiOperation(value = "修改定时任务", notes = "修改定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path")
    })
    @PutMapping("/{id}")
    public ApiResponses<Void> update(@PathVariable("id") Long id) {
       /* Job job = jobPARM.convert(Job.class);
        job.setId(id);
        jobService.update(job);*/
        return success();
    }

    @ApiOperation(value = "更改定时任务状态", notes = "更改定时任务状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path")
    })
    @PutMapping(value = "/{id}/paused")
    public ApiResponses<Void> updatePaused(@PathVariable("id") Long id) {
        //   Job job = jobService.selectById(id);
        //    ApiAssert.notNull(ErrorCode.x100019, job);
        // jobService.updatePaused(job);
        return success();
    }

    @ApiOperation(value = "执行定时任务", notes = "执行定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path")
    })
    @PutMapping(value = "/{id}/run")
    public ApiResponses<Void> execution(@PathVariable("id") Long id) {
        //  Job job = jobService.selectById(id);
        //    ApiAssert.notNull(ErrorCode.x100019, job);
        //   jobService.execute(job);
        return success();
    }

    @ApiOperation(value = "删除定时任务", notes = "删除定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public ApiResponses<Void> delete(@PathVariable("id") Long id) {
        // Job job = jobService.selectById(id);
        //     ApiAssert.notNull(ErrorCode.x100019, job);
        // jobService.delete(job);
        return success();
    }

    @ApiOperation(value = "根据Id查询定时任务", notes = "根据Id查询定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path")
    })
    @GetMapping("/{id}")
    public ApiResponses<Job> getJobById(@PathVariable("id") Long id) {
        return success(jobService.getById(id));
    }

}
