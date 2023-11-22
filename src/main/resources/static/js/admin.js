function startLoading() {
	$('.loading').css({ "display": "none" });
	$('.spinner-container').css({ "display": "block" });
}

function stopLoading() {
	$('.loading').css({ "display": "block" });
	$('.spinner-container').css({ "display": "none" });
}
startLoading();
window.onload = function() {
	stopLoading();
	console.log("Loading is complete!");
};

$(document).ready(function() {
	$(".add_admin").click(function(e) {
		e.preventDefault();
		const valid_name = /^[a-zA-Z]+$/
		const validate_email = /^[a-z0-9]+@[a-z]+\.[a-z]{2,3}$/;
		// const validate_phone = /^[6-9]\d{9}$/;
		const validate_password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{7,}$/;
		var csrfToken = $('[name="_csrf"]').val();
		var name = $(".name").val().replace(/ +/g, ' ');
		var email = $(".email").val().replace(/ +/g, ' ');
		var password = $(".password").val();
		var confirm_password = $(".confirm_password").val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (name == "" || name == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your name");
		} else if (name.length <= 2) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your full name without space");
		} else if (!valid_name.test(name)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid name. Not allowed number,special characters and space");
		} else if (email == "" || email == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your email address");
		} else if (!email.match(validate_email)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid email address");
		} else if (password == "" || password == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your password");
		} else if (!password.match(validate_password)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your strong password");
		} else if (confirm_password == null || confirm_password == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a confirm password");
		} else if (password != confirm_password) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Password not match");
		} else {
			startLoading();
			var formData = {
				name: name,
				email: email,
				password: password,
			};
			$.ajax({
				type: "POST",
				url: "/add-admin",
				data: JSON.stringify(formData),
				contentType: "application/json",
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					$('input').val('');
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.success(response);
					setTimeout(function() {
						window.location.href = "/view-admin";
					}, 1000);

				}, error: function(error) {
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.error(error.responseText);
				}
			});
		}
	});
});

$(document).ready(function() {
	$(".add_user").click(function(e) {
		e.preventDefault();
		const valid_name = /^[a-zA-Z]+$/
		const validate_email = /^[a-z0-9]+@[a-z]+\.[a-z]{2,3}$/;
		// const validate_phone = /^[6-9]\d{9}$/;
		const validate_password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{7,}$/;
		var csrfToken = $('[name="_csrf"]').val();
		var name = $(".name").val().replace(/ +/g, ' ');
		var email = $(".email").val().replace(/ +/g, ' ');
		var password = $(".password").val();
		var confirm_password = $(".confirm_password").val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (name == "" || name == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your name");
		} else if (name.length <= 2) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your full name without space");
		} else if (!valid_name.test(name)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid name. Not allowed number,special characters and space");
		} else if (email == "" || email == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your email address");
		} else if (!email.match(validate_email)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid email address");
		} else if (password == "" || password == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your password");
		} else if (!password.match(validate_password)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your strong password");
		} else if (confirm_password == null || confirm_password == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a confirm password");
		} else if (password != confirm_password) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Password not match");
		} else {
			startLoading();
			var formData = {
				name: name,
				email: email,
				password: password,
			};
			$.ajax({
				type: "POST",
				url: "/add-user",
				data: JSON.stringify(formData),
				contentType: "application/json",
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					$('input').val('');
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.success(response);
					setTimeout(function() {
						window.location.href = "/admin-dashboard";
					}, 1000);

				}, error: function(error) {
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.error(error.responseText);
				}
			});
		}
	});
});

$(document).ready(function() {
	$(".add_course").click(function(e) {
		e.preventDefault();
		const valid_name = /^[a-zA-Z]+(?:\s[a-zA-Z]+)?$/
		const validate_price = /^[0-9]*$/;
		var csrfToken = $('[name="_csrf"]').val();
		var name = $(".course_name").val().replace(/ +/g, ' ');
		var price = $(".course_price").val().replace(/ +/g, ' ');
		var short = $(".short_discription").val().replace(/ +/g, ' ');
		var long = $(".long_discription").val().replace(/ +/g, ' ');
		var video = $(".course_video");
		var course_video = video[0].files;
		var formData = new FormData();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (name == "" || name == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your course name");
		} else if (name.length <= 2) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your full course name");
		} else if (!valid_name.test(name)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid name. Not allowed number,special characters and space");
		} else if (price == "" || price == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a price");
		} else if (!price.match(validate_price)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid price");
		} else if (course_video.length <= 0) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please upload videos");
		} else if (course_video.length > 10) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Only allowed 10 videos");
		} else if (short == "" || short == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a short discription");
		} else if (long == null || long == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a long description");
		} else {
			for (var i = 0; i < course_video.length; i++) {
				if (!checkFileExtension(course_video[i].name)) {
					alertify.set('notifier', 'position', 'top-right');
					alertify.error("Please upload mp4 format only");
					return;
				} else {
					if (course_video[i].size > 100 * 1024 * 1024) {
						alertify.set('notifier', 'position', 'top-right');
						alertify.error("Video upload size allowed 100mb");
						return;
					}else{
						formData.append("videos",course_video[i]);
					}
				}
			}
			startLoading();
			formData.append("courseName",name);
			formData.append("coursePrice",price);
			formData.append("shortDescription",short);
			formData.append("longDescription",long);
			$.ajax({
				type: "POST",
				url: "/add-course",
				data: formData,
				contentType: false,
				processData: false,
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					$('input').val('');
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.success(response[0]);
					setTimeout(function() {
						window.location.href = "/view-course";
					}, 1000);

				}, error: function(error) {
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.error(error.responseText[0]);
				}
			});
		}
	});
});


function checkFileExtension(course_video) {
	var allowedExtension = ["mp4"];
	var ext = course_video.split('.').pop().toLowerCase();
	return allowedExtension.includes(ext);
}


// update profile 

$(document).ready(function() {
	$(".update_profile").click(function(e) {
		e.preventDefault();
		const valid_name = /^[a-zA-Z]+$/;
		var csrfToken = $('[name="_csrf"]').val();
		var name = $(".name").val().replace(/ +/g, ' ');
		var profile = $(".profile");
		var file = profile[0].files;
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (name == "" || name == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your name");
		} else if (name.length <= 2) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your full name without space");
		} else if (!valid_name.test(name)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid name. Not allowed number,special characters and space");
		} else {
			var formData = new FormData();
			if (file.length == 0) {
				startLoading();
				formData.append("name", name);
				$.ajax({
					type: "POST",
					url: "/admin-profile",
					data: formData,
					contentType: false,
					processData: false,
					beforeSend: function(xhr) {
						xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
					}, success: function(response) {
						stopLoading();
						$('input').val('');
						alertify.set('notifier', 'position', 'top-right');
						alertify.success(response);
						setTimeout(function() {
							window.location.href = "/admin-profile";
						}, 1000);

					}, error: function(error) {
						stopLoading();
						alertify.set('notifier', 'position', 'top-right');
						alertify.error(error.responseText);
					}
				});
			} else {
				var avatar = file[0];
				var allowedExtension = /(\.jpg|\.jpeg|\.png)$/i;
				if (!allowedExtension.exec(avatar.name)) {
					alertify.set('notifier', 'position', 'top-right');
					alertify.error("Please upload image format only");
					$(".profile").val("");
				} else {
					if (avatar.size > 100 * 1024) {
						alertify.set('notifier', 'position', 'top-right');
						alertify.error("Image size is greater than 100kb");
						$(".profile").val("");
					} else if (avatar.size < 10 * 1024) {
						alertify.set('notifier', 'position', 'top-right');
						alertify.error("Image size is less than 10kb");
						$(".profile").val("");
					} else {
						startLoading();
						formData.append("image", avatar);
						formData.append("name", name);
						$.ajax({
							type: "POST",
							url: "/admin-profile",
							data: formData,
							contentType: false,
							processData: false,
							beforeSend: function(xhr) {
								xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
							}, success: function(response) {
								stopLoading();
								$('input').val('');
								alertify.set('notifier', 'position', 'top-right');
								alertify.success(response);
								setTimeout(function() {
									window.location.href = "/admin-profile";
								}, 1000);

							}, error: function(error) {
								stopLoading();
								$(".profile").val("");
								alertify.set('notifier', 'position', 'top-right');
								alertify.error(error.responseText);
							}
						});
					}
				}
			}
		}
	});
});


// update password

$(document).ready(function() {
	$(".update_password").click(function(e) {
		e.preventDefault();
		var oldPassword = $(".old_password").val();
		var newPassword = $(".new_password").val();
		const validate_password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{7,}$/;
		var csrfToken = $('[name="_csrf"]').val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (oldPassword == "" || oldPassword == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a old password");
		} else if (newPassword == "" || newPassword == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a new password");
		} else if (!validate_password.test(newPassword)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter strong password");
		} else {
			startLoading();
			var formData = {
				oldPassword: oldPassword,
				newPassword: newPassword
			}
			$.ajax({
				type: "POST",
				url: "/settings",
				data: JSON.stringify(formData),
				contentType: "application/json",
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					stopLoading();
					$('input').val('');
					alertify.set('notifier', 'position', 'top-right');
					alertify.success(response);
					setTimeout(function() {
						window.location.href = "/login";
					}, 1000);
				}, error: function(error) {
					stopLoading();
					$(".profile").val("");
					alertify.set('notifier', 'position', 'top-right');
					alertify.error(error.responseText);
				}
			});
		}
	});
});


// update course
$(document).ready(function() {
	$(".update_course").click(function(e) {
		e.preventDefault();
		const validate_price = /^[0-9]*$/;
		var csrfToken = $('[name="_csrf"]').val();
		var price = $(".course_price").val().replace(/ +/g, ' ');
		var course_available = $(".available").val().replace(/ +/g, ' ');
		var available = Boolean(course_available);
		var short = $(".short_discription").val().replace(/ +/g, ' ');
		var long = $(".long_discription").val().replace(/ +/g, ' ');
		const url = new URLSearchParams(window.location.search);
		const token = url.get("ci");
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (price == "" || price == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a price");
		} else if (!price.match(validate_price)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid price");
		} else if (short == "" || short == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a short discription");
		} else if (long == null || long == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a long description");
		} else {
			startLoading();
			var formData = {
				coursePrice: price,
				available: available,
				shortDescription: short,
				longDescription: long,
				token: token
			};
			$.ajax({
				type: "POST",
				url: "/update-course",
				data: JSON.stringify(formData),
				contentType: "application/json",
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					$('input').val('');
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.success(response);
					setTimeout(function() {
						window.location.href = "/view-course";
					}, 1000);

				}, error: function(error) {
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.error(error.responseText);
				}
			});
		}
	});
});



// delete course

$(document).ready(function() {
	$(".delete_course").click(function(e) {
		e.preventDefault();
		var courseId = $(this).val();
		var csrfToken = $('[name="_csrf"]').val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		}
		else if (courseId == null || courseId == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Invalid course");
		} else {
			Swal.fire({
				title: 'Are you sure?',
				text: 'You won\'t be able to revert this!',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, delete it',
				cancelButtonText: 'No',
				allowOutsideClick: false
			}).then((result) => {
				if (result.isConfirmed) {
					startLoading();
					$.ajax({
						type: "POST",
						url: "/view-course",
						data: {
							courseId: courseId
						},
						beforeSend: function(xhr) {
							xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
						}, success: function(response) {
							stopLoading();
							Swal.fire(
								'Deleted!',
								response,
								'success'
							);
							$("#datatablesSimple").load(location.href + " #datatablesSimple");

						}, error: function(error) {
							stopLoading();
							Swal.fire(
								'Error!',
								error.responseText,
								'error'
							);
						}
					});
				}
			})
		}

	});
});



// delete admin

$(document).ready(function() {
	$(".delete_admin").click(function(e) {
		e.preventDefault();
		var adminId = $(this).val();
		var csrfToken = $('[name="_csrf"]').val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		}
		else if (adminId == null || adminId == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Invalid course");
		} else {
			Swal.fire({
				title: 'Are you sure?',
				text: 'You won\'t be able to revert this!',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, delete it',
				cancelButtonText: 'No',
				allowOutsideClick: false
			}).then((result) => {
				if (result.isConfirmed) {
					startLoading();
					$.ajax({
						type: "POST",
						url: "/view-admin",
						data: {
							adminId: adminId
						},
						beforeSend: function(xhr) {
							xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
						}, success: function(response) {
							stopLoading();
							Swal.fire(
								'Deleted!',
								response,
								'success'
							);
							$("#datatablesSimple").load(location.href + " #datatablesSimple");

						}, error: function(error) {
							stopLoading();
							Swal.fire(
								'Error!',
								error.responseText,
								'error'
							);
						}
					});
				}
			})
		}

	});
});



// delete user

$(document).ready(function() {
	$(".delete_user").click(function(e) {
		e.preventDefault();
		var userId = $(this).val();
		var csrfToken = $('[name="_csrf"]').val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		}
		else if (userId == null || userId == "") {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Invalid course");
		} else {
			Swal.fire({
				title: 'Are you sure?',
				text: 'You won\'t be able to revert this!',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, delete it',
				cancelButtonText: 'No',
				allowOutsideClick: false
			}).then((result) => {
				if (result.isConfirmed) {
					startLoading();
					$.ajax({
						type: "POST",
						url: "/admin-dashboard",
						data: {
							userId: userId
						},
						beforeSend: function(xhr) {
							xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
						}, success: function(response) {
							stopLoading();
							Swal.fire(
								'Deleted!',
								response,
								'success'
							);
							$("#datatablesSimple").load(location.href + " #datatablesSimple");

						}, error: function(error) {
							stopLoading();
							Swal.fire(
								'Error!',
								error.responseText,
								'error'
							);
						}
					});
				}
			})
		}

	});
});
