(function($) {
	"use strict";

	$.fn.textclear = function() {
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
				}
				$t.focus();
				return false;
			}).parent().css('position', 'relative');
		});
	};
	
	// ==================
	$(window).on('load', function () {
		$('[textclear]').textclear();
	});
})(jQuery);
