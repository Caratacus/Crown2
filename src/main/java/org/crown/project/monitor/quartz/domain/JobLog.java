package org.crown.project.monitor.quartz.domain;

import java.util.Date;

import org.crown.framework.web.domain.BaseQueryParams;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 定时任务日志
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Setter
@Getter
public class JobLog extends BaseQueryParams {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    @ApiModelProperty(notes = "Class名称")
    private String className;
    @ApiModelProperty(notes = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(notes = "cron表达式")
    private String cron;
    @ApiModelProperty(notes = "异常信息")
    private String exception;
    @ApiModelProperty(notes = "是否成功")
    private Boolean successed;
    @ApiModelProperty(notes = "任务名称")
    private String taskName;
    @ApiModelProperty(notes = "参数")
    private String jobParams;
    @ApiModelProperty(notes = "运行时间")
    private String runTime;

}
