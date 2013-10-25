(function($) {
	function collapse($el) {
		if (!$el.hasClass('ui-collapsed')) {
			$el.addClass('ui-collapsed')
				.children(':not(legend)').slideUp().end()
				.find('.ui-fieldset-icon')
					.removeClass('ui-icon-triangle-1-s')
					.addClass('ui-icon-triangle-1-e');
		}
	}
	
	function expand($el) {
		if ($el.hasClass('ui-collapsed')) {
			$el.removeClass('ui-collapsed')
				.children(':not(legend)').slideDown().end()
				.find('.ui-fieldset-icon')
					.removeClass('ui-icon-triangle-1-e')
					.addClass('ui-icon-triangle-1-s');
		}
	}
	
	$.fn.fieldset = function(config) {
		config = config || {};
		return this.each(function() {
			if (!$(this).data('fieldset')) {
				$(this).data('fieldset', true)
					.addClass('ui-collapsible' + (config.collapse ? ' ui-collapsed' : ''))
					.children('legend')
						.click(function() {
							var $el = $(this).closest('fieldset');
							if ($el.hasClass('ui-collapsed')) {
								expand($el);
							}
							else {
								collapse($el);
							}
						});

				if (config.icon !== false) {
					$(this).children('legend')
						.prepend('<em class="ui-fieldset-icon ui-icon ui-icon-triangle-1-'
								+ ($(this).hasClass('collapsed') ? 'e' : 's')
								+ '"></em>');
				}
				$(this).children(':not(legend)')[config.collapse ? 'hide' : 'show']();
			}
			
			switch(config) {
			case 'collapse':
				collapse($(this));
				break;
			case 'expand':
				expand($(this));
				break;
			}
		});
	};
})(jQuery);
