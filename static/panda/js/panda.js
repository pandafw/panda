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
			if (fn.call(scope, this[i]) === false) {
				break;
			}
		}
	}
}

if (typeof Array.prototype.find != 'function') {
	Array.prototype.find = function(fn, scope) {
		scope = scope || window;
		for (var i = 0; i < this.length; i++) {
			if (fn.call(scope, this[i])) {
				return this[i];
			}
		}
		return null;
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

if (typeof Function.prototype.createCallback != "function") {
	/**
	 * Creates a callback that passes arguments[0], arguments[1], arguments[2], ...
	 * Call directly on any function. Example: <code>myFunction.createCallback(arg1, arg2)</code>
	 * Will create a function that is bound to those 2 args. <b>If a specific scope is required in the
	 * callback, use {@link #createDelegate} instead.</b> The function returned by createCallback always
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
			handler: sayHi.createCallback('Fred')
		});
		</code></pre>
	 * @return {Function} The new function
	 */
	Function.prototype.createCallback = function(/*args...*/){
		// make args available, in function below
		var args = arguments;
		var method = this;
		return function() {
			return method.apply(window, args);
		};
	};
}

if (typeof Function.prototype.createDelegate != "function") {
	/**
	 * Creates a delegate (callback) that sets the scope to obj.
	 * Call directly on any function. Example: <code>this.myFunction.createDelegate(this, [arg1, arg2])</code>
	 * Will create a function that is automatically scoped to obj so that the <tt>this</tt> variable inside the
	 * callback points to obj. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			// Note this use of "this.text" here.  This function expects to
			// execute within a scope that contains a text property.  In this
			// example, the "this" variable is pointing to the btn object that
			// was passed in createDelegate below.
			alert('Hi, ' + name + '. You clicked the "' + this.text + '" button.');
		}

		var btn = new Ext.Button({
			text: 'Say Hi',
			renderTo: Ext.getBody()
		});

		// This callback will execute in the scope of the
		// button instance. Clicking the button alerts
		// "Hi, Fred. You clicked the "Say Hi" button."
		btn.on('click', sayHi.createDelegate(btn, ['Fred']));
		</code></pre>
	 * @param {Object} obj (optional) The object for which the scope is set
	 * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
	 * @param {Boolean/Number} appendArgs (optional) if True args are appended to call args instead of overriding,
	 *											   if a number the args are inserted at the specified position
	 * @return {Function} The new function
	 */
	Function.prototype.createDelegate = function(obj, args, appendArgs) {
		var method = this;
		return function() {
			var callArgs = args || arguments;
			if(appendArgs === true){
				callArgs = Array.prototype.slice.call(arguments, 0);
				callArgs = callArgs.concat(args);
			}else if(typeof appendArgs == "number"){
				callArgs = Array.prototype.slice.call(arguments, 0); // copy arguments first
				var applyArgs = [appendArgs, 0].concat(args); // create method call params
				Array.prototype.splice.apply(callArgs, applyArgs); // splice them in
			}
			return method.apply(obj || window, callArgs);
		};
	};
}

if (typeof Function.prototype.defer != "function") {
	/**
	 * Calls this function after the number of millseconds specified, optionally in a specific scope. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		// executes immediately:
		sayHi('Fred');

		// executes after 2 seconds:
		sayHi.defer(2000, this, ['Fred']);

		// this syntax is sometimes useful for deferring
		// execution of an anonymous function:
		(function(){
			alert('Anonymous');
		}).defer(100);
		</code></pre>
	 * @param {Number} millis The number of milliseconds for the setTimeout call (if 0 the function is executed immediately)
	 * @param {Object} obj (optional) The object for which the scope is set
	 * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
	 * @param {Boolean/Number} appendArgs (optional) if True args are appended to call args instead of overriding,
	 *											   if a number the args are inserted at the specified position
	 * @return {Number} The timeout id that can be used with clearTimeout
	 */
	Function.prototype.defer = function(millis, obj, args, appendArgs) {
		var fn = this.createDelegate(obj, args, appendArgs);
		if(millis){
			return setTimeout(fn, millis);
		}
		fn();
		return 0;
	};
}

if (typeof Function.prototype.createSequence != "function") {
	/**
	 * Create a combined function call sequence of the original function + the passed function.
	 * The resulting function returns the results of the original function.
	 * The passed fcn is called with the parameters of the original function. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		sayHi('Fred'); // alerts "Hi, Fred"

		var sayGoodbye = sayHi.createSequence(function(name){
			alert('Bye, ' + name);
		});

		sayGoodbye('Fred'); // both alerts show
		</code></pre>
	 * @param {Function} fcn The function to sequence
	 * @param {Object} scope (optional) The scope of the passed fcn (Defaults to scope of original function or window)
	 * @return {Function} The new function
	 */
	Function.prototype.createSequence = function(fcn, scope) {
		if(typeof fcn != "function"){
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

if (typeof Function.prototype.createInterceptor != "function") {
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
		var sayHiToFriend = sayHi.createInterceptor(function(name){
			return name == 'Brian';
		});

		sayHiToFriend('Fred');	// no alert
		sayHiToFriend('Brian'); // alerts "Hi, Brian"
		</code></pre>
	 * @param {Function} fcn The function to call before the original
	 * @param {Object} scope (optional) The scope of the passed fcn (Defaults to scope of original function or window)
	 * @return {Function} The new function
	 */
	Function.prototype.createInterceptor = function(fcn, scope) {
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
		return this.escapeHtml().replace(/\n/g, "<br/>");
	};
}
if (typeof String.prototype.unescapePhtml != "function") {
	String.prototype.unescapePhtml = function() {
		return this.replace(/<br\/>/g, "\n").unescapeHtml();
	};
}
if (typeof String.prototype.formatXml != "function") {
	String.prototype.formatXml = function() {
		var fmt = '';
		var reg = /(>)(<)(\/*)/g;
		var xml = this.replace(reg, '$1\r\n$2$3');
		var pad = 0;
		$.each(xml.split('\r\n'), function(index, node) {
			var indent = 0;
			if (node.match( /.+<\/\w[^>]*>$/ )) {
				indent = 0;
			}
			else if (node.match( /^<\/\w/ )) {
				if (pad != 0) {
					pad -= 1;
				}
			}
			else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
				indent = 1;
			}
			else {
				indent = 0;
			}
	
			fmt += ("").leftPad(' ', pad * 2) + node + '\r\n';
			pad += indent;
		});
	
		return fmt;
	}
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

if (typeof String.prototype.queryParams != "function") {
	String.prototype.queryParams = function(f) {
		var qs = [], s = this, i = s.indexOf('?');
		if (i >= 0) {
			s = s.substring(i + 1);
		}
		var ss = s.split('&');
		for (i = 0; i < ss.length; i++) {
			var pv = ss[i].split('=');
			var n = decodeURIComponent(pv[0]);
			if (f != n) {
				qs.push({
					name: n,
					value: pv.length > 1 ? decodeURIComponent(pv[1]) : ''
				});
			}
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
	String.rightPad = function(val, ch, size) {
		return s != null ? String(s).rightPad(ch, size) : "".rightPad(ch, size);
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
if (typeof String.formatXml != "function") {
	String.formatXml = function(s) {
		return s != null ? String(s).formatXml() : "";
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
	String.formatSize = function(n) {
		var sz = "";
		if (n > 1024 * 1024 * 1024) {
			sz = Math.round(n / 1024 / 1024 / 1024) + 'GB';
		}
		if (n > 1024 * 1024) {
			sz = Math.round(n / 1024 / 1024) + 'MB';
		}
		else if (n > 1024) {
			sz = Math.round(n / 1024) + 'KB';
		}
		else if (n != '') {
			sz = n + 'B';
		}
		return sz;
	};
}

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
	$.datepicker._triggerClass = 'n-icon n-icon-date_picker ui-datepicker-trigger';
	$.datetimepicker.defaults.triggerClass = 'n-icon n-icon-datetime_picker ui-datetimepicker-trigger';
	$.timepicker.defaults.triggerClass = 'n-icon n-icon-time_picker ui-timepicker-trigger';
	
	$.trim = function(text) { return text == null ? "" : text.strip(); };
})(jQuery);
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
if (typeof(pw) == "undefined") { pw = {}; }

function nlv_options(id, options) {
	var lv = document.getElementById(id);
	for (var p in options) {
		lv[p] = options[p];
	}
}

function nlv_getForm(id) {
	return document.getElementById(id + "_form");
}

function nlv_getBForm(id) {
	return document.getElementById(id + "_bform");
}

function nlv_submitBForm(id, an, ps) {
	$('#' + id).loadmask();
	var bf = nlv_getBForm(id);
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
	bf.submit();
	return false;
}

function nlv_submitCheckedRows(id, an, ps) {
	if (nlv_enableCheckedValues(id) > 0) {
		nlv_submitBForm(id, an, ps);
	}
	return false;
}

function nlv_submitCheckedKeys(id, an, ps) {
	if (nlv_enableCheckedKeys(id) > 0) {
		nlv_submitBForm(id, an, ps);
	}
	return false;
}

function nlv_enableCheckedValues(id, ns) {
	var count = 0;
	$("#" + id + " .n-lv-tbody input.n-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
				if (ns) {
					$(this).closest("tr").find("input.n-lv-cv").each(function() {
						var n = _nlv_getPropertyName(this.name);
						if (ns == n || ns.contains(n)) {
							this.disabled = false;
						}
					});
				}
				else {
					$(this).closest("tr").find("input.n-lv-cv").prop("disabled", false);
				}
			}
			else {
				$(this).closest("tr").find("input.n-lv-cv").prop("disabled", true);
			}
		});
	return count;
}

function nlv_enableCheckedKeys(id) {
	var count = 0;
	$("#" + id + " .n-lv-tbody input.n-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
			}
			$(this).closest("tr").find("input.n-lv-ck").prop("disabled", !this.checked);
		});
	return count;
}

function nlv_getRowData(tr) {
	var d = {};
	$(tr).find("input.n-lv-cv").each(function () {
		var n = _nlv_getPropertyName(this.name);
		d[n] = this.value;
	});
	return d;
}

function _nlv_getPropertyName(n) {
	var i = n.lastIndexOf('.');
	if (i >= 0) {
		n = n.substring(i + 1);
	}
	return n;
}

function nlv_getTBodyRows(id) {
	return $("#" + id + " .n-lv-tbody > tr");
}

function nlv_checkAll(id, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var $lv = $("#" + id);

	_nlv_setCheckAll($lv, check, true);
}

function nlv_checkRow(id, row, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var trs = $("#" + id + " .n-lv-tbody > tr");
	_nlv_selectRow(trs.eq(row), check);
}

function _nlv_init_filters(id, collapse) {
	$("#" + id).find(".n-lv-filters").fieldset({
		collapse: collapse
	}).find('.n-tr-input').each(function() {
		if ($(this).hasClass('n-hidden')) {
			$(this).find("input,select,textarea").prop('disabled', true);
		}
	}).end().find('form').submit(function() {
		$('#' + id).loadmask();
	});
}

function _nlv_init_table(id, cfg) {
	var $lv = $("#" + id);

	if (cfg.autosize && !($.browser.ios || $.browser.android)) {
		$lv.addClass("n-lv-autosize");
		var $lvb = $lv.children(".n-lv-body").autosize();

		var $sth = $lv.find(".n-lv-thead");
		var $cth = $sth.clone();
		
		$cth.find('tr').append('<th><div class="n-lv-cell-last"></div></th>');
		
		var $bht = $('<table class="n-table"></table>').css('visibility', 'hidden').append($cth);
		
		$sth.removeClass('n-lv-thead').addClass('n-lv-thead-shadow');
		$sth.parent().css('margin-top', -1 - $sth.outerHeight() + "px");
		
		$('<div class="n-lv-body-head n-table-wrapper"></div>')
			.append($bht)
			.insertBefore($lvb).autosize({ 
				overflow: 'hidden',
				callback: function() {
					var $cths = $cth.find('.n-lv-cell');
					var $sths = $sth.find('.n-lv-cell');
					$cths.each(function(i) {
						var $sc = $sths.eq(i);
						if (!$sc.parent().is(':hidden')) {
							var cw = $sc.width();
							var hw = $sc.parent().width();
							$(this).width(cw >= hw ? cw : hw + 1);
						}
					});
					$bht.css('visibility', 'visible');
				}
			});
		$lvb.scroll(function() {
			$bht.css('margin-left', -1 - $lvb.scrollLeft() + "px");
		});
	}

	$lv.find(".n-lv-thead > tr > th").each(function() {
		var $th = $(this);
		if ($th.hasClass("n-lv-sortable")) {
			$th.click(function() {
				_nlv_onThClick.call(this, id);
			  });
			if ($.browser.msie && $.browser.majorVersion < 7) {
				$th.mouseenter(_nlv_onThMouseEnter).mouseleave(_nlv_onThMouseLeave);
			}
		}
	});

	var icd = 0;
	var inc = 0;
	var $tb = $lv.find(".n-lv-tbody");
	$tb.click(_nlv_onTBodyClick)
		.find("input.n-lv-cb")
			.each(function() {
				_nlv_selectRow($(this).closest("tr"), this.checked);
				if (this.checked) {
					icd++;
				}
				else {
					inc++;
				}
			});
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$tb.mouseover(_nlv_onTBodyMouseOver)
			.mouseout(_nlv_onTBodyMouseOut);
	}
	
	$lv.find(".n-lv-thead input.n-lv-ca")
		.click(_nlv_onAllCheck)
		.prop("checked", (icd > 0 && inc == 0));
}

function _nlv_sort(id, cn) {
	$('#' + id).loadmask();

	var es = document.getElementById(id + "_sort");
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

function _nlv_goto(id, p) {
	$('#' + id).loadmask();

	document.getElementById(id + "_form").reset();
	document.getElementById(id + "_start").value = p;
	document.getElementById(id + "_submit").click();
}

function _nlv_limit(id, l) {
	$('#' + id).loadmask();

	document.getElementById(id + "_limit").value = l;
	document.getElementById(id + "_submit").click();
}

function _nlv_clearFieldValue() {
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

function _nlv_selectRow(tr, c) {
	if (c) {
		tr.addClass("n-selected n-lv-selected")
		  .find("input.n-lv-cb").prop("checked", c);
	}
	else {
		tr.removeClass("n-selected n-lv-selected")
		  .find("input.n-lv-cb").prop("checked", c);
	}
}

function _nlv_toggleRow($tr, ts) {
	var $lv = $tr.closest("div.n-lv");
	if ($lv.get(0).singleSelect || $lv.attr("singleSelect") == "true") {
		if ($tr.hasClass("n-lv-selected")) {
			if (ts || !($lv.get(0).toggleSelect === false || $lv.attr("toggleSelect") === "false")) {
				_nlv_selectRow($tr, false);
			}
		}
		else {
			_nlv_selectRow($lv.find("tr.n-lv-selected"), false);
			_nlv_selectRow($tr, true);
		}
	}
	else {
		if ($tr.hasClass("n-lv-selected")) {
			_nlv_selectRow($tr, false);
		}
		else {
			_nlv_selectRow($tr, true);
		}
	}

	var all = true;
	$lv.find(".n-lv-tbody input.n-lv-cb")
		.each(function() {
			if (!this.checked) {
				all = false;
				return false;
			}
		});
	_nlv_setCheckAll($lv, all);
}

function _nlv_onBetweenChange(el) {
	var $t = $(el),
		d = $t.val() != 'bt',
		v = d ? 'hidden' : 'visible';
	if (d) {
		$t.nextAll('INPUT').val('');
	}
	$t.nextAll().css('visibility', v);
}

function _nlv_onAddFilter(e) {
	if (e.selectedIndex > 0) {
		$(e).closest(".n-lv-filters")
			.find('.n-lv-fsi-' + e.value)
				.removeClass('n-hidden')
				.find("input,select,textarea").prop('disabled', false).end()
				.end()
			.fieldset('expand');
		e.options[e.selectedIndex].disabled = true;
		e.selectedIndex = 0;
	}
	return false;
}

function _nlv_onDelFilter(e, n) {
	$(e).closest(".n-lv-filters")
		.find('.n-lv-fsi-' + n)
			.addClass('n-hidden')
			.find("input,select,textarea")
				.prop('disabled', true)
				.each(_nlv_clearFieldValue)
				.end()
			.find(".n-label-error")
				.removeClass('n-label-error')
				.end()
			.find(".n-field-errors")
				.remove()
				.end()
			.end()
		.find('.n-lv-fs-select>option[value=' + n + ']')
			.prop('disabled', false);
}

function _nlv_onClearFilters(e) {
	$(e).closest(".n-lv-filters-t")
		.find(".n-field-errors")
			.remove()
			.end()
		.find(".n-label-error")
			.removeClass('n-label-error')
			.end()
		.find("input,select,textarea")
			.each(_nlv_clearFieldValue);
	return false;
}

function _nlv_onThClick(id) {
	var cn = $(this).attr("column");
	_nlv_sort(id, cn);
}

function _nlv_setCheckAll($lv, check, crows) {
	$lv.find(".n-lv-thead input.n-lv-ca").each(function() {
		this.checked = check;
		this.title = $(this).attr(check ? 'selectNone' : 'selectAll');
	});
	$lv.find(".n-lv-cab").each(function() {
		var $b = $(this);
		$b.prop('checked', check)
		  .find(".n-button-icon")
		  	.removeClass($b.attr(check ? "iconSelectAll" : "iconSelectNone"))
		  	.addClass($b.attr(check ? "iconSelectNone" : "iconSelectAll"))
		  .end()
		  .find(".n-button-text")
		  	.text($b.attr(check ? 'textSelectNone' : 'textSelectAll'));
	});
	if (crows) {
		$lv.find(".n-lv-tbody > tr").each(function() {
			_nlv_selectRow($(this), check);
		});
	}
}

function _nlv_onAllCheck() {
	var c = this.checked;
	var $lv = $(this).closest(".n-lv");

	_nlv_setCheckAll($lv, c, true);
}

function _nlv_onAllClick(el) {
	var c = !($(el).prop('checked') || false);
	var $lv = $(el).closest(".n-lv");
	_nlv_setCheckAll($lv, c, true);
}

function _nlv_onTBodyClick(evt) {
	var $t = $(evt.target);
	
	var $tr = $t.closest('tr');
	if ($t.hasClass('n-lv-cb')) {
		evt.stopPropagation();
		_nlv_toggleRow($tr, true);
		return;
	}

	if ($tr.size() > 0) {
		_nlv_toggleRow($tr);
		
		var $lv = $tr.closest("div.n-lv");
		var handler = $lv.get(0).onrowclick || $lv.attr("onrowclick");
		switch (typeof(handler)) {
		case "function":
			handler.call($tr.get(0));
			break;
		case "string":
			handler = new Function(handler);
			handler.call($tr.get(0));
			break;
		}
	}
}

function _nlv_onThMouseEnter() {
	$(this).addClass("ui-state-hover n-lv-hover");
	return false;
}
function _nlv_onThMouseLeave() {
	$(this).removeClass("ui-state-hover n-lv-hover");
	return false;
}

function _nlv_onTBodyMouseOver(evt) {
	$(evt.target).closest("tr.n-lv-tr").addClass("ui-state-hover n-lv-hover");
	return false;
}
function _nlv_onTBodyMouseOut(evt) {
	$(evt.target).closest("tr.n-lv-tr").removeClass("ui-state-hover n-lv-hover");
	return false;
}
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
if (typeof(pw) == "undefined") { pw = {}; }

(function() {
	function setContentType($p, t) {
		if (t == 'error') {
			if (!$p.hasClass('error')) {
				$p.addClass('error').removeClass('warn help info');
			}
		}
		else if (t == 'warn') {
			if (!$p.hasClass('error') && !$p.hasClass('warn')) {
				$p.addClass('warn').removeClass('help info');
			}
		}
		else if (t == 'confirm') {
			if (!$p.hasClass('error') && !$p.hasClass('warn') && !$p.hasClass('help')) {
				$p.addClass('help').removeClass('info');
			}
		}
		else {
			if (!$p.hasClass('error') && !$p.hasClass('warn') && !$p.hasClass('help') && !$p.hasClass('info')) {
				$p.addClass('info');
			}
		}
	}
	
	function addMsg($p, s, m, t) {
		var c = s.baseCls + '-' + t;
		var ic = s.icons[t];
		$p.append('<ul class="' + c + 's"><li class="' + c + '"><i class="' + ic + '"></i>' + m + '</li></ul>');
		setContentType($p, t);
	}

	function addMsgs($p, s, m, t) {
		if (m) {
			var c = s.baseCls + '-' + t;
			var ic = s.icons[t] + ' ' + c;
			var h = '<ul class="' + c + 's">';
			if ($.isArray(m)) {
				for (var i = 0; i < m.length; i++) {
					h += '<li class="' + c + '"><i class="' + ic + '"></i>' + m[i] + '</li>';
				}
			}
			else {
				for (var n in m) {
					var v = m[n];
					for (var i = 0; i < v.length; i++) {
						h += '<li class="' + c + '"><i class="' + ic + '"></i>' + v[i] + '</li>';
					}
				}
			}
			h += '</ul>';
			$p.append(h);
			setContentType($p, t);
		}
	}
	
	pw.notice = function(s) {
		if (typeof(s) == 'string') {
			s = { container: s };
		}
		s = $.extend({}, pw.notice.defaults, s);
		return {
			clear: function() {
				$(s.container).children('.' + s.baseCls + '-notice').remove();
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
				var $c = $(s.container);
				var $p = $c.children('.' + s.baseCls + '-notice');
				var a = false;
				if ($p.size() < 1) {
					$p = $('<div></div>').addClass(s.baseCls + '-notice').css('display', 'none');
					$c.prepend($p);
					a = true;
				}
				
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
					addMsgs($p, s, m.actionErrors, "error");
					addMsgs($p, s, m.fieldErrors, "error");
					addMsgs($p, s, m.actionWarnings, "warn");
					addMsgs($p, s, m.actionConfirms, "help");
					addMsgs($p, s, m.actionMessages, "info");
				}
				
				if (a) { 
					$p.slideDown();
				}
				return this;
			}
		}
	};
	
	pw.notice.defaults = {
		baseCls: 'n-action',
		container: 'body',
		icons: {
			'help': 'icon-question-sign',
			'info': 'icon-info',
			'error': 'icon-exclamation-sign',
			'warn': 'icon-warning-sign'
		}
	};
})();
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
if (typeof(pw) == "undefined") { pw = {}; }

(function() {
	function _click(evt) {
		var $el = $(this);
		if (!$el.hasClass('n-p-disabled')) {
			var pn = $el.attr("pageno");
			if (pn) {
				var $pg = $el.closest("div.n-p");
				var cmd = $pg.attr("click").replace("#", (pn - 1) * $pg.attr("limit"));
				eval(cmd);
			}
		}
	}
	
	pw.pager = function(o) {
		var $p = $(o);
		if ($p.attr("pager") != "true") {
			$p.attr("pager", "true");
			var $pn = $p.find(".n-p-pageno, .n-p-prev, .n-p-next, .n-p-first, .n-p-last, .n-p-active");

			if ($p.attr("click")) {
				$pn.click(_click);
			}
		}
		
		this.$el = $p;
		
		this.has = function(n) {
			return this.$el.find("[pageno=" + n + "]").size() > 0;
		};
		
		this.val = function(n) {
			this.$el.find(".n-p-active")
				.removeClass("n-p-active")
				.addClass("n-p-pageno")
			 	.click(_click);
			this.$el.find(".n-p-pageno[pageno=" + n + "]")
				.unbind()
				.removeClass("n-p-pageno")
				.addClass("n-p-active");
			
			if (n <= 1) {
				this.$el.find(".n-p-prev")
					.children().attr("pageno", n - 1)
					.end().parent().hide();
			}
			else {
				this.$el.find(".n-p-prev")
					.children().attr("pageno", n - 1)
					.end().parent().show();
			}
			
			if (n >= this.$el.attr("total")) {
				this.$el.find(".n-p-next")
					.children().attr("pageno", n + 1)
					.end().parent().hide();
			}
			else {
				this.$el.find(".n-p-next")
					.children().attr("pageno", n + 1)
					.end().parent().show();
			}
			return this;
		};
		
		return this;
	};
})();
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
if (typeof(pw) == "undefined") { pw = {}; }

pw.upload = function(id) {
	var id = id;
	var $u = $('#' + id);
	var pua = $u.attr('uploadAction');
	var pup = $u.attr('uploadParam');
	var pda = $u.attr('dnloadAction');
	var pdp = $u.attr('dnloadParam');
	var pdl = $u.attr('defaultLink');
	var pdt = $u.attr('defaultText');
	
	var $uf = $u.children('.n-uploader-file');
	var $up = $u.children('.n-uploader-progress');
	var $ue = $u.children('.n-uploader-error');
	var $ui = $u.children('.n-uploader-image');
	var $ut = $u.children('.n-uploader-text');

	var $uct = $u.children('.n-uploader-ct');
	var $ufn = $u.children('.n-uploader-fn');
	var $ufs = $u.children('.n-uploader-fs');
	var $usn = $u.children('.n-uploader-sn');
	
	function _filesize(fs) {
		var sz = String.formatSize(fs);
		if (sz.length > 0) {
			sz = '(' + sz + ')';
		}
		return sz;
	}

	function _info(uct, ufn, ufs, usn) {
		uct = uct || $uct.val();
		ufn = ufn || $ufn.val();
		ufs = ufs || $ufs.val();
		usn = usn || $usn.val();

		if (ufn) {
			if (usn) {
				$ut.html('<a class="n-a n-a-it" href="' + pda + '?' + pdp + '=' + encodeURIComponent(usn) + '">'
						+ '<em class="ui-icon ui-icon-check n-a-icon n-uploader-icon"></em>'
						+ ufn + ' ' + _filesize(ufs)
						+ '</a>');
			}
			else {
				$ut.html('<span><em class="ui-icon ui-icon-check n-a-icon n-uploader-icon"></em>'
						+ ufn + ' ' + _filesize(ufs)
						+ '</span>');
			}
			$ut.show();
		}
		else if (pdl) {
			$ut.html('<a class="n-a'
					+ (String.isEmpty(pdt) ? ' n-a-io' : ' n-a-it')
					+ '" href="' + pdl + '">'
					+ '<em class="n-icon '
					+ (uct.startsWith('image') ? 'n-icon-file_img' : 'n-icon-attach')
					+ ' n-a-icon n-uploader-icon"></em>'
					+ String.defaults(pdt)
					+ '</a>')
				.show();
		}

		if (usn && uct.startsWith('image')) {
			$ui.html('<img src="' + pda + '?' + pdp + '=' + usn + '"></img>').fadeIn();
		}
		else if (pdl && uct.startsWith('image')) {
			$ui.html('<img src="' + pdl + '"></img>').fadeIn();
		}
	}
	
	function _error(uct, ufn, ufs, usn) {
		$ut.html('<span><em class="ui-icon ui-icon-close n-uploader-icon"></em>'
			+ ((ufn || $uf.val()) + ' ' + _filesize(ufs))
			+ '</span>')
			.show();
	}
	
	function _upload() {
		var progress = 0;

		$uct.val('');
		$ufn.val('');
		$ufs.val('');
		$usn.val('');
		
		$ue.hide().empty();
		$ui.hide().empty();
		$ut.hide().empty();
		$uf.hide();		

		$up.css({
			width: $uf.width() + 'px',
			height: Math.floor($uf.height() * 0.8) + 'px'
		})
		.show()
		.progressbar('value', progress);

		var timer = setInterval(function() {
			$up.progressbar('value', progress++);
			if (progress >= 90) {
				if (timer) {
					clearInterval(timer);
					timer = null;
				}
			}
		}, 20);

		function _endUpload() {
			$uf = $u.children('.n-uploader-file');
			progress = 100;
			$up.progressbar('value', progress).css({display: 'none'});
			$uf.show();
		}

		var file = {}; file[pup] = $uf; 
		$.ajaf({
			url: pua,
			file: file,
			dataType: 'json',
			success: function(d) {
				_endUpload();
				var r = d[pup];
				if (d.success) {
					$uct.val(r.contentType);
					$ufn.val(r.fileName);
					$ufs.val(r.fileSize);
					$usn.val(r.saveName);
					_info();
				}
				else {
					_error(r.contentType, r.fileName, r.fileSize, r.saveName);
					pw.notice({ container: $ue }).add(d);
					$ue.slideDown();
				}
			},
			error: function(xhr, status, e) {
				_endUpload();
				pw.notice({ container: $ue }).add(
						(e ? "<pre>" + (e + "").escapeHtml() + "</pre>" : (xhr ? xhr.responseText : status)),
						'error'
					);
				$ue.slideDown();
			}
		});
	}

	_info();
	$up.hide().progressbar();
	$uf.change(function() { 
		setTimeout(_upload, 10); 
	});
};
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
if (typeof(pw) == "undefined") { pw = {}; }

pw.viewfield = function(o) {
	var api = {
		el: $(o),
		val: function(v) {
			if (typeof(v) == 'undefined') {
				return this.el.val();
			}
			else {
				this.el.val(v).next().text(v);
				return this;
			}
		}
	};
	
	return api;
};
