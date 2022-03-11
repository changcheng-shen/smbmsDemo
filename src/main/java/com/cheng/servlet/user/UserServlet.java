package com.cheng.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.cheng.pojo.Role;
import com.cheng.pojo.User;
import com.cheng.service.role.RoleServiceImpl;
import com.cheng.service.user.UserService;
import com.cheng.service.user.UserServiceImpl;
import com.cheng.util.Constants;
import com.cheng.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        // 一个userServlet复用
        if(method != null && method.equals("savepwd")){
            this.updatePwd(req,resp);
        }else if(method != null && method.equals("pwdmodify")){
            this.verifyOldPwd(req, resp);
        }else if(method != null && method.equals("query")){
            this.query(req, resp);
        }else if(method != null && method.equals("add")){
            this.add(req, resp);

        }
    }

    private void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("add()================");
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if(userService.add(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }

    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 获取页面的数据
            // 1、用户姓名
        String queryName = req.getParameter("queryName");
            // 2、用户角色
        String tmp = req.getParameter("queryUserRole");
            // 3、当前页码
        String pageIndex = req.getParameter("pageIndex");

        // 调用函数所需要的参数
        int queryUserRole = 0;
        int pageSize = Constants.pageSize;
        int currentPageNo = 1;

        // 检查从前端传回的数据
        if(queryName == null){
            queryName = "";
        }
        if(tmp != null && !tmp.equals("")){
            queryUserRole = Integer.parseInt(tmp);
        }
        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                resp.sendRedirect("error.jsp");
            }
        }

        // 获取用户总数
        UserServiceImpl userService = new UserServiceImpl();
        int totalUserCount = userService.getUserCount(queryName, queryUserRole);

        // 获取总页数 : 用这个专门的类感觉有点鸡肋
//        PageSupport pageSupport = new PageSupport();
//        pageSupport.setCurrentPageNo(currentPageNo);
//        pageSupport.setPageSize(pageSize);
//        pageSupport.setTotalCount(totalUserCount);
//        int totalPageCount = pageSupport.getTotalPageCount();

        int totalPageCount = totalUserCount / pageSize + 1;

        // 控制首页和尾页 防止检索越界
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }

        // 准备工作就绪 查找前端页面需要的数据
        List<User> userList = userService.getUserList(queryName, queryUserRole, currentPageNo, pageSize);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        // 将从数据库中找到的数据转给前端
        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalPageCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);

    }

    private void verifyOldPwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从session中获取用户id
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        // 获取用户输入的新密码 : req.getParameter
        String oldpassword = req.getParameter("oldpassword");
        // 用map存要返回给前端的数据
        HashMap<String, String> resultMap = new HashMap<>();

        if(user == null){// 1、session过期
            resultMap.put("result", "sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){// 2、旧密码输入为空
            resultMap.put("result", "error");
        }else {
            String sessionPwd = user.getUserPassword();
            if(oldpassword.equals(sessionPwd)){
                resultMap.put("result", "true");// 3、旧密码验证成功
            }else {
                resultMap.put("result", "false");// 4、旧密码验证失败
            }
        }

        // 把map中的数据转化为json格式传输
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));//以json形式写回
        writer.flush();
        writer.close();
    }


    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从session中获取用户id
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        // 获取用户输入的新密码 : req.getParameter
        String password = req.getParameter("newpassword");

        if(user != null && !StringUtils.isNullOrEmpty(password)){
            UserServiceImpl userService = new UserServiceImpl();

            boolean flag = userService.updatePwd(user.getId(), password);
            if(flag){
                req.setAttribute(Constants.SYS_MESSAGE, "修改密码成功，请退出并使用新密码重新登录！");
                // 移除原来的session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        }else{
            req.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }

        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);

    }

}
