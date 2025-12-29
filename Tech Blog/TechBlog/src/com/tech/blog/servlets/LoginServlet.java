package com.tech.blog.servlets;

import java.io.IOException;
//import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tech.blog.dao.UserDao;
import com.tech.blog.entities.Message;
import com.tech.blog.entities.User;
import com.tech.blog.helper.ConnectionProvider;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		//PrintWriter out=response.getWriter();
		String userEmail=request.getParameter("email");
		String userPassword=request.getParameter("password");
		UserDao dao=new UserDao(ConnectionProvider.getConnection());
		User user = dao.getUserByEmailAndPassword(userEmail, userPassword);
		if(user==null)
		{
			//login error
			//out.println("Invalid Details..try again");
			Message msg=new Message("Invalid Details ! try again","error","alert-danger");
			HttpSession s=request.getSession();
			s.setAttribute("msg",msg);
			response.sendRedirect("login_page.jsp");
		}
		else
		{
			//login success
			HttpSession s=request.getSession();
			s.setAttribute("currentUser",user);
			response.sendRedirect("profile.jsp");
		}
	}

}
