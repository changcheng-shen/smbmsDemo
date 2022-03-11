package com.cheng.service.user;

import com.cheng.dao.BaseDao;
import com.cheng.dao.user.UserDao;
import com.cheng.dao.user.UserDaoImpl;
import com.cheng.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements  UserService{

    // 业务层都要调用dao层 所以 在实例化UserServiceImpl时 就应该把需要调用的dao层的对象实例化出来
    // 固定写法
    private UserDao userDao = null;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String userPassword) {
        Connection connection = BaseDao.getConnection();
        User user = null;
        try {
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        // 验证密码
        if(user != null){
            //如果密码不符合 返回user为null  即查不到这个用户的信息
            if(!user.getUserPassword().equals(userPassword)){
                user = null;
            }
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) {
        Connection connection = BaseDao.getConnection();
        int updateRow = 0;
        boolean flag = false;
        try {
            updateRow = userDao.updatePwd(connection, id, pwd);
            if(updateRow > 0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String userName, int userRole) {
//        userName 和 userRole 相当于搜索框的两个选项 从前端获得
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

    @Override
    public boolean add(User user) {
        // TODO Auto-generated method stub

        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            int updateRows = userDao.add(connection,user);
            connection.commit();
            if(updateRows > 0){
                flag = true;
                System.out.println("add success!");
            }else{
                System.out.println("add failed!");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }finally{
            //在service层进行connection连接的关闭
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }



}
