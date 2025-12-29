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

import com.tech.blog.dao.UserDao;
import com.tech.blog.entities.Message;
import com.tech.blog.entities.User;
import com.tech.blog.helper.ConnectionProvider;
import com.tech.blog.helper.Helper;

@WebServlet("/EditServlet")
@MultipartConfig
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public EditServlet() {
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String userEmail=request.getParameter("user_email");
		String userName=request.getParameter("user_name");
		String userPassword=request.getParameter("user_password");
		String userAbout=request.getParameter("user_about");
		ApplicationPart part=(ApplicationPart) request.getPart("image");
		String imageName=part.getSubmittedFileName();
		
		//get the user from session
		HttpSession s=request.getSession();
		User user = (User) s.getAttribute("currentUser");
		user.setEmail(userEmail);
		user.setName(userName);
		user.setPassword(userPassword);
		user.setAbout(userAbout);
		String oldFile=user.getProfile();
		user.setProfile(imageName);
		//update database
		UserDao userDao=new UserDao(ConnectionProvider.getConnection());
		boolean ans = userDao.updateUser(user);
		if(ans)
		{
			@SuppressWarnings("deprecation")
			String path=request.getRealPath("/")+"pics"+File.separator+user.getProfile();
			
			//F:\Project2\Tech Blog\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\TechBlog\pics\lokesh.jpg
			
			@SuppressWarnings("deprecation")
			String oldPath=request.getRealPath("/")+"pics"+File.separator+oldFile;
			if(!oldFile.equals("default.png"))
			{
				Helper.deleteFile(oldPath);
			}
			if(Helper.saveFile(part.getInputStream(), path))
			{
				out.println("Profile Updated..");
				Message msg=new Message("Profile Updated..","success","alert-success");
				s.setAttribute("msg",msg);
			}
			else
			{
				Message msg=new Message("Something went wrong","error","alert-danger");
				s.setAttribute("msg",msg);
			}
		}
		else
		{
			out.println("not updated..");
			Message msg=new Message("Something went wrong","error","alert-danger");
			s.setAttribute("msg",msg);
		}
		response.sendRedirect("profile.jsp");
	}

}
