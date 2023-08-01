(function($) {
	"use strict";

	$(window).on('load', function () {
		$('.navbar-toggle').click(function() {
			$(this).toggleClass('active');
		});
	});
})(jQuery);
