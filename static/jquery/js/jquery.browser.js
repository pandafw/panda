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
	// Use of jQuery.browser is frowned upon.
	// More details: http://api.jquery.com/jQuery.browser
	// jQuery.uaMatch maintained for back-compat
	$.uaMatch = function( ua ) {
		ua = ua.toLowerCase();

		var m = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
			/(webkit)[ \/]([\w.]+)/.exec( ua ) ||
			/(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
			/(msie) ([\w.]+)/.exec( ua ) ||
			ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
			[];

		return {
			b: m[ 1 ] || "",
			v: m[ 2 ] || "0"
		};
	};

	var m = $.uaMatch( navigator.userAgent );
	var b = { version: 0 };

	if (m.b) {
		b[m.b] = true;
		b.version = m.v;
	}

	// Chrome is Webkit, but Webkit is also Safari.
	if (b.chrome) {
		b.webkit = true;
	}
	else if (b.webkit) {
		b.safari = true;
	}

	$.browser = $.extend(b, {
		width: function() {
			return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
		},
		height: function() {
			return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
		},
		ipad: /ipad/i.test(navigator.userAgent),
		ipod: /ipod/i.test(navigator.userAgent),
		iphone: /iphone/i.test(navigator.userAgent),
		ios: /iphone|ipad|ipod/i.test(navigator.userAgent),
		android: /android/i.test(navigator.userAgent),
		majorVersion: parseInt(b.version, 10)
	});
})(jQuery);
