(function($) {
	"use strict";

	$.fn.totop = function() {
		$(this).each(function() {
			var $t = $(this), $w = $(window);

			$t.click(function() {
				$('html,body').animate({ scrollTop: 0 }, 'slow');
			}).css({ cursor: 'pointer' });

			$w.scroll(function() {
				$t[$w.scrollTop() > $w.height() ? 'show' : 'hide']();
			});
		});
	};

	$(window).on('load', function() {
		$('[totop="true"]').totop();
	});

})(jQuery);
