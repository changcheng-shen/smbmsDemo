package com.cheng.dao.user;

import com.cheng.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 实现登录功能(能查找定位到这个用户)
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改用户密码(修改密码的update语句所用到的参数)
    public int updatePwd(Connection connection, int id, String password) throws SQLException;

    // 查找用户人数
    public int getUserCount(Connection connection, String userName, int userRole)throws SQLException;

    //获取用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)throws Exception;

    public int add(Connection connection, User user)throws Exception;
}
