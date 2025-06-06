(function($) {
	"use strict";

	function linkify(node, c) {
		switch (node.nodeType) {
		case 3: // Text Node
			c.regexp.lastIndex = 0;
			var r = c.regexp.exec(node.nodeValue);
			if (r) {
				var $a = $('<a>', { target: c.target, href: r[0] }).text(r[0]);
				if (c.prepend) {
					$a.prepend(c.prepend);
				}
				if (c.append) {
					$a.append(c.append);
				}

				var m = node.splitText(r.index);
				m.splitText(r[0].length);
				$(m).replaceWith($a);
				return 1;
			}
			break;
		case 1: // Element Node
			if (node.childNodes && !c.ignore.test(node.tagName)) {
				for (var i = 0; i < node.childNodes.length; i++) {
					i += linkify(node.childNodes[i], c);
				}
			}
			break;
		}
		return 0;
	}

	$.linkify = {
		defaults: {
			ignore: /(script|style|a)/i,
			// URLs starting with http://, https://
			regexp: /https?:\/\/[\w~!@#\$%&\*\(\)_\-\+=\[\]\|:;,\.\?\/']+/i,
			target: '_blank',
			prepend: '',
			append: ''
		}
	};

	$.fn.linkify = function(c) {
		c = $.extend({}, $.linkify.defaults, c);

		return this.each(function() {
			linkify(this, c);
			$(this).removeAttr('linkify');
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[linkify]').linkify();
	});
})(jQuery);
