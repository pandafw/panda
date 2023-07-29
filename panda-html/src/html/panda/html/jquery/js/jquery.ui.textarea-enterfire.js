(function($) {
	"use strict";

	function _enterfire(evt) {
		if (evt.ctrlKey && evt.which == 13) {
			var $t = $(this), ef = $t.attr('enterfire');
			if (ef == 'form' || ef == 'submit' || ef == 'true') {
				$t.closest('form').submit();
			} else {
				$(ef).click();
			}
		}
	}

	$.fn.enterfire = function() {
		$(this).off('keyup.enterfire').on('keyup.enterfire', _enterfire);
	};

	$(window).on('load', function() {
		$('textarea[enterfire]').enterfire();
	});

})(jQuery);
