(function($) {
	"use strict";

	$.fn.enableBy = function(target) {
		$(this).each(function() {
			var $a = $(this), $cs = $(target || $a.attr('enableby'));
			if ($cs.length) {
				$cs.change(function() {
					$a.prop('disabled', $cs.filter(':checked').length == 0);
				}).trigger('change');
			} else {
				$a.prop('disabled', true);
			}
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[enableby]').enableBy();
	});
})(jQuery);
