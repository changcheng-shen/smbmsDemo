package com.cheng.service.user;

import com.cheng.pojo.User;
import java.util.List;

public interface UserService {
    // 参数大都是从前端获得！
    public User login(String userCode, String userPassword);

    public boolean updatePwd(int id, String pwd);

    public int getUserCount(String userName, int userRole);

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    public boolean add(User user);

}
