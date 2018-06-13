(function($) {
	$.jscript = function(url, sync) {
		var s = document.createElement('script');
		s.type = 'text/javascript';
		s.async = !sync;
		s.src = url;
		$('body').append(s);
		return $;
	};
})(jQuery);

