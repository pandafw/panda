(function($) {
	$.fn.changeValue = function(v) {
		var o = this.val();
		
		this.val(v);
		if (o != v) {
			this.trigger('change');
		}
	};
})(jQuery);
