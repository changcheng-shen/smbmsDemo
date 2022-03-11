package com.cheng.servlet.user;

import com.cheng.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginouServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 移除session 重定向到login.jsp
        req.getSession().removeAttribute(Constants.USER_SESSION);

//        resp.sendRedirect("/login.jsp"); http://localhost:8080/login.jsp
//        resp.sendRedirect("login.jsp"); http://localhost:8080/smbms/jsp/login.jsp

//        http://localhost:8080/smbms/login.jsp
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
