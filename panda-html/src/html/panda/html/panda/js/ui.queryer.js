(function($) {
	function _pqr_init($qr) {
		if ($qr.data("pqueryer")) {
			return;
		}
		$qr.data("pqueryer", true);
		_pqr_init_filters($qr);
	}

	function _pqr_init_filters($qr) {
		$qr.find(".p-qr-filters")
			.find('.form-group')
				.each(function() {
					if ($(this).hasClass('p-hidden')) {
						$(this).find("input,select,textarea").prop('disabled', true);
					}
				}).end()
			.find('.p-qr-remove').click(_pqr_onDelFilter).end()
			.find('.p-qr-clear').click(_pqr_onClearFilters).end()
			.find('.p-qr-select').change(_pqr_onAddFilter).end()
			.find('.p-qr-f-number-c, .p-qr-f-date-c, .p-qr-f-datetime-c, .p-qr-f-time-c')
				.on('change', _pqr_onBetweenChange)
				.end()
			.find('form')
				.submit(function() {
					$qr.loadmask();
				});
	}

	function _pqr_clearFieldValue() {
		switch (this.tagName) {
		case "INPUT":
			switch (this.type) {
			case "radio":
				if (this.name != 'm' && !this.name.endsWith(".m")) {
					this.checked = false;
				}
				break;
			case "checkbox":
				this.checked = false;
				break;
			case "text":
				this.value = "";
				break;
			}
			break;
		case "SELECT":
			if (!this.name.endsWith(".c")) {
				this.selectedIndex = 0;
			}
			break;
		case "TEXTAREA":
			this.value = "";
			break;
		}
	}

	function _pqr_onBetweenChange() {
		var $t = $(this);
		if ($t.val() == 'bt') {
			$t.nextAll().removeClass('p-hidden').find('INPUT').prop('disabled', false);
		}
		else {
			$t.nextAll().addClass('p-hidden').find('INPUT').prop('disabled', true);
		}
	}

	function _pqr_onAddFilter() {
		var e = this;
		if (e.selectedIndex > 0) {
			$(e).closest(".p-qr-filters")
				.find('.p-qr-fsi-' + e.value)
					.removeClass('p-hidden')
					.find("input,select,textarea").prop('disabled', false).end()
					.end()
				.fieldset('expand');
			e.options[e.selectedIndex].disabled = true;
			e.selectedIndex = 0;
		}
		return false;
	}

	function _pqr_onDelFilter() {
		var $g = $(this).closest(".form-group");
		$g.addClass('p-hidden')
			.find("input,select,textarea")
				.prop('disabled', true)
				.each(_pqr_clearFieldValue)
				.end()
			.find(".p-label-error")
				.removeClass('p-label-error')
				.end()
			.find(".p-field-errors")
				.remove()
				.end()
			.closest(".p-qr-filters")
				.find('.p-qr-select > option[value=' + $g.data('item') + ']')
					.prop('disabled', false);
	}

	function _pqr_onClearFilters() {
		$(this).closest(".p-qr-form")
			.find(".p-field-errors")
				.remove()
				.end()
			.find(".p-label-error")
				.removeClass('p-label-error')
				.end()
			.find("input,select,textarea")
				.each(_pqr_clearFieldValue);
		return false;
	}

	$(window).on('load', function() {
		$('div.p-qr').each(function() {
			_pqr_init($(this));
		});
	});
})(jQuery);

