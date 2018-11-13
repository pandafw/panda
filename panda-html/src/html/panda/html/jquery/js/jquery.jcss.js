(function($) {
	$.jcss = function(url, dup) {
		if (dup && $('link[href="' + url + '"]').size()) {
			return false;
		}
		$('<link>').attr({ type: 'text/css', rel: 'stylesheet', href: url}).appendTo('body');
		return true;
	};
})(jQuery);

