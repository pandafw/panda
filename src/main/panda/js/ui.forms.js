(function($) {
	function focusForm($i) {
		$i = $i.find('input,select,textarea,button');
		$i = $i.not(':hidden,:disabled,[readonly]').eq(0);
		if ($i.length > 0) {
			$i.focus();
			$('body').scrollTop(0).scrollLeft(0);
		}
	}

	$(window).on('load', function() {
		var $i = $('form[initfocus="true"]').eq(0).attr('initfocus', 'focus');
		focusForm($i);
	});
})(jQuery);