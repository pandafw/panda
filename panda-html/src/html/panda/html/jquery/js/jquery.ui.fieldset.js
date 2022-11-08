(function($) {
	"use strict";

	function collapse($f) {
		if (!$f.hasClass('collapsed')) {
			$f.addClass('collapsed').children(':not(legend)').slideUp();
		}
	}
	
	function expand($f) {
		if ($f.hasClass('collapsed')) {
			$f.removeClass('collapsed').children(':not(legend)').slideDown();
		}
	}
	
	$.fn.fieldset = function(config) {
		config = config || {};
		return this.each(function() {
			var $f = $(this);
			if (!$f.data('fieldset')) {
				var $l = $f.children('legend'), c = config.collapsed && !($f.hasClass('collapsed'));
				$f.data('fieldset', config).addClass('ui-fieldset' + (c ? ' collapsed' : ''));
				$l.click(function() {
					var $f = $(this).closest('fieldset');
					if ($f.hasClass('collapsed')) {
						expand($f);
					}
					else {
						collapse($f);
					}
				});

				c = $f.hasClass('collapsed');
				$f.children(':not(legend)')[c ? 'hide' : 'show']();
			}
			
			switch(config) {
			case 'collapse':
				collapse($f);
				break;
			case 'expand':
				expand($f);
				break;
			}
		});
	};

	// FIELDSET DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="fieldset"]').fieldset();
	});
})(jQuery);
