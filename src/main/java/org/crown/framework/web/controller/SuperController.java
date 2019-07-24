/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.crown.framework.web.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.crown.common.cons.PageCons;
import org.crown.common.utils.DateUtils;
import org.crown.common.utils.StringUtils;
import org.crown.common.utils.TypeUtils;
import org.crown.common.utils.sql.AntiSQLFilter;
import org.crown.framework.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * SuperController
 *
 * @author Caratacus
 */
@Slf4j
public class SuperController<Entity> implements PageCons {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 成功返回
     *
     * @param object
     * @return
     */
    public <T> ApiResponses<T> success(T object) {
        return ApiResponses.<T>success(response, object);
    }

    /**
     * 成功返回
     *
     * @return
     */
    public ApiResponses<Void> success() {
        return success(HttpStatus.OK);
    }

    /**
     * 成功返回
     *
     * @param status
     * @param object
     * @return
     */
    public <T> ApiResponses<T> success(HttpStatus status, T object) {
        return ApiResponses.<T>success(response, status, object);
    }

    /**
     * 成功返回
     *
     * @param status
     * @return
     */
    public ApiResponses<Void> success(HttpStatus status) {
        return ApiResponses.<Void>success(response, status);
    }

    /**
     * 获取分页对象
     *
     * @return
     */
    protected <T> Page<T> getPage() {
        return getPage(false);
    }

    /**
     * 获取分页对象
     *
     * @param openSort
     * @return
     */
    protected <T> Page<T> getPage(boolean openSort) {
        int index = 1;
        // 页数
        Integer cursor = TypeUtils.castToInt(request.getParameter(PAGE_NUM), index);
        // 分页大小
        Integer limit = TypeUtils.castToInt(request.getParameter(PAGE_SIZE), DEFAULT_PAGE_SIZE);
        // 是否查询分页
        Boolean searchCount = TypeUtils.castToBoolean(request.getParameter(SEARCH_COUNT), true);
        limit = limit > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : limit;
        Page<T> page = new Page<>(cursor, limit, searchCount);
        if (openSort) {
            String pageSort = getParameterSafeValue(PAGE_SORT);
            String pageOrder = getParameterSafeValue(PAGE_ORDER);
            if (StringUtils.isNotEmpty(pageSort)) {
                boolean isAsc = "asc".equalsIgnoreCase(pageOrder);
                addOrder(page, pageSort, isAsc);
            }
        }
        return page;
    }

    /**
     * 使用排序
     *
     * @param page
     * @param orderStr
     * @param asc
     * @param <T>
     */
    private <T> void addOrder(Page<T> page, String orderStr, boolean asc) {
        if (StringUtils.isNotEmpty(getAlias())) {
            orderStr = getAlias() + "." + orderStr;
        }
        page.addOrder(asc ? OrderItem.asc(orderStr) : OrderItem.desc(orderStr));
    }

    /**
     * 获取排序的table
     */
    protected String getAlias() {
        try {
            Class<Entity> entityClass = (Class<Entity>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
            return TableInfoHelper.getTableInfo(entityClass).getTableName();
        } catch (Exception e) {
            log.warn("获取排序别名出错:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取安全参数(SQL ORDER BY 过滤)
     *
     * @param parameter
     * @return
     */
    protected String getParameterSafeValue(String parameter) {
        return AntiSQLFilter.getSafeValue(request.getParameter(parameter));
    }
}
