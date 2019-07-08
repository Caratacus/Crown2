package org.crown.project.system.dict.domain;

import org.crown.common.annotation.Excel;
import org.crown.framework.web.domain.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型对象 sys_dict_type
 *
 * @author ruoyi
 */
@Setter
@Getter
public class DictType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @Excel(name = "字典主键")
    @TableId
    private Long dictId;

    /**
     * 字典名称
     */
    @Excel(name = "字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型 ")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    private String remark;

}
