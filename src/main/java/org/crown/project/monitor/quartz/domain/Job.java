package org.crown.project.monitor.quartz.domain;

import org.crown.framework.web.domain.BaseEntity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author Mybatis Plus
 * @since 2019-05-29
 */
@Setter
@Getter
public class Job extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    @ApiModelProperty(notes = "任务名称")
    private String taskName;
    @ApiModelProperty(notes = "Class名称")
    private String className;
    @ApiModelProperty(notes = "参数")
    private String jobParams;
    @ApiModelProperty(notes = "cron表达式")
    private String cron;
    @ApiModelProperty(notes = "是否暂停")
    private Boolean paused;
    @ApiModelProperty(notes = "备注")
    private String description;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

}
