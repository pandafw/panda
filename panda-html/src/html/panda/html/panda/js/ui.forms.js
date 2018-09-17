(function($) {
	function hookForm() {
		$('form').each(function() {
			var $t = $(this);
			if ($t.data("actionHooked")) {
				return;
			}
			$t.data('actionHooked', true)
				.find('input[data-action], button[data-action]').click(function() {
					$i = $(this);
					$i.closest('form').attr('action', $i.data('action'));
				});
		});
	}
	
	$(window).on('load', function() {
		hookForm();
	});
})(jQuery);
