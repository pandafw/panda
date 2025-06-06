(function($) {
	"use strict";

	$.fn.totop = function() {
		return this.each(function() {
			var $t = $(this), $w = $(window);

			$t.click(function() {
				$('html,body').animate({ scrollTop: 0 }, 'slow');
			}).css({ cursor: 'pointer' });

			$w.scroll(function() {
				$t.toggle($w.scrollTop() > $w.height());
			});
		});
	};

	$(window).on('load', function() {
		$('[totop]').totop();
	});

})(jQuery);
