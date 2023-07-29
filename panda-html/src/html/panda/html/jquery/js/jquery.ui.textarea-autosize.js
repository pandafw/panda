(function($) {
	"use strict";

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
		$('textarea[autosize]').autosize();
	});

})(jQuery);
