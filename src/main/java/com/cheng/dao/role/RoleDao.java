package com.cheng.dao.role;

import com.cheng.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    // 获取角色列表（具体角色数据从前端获得）
    // 获取角色列表的sql语句不需要其他参数
    // 所以这个方法就只有connection一个参数
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
