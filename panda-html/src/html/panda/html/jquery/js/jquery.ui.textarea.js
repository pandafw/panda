(function($) {
	$.fn.enterfire = function() {
		$(this).each(function() {
			var f = $(this).attr('enterfire');
			if (f != 'hooked') {
				$(this).attr('enterfire', 'hooked').keyup(function(evt) {
					if (evt.ctrlKey && evt.which == 13) {
						if (f == 'form' || f == 'submit' || f == 'true') {
							$(this).closest('form').submit();
						}
						else {
							$(f).click();
						}
					}
				});
			}
		});
	};
	
	$.fn.autosize = function() {
		$(this).each(function() {
			var a = $(this).attr('autosize');
			if (a == 'hooked') {
				$(this).css('height', 'auto').height($(this).prop('scrollHeight'));
			}
			else {
				$(this).css('overflow-y', 'hidden').attr('autosize', 'hooked').on('input', function() {
					$(this).css('height', 'auto').height($(this).prop('scrollHeight'));
				});
			}
		});
	};

	$(window).on('load', function() {
		$('textarea[enterfire]').enterfire();
		$('textarea[autosize]').autosize();
	});
})(jQuery);
