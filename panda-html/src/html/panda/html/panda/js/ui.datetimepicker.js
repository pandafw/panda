(function($) {
	var langs = {};

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

		$('.p-datepicker, .p-datetimepicker, .p-timepicker').datetimepicker();
	}

	$(window).on('load', function () {
		var $dps = $('.p-datepicker, .p-datetimepicker, .p-timepicker');
		if ($dps.size()) {
			var css = $dps.data('datetimepickerCss');
			if (css) {
				$.jcss(css);
			}
			var js = $dps.data('datetimepickerJs');
			if (js) {
				$.jscript(js);
			}
			$dps.each(function() {
				var i = $(this).data('language');
				var v = $(this).data('datetimepickerLangJs');
				if (i && v && !langs[i]) {
					$.jscript(v);
					langs[i] = v;
				}
			});
			initDateTimePicker();
		}
	});
})(jQuery);
