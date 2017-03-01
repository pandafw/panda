if (typeof Array.prototype.indexOf != 'function') {
	Array.prototype.indexOf = function(o) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] === o) {
				return i;
			}
		}
		return -1;
	}
}

if (typeof Array.prototype.contains != 'function') {
	Array.prototype.contains = function(o) {
		return this.indexOf(o) >= 0;
	}
}

if (typeof Array.prototype.each != 'function') {
	Array.prototype.each = function(fn, scope) {
		scope = scope || window;
		for (var i = 0; i < this.length; i++) {
			if (fn.call(scope, this[i], i, this) === false) {
				break;
			}
		}
	}
}

if (typeof Array.prototype.empty != 'function') {
	Array.prototype.empty = function() {
		for (var i = 0; i <= this.length; i++) {
			this.shift();
		}
	}
}

if (typeof Array.prototype.remove != 'function') {
	Array.prototype.remove = function(o) {
		for (var i = this.length - 1; i >= 0; i--) {
			if (this[i] === o) {
				this.splice(i, 1);
			}
		}
	}
}

if (typeof Array.prototype.removeDuplicates != 'function') {
	Array.prototype.removeDuplicates = function() {
		for (var i = 0; i < this.length; i++) {
			for (var j = this.length - 1; j > i; j--) {
				if (this[i] === this[j]) {
					this.splice(j, 1);
				}
			}
		}
	}
}
/*--------------------------------------------------------
 dateformat.js - Simple date formatter
 Version 1.1 (Update 2008/04/02)

 Copyright (c) 2007-2008 onozaty (http://www.enjoyxstudy.com)

 Released under an MIT-style license.

 For details, see the web site:
 http://www.enjoyxstudy.com/javascript/dateformat/

 --------------------------------------------------------
 patterns
 y : Year         ex. "yyyy" -> "2007", "yy" -> "07"
 M : Month        ex. "MM" -> "05" "12", "M" -> "5" "12"
 d : Day          ex. "dd" -> "09" "30", "d" -> "9" "30"
 H : Hour (0-23)  ex. "HH" -> "00" "23", "H" -> "0" "23"
 h : Hour (1-12)  ex. "hh" -> "01" "12", "h" -> "1" "12"
 m : Minute       ex. "mm" -> "01" "59", "m" -> "1" "59"
 s : Second       ex. "ss" -> "00" "59", "s" -> "0" "59"
 S : Millisecond  ex. "SSS" -> "000" "012" "999", 
 "SS" -> "00" "12" "999", "S" -> "0" "12" "999"

 Text can be quoted using single quotes (') to avoid interpretation.
 "''" represents a single quote. 


 Useing..

 var fmt = new DateFormat("yyyy/MM/dd HH:mm:ss SSS");

 var str = fmt.format(new Date()); // "2007/05/10 12:21:19 002"
 var date = fmt.parse("2007/05/10 12:21:19 002"); // return Date object

 --------------------------------------------------------*/

if (typeof Date.prototype.format != "function") {
	Date.prototype.format = function(f) {
		return (new DateFormat(f)).format(this);
	};
}

var DateFormat = function(pattern) {
	this._init(pattern);
};

DateFormat.prototype = {
	_init : function(pattern) {
		this.pattern = pattern;
		this._patterns = [];

		for ( var i = 0; i < pattern.length; i++) {
			var ch = pattern.charAt(i);
			if (this._patterns.length == 0) {
				this._patterns[0] = ch;
			} else {
				var index = this._patterns.length - 1;
				if (this._patterns[index].charAt(0) == "'") {
					if (this._patterns[index].length == 1
							|| this._patterns[index]
									.charAt(this._patterns[index].length - 1) != "'") {
						this._patterns[index] += ch;
					} else {
						this._patterns[index + 1] = ch;
					}
				} else if (this._patterns[index].charAt(0) == ch) {
					this._patterns[index] += ch;
				} else {
					this._patterns[index + 1] = ch;
				}
			}
		}
	},

	format : function(date) {
		var result = [];
		for ( var i = 0; i < this._patterns.length; i++) {
			result[i] = this._formatWord(date, this._patterns[i]);
		}
		return result.join('');
	},

	_formatWord : function(date, pattern) {
		var formatter = this._formatter[pattern.charAt(0)];
		if (formatter) {
			return formatter.apply(this, [ date, pattern ]);
		} else {
			return pattern;
		}
	},

	_formatter : {
		"y" : function(date, pattern) {
				// Year
			var year = String(date.getFullYear());
			if (pattern.length <= 2) {
				year = year.substring(2, 4);
			} else {
				year = this._zeroPadding(year, pattern.length);
			}
			return year;
		},
		"M" : function(date, pattern) {
			// Month in year
			return this._zeroPadding(String(date.getMonth() + 1), pattern.length);
		},
		"d" : function(date, pattern) {
			// Day in month
			return this._zeroPadding(String(date.getDate()), pattern.length);
		},
		"H" : function(date, pattern) {
			// Hour in day (0-23)
			return this._zeroPadding(String(date.getHours()), pattern.length);
		},
		"h" : function(date, pattern) {
			// Hour in day (1-12)
			var h = date.getHours();
			h = h > 12 ? h - 12 : (h == 0 ? 12 : h);
			return this._zeroPadding(String(h), pattern.length);
		},
		"m" : function(date, pattern) {
			// Minute in hour
			return this._zeroPadding(String(date.getMinutes()), pattern.length);
		},
		"s" : function(date, pattern) {
			// Second in minute
			return this._zeroPadding(String(date.getSeconds()), pattern.length);
		},
		"S" : function(date, pattern) {
			// Millisecond
			return this
					._zeroPadding(String(date.getMilliseconds()), pattern.length);
		},
		"'" : function(date, pattern) {
			// escape
			if (pattern == "''") {
				return "'";
			} else {
				return pattern.replace(/'/g, '');
			}
		}
	},

	_zeroPadding : function(str, length) {
		if (str.length >= length) {
			return str;
		}

		return new Array(length - str.length + 1).join("0") + str;
	},

	/// Parser ///
	parse : function(text) {
		if (typeof text != 'string' || text == '')
			return null;

		var result = {
			year : 1970,
			month : 1,
			day : 1,
			hour : 0,
			min : 0,
			sec : 0,
			msec : 0
		};

		for ( var i = 0; i < this._patterns.length; i++) {
			if (text == '')
				return null; // parse error!!
			text = this._parseWord(text, this._patterns[i], result);
			if (text === null)
				return null; // parse error!!
		}
		if (text != '')
			return null; // parse error!!

		return new Date(result.year, result.month - 1, result.day, result.hour,
				result.min, result.sec, result.msec);
	},

	_parseWord : function(text, pattern, result) {
		var parser = this._parser[pattern.charAt(0)];
		if (parser) {
			return parser.apply(this, [ text, pattern, result ]);
		} else {
			if (text.indexOf(pattern) != 0) {
				return null;
			} else {
				return text.substring(pattern.length);
			}
		}
	},

	_parser : {
		"y" : function(text, pattern, result) {
				// Year
			var year;
			if (pattern.length <= 2) {
				year = text.substring(0, 2);
				year = year < 70 ? '20' + year : '19' + year;
				text = text.substring(2);
			} else {
				var length = (pattern.length == 3) ? 4 : pattern.length;
				year = text.substring(0, length);
				text = text.substring(length);
			}
			if (!this._isNumber(year))
				return null; // error
			result.year = parseInt(year, 10);
			return text;
		},
		"M" : function(text, pattern, result) {
			// Month in year
			var month;
			if (pattern.length == 1 && text.length > 1
					&& text.substring(0, 2).match(/1[0-2]/) != null) {
				month = text.substring(0, 2);
				text = text.substring(2);
			} else {
				month = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(month))
				return null; // error
			result.month = parseInt(month, 10);
			return text;
		},
		"d" : function(text, pattern, result) {
			// Day in month
			var day;
			if (pattern.length == 1 && text.length > 1
					&& text.substring(0, 2).match(/1[0-9]|2[0-9]|3[0-1]/) != null) {
				day = text.substring(0, 2);
				text = text.substring(2);
			} else {
				day = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(day))
				return null; // error
			result.day = parseInt(day, 10);
			return text;
		},
		"H" : function(text, pattern, result) {
			// Hour in day (0-23)
			var hour;
			if (pattern.length == 1 && text.length > 1
					&& text.substring(0, 2).match(/1[0-9]|2[0-3]/) != null) {
				hour = text.substring(0, 2);
				text = text.substring(2);
			} else {
				hour = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(hour))
				return null; // error
			result.hour = parseInt(hour, 10);
			return text;
		},
		"h" : function(text, pattern, result) {
			// Hour in day (1-12)
			var hour;
			if (pattern.length == 1 && text.length > 1
					&& text.substring(0, 2).match(/1[0-2]/) != null) {
				hour = text.substring(0, 2);
				text = text.substring(2);
			} else {
				hour = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(hour))
				return null; // error
			result.hour = parseInt(hour, 10);
			return text;
		},
		"m" : function(text, pattern, result) {
			// Minute in hour
			var min;
			if (pattern.length == 1 && text.length > 1
					&& text.substring(0, 2).match(/[1-5][0-9]/) != null) {
				min = text.substring(0, 2);
				text = text.substring(2);
			} else {
				min = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(min))
				return null; // error
			result.min = parseInt(min, 10);
			return text;
		},
		"s" : function(text, pattern, result) {
			// Second in minute
			var sec;
			if (pattern.length == 1 && text.length > 1
					&& text.substring(0, 2).match(/[1-5][0-9]/) != null) {
				sec = text.substring(0, 2);
				text = text.substring(2);
			} else {
				sec = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(sec))
				return null; // error
			result.sec = parseInt(sec, 10);
			return text;
		},
		"S" : function(text, pattern, result) {
			// Millimsecond
			var msec;
			if (pattern.length == 1 || pattern.length == 2) {
				if (text.length > 2
						&& text.substring(0, 3).match(/[1-9][0-9][0-9]/) != null) {
					msec = text.substring(0, 3);
					text = text.substring(3);
				} else if (text.length > 1
						&& text.substring(0, 2).match(/[1-9][0-9]/) != null) {
					msec = text.substring(0, 2);
					text = text.substring(2);
				} else {
					msec = text.substring(0, pattern.length);
					text = text.substring(pattern.length);
				}
			} else {
				msec = text.substring(0, pattern.length);
				text = text.substring(pattern.length);
			}
			if (!this._isNumber(msec))
				return null; // error
			result.msec = parseInt(msec, 10);
			return text;
		},
		"'" : function(text, pattern, result) {
			// escape
			if (pattern == "''") {
				pattern = "'";
			} else {
				pattern = pattern.replace(/'/g, '');
			}
			if (text.indexOf(pattern) != 0) {
				return null; // error
			} else {
				return text.substring(pattern.length);
			}
		}
	},

	_isNumber : function(str) {
		return /^[0-9]*$/.test(str);
	}
};

function panda_call(f, p) {
	switch (typeof(f)) {
	case "function":
		f.call(p);
		break;
	case "string":
		f = new Function(f);
		f.call(p);
		break;
	}
}

if (typeof Function.prototype.callback != "function") {
	/**
	 * Creates a callback that passes arguments[0], arguments[1], arguments[2], ...
	 * Call directly on any function. Example: <code>myFunction.callback(arg1, arg2)</code>
	 * Will create a function that is bound to those 2 args. <b>If a specific scope is required in the
	 * callback, use {@link #delegate} instead.</b> The function returned by callback always
	 * executes in the window scope.
	 * <p>This method is required when you want to pass arguments to a callback function.  If no arguments
	 * are needed, you can simply pass a reference to the function as a callback (e.g., callback: myFn).
	 * However, if you tried to pass a function with arguments (e.g., callback: myFn(arg1, arg2)) the function
	 * would simply execute immediately when the code is parsed. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}
		
		// clicking the button alerts "Hi, Fred"
		new Ext.Button({
			text: 'Say Hi',
			renderTo: Ext.getBody(),
			handler: sayHi.callback('Fred')
		});
		</code></pre>
	 * @return {Function} The new function
	 */
	Function.prototype.callback = function(/*args...*/){
		// make args available, in function below
		var args = arguments;
		var method = this;
		return function() {
			return method.apply(window, args);
		};
	};
}

if (typeof Function.prototype.bind != "function") {
	/**
	 * @see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind
	 * 
	 * Syntax:
	 * <pre><code>
		fun.bind(thisArg[, arg1[, arg2[, ...]]])
		</code></pre>
	 * @return {Function} The new function
	 */
	Function.prototype.bind = function(/*args...*/){
		// make args available, in function below
		var scope = arguments[0] || window;
		var args = Array.prototype.slice.call(arguments, 1);
		var method = this;
		return function() {
			return method.apply(scope, args);
		};
	};
}

if (typeof Function.prototype.delegate != "function") {
	/**
	 * Creates a delegate (callback) that sets the scope to obj.
	 * Call directly on any function. Example: <code>this.myFunction.delegate(this, [arg1, arg2])</code>
	 * Will create a function that is automatically scoped to obj so that the <tt>this</tt> variable inside the
	 * callback points to obj. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			// Note this use of "this.text" here.  This function expects to
			// execute within a scope that contains a text property.  In this
			// example, the "this" variable is pointing to the btn object that
			// was passed in delegate below.
			alert('Hi, ' + name + '. You clicked the "' + this.text + '" button.');
		}

		var btn = new Button({
			text: 'Say Hi'
		});

		// This callback will execute in the scope of the
		// button instance. Clicking the button alerts
		// "Hi, Fred. You clicked the "Say Hi" button."
		btn.on('click', sayHi.delegate(btn, ['Fred']));
		</code></pre>
	 * @param {Object} obj (optional) The object for which the scope is set
	 * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
	 * @param {Boolean} appendArgs (optional) if True args are appended to call args instead of overriding
	 * @return {Function} The new function
	 */
	Function.prototype.delegate = function(obj, args, appendArgs) {
		var method = this;
		return function() {
			var callArgs = args || arguments;
			if (appendArgs === true) {
				callArgs = Array.prototype.slice.call(arguments, 0);
				callArgs = callArgs.concat(args);
			}
			return method.apply(obj || window, callArgs);
		};
	};
}

if (typeof Function.prototype.delay != "function") {
	/**
	 * Calls this function after the number of millseconds specified, optionally in a specific scope. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		// executes immediately:
		sayHi('Fred');

		// executes after 2 seconds:
		sayHi.delay(2000, this, ['Fred']);

		// this syntax is sometimes useful for deferring
		// execution of an anonymous function:
		(function(){
			alert('Anonymous');
		}).delay(100);
		</code></pre>
	 * @param {Number} millis The number of milliseconds for the setTimeout call (if 0 the function is executed immediately)
	 * @param {Object} obj (optional) The object for which the scope is set
	 * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
	 * @return {Number} The timeout id that can be used with clearTimeout
	 */
	Function.prototype.delay = function(millis, obj, args) {
		var fn = this.delegate(obj, args);
		if (millis) {
			return setTimeout(fn, millis);
		}
		fn();
		return 0;
	};
}

if (typeof Function.prototype.precall != "function") {
	/**
	 * Creates an interceptor function. The passed fcn is called before the original one. If it returns false,
	 * the original one is not called. The resulting function returns the results of the original function.
	 * The passed fcn is called with the parameters of the original function. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		sayHi('Fred'); // alerts "Hi, Fred"

		// create a new function that validates input without
		// directly modifying the original function:
		var sayHiToFriend = sayHi.precall(function(name){
			return name == 'Brian';
		});

		sayHiToFriend('Fred');	// no alert
		sayHiToFriend('Brian'); // alerts "Hi, Brian"
		</code></pre>
	 * @param {Function} fcn The function to call before the original
	 * @param {Object} scope (optional) The scope of the passed fcn (Defaults to scope of original function or window)
	 * @return {Function} The new function
	 */
	Function.prototype.precall = function(fcn, scope) {
		if(typeof fcn != "function"){
			return this;
		}
		var method = this;
		return function() {
			fcn.target = this;
			fcn.method = method;
			if(fcn.apply(scope || this || window, arguments) === false){
				return;
			}
			return method.apply(this || window, arguments);
		};
	};
}

if (typeof Function.prototype.postcall != "function") {
	/**
	 * Create a combined function call sequence of the original function + the passed function.
	 * The resulting function returns the results of the original function.
	 * The passed fcn is called with the parameters of the original function. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		sayHi('Fred'); // alerts "Hi, Fred"

		var sayGoodbye = sayHi.postcall(function(name){
			alert('Bye, ' + name);
		});

		sayGoodbye('Fred'); // both alerts show
		</code></pre>
	 * @param {Function} fcn The function to sequence
	 * @param {Object} scope (optional) The scope of the passed fcn (Defaults to scope of original function or window)
	 * @return {Function} The new function
	 */
	Function.prototype.postcall = function(fcn, scope) {
		if (typeof fcn != "function") {
			return this;
		}
		var method = this;
		return function() {
			var retval = method.apply(this || window, arguments);
			fcn.apply(scope || this || window, arguments);
			return retval;
		};
	};
}
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

if (typeof Number.trim != "function") {
	Number.trim = function(s) {
		if (typeof(s) != 'string') {
			return s;
		}
		var h = '1234567890';
		var z = '１２３４５６７８９０';
		var ss = s.replace(/,/g, '').split('');
		for (i = 0; i < ss.length; i++) {
			var j = z.indexOf(ss[i]);
			if (j >= 0) {
				ss[i] = h[j];
			}
		}
		return ss.join('');
	};
}
if (typeof Number.parseInt != "function") {
	Number.parseInt = function(s, r) {
		var n = parseInt(Number.trim(s), r);
		return isNaN(n) ? 0 : n;
	};
}
if (typeof Number.parseFloat != "function") {
	Number.parseFloat = function(s) {
		var n = parseFloat(Number.trim(s));
		return isNaN(n) ? 0 : n;
	};
}

if (typeof Number.prototype.format != "function") {
	Number.prototype.format = function(pattern) {
		return (new DecimalFormat(pattern)).format(this);
	};
}

/**
 * @class DecimalFormat
 * @constructor
 * @param {String} formatStr
 * @author Oskan Savli
 */
function DecimalFormat(formatStr) {
	/**
	 * @fieldOf DecimalFormat
	 * @type String
	 */
	this.prefix = '';
	/**
	 * @fieldOf DecimalFormat
	 * @type String
	 */
	this.suffix = '';
	/**
	 * @description Grouping size
	 * @fieldOf DecimalFormat
	 * @type String
	 */
	this.comma = 0;
	/**
	 * @description Minimum integer digits to be displayed
	 * @fieldOf DecimalFormat
	 * @type Number
	 */
	this.minInt = 1;
	/**
	 * @description Minimum fractional digits to be displayed
	 * @fieldOf DecimalFormat
	 * @type String
	 */
	this.minFrac = 0;
	/**
	 * @description Maximum fractional digits to be displayed
	 * @fieldOf DecimalFormat
	 * @type String
	 */
	this.maxFrac = 0;

	// get prefix
	for (var i = 0; i < formatStr.length; i++) {
		if (formatStr.charAt(i) == '#' || formatStr.charAt(i) == '0') {
			this.prefix = formatStr.substring(0, i);
			formatStr = formatStr.substring(i);
			break;
		}
	}

	// get suffix
	this.suffix = formatStr.replace(/[#]|[0]|[,]|[.]/g, '');

	// get number as string
	var numberStr = formatStr.replace(/[^0#,.]/g, '');

	var intStr = '';
	var fracStr = '';
	var point = numberStr.indexOf('.');
	if (point != -1) {
		intStr = numberStr.substring(0, point);
		fracStr = numberStr.substring(point + 1);
	}
	else {
		intStr = numberStr;
	}

	var commaPos = intStr.lastIndexOf(',');
	if (commaPos != -1) {
		this.comma = intStr.length - 1 - commaPos;
	}

	intStr = intStr.replace(/[,]/g, ''); // remove commas

	fracStr = fracStr.replace(/[,]|[.]+/g, '');

	this.maxFrac = fracStr.length;
	var tmp = intStr.replace(/[^0]/g, ''); // remove all except zero
	if (tmp.length > this.minInt)
		this.minInt = tmp.length;
	tmp = fracStr.replace(/[^0]/g, '');
	this.minFrac = tmp.length;
}

/**
 * @description Formats given value
 * @methodOf DecimalFormat
 * @param {String}
 *            numberStr
 * @return {String} Formatted number
 * @author Oskan Savli
 */
DecimalFormat.prototype.format = function(numStr) { // 1223.06 --> $1,223.06
    // remove prefix, suffix and commas
	var numberStr = this.parse(numStr).toLowerCase();

	// do not format if not a number
	if (isNaN(numberStr) || numberStr.length == 0)
		return numStr;

	// scientific numbers
	if (i = numberStr.indexOf("e") != -1) {
		var n = Number(numberStr);
		if (n == "Infinity" || n == "-Infinity")
			return numberStr;
		numberStr = n + "";
		if (numberStr.indexOf('e') != -1)
			return numberStr;
	}

	var negative = false;
	// remove sign
	if (numberStr.charAt(0) == '-') {
		negative = true;
		numberStr = numberStr.substring(1);
	}
	else if (numberStr.charAt(0) == '+') {
		numberStr = numberStr.substring(1);
	}

	var point = numberStr.indexOf('.'); // position of point character
	var intStr = '';
	var fracStr = '';
	if (point != -1) {
		intStr = numberStr.substring(0, point);
		fracStr = numberStr.substring(point + 1);
	}
	else {
		intStr = numberStr;
	}
	fracStr = fracStr.replace(/[.]/, ''); // remove other point characters

	var isPercentage = this.suffix && this.suffix.charAt(0) === '%';
	// if percentage, number will be multiplied by 100.
	var minInt = this.minInt, minFrac = this.minFrac, maxFrac = this.maxFrac;
	if (isPercentage) {
		minInt -= 2;
		minFrac += 2;
		maxFrac += 2;
	}

	if (fracStr.length > maxFrac) { // round
		// case 6143
		var num = new Number('0.' + fracStr);
		num = (maxFrac == 0) ? Math.round(num) : num.toFixed(maxFrac);
		// toFixed method has bugs on IE (0.7 --> 0)
		fracStr = num.toString(10).substr(2);
		var c = (num >= 1) ? 1 : 0; // carry
		var x, i = intStr.length - 1;
		while (c) { // increment intStr
			if (i == -1) {
				intStr = '1' + intStr;
				break;
			}
			else {
				x = intStr.charAt(i);
				if (x == 9) {
					x = '0';
					c = 1;
				}
				else {
					x = (++x) + '';
					c = 0;
				}
				intStr = intStr.substring(0, i) + x
						+ intStr.substring(i + 1, intStr.length);
				i--;
			}
		}
	}
	for ( var i = fracStr.length; i < minFrac; i++) { // if minFrac=4 then
														// 1.12 --> 1.1200
		fracStr = fracStr + '0';
	}
	while (fracStr.length > minFrac
			&& fracStr.charAt(fracStr.length - 1) == '0') { // if minInt=4 then
															// 00034 --> 0034)
		fracStr = fracStr.substring(0, fracStr.length - 1);
	}

	for (var i = intStr.length; i < minInt; i++) { // if minInt=4 then 034 -->
													// 0034
		intStr = '0' + intStr;
	}
	
	while (intStr.length > minInt && intStr.charAt(0) == '0') { // if minInt=4
																// then 00034
																// --> 0034)
		intStr = intStr.substring(1);
	}

	if (isPercentage) { // multiply by 100
		intStr += fracStr.substring(0, 2);
		fracStr = fracStr.substring(2);
	}

	var j = 0;
	for (var i = intStr.length; i > 0; i--) { // add commas
		if (j != 0 && j % this.comma == 0) {
			intStr = intStr.substring(0, i) + ',' + intStr.substring(i);
			j = 0;
		}
		j++;
	}

	var formattedValue;
	if (fracStr.length > 0)
		formattedValue = this.prefix + intStr + '.' + fracStr + this.suffix;
	else
		formattedValue = this.prefix + intStr + this.suffix;

	if (negative) {
		formattedValue = '-' + formattedValue;
	}

	return formattedValue;
}


/**
 * @description Converts formatted value back to non-formatted value
 * @methodOf DecimalFormat
 * @param {String}
 *            fNumberStr Formatted number
 * @return {String} Original number
 * @author Oskan Savli
 */
DecimalFormat.prototype.parse = function(fNumStr) { // $1,223.06 -->
															// 1223.06
  fNumStr += ''; // ensure it is string
	if (!fNumStr)
		return ''; // do not return undefined or null
	if (!isNaN(fNumStr))
		return this.getNumericString(fNumStr);
	var fNumberStr = fNumStr;
	var negative = false;
	if (fNumStr.charAt(0) == '-') {
		fNumberStr = fNumberStr.substr(1);
		negative = true;
	}
	var pIndex = fNumberStr.indexOf(this.prefix);
	var sIndex = (this.suffix == '') ? fNumberStr.length : fNumberStr.indexOf(
			this.suffix, this.prefix.length + 1);
	if (pIndex == 0 && sIndex > 0) {
		// remove suffix
		fNumberStr = fNumberStr.substr(0, sIndex);
		// remove prefix
		fNumberStr = fNumberStr.substr(this.prefix.length);
		// remove commas
		fNumberStr = fNumberStr.replace(/,/g, '');
		if (negative)
			fNumberStr = '-' + fNumberStr;
		if (!isNaN(fNumberStr))
			return this.getNumericString(fNumberStr);
	}
	return fNumStr;
}

/**
 * @description We shouldn't return strings like 1.000 in parse method.
 *              However, using only Number(str) is not enough, because it omits .
 *              in big numbers like 23423423423342234.34 => 23423423423342236 .
 *              There's a conflict in cases 6143 and 6541.
 * @methodOf DecimalFormat
 * @param {String}
 *            str Numberic string
 * @return {String} Corrected numeric string
 * @author Serdar Bicer
 */
DecimalFormat.prototype.getNumericString = function(str){
	// first convert to number
	var num = new Number(str);
	// check if there is a missing dot
	var numStr = num + '';
	if (str.indexOf('.') > -1 && numStr.indexOf('.') < 0) {
		// check if original string has all zeros after dot or not
		for ( var i = str.indexOf('.') + 1; i < str.length; i++) {
			// if not, this means we lost precision
			if (str.charAt(i) !== '0')
				return str;
		}
		return numStr;
	}
	return str;
}
if (typeof String.prototype.hashCode != "function") {
	String.prototype.hashCode = function() {
		var h = 0;
		for (var i = 0; i < this.length; i++) {
//			h = 31 * h + this.charCodeAt(i);
			h = ((h << 5) - h) + this.charCodeAt(i); // faster
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
if (typeof String.prototype.trimLeft != "function") {
	String.prototype.trimLeft = function() {
		var re = /^\s+/;
		return this.replace(re, "");
	};
}
if (typeof String.prototype.trimRight != "function") {
	String.prototype.trimRight = function() {
		var re = /\s+$/;
		return this.replace(re, "");
	};
}
if (typeof String.prototype.trim != "function") {
	String.prototype.trim = function() {
		var re = /^\s+|\s+$/g;
		return this.replace(re, "");
	};
}
if (typeof String.prototype.stripLeft != "function") {
	String.prototype.stripLeft = function() {
		var re = /^[\s\u3000]+/;
		return this.replace(re, "");
	};
}
if (typeof String.prototype.stripRight != "function") {
	String.prototype.stripRight = function() {
		var re = /[\s\u3000]+$/;
		return this.replace(re, "");
	};
}
if (typeof String.prototype.strip != "function") {
	String.prototype.strip = function() {
		var re = /^[\s\u3000]+|[\s\u3000]+$/g;
		return this.replace(re, "");
	};
}
if (typeof String.prototype.left != "function") {
	String.prototype.left = function(len) {
		return this.substr(0, len);
	};
}
if (typeof String.prototype.mid != "function") {
	String.prototype.mid = String.prototype.substr;
}
if (typeof String.prototype.right != "function") {
	String.prototype.right = function(len) {
		if (len >= this.length) {
			return this;
		}
		return this.substr(this.length - len, len);
	};
}
if (typeof String.prototype.leftPad != "function") {
	String.prototype.leftPad = function(ch, size) {
		if (!ch) {
			ch = " ";
		}
		var r = this;
		while (r.length < size) {
			r = ch + r;
		}
		return r;
	};
}
if (typeof String.prototype.rightPad != "function") {
	String.prototype.rightPad = function(ch, size) {
		if (!ch) {
			ch = " ";
		}
		var r = this;
		while (r.length < size) {
			r += chr;
		}
		return r;
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
if (typeof String.prototype.capitalize != "function") {
	String.prototype.capitalize = function() {
		return this.substr(0, 1).toUpperCase() + this.substr(1);
	};
}
if (typeof String.prototype.uncapitalize != "function") {
	String.prototype.uncapitalize = function() {
		return this.substr(0, 1).toLowerCase() + this.substr(1);
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
if (typeof String.prototype.size != 'function') {
	String.prototype.size = function(s) {
		var sz = 0;
		for (var i = 0; i < this.length; i++) {
			sz++;
			if (this.charCodeAt(i) > 0xFF) {
				sz++;
			}
		}
		return sz;
	};
}
if (typeof String.prototype.ellipsis != 'function') {
	String.prototype.ellipsis = function(len) {
		if (this.length > len) {
			return this.substr(0, len - 3) + "...";
		}
		return this;
	};
}
/**
 * Truncate a string and add an ellipsiz ('...') to the end if it exceeds the specified length
 * the length of charCodeAt(i) > 0xFF will be treated as 2. 
 * @param {String} value The string to truncate
 * @param {Number} length The maximum length to allow before truncating
 * @return {String} The converted text
 */
if (typeof String.prototype.ellipsiz != 'function') {
	String.prototype.ellipsiz = function(len) {
		var sz = 0;
		for (var i = 0; i < this.length; i++) {
			sz++;
			if (this.charCodeAt(i) > 0xFF) {
				sz++;
			}
			if (sz > len) {
				return this.substring(0, i) + '...';
			}
		}
		return this;
	}
}

if (typeof String.prototype.escapeRegExp != "function") {
	String.prototype.escapeRegExp = function() {
		return this.replace(/([.*+?^=!:${}()|[\]\/\\])/g, '\\$1');
	};
}
if (typeof String.prototype.escapeHtml != "function") {
	String.prototype.escapeHtml = function() {
		return this.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
	};
}
if (typeof String.prototype.unescapeHtml != "function") {
	String.prototype.unescapeHtml = function() {
		return this.replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&amp;/g, "&");
	};
}
if (typeof String.prototype.escapePhtml != "function") {
	String.prototype.escapePhtml = function() {
		return this.escapeHtml().replace(/\r?\n/g, "<br/>");
	};
}
if (typeof String.prototype.unescapePhtml != "function") {
	String.prototype.unescapePhtml = function() {
		return this.replace(/<br\/>/g, "\n").unescapeHtml();
	};
}
if (typeof String.prototype.prettifyXml != "function") {
	String.prototype.prettifyXml = function() {
		var fmt = '';
		var reg = /(>)(<)(\/*)/g;
		var xml = this.replace(reg, '$1\r\n$2$3');
		var pad = 0;
		var ss = xml.split('\r\n');
		for (var i = 0; i < ss.length; i++) {
			var s = ss[i];
			var indent = 0;
			if (s.match( /.+<\/\w[^>]*>$/ )) {
				indent = 0;
			}
			else if (s.match( /^<\/\w/ )) {
				if (pad != 0) {
					pad -= 1;
				}
			}
			else if (s.match( /^<\w[^>]*[^\/]>.*$/ )) {
				indent = 1;
			}
			else {
				indent = 0;
			}
	
			fmt += ("").leftPad(' ', pad * 2) + s + '\r\n';
			pad += indent;
		}
	
		return fmt;
	};
}
if (typeof String.prototype.encodeUtf8 != "function") {
	String.prototype.encodeUtf8 = function() {
		string = this.replace(/rn/g,"\n");
		var utftext = "";

		for (var n = 0; n < string.length; n++) {

			var c = string.charCodeAt(n);

			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
		}

		return utftext;
	};
}
if (typeof String.prototype.decodeUtf8 != "function") {
	String.prototype.decodeUtf8 = function() {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;

		while ( i < this.length ) {
			c = this.charCodeAt(i);

			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = this.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = this.charCodeAt(i+1);
				c3 = this.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}

		return string;
	};
}
if (typeof String.prototype.encodeBase64 != "function") {
	String.prototype.encodeBase64 = function() {
		if (typeof btoa === 'function') {
			 return btoa(this);
		}
		var base64s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		var bits;
		var dual;
		var i = 0;
		var encOut = "";
		while(this.length >= i + 3){
			bits = (this.charCodeAt(i++) & 0xff) <<16 | (this.charCodeAt(i++) & 0xff) <<8 | this.charCodeAt(i++) & 0xff;
			encOut += base64s.charAt((bits & 0x00fc0000) >>18) + base64s.charAt((bits & 0x0003f000) >>12) + base64s.charAt((bits & 0x00000fc0) >> 6) + base64s.charAt((bits & 0x0000003f));
		}
		if(this.length -i > 0 && this.length -i < 3){
			dual = Boolean(this.length -i -1);
			bits = ((this.charCodeAt(i++) & 0xff) <<16) |	 (dual ? (this.charCodeAt(i) & 0xff) <<8 : 0);
			encOut += base64s.charAt((bits & 0x00fc0000) >>18) + base64s.charAt((bits & 0x0003f000) >>12) + (dual ? base64s.charAt((bits & 0x00000fc0) >>6) : '=') + '=';
		}
		return(encOut);
	};
}

if (typeof String.prototype.decodeBase64 != "function") {
	String.prototype.decodeBase64 = function() {
		if (typeof atob === 'function') {
			return atob(this);
		}
		var base64s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		var bits, decOut = "";
		for (var i = 0; i < this.length; i += 4) {
			bits = (base64s.indexOf(this.charAt(i)) & 0xff) <<18 | (base64s.indexOf(this.charAt(i +1)) & 0xff) <<12 | (base64s.indexOf(this.charAt(i +2)) & 0xff) << 6 | base64s.indexOf(this.charAt(i +3)) & 0xff;
			decOut += String.fromCharCode((bits & 0xff0000) >>16, (bits & 0xff00) >>8, bits & 0xff);
		}
		if (this.charCodeAt(i -2) == 61) {
			return(decOut.substring(0, decOut.length -2));
		}
		else if (this.charCodeAt(i -1) == 61) {
			return(decOut.substring(0, decOut.length -1));
		}
		else {
			return(decOut);
		}
	};
}

if (typeof String.prototype.queryArrays != "function") {
	String.prototype.queryArrays = function(f) {
		var qs = [], s = this, i = s.indexOf('?');
		if (i >= 0) {
			s = s.substring(i + 1);
		}
		var ss = s.split('&');
		for (i = 0; i < ss.length; i++) {
			var pv = ss[i].split('=');
			var n = decodeURIComponent(pv[0]);
			if (f == null || f == n) {
				qs.push({
					name: n,
					value: pv.length > 1 ? decodeURIComponent(pv[1]) : ''
				});
			}
		}
		return qs;
	};
}

if (typeof String.prototype.queryParams != "function") {
	String.prototype.queryParams = function() {
		var qs = {}, s = this, i = s.indexOf('?');
		if (i >= 0) {
			s = s.substring(i + 1);
		}
		var ss = s.split('&');
		for (i = 0; i < ss.length; i++) {
			var pv = ss[i].split('=');
			var n = decodeURIComponent(pv[0]);
			qs[n] = pv.length > 1 ? decodeURIComponent(pv[1]) : '';
		}
		return qs;
	};
}

//-----------------------------------
//  Static functions
//-----------------------------------
if (typeof String.defaults != "function") {
	String.defaults = function(s, d) {
		return s == null ? d : s;
	};
}
if (typeof String.hashCode != "function") {
	String.hashCode = function(s) {
		return s == null ? 0 : s.hashCode();
	};
}
if (typeof String.isEmpty != "function") {
	String.isEmpty = function(s) {
		return s == null || s.length < 1;
	};
}
if (typeof String.isNotEmpty != "function") {
	String.isNotEmpty = function(s) {
		return s != null && s.length > 0;
	};
}
if (typeof String.leftPad != "function") {
	String.leftPad = function(s, ch, size) {
		return s != null ? String(s).leftPad(ch, size) : "".leftPad(ch, size);
	};
}
if (typeof String.rightPad != "function") {
	String.rightPad = function(s, ch, size) {
		return s != null ? String(s).rightPad(ch, size) : "".rightPad(ch, size);
	};
}
if (typeof String.startsWith != "function") {
	String.startsWith = function(s, w) {
		return s != null ? String(s).startsWith(w) : false;
	};
}
if (typeof String.endsWith != "function") {
	String.endsWith = function(s, w) {
		return s != null ? String(s).endsWith(w) : false;
	};
}
if (typeof String.ellipsis != "function") {
	String.ellipsis = function(s, l) {
		return s != null ? String(s).ellipsis(l) : "";
	};
}
if (typeof String.ellipsiz != "function") {
	String.ellipsiz = function(s, l) {
		return s != null ? String(s).ellipsiz(l) : "";
	};
}
if (typeof String.escapeHtml != "function") {
	String.escapeHtml = function(s) {
		return s != null ? String(s).escapeHtml() : "";
	};
}
if (typeof String.unescapeHtml != "function") {
	String.unescapeHtml = function(s) {
		return s != null ? String(s).unescapeHtml() : "";
	};
}
if (typeof String.escapePhtml != "function") {
	String.escapePhtml = function(s) {
		return s != null ? String(s).escapePhtml() : "";
	};
}
if (typeof String.unescapePhtml != "function") {
	String.unescapePhtml = function(s) {
		return s != null ? String(s).unescapePhtml() : "";
	};
}
if (typeof String.prettifyXml != "function") {
	String.prettifyXml = function(s) {
		return s != null ? String(s).prettifyXml() : "";
	};
}
if (typeof String.encodeUtf8 != 'function') {
	String.encodeUtf8 = function(s) {
		return s != null ? String(s).encodeUtf8() : "";
	};
}
if (typeof String.decodeUtf8 != 'function') {
	String.decodeUtf8 = function(s) {
		return s != null ? String(s).decodeUtf8() : "";
	};
}
if (typeof String.encodeBase64 != 'function') {
	String.encodeBase64 = function(s) {
		return s != null ? String(s).encodeBase64() : "";
	};
}
if (typeof String.decodeBase64 != 'function') {
	String.decodeBase64 = function(s) {
		return s != null ? String(s).decodeBase64() : "";
	};
}
if (typeof String.escapeRegExp != 'function') {
	String.escapeRegExp = function(s) {
		return s != null ? String(s).escapeRegExp() : "";
	};
}
if (typeof String.queryParams != "function") {
	String.queryParams = function(s) {
		return s != null ? String(s).queryParams(s) : [];
	};
}
if (typeof String.formatSize != "function") {
	var KB = 1024;
	var MB = KB * KB;
	var GB = MB * KB;
	var TB = GB * KB;
	var PB = TB * KB;
	
	String.formatSize = function(n) {
		var sz = "";
		if (n >= PB) {
			sz = Math.round(n / PB) + ' PB';
		}
		else if (n >= TB) {
			sz = Math.round(n / TB) + ' TB';
		}
		else if (n >= GB) {
			sz = Math.round(n / GB) + ' GB';
		}
		else if (n >= MB) {
			sz = Math.round(n / MB) + ' MB';
		}
		else if (n >= KB) {
			sz = Math.round(n / KB) + ' KB';
		}
		else if (n != '') {
			sz = n + ' bytes';
		}
		return sz;
	};
}

(function($) {
	function setAlertType($p, s, t) {
		$p.removeClass('alert-danger alert-warning alert-info alert-success').addClass(s.types[t]);
	}

	function addMsg($p, s, m, t) {
		var ic = s.icons[t];
		var tc = s.texts[t];
		$p.append('<ul class="' +  s.ulCls + '"><li class="' + tc + '"><i class="' + ic + '"></i>' + m + '</li></ul>');
		setAlertType($p, s, t);
	}

	function addMsgs($p, s, m, t) {
		if (m) {
			var ic = s.icons[t];
			var tc = s.texts[t];
			var h = '<ul class="' + s.ulCls + '">';
			if ($.isArray(m)) {
				for (var i = 0; i < m.length; i++) {
					h += '<li class="' + tc + '"><i class="' + ic + '"></i>' + m[i] + '</li>';
				}
			}
			else {
				for (var n in m) {
					var v = m[n];
					for (var i = 0; i < v.length; i++) {
						h += '<li class="' + tc + '"><i class="' + ic + '"></i>' + v[i] + '</li>';
					}
				}
			}
			h += '</ul>';
			$p.append(h);
			setAlertType($p, s, t);
		}
	}
	
	function addAlerts($p, s, m, t) {
		if (typeof(m) == 'string') {
			addMsg($p, s, m, t);
		}
		else if ($.isArray(m)) {
			for (var i = 0; i < m.length; i++) {
				if (typeof(m[i]) == 'string') {
					addMsg($p, s, m[i], t);
				}
				else {
					addMsg($p, s, m[i].html, m[i].type);
				}
			}
		}
		else if (m) {
			if (m.params) {
				addMsgs($p, s, m.params.errors, "error");
			}
			if (m.action) {
				addMsgs($p, s, m.action.errors, "error");
				addMsgs($p, s, m.action.warnings, "warn");
				addMsgs($p, s, m.action.confirms, "help");
				addMsgs($p, s, m.action.messages, "info");
			}
		}
	}

	$.palert = {
		ulCls: 'fa-ul',
		icons: {
			'down': 'fa-caret-down',
			'up': 'fa-caret-up',
			'info': 'fa-li fa fa-info-circle',
			'help':'fa-li fa fa-question-circle',
			'warn': 'fa-li fa fa-exclamation-triangle',
			'error': 'fa-li fa fa-exclamation-circle'
		},
		texts: {
			'info': 'text-success',
			'help':'text-warning',
			'warn': 'text-warning',
			'error': 'text-danger'
		},
		types: {
			'info': 'alert-success',
			'help':'alert-info',
			'warn': 'alert-warning',
			'error': 'alert-danger'
		}
	};
	
	var palert = function($c, s) {
		s = $.extend({}, $.palert, s);
		return {
			api: function() {
				return this;
			},
			clear: function() {
				$c.children('.alert').remove();
				return this;
			},
			error: function(m) {
				this.add(m, 'error');
				return this;
			},
			warn: function(m) {
				this.add(m, 'warn');
				return this;
			},
			promt: function(m) {
				this.add(m, 'help');
				return this;
			},
			info: function(m) {
				this.add(m, 'info');
				return this;
			},
			add: function(m, t) {
				t = t || 'info';
				var $p = $c.children('.p-alert');
				var a = false;
				if ($p.size() < 1) {
					$p = $('<div></div>').addClass('p-alert alert alert-dismissable fade in').css('display', 'none');
					$c.prepend($p);
					$p.append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>");
					a = true;
				}
				
				addAlerts($p, s, m, t);

				if (a) { 
					$p.slideDown();
				}
				return this;
			}
		}
	};
	
	$.fn.palert = function(option, v1, v2) {
		return this.each(function () {
			var ops = typeof option === 'object' && option;
			var pa = palert($(this), ops);
			if (typeof option === 'string') {
				pa[option](v1, v2);
			}
		});
	};

	$.palert.notify = function(m, t, s) {
		s = $.extend({}, $.palert, s);
		t = t || 'info';
		var ns = $.extend({ style: 'palert' }, $.palert.notifys);

		var $p = $('<div></div>').addClass('p-alert alert');
		addAlerts($p, s, m, t);
		
		$.notify({ html: $p}, ns);
	}
	
	$.palert.toggleFieldErrors = function(el) {
		var $fes = $(el).closest('.p-field-errors-alert').next('.p-field-errors');
		var id = $.palert.icons.down;
		var iu = $.palert.icons.up;
		if ($fes.is(':hidden')) {
			$fes.slideDown();
			$(el).children('i').removeClass(id).addClass(iu);
		}
		else {
			$fes.slideUp();
			$(el).children('i').removeClass(iu).addClass(id);
		}
		return false;
	};

	// ==================
	$(window).on('load', function () {
		if ($.notify) {
			$.notify.addStyle('palert', {
				html: '<div data-notify-html="html"></div>'
			});
		}
	});
})(jQuery);
(function($) {
	$(window).on('load', function() {
		$(".p-checkboxlist.order")
			.removeClass("order")
			.addClass("ordered")
			.find(":checkbox").click(function() {
				var $t = $(this);
				var $l = $t.closest('label');
				var $h = $t.closest('.p-checkboxlist').find('hr');
				$l.fadeOut(function() {
					if ($t.is(':checked')) {
						$l.insertBefore($h);
					}
					else {
						$l.insertAfter($h);
					}
					$l.fadeIn();
				});
			});
	});
})(jQuery);
(function($) {
	function focusForm() {
		var f = false;
		$('form[focusme]').each(function() {
			var $i = $(this);
			if (f) {
				$i.removeAttr('focusme');
				return;
			}

			var a = $i.attr('focusme');
			$i.removeAttr('focusme');

			var $a = null;
			if (a == 'true') {
				$a = $i.find('input,select,textarea,button').not(':hidden,:disabled,[readonly]').eq(0);
			}
			else if (a != '' && a != 'false') {
				$a = $i.find(a).eq(0);
			}
			
			if ($a && $a.length) {
				f = true;
				$a.focus();
				$('body').scrollTop(0).scrollLeft(0);
			}
			
		});
	}

	function fireForm() {
		$('textarea[enterfire]').each(function() {
			var f = $(this).attr("enterfire");
			if (f) {
				$(this).removeAttr("enterfire").keyup(function(evt) {
					if (evt.ctrlKey && evt.which == 13) {
						if (f == "form") {
							$(this).closest("form").submit();
						}
						else {
							$(f).click();
						}
					}
				});
			}
		});
	}

	function hookForm() {
		$('form').each(function() {
			var $t = $(this);
			if ($t.data("actionHooked")) {
				return;
			}
			$t.data('actionHooded', true)
				.find('input[data-action], button[data-action]').click(function() {
					$i = $(this);
					$i.closest('form').attr('action', $i.data('action'));
				});
		});
	}
	
	$(window).on('load', function() {
		hookForm();
		fireForm();
		focusForm();
	});
})(jQuery);
if (typeof(panda) == "undefined") { panda = {}; }

function plv_options(id, options) {
	var lv = document.getElementById(id);
	for (var p in options) {
		lv[p] = options[p];
	}
}

function plv_getForm(id) {
	return document.getElementById(id + "_form");
}

function plv_getBForm(id) {
	return document.getElementById(id + "_bform");
}

function plv_submitBForm(id, an, ps, t) {
	$('#' + id).loadmask();
	var bf = plv_getBForm(id);
	if (an) {
		if (typeof(an) == 'string') {
			bf.action = an;
		}
		else if (!ps) {
			ps = an;
		}
	}
	if (ps) {
		for (n in ps) {
			$('<input type="hidden" name="' + n + '"/>').val(ps[n]).appendTo(bf);
		}
	}
	bf.target = t ? t : "";
	bf.submit();
	return false;
}

function plv_submitCheckedRows(id, an, ps, t) {
	if (plv_enableCheckedValues(id) > 0) {
		plv_submitBForm(id, an, ps, t);
	}
	return false;
}

function plv_submitCheckedKeys(id, an, ps, t) {
	if (plv_enableCheckedKeys(id) > 0) {
		plv_submitBForm(id, an, ps, t);
	}
	return false;
}

function plv_enableCheckedValues(id, ns) {
	var count = 0;
	$("#" + id + " .p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
				if (ns) {
					$(this).closest("tr").find("input.p-lv-cv").each(function() {
						var n = _plv_getPropertyName(this.name);
						if (ns == n || ns.contains(n)) {
							this.disabled = false;
						}
					});
				}
				else {
					$(this).closest("tr").find("input.p-lv-cv").prop("disabled", false);
				}
			}
			else {
				$(this).closest("tr").find("input.p-lv-cv").prop("disabled", true);
			}
		});
	return count;
}

function plv_enableCheckedKeys(id) {
	var count = 0;
	$("#" + id + " .p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
			}
			$(this).closest("tr").find("input.p-lv-ck").prop("disabled", !this.checked);
		});
	return count;
}

function plv_getRowData(tr) {
	var d = {};
	$(tr).find("input.p-lv-cv").each(function () {
		var n = _plv_getPropertyName(this.name);
		d[n] = this.value;
	});
	return d;
}

function _plv_getPropertyName(n) {
	var i = n.lastIndexOf('.');
	if (i >= 0) {
		n = n.substring(i + 1);
	}
	return n;
}

function plv_getTBodyRows(id) {
	return $("#" + id + " .p-lv-tbody > tr");
}

function plv_checkAll(id, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var $lv = $("#" + id);

	_plv_setCheckAll($lv, check, true);
}

function plv_checkRow(id, row, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var trs = $("#" + id + " .p-lv-tbody > tr");
	_plv_selectRow(trs.eq(row), check);
}

function _plv_init_filters($lv) {
	$lv.find(".p-lv-filters")
		.find('.form-group')
			.each(function() {
				if ($(this).hasClass('p-hidden')) {
					$(this).find("input,select,textarea").prop('disabled', true);
				}
			}).end()
		.find('.p-lv-fs-remove').click(_plv_onDelFilter).end()
		.find('.p-lv-fs-clear').click(_plv_onClearFilters).end()
		.find('.p-lv-fs-select').change(_plv_onAddFilter).end()
		.find('.p-lv-f-number-c, .p-lv-f-date-c, .p-lv-f-datetime-c, .p-lv-f-time-c')
			.on('change', _plv_onBetweenChange)
			.end()
		.find('form')
			.submit(function() {
				$lv.loadmask();
			});
}

function _plv_init_table($lv) {
	if ($lv.data("autosize") == 'true') {
		$lv.addClass("p-lv-autosize");
		var $lvb = $lv.children(".p-lv-body").autosize();

//		var $sth = $lv.find(".p-lv-thead");
//		var $cth = $sth.clone();
//		
//		$cth.find('tr').append('<th><div class="p-lv-cell-last"></div></th>');
//		
//		var $bht = $('<table class="p-table"></table>').css('visibility', 'hidden').append($cth);
//		
//		$sth.removeClass('p-lv-thead').addClass('p-lv-thead-shadow');
//		$sth.parent().css('margin-top', -1 - $sth.outerHeight() + "px");
//		
//		$('<div class="p-lv-body-head p-table-wrapper"></div>')
//			.append($bht)
//			.insertBefore($lvb).autosize({ 
//				overflow: 'hidden',
//				callback: function() {
//					var $cths = $cth.find('.p-lv-cell');
//					var $sths = $sth.find('.p-lv-cell');
//					$cths.each(function(i) {
//						var $sc = $sths.eq(i);
//						if (!$sc.parent().is(':hidden')) {
//							var cw = $sc.width();
//							var hw = $sc.parent().width();
//							$(this).width(cw >= hw ? cw : hw + 1);
//						}
//					});
//					$bht.css('visibility', 'visible');
//				}
//			});
//		$lvb.scroll(function() {
//			$bht.css('margin-left', -1 - $lvb.scrollLeft() + "px");
//		});
	}

	$lv.find(".p-lv-thead > tr > th").each(function() {
		var $th = $(this);
		if ($th.hasClass("p-lv-sortable")) {
			$th.click(function() {
				_plv_sort($lv.attr("id"), $(this).attr("column"));
			});
			if ($.browser.msie && $.browser.majorVersion < 7) {
				$th.mouseenter(function() {
					$(this).addClass("p-lv-hover");
					return false;
				}).mouseleave(function() {
					$(this).removeClass("p-lv-hover");
					return false;
				});
			}
		}
	});

	var icd = 0;
	var inc = 0;
	var $tb = $lv.find(".p-lv-tbody");
	$tb.click(_plv_onTBodyClick)
		.find("input.p-lv-cb")
			.each(function() {
				_plv_selectRow($(this).closest("tr"), this.checked);
				if (this.checked) {
					icd++;
				}
				else {
					inc++;
				}
			});
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$tb.mouseover(_plv_onTBodyMouseOver)
			.mouseout(_plv_onTBodyMouseOut);
	}
	
	$lv.find(".p-lv-thead input.p-lv-ca")
		.click(_plv_onAllCheck)
		.prop("checked", (icd > 0 && inc == 0));
}

function _plv_init($lv) {
	if ($lv.data("plistview")) {
		return;
	}
	$lv.data("plistview", true);
	_plv_init_table($lv);
	_plv_init_filters($lv);
}

function _plv_sort(id, cn) {
	var es = document.getElementById(id + "_sort");
	if (es) {
		$('#' + id).loadmask();

		var ed = document.getElementById(id + "_dir");
		if (cn == es.value) {
			ed.value = (ed.value.toLowerCase() == "asc" ? "desc" : "asc");
		}
		else {
			es.value = cn;
			ed.value = "asc";
		}
		document.getElementById(id + "_submit").click();
	}
}

function _plv_goto(id, p) {
	$('#' + id).loadmask();

	document.getElementById(id + "_form").reset();
	document.getElementById(id + "_start").value = p;
	var es = document.getElementById(id + "_sort");
	if (es && es.value == "") {
		es.disabled = true;
	}
	var ed = document.getElementById(id + "_dir");
	if (ed && ed.value == "") {
		ed.disabled = true;
	}
	document.getElementById(id + "_submit").click();
}

function _plv_limit(id, l) {
	$('#' + id).loadmask();

	document.getElementById(id + "_limit").value = l;
	document.getElementById(id + "_submit").click();
}

function _plv_clearFieldValue() {
	switch (this.tagName) {
	case "INPUT":
		switch (this.type) {
		case "radio":
		case "checkbox":
			this.checked = false;
			break;
		case "text":
			this.value = "";
			break;
		}
		break;
	case "SELECT":
		if (!this.name.endsWith(".c")) {
			this.selectedIndex = 0;
		}
		break;
	case "TEXTAREA":
		this.value = "";
		break;
	}
}

function _plv_selectRow(tr, c) {
	if (c) {
		tr.addClass("p-selected p-lv-selected")
		  .find("input.p-lv-cb").prop("checked", c);
	}
	else {
		tr.removeClass("p-selected p-lv-selected")
		  .find("input.p-lv-cb").prop("checked", c);
	}
}

function _plv_toggleRow($tr, ts) {
	var $lv = $tr.closest("div.p-lv");
	if ($lv.data('singleSelect') == "true") {
		if ($tr.hasClass("p-lv-selected")) {
			if (ts || !($lv.data("untoggleSelect") == "true")) {
				_plv_selectRow($tr, false);
			}
		}
		else {
			_plv_selectRow($lv.find("tr.p-lv-selected"), false);
			_plv_selectRow($tr, true);
		}
	}
	else {
		if ($tr.hasClass("p-lv-selected")) {
			_plv_selectRow($tr, false);
		}
		else {
			_plv_selectRow($tr, true);
		}
	}

	var all = true;
	$lv.find(".p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (!this.checked) {
				all = false;
				return false;
			}
		});
	_plv_setCheckAll($lv, all);
}

function _plv_onBetweenChange() {
	var $t = $(this);
	if ($t.val() == 'bt') {
		$t.nextAll().removeClass('p-hidden').find('INPUT').prop('disabled', false);
	}
	else {
		$t.nextAll().addClass('p-hidden').find('INPUT').prop('disabled', true);
	}
}

function _plv_onAddFilter() {
	var e = this;
	if (e.selectedIndex > 0) {
		$(e).closest(".p-lv-filters")
			.find('.p-lv-fsi-' + e.value)
				.removeClass('p-hidden')
				.find("input,select,textarea").prop('disabled', false).end()
				.end()
			.fieldset('expand');
		e.options[e.selectedIndex].disabled = true;
		e.selectedIndex = 0;
	}
	return false;
}

function _plv_onDelFilter() {
	var $g = $(this).closest(".form-group");
	$g.addClass('p-hidden')
		.find("input,select,textarea")
			.prop('disabled', true)
			.each(_plv_clearFieldValue)
			.end()
		.find(".p-label-error")
			.removeClass('p-label-error')
			.end()
		.find(".p-field-errors")
			.remove()
			.end()
		.closest(".p-lv-filters")
			.find('.p-lv-fs-select>option[value=' + $g.data('item') + ']')
				.prop('disabled', false);
}

function _plv_onClearFilters() {
	$(this).closest(".p-lv-fsform")
		.find(".p-field-errors")
			.remove()
			.end()
		.find(".p-label-error")
			.removeClass('p-label-error')
			.end()
		.find("input,select,textarea")
			.each(_plv_clearFieldValue);
	return false;
}

function _plv_setCheckAll($lv, check, crows) {
	$lv.find(".p-lv-thead input.p-lv-ca").each(function() {
		this.checked = check;
		this.title = $(this).data(check ? 'selectNone' : 'selectAll');
	});
	$lv.find(".p-lv-cab").each(function() {
		var $b = $(this);
		$b.prop('checked', check)
		  .find("i")
		  	.removeClass($b.data(check ? "iconSelectAll" : "iconSelectNone"))
		  	.addClass($b.data(check ? "iconSelectNone" : "iconSelectAll"))
		  	.get(0).nextSibling.data = ' ' + $b.data(check ? 'textSelectNone' : 'textSelectAll');
	});
	if (crows) {
		$lv.find(".p-lv-tbody > tr").each(function() {
			_plv_selectRow($(this), check);
		});
	}
}

function _plv_onAllCheck() {
	var c = this.checked;
	var $lv = $(this).closest(".p-lv");

	_plv_setCheckAll($lv, c, true);
}

function _plv_onAllClick(el) {
	var c = !($(el).prop('checked') || false);
	var $lv = $(el).closest(".p-lv");
	_plv_setCheckAll($lv, c, true);
}

function _plv_onTBodyClick(evt) {
	var $t = $(evt.target);
	
	if ($t.prop('tagName') == 'A') {
		return;
	}
	
	var $tr = $t.closest('tr');
	if ($t.hasClass('p-lv-cb')) {
		evt.stopPropagation();
		_plv_toggleRow($tr, true);
		return;
	}

	if ($tr.size() > 0) {
		_plv_toggleRow($tr);
		
		var $lv = $tr.closest("div.p-lv");
		var handler = $lv.get(0).onrowclick || $lv.data("onrowclick");
		panda_call(handler, $tr.get(0));
	}
}


function _plv_onTBodyMouseOver(evt) {
	$(evt.target).closest("tr.p-lv-tr").addClass("ui-state-hover p-lv-hover");
	return false;
}
function _plv_onTBodyMouseOut(evt) {
	$(evt.target).closest("tr.p-lv-tr").removeClass("ui-state-hover p-lv-hover");
	return false;
}

(function($) {
	$(window).on('load', function() {
		$('div.p-lv').each(function() {
			_plv_init($(this));
		});
	});
})(jQuery);

(function() {
	function _click(evt) {
		var $el = $(this);
		if ($el.parent().hasClass('disabled')) {
			evt.preventDefault();
		}
		else {
			var pn = $el.data("pageno");
			if (pn >= 0) {
				var $pg = $el.closest(".p-pager");
				var js = $pg.data("click");
				if (js.contains('#')) {
					evt.preventDefault();
					js = js.replace("#", (pn - 1) * $pg.data("limit"));
				}
				eval(js);
			}
		}
	}
	
	$.fn.ppager = function(api, pno) {
		return this.each(function() {
			var $p = $(this);
			if ($p.attr("ppager") != "true") {
				$p.attr("ppager", "true");
				if ($p.data("click")) {
					$p.find("a[data-pageno]").click(_click);
				}
			}
		});
	};
	
	// PAGER DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="ppager"]').ppager();
	});
})();
(function() {
	function panel_wheel(e, delta) {
		var o = this.scrollLeft;
		this.scrollLeft -= (delta * 40);
		if (o != this.scrollLeft) {
			e.preventDefault();
		}
	}

	// ==================
	$(window).on('load', function() {
		$('.p-panel-hscroll .panel-body').each(function() {
			var $t = $(this);
			if ($t.data('pwheelpanel')) {
				return;
			}
			$t.data('pwheelpanel', true).mousewheel(panel_wheel);
		});
	});
})();
//------------------------------------------------------
function s_onpageload() {
	//invoke onPageLoad function
	if (window.onPageLoad) {
		window.onPageLoad();
		window.onPageLoad = null;
	}

	for (var i = 1; ; i++) {
		var f = 'onPageLoad' + i;
		if (window[f]) {
			window[f]();
			window[f] = null;
		}
		else {
			break;
		}
	}
}

function s_totop() {
	$('.p-totop').click(function() {
		$('html,body').animate({ scrollTop: 0 }, 'slow');
	});
}

function s_preload() {
	$('body').append(
		'<div id="preload" class="p-dispear">'
			+ '<div class="ui-loadmask"></div>'
			+ '<div class="p-loader-large-snake"></div>'
		+ '</div>');
}

function s_submit_form() {
	var form = this;
	var $f = $(form);
	var $c = $f.closest('.p-popup, .p-inner');
	var lm = ($f.height() > 20 && $f.attr('loadmask') != 'false');
	if ($c.length > 0) {
		setTimeout(function() {
			var data = $f.serializeArray();
			if ($c.hasClass('p-inner')) {
				data.push({ name: '__inner', value: 'true' });
			}
			else {
				data.push({ name: '__popup', value: 'true' });
			}
			if (lm) {
				$c.parent().loadmask();
			}
			$.ajax({
				url: form.action,
				data: data,
				dataType: 'html',
				success: function(html, ts, xhr) {
					$c.parent().html(html);
				},
				error: function(xhr, ts, err) {
					$c.parent().html(xhr.responseText);
				},
				complete: function(xhr, ts) {
					$c.parent().unloadmask();
				}
			});
		}, 10);
		return false;
	}
	else {
		if (lm) {
			$f.loadmask();
		}
		return true;
	}
}

function s_hook_forms($w) {
	$w.find('form[hooked!=true]').each(function() {
		var $t = $(this);
		$t.attr('hooked', 'true');
		if (this.target == '' || this.target == '_self'
			|| this.target == '_top' || this.target == '_parent') {
			$t.submit(s_submit_form);
		}
	});
}

function s_ie6_submit_onclick() {
	var t = this;
	$(this).closest('form').find('input[type=submit],button').each(function() {
		if (this != t) {
			this.disabled = true;
		}
	});
};

function s_ie6_hack_forms($w) {
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$w.find('form[hacked!=true]')
			.find('button[type=submit]').each(function() {
				if (!this.onclick) {
					$(this).click(s_ie6_submit_onclick);
				}
			}).end()
			.attr('hacked', 'true');
	}
}

//------------------------------------------------------
function sl_sort(id, el) {
	var co = el.value.split(' ');
	if (co.length == 2) {
		$('#' + id + '_s_c').val(co[0]);
		$('#' + id + '_s_d').val(co[1]);
		// backward
		$('#' + id + '_so_c').val(co[0]);
		$('#' + id + '_so_d').val(co[1]);
		sl_submit(id);
	}
}
function sl_sorta(id, name, dir) {
	sl_sortn(id, name, dir.toLowerCase() == "asc" ? "desc" : "asc");
}
function sl_sortn(id, name, dir) {
	if (id == '') {
		s_loadmask();
		location.href = s_setQueryParam({ 's.c': name, 's.d': dir });
	}
	else {
		$('#' + id + '_s_c').val(name);
		$('#' + id + '_s_d').val(dir);
		// backward
		$('#' + id + '_so_c').val(name);
		$('#' + id + '_so_d').val(dir);

		sl_submit(id);
	}
}
function sl_goto(id, s) {
	if (id == '') {
		s_loadmask();
		location.href = s_setQueryParam({ 'p.s': s });
	}
	else {
		$('#' + id + '_p_s').val(s);
		// backward
		$('#' + id + '_pg_s').val(s);

		sl_submit(id);
	}
}
function sl_limit(id, el) {
	if (id == '') {
		s_loadmask();
		location.href = s_setQueryParam({ 'p.l': el.value });
	}
	else {
		$('#' + id + '_p_l').val(el.value);
		// backward
		$('#' + id + '_pg_l').val(el.value);

		sl_submit(id);
	}
}
function sl_submit(id) {
	var $f = $('#' + id);
	var $i = $f.closest('.p-inner');
	if ($i.size() > 0) {
		var d = $f.serializeArray();
		d[d.length] = { name: '__inner', value: 'true' };
		
		var $p = $i.parent();
		$p.loadmask({ cssClass: 'p-loader-large-snake' });
		$p.load($f.attr('action'), d, function() {
				$p.unloadmask();
			});
	}
	else {
		s_loadmask();
		$f.submit();
	}
}

function s_loadmask() {
	$('body').loadmask({
		cssClass: 'p-loader-large-snake',
		mask: false,
		window: true
	});
}
function s_resize() {
	$(window).trigger('resize');
}

function s_getLinkMark() {
	var i = location.href.lastIndexOf('#');
	if (i > 0) {
		return location.href.substring(i);
	}
	else {
		return "";
	}
}

function s_setTitle(title) {
	var d = document.title.indexOf(' - ');
	if (d < 0) {
		d = document.title.indexOf(' | ');
	}
	if (d < 0) {
		document.title = title;
	}
	else {
		document.title = title + document.title.substring(d);
	}
}

function s_setQueryParam(vs) {
	var ps;
	var u = location.href, i = u.indexOf('?');
	if (i >= 0) {
		ps = $.extend(u.queryParams(), vs);
		u = u.substring(0, i);
	}
	else {
		ps = vs;
	}
	var qs = $.param(ps);
	return u + '?' + qs;
}

function s_setLang(v) {
	location.href = s_setQueryParam({ '__locale': v });
}

function s_addScript(url) {
	$.jscript(url);
}

//------------------------------------------------------
// google analytics
var _gaq = [];
function s_google_analytics(c) {
	if (c.google_analytics) {
		_gaq.push(['_setAccount', c.google_analytics]);
		if (c.google_analytics_domain) {
			_gaq.push(['_setDomainName', c.google_analytics_domain]);
		}
		_gaq.push(['_trackPageview']);
		
		s_addScript(('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js');
	}
}


//------------------------------------------------------
// facebook comments
function s_facebook_comments(i, h, w) {
	h = h || document.location;
	$(i).attr({
		"data-href": h || document.location,
		"data-num-posts": '2',
		"data-width": w || '500'
	});
	s_addScript("http://connect.facebook.net/ja_JP/all.js#xfbml=1");
}

//------------------------------------------------------
// google plus one
function s_google_plusone() {
	s_addScript("https://apis.google.com/js/plusone.js");
}

//------------------------------------------------------
// site vars
var site = {
	statics: '/static'
};

function s_setbase(c) {
	c = $.extend(site, c);
	$.cookie.defaults = c.cookie || {};
	return site;
}

//------------------------------------------------------
//clipboard
function s_copyToClipboard(s) {
	try {
		// ie
		clipboardData.setData('Text', s);
	}
	catch (e) {
		var swf = document.createElement('embed');
		swf.src = site.statics + "/panda/swf/clipboard.swf";
		swf.setAttribute('FlashVars','code=' + encodeURIComponent(s));
		swf.type = 'application/x-shockwave-flash';
		swf.width = '0';
		swf.height = '0';
		$('body').append(swf);
	}
}

//------------------------------------------------------
function s_decorate(selector) {
	$(selector).each(function() {
		var $w = $(this);
		s_hook_forms($w);
		//$w.find('textarea[autosize="true"]').autosize();
	});
	
	$(window).trigger('load');
}

function s_init(c) {
	var m = { body: 'body' };
	$('meta').each(function() {
		var $t = $(this);
		var a = $t.attr('property');
		if (a && a.startsWith('s:')) {
			var v = $t.attr('content');
			m[a.substring(2)] = v;
		}
	});

	c = $.extend(m, c);
	c = s_setbase(c);

	// document - onload
	$(function() {
		s_onpageload();
		
		s_preload();
		
		s_totop();
		
		var $w = $(c.body);
		s_hook_forms($w);
		s_ie6_hack_forms($w);

		// google analytics
		s_google_analytics(c);
	});
}

// set default
s_setbase({
	base: '',
	cookie: { expires: 180 }
});

(function() {
	$.fn.ptrigger = function(option) {
		option = $.extend({ 'icon' : 'fa fa-remove' }, option);
		return this.each(function() {
			var $t = $(this);
			if ($t.data('ptriggerHooked')) {
				return;
			}
			$t.data('ptriggerHooked', true);
			var f = option.onclick || $t.data('ptrigger');
			if (!f || f == 'false') {
				return;
			}

			var i = option.icon || $t.data('ptrigger-icon');
			var $i = $('<i class="p-trigger ' + i + '"></i>');
			$t.addClass('p-has-trigger');
			$i.insertAfter($t)
			  .click(function() {
					if (f && f != "true" && f != true) {
						panda_call(f, $t.get(0));
					}
					else {
						$t.val('');
					}
			});
		});
	};
	
	// DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-ptrigger]').ptrigger();
	});
})();
(function($) {
	var puploader = function($u) {
		if ($u.data('puploader')) {
			return;
		}
		$u.data('puploader', true);
		
		var loading = false;
		
		var pul = $u.data('uploadLink');
		var pun = $u.data('uploadName');
		var pud = JSON.sparse($u.data('uploadData'));
		var pdl = $u.data('dnloadLink');
		var pdn = $u.data('dnloadName');
		var pdd = JSON.sparse($u.data('dnloadData'));
		var pel = $u.data('defaultLink');
		var pet = $u.data('defaultText');
		
		var $uid = $u.children('.p-uploader-fid');
		var $uf = $u.children('.p-uploader-file');
		var $ub = $u.children('.p-uploader-btn');
		var $ut = $u.children('.p-uploader-text');
		var $ui = $u.children('.p-uploader-image');

		var $up = $('<div class="p-uploader-progress progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertBefore($ut);

		var $ue = $('<div class="p-uploader-error"></div>');
		$ue.insertAfter($ut);
		
		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _info(uid, ufn, ufs, uct) {
			$uid.val(uid || '');
			
			ufn = ufn || uid || $uf.val();
			var durl = null;
			if (pdl && uid) {
				var ps = $.extend({}, pdd);
				ps[pdn] = uid;
				durl = pdl + '?' + $.param(ps);
			}
			
			if (ufn) {
				var s = '<i class="fa fa-check p-uploader-icon"></i> ' + ufn + ' ' + _filesize(ufs)
				if (durl) {
					$ut.html('<a href="' + durl + '">' + s + '</a>');
				}
				else {
					$ut.html('<span>' + s + '</span>');
				}
				$ut.show();
			}
			else if (pel) {
				$ut.html('<a href="' + pel + '">'
						+ '<i class="fa '
						+ (String.startsWith(uct, 'image/') ? 'fa-picture-o' : 'fa-paperclip')
						+ ' p-uploader-icon"></i> '
						+ String.defaults(pet)
						+ '</a>')
					.show();
			}
			
			$('<i class="p-uploader-remove fa fa-remove"></i>').click(_clear).appendTo($ut);

			if (String.startsWith(uct, 'image/')) {
				if (durl) {
					$ui.html('<img class="img-thumbnail" src="' + durl + '"></img>').fadeIn();
				}
				else if (pel) {
					$ui.html('<img class="img-thumbnail" src="' + pel + '"></img>').fadeIn();
				}
			}
		}

		function _progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _clear() {
			$uid.val('');
			$uf.val('');
			$ut.html("&nbsp;");
			$ui.empty();
			$ue.hide().empty();
		}
		
		function _upload() {
			if (loading || $uf.val() == "") {
				return;
			}
			loading = true;

			$uid.val('');
			$ub.length ? $ub.hide() : $uf.hide();
			$ui.hide().empty();
			$ut.hide().html("&nbsp;");
			$ue.hide().empty();

			$up.show();

			var progress = 0;
			
			_progress(progress);

			var timer = setInterval(function() {
				_progress(progress++);
				if (progress >= 90) {
					if (timer) {
						clearInterval(timer);
						timer = null;
					}
				}
			}, 20);

			function _endUpload() {
				_progress(100);
				$up.hide();
				$uf.val("");
				$ub.length ? $ub.show() : $uf.show();
				loading = false;
			}

			var file = {}; file[pun] = $uf; 
			$.ajaf({
				url: pul,
				data: pud,
				file: file,
				dataType: 'json',
				success: function(d) {
					_endUpload();
					if (d.success) {
						var r = d.result;
						_info(r.id, r.name, r.size, r.contentType);
					}
					else {
						if (d.alerts) {
							$ue.palert('add', d.alerts);
						}
						if (d.exception) {
							var e = d.exception;
							$ue.palert('error', (e.message + (e.stackTrace ? ("\n" + e.stackTrace) : "")).escapePhtml());
						}
						$ue.slideDown();
					}
				},
				error: function(xhr, status, e) {
					_endUpload();
					$ue.palert('error', (e ? (e + "").escapePhtml() : (xhr ? xhr.responseText : status)));
					$ue.slideDown();
				}
			});
		}

		$uf.change(function() { 
			setTimeout(_upload, 10);
		});
		
		$ub.click(function(e) {
			e.preventDefault();
			$uf.trigger('click');
			return false;
		});
	};

	// UPLOADER FUNCTION
	// ==================
	$.fn.puploader = function(c) {
		return this.each(function() {
			puploader($(this));
		});
	};
	
	// UPLOADER DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="puploader"]').puploader();
	});
})(jQuery);
if (typeof(panda) == "undefined") { panda = {}; }

panda.viewfield = function(o) {
	var api = {
		el: $(o),
		val: function(v) {
			if (typeof(v) == 'undefined') {
				return this.el.val();
			}
			else {
				this.el.val(v).next().text(v == '' ? '\u3000' : v);
				return this;
			}
		}
	};
	
	return api;
};
