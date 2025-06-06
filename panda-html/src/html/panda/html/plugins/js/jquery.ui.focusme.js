(function($) {
	"use strict";

	$.fn.focusme = function() {
		var done = false;
		return this.each(function() {
			if (done) {
				return;
			}

			var $i = $(this), a = $i.attr('focusme') || 'true', $a;

			if (a == 'true') {
				$a = $i.find('input,select,textarea,button').not(':hidden,:disabled,[readonly]').eq(0);
				if ($a.length < 1) {
					$a = $i.find('a').not(':hidden,:disabled').eq(0);
					if ($a.length < 1) {
						$a = $i;
					}
				}
			} else if (a != 'false') {
				$a = $i.find(a).eq(0);
			}
			
			if ($a && $a.length) {
				var $w = $(window), st = $w.scrollTop(), sl = $w.scrollLeft();
				$a.focus();
				$(window).scrollTop(st).scrollLeft(sl);
				done = true;
			}
		});
	};

	$(window).on('load', function() {
		$('[focusme]').focusme();
	});

})(jQuery);
