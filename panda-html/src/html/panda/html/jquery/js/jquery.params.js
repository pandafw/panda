(function($) {
	$.queryArrays = function(s, f) {
		var qs = [], i = s.indexOf('#');
		if (i >= 0) {
			s = s.substring(0, i);
		}

		i = s.indexOf('?');
		if (i >= 0) {
			s = s.substring(i + 1);
		}

		var ss = s.split('&');
		for (i = 0; i < ss.length; i++) {
			var pv = ss[i].split('=');
			var n = decodeURIComponent(pv[0]);
			if (f == null || f == n) {
				qs.push({
					name: n,
					value: pv.length > 1 ? decodeURIComponent(pv[1]) : ''
				});
			}
		}
		return qs;
	};

	$.queryParams = function(s) {
		var qs = {}, i = s.indexOf('#');
		if (i >= 0) {
			s = s.substring(0, i);
		}
		
		i = s.indexOf('?');
		if (i >= 0) {
			s = s.substring(i + 1);
		}
		
		var ss = s.split('&');
		for (i = 0; i < ss.length; i++) {
			var pv = ss[i].split('=');
			var n = decodeURIComponent(pv[0]);
			qs[n] = pv.length > 1 ? decodeURIComponent(pv[1]) : '';
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


