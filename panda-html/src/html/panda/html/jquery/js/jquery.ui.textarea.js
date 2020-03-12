(function($) {
	function enterfire() {
		$('textarea[enterfire]').each(function() {
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
	}
	
	function autosize() {
		$('textarea[autosize]').each(function() {
			var a = $(this).attr('autosize').charAt(0).toLowerCase();
			if ('1tye'.indexOf(a) >= 0) {
				$(this).css('overflow-y', 'hidden').attr('autosize', 'hooked').on('input', function() {
					var $t = $(this);
					$t.css('height', 'auto').height($t.prop('scrollHeight'));
				});
			}
		});
	}

	$(window).on('load', function() {
		enterfire();
		autosize();
	});
})(jQuery);
