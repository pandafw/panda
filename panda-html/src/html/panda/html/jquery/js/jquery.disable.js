(function ($) {
	"use strict";

	$.fn.disable = function(state) {
		return this.each(function() {
			this.disabled = state;
		});
	};
})(jQuery);
