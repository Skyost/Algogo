$(document).ready(function() {
	$('.feature figure').matchHeight();
	$('.screenshot figure img').matchHeight();
	$('.screenshot figure').matchHeight();
	
	$('#screenshots').lightGallery();
	
	$('[data-localize]').localize('index', {
		pathPrefix: 'assets/json',
		callback: function(data, defaultCallback) {
			let screenshots = $('.screenshot');
			for(let i = 0; i < screenshots.length; i++) {
				let screenshot = $(screenshots[i]);
				let screenshotData = data.screenshots[i + 1];
				
				screenshot.attr('data-src', screenshotData.src);
				
				let image = screenshot.find('img');
				image.attr('src', screenshotData.src);
				image.attr('title', screenshotData.caption);
				image.attr('alt', screenshotData.caption);
			}
			
			$('#play-button img').attr('src', data.mobile.badge);
			
			defaultCallback(data);
		}
	});
});