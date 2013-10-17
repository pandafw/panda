/*
 * This file is part of Pandaw.
 * Copyright(C) 2013 Pandaw Develop Team.
 *
 * Pandaw is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 * 
 * Pandaw is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pandaw. If not, see <http://www.gnu.org/licenses/>.
 */
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
