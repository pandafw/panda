///////////////////////////////////////////////////////
// Patch for carousel
//
(function($) {
	var regTouchGestures = function($e) {
		$e.hammer()
			.on("swipeleft", function(e) {
				$(this).data('bs.carousel').next();
				e.gesture.stopDetect();
			})
			.on("swiperight", function(e) {
				$(this).data('bs.carousel').prev();
				e.gesture.stopDetect();
			});
	};
	
	$(window).on('load', function () {
		$('[data-ride="carousel"]').each(function() {
			regTouchGestures($(this));
		})
	});
})(jQuery);
