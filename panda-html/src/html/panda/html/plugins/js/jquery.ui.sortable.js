(function($) {
	'use strict';

	function sortable_click(evt) {
		var $e = $(evt.target),
			col = $e.data('sortCol') || $e.text(),
			dir = $e.data('sortDir') || '';

		if ($e.hasClass('sorted')) {
			dir = $e.hasClass('asc') ? 'desc' : 'asc';
		}

		$(this).trigger('sort.sortable', [ col, dir ]);
	}

	$.fn.sortable = function(api, col, dir) {
		if (api == 'sorted') {
			this.find('.sortable').removeClass('sorted desc asc')
				.filter('[data-sort-col="' + col + '"]').addClass('sorted ' + (dir || 'asc'));
			return this;
		}
		return this.addClass('ui-sortable').off('click.sortable').on('click.sortable', '.sortable', sortable_click);
	};

	// SORTABLE DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="sortable"]').sortable();
	});
})(jQuery);
