<%@page import="com.entities.Note"%>
<%@page import="org.hibernate.Transaction"%>
<%@page import="org.hibernate.Session"%>
<%@page import="com.helper.FactoryProvider"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit</title>
<%@include file="all_js_css.jsp" %>
</head>
<body>
	<div class="container">
	<%@include file="navbar.jsp" %>
	<h1>Edit your note</h1>
	<%
	int  noteId=Integer.parseInt(request.getParameter("note_id").trim());
	Session s=FactoryProvider.getFactory().openSession();
	Transaction tx=s.beginTransaction();
	Note note = s.get(Note.class,noteId);
	%>
	
	<!-- form -->
		
		<form action="UpdateServlet" method="post">
			<input value="<%=note.getId()%>" name="noteId" type="hidden">
			<div class="form-group">
				<label for="title">Note Title</label> 
				<input required
					name="title"
					type="text"
					class="form-control" 
					id="title"
					aria-describedby="emailHelp"
					placeholder="Enter here"
					value="<%=note.getTitle()%>"> 
			</div>
			<div class="form-group">
				<label for="content">Note Content</label>
				<textarea name="content" required class="form-control" 
				id="content" 
				placeholder="Enter your content here"
				style="height: 300px"><%=note.getContent()%>
				</textarea>
			</div>
			<div class="container text-center">
				<button type="submit" class="btn btn-success">Save</button>
			</div>
		</form>
	</div>
</body>
</html>