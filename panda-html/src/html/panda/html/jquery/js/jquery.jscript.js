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
	
	// enable script cache
	$.enableScriptCache = function() {
		$.ajaxPrefilter(function(options, org, xhr) {
			if (options.dataType == 'script' || org.dataType == 'script') {
				options.cache = true;
			}
		});
	}
})(jQuery);

