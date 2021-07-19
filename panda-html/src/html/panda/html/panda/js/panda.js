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
		return this.splice(0, this.length);
	}
}

if (typeof Array.prototype.insert != 'function') {
	Array.prototype.insert = function() {
		var args = [ arguments[0], 0 ];
		[].push.apply(args, [].slice.call(arguments, 1));
		[].splice.apply(this, args);
		return this;
	}
}

if (typeof Array.prototype.remove != 'function') {
	Array.prototype.remove = function(o) {
		var c = 0;
		for (var i = this.length - 1; i >= 0; i--) {
			if (this[i] === o) {
				this.splice(i, 1);
				c++;
			}
		}
		return c;
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
//  json2.js
//  2016-10-28
//  Public Domain.
//  NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.
//  See http://www.JSON.org/js.html
//  This code should be minified before deployment.
//  See http://javascript.crockford.com/jsmin.html

//  USE YOUR OWN COPY. IT IS EXTREMELY UNWISE TO LOAD CODE FROM SERVERS YOU DO
//  NOT CONTROL.

//  This file creates a global JSON object containing two methods: stringify
//  and parse. This file provides the ES5 JSON capability to ES3 systems.
//  If a project might run on IE8 or earlier, then this file should be included.
//  This file does nothing on ES5 systems.

//      JSON.stringify(value, replacer, space)
//          value       any JavaScript value, usually an object or array.
//          replacer    an optional parameter that determines how object
//                      values are stringified for objects. It can be a
//                      function or an array of strings.
//          space       an optional parameter that specifies the indentation
//                      of nested structures. If it is omitted, the text will
//                      be packed without extra whitespace. If it is a number,
//                      it will specify the number of spaces to indent at each
//                      level. If it is a string (such as "\t" or "&nbsp;"),
//                      it contains the characters used to indent at each level.
//          This method produces a JSON text from a JavaScript value.
//          When an object value is found, if the object contains a toJSON
//          method, its toJSON method will be called and the result will be
//          stringified. A toJSON method does not serialize: it returns the
//          value represented by the name/value pair that should be serialized,
//          or undefined if nothing should be serialized. The toJSON method
//          will be passed the key associated with the value, and this will be
//          bound to the value.

//          For example, this would serialize Dates as ISO strings.

//              Date.prototype.toJSON = function (key) {
//                  function f(n) {
//                      // Format integers to have at least two digits.
//                      return (n < 10)
//                          ? "0" + n
//                          : n;
//                  }
//                  return this.getUTCFullYear()   + "-" +
//                       f(this.getUTCMonth() + 1) + "-" +
//                       f(this.getUTCDate())      + "T" +
//                       f(this.getUTCHours())     + ":" +
//                       f(this.getUTCMinutes())   + ":" +
//                       f(this.getUTCSeconds())   + "Z";
//              };

//          You can provide an optional replacer method. It will be passed the
//          key and value of each member, with this bound to the containing
//          object. The value that is returned from your method will be
//          serialized. If your method returns undefined, then the member will
//          be excluded from the serialization.

//          If the replacer parameter is an array of strings, then it will be
//          used to select the members to be serialized. It filters the results
//          such that only members with keys listed in the replacer array are
//          stringified.

//          Values that do not have JSON representations, such as undefined or
//          functions, will not be serialized. Such values in objects will be
//          dropped; in arrays they will be replaced with null. You can use
//          a replacer function to replace those with JSON values.

//          JSON.stringify(undefined) returns undefined.

//          The optional space parameter produces a stringification of the
//          value that is filled with line breaks and indentation to make it
//          easier to read.

//          If the space parameter is a non-empty string, then that string will
//          be used for indentation. If the space parameter is a number, then
//          the indentation will be that many spaces.

//          Example:

//          text = JSON.stringify(["e", {pluribus: "unum"}]);
//          // text is '["e",{"pluribus":"unum"}]'

//          text = JSON.stringify(["e", {pluribus: "unum"}], null, "\t");
//          // text is '[\n\t"e",\n\t{\n\t\t"pluribus": "unum"\n\t}\n]'

//          text = JSON.stringify([new Date()], function (key, value) {
//              return this[key] instanceof Date
//                  ? "Date(" + this[key] + ")"
//                  : value;
//          });
//          // text is '["Date(---current time---)"]'

//      JSON.parse(text, reviver)
//          This method parses a JSON text to produce an object or array.
//          It can throw a SyntaxError exception.

//          The optional reviver parameter is a function that can filter and
//          transform the results. It receives each of the keys and values,
//          and its return value is used instead of the original value.
//          If it returns what it received, then the structure is not modified.
//          If it returns undefined then the member is deleted.

//          Example:

//          // Parse the text. Values that look like ISO date strings will
//          // be converted to Date objects.

//          myData = JSON.parse(text, function (key, value) {
//              var a;
//              if (typeof value === "string") {
//                  a =
//   /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)Z$/.exec(value);
//                  if (a) {
//                      return new Date(Date.UTC(+a[1], +a[2] - 1, +a[3], +a[4],
//                          +a[5], +a[6]));
//                  }
//              }
//              return value;
//          });

//          myData = JSON.parse('["Date(09/09/2001)"]', function (key, value) {
//              var d;
//              if (typeof value === "string" &&
//                      value.slice(0, 5) === "Date(" &&
//                      value.slice(-1) === ")") {
//                  d = new Date(value.slice(5, -1));
//                  if (d) {
//                      return d;
//                  }
//              }
//              return value;
//          });

//  This is a reference implementation. You are free to copy, modify, or
//  redistribute.

/*jslint
    eval, for, this
*/

/*property
    JSON, apply, call, charCodeAt, getUTCDate, getUTCFullYear, getUTCHours,
    getUTCMinutes, getUTCMonth, getUTCSeconds, hasOwnProperty, join,
    lastIndex, length, parse, prototype, push, replace, slice, stringify,
    test, toJSON, toString, valueOf
*/


// Create a JSON object only if one does not already exist. We create the
// methods in a closure to avoid creating global variables.

if (typeof JSON !== "object") {
    JSON = {};
}

(function () {
    "use strict";

    var rx_one = /^[\],:{}\s]*$/;
    var rx_two = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g;
    var rx_three = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g;
    var rx_four = /(?:^|:|,)(?:\s*\[)+/g;
    var rx_escapable = /[\\"\u0000-\u001f\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;
    var rx_dangerous = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10
            ? "0" + n
            : n;
    }

    function this_value() {
        return this.valueOf();
    }

    if (typeof Date.prototype.toJSON !== "function") {

        Date.prototype.toJSON = function () {

            return isFinite(this.valueOf())
                ? this.getUTCFullYear() + "-" +
                        f(this.getUTCMonth() + 1) + "-" +
                        f(this.getUTCDate()) + "T" +
                        f(this.getUTCHours()) + ":" +
                        f(this.getUTCMinutes()) + ":" +
                        f(this.getUTCSeconds()) + "Z"
                : null;
        };

        Boolean.prototype.toJSON = this_value;
        Number.prototype.toJSON = this_value;
        String.prototype.toJSON = this_value;
    }

    var gap;
    var indent;
    var meta;
    var rep;


    function quote(string) {

// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can safely slap some quotes around it.
// Otherwise we must also replace the offending characters with safe escape
// sequences.

        rx_escapable.lastIndex = 0;
        return rx_escapable.test(string)
            ? "\"" + string.replace(rx_escapable, function (a) {
                var c = meta[a];
                return typeof c === "string"
                    ? c
                    : "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4);
            }) + "\""
            : "\"" + string + "\"";
    }


    function str(key, holder) {

// Produce a string from holder[key].

        var i;          // The loop counter.
        var k;          // The member key.
        var v;          // The member value.
        var length;
        var mind = gap;
        var partial;
        var value = holder[key];

// If the value has a toJSON method, call it to obtain a replacement value.

        if (value && typeof value === "object" &&
                typeof value.toJSON === "function") {
            value = value.toJSON(key);
        }

// If we were called with a replacer function, then call the replacer to
// obtain a replacement value.

        if (typeof rep === "function") {
            value = rep.call(holder, key, value);
        }

// What happens next depends on the value's type.

        switch (typeof value) {
        case "string":
            return quote(value);

        case "number":

// JSON numbers must be finite. Encode non-finite numbers as null.

            return isFinite(value)
                ? String(value)
                : "null";

        case "boolean":
        case "null":

// If the value is a boolean or null, convert it to a string. Note:
// typeof null does not produce "null". The case is included here in
// the remote chance that this gets fixed someday.

            return String(value);

// If the type is "object", we might be dealing with an object or an array or
// null.

        case "object":

// Due to a specification blunder in ECMAScript, typeof null is "object",
// so watch out for that case.

            if (!value) {
                return "null";
            }

// Make an array to hold the partial results of stringifying this object value.

            gap += indent;
            partial = [];

// Is the value an array?

            if (Object.prototype.toString.apply(value) === "[object Array]") {

// The value is an array. Stringify every element. Use null as a placeholder
// for non-JSON values.

                length = value.length;
                for (i = 0; i < length; i += 1) {
                    partial[i] = str(i, value) || "null";
                }

// Join all of the elements together, separated with commas, and wrap them in
// brackets.

                v = partial.length === 0
                    ? "[]"
                    : gap
                        ? "[\n" + gap + partial.join(",\n" + gap) + "\n" + mind + "]"
                        : "[" + partial.join(",") + "]";
                gap = mind;
                return v;
            }

// If the replacer is an array, use it to select the members to be stringified.

            if (rep && typeof rep === "object") {
                length = rep.length;
                for (i = 0; i < length; i += 1) {
                    if (typeof rep[i] === "string") {
                        k = rep[i];
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (
                                gap
                                    ? ": "
                                    : ":"
                            ) + v);
                        }
                    }
                }
            } else {

// Otherwise, iterate through all of the keys in the object.

                for (k in value) {
                    if (Object.prototype.hasOwnProperty.call(value, k)) {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (
                                gap
                                    ? ": "
                                    : ":"
                            ) + v);
                        }
                    }
                }
            }

// Join all of the member texts together, separated with commas,
// and wrap them in braces.

            v = partial.length === 0
                ? "{}"
                : gap
                    ? "{\n" + gap + partial.join(",\n" + gap) + "\n" + mind + "}"
                    : "{" + partial.join(",") + "}";
            gap = mind;
            return v;
        }
    }

// If the JSON object does not yet have a stringify method, give it one.

    if (typeof JSON.stringify !== "function") {
        meta = {    // table of character substitutions
            "\b": "\\b",
            "\t": "\\t",
            "\n": "\\n",
            "\f": "\\f",
            "\r": "\\r",
            "\"": "\\\"",
            "\\": "\\\\"
        };
        JSON.stringify = function (value, replacer, space) {

// The stringify method takes a value and an optional replacer, and an optional
// space parameter, and returns a JSON text. The replacer can be a function
// that can replace values, or an array of strings that will select the keys.
// A default replacer method can be provided. Use of the space parameter can
// produce text that is more easily readable.

            var i;
            gap = "";
            indent = "";

// If the space parameter is a number, make an indent string containing that
// many spaces.

            if (typeof space === "number") {
                for (i = 0; i < space; i += 1) {
                    indent += " ";
                }

// If the space parameter is a string, it will be used as the indent string.

            } else if (typeof space === "string") {
                indent = space;
            }

// If there is a replacer, it must be a function or an array.
// Otherwise, throw an error.

            rep = replacer;
            if (replacer && typeof replacer !== "function" &&
                    (typeof replacer !== "object" ||
                    typeof replacer.length !== "number")) {
                throw new Error("JSON.stringify");
            }

// Make a fake root object containing our value under the key of "".
// Return the result of stringifying the value.

            return str("", {"": value});
        };
    }


// If the JSON object does not yet have a parse method, give it one.

    if (typeof JSON.parse !== "function") {
        JSON.parse = function (text, reviver) {

// The parse method takes a text and an optional reviver function, and returns
// a JavaScript value if the text is a valid JSON text.

            var j;

            function walk(holder, key) {

// The walk method is used to recursively walk the resulting structure so
// that modifications can be made.

                var k;
                var v;
                var value = holder[key];
                if (value && typeof value === "object") {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }


// Parsing happens in four stages. In the first stage, we replace certain
// Unicode characters with escape sequences. JavaScript handles many characters
// incorrectly, either silently deleting them, or treating them as line endings.

            text = String(text);
            rx_dangerous.lastIndex = 0;
            if (rx_dangerous.test(text)) {
                text = text.replace(rx_dangerous, function (a) {
                    return "\\u" +
                            ("0000" + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

// In the second stage, we run the text against regular expressions that look
// for non-JSON patterns. We are especially concerned with "()" and "new"
// because they can cause invocation, and "=" because it can cause mutation.
// But just to be safe, we want to reject all unexpected forms.

// We split the second stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace the JSON backslash pairs with "@" (a non-JSON character). Second, we
// replace all simple value tokens with "]" characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or "]" or
// "," or ":" or "{" or "}". If that is so, then the text is safe for eval.

            if (
                rx_one.test(
                    text
                        .replace(rx_two, "@")
                        .replace(rx_three, "]")
                        .replace(rx_four, "")
                )
            ) {

// In the third stage we use the eval function to compile the text into a
// JavaScript structure. The "{" operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                j = eval("(" + text + ")");

// In the optional fourth stage, we recursively walk the new structure, passing
// each name/value pair to a reviver function for possible transformation.

                return (typeof reviver === "function")
                    ? walk({"": j}, "")
                    : j;
            }

// If the text is not JSON parseable, then a SyntaxError is thrown.

            throw new SyntaxError("JSON.parse");
        };
    }

    if (typeof JSON.sparse !== "function") {
    	JSON.sparse = function(v, r) {
    		try {
    			return JSON.parse(v, r);
    		}
    		catch (e) {
    			return null;
    		}
    	}
    }
}());
	
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
		return parseInt(Number.trim(s), r);
	};
}
if (typeof Number.parseFloat != "function") {
	Number.parseFloat = function(s) {
		return parseFloat(Number.trim(s));
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
if (typeof String.prototype.substrAfter != 'function') {
	String.prototype.substrAfter = function(c) {
		var s = this, i = s.indexOf(c);
		return (i >= 0) ? s.substring(i + 1) : "";
	};
}
if (typeof String.prototype.substrBefore != 'function') {
	String.prototype.substrBefore = function(c) {
		var s = this, i = s.indexOf(c);
		return (i >= 0) ? s.substring(0, i) : s;
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
		return this.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;").replace(/'/g, "&apos;");
	};
}
if (typeof String.prototype.unescapeHtml != "function") {
	String.prototype.unescapeHtml = function() {
		return this.replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&apos;/g, "'").replace(/&amp;/g, "&");
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
		var xml = '';
		var pad = 0;
		var ss = this.replace(/(>)(<)(\/*)/g, '$1\r\n$2$3').split('\r\n');
		for (var i = 0; i < ss.length; i++) {
			var s = ss[i];
			var indent = 0;
			if (s.match( /.+<\/\w[^>]*>$/ )) {
			}
			else if (s.match( /^<\/\w/ )) {
				if (pad > 0) {
					pad -= 1;
				}
			}
			else if (s.match( /^<\w[^>]*[^\/]*>.*$/ )) {
				indent = 1;
			}
	
			xml += ('').leftPad(' ', pad * 2) + s + '\r\n';
			pad += indent;
		}
	
		return xml;
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
if (typeof String.substrAfter != "function") {
	String.substrAfter = function(s, c) {
		return s != null ? String(s).substrAfter(c) : "";
	};
}
if (typeof String.substrBefore != "function") {
	String.substrBefore = function(s, c) {
		return s != null ? String(s).substrBefore(c) : "";
	};
}
if (typeof String.ellipsis != "function") {
	String.ellipsis = function(s, l) {
		return s != null ? String(s).ellipsis(l) : "";
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
if (typeof String.formatSize != "function") {
	var KB = 1024,
		MB = KB * KB,
		GB = MB * KB,
		TB = GB * KB,
		PB = TB * KB;
	
	String.formatSize = function(n, p) {
		p = Math.pow(10, p || 2);
		var sz = "";
		if (n >= PB) {
			sz = Math.round(n * p / PB) / p + ' PB';
		}
		else if (n >= TB) {
			sz = Math.round(n * p / TB) / p + ' TB';
		}
		else if (n >= GB) {
			sz = Math.round(n * p / GB) / p + ' GB';
		}
		else if (n >= MB) {
			sz = Math.round(n * p / MB) / p + ' MB';
		}
		else if (n >= KB) {
			sz = Math.round(n * p / KB) / p + ' KB';
		}
		else if (n != '') {
			sz = n + ' bytes';
		}
		return sz;
	};
}

(function($) {
	function setAlertType($a, s, t) {
		for (var i in s.types) {
			$a.removeClass(s.types[i]);
		}
		$a.addClass(s.types[t]);
	}

	function msg_li(s, t, m, n) {
		if (n && s.label) {
			m = n + s.label + m;
		}
		if (s.escape) {
			m = m.escapeHtml();
		}
		return $('<li>').addClass(s.texts[t])
			.append($('<i>').addClass(s.icons[t]))
			.append($('<span>').html(m));
	}

	function addMsg($a, s, m, t) {
		$a.append($('<ul>').addClass(s.css).append(msg_li(s, t, m)));
		setAlertType($a, s, t);
	}

	function addMsgs($a, s, m, t) {
		if (m) {
			var $u = $('<ul>').addClass(s.css);
			if ($.isArray(m)) {
				for (var i = 0; i < m.length; i++) {
					$u.append(msg_li(s, t, m[i]));
				}
			}
			else {
				for (var n in m) {
					var v = m[n];
					if ($.isArray(v)) {
						for (var i = 0; i < v.length; i++) {
							$u.append(msg_li(s, t, v[i], n));
						}
					}
					else {
						$u.append(msg_li(s, t, v, n));
					}
				}
			}
			$a.append($u);
			setAlertType($a, s, t);
		}
	}

	function addInputErrors($f, s, m, t) {
		if (m) {
			for (var n in m) {
				var $i = $f.find('input[name="' + n + '"]');
				if ($i.length) {
					$i.closest('.form-group').addClass('has-error').find('.p-field-errors').remove();
					var $u = $('<ul>').attr('errorfor', n).addClass(s.css).addClass('p-field-errors');
					var v = m[n];
					if ($.isArray(v)) {
						for (var i = 0; i < v.length; i++) {
							$u.append(msg_li(s, t, v[i], n));
						}
					}
					else {
						$u.append(msg_li(s, t, v, n));
					}
					$u.insertAfter($i);
				}
			}
		}
	}

	function addAlerts($a, s, m, t) {
		if (typeof(m) == 'string') {
			addMsg($a, s, m, t || 'info');
		}
		else if ($.isArray(m)) {
			for (var i = 0; i < m.length; i++) {
				if (typeof(m[i]) == 'string') {
					addMsg($a, s, m[i], t || 'info');
				}
				else {
					addMsg($a, s, m[i].html, m[i].type);
				}
			}
		}
		else if (m) {
			if (m.params) {
				if (t) {
					addInputErrors($(t), s, m.params.errors, 'error');
				}
				else {
					addMsgs($a, s, m.params.errors, "error");
				}
			}
			if (m.action) {
				addMsgs($a, s, m.action.errors, 'error');
				addMsgs($a, s, m.action.warnings, 'warn');
				addMsgs($a, s, m.action.confirms, 'help');
				addMsgs($a, s, m.action.messages, 'info');
			}
		}
	}

	$.palert = {
		css: 'fa-ul',
		label: false, //': ',
		escape: true,
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
		if (s) {
			$c.data('palert', s);
		}
		return {
			clear: function() {
				$c.children('.alert').remove();
				return this;
			},
			error: function(m) {
				return this.add(m, 'error');
			},
			warn: function(m) {
				return this.add(m, 'warn');
			},
			help: function(m) {
				return this.add(m, 'help');
			},
			info: function(m) {
				return this.add(m, 'info');
			},
			add: function(m, t) {
				var s = $.extend({}, $c.data('palert'), $.palert);
				var a = false, $a = $c.children('.p-alert');
				if ($a.size() < 1) {
					$a = $('<div></div>').addClass('p-alert alert alert-dismissable fade in').css('display', 'none');
					a = true;
				}

				addAlerts($a, s, m, t);

				if (a && $a.children().length) {
					$a.prepend("<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>");
					$c.prepend($a);
					$a.slideDown();
				}
				return this;
			},
			actionAlert: function(d, f) {
				if (d.alerts) {
					this.add(d.alerts, f);
					if (d.alerts.params && !d.alerts.params.empty) {
						this.error($(f).data('ajaxInputError'));
					}
				}
				if (d.exception) {
					var e = d.exception;
					var m = e.message + (e.stackTrace ? ("\n" + e.stackTrace) : "");
					this.add(m, 'error');
				}
				return this;
			},
			ajaxJsonError: function(xhr, status, e, m) {
				if (xhr && xhr.responseJSON) {
					var d = xhr.responseJSON;
					if (d && (d.alerts || d.exception)) {
						return this.actionAlert(d);
					}
				}
			
				msg = '';
				if (e) {
					msg += e + '\n';
				}
				
				if (xhr && xhr.responseText) {
					try {
						var r = JSON.parse(xhr.responseText);
						msg += JSON.stringify(r, null, 2);
					}
					catch (ex) {
						msg += xhr.responseText;
					}
				}
			
				return this.add(msg || m, 'error');
			}
		}
	};
	
	$.fn.palert = function(opt) {
		var ops = typeof opt === 'object' && opt;
		var args = Array.prototype.slice.call(arguments, 1);
		return this.each(function() {
			var pa = palert($(this), ops);
			if (typeof opt === 'string') {
				pa[opt].apply(pa, args);
			}
		});
	};

	$.palert.notify = function(m, t, s) {
		s = $.extend({}, $.palert, s);
		var ns = $.extend({ style: 'palert' }, $.palert.notifys);

		var $a = $('<div>').addClass('p-alert alert');
		addAlerts($a, s, m, t);
		
		if ($.notify) {
			if (!$.notify.getStyle('palert')) {
				$.notify.addStyle('palert', {
					html: '<div data-notify-html="html"></div>'
				});
			}
			$.notify({ html: $a}, ns);
		}
	};
	
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

})(jQuery);
///////////////////////////////////////////////////////
// swipe for carousel
//
(function($) {
	var regTouchGestures = function($e) {
		$e.hammer()
			.on("swipeleft", function(e) {
				$(this).data('bs.carousel').next();
				e.gesture.stopDetect();
			})
			.on("swiperight", function(e) {
				$(this).data('bs.carousel').prev();
				e.gesture.stopDetect();
			});
	};
	
	$(window).on('load', function () {
		if (typeof($.fn.hammer) == 'function') {
			$('[data-ride="carousel"]').each(function() {
				regTouchGestures($(this));
			})
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
	var langs = {};
	var dps = 'div.p-datepicker, div.p-datetimepicker, div.p-timepicker';

	function initDateTimePicker() {
		if (typeof($.fn.datetimepicker) == 'undefined') {
			setTimeout(initDateTimePicker, 100);
			return;
		}
		for (var i in langs) {
			if (!$.fn.datetimepicker.dates[i]) {
				setTimeout(initDateTimePicker, 100);
				return;
			}
		}

		$(dps).datetimepicker();
	}

	$(window).on('load', function () {
		var $dps = $(dps);
		if ($dps.size()) {
			var css = $dps.data('datetimepickerCss');
			if (css) {
				$.jcss(css);
			}
			var js = $dps.data('datetimepickerJs');
			if (js) {
				$.jscript(js, function() {
					$dps.each(function() {
						var i = $(this).data('language');
						var v = $(this).data('datetimepickerLangJs');
						if (i && v && !langs[i]) {
							$.jscript(v);
							langs[i] = v;
						}
					});
				});
			}
			initDateTimePicker();
		}
	});
})(jQuery);
if (typeof(panda) == "undefined") { panda = {}; }

(function($) {
	function ajaxFormSubmit() {
		var $f = $(this);
		var $a = $('#' + $f.attr('id') + '_alert').empty();

		$f.find('.has-error').removeClass('has-error').end().find('.p-field-errors').remove();
		if (!$f.isLoadMasked()) {
			$f.loadmask();
		}

		$.ajax({
			url: $f.data('ajaxAction'),
			method: 'post',
			data: $f.serializeArray(),
			dataType: 'json',
			success: function(d) {
				$a.palert('actionAlert', d, $f);
				if (d.result) {
					$f.vals(d.result);
					$f.find('div.p-datepicker, div.p-datetimepicker, div.p-timepicker').each(function() {
						var v = $(this).find('input').val();
						if (v) {
							$(this).datetimepicker('setValue', new Date(v));
						}
					});
				}
				if ($a.children().length) {
					$a.scrollIntoView();
				}
			},
			error: function(xhr, status, e) {
				$a.palert('ajaxJsonAlert', xhr, status, e, $f.data('ajaxServerError'));
			},
			complete: function() {
				$f.unloadmask();
			}
		});
		return false;
	}
			
	function ajaxSubmitHook($f) {
		// hook submit
		if (!$f.data("ajaxSubmitHooked") && $f.data('ajaxAction')) {
			$f.data('ajaxSubmitHooked', true).submit(ajaxFormSubmit);
		}
	}

	function ajaxLoadInnerForm($f) {
		var $c = $f.closest('.p-popup, .p-inner');
		var data = $f.serializeArray();
		if ($c.hasClass('p-inner')) {
			data.push({ name: '__inner', value: 'true' });
		}
		else {
			data.push({ name: '__popup', value: 'true' });
		}
		
		if ($f.attr('loadmask') != 'false') {
			$c.parent().loadmask();
		}

		$.ajax({
			url: $f.attr('action'),
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
	}

	function actionHook($f) {
		// hook action
		if (!$f.data("actionHooked")) {
			$f.data('actionHooked', true)
				.find('input[data-action], button[data-action]').click(function() {
					$(this).closest('form').attr('action', $(this).data('action'));
				});
		}
	}

	function isSelfForm($f) {
		var t = $f.attr('target') || '_self';
		return t == '_self' || t == '_top' || t == '_parent';
	}

	function innerHook($f) {
		// hook inner, popup
		if ($f.data("hooked")) {
			return;
		}
		var $c = $f.closest('.p-popup, .p-inner');
		if ($c.length > 0) {
			$f.data('hooked', true);
			$f.submit(function(e) {
				e.preventDefault();
				ajaxLoadInnerForm($(this));
				return false;
			});
		}
	}

	function loadmaskHook($f) {
		// hook loadmask
		if ($f.data("hooked")) {
			return;
		}

		if ($f.height() > 20 && $f.attr('loadmask') != 'false') {
			$f.data('hooked', true);
			$f.submit(function() {
				$(this).loadmask();
			})
		}
	}

	$(window).on('load', function() {
		$('form').each(function() {
			var $f = $(this);

			// hook action
			actionHook($f);

			if (isSelfForm($f)) {
				// hook inner, popup
				innerHook($f);

				// hook loadmask
				if (panda.enable_loadmask_form) {
					loadmaskHook($f);
				}
				
				ajaxSubmitHook($f);
			}
		});
	});
})(jQuery);
(function($) {
	var langs = {};
	
	function initSummerNotePlugins() {
		$.extend($.summernote.plugins, {
			/**
			 * @param {Object}
			 *            context - context object has status of editor.
			 */
			'media': function (context) {
				// ui has renders to build ui elements.
				// - you can create a button with `ui.button`
				var ui = $.summernote.ui;
				var $n = context.$note;

				// add plugin button
				context.memo('button.media', function() {
					function popup_callback(ms) {
						$.popup().hide();
						if (ms) {
							for (var i = 0; i < ms.length; i++) {
								$n.summernote('insertImage', ms[i].href, ms[i].name);
							}
						}
					}

					// create button
					var button = ui.button({
						contents: '<i class="' + ($n.data('mediaIcon') || 'fa fa-list-alt') + '"/>',
						tooltip: $n.data('mediaText') || 'Media Browser',
						click: function() {
							var url = $n.data('mediaHref');
							var id = $n.data('mediaPopupId');
							if (!id) {
								id = ($n.attr('id') || $n.attr('name') || url.hashCode()) + '_media_popup';
							}
							$.popup({
									id: id,
									url: url
								})
								.show({
									id: id,
									trigger: this,
									popover: true,
									onpopup: null,
									onhide: null,
									callback: popup_callback
								});
						}
					});

					// create jQuery object from button instance.
					return button.render();
				});

				// This methods will be called when editor is destroyed by $('..').summernote('destroy');
				// You should remove elements on `initialize`.
				this.destroy = function() {
					this.$panel.remove();
					this.$panel = null;
				};
			}
		});
	}
	
	function initSummerNote() {
		if (typeof($.fn.summernote) == 'undefined') {
			setTimeout(initSummerNote, 100);
			return;
		}
		for (var i in langs) {
			if (!$.summernote.lang[i]) {
				setTimeout(initSummerNote, 100);
				return;
			}
		}

		initSummerNotePlugins();
		
		$('textarea.p-htmleditor.p-summernote').each(function() {
			var $t = $(this);
			var o = {};
			var p = [ 'toolbar', 'popover', 'fontNames', 'fontNamesIgnoreCheck' ];
			for (var i = 0; i < p.length; i++) {
				var v = $t.attr(p[i]);
				if (v) {
					try {
						o[p[i]] = JSON.parse(v);
					}
					catch (e) {
					}
				}
			}
			$.extend(o, $t.data('summernoteOptions'));

			var l = $t.data('summernoteLang');
			if (l) {
				o.lang = l;
			}

			if ($t.attr('readonly')) {
				$t.parent().addClass('p-htmleditor-readonly');
				$t.summernote($.extend(o, { toolbar: false })).summernote('disable');
			}
			else {
				var ms = [ 'picture', 'video' ];
				if ($t.data('mediaHref')) {
					ms.push('media');
				}
				$t.summernote($.extend({
					height: $t.attr('height'),
					followingToolbar: $t.attr('followingToolbar') || false,
					toolbar: [
						[ 'style', [ 'style', 'fontname', 'fontsize', 'color' ] ],
						[ 'text', [ 'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript' ] ],
						[ 'para', [ /*'height', */'paragraph', 'ol', 'ul' ] ],
						[ 'insert', [ 'hr', 'table', 'link' ] ],
						[ 'media', ms ],
						[ 'edit', [ 'undo', 'redo', 'clear' ] ], 
						[ 'misc', [ 'codeview', 'fullscreen', 'help' ] ],
					]
				}, o));
			}
		});
	}

	function initClEditor() {
		if (typeof($.fn.cleditor) == 'undefined') {
			setTimeout(initClEditor, 100);
			return;
		}

		$('textarea.p-htmleditor.p-cleditor').cleditor();
	}

	$(window).on('load', function () {
		var $sns = $('textarea.p-htmleditor.p-summernote');
		if ($sns.size()) {
			var css = $sns.data('summernoteCss');
			if (css) {
				$.jcss(css);
			}
			var js = $sns.data('summernoteJs');
			if (js) {
				$.jscript(js, function() {
					$sns.each(function() {
						var i = $(this).data('summernoteLang');
						var v = $(this).data('summernoteLangJs');
						if (i && v && !langs[i]) {
							$.jscript(v);
							langs[i] = v;
						}
					});
				});
			}
			initSummerNote();
		}

		var $ces = $('textarea.p-htmleditor.p-cleditor');
		if ($ces.size()) {
			var css = $ces.data('cleditorCss');
			if (css) {
				$.jcss(css);
			}
			var js = $ces.data('cleditorJs');
			if (js) {
				$.jscript(js, true);
			}
			initClEditor();
		}
	});
})(jQuery);
if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.loading = function(timeout) {
	$('body').loadmask({ mask: false, fixed: true, timeout: timeout || 1000 });
};

panda.page_sort = function(name, dir) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 's.c': name, 's.d': dir });
	return false;
};
panda.page_sort_reverse = function(name, dir) {
	return panda.page_sort(name, dir.toLowerCase() == "asc" ? "desc" : "asc");
};
panda.page_goto = function(s) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 'p.s': s });
	return false;
};
panda.page_limit = function(l) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 'p.l': l });
	return false;
};
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


function _plv_init_table($lv) {
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
	return false;
}

function _plv_limit(id, n) {
	$('#' + id).loadmask();

	document.getElementById(id + "_limit").value = n;
	document.getElementById(id + "_submit").click();
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
	if ($lv.data('singleSelect')) {
		if ($tr.hasClass("p-lv-selected")) {
			if (ts || !$lv.data("untoggleSelect")) {
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

if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.meta_props = function() {
	var m = {};
	$('meta').each(function() {
		var $t = $(this);
		var a = $t.attr('property');
		if (a && a.startsWith('s:')) {
			var v = $t.attr('content');
			m[a.substring(2)] = v;
		}
	});
	return m;
};

(function($) {
	$(window).on('load', function () {
		$('.navbar-toggle').click(function() {
			$(this).toggleClass('active');
		});
	});
})(jQuery);
(function($) {
	$(window).on('load', function() {
		// invoke onPageLoad function
		for (var i in window) {
			if (i.startsWith('onPageLoad') && typeof(window[i]) == 'function') {
				window[i]();
				window[i] = null;
			}
		}
	});
})(jQuery);
(function() {
	function _click(evt) {
		var $el = $(this);
		if ($el.parent().hasClass('disabled')) {
			evt.preventDefault();
		}
		else {
			var pn = $el.attr('pageno');
			if (pn >= 0) {
				var $pg = $el.closest('.p-pager');
				var js = $pg.data('click');
				if (js) {
					js = js.replace('$', pn);
					js = js.replace('#', (pn - 1) * $pg.data('limit'));
					if (eval(js) === false) {
						evt.preventDefault();
					}
				}
			}
		}
	}

	function _setActivePage($p, n) {
		var $u = $p.find('ul.pagination');
		$u.find('li.active').removeClass('active');

		var $n = $u.find('li.p-pager-page');

		var m = $p.data('pages');
		var b = n - Math.floor($n.size() / 2);
		if (b + $n.size() > m) b = m - $n.size() + 1;
		if (b < 1) b = 1;

		var s = $p.data('style');
		if (n > 1) {
			$u.find('.p-pager-first, .p-pager-prev').removeClass('hidden disabled');
			$u.find('.p-pager-prev>a').attr('pageno', n - 1);
		}
		else {
			$u.find('.p-pager-first').addClass(s.contains('F') ? 'disabled' : 'hidden');
			$u.find('.p-pager-prev').addClass(s.contains('P') ? 'disabled' : 'hidden');
		}

		$u.find('.p-pager-ellipsis-left')[b > 1 ? 'removeClass' : 'addClass']('hidden');
		$n.each(function() {
			var $a = $(this).find('a');
			$a.attr('pageno', b).text(b);
			if (b == n) {
				$(this).addClass('active');
			}
			b++;
		});
		$u.find('.p-pager-ellipsis-right')[b <= m ? 'removeClass' : 'addClass']('hidden');

		if (n < m) {
			$u.find('.p-pager-next, .p-pager-last').removeClass('hidden disabled');
			$u.find('.p-pager-next>a').attr('pageno', n + 1);
		}
		else {
			$u.find('.p-pager-next').addClass(s.contains('N') ? 'disabled' : 'hidden');
			$u.find('.p-pager-last').addClass(s.contains('L') ? 'disabled' : 'hidden');
		}
	}
	
	$.fn.ppager = function(api, pno) {
		if (api == 'getActivePage') {
			return this.find('ul.pagination>li.active>a').attr('pageno');
		}
		if (api == 'setActivePage') {
			return this.each(function() { _setActivePage($(this), pno); });
		}
		return this.each(function() {
			var $p = $(this);
			if ($p.attr("ppager") != "true") {
				$p.attr("ppager", "true");
				if ($p.data("click")) {
					$p.find("a[pageno]").click(_click);
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
(function($) {
	function _pqr_init($qr) {
		if ($qr.data("pqueryer")) {
			return;
		}
		$qr.data("pqueryer", true);
		_pqr_init_filters($qr);
	}

	function _pqr_init_filters($qr) {
		$qr.find(".p-qr-filters")
			.find('.form-group')
				.each(function() {
					if ($(this).hasClass('p-hidden')) {
						$(this).find("input,select,textarea").prop('disabled', true);
					}
				}).end()
			.find('.p-qr-remove').click(_pqr_onDelFilter).end()
			.find('.p-qr-clear').click(_pqr_onClearFilters).end()
			.find('.p-qr-select').change(_pqr_onAddFilter).end()
			.find('.p-qr-f-number-c, .p-qr-f-date-c, .p-qr-f-datetime-c, .p-qr-f-time-c')
				.on('change', _pqr_onBetweenChange)
				.end()
			.find('form')
				.submit(function() {
					$qr.loadmask();
				});
	}

	function _pqr_clearFieldValue() {
		switch (this.tagName) {
		case "INPUT":
			switch (this.type) {
			case "radio":
				if (this.name != 'm' && !this.name.endsWith(".m")) {
					this.checked = false;
				}
				break;
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

	function _pqr_onBetweenChange() {
		var $t = $(this);
		if ($t.val() == 'bt') {
			$t.nextAll().removeClass('p-hidden').find('INPUT').prop('disabled', false);
		}
		else {
			$t.nextAll().addClass('p-hidden').find('INPUT').prop('disabled', true);
		}
	}

	function _pqr_onAddFilter() {
		var e = this;
		if (e.selectedIndex > 0) {
			$(e).closest(".p-qr-filters")
				.find('.p-qr-fsi-' + e.value)
					.removeClass('p-hidden')
					.find("input,select,textarea").prop('disabled', false).end()
					.end()
				.fieldset('expand');
			e.options[e.selectedIndex].disabled = true;
			e.selectedIndex = 0;
		}
		return false;
	}

	function _pqr_onDelFilter() {
		var $g = $(this).closest(".form-group");
		$g.addClass('p-hidden')
			.find("input,select,textarea")
				.prop('disabled', true)
				.each(_pqr_clearFieldValue)
				.end()
			.find(".p-label-error")
				.removeClass('p-label-error')
				.end()
			.find(".p-field-errors")
				.remove()
				.end()
			.closest(".p-qr-filters")
				.find('.p-qr-select > option[value=' + $g.data('item') + ']')
					.prop('disabled', false);
	}

	function _pqr_onClearFilters() {
		$(this).closest(".p-qr-form")
			.find(".p-field-errors")
				.remove()
				.end()
			.find(".p-label-error")
				.removeClass('p-label-error')
				.end()
			.find("input,select,textarea")
				.each(_pqr_clearFieldValue);
		return false;
	}

	$(window).on('load', function() {
		$('div.p-qr').each(function() {
			_pqr_init($(this));
		});
	});
})(jQuery);

(function($) {
	$(window).on('load', function() {
		var $t = $('.p-totop');
		if ($t.size() > 0) {
			$t.click(function() {
				$('html,body').animate({ scrollTop: 0 }, 'slow');
			});
	
			var $w = $(window);
			$w.scroll(function() {
				$t[$w.scrollTop() > $w.height() ? 'show' : 'hide']();
			});
		}
	});
})(jQuery);

(function($) {
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
						if ($t.val() != '') {
							$t.val('').trigger('change');
						}
					}
				});
		});
	};
	
	// DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-ptrigger]').ptrigger();
	});
})(jQuery);
(function($) {
	var isAdvancedUpload = function() {
		var div = document.createElement('div');
		return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
	}();

	var puploader = function($u) {
		if ($u.data('puploader')) {
			return;
		}
		$u.data('puploader', true);
		
		var loading = false;
		var progress = 0;

		var $uf = $u.children('.p-uploader-file');
		var $ub = $u.children('.p-uploader-btn');
		var $us = $u.children('.p-uploader-sep');
		var $ue = $u.children('.p-uploader-error');

		var $up = $('<div class="p-uploader-progress progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertAfter($ub.length > 0 ? $ub : $uf);

		if ($ue.length < 1) {
			$ue = $('<div class="p-uploader-error"></div>').insertAfter($up);
		}
		
		if ($us.length < 1) {
			$us = $('<div class="p-uploader-sep"></div>').insertAfter($ue);
		}

		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _filename(fn) {
			var u = fn.lastIndexOf('/');
			var w = fn.lastIndexOf('\\');
			var i = u > w ? u : w;
			return fn.substr(i + 1);
		}
		
		function _info(fi) {
			var fid = fi.name, fnm = _filename(fi.name), fsz = fi.size, fct = fi.type;
			var pdl = $u.data('dnloadLink');
			var pdn = $u.data('dnloadName');
			var pdd = JSON.sparse($u.data('dnloadData'));

			var $fit = $('<div>').addClass('p-uploader-item').insertAfter($us);
			var $fid = $('<input>').attr('type', 'hidden').attr('name', $u.data('name')).addClass('p-uploader-fid').appendTo($fit);
			var $ftx = $('<span>').addClass('p-uploader-text').appendTo($fit);
			var $fim = $('<div>').addClass('p-uploader-image').appendTo($fit);


			$fid.val(fid || '');
			
			fnm = fnm || fid || $uf.val();
			var durl = null;
			if (pdl && fid) {
				var ps = $.extend({}, pdd);
				ps[pdn] = fid;
				durl = pdl + '?' + $.param(ps);
			}
			
			var img = String.startsWith(fct, 'image/');
			if (fnm) {
				var s = '<i class="fa fa-' + (img ? 'image' : 'paperclip') + ' p-uploader-icon"></i> ' + fnm + ' ' + _filesize(fsz);
				if (durl) {
					$('<a>').attr('href', durl).html(s).appendTo($ftx);
				}
				else {
					$('<span>').html(s).appendTo($ftx);
				}
			}
			
			$('<i>').addClass('p-uploader-remove fa fa-remove').click(_remove).appendTo($ftx);

			if (img && durl) {
				var $a = $('<a>').attr('href', durl);
				var $i = $('<img>').addClass('img-thumbnail').attr('src', durl).appendTo($a);
				$a.appendTo($fim).fadeIn();
			}
		}

		function _set_progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _remove() {
			$(this).closest('.p-uploader-item').fadeOut(function() { $(this).remove(); });
			$ue.empty();
		}

		function _start_upload() {
			loading = true;
			progress = 0;

			($ub.length ? $ub : $uf).addClass('p-hidden');
			$ue.empty();

			_set_progress(0);
			$up.show();
		}

		function _fake_progress() {
			_set_progress(progress++);
			if (progress < 90) {
				setTimeout(_fake_progress, 20);
			}
		}

		function _ajax_progress(e) {
			if (e.lengthComputable) {
				var p = Math.round(e.loaded * 100 / e.total);
				_set_progress(p);
			}
		}
		
		function _end_upload() {
			_set_progress(100);
			$up.hide();
			$uf.val("");
			($ub.length ? $ub : $uf).removeClass('p-hidden');
			loading = false;
		}

		function _on_success_uploaded(d) {
			var fn = $u.data('onUploaded');
			if (typeof(window[fn]) == 'function') {
				return window[fn](d);
			}
			return false;
		}
		
		function _upload_on_success(d) {
			_end_upload();

			$ue.palert('actionAlert', d);
			if (d.success) {
				if (_on_success_uploaded(d)) {
					return;
				}
				
				if (!$uf.attr('multiple')) {
					$u.children('.p-uploader-item').remove();
				}
				
				var r = d.result;
				if ($.isArray(r)) {
					for (var i = 0; i < r.length; i++) {
						_info(r[i]);
					}
				}
				else {
					_info(r);
				}
			}
		}

		function _upload_on_error(xhr, status, e) {
			_end_upload();
			$ue.palert('error', (e ? (e + "") : (xhr ? xhr.responseText : status)));
		}
		
		function _upload_on_change() {
			if (loading || $uf.val() == "") {
				return;
			}

			if (isAdvancedUpload && $uf[0].files) {
				_ajax_upload($uf[0].files);
			}
			else {
				_ajaf_upload();
			}
		}

		function _upload_on_drop(e) {
			e.preventDefault();
			if (loading) {
				return;
			}
			_ajax_upload(e.originalEvent.dataTransfer.files);
		}

		function _ajaf_upload() {
			_start_upload();
			_fake_progress();

			var file = {}; file[$u.data('uploadName')] = $uf; 
			$.ajaf({
				url: $u.data('uploadLink'),
				data: JSON.sparse($u.data('uploadData')),
				file: file,
				dataType: 'json',
				success: _upload_on_success,
				error: _upload_on_error
			});
		}

		function _ajax_upload(dfs) {
			_start_upload();

			var data = new FormData();

			var ud = JSON.sparse($u.data('uploadData'));
			if (ud) {
				for (var i in ud) {
					data.append(i, ud[i]);
				}
			}

			if (dfs) {
				$.each(dfs, function(i, f) {
					data.append($u.data('uploadName'), f);
				});
			}

			$.ajax({
				url: $u.data('uploadLink'),
				type: 'POST',
				data: data,
				dataType: 'json',
				cache: false,
				contentType: false,
				processData: false,
				xhr: function() {
					var xhr = $.ajaxSettings.xhr();
					xhr.upload.addEventListener('progress', _ajax_progress);
					return xhr;
				},
				success: _upload_on_success,
				error: _upload_on_error
			});
		}

		// event handler
		$uf.change(function() {
			setTimeout(_upload_on_change, 10);
		});
		
		$ub.click(function(e) {
			e.preventDefault();
			$uf.trigger('click');
			return false;
		});

		// drap & drop
		if (isAdvancedUpload) {
			$u.addClass('p-uploader-draggable')
				.on('drag dragstart dragend dragover dragenter dragleave drop', function(e) {
					e.preventDefault();
					e.stopPropagation();
				})
				.on('dragover dragenter', function() {
					$u.addClass('p-uploader-dragover');
				})
				.on('dragleave dragend drop', function() {
					$u.removeClass('p-uploader-dragover');
				})
				.on('drop', _upload_on_drop);
		}
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
(function($) {	
	$(window).on('load', function () {
		$('.p-viewfield').prev('input').change(function() {
			var v = $(this).val();
			$(this).next('.p-viewfield').text(v == '' ? '\u3000' : v);
		});
		
		$('.p-viewfield[data-format="html"]').each(function() {
			$('<a href="#" class="p-vf-code"><i class="fa fa-code"></i></a>').appendTo($(this).parent());
		});
		
		$('.p-vf-code').click(function() {
			var $t = $(this);
			var $p = $t.toggleClass('active').parent();
			var v = $p.find('input').val();
			$p.find('.p-viewfield').html($t.hasClass('active') ? v.escapeHtml() : v);
			return false;
		});
	});
})(jQuery);
