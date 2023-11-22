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


$(document).ready(function(){
	$(".send_message").click(function(e){
		e.preventDefault();
		const validate = /^[a-zA-Z0-9]+(?:\s[a-zA-Z0-9]+)*$/;
		var message = $(".message").val();
		var csrfToken = $('[name="_csrf"]').val();
		if (csrfToken == "" || csrfToken == null) {
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Unauthorized person");
		} else if(message==null || message==""){
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter a message");
		} else if(!validate.test(message)){
			alertify.set('notifier', 'position', 'top-right');
			alertify.error("Please enter vaild message and only allowed alphabet and numbers");
		}else{
			startLoading();
			$.ajax({
				type: "POST",
				url: "/contact",
				data: {
					message:message
				},
				beforeSend: function(xhr) {
					xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
				}, success: function(response) {
					$('textarea').val('');
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

$(document).ready(function(){
	$(".myVideos").on("contextmenu",function(e){
		e.preventDefault();
	});
});

