(function($) {
	'use strict';

	function sortable_onclick(evt) {
		var $e = $(evt.target),
			col = $e.data('sortCol') || $e.text(),
			dir = $e.data('sortDir') || '';

		if ($e.hasClass('sorted')) {
			dir = $e.hasClass('asc') ? 'desc' : 'asc';
		}

		$(this).trigger('sort.sortable', [ col, dir ]);
	}

	function set_sorted($s, col, dir) {
		$s.find('.sortable').removeClass('sorted desc asc')
			.filter('[data-sort-col="' + col + '"]').addClass('sorted ' + (dir || 'asc'));
	}

	$.fn.sortable = function(api, col, dir) {
		if (api == 'sorted') {
			set_sorted(this, col, dir);
			return this;
		}

		return this.addClass('ui-sortable')
			.off('click.sortable')
			.on('click.sortable', '.sortable', sortable_onclick)
			.each(function() {
				var $t = $(this), c = $t.data('sortedCol');
				if (c) {
					set_sorted($t, c, $t.data('sortedDir'));
				}
			});
	};

	// SORTABLE DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="sortable"]').sortable();
	});
})(jQuery);
