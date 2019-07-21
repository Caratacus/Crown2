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

@MappedTypes({String[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringArrayTypeHandler implements TypeHandler<String[]> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null)
            ps.setNull(i, Types.VARCHAR);
        else {
            JSONArray array = new JSONArray(Arrays.asList(parameter));

            ps.setString(i, array.toString());
        }
    }

    @Override
    public String[] getResult(ResultSet rs, String s) throws SQLException {

        String columnValue = rs.getString(s);
        return this.getStringArray(columnValue);
    }

    @Override
    public String[] getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return this.getStringArray(columnValue);
    }

    @Override
    public String[] getResult(CallableStatement cs, int i) throws SQLException {
        String columnValue = cs.getString(i);
        return this.getStringArray(columnValue);

    }

    private String[] getStringArray(String columnValue) {

        if (columnValue == null)
            return null;
        JSONArray jsonArr = JSONArray.parseArray(columnValue);

        return jsonArr.toArray(new String[0]);
    }
}