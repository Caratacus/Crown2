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

@MappedTypes({Long[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class LongArrayTypeHandler implements TypeHandler<Long[]> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null)
            ps.setNull(i, Types.VARCHAR);
        else {
            JSONArray array = new JSONArray(Arrays.asList(parameter));

            ps.setString(i, array.toString());
        }
    }

    @Override
    public Long[] getResult(ResultSet rs, String s) throws SQLException {

        String columnValue = rs.getString(s);
        return this.getLongArray(columnValue);
    }

    @Override
    public Long[] getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return this.getLongArray(columnValue);
    }

    @Override
    public Long[] getResult(CallableStatement cs, int i) throws SQLException {
        String columnValue = cs.getString(i);
        return this.getLongArray(columnValue);

    }

    private Long[] getLongArray(String columnValue) {

        if (columnValue == null)
            return null;
        JSONArray jsonArr = JSONArray.parseArray(columnValue);
        return jsonArr.toArray(new Long[0]);
    }
}