(function($) {
	"use strict";

	$.copyToClipboard = function(s) {
		if (window.clipboardData) {
			// ie
			clipboardData.setData('Text', s);
			return;
		}

		var $t = $('<textarea>')
			.css({ width: 0, height: 0 })
			.text(s)
			.appendTo('body');

		$t.get(0).select();

		document.execCommand('copy');

		$t.remove();
	};
})(jQuery);

