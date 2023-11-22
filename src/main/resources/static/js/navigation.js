// scroll down page
function scrollToSection(sectionId) {
	var target = document.getElementById(sectionId);
	var offset = target.offsetTop - document.querySelector('.navbar').offsetHeight;

	// Scroll to the target section
	window.scrollTo({
		top: offset,
		behavior: 'smooth'
	});

	// Update the URL without the fragment identifier
	history.replaceState({}, document.title, window.location.pathname);
}



function startLoading(){
	$('.loading').css({"display":"none"});
	$('.spinner-container').css({"display":"block"});
}

function stopLoading(){
	$('.loading').css({"display":"block"});
	$('.spinner-container').css({"display":"none"});
}









