(function($) {
	$.jscript = function(url) {
		var s = document.createElement('script');
		s.type = 'text/javascript';
		s.async = true;
		s.src = url;
		$('body').append(s);
		return $;
	};
})(jQuery);

