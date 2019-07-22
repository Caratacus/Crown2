package org.crown.framework.mybatisplus.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * fastjson JSONObject与mybatis数据转换
 *
 * @author Caratacus
 * @see JSONObject
 */
@MappedTypes({JSONObject.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JSONObjectTypeHandler extends BaseTypeHandler<JSONObject> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        if (Objects.nonNull(parameter)) {
            ps.setString(i, parameter.toJSONString());
        } else {
            ps.setString(i, "{}");
        }
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return parseObject(rs.getString(columnName));
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseObject(rs.getString(columnIndex));
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return parseObject(cs.getString(columnIndex));
    }

    private JSONObject parseObject(String text) {
        try {
            return JSON.parseObject(text);
        } catch (Exception ignored) {
        }
        return new JSONObject();
    }

}