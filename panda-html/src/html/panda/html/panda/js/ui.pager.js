(function() {
	"use strict";

	function _click(evt) {
		var $el = $(this);

		if ($el.parent().hasClass('disabled')) {
			evt.preventDefault();
			return;
		}

		var pn = $el.attr('pageno');
		if (pn >= 0) {
			var $pg = $el.closest('.p-pager');
			var js = $pg.data('onclick');
			if (js) {
				js = js.replace('$', pn);
				js = js.replace('#', (pn - 1) * $pg.data('limit'));
				if (eval(js) === false) {
					evt.preventDefault();
				}
			}
		}
	}

	function _setActivePage($p, n) {
		var $u = $p.children('ul.pagination'),
			$n = $u.children('li.page');

		$u.find('li.active').removeClass('active');

		var m = $p.data('pages'), b = n - Math.floor($n.size() / 2);

		if (b + $n.size() > m) {
			b = m - $n.size() + 1;
		}
		if (b < 1) {
			b = 1;
		}

		var s = $p.data('style');
		if (n > 1) {
			$u.children('li.first, li.prev').removeClass('hidden disabled');
			$u.find('.p-pager-prev>a').attr('pageno', n - 1);
		} else {
			$u.children('li.first').addClass(s.contains('F') ? 'disabled' : 'hidden');
			$u.children('li.prev').addClass(s.contains('P') ? 'disabled' : 'hidden');
		}

		$u.children('li.eleft')[b > 1 ? 'removeClass' : 'addClass']('hidden');
		$n.each(function() {
			var $li = $(this);
			$li.find('a').attr('pageno', b).text(b);
			if (b == n) {
				$li.addClass('active');
			}
			b++;
		});
		$u.children('li.eright')[b <= m ? 'removeClass' : 'addClass']('hidden');

		if (n < m) {
			$u.children('li.next, li.last').removeClass('hidden disabled');
			$u.children('li.next>a').attr('pageno', n + 1);
		} else {
			$u.children('li.next').addClass(s.contains('N') ? 'disabled' : 'hidden');
			$u.children('li.last').addClass(s.contains('L') ? 'disabled' : 'hidden');
		}
	}

	$.fn.ppager = function(api, pno) {
		if (api == 'page') {
			if (pno > 0) {
				return this.each(function() { _setActivePage($(this), pno); });
			}
			return this.find('ul.pagination>li.active>a').attr('pageno');
		}
		return this.each(function() {
			var $p = $(this);
			if ($p.attr("ppager") != "true") {
				$p.attr("ppager", "true");
				if ($p.data("onclick")) {
					$p.find("a[pageno]").click(_click);
				}
			}
		});
	};

	// PAGER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="ppager"]').ppager();
	});
})();
