(function($) {
	$.jscript = function(url, async) {
		if ($('script[src="' + url + '"]').size()) {
			return false;
		}

		var s = document.createElement('script');
		s.type = 'text/javascript';
		s.async = async;
		s.src = url;
		$('body').append(s);
		return true;
	};
})(jQuery);

