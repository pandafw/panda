(function($) {
	/**
	 * Move the selected element(s) to the center position of parent element (default: body)
	 */
	$.fn.center = function(c) {
		c = c || {};
		return this.each(function() {
			var $s = $(this),
				$t = c.target ? $(c.target) : $s.parent();

			var tl = $t.scrollLeft(),
				tt = $t.scrollTop(),
				tw = $t.width(),
				th = $t.height(),
				ow = $s.outerWidth(),
				oh = $s.outerHeight();
			
			try {
				if (c.relative === false || (c.relative !== true && $t.css('position') != 'relative')) {
					var r = $t.offset();
					tl = r.left;
					tt = r.top;
				}
			}
			catch (e) {
			}

			var ot = Math.round(tt + (th - oh) / 2),
				ol = Math.round(tl + (tw - ow) / 2);
			$s.css({
				top: ot + "px",
				left: ol + "px"
			});
		});
	};
})(jQuery);
