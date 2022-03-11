package com.cheng.servlet.user;

import com.cheng.pojo.User;
import com.cheng.service.user.UserServiceImpl;
import com.cheng.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        // 调用service方法，进行用户匹配
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);

        if(user != null){//登录成功
            // 将这个用户信息放入Session
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            // 页面跳转（frame.jsp：前端写好的页面 ）

            //三种写法都可以
//            resp.sendRedirect(req.getContextPath() + "/jsp/frame.jsp");
//            req.getRequestDispatcher("jsp/frame.jsp").forward(req,resp);
            resp.sendRedirect("jsp/frame.jsp");

        }else {// 登录失败
            req.setAttribute("error", "用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
