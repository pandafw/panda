(function($) {
	"use strict";

	$.fn.checkAll = function(target) {
		$(this).each(function() {
			var $ca = $(this), $ct = $(target || $ca.attr('checkall'));
			$ca.change(function(evt, sup) {
				if (!sup) {
					var c = $ca.prop('checked');
					$ct.each(function() {
						var $t = $(this), o = $t.prop('checked');
						if (c != o) {
							$t.prop('checked', c).trigger('change');
						}
					});
				}
			});
			$ct.change(function() {
				var tz = $ct.length, cz = $ct.filter(':checked').length, c = (tz > 0 && tz == cz);
				if (c != $ca.prop('checked')) {
					$ca.prop('checked', c).trigger('change', true);
				}
			}).first().trigger('change');
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[checkall]').checkAll();
	});
})(jQuery);
