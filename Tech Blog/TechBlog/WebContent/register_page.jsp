<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Register</title>
<!-- css -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
	integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
	crossorigin="anonymous">
<link href="css/mystyle.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
	.banner-background{
		clip-path: polygon(30% 0%, 70% 0%, 100% 0, 100% 89%, 61% 98%, 27% 90%, 0 96%, 0 0);
	}
</style>
</head>
<body>
	<!-- navbar -->
	<%@include file="normal_navbar.jsp" %>
	<main class="primary-background banner-background" style="padding-bottom:80px">
		<div class="container">
			<div class="col-md-6 offset-md-3">
				<div class="card">
					<div class="card-header text-center primary-background text-white">
						<span class="fa fa-3x fa-user-circle"></span>
						<br>
						Register Here
					</div>
					<div class="card-body">
						<form id="reg-form" action="RegisterServlet" method="POST">
							<div class="form-group">
						    <label for="user_name">User Name</label>
						    <input type="text" name="user_name" class="form-control" id="user_name" aria-describedby="emailHelp" placeholder="Enter name">
						  </div>
						  
						  <div class="form-group">
						    <label for="exampleInputEmail1">Email address</label>
						    <input type="email" name="user_email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter Email">
						    <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
						  </div>
						  
						  <div class="form-group">
						    <label for="exampleInputPassword1">Password</label>
						    <input type="password" name="user_password" class="form-control" id="exampleInputPassword1" placeholder="Enter password">
						  </div>
						  
						  <div class="form-group">
						    <label for="gender">Select Gender</label>
						    <br>
						    <input type="radio" id="gender1" name="gender" value="male">Male
						    <input type="radio" id="gender2" name="gender" value="female">Female
						  </div>
						  
						  <div class="form-group">
						  	<textarea name="about" class="form-control" rows="5" cols="" placeholder="About Yourself"></textarea>
						  </div>
						  
						  <div class="form-group form-check">
						    <input type="checkbox" name="check" class="form-check-input" id="exampleCheck1">
						    <label class="form-check-label" for="exampleCheck1">Agree Terms And Conditions</label>
						  </div>
						  <div class="container text-center" id="loader" style="display:none">
						  	<span class="fa fa-refresh fa-spin fa-4x"></span>
						  	<h4>Please Wait...</h4>
						  </div>
						  <button id="submit-btn" type="submit" class="btn btn-primary">Submit</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</main>

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
	<script src="js/myjs.js" type="text/javascript"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.2/sweetalert.min.js" integrity="sha512-AA1Bzp5Q0K1KanKKmvN/4d3IRKVlv9PYgwFPvm32nPO6QS8yH1HO7LbgB1pgiOxPtfeg5zEn2ba64MUcqJx6CA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<script>
		$(document).ready(function(){
			console.log("loaded...")
			$('#reg-form').on('submit',function(event){
				event.preventDefault();
				let form=new FormData(this);
				$("#submit-btn").hide();
				$("#loader").show();
				//send to register servlet
				$.ajax({
					url:"RegisterServlet",
					type:'POST',
					data:form,
					success: function(data,textStatus,jqXHR){
						console.log(data)
						$("#submit-btn").show();
						$("#loader").hide();
						if(data.trim()==='done')
						{
							swal("Registered Successfully..Redirecting to login page")
							.then((value) => {
							  window.location="login_page.jsp"
							});
						}
						else
						{
							swal(data);
						}
					},
					error: function(jqXHR,textStatus,errorThrown)
					{
						console.log(jqXHR)
						$("#submit-btn").show();
						$("#loader").hide();
						swal("Something Went Wrong... try again");
					},
					processData: false,
					contentType: false
				});
			});
		})
	</script>
</body>
</html>