(function($) {
	"use strict";

	var E = 'change', P = 'checked';

	$.fn.checkall = function(s) {
		return this.each(function() {
			var $a = $(this),
				b = s || $a.attr('checkall'),
				t = b, f = '',
				i = b.indexOf(' ');

			if (i > 0) {
				t = b.substring(0, i);
				f = b.substring(i+1);
			}

			$a.on(E, function(evt, sup) {
				if (!sup) {
					var c = $a.prop(P);
					$(b)[c ? 'not' : 'filter'](':checked').each(function() {
						$(this).prop(P, c).trigger(E);
					});
				}
			});

			$(t).on(E, f, function() {
				var $b = $(b), bz = $b.length, cz = $b.filter(':checked').length, ca = (bz > 0 && bz == cz);
				if (ca != $a.prop(P)) {
					$a.prop(P, ca).trigger(E, true);
				}
			});
			
			$(b).first().trigger(E);
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[checkall]').checkall();
	});
})(jQuery);
