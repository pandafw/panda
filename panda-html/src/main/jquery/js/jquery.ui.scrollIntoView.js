(function($) {
	$.fn.scrollIntoView = function(c) {
		if (!this.length) {
			return this;
		}
		
		var $e = this.first(), $w = $(window), eo = $e.offset();
		var wh = $w.height(), ww = $w.width();
		var st = $w.scrollTop(), sb = st + wh, sl = $w.scrollLeft(), sr = sl + ww;
		var et = eo.top, eh = $e.outerHeight(), eb = et + eh;
		var el = eo.left, ew = $e.outerWidth(), er = el + ew;
		
		var x = sl > er ? el : (sr < el ? (ew > ww ? el : el - (ww - ew)) : -1);
		var y = st > eb ? et : (sb < et ? (eh >= wh ? et : et - (wh - eh)) : -1);
		
		if (x >= 0 || y >= 0) {
			var ss = {};
			if (x >= 0) { ss.scrollLeft = x; }
			if (y >= 0) { ss.scrollTop = y; }
			var $hb = $('html,body');
			if (c) {
				$hb.animate(ss, c);
			}
			else {
				$hb.prop(ss);
			}
		}
		return this;
	};
})(jQuery);
