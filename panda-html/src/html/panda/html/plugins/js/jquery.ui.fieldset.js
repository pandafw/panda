(function($) {
	"use strict";

	function _collapse($f, t) {
		t = t || $f.data('fieldset').hideTransition;
		$f.addClass('collapsing').trigger('collapse.fieldset').children(':not(legend)')[t](function() {
			$f.removeClass('collapsing').addClass('collapsed').trigger('collapsed.fieldset');
		});
	}

	function _expand($f, t) {
		t = t || $f.data('fieldset').showTransition;
		$f.addClass('expanding').trigger('expand.fieldset').children(':not(legend)')[t](function() {
			$f.removeClass('expanding collapsed').trigger('expanded.fieldset');
		});
	}

	function collapse($f, t) {
		if (!$f.hasClass('collapsed')) {
			_collapse($f, t);
		}
	}

	function expand($f, t) {
		if ($f.hasClass('collapsed')) {
			_expand($f, t);
		}
	}

	function toggle($f) {
		$f.hasClass('collapsed') ? _expand($f) : _collapse($f);
	}

	function _click() {
		toggle($(this).closest('fieldset'));
	}

	function _init($f, c) {
		if (!$f.data('fieldset')) {
			c = $.extend({}, $.fieldset.defaults, c);

			var h = c.collapsed || $f.hasClass('collapsed'), e = 'click.fieldset';
	
			$f.data('fieldset', c).addClass('ui-fieldset collapsible' + (h ? ' collapsed' : ''));
			$f.children('legend').off(e).on(e, _click);
			$f.children(':not(legend)').toggle(!h);
		}
	}

	var api = {
		collapse: collapse,
		expand: expand,
		toggle: toggle
	};

	$.fieldset = {
		defaults: {
			showTransition: 'slideDown',
			hideTransition: 'slideUp'
		}
	};

	$.fn.fieldset = function(c) {
		var args = [].slice.call(arguments);

		return this.each(function() {
			var $f = $(this);
			if (typeof(c) == 'string') {
				_init($f);
				args[0] = $f;
				api[c].apply($f, args);
				return;
			}
			_init($f, c);
		});
	};

	// FIELDSET DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="fieldset"]').fieldset();
	});
})(jQuery);
