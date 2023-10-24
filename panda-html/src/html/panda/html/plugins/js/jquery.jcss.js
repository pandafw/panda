(function($) {
	"use strict";

	$.jcss = function(url) {
		if ($('link[href="' + url + '"]').length) {
			return false;
		}
		$('<link>').attr({ type: 'text/css', rel: 'stylesheet', href: url }).appendTo('head');
		return true;
	};
})(jQuery);

