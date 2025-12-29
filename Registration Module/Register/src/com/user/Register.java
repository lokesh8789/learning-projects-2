package com.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.core.ApplicationPart;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
@MultipartConfig
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Register() {
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String name=request.getParameter("user_name");
		String password=request.getParameter("user_password");
		String email=request.getParameter("user_email");
		ApplicationPart part=(ApplicationPart) request.getPart("image");
		String filename=part.getSubmittedFileName();
		//out.println(filename);
//		out.println(name);
//		out.println(password);
//		out.println(email);
		
		//connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/db","root","beast");
			String q="insert into user(name,email,password,imageName) values(?,?,?,?)";
			PreparedStatement p=con.prepareStatement(q);
			p.setString(1,name);
			p.setString(2,email);
			p.setString(3,password);
			p.setString(4,filename);
			p.executeUpdate();
			out.println("done");
			//upload image
			InputStream is=part.getInputStream();
			byte[] data=new byte[is.available()];
			is.read(data);
			@SuppressWarnings("deprecation")
			String path=request.getRealPath("/")+"img"+File.separator+filename;
			//out.println(path);
			FileOutputStream fos=new FileOutputStream(path);
			fos.write(data);
			fos.close();
			out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("error");
		}
	}

}
