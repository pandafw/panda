if (typeof(panda) == "undefined") { panda = {}; }

(function($) {
	function ajaxFormSubmit() {
		var $f = $(this);
		var $a = $('#' + $f.attr('id') + '_alert').empty();

		$f.find('.has-error').removeClass('has-error').end().find('.p-field-errors').remove();
		if (!$f.isLoadMasked()) {
			$f.loadmask();
		}

		$.ajax({
			url: $f.data('ajaxAction'),
			method: 'post',
			data: $f.serializeArray(),
			dataType: 'json',
			success: function(d) {
				$a.palert('actionAlert', d, $f);
				if (d.result) {
					$f.vals(d.result);
					$f.find('div.p-datepicker, div.p-datetimepicker, div.p-timepicker').each(function() {
						var v = $(this).find('input').val();
						if (v) {
							$(this).datetimepicker('setValue', new Date(v));
						}
					});
				}
				if ($a.children().length) {
					$a.scrollIntoView();
				}
			},
			error: function(xhr, status, err) {
				$a.palert('ajaxJsonError', xhr, status, err, $f.data('ajaxServerError'));
			},
			complete: function() {
				$f.unloadmask();
			}
		});
		return false;
	}
			
	function ajaxSubmitHook($f) {
		// hook submit
		if (!$f.data("ajaxSubmitHooked") && $f.data('ajaxAction')) {
			$f.data('ajaxSubmitHooked', true).submit(ajaxFormSubmit);
		}
	}

	function ajaxLoadInnerForm($f) {
		var $c = $f.closest('.p-popup, .p-inner');
		var data = $f.serializeArray();
		if ($c.hasClass('p-inner')) {
			data.push({ name: '__inner', value: 'true' });
		}
		else {
			data.push({ name: '__popup', value: 'true' });
		}
		
		if ($f.attr('loadmask') != 'false') {
			$c.parent().loadmask();
		}

		$.ajax({
			url: $f.attr('action'),
			data: data,
			dataType: 'html',
			success: function(html, ts, xhr) {
				$c.parent().html(html);
			},
			error: function(xhr, ts, err) {
				$c.parent().html(xhr.responseText);
			},
			complete: function(xhr, ts) {
				$c.parent().unloadmask();
			}
		});
	}

	function actionHook($f) {
		// hook action
		if (!$f.data("actionHooked")) {
			$f.data('actionHooked', true)
				.find('input[data-action], button[data-action]').click(function() {
					$(this).closest('form').attr('action', $(this).data('action'));
				});
		}
	}

	function isSelfForm($f) {
		var t = $f.attr('target') || '_self';
		return t == '_self' || t == '_top' || t == '_parent';
	}

	function innerHook($f) {
		// hook inner, popup
		if ($f.data("hooked")) {
			return;
		}
		var $c = $f.closest('.p-popup, .p-inner');
		if ($c.length > 0) {
			$f.data('hooked', true);
			$f.submit(function(e) {
				e.preventDefault();
				ajaxLoadInnerForm($(this));
				return false;
			});
		}
	}

	function loadmaskHook($f) {
		// hook loadmask
		if ($f.data("hooked")) {
			return;
		}

		if ($f.height() > 20 && $f.attr('loadmask') != 'false') {
			$f.data('hooked', true);
			$f.submit(function() {
				$(this).loadmask();
			})
		}
	}

	$(window).on('load', function() {
		$('form').each(function() {
			var $f = $(this);

			// hook action
			actionHook($f);

			if (isSelfForm($f)) {
				// hook inner, popup
				innerHook($f);

				// hook loadmask
				if (panda.enable_loadmask_form) {
					loadmaskHook($f);
				}
				
				ajaxSubmitHook($f);
			}
		});
	});
})(jQuery);
