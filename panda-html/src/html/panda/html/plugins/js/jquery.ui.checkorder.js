(function($) {
	"use strict";

	var E = 'click.checkorder',
		DS = 'dragstart.checkorder',
		DE = 'dragend.checkorder',
		DO = 'dragover.checkorder',
		DL = 'dragleave.checkorder',
		DP = 'drop.checkorder';

	var _d;
	function _dragstart() {
		_d = this;
	}
	function _dragend() {
		_d = null;
	}
	function _dragover(e) {
		e.preventDefault();
		$(this).addClass('dragover');
	}
	function _dragleave() {
		$(this).removeClass('dragover');
	}

	function _drop() {
		var $l = $(this);
		if (_d && _d !== this) {
			var $d = $(_d), $p = $l.parent(), $ls = $p.children('label');
			if ($ls.filter(function() { return this === _d; }).length) {
				var c = $l.find(':checkbox').prop('checked');

				var vs = [];
				$ls.each(function() {
					if (this !== _d) {
						if (this === $l[0]) {
							vs.push({ label: _d, drop: true, check: c });
						}
						vs.push({ label: this });
					}
				});

				$p.data('checkorder', '').trigger('dropstart.checkorder', vs);
				if ($p.data('checkorder') != 'cancel') {
					$d.find(':checkbox').prop('checked', c);
					$d.insertBefore($l);
					$p.trigger('dropend.checkorder');
				}
			}
		}
		$l.removeClass('dragover');
	}

	function _click() {
		var $c = $(this), $l = $c.closest('label'), $p = $l.parent();

		$p.data('checkorder', '').trigger('checkclick.checkorder', [ $c[0] ]);
		if ($p.data('checkorder') != 'cancel') {
			$l.fadeOut(200, function() {
				var $h = $c.closest('.ui-checks').find('hr');
				if ($c.prop('checked')) {
					$l.insertBefore($h);
				} else {
					$l.insertAfter($h);
				}
				$l.fadeIn(200);
			});
		}
	}

	$.fn.checkorder = function() {
		var $t = $(this), $h = $t.find('hr');
		if ($h.length == 0) {
			$t.prepend($('<hr>'));
		}
		$t.off('.checkorder')
			.on(E, ":checkbox", _click)
			.on(DS, "label", _dragstart)
			.on(DE, "label", _dragend)
			.on(DO, "label", _dragover)
			.on(DL, "label", _dragleave)
			.on(DP, "label", _drop);
		$t.children('label').prop('draggable', true);
		return this;
	}

	// ==================
	$(window).on('load', function() {
		$('.ui-checks.ordered').checkorder();
	});
})(jQuery);
