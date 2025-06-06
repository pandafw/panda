(function($) {
	"use strict";

	function _enterfire(evt) {
		if (evt.ctrlKey && evt.key == 'Enter') {
			var $t = $(this), ef = $t.attr('enterfire') || 'true';
			if (ef == 'true' || ef == 'form' || ef == 'submit') {
				$t.closest('form').submit();
			} else {
				$(ef).click();
			}
		}
	}

	$.fn.enterfire = function() {
		return this.off('keyup.enterfire').on('keyup.enterfire', _enterfire);
	};

	$(window).on('load', function() {
		$('textarea[enterfire]').enterfire();
	});

})(jQuery);
