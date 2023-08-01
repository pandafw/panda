(function($) {
	"use strict";

	$(window).on('load', function() {
		$('input[data-action], button[data-action]').off('click.action').on('click.action', function() {
			$(this).closest('form').attr('action', $(this).data('action'));
		});
	});

})(jQuery);

