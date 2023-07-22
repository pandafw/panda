if (typeof(panda) == "undefined") { panda = {}; }

(function() {
	"use strict";

	$.extend(panda, {
		page_loading: function(timeout) {
			$('body').loadmask({ mask: false, fixed: true, timeout: timeout || 1000 });
		},
		page_sort: function(name, dir) {
			panda.page_loading();
			location.href = $.addQueryParams(location.href, { 's.c': name, 's.d': dir });
			return false;
		},
		page_sort_reverse: function(name, dir) {
			return panda.page_sort(name, dir.toLowerCase() == "asc" ? "desc" : "asc");
		},
		page_goto: function(s) {
			panda.page_loading();
			location.href = $.addQueryParams(location.href, { 'p.s': s });
			return false;
		},
		page_limit: function(l) {
			panda.page_loading();
			location.href = $.addQueryParams(location.href, { 'p.l': l });
			return false;
		}
	});

	function _click(evt) {
		var $el = $(this);
		if ($el.parent().hasClass('disabled')) {
			evt.preventDefault();
		}
		else {
			var pn = $el.attr('pageno');
			if (pn >= 0) {
				var $pg = $el.closest('.p-pager');
				var js = $pg.data('click');
				if (js) {
					js = js.replace('$', pn);
					js = js.replace('#', (pn - 1) * $pg.data('limit'));
					if (eval(js) === false) {
						evt.preventDefault();
					}
				}
			}
		}
	}

	function _setActivePage($p, n) {
		var $u = $p.find('ul.pagination');
		$u.find('li.active').removeClass('active');

		var $n = $u.find('li.p-pager-page');

		var m = $p.data('pages');
		var b = n - Math.floor($n.size() / 2);
		if (b + $n.size() > m) b = m - $n.size() + 1;
		if (b < 1) b = 1;

		var s = $p.data('style');
		if (n > 1) {
			$u.find('.p-pager-first, .p-pager-prev').removeClass('hidden disabled');
			$u.find('.p-pager-prev>a').attr('pageno', n - 1);
		}
		else {
			$u.find('.p-pager-first').addClass(s.contains('F') ? 'disabled' : 'hidden');
			$u.find('.p-pager-prev').addClass(s.contains('P') ? 'disabled' : 'hidden');
		}

		$u.find('.p-pager-ellipsis-left')[b > 1 ? 'removeClass' : 'addClass']('hidden');
		$n.each(function() {
			var $a = $(this).find('a');
			$a.attr('pageno', b).text(b);
			if (b == n) {
				$(this).addClass('active');
			}
			b++;
		});
		$u.find('.p-pager-ellipsis-right')[b <= m ? 'removeClass' : 'addClass']('hidden');

		if (n < m) {
			$u.find('.p-pager-next, .p-pager-last').removeClass('hidden disabled');
			$u.find('.p-pager-next>a').attr('pageno', n + 1);
		}
		else {
			$u.find('.p-pager-next').addClass(s.contains('N') ? 'disabled' : 'hidden');
			$u.find('.p-pager-last').addClass(s.contains('L') ? 'disabled' : 'hidden');
		}
	}
	
	$.fn.ppager = function(api, pno) {
		if (api == 'getActivePage') {
			return this.find('ul.pagination>li.active>a').attr('pageno');
		}
		if (api == 'setActivePage') {
			return this.each(function() { _setActivePage($(this), pno); });
		}
		return this.each(function() {
			var $p = $(this);
			if ($p.attr("ppager") != "true") {
				$p.attr("ppager", "true");
				if ($p.data("click")) {
					$p.find("a[pageno]").click(_click);
				}
			}
		});
	};
	
	// PAGER DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="ppager"]').ppager();
	});
})();
