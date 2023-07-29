(function($) {
	"use strict";

	$.fn.textClear = function() {
		return this.each(function() {
			var $t = $(this);
			if ($t.data('textClear')) {
				return;
			}

			$t.data('textClear', true);

			var $i = $('<i class="ui-text-clear">&times;</i>');
			$t.addClass('ui-has-text-clear');
			$i.insertAfter($t).click(function() {
				if ($t.val() != '') {
					$t.focus().val('').trigger('change');
				}
			});
		});
	};
	
	// DATA-API
	// ==================
	$(window).on('load', function () {
		$('[text-clear]').textClear();
	});
})(jQuery);
