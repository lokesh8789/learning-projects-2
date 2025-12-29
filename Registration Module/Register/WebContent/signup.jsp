<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Sign Up</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
</head>
<body
	style="background: url(img/sky2.jpg); background-size: cover; background-attachment: fixed;">

	<div class="container">
		<div class="row">
			<div class="col-md-6 offset-md-3">
				<div class="card" style="width: 18rem;">
					<div class="card-body">
						<h3>Register Here</h3>
						<h5 id="msg"></h5>
						<div class="from-group center-align">
							<form action="Register" method="post" id="myform">
								<input type="text" class="form-control" name="user_name"
									placeholder="Enter user name" /> <input type="email"
									class="form-control" name="user_email"
									placeholder="Enter user email" /> <input type="password"
									class="form-control" name="user_password"
									placeholder="Enter user password" />
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<span class="input-group-text" id="inputGroupFileAddon01">Upload</span>
									</div>
									<div class="custom-file">
										<input name="image" type="file" class="custom-file-input"
											id="inputGroupFile01"
											aria-describedby="inputGroupFileAddon01"> <label
											class="custom-file-label" for="inputGroupFile01">Choose
											file</label>
									</div>
								</div>
								<button type="submit" class="btn btn-primary">Submit</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

	<script>
		$(document).ready(function() {
			$("#myform").on('submit', function(event) {
				event.preventDefault();
				//var f = $(this).serialize();
				let f=new FormData(this);
				console.log(f);
				$.ajax({
					url : "Register",
					data : f,
					type : 'POST',
					success : function(data, textStatus, jqXHR) {
						console.log(data);
						console.log("success......");
						if (data.trim() === 'done') {
							$("#msg").html("successfully registered!!");
							$("#msg").addClass('text-success');
						} else {
							$("#msg").html("something went wrong");
							$("#msg").addClass('text-warning');
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log(data);
						console.log("error......");
						$("#msg").html("something went wrong");
						$("#msg").addClass('text-warning');
					},
					processData:false,
					contentType:false
				})
			})
		})
	</script>
</body>
</html>