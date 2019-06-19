package com.ruoyi.project.tool.gen.domain;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ry 数据库表
 *
 * @author ruoyi
 */
@Setter
@Getter
public class TableInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 表的主键列信息
     */
    private ColumnInfo primaryKey;

    /**
     * 表的列名(不包含主键)
     */
    private List<ColumnInfo> columns;

    /**
     * 类名(第一个字母大写)
     */
    private String className;

    /**
     * 类名(第一个字母小写)
     */
    private String classname;

    public ColumnInfo getColumnsLast() {
        ColumnInfo columnInfo = null;
        if (StringUtils.isNotNull(columns) && columns.size() > 0) {
            columnInfo = columns.get(0);
        }
        return columnInfo;
    }

    @Override
    public String toString() {
        return "TableInfo [tableName=" + tableName + ", tableComment=" + tableComment + ", primaryKey=" + primaryKey
                + ", columns=" + columns + ", className=" + className + ", classname=" + classname + "]";
    }
}
