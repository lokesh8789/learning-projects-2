<%@page import="com.tech.blog.entities.*"%>
<%@page import="com.tech.blog.dao.*"%>
<%@page import="com.tech.blog.helper.*"%>
<%@page import="java.util.*"%>

<%@page errorPage="error_page.jsp"%>
<%
	User user = (User) session.getAttribute("currentUser");
	if (user == null) {
		response.sendRedirect("login_page.jsp");
	}
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Profile</title>
<!-- css -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
	integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
	crossorigin="anonymous">
<link href="css/mystyle.css" rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
.banner-background {
	clip-path: polygon(30% 0%, 70% 0%, 100% 0, 100% 89%, 61% 98%, 27% 90%, 0 96%, 0 0);
}
</style>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark primary-background">
		<a class="navbar-brand" href="index.jsp"><span
			class="fa fa-asterisk"></span> Tech Blog</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item active"><a class="nav-link" href="#"> <span
						class="fa fa-bell-o"></span> Home <span class="sr-only">(current)</span>
				</a></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" role="button"
					data-toggle="dropdown" aria-expanded="false"><span
						class="fa fa-check-square-o"></span> Categories </a>
					<div class="dropdown-menu">
						<a class="dropdown-item" href="#">Programming Language</a> <a
							class="dropdown-item" href="#">Project Implementation/</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#">Data Structure</a>
					</div></li>
				<li class="nav-item"><a class="nav-link" href="#"><span
						class="fa fa-address-card-o"></span> Contact</a></li>
				<li class="nav-item"><a class="nav-link" href="#" data-toggle="modal" data-target="#add-post-modal" ><span
						class="fa fa-asterisk"></span> Do Post</a></li>

			</ul>
			<ul class="navbar-nav mr-right">
				<li class="nav-item"><a class="nav-link" href="#!"
					data-toggle="modal" data-target="#profile-modal"><span
						class="fa fa-user-circle"></span> <%=user.getName()%></a></li>
				<li class="nav-item"><a class="nav-link" href="LogoutServlet"><span
						class="fa fa-user-plus"></span> Logout</a></li>
			</ul>
		</div>
	</nav>
	<%
						Message m=(Message)session.getAttribute("msg");
						if(m!=null)
						{
							%>
							<div class="alert <%=m.getCssClass() %>" role="alert">
								<%=m.getContent() %>
							</div>
							<%
							session.removeAttribute("msg");
						}
						%>
						
	<!-- main body  -->
	<main>
		<div class="container">
			<div class="row mt-4">
				<div class="col-md-4">
					<!-- categories -->
					<div class="list-group">
					  <a href="#" onclick="getPosts(0,this)" class="c-link list-group-item list-group-item-action active" aria-current="true">
					    All Posts
					  </a>
					  <%
					  PostDao d=new PostDao(ConnectionProvider.getConnection());
					  ArrayList<Category> list1=d.getAllCategories();
					  for(Category c1:list1)
					  {
						  %>
						  <a href="#" onclick="getPosts(<%=c1.getCid() %>,this)" class="c-link list-group-item list-group-item-action"><%=c1.getName() %></a>
						  <%
					  }
					  %>
					  
					</div>
				</div>
				<div class="col-md-8">
					<!-- posts -->
					<div class="container text-center" id="loader">
						<i class="fa fa-refresh fa-4x fa-spin"></i>
						<h3 class="mt-2">Loading...</h3>
					</div>
					<div class="container-fluid"  id="post-container"></div>
				</div>
			</div>
		</div>
	</main>
	<!-- profile modal -->
	
	<!-- Modal -->
	<div class="modal fade" id="profile-modal" tabindex="-1"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header primary-background text-white">
					<h5 class="modal-title" id="exampleModalLabel">TechBlog</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="container text-center">
						<img src="pics/<%=user.getProfile()%>" class="img-fluid"
							style="border-radius: 50%; max-width: 150px;"> <br>
						<h5 class="modal-title mt-3" id="exampleModalLabel"><%=user.getName()%></h5>
						<!--details  -->
						<div id="profile-details">
							<table class="table">
								<tbody>
									<tr>
										<th scope="row">ID : </th>
										<td><%=user.getId() %></td>
									</tr>
									<tr>
										<th scope="row">Email : </th>
										<td><%=user.getEmail() %></td>
									</tr>
									<tr>
										<th scope="row">Gender : </th>
										<td><%=user.getGender() %></td>
									</tr>
									<tr>
										<th scope="row">Registered On : </th>
										<td><%=user.getDateTime().toString() %></td>
									</tr>
									<tr>
										<th scope="row">Status : </th>
										<td><%=user.getAbout() %></td>
									</tr>
								</tbody>
							</table>
						</div>
						<!-- profile edit -->
						<div id="profile-edit" style="display: none;">
							<h3 class="mt-2">Please Edit Carefully</h3>
							<form action="EditServlet" method="POST" enctype="multipart/form-data">
								<table class="table">
									<tr>
										<td>ID : </td>
										<td><%=user.getId() %></td>
									</tr>
									<tr>
										<td>Email : </td>
										<td><input type="email" class="from-control" name="user_email" value="<%=user.getEmail() %>"></td>
									</tr>
									<tr>
										<td>Name : </td>
										<td><input type="type" class="from-control" name="user_name" value="<%=user.getName() %>"></td>
									</tr>
									<tr>
										<td>Password : </td>
										<td><input type="password" class="from-control" name="user_password" value="<%=user.getPassword() %>"></td>
									</tr>
									<tr>
										<td>Gender : </td>
										<td><%=user.getGender().toUpperCase() %></td>
									</tr>
									<tr>
										<td>About : </td>
										<td>
											<textarea rows="3" class="form-control" name="user_about"><%=user.getAbout()%>
											</textarea>
										</td>
									</tr>
									<tr>
										<td>New Profile : </td>
										<td>
											<input type="file" name="image" class="form-control">
										</td>
									</tr>
								</table>
								<div class="container">
									<button type="submit" class="btn btn-outline-primary">Save</button>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
					<button id="edit-profile-button" type="button" class="btn btn-primary">EDIT</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Add post modal -->
	<!-- Modal -->
	<div class="modal fade" id="add-post-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">Provide the post details..</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form id="add-post-form" action="AddPostServlet" method="post">
	        	<div class="form-group">
	        		<select class="form-control" name="cid">
	        			<option selected disabled>---Select Category---</option>
	        			<%
	        			PostDao postd=new PostDao(ConnectionProvider.getConnection());
	        			ArrayList<Category> list=postd.getAllCategories();
	        			for(Category c:list)
	        			{
	        			%>
	        			<option value="<%= c.getCid() %>"><%=c.getName() %></option>
	        			<%
	        			}
	        			%>
	        		</select>
	        	</div>
	        	<div class="form-group">
	        		<input type="text" name="pTitle" placeholder="Enter Title" class="form-control">
	        	</div>
	        	<div class="form-group">
	        		<textarea name="pContent" class="form-control" placeholder="Enter your content" style="height:200px"></textarea>
	        	</div>
	        	<div class="form-group">
	        		<textarea name="pCode" class="form-control" placeholder="Enter your code (if any)" style="height:200px"></textarea>
	        	</div>
	        	<div class="form-group">
	        		<label>Select Your Pic..</label>
	        		<br>
	        		<input type="file" name="pic">
	        	</div>
	        	<div class="container text-center">
	        		<button type="submit" class="btn btn-outline-primary">Post</button>
	        	</div>
	        </form>
	      </div>
	    </div>
	  </div>
	</div>
	<!-- end post modal -->
	<!-- javascript -->
	<script src="https://code.jquery.com/jquery-3.6.3.min.js"
		integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU="
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
		integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"
		integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+"
		crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.2/sweetalert.min.js"></script>
	<script src="js/myjs.js" type="text/javascript"></script>
	<script>
		$(document).ready(function(){
			let editStatus=false;
			$('#edit-profile-button').click(function() {
				if(editStatus==false)
				{
					$('#profile-details').hide();
					$('#profile-edit').show();
					editStatus=true;
					$(this).text("Back")
				}
				else
				{
					$('#profile-details').show();
					$('#profile-edit').hide();
					editStatus=false;
					$(this).text("Edit")
				}
			})
		})
	</script>
	<!-- add post js -->
	<script>
		$(document).ready(function(){
			$("#add-post-form").on('submit',function(event){
				event.preventDefault();
				let form=new FormData(this);
				//requesting to server
				$.ajax({
					url:"AddPostServlet",
					type:'POST',
					data:form,
					success:function(data,textStatus,jqXHR)
					{
						if(data.trim()==='done')
						{
							swal("Good job!", "Saved Successfully!", "success");
						}
						else
						{
							swal("Something went wrong!! try again...", "error");
						}
					},
					error:function(jqXHR,textStatus,errorThrown)
					{
						swal("Something went wrong!! try again...", "error");
					},
					processData:false,
					contentType:false
				})
			})
		})
	</script>
	<!-- loading post using ajax -->
	<script>
		function getPosts(catId,temp){
			$("#loader").show();
			$("#post-container").hide();
			$(".c-link").removeClass('active')
			$.ajax({
				url:"load_post.jsp",
				data:{cid:catId},
				success:function(data,textStatus,jqXHR)
				{
					$('#loader').hide();
					$("#post-container").show();
					$('#post-container').html(data);
					$(temp).addClass('active')
				}
			})
		}
		$(document).ready(function(){
			let allPostRef=$('.c-link')[0]
			getPosts(0,allPostRef)
		})
	</script>
</body>
</html>