package com.cheng.dao;



import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    // 静态代码块 在类加载的时候执行
    static {
        init();// 初始化直接拿到连接数据库需要的参数
    }

    private static void init() {
        // 读取properties文件内容
        Properties properties = new Properties();
        // 通过类加载器读取对应的资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);// 将资源读取到properties文件中
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 拿文件
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("password");
    }


    // 获取数据库连接
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    // 查询操作
    public static ResultSet execute(Connection connection, PreparedStatement pstm, ResultSet rs,
                                    String sql, Object[] params) throws SQLException {

        pstm = connection.prepareStatement(sql);
        // 填充对应的占位符
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i+1, params[i]);
        }
        rs = pstm.executeQuery();
        return rs;
    }

    // 更新操作
    public static int execute(Connection connection, PreparedStatement pstm,
                              String sql, Object[] params) throws SQLException {
        int updateRow = 0;
        pstm = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i+1, params[i]);
        }
        updateRow = pstm.executeUpdate();
        return updateRow;
    }

    //释放资源
    public static boolean closeResource(Connection connection, PreparedStatement pstm,
                                        ResultSet rs){
        boolean flag = true;
        if(rs != null){
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(connection != null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(pstm != null){
            try {
                pstm.close();
                pstm = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
