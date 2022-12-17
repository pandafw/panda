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

	function _autosize() {
		var $t = $(this);
		$t.css('height', 'auto').height($t.prop('scrollHeight'));
	}

	$.fn.autosize = function() {
		$(this).off('input.autosize').on('input.autosize', _autosize).css({
			'overflow-y': 'hidden',
			'resize': 'none'
		});
		_autosize.call(this);
	};

	$(window).on('load', function() {
		$('textarea[enterfire]').enterfire();
		$('textarea[autosize]').autosize();
	});

})(jQuery);
