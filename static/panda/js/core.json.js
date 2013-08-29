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
(function() {
	var m = {
		'\b': '\\b',
		'\t': '\\t',
		'\n': '\\n',
		'\f': '\\f',
		'\r': '\\r',
		'"' : '\\"',
		'\\': '\\\\'
	};
	if (typeof JSON == 'undefined') {
		var s = {
			'array': function (x, r, w) {
				var a = [], f, i, l = x.length, v;
				for (i = 0; i < l; i++) {
					v = x[i];
					f = s[typeof v];
					if (f) {
						a.push(f(v, r, w));
					}
				}
				return '[' + w + a.join(',' + w) + w + ']';
			},
			'boolean': function (x) {
				return String(x);
			},
			'null': function (x) {
				return "null";
			},
			'number': function (x) {
				return isFinite(x) ? String(x) : 'null';
			},
			'object': function (x, r, w) {
				if (x) {
					if (x instanceof Array) {
						return s.array(x, r, w);
					}
					var a = [], f, i, v;
					for (i in x) {
						v = x[i];
						f = s[typeof v];
						if (f) {
							v = f(v, r, w);
							a.push(s.string(i) + ':' + v);
						}
					}
					return '{' + w + a.join(w) + w + '}';
				}
				return 'null';
			},
			'string': function (x) {
				if (/["\\\x00-\x1f]/.test(x)) {
					x = x.replace(/([\x00-\x1f\\"])/g, function(a, b) {
						var c = m[b];
						if (c) {
							return c;
						}
						c = b.charCodeAt();
						return '\\u00' +
							Math.floor(c / 16).toString(16) +
							(c % 16).toString(16);
					});
				}
				return '"' + x + '"';
			}
		};
	
		JSON = {
			stringify: function(v, r, w) {
				w = w || '';
				var f = isNaN(v) ? s[typeof v] : s['number'];
				if (f) return f(v, r, w);
			},
		
			parse: function(v, safe) {
				if (safe === undefined) safe = JSON.parse.safe;
				if (safe && !/^("(\\.|[^"\\\n\r])*?"|[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t])+?$/.test(v)) {
					return undefined;
				}
				return eval('('+v+')');
			}
		};		

		JSON.parse.safe = false;
	}
	
	var ps = function(d, w) {
		var s = '';
		for (var i = 0; i < d; i++) {
			s += w;
		}
		return s;
	}
	var pp = {
		'array': function (x, d, w) {
			var a = [], f, i, l = x.length, v;
			if (l < 1) return '[]';
			for (i = 0; i < l; i++) {
				v = x[i];
				f = pp[typeof v];
				if (f) {
					v = f(v, d + 1, w);
					a.push(ps(d + 1, w) + v);
				}
			}
			return '[\n' + a.join(',\n') + '\n' + ps(d, w) + ']';
		},
		'boolean': function (x) {
			return String(x);
		},
		'null': function (x) {
			return "null";
		},
		'number': function (x) {
			return isFinite(x) ? String(x) : 'null';
		},
		'object': function (x, d, w) {
			if (x) {
				if (x instanceof Array) {
					return pp.array(x, d, w);
				}
				var a = [], f, i, v;
				for (i in x) {
					v = x[i];
					f = pp[typeof v];
					if (f) {
						v = f(v, d + 1, w);
						a.push(ps(d + 1, w) + pp.string(i) + ': ' + v);
					}
				}
				if (a.length < 1) return '{}';
				return '{\n' + a.join(',\n') + '\n' + ps(d, w) + '}';
			}
			return 'null';
		},
		'string': function (x) {
			if (/["\\\x00-\x1f]/.test(x)) {
				x = x.replace(/([\x00-\x1f\\"])/g, function(a, b) {
					var c = m[b];
					if (c) {
						return c;
					}
					c = b.charCodeAt();
					return '\\u00' +
						Math.floor(c / 16).toString(16) +
						(c % 16).toString(16);
				});
			}
			return '"' + x + '"';
		}
	};
	
	JSON.pprint = function(v, w) {
		w = w || '  ';
		var f = isNaN(v) ? pp[typeof v] : pp['number'];
		if (f) return f(v, 0, w);
		return "undefined";
	};
})();

