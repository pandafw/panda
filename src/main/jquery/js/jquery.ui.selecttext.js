(function($) {
	$.fn.selectText = function(inp) {
		if (inp) {
			this.find('input').each(function() {
				if ($(this).prev().length == 0
						|| !$(this).prev().hasClass('p_copy')) {
					$('<p class="p_copy" style="position: absolute; z-index: -1;"></p>').insertBefore($(this));
				}
				$(this).prev().html($(this).val());
			});
		}

		var doc = document;
		var el = this[0];
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
	};
})(jQuery);
