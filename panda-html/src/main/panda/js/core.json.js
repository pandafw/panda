(function() {
	if (typeof JSON == 'undefined') {
		var m = {
			'\b' : '\\b',
			'\t' : '\\t',
			'\n' : '\\n',
			'\f' : '\\f',
			'\r' : '\\r',
			'"' : '\\"',
			'\\' : '\\\\'
		};

		var pr = function(r, k, v) {
			if (r === undefined) {
				return v;
			}
			if (typeof (r) == 'function') {
				return r(k, v);
			}
			return r.indexOf(k) >= 0 ? v : undefined;
		}
		var ps = function(d, w) {
			if (w.length < 1) {
				return '';
			}
			var s = '';
			for (var i = 0; i < d; i++) {
				s += w;
			}
			return s;
		}
		var pp = {
			'array': function (x, r, d, w, p) {
				var a = [], f, i, l = x.length, v;
				if (l < 1) return '[]';
				for (i = 0; i < l; i++) {
					v = pr(r, i, x[i]);
					f = pp[typeof v];
					if (f) {
						v = f(v, r, d + p, w, p);
						a.push(ps(d + p, w) + v);
					}
				}
				if (p) {
					return '[\n' + a.join(',\n') + '\n' + ps(d, w) + ']';
				}
				else {
					return '[' + a.join(',') + ']';
				}
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
			'object': function (x, r, d, w, p) {
				if (x) {
					if (x instanceof Array) {
						return pp.array(x, r, d, w, p);
					}
					var a = [], f, i, v;
					for (i in x) {
						v = pr(r, s, x[i]);
						f = pp[typeof v];
						if (f) {
							v = f(v, r, d + p, w, p);
							a.push(ps(d + p, w) + pp.string(i) + ':' + (p ? ' ' : '') + v);
						}
					}
					if (a.length < 1) return '{}';
					if (p) {
						return '{\n' + a.join(',\n') + '\n' + ps(d, w) + '}';
					}
					else {
						return '{' + a.join(',') + '}';
					}
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
						return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
					});
				}
				return '"' + x + '"';
			}
		};

		JSON = {
			stringify: function(v, r, w) {
				w = w || '';
				var f = isNaN(v) ? pp[typeof v] : pp['number'];
				if (f) return f(v, r, 1, w, w.length > 0 ? 1 : 0);
				return "undefined";
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
	
	JSON.pprint = function(v, w) {
		w = w || '  ';
		var f = isNaN(v) ? pp[typeof v] : pp['number'];
		if (f) return f(v, r, 0, w, 1);
		return JSON.stringify(v, null, w);
	};
	
	JSON.sparse = function(v, r) {
		try {
			return JSON.parse(v, r);
		}
		catch (e) {
			return null;
		}
	}
})();

