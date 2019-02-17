(function($) {
	$(window).on('load', function() {
		var $t = $('.p-totop');
		if ($t.size() > 0) {
			$t.click(function() {
				$('html,body').animate({ scrollTop: 0 }, 'slow');
			});
	
			var $w = $(window);
			$w.scroll(function() {
				$t[$w.scrollTop() > $w.height() ? 'show' : 'hide']();
			});
		}
	});
})(jQuery);

