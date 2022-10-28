(function ($) {
	"use strict";

	$.fn.replaceClass = function(s, t) {
		return this.removeClass(s).addClass(t);
	};
})(jQuery);
