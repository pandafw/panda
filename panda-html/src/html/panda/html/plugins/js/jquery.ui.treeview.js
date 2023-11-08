(function($) {
	"use strict";

	function init($t) {
		$t.find('li').removeClass('node leaf').children('.item').off('.treeview').each(function() {
			var $i = $(this), $n = $i.parent();
			if ($i.next('ul').length) {
				$n.addClass('node');
				$i.on('click.treeview', _on_item_click);
			} else {
				$n.addClass('leaf');
			}
		});
	}

	function _on_item_click() {
		_toggle($(this).parent());
	}

	function _collapse($n) {
		$n.addClass('collapsed').children('.item').next().slideUp();
	}

	function _expand($n) {
		$n.removeClass('collapsed').children('.item').next().slideDown();
	}

	function _toggle($n) {
		$n.hasClass('collapsed') ? _expand($n) : _collapse($n);
	}

	function collapse($t, $n) {
		_collapse($n || $t.find('li:not(.collapsed .leaf)'));
	}

	function expand($t, $n) {
		_expand($n || $t.find('li.collapsed'));
	}

	function toggle($t, $n) {
		_toggle($n || $t.find('li:not(.leaf)'));
	}

	function unbind($t) {
		$t.find('li').removeClass('node').children('.item').off('.treeview');
	}

	var api = {
		'collapse': collapse,
		'expand': expand,
		'toggle': toggle,
		'destroy': unbind
	};

	$.fn.treeview = function(method, target) {
		// Methods
		if (typeof method == 'string') {
			api[method].call(this, target);
			return this;
		}

		init(this);
		return this;
	};

	// TREEVIEW DATA-API
	// ==================
	$(window).on('load', function() {
		$('ul[data-spy="treeview"]').treeview();
	});

})(jQuery);
