(function($) {
	$.copyToClipboard = function(s) {
		if (window.clipboardData) {
			// ie
			clipboardData.setData('Text', s);
			return;
		}

		var $t = $('<textarea>').css({ 'width' : '0px', 'height': '0px' }).text(s);
		$('body').append($t);

		$t.get(0).select();
		document.execCommand('copy');

		$t.remove();
	};
})(jQuery);

