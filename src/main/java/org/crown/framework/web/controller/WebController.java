package org.crown.framework.web.controller;

import java.util.List;

import org.crown.common.utils.StringUtils;
import org.crown.common.utils.security.ShiroUtils;
import org.crown.common.utils.sql.AntiSQLFilter;
import org.crown.framework.web.page.PageDomain;
import org.crown.framework.web.page.TableDataInfo;
import org.crown.framework.web.page.TableSupport;
import org.crown.project.system.user.domain.User;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * web层通用数据处理
 *
 * @author Crown
 */
public class WebController extends SuperController {

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.getPageDomain(request);
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = AntiSQLFilter.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    public User getSysUser() {
        return ShiroUtils.getSysUser();
    }

    public void setSysUser(User user) {
        ShiroUtils.setSysUser(user);
    }

    public Long getUserId() {
        return getSysUser().getUserId();
    }

    public String getLoginName() {
        return getSysUser().getLoginName();
    }
}
