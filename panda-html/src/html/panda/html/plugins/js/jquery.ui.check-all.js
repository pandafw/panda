(function($) {
	"use strict";

	$.fn.checkAll = function(target) {
		$(this).each(function() {
			var $a = $(this), $g = $(target || $a.attr('checkall'));
			$a.change(function(evt, sup) {
				if (!sup) {
					var c = $a.prop('checked');
					$g.each(function() {
						var $t = $(this), o = $t.prop('checked');
						if (c != o) {
							$t.prop('checked', c).trigger('change');
						}
					});
				}
			});
			$g.change(function() {
				var gz = $g.length, cz = $g.filter(':checked').length, c = (gz > 0 && gz == cz);
				if (c != $a.prop('checked')) {
					$a.prop('checked', c).trigger('change', true);
				}
			});
		});
	};


	// POPUP DATA-API
	// ==================
	$(window).on('load', function() {
		$('[checkall]').checkAll();
	});
})(jQuery);
