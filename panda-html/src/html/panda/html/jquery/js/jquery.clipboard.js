(function($) {
	$.copyToClipboard = function(s) {
		if (window.clipboardData) {
			// ie
			clipboardData.setData('Text', s);
			return;
		}

		var $t = $('<textarea>').css({ 'width' : '0px', 'height': '0px' }).html(s.escapeHtml());
		var l = $t.val().length;
		var t = $t.get(0);
		
		t.setSelectionRange(0, l);
		document.execCommand('copy');
		t.blur();
	}
})(jQuery);

