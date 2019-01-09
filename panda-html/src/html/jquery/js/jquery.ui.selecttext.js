(function($) {
	$.fn.selectText = function() {
		var $t = $(this);
		if ($t.length) {
			var doc = document;
			var el = $t.get(0);
			if (doc.body.createTextRange) {
				var r = doc.body.createTextRange();
				r.moveToElementText(el);
				r.select();
			}
			else {
				if (window.getSelection) {
					var ws = window.getSelection();
					var r = doc.createRange();
					r.selectNodeContents(el);
					ws.removeAllRanges();
					ws.addRange(r);
				}
			}
		}
	};
})(jQuery);
