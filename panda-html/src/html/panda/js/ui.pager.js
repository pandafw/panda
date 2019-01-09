(function() {
	function _click(evt) {
		var $el = $(this);
		if ($el.parent().hasClass('disabled')) {
			evt.preventDefault();
		}
		else {
			var pn = $el.data("pageno");
			if (pn >= 0) {
				var $pg = $el.closest(".p-pager");
				var js = $pg.data("click");
				if (js.contains('#')) {
					evt.preventDefault();
					js = js.replace("#", (pn - 1) * $pg.data("limit"));
				}
				eval(js);
			}
		}
	}
	
	$.fn.ppager = function(api, pno) {
		return this.each(function() {
			var $p = $(this);
			if ($p.attr("ppager") != "true") {
				$p.attr("ppager", "true");
				if ($p.data("click")) {
					$p.find("a[data-pageno]").click(_click);
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
