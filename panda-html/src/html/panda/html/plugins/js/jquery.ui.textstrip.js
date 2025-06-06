(function($) {
	"use strict";

	var E = 'blur.textstrip', re = /^[\s\u3000]+|[\s\u3000]+$/g;

	function _textstrip() {
		var $t = $(this), a = $t.attr('textstrip') || '';
		if (a != 'false') {
			$t.val(($t.val() || '').replace(re, ''));
		}
	}

	$.fn.textstrip = function() {
		return this.off(E).on(E, _textstrip);
	};
	
	// ==================
	$(window).on('load', function () {
		$('[textstrip]').textstrip();
	});
})(jQuery);
