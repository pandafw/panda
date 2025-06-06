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
		var $focused = ($dropdown.find('.focus') || $dropdown.find('.selected')).first();

		switch (evt.key) {
		case ' ': // Space
		case 'Enter':
			if ($dropdown.hasClass('open')) {
				$focused.trigger('click');
			} else {
				$dropdown.trigger('click');
			}
			return false;
		case 'ArrowDown':
			if (!$dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			} else {
				var $next = ($focused.length > 0 ? $focused.nextAll('li:not(.disabled)') : $dropdown.find('li:not(.disabled)')).first();
				if ($next.length > 0) {
					$dropdown.find('.focus').removeClass('focus');
					$next.addClass('focus').focus();
				}
			}
			return false;
		case 'ArrowUp':
			if (!$dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			} else {
				var $prev = ($focused.length > 0 ? $focused.prevAll('li:not(.disabled)').first() : $dropdown.find('li:not(.disabled)').last());
				if ($prev.length > 0) {
					$dropdown.find('.focus').removeClass('focus');
					$prev.addClass('focus').focus();
				}
			}
			return false;
		case 'Escape':
			if ($dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			}
			break;
		case 'Tab':
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
			// Close when clicking outside
			$(document).on('click.nice_select', __document_click);
		} else {
			$dropdown.focus();

			// Unbind existing events in case that the plugin has been initialized before
			$(document).off('.nice_select');
		}
	}

	function __dropdown_option_click() {
		var $li = $(this);

		if ($li.hasClass('selected')) {
			return;
		}

		var $dropdown = $li.closest('.ui-nice-select'),
			$select = $dropdown.prev('select'),
			val = $li.attr('value');

		if ($dropdown.hasClass('multiple')) {
			var vs = $select.val() || [];
			vs.push(val);
			val = vs;
		}
		$select.val(val).trigger('change');
	}

	// multiple only
	function __dropdown_current_click() {
		var $t = $(this), val = $t.attr('value'),
			$dropdown = $t.closest('.ui-nice-select'),
			$select = $dropdown.prev('select');

		$t.remove();
		
		$dropdown.find('li').filter(function() { return $(this).attr('value') == val; }).removeClass('selected');
		var val = [];
		$dropdown.find('.current').each(function() {
			val.push($(this).attr('value'));
		})
		$select.val(val);
		return false;
	}

	function __select_change() {
		change.apply($(this));
	}

	function change() {
		this.each(function() {
			var $select = $(this), vs = $select.val();
			var $dropdown = $select.next('.ui-nice-select');

			var eq = $.isArray(vs)
				? function(v, a) {
					for (var i = 0; i < a.length; i++) {
						if (v == a[i]) {
							return true;
						}
					}
					return false;
				}
				: function(v, a) {
					return v == a;
				};

			$dropdown.find('.current').remove();
			$dropdown.find('.selected').removeClass('selected');
			$dropdown.find('li')
				.filter(function() { return eq($(this).attr('value'), vs); })
				.addClass('selected')
				.each(function() {
					var $t = $(this);
					$dropdown.append($('<span>', { 'class': 'current', 'value': $t.attr('value')}).text($t.attr('display') || $t.text()));
				});
		});
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
				$select.css('display', '').off('.nice_select');
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
		var $options = $select.find('option'),
			$selected = $select.find('option:selected'),
			$ul = $('<ul></ul>'),
			$dropdown = $('<div></div>')
				.addClass('ui-nice-select')
				.addClass($select.attr('class') || '')
				.addClass($select.prop('disabled') ? 'disabled' : '')
				.addClass($select.prop('multiple') ? 'multiple' : '')
				.attr('tabindex', $select.prop('disabled') ? null : ($select.attr('tabindex') || '0'))
				.append($ul);

		$selected.each(function() {
			var $op = $(this);
			$dropdown.append($('<span>', { 'class': 'current', 'value': $op.val()}).text($op.attr('display') || $op.text()));
		});

		$options.each(function() {
			var $option = $(this);

			$ul.append($('<li></li>')
				.attr('value', $option.val())
				.attr('display', ($option.attr('display') || ''))
				.addClass(
					($option.is(':selected') ? ' selected' : '') +
					($option.is(':disabled') ? ' disabled' : ''))
				.attr('tabindex', $option.is(':disabled') ? null : '0')
				.text($option.text())
			);
		});

		// Open, close
		$dropdown.click(__dropdown_click);

		// Keyboard events
		$dropdown.keydown(__dropdown_keydown);

		// Option click
		$dropdown.on('click', 'li:not(.disabled)', __dropdown_option_click);

		// multiple
		if ($dropdown.hasClass('multiple')) {
			$dropdown.on('click', '.current', __dropdown_current_click)
		}

		$select.after($dropdown);

		$select.on('change.nice_select', __select_change);
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
			var $s = $(this);
			if ($s.next().hasClass('ui-nice-select')) {
				update.apply($s);
			} else {
				create_nice_select($s);
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

})(jQuery);
