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
};


// signup
$(document).ready(function() {
	$(".signup").click(function(e) {
		e.preventDefault();
		const valid_name = /^[a-zA-Z]+$/
		const validate_email = /^[a-z0-9]+@[a-z]+\.[a-z]{2,3}$/;
		// const validate_phone = /^[6-9]\d{9}$/;
		const validate_password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{7,}$/;
		var csrfToken = $('[name="_csrf"]').val();
		var name = $(".name").val().replace(/ +/g, ' ');
		var email = $(".email").val().replace(/ +/g, ' ');
		var password = $(".password").val();
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
		} else {
			startLoading();
			var formData = {
				name: name,
				email: email,
				password: password,
			};
			$.ajax({
				type: "POST",
				url: "/signup",
				data: JSON.stringify(formData),
				contentType: "application/json",
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					$('input').val('');
					stopLoading();
					alertify.set('notifier', 'position', 'top-right');
					alertify.success(response);

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
	$(".update").click(function(e) {
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
					url: "/profile",
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
							window.location.href = "/profile";
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
							url: "/profile",
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
									window.location.href = "/profile";
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

$(document).ready(function() {
	$(".password_update").click(function(e) {
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
				url: "/update-password",
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


$(document).ready(function() {
	$(".forgot_password").click(function(e) {
		e.preventDefault();
		const validate_email = /^[a-z0-9]+@[a-z]+\.[a-z]{2,3}$/;
		var csrfToken = $('[name="_csrf"]').val();
		var email = $(".email").val().replace(/ +/g, ' ');
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (email == "" || email == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter your email address");
		} else if (!email.match(validate_email)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter valid email address");
		} else {
			startLoading();
			var formData = {
				email: email,
			}
			$.ajax({
				type: "POST",
				url: "/forgot-password",
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

$(document).ready(function() {
	$(".change_password").click(function(e) {
		e.preventDefault();
		var password = $(".password").val();
		const validate_password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{7,}$/;
		var csrfToken = $('[name="_csrf"]').val();
		const url = new URLSearchParams(window.location.search);
		const token = url.get("token");
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if (password == "" || password == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a password");
		} else if (!validate_password.test(password)) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter strong password");
		} else {
			startLoading();
			var formData = {
				password: password,
				token: token
			}
			$.ajax({
				type: "POST",
				url: "/change-password",
				data: JSON.stringify(formData),
				contentType: "application/json",
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				},
				dataType: "html",
				success: function(response) {
					stopLoading();
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




