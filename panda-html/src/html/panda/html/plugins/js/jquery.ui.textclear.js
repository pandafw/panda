(function($) {
	"use strict";

	$.fn.textClear = function() {
		return this.each(function() {
			var $t = $(this);
			if ($t.hasClass('ui-has-textclear')) {
				return;
			}

			$t.addClass('ui-has-textclear');

			var $i = $('<i class="ui-close ui-textclear"></i>');
			$i.insertAfter($t).click(function() {
				if ($t.val() != '') {
					$t.val('').trigger('input').trigger('change');
					if ($t.attr('textclear') == 'focus') {
						$t.focus();
					}
				}
			});
		});
	};
	
	// DATA-API
	// ==================
	$(window).on('load', function () {
		$('[textclear]').textClear();
	});
})(jQuery);
