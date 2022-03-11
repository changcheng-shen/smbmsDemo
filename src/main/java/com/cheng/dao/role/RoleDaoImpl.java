package com.cheng.dao.role;

import com.cheng.dao.BaseDao;
import com.cheng.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    // 和数据库进行交互，从数据库中拿到相应的数据
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm = null;
        String sql = "select * from smbms_role"; // 不需要参数的传递
        ResultSet rs = null;
        List<Role> list = new ArrayList<>();
        // 参数集合：虽然不需要参数 但是也不能写null  防止执行方法时会报空指针异常
        Object[] params = {};
        if(connection != null){
            // 从数据库中拿到结果
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            // 将结果存入最后的List列表中
            while (rs.next()){
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                list.add(_role);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return list;
    }
}
