// jQuery Nice Select - v1.1.0
// https://github.com/hernansartorio/jquery-nice-select
// Made by Hernán Sartorio
// Modified by Frank Wang

(function($) {
	"use strict";

	function __document_click(evt) {
		if ($(evt.target).closest('.ui-nice-select').length === 0) {
			$('.ui-nice-select').removeClass('open');
		}
	}

	function __dropdown_keydown(evt) {
		var $dropdown = $(this);
		var $focused_option = $($dropdown.find('.focus') || $dropdown.find('ui li.selected'));

		switch (evt.keyCode) {
		case 32: // Space
		case 13: // Enter
			if ($dropdown.hasClass('open')) {
				$focused_option.trigger('click');
			} else {
				$dropdown.trigger('click');
			}
			return false;
		case 40: // Down
			if (!$dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			} else {
				var $next = $focused_option.nextAll('li:not(.disabled)').first();
				if ($next.length > 0) {
					$dropdown.find('.focus').removeClass('focus');
					$next.addClass('focus');
				}
			}
			return false;
		case 38: // Up
			if (!$dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			} else {
				var $prev = $focused_option.prevAll('li:not(.disabled)').first();
				if ($prev.length > 0) {
					$dropdown.find('.focus').removeClass('focus');
					$prev.addClass('focus');
				}
			}
			return false;
		case 27: // Esc
			if ($dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			}
			break;
		case 9: // Tab
			if ($dropdown.hasClass('open')) {
				return false;
			}
		}
	}

	function __dropdown_click(evt) {
		evt.stopPropagation();

		var $dropdown = $(this);

		$('.ui-nice-select').not($dropdown).removeClass('open');
		$dropdown.toggleClass('open');

		if ($dropdown.hasClass('open')) {
			$dropdown.find('li');
			$dropdown.find('.focus').removeClass('focus');
			$dropdown.find('.selected').addClass('focus');

			// Close when clicking outside
			$(document).on('click.nice_select', __document_click);
		} else {
			$dropdown.focus();

			// Unbind existing events in case that the plugin has been initialized before
			$(document).off('.nice_select');
		}
	}

	function __dropdown_option_click() {
		var $option = $(this);
		var $dropdown = $option.closest('.ui-nice-select');

		$dropdown.find('.selected').removeClass('selected');
		$option.addClass('selected');

		var text = $option.data('display') || $option.text();
		$dropdown.find('.current').text(text);

		$dropdown.prev('select').val($option.data('value')).trigger('change');
	}

	function update() {
		this.each(function() {
			var $select = $(this);
			var $dropdown = $select.next('.ui-nice-select');

			if ($dropdown.length) {
				$dropdown.remove();
				create_nice_select($select);

				if ($dropdown.hasClass('open')) {
					$select.next().trigger('click');
				}
			}
		});
	}

	function destroy() {
		this.each(function() {
			var $select = $(this);
			var $dropdown = $select.next('.ui-nice-select');

			if ($dropdown.length) {
				$dropdown.remove();
				$select.css('display', '');
			}
		});
		if ($('.ui-nice-select').length == 0) {
			$(document).off('.nice_select');
		}
	}

	function no_css_pointer_events() {
		// Detect CSS pointer-events support, for IE <= 10. From Modernizr.
		var style = document.createElement('a').style;
		style.cssText = 'pointer-events:auto';
		if (style.pointerEvents !== 'auto') {
			$('html').addClass('ui-nice-select-no-csspointerevents');
		}
	}

	function create_nice_select($select) {
		$select.after($('<div></div>')
			.addClass('ui-nice-select')
			.addClass($select.attr('class') || '')
			.addClass($select.attr('disabled') ? 'disabled' : '')
			.attr('tabindex', $select.attr('disabled') ? null : '0')
			.html('<span class="current"></span><ul></ul>')
		);

		var $dropdown = $select.next();
		var $options = $select.find('option');
		var $selected = $select.find('option:selected');

		$dropdown.find('.current').html($selected.data('display') || $selected.text());

		$options.each(function() {
			var $option = $(this);
			var display = $option.data('display');

			$dropdown.find('ul').append($('<li></li>')
				.attr('data-value', $option.val())
				.attr('data-display', (display || null))
				.addClass(
					($option.is(':selected') ? ' selected' : '') +
					($option.is(':disabled') ? ' disabled' : ''))
				.html($option.text())
			);
		});

		// Open, close
		$dropdown.click(__dropdown_click);

		// Keyboard events
		$dropdown.keydown(__dropdown_keydown);

		// Option click
		$dropdown.on('click', 'li:not(.disabled)', __dropdown_option_click);
	}

	var api = {
		'update': update,
		'destroy': destroy
	};

	$.fn.niceSelect = function(method) {
		// Methods
		if (typeof method == 'string') {
			api[method].apply(this);
			return this;
		}

		// Hide native select
		this.hide();

		// Create custom markup
		this.each(function() {
			var $select = $(this);

			if (!$select.next().hasClass('ui-nice-select')) {
				create_nice_select($select);
			}
		});

		return this;
	};

	// niceSelect DATA-API
	// ==================
	$(window).on('load', function() {
		no_css_pointer_events();
		$('[data-spy="niceSelect"]').niceSelect();
	});

}(jQuery));
