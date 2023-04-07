(function($) {
	var ArrowClasses = {
		'top left': 'dn hr1 vb',
		'top right': 'dn hl1 vb',
		'top center': 'dn hc vb',
		'bottom left': 'up hr1 vt',
		'bottom right': 'up hl1 vt',
		'bottom center': 'up hc vt',
		'left bottom': 'rt hr vt1',
		'left top': 'rt hr vb1',
		'left middle': 'rt hr vm',
		'right bottom': 'lt hl vt1',
		'right top': 'lt hl vb1',
		'right middle': 'lt hl vm'
	};

	function _position($p, $t, position) {
		var tw = $t.outerWidth(), th = $t.outerHeight(), p = $t.offset();
		var pw = $p.outerWidth(), ph = $p.outerHeight();

		switch (position) {
		case 'top left':
			p.top -= (ph + 11);
			p.left -= (pw - 50);
			break;
		case 'top right':
			p.top -= (ph + 11);
			p.left += (tw - 50);
			break;
		case 'top center':
			p.top -= (ph + 11);
			p.left += (tw - pw) / 2;
			break;
		case 'bottom left':
			p.top += th + 11;
			p.left -= (pw - 50);
			break;
		case 'bottom right':
			p.top += th + 11;
			p.left += (tw - 50);
			break;
		case 'bottom center':
			p.top += th + 11;
			p.left += (tw - pw) / 2;
			break;
		case 'left bottom':
			p.left -= (pw + 11);
			p.top -= 20;
			break;
		case 'left top':
			p.left -= (pw + 11);
			p.top += th - ph + 20;
			break;
		case 'left middle':
			p.left -= (pw + 11);
			p.top -= (ph - th) / 2;
			break;
		case 'right bottom':
			p.left += tw + 11;
			p.top -= 20;
			break;
		case 'right top':
			p.left += tw + 11;
			p.top += th - ph + 20;
			break;
		case 'right middle':
			p.left += tw + 11;
			p.top -= (ph - th) / 2;
			break;
		}

		return p;
	}

	function _in_screen($p, p) {
		var $w = $(window),
			wt = $w.scrollTop(), wl = $w.scrollLeft(),
			wb = wt + $w.height(), wr = wl + $w.width(),
			pr = p.left + $p.outerWidth(), pb = p.top + $p.outerHeight();

		return p.left >= wl && p.left <= wr
			&& p.top >= wt && p.top <= wb
			&& pr >= wl && pr <= wr
			&& pb >= wt && pb <= wb;
	}

	function _positions($p, $t, ps) {
		for (var i = 0; i < ps.length; i++) {
			var p = _position($p, $t, ps[i]);
			p.position = ps[i];
			if (_in_screen($p, p)) {
				return p;
			}
			ps[i] = p;
		}
		return ps[0];
	}

	function _center($p, $w) {
		var p = {
			left: $w.scrollLeft() + ($w.outerWidth() - $p.outerWidth()) / 2,
			top: $w.scrollTop() + ($w.outerHeight() - $p.outerHeight()) / 2
		};

		p.left = (p.left < 10 ? 10 : p.left);
		p.top = (p.top < 10 ? 10 : p.top);
		return p;
	}

	function _align($p, trigger, position) {
		$p.css({
			display: 'block',
			visibility: 'hidden'
		});

		var p, ac, $a = $p.find('.ui-popup-arrow').hide();
		if (position == 'center') {
			p = _center($p, $(window));
		} else {
			var $t = $(trigger);

			ac = ArrowClasses[position];
			if (ac) {
				p = _position($p, $t, position);
			} else {
				switch (position) {
				case 'top':
					p = _positions($p, $t, ['top center', 'top left', 'top right']);
					break;
				case 'bottom':
					p = _positions($p, $t, ['bottom center', 'bottom left', 'bottom right']);
					break;
				case 'left':
					p = _positions($p, $t, ['left middle', 'left bottom', 'left top']);
					break;
				case 'right':
					p = _positions($p, $t, ['right middle', 'right bottom', 'right top']);
					break;
				//case 'auto':
				default:
					p = _positions($p, $t, [
						'bottom center', 'bottom left', 'bottom right',
						'right middle', 'right bottom', 'right top',
						'top center', 'top left', 'top right',
						'right middle', 'right bottom', 'right top'
					]);
					break;
				}
				ac = ArrowClasses[p.position];
			}
		}

		$p.css({
			top: p.top,
			left: p.left,
			visibility: 'visible'
		});
		if (ac) {
			$a.attr('class', 'ui-popup-arrow ' + ac).show();
		}
	}

	function _masker() {
		return $('.ui-popup-mask');
	}
	function _active() {
		return $('.ui-popup-wrap:visible>.ui-popup-frame>.ui-popup');
	}
	function _wrapper($c) {
		return $c.parent().parent('.ui-popup-wrap');
	}
	function _data($c) {
		return $c.data('popup');
	}

	function toggle($c, trigger) {
		trigger = trigger || window;
		var $p = _wrapper($c);
		if ($p.is(':hidden')) {
			show($c, trigger);
			return;
		}

		if (_data($c).trigger === trigger) {
			hide($c);
			return;
		}

		show($c, trigger);
	}

	function hide($c) {
		var $p = _wrapper($c);
		if ($p.is(':visible')) {
			$c.trigger('hide.popup');
			$p.hide();
			$(document).off('.popup');
			$(window).off('.popup');
			$c.trigger('hidden.popup');
		}
		_masker().hide();
	}

	function show($c, trigger) {
		hide(_active());

		var $p = _wrapper($c), c = _data($c);

		if (c.mask) {
			_masker().show();
		}

		if (c.loaded || !c.ajax.url) {
			_show($p, $c, c, trigger);
			return;
		}

		c.showing = trigger || window;
		load($c, c);
	}

	function _bind(c) {
		$(document).off('.popup');
		if (c.mouse) {
			$(document).on('click.popup', __doc_click);
		}
		if (c.keyboard) {
			$(document).on('keydown.popup', __doc_keydown);
		}
		if (c.resize) {
			$(window).on('resize.popup', __doc_resize);
		}
	}

	function _show($p, $c, c, trigger) {
		$c.trigger('show.popup');

		$p.find('.ui-popup-closer')[c.closer ? 'show' : 'hide']();

		c.trigger = trigger || window;

		_align($p, c.trigger, c.position);

		$p.children('.ui-popup-frame').hide()[c.transition](function() {
			$c.trigger('shown.popup');
			_bind(c);
		}).focus();
	}

	function __doc_click(evt) {
		if ($(evt.target).closest('.ui-popup-wrap').length) {
			return;
		}
		hide(_active());
	}

	function __doc_keydown(evt) {
		if (evt.keyCode == 27) { // Esc
			hide(_active());
		}
	}

	function __doc_resize() {
		var $c = _active(), $p = _wrapper($c), c = _data($c);
		_align($p, c.trigger, c.position);
	}

	function load($c, c) {
		var $p = _wrapper($c);

		c = $.extend(_data($c), c);

		if (c.loader) {
			$c.html('<div class="ui-popup-loader"></div>');
			_align($p, c.showing, c.position);
		}

		_load($p, $c, c);
	}

	function _load($p, $c, c) {
		var seq = ++c.sequence;

		$p.addClass('loading').find('.ui-popup-closer, .ui-popup-arrow').hide();

		$c.trigger('load.popup');

		$.ajax($.extend({}, c.ajax, {
			success: function(data, status, xhr) {
				if (seq == c.sequence) {
					c.ajaxDone.call($c, data, status, xhr);
					$c.find('[popup-dismiss="true"]').click(function() {
						hide($c);
						return false;
					});
					c.loaded = true;
					$c.trigger('loaded.popup', data);
				}
			},
			error: function(xhr, status, err) {
				if (seq == c.sequence) {
					c.ajaxFail.call($c, xhr, status, err);
					$c.trigger('failed.popup');
				}
			},
			complete: function() {
				$p.removeClass('loading');
				if (seq == c.sequence && c.showing) {
					_show($p, $c, c, c.showing);
					delete c.showing;
				}
			}
		}));
	}

	function _ajaxFail(xhr, status, err) {
		var $c = $(this), $e = $('<div class="ui-popup-error">');

		if (xhr.responseJSON) {
			$e.addClass('json').text(JSON.stringify(xhr.responseJSON, null, 4));
		} else if (xhr.responseText) {
			$e.html(xhr.responseText);
		} else {
			$e.text(err || status || 'Server error!');
		}

		$c.empty().append($e);
	}

	function _ajaxDone(data, status, xhr) {
		$(this).html(xhr.responseText);
	}

	function update($c, c) {
		if (c) {
			c = $.extend(_data($c), c);
			var $p = _wrapper($c);
			if (!$p.is(':hidden')) {
				_bind(c);
				_masker()[c.mask ? 'show' : 'hide']();
			}
		}
	}

	function trigger($c, evt) {
		var a = [].slice.call(arguments, 2);
		$(_data($c).trigger).trigger(evt, a);
	}

	function destroy($c) {
		_wrapper($c).remove();
	}

	function _camelCase(s) {
		s = s.charAt(0).toLowerCase() + s.slice(1);
		return s.replace(/[-_](.)/g, function(m, g) {
			return g.toUpperCase();
		});
	}

	function _options($c) {
		var fs = ['ajax-done', 'ajax-fail'];
		var bs = ['loaded', 'autoload', 'mask', 'loader', 'closer', 'mouse', 'keyboard', 'resize'];

		var c = {};
		$.each($c[0].attributes, function(i, a) {
			var p = a.name.substring(0, 6),
				n = a.name.substring(6),
				v = a.value;

			if ('popup-' != p || !v) {
				return;
			}

			if ($.inArray(n, fs) >= 0) {
				c[_camelCase(n)] = new Function(v);
				return;
			}

			if ($.inArray(n, bs) >= 0) {
				v = (v === 'true');
			}

			if ('ajax-' == n.substring(0, 5)) {
				c.ajax ||= {};
				c.ajax[_camelCase(n.substring(5))] = v;
			} else {
				c[_camelCase(n)] = v;
			}
		});
		return c;
	}

	function _init($c, c) {
		if (_masker().length == 0) {
			$('<div class="ui-popup-mask">').appendTo('body');
		}

		var $p = _wrapper($c);
		if ($p.length) {
			update($c, c);
			return;
		}

		c = $.extend({ sequence: 0 }, $.popup.defaults, _options($c), c);

		var $f = $('<div class="ui-popup-frame" tabindex="0">')
			.append($('<div class="ui-popup-arrow">'))
			.append($('<i class="ui-popup-closer">&times;</i>').click(function() {
				hide($c);
			}));

		$p = $('<div class="ui-popup-wrap">').append($f).appendTo('body');

		if (c.cssClass) {
			$p.addClass(c.cssClass);
		}

		$c.appendTo($f).data('popup', c).addClass('ui-popup').show();

		if (c.ajax.url) {
			c.loaded = false;
			if (c.autoload) {
				_load($p, $c, c);
			}
		} else {
			c.loaded = true;
			$c.find('[popup-dismiss="true"]').click(function() {
				hide($c);
				return false;
			});
		}
	}

	var api = {
		load: load,
		show: show,
		hide: hide,
		toggle: toggle,
		update: update,
		trigger: trigger,
		destroy: destroy
	};

	$.fn.popup = function(c) {
		var args = [].slice.call(arguments);
		return this.each(function() {
			var $c = $(this);

			if (typeof (c) == 'string') {
				var p = _data($c);
				if (!p) {
					_init($c);
				}
				args[0] = $c;
				api[c].apply($c, args);
				return;
			}

			_init($c, c);
		});
	};

	$.popup = function() {
		var $c = _active();
		$c.popup.apply($c, arguments);
		return $c;
	};

	$.popup.defaults = {
		position: 'auto',
		transition: 'slideDown',
		mask: false,
		loader: false,
		closer: false,
		mouse: true,
		keyboard: true,
		resize: true,
		ajax: {},
		ajaxDone: _ajaxDone,
		ajaxFail: _ajaxFail
	};

	// POPUP DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="popup"]').popup();
		$('body').on('click.popup', '[popup-target]', function(evt) {
			evt.stopPropagation();
			var $t = $(this), c = _options($t);
			$($t.attr('popup-target')).popup(c).popup('toggle', this);
		});
	});

})(jQuery);
