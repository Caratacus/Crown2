package org.crown.framework.mybatisplus.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * fastjson JSONArray与mybatis数据转换
 *
 * @author Caratacus
 * @see JSONArray
 */
@MappedTypes({JSONArray.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JSONArrayTypeHandler implements TypeHandler<JSONArray> {

    @Override
    public void setParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws SQLException {
        if (Objects.nonNull(parameter)) {
            ps.setString(i, parameter.toJSONString());
        } else {
            ps.setString(i, "[]");
        }
    }

    @Override
    public JSONArray getResult(ResultSet rs, String s) throws SQLException {
        String columnValue = rs.getString(s);
        return parseArray(columnValue);
    }

    @Override
    public JSONArray getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return parseArray(columnValue);
    }

    @Override
    public JSONArray getResult(CallableStatement cs, int i) throws SQLException {
        String columnValue = cs.getString(i);
        return parseArray(columnValue);

    }

    private JSONArray parseArray(String text) {
        try {
            return JSON.parseArray(text);
        } catch (Exception ignored) {
        }
        return new JSONArray();
    }
}