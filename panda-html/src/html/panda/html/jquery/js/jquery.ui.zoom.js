(function($) {
	"use strict";

	$.each({
		zoomIn: { opacity: 'show' },
		zoomOut: { opacity: 'hide' },
	}, function(name, props) {
		$.fn[name] = function(speed, easing, callback) {
			var opt = $.speed(speed, easing, callback);
			var old = opt.step;
			opt.step = function(s) {
				$(this).css({ transform: 'scale(' + s + ')' });
				if (old) {
					old.call(this, s);
				}
			}
			return this.animate(props, opt);
		};
	});

})(jQuery);
