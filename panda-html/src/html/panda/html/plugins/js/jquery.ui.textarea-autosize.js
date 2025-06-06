(function($) {
	"use strict";

	function _autosize() {
		var $t = $(this);
		$t.css('height', 'auto').outerHeight($t.prop('scrollHeight'));
	}

	var E = 'input.autosize';

	$.fn.autosize = function() {
		return this.off(E).on(E, _autosize).css({
			'overflow-y': 'hidden',
			'resize': 'none'
		}).trigger('input');
	};

	$(window).on('load', function() {
		$('textarea[autosize]').autosize();
	});

})(jQuery);
