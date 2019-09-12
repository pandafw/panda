(function($) {
	var langs = {};
	var dps = 'div.p-datepicker, div.p-datetimepicker, div.p-timepicker';

	function initDateTimePicker() {
		if (typeof($.fn.datetimepicker) == 'undefined') {
			setTimeout(initDateTimePicker, 100);
			return;
		}
		for (var i in langs) {
			if (!$.fn.datetimepicker.dates[i]) {
				setTimeout(initDateTimePicker, 100);
				return;
			}
		}

		$(dps).datetimepicker();
	}

	$(window).on('load', function () {
		var $dps = $(dps);
		if ($dps.size()) {
			var css = $dps.data('datetimepickerCss');
			if (css) {
				$.jcss(css);
			}
			var js = $dps.data('datetimepickerJs');
			if (js) {
				$.jscript(js, function() {
					$dps.each(function() {
						var i = $(this).data('language');
						var v = $(this).data('datetimepickerLangJs');
						if (i && v && !langs[i]) {
							$.jscript(v);
							langs[i] = v;
						}
					});
				});
			}
			initDateTimePicker();
		}
	});
})(jQuery);
