(function($) {
	$.jscript = function(url, async) {
		var s = document.createElement('script');
		s.type = 'text/javascript';
		s.async = async;
		s.src = url;
		$('body').append(s);
		return $;
	};
})(jQuery);

