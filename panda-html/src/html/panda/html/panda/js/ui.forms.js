(function($) {
	function focusForm() {
		var f = false;
		$('form[focusme]').each(function() {
			var $i = $(this);
			if (f) {
				$i.removeAttr('focusme');
				return;
			}

			var a = $i.attr('focusme');
			$i.removeAttr('focusme');

			var $a = null;
			if (a == 'true') {
				$a = $i.find('input,select,textarea,button').not(':hidden,:disabled,[readonly]').eq(0);
			}
			else if (a != '' && a != 'false') {
				$a = $i.find(a).eq(0);
			}
			
			if ($a && $a.length) {
				f = true;
				var $w = $(window), st = $w.scrollTop(), sl = $w.scrollLeft();
				$a.focus();
				$(window).scrollTop(st).scrollLeft(sl);
			}
		});
	}

	function fireForm() {
		$('textarea[enterfire]').each(function() {
			var f = $(this).attr("enterfire");
			if (f) {
				$(this).removeAttr("enterfire").keyup(function(evt) {
					if (evt.ctrlKey && evt.which == 13) {
						if (f == "form") {
							$(this).closest("form").submit();
						}
						else {
							$(f).click();
						}
					}
				});
			}
		});
	}

	function hookForm() {
		$('form').each(function() {
			var $t = $(this);
			if ($t.data("actionHooked")) {
				return;
			}
			$t.data('actionHooded', true)
				.find('input[data-action], button[data-action]').click(function() {
					$i = $(this);
					$i.closest('form').attr('action', $i.data('action'));
				});
		});
	}
	
	$(window).on('load', function() {
		hookForm();
		fireForm();
		focusForm();
	});
})(jQuery);
