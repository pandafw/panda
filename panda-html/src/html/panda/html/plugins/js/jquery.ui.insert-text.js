(function($) {
	"use strict";

	$.fn.insertText = function(s, append) {
		return this.each(function() {
			var $t = $(this), tv = $t.val();
			var ss = $t.prop('selectionStart') || (append ? tv.length : 0);
			var tb = tv.substring(0, ss), ta = tv.substring(ss);

			$t.val(tb + s + ta).prop('selectionEnd', tb.length + s.length);
		});
	};

})(jQuery);
