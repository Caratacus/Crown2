package org.crown.framework.web.page;

import javax.servlet.http.HttpServletRequest;

import org.crown.common.cons.Constants;
import org.crown.common.utils.TypeUtils;

/**
 * 表格数据处理
 *
 * @author Crown
 */
public class TableSupport {

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain(HttpServletRequest request) {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(TypeUtils.castToInt(request.getParameter(Constants.PAGE_NUM)));
        pageDomain.setPageSize(TypeUtils.castToInt(request.getParameter(Constants.PAGE_SIZE)));
        pageDomain.setOrderByColumn(request.getParameter(Constants.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(request.getParameter(Constants.IS_ASC));
        return pageDomain;
    }

}
