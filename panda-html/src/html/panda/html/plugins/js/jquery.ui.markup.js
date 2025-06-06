(function($) {
	"use strict";

	var ws = /[\s\u0085\u00a0\u2000\u3000]/g;

	function split(s) {
		var ss = s.split(ws), rs = [];
		for (var i = 0; i < ss.length; i++) {
			if (ss[i].length) {
				rs.push(ss[i])
			}
		}
		return rs;
	}

	function index_any(s, c) {
		var i = 0;
		while (s.length > 0) {
			for (var j = 0; j < c.markups.length; j++) {
				var m = c.markups[j], l = m.length, t = s.substring(0, l);
				if (t == m || (c.caseInsensitive && t.toLowerCase() == m)) {
					return [i, l]
				}
			}
			s = s.substring(1);
			i++;
		}
		return false;
	}

	function markup(node, c) {
		switch (node.nodeType) {
		case 3: // Text Node
			var r = index_any(node.nodeValue, c);
			if (r) {
				var m = node.splitText(r[0]);
				m.splitText(r[1]);
				$(m).wrap(c.wrap);
				return 1;
			}
			break;
		case 1: // Element Node
			if (node.childNodes && !c.ignore.test(node.tagName)) {
				for (var i = 0; i < node.childNodes.length; i++) {
					i += markup(node.childNodes[i], c);
				}
			}
			break;
		}
		return 0;
	}

	$.markup = {
		defaults: {
			caseInsensitive: true,
			ignore: /(script|style|mark)/i,
			wrap: '<mark></mark>',
		}
	};

	$.fn.markup = function(o) {
		if ($.isArray(o)) {
			o = { markups: o };
		} else if (typeof(o) == 'string') {
			o = { markup: o };
		}

		return this.each(function() {
			var $t = $(this), c = $.extend({}, $.markup.defaults, o);

			c.markups ||= split(c.markup || $t.attr('markup') || '');
			if (c.markups.length) {
				if (c.caseInsensitive) {
					for (var i = 0; i < c.markups.length; i++) {
						c.markups[i] = c.markups[i].toLowerCase();
					}
				}
				markup(this, c);
			}
			$t.removeAttr('markup');
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[markup]').markup();
	});
})(jQuery);
