(function() {
	"use strict";

	if (typeof String.prototype.hashCode != "function") {
		String.prototype.hashCode = function() {
			var s = this, h = 0;
			for (var i = 0; i < s.length; i++) {
				// h = 31 * h + s.charCodeAt(i);
				h = ((h << 5) - h) + s.charCodeAt(i); // faster
				h |= 0; // Convert to 32bit integer
			}
			return h;
		};
	}
	if (typeof String.prototype.isEmpty != "function") {
		String.prototype.isEmpty = function(s) {
			return this.length < 1;
		};
	}
	if (typeof String.prototype.contains != "function") {
		String.prototype.contains = function(s) {
			return this.indexOf(s) >= 0;
		};
	}
	if (typeof String.prototype.startsWith != "function") {
		String.prototype.startsWith = function(prefix, toffset) {
			return this.indexOf(prefix) == (toffset || 0);
		};
	}
	if (typeof String.prototype.endsWith != "function") {
		String.prototype.endsWith = function(suffix) {
			return this.startsWith(suffix, this.length - suffix.length);
		};
	}

	if (typeof String.prototype.trimLeft != "function") {
		var re = /^\s+/;
		String.prototype.trimLeft = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.trimRight != "function") {
		var re = /\s+$/;
		String.prototype.trimRight = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.trim != "function") {
		var re = /^\s+|\s+$/g;
		String.prototype.trim = function() {
			return this.replace(re, "");
		};
	}

	if (typeof String.prototype.stripLeft != "function") {
		var re = /^[\s\u0085\u00a0\u2000\u3000]+/;
		String.prototype.stripLeft = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.stripRight != "function") {
		var re = /[\s\u0085\u00a0\u2000\u3000]+$/;
		String.prototype.stripRight = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.strip != "function") {
		var re = /^[\s\u0085\u00a0\u2000\u3000]+|[\s\u0085\u00a0\u2000\u3000]+$/g;
		String.prototype.strip = function() {
			return this.replace(re, "");
		};
	}

	if (typeof String.prototype.fields != "function") {
		var ws = /[\s\u0085\u00a0\u2000\u3000]/g;
		String.prototype.fields = function(re) {
			re ||= ws;

			var ss = this.split(re), rs = [];
			for (var i = 0; i < ss.length; i++) {
				if (ss[i].length) {
					rs.push(ss[i])
				}
			}
			return rs;
		};
	}

	if (typeof String.prototype.left != "function") {
		String.prototype.left = function(n) {
			return this.slice(0, n);
		};
	}
	if (typeof String.prototype.mid != "function") {
		String.prototype.mid = String.prototype.substr;
	}
	if (typeof String.prototype.right != "function") {
		String.prototype.right = function(n) {
			var s = this;
			return s.length <= n ? s : s.slice(s.length - n);
		};
	}

	if (typeof String.prototype.padLeft != "function") {
		String.prototype.padLeft = function(n, c) {
			c = c || ' ';
			var s = this;
			if (s.length >= n) {
				return s;
			}
			while (s.length < n) {
				s = c + s;
			}
			return s.right(n);
		};
	}
	if (typeof String.prototype.padRight != "function") {
		String.prototype.padRight = function(n, c) {
			c = c || ' ';
			var s = this;
			if (s.length >= n) {
				return s;
			}
			while (s.length < n) {
				s += c;
			}
			return s.left(n);
		};
	}
	if (typeof String.prototype.padCenter != "function") {
		String.prototype.padCenter = function(n, c) {
			c = c || ' ';
			var s = this, z = s.length, p = n - z;
			if (p <= 0) {
				return s;
			}
			s = s.padLeft(z+p/2, c);
			s = s.padRight(n, c);
			return s;
		};
	}

	if (typeof String.prototype.capitalize != "function") {
		String.prototype.capitalize = function() {
			return this.charAt(0).toUpperCase() + this.slice(1);
		};
	}
	if (typeof String.prototype.uncapitalize != "function") {
		String.prototype.uncapitalize = function() {
			return this.charAt(0).toLowerCase() + this.slice(1);
		};
	}


	if (typeof String.prototype.snakeCase != "function") {
		String.prototype.snakeCase = function(d) {
			d ||= '_';

			var s = this, uc = 0, lc = '', n = '';
			for (var i = 0; i < s.length; i++) {
				var x = s.charCodeAt(i), c = s.charAt(i);
				if (x >= 0x41 && x <= 0x5A) {
					if (i > 0 && uc == 0 && lc != d) {
						n += d
					}

					uc++;
					lc = c.toLowerCase()
					n += lc;
					continue
				}

				if (uc > 1 && d != c) {
					n += d;
				}
				n += c;
				uc = 0;
				lc = c;
			}
			return n;
		};
	}
	if (typeof String.prototype.camelCase != "function") {
		String.prototype.camelCase = function() {
			var s = this.charAt(0).toLowerCase() + this.slice(1);
			return s.replace(/[-_](.)/g, function(m, g) {
				return g.toUpperCase();
			});
		};
	}
	if (typeof String.prototype.pascalCase != "function") {
		String.prototype.pascalCase = function(c) {
			return this.camelCase().capitalize();
		};
	}

	if (typeof String.prototype.format != "function") {
		String.prototype.format = function() {
			var args = arguments;
			return this.replace(/\{(\d+)\}/g, function(m, i) {
				return args[i];
			});
		};
	}
	if (typeof String.prototype.substrAfter != 'function') {
		String.prototype.substrAfter = function(c) {
			var s = this, i = s.indexOf(c);
			return (i >= 0) ? s.slice(i + 1) : "";
		};
	}
	if (typeof String.prototype.substrBefore != 'function') {
		String.prototype.substrBefore = function(c) {
			var s = this, i = s.indexOf(c);
			return (i >= 0) ? s.slice(0, i) : s;
		};
	}
	if (typeof String.prototype.ellipsis != 'function') {
		String.prototype.ellipsis = function(n) {
			var s = this;
			return s.length > n ? s.slice(0, n - 3) + "..." : s;
		};
	}

	/**
	 * Truncate a string and add an ellipsiz ('...') to the end if it exceeds the specified length
	 * the length of charCodeAt(i) > 0xFF will be treated as 2. 
	 * @param {Number} n The maximum length to allow before truncating
	 * @return {String} The converted text
	 */
	if (typeof String.prototype.ellipsiz != 'function') {
		String.prototype.ellipsiz = function(n) {
			var s = this, z = 0;
			for (var i = 0; i < s.length; i++) {
				z++;
				if (s.charCodeAt(i) > 0xFF) {
					z++;
				}
				if (z > n) {
					return s.slice(0, i) + '...';
				}
			}
			return s;
		}
	}

	if (typeof String.prototype.escapeRegExp != "function") {
		String.prototype.escapeRegExp = function() {
			return this.replace(/([.*+?^=!:${}()|[\]\/\\])/g, '\\$1');
		};
	}
	if (typeof String.prototype.escapeHTML != "function") {
		var ehm = {
			'&': '&amp;',
			"'": '&apos;',
			'`': '&#x60;',
			'"': '&quot;',
			'<': '&lt;',
			'>': '&gt;'
		};

		String.prototype.escapeHTML = function() {
			return this.replace(/[&'`"<>]/g, function(c) {
				return ehm[c];
			});
		};
	}
	if (typeof String.prototype.unescapeHTML != "function") {
		// a simple version, complete version: https://stackoverflow.com/questions/994331/how-to-unescape-html-character-entities-in-java
		var uhm = {
			'&lt;': '<',
			'&gt;': '>',
			'&amp;': '&',
			'&quot;': '"',
			'&apos;': "'",
			'&#x27;': "'",
			'&#x60;': '`'
		};

		String.prototype.unescapeHTML = function() {
			return this.replace(/&(lt|gt|amp|quot|apos|#x27|#x60);/g, function(t) {
				return uhm[t];
			});
		};
	}

	if (typeof String.prototype.encodeUTF8 != "function") {
		String.prototype.encodeUTF8 = function() {
			var s = this, utf8 = "";
			for (var n = 0; n < s.length; n++) {
				var c = s.charCodeAt(n);

				if (c < 128) {
					utf8 += String.fromCharCode(c);
				} else if ((c > 127) && (c < 2048)) {
					utf8 += String.fromCharCode((c >> 6) | 192);
					utf8 += String.fromCharCode((c & 63) | 128);
				} else {
					utf8 += String.fromCharCode((c >> 12) | 224);
					utf8 += String.fromCharCode(((c >> 6) & 63) | 128);
					utf8 += String.fromCharCode((c & 63) | 128);
				}
			}

			return utf8;
		};
	}
	if (typeof String.prototype.decodeUTF8 != "function") {
		String.prototype.decodeUTF8 = function() {
			var s = this, o = "", i = 0, c = 0, c2 = 0, c3 = 0;
			while (i < s.length) {
				c = s.charCodeAt(i);

				if (c < 128) {
					o += String.fromCharCode(c);
					i++;
				} else if (c > 191 && c < 224) {
					c2 = s.charCodeAt(i + 1);
					o += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
					i += 2;
				} else {
					c2 = s.charCodeAt(i + 1);
					c3 = s.charCodeAt(i + 2);
					o += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
					i += 3;
				}
			}
			return o;
		};
	}
	if (typeof String.prototype.encodeBase64 != "function") {
		String.prototype.encodeBase64 = function() {
			return btoa(this.encodeUTF8());
		};
	}
	if (typeof String.prototype.decodeBase64 != "function") {
		String.prototype.decodeBase64 = function() {
			return atob(this).decodeUTF8();
		};
	}
})();
