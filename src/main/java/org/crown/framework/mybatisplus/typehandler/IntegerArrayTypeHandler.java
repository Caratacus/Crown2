package org.crown.framework.mybatisplus.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import com.alibaba.fastjson.JSONArray;

@MappedTypes({Integer[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class IntegerArrayTypeHandler implements TypeHandler<Integer[]> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Integer[] parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null)
            ps.setNull(i, Types.VARCHAR);
        else {
            JSONArray array = new JSONArray(Arrays.asList(parameter));

            ps.setString(i, array.toString());
        }
    }

    @Override
    public Integer[] getResult(ResultSet rs, String s) throws SQLException {
        String columnValue = rs.getString(s);
        return this.getIntegerArray(columnValue);
    }

    @Override
    public Integer[] getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return this.getIntegerArray(columnValue);
    }

    @Override
    public Integer[] getResult(CallableStatement cs, int i) throws SQLException {
        String columnValue = cs.getString(i);
        return this.getIntegerArray(columnValue);

    }

    private Integer[] getIntegerArray(String columnValue) {
        if (columnValue == null) {
            return null;
        }
        JSONArray jsonArr = JSONArray.parseArray(columnValue);
        return jsonArr.toArray(new Integer[0]);
    }
}