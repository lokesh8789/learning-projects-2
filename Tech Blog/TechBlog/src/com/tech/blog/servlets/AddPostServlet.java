package com.tech.blog.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.core.ApplicationPart;

import com.tech.blog.dao.PostDao;
import com.tech.blog.entities.Post;
import com.tech.blog.entities.User;
import com.tech.blog.helper.ConnectionProvider;
import com.tech.blog.helper.Helper;

@WebServlet("/AddPostServlet")
@MultipartConfig
public class AddPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AddPostServlet() {
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		int cid=Integer.parseInt(request.getParameter("cid"));
		String pTitle=request.getParameter("pTitle");
		String pContent=request.getParameter("pContent");
		String pCode=request.getParameter("pCode");
		ApplicationPart part=(ApplicationPart) request.getPart("pic");
		String pPic=part.getSubmittedFileName();
		//getting current user
		HttpSession s=request.getSession();
		User user=(User) s.getAttribute("currentUser");
		Post p=new Post(pTitle, pContent, pCode, pPic, null, cid, user.getId());
		PostDao dao=new PostDao(ConnectionProvider.getConnection());
		if(dao.savePost(p))
		{
			@SuppressWarnings("deprecation")
			String path=request.getRealPath("/")+"blog_pics"+File.separator+pPic;
			Helper.saveFile(part.getInputStream(), path);
			out.println("done");
		}
		else
		{
			out.println("error");
		}
	}

}
