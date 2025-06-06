(function($) {
	"use strict";

	$.queryArrays = function(s, f) {
		var qa = [], ss = s.split('&');

		for (var i = 0; i < ss.length; i++) {
			var p = ss[i].split('='),
				k = decodeURIComponent(p[0]),
				v = p.length > 1 ? decodeURIComponent(p[1]) : '';

			if (!f || f == k) {
				qa.push({
					name: k,
					value: v
				});
			}
		}
		return qa;
	};

	$.queryParams = function(s) {
		var qs = {}, ss = s.split('&');

		for (var i = 0; i < ss.length; i++) {
			var p = ss[i].split('='),
				k = decodeURIComponent(p[0]),
				v = p.length > 1 ? decodeURIComponent(p[1]) : '';
			if (k in qs) {
				if (!$.isArray(qs[k])) {
					qs[k] = [ qs[k] ];
				}
				qs[k].push(v);
			} else {
				qs[k] = v;
			}
		}
		return qs;
	};

	$.addQueryParams = function(u, p) {
		var i = u.indexOf('#');
		if (i >= 0) {
			u = u.substring(0, i);
		}

		i = u.indexOf('?');
		if (i >= 0) {
			p = $.extend($.queryParams(u), p);
			u = u.substring(0, i);
		}
		return u + '?' + $.param(p);
	};

})(jQuery);
