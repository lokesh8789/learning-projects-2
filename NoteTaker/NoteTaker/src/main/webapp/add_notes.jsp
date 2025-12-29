<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@include file="all_js_css.jsp"%>
<title>Add Notes</title>
</head>
<body>
	<div class="container">
		<%@include file="navbar.jsp"%>
		<br>
		<h1>Please fill your note</h1>
		<br>
		
		<!-- form -->
		
		<form action="SaveNoteServlet" method="post">
			<div class="form-group">
				<label for="title">Note Title</label> 
				<input required
					name="title"
					type="text"
					class="form-control" 
					id="title"
					aria-describedby="emailHelp"
					placeholder="Enter here"> 
			</div>
			<div class="form-group">
				<label for="content">Note Content</label>
				<textarea name="content" required class="form-control" 
				id="content" 
				placeholder="Enter your content here"
				style="height: 300px"></textarea>
			</div>
			<div class="container text-center">
				<button type="submit" class="btn btn-primary">Add</button>
			</div>
		</form>
	</div>
</body>
</html>