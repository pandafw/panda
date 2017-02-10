(function($) {
	$.fn.selectText = function() {
		if ($(this).length) {
			if (document.selection) {
				var range = document.body.createTextRange();
				range.moveToElementText($(this).get(0));
				range.select();
			}
			else if (window.getSelection) {
				var range = document.createRange();
				range.selectNode($(this).get(0));
				window.getSelection().addRange(range);
			}
		}
	}
})(jQuery);

