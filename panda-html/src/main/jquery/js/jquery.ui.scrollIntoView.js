(function($) {
	$.fn.scrollIntoView = function() {
		if (this.length) {
			this.get(0).scrollIntoView();
		}
		return this;
	};
})(jQuery);
