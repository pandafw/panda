(function($) {
	"use strict";

	$.fn.scrollIntoView = function(speed, easing, callback) {
		if (!this.length) {
			return this;
		}

		var $e = this.first(), $w = $(window), eo = $e.offset(),
			wh = $w.height(), ww = $w.width(),
			st = $w.scrollTop(), sb = st + wh, sl = $w.scrollLeft(), sr = sl + ww,
			et = eo.top, eh = $e.outerHeight(), eb = et + eh,
			el = eo.left, ew = $e.outerWidth(), er = el + ew,
			x = sl > er ? el : (sr < el ? (ew > ww ? el : el - (ww - ew)) : -1),
			y = st > eb ? et : (sb < et ? (eh >= wh ? et : et - (wh - eh)) : -1);

		var ss = {};
		if (x >= 0) { ss.scrollLeft = x; }
		if (y >= 0) { ss.scrollTop = y; }
		$('html').animate(ss, speed, easing, callback);
		return this;
	};

})(jQuery);
