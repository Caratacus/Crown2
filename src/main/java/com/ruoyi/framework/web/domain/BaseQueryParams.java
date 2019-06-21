package com.ruoyi.framework.web.domain;

import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.collect.Maps;
import com.ruoyi.common.utils.JacksonUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity基类
 *
 * @author ruoyi
 */
@Setter
@Getter
public class BaseQueryParams implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @TableField(exist = false)
    private String searchValue;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = Maps.newHashMap();
        }
        return params;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + JacksonUtils.toJson(this);
    }

}
