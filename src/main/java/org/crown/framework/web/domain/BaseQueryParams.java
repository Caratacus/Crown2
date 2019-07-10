package org.crown.framework.web.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.crown.common.utils.JacksonUtils;
import org.crown.common.utils.TypeUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.collect.Maps;

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

    public Date getBeginTime() {
        return TypeUtils.castToDate(getParams().get("beginTime"));
    }

    public Date getEndTime() {
        return TypeUtils.castToDate(getParams().get("endTime"));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + JacksonUtils.toJson(this);
    }

}
