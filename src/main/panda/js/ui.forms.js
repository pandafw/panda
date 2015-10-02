(function($) {
	function focusForm() {
		$i = $('form[initfocus="true"]').eq(0).attr('initfocus', 'focus');
		$i = $i.find('input,select,textarea,button');
		$i = $i.not(':hidden,:disabled,[readonly]').eq(0);
		if ($i.length > 0) {
			$i.focus();
			$('body').scrollTop(0).scrollLeft(0);
		}
	}

	function actionForm() {
		$('input[data-action], button[data-action]').click(function() {
			$i = $(this);
			$i.closest('form').attr('action', $i.data('action'));
		});
	}
	
	$(window).on('load', function() {
		focusForm();
		actionForm();
	});
})(jQuery);
