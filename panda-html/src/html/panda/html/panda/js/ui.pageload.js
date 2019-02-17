(function($) {
	$(window).on('load', function() {
		// invoke onPageLoad function
		for (var i in window) {
			if (i.startsWith('onPageLoad') && typeof(window[i]) == 'function') {
				window[i]();
				window[i] = null;
			}
		}
	});
})(jQuery);
