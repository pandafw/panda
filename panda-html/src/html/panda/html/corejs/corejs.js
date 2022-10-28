(function() {
	"use strict";

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
			var args = [arguments[0], 0];
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
})();
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

var DateFormat = function(pattern) {
	"use strict";

	this._init(pattern);
};

(function() {
	"use strict";

	if (typeof Date.prototype.format != "function") {
		Date.prototype.format = function(f) {
			return (new DateFormat(f)).format(this);
		};
	}

	DateFormat.prototype = {
		_init: function(pattern) {
			this.pattern = pattern;
			this._patterns = [];

			for (var i = 0; i < pattern.length; i++) {
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

		format: function(date) {
			var result = [];
			for (var i = 0; i < this._patterns.length; i++) {
				result[i] = this._formatWord(date, this._patterns[i]);
			}
			return result.join('');
		},

		_formatWord: function(date, pattern) {
			var formatter = this._formatter[pattern.charAt(0)];
			if (formatter) {
				return formatter.apply(this, [date, pattern]);
			}
			return pattern;
		},

		_formatter: {
			"y": function(date, pattern) {
				// Year
				var year = String(date.getFullYear());
				if (pattern.length <= 2) {
					year = year.substring(2, 4);
				} else {
					year = this._zeroPadding(year, pattern.length);
				}
				return year;
			},
			"M": function(date, pattern) {
				// Month in year
				return this._zeroPadding(String(date.getMonth() + 1), pattern.length);
			},
			"d": function(date, pattern) {
				// Day in month
				return this._zeroPadding(String(date.getDate()), pattern.length);
			},
			"H": function(date, pattern) {
				// Hour in day (0-23)
				return this._zeroPadding(String(date.getHours()), pattern.length);
			},
			"h": function(date, pattern) {
				// Hour in day (1-12)
				var h = date.getHours();
				h = h > 12 ? h - 12 : (h == 0 ? 12 : h);
				return this._zeroPadding(String(h), pattern.length);
			},
			"m": function(date, pattern) {
				// Minute in hour
				return this._zeroPadding(String(date.getMinutes()), pattern.length);
			},
			"s": function(date, pattern) {
				// Second in minute
				return this._zeroPadding(String(date.getSeconds()), pattern.length);
			},
			"S": function(date, pattern) {
				// Millisecond
				return this._zeroPadding(String(date.getMilliseconds()), pattern.length);
			},
			"'": function(date, pattern) {
				// escape
				if (pattern == "''") {
					return "'";
				}
				return pattern.replace(/'/g, '');
			}
		},

		_zeroPadding: function(str, length) {
			if (str.length >= length) {
				return str;
			}

			return new Array(length - str.length + 1).join("0") + str;
		},

		/// Parser ///
		parse: function(text) {
			if (typeof text != 'string' || text == '')
				return null;

			var result = {
				year: 1970,
				month: 1,
				day: 1,
				hour: 0,
				min: 0,
				sec: 0,
				msec: 0
			};

			for (var i = 0; i < this._patterns.length; i++) {
				if (text == '') {
					return null; // parse error!!
				}
				text = this._parseWord(text, this._patterns[i], result);
				if (text === null) {
					return null; // parse error!!
				}
			}
			if (text != '') {
				return null; // parse error!!
			}

			return new Date(result.year, result.month - 1, result.day, result.hour,
				result.min, result.sec, result.msec);
		},

		_parseWord: function(text, pattern, result) {
			var parser = this._parser[pattern.charAt(0)];
			if (parser) {
				return parser.apply(this, [text, pattern, result]);
			}
			if (text.indexOf(pattern) != 0) {
				return null;
			}
			return text.substring(pattern.length);
		},

		_parser: {
			"y": function(text, pattern, result) {
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
				if (!this._isNumber(year)) {
					return null; // error
				}
				result.year = parseInt(year, 10);
				return text;
			},
			"M": function(text, pattern, result) {
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
				if (!this._isNumber(month)) {
					return null; // error
				}
				result.month = parseInt(month, 10);
				return text;
			},
			"d": function(text, pattern, result) {
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
				if (!this._isNumber(day)) {
					return null; // error
				}
				result.day = parseInt(day, 10);
				return text;
			},
			"H": function(text, pattern, result) {
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
				if (!this._isNumber(hour)) {
					return null; // error
				}
				result.hour = parseInt(hour, 10);
				return text;
			},
			"h": function(text, pattern, result) {
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
				if (!this._isNumber(hour)) {
					return null; // error
				}
				result.hour = parseInt(hour, 10);
				return text;
			},
			"m": function(text, pattern, result) {
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
				if (!this._isNumber(min)) {
					return null; // error
				}
				result.min = parseInt(min, 10);
				return text;
			},
			"s": function(text, pattern, result) {
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
				if (!this._isNumber(sec)) {
					return null; // error
				}
				result.sec = parseInt(sec, 10);
				return text;
			},
			"S": function(text, pattern, result) {
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
				if (!this._isNumber(msec)) {
					return null; // error
				}
				result.msec = parseInt(msec, 10);
				return text;
			},
			"'": function(text, pattern, result) {
				// escape
				if (pattern == "''") {
					pattern = "'";
				} else {
					pattern = pattern.replace(/'/g, '');
				}
				if (text.indexOf(pattern) != 0) {
					return null; // error
				}
				return text.substring(pattern.length);
			}
		},

		_isNumber: function(str) {
			return /^[0-9]*$/.test(str);
		}
	};

})();
(function() {
	"use strict";

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
		Function.prototype.callback = function(/*args...*/) {
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
		Function.prototype.bind = function(/*args...*/) {
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
			if (typeof fcn != "function") {
				return this;
			}
			var method = this;
			return function() {
				fcn.target = this;
				fcn.method = method;
				if (fcn.apply(scope || this || window, arguments) === false) {
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
})();

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
	
/**
 * @class DecimalFormat
 * @constructor
 * @param {String} pattern
 * @author Oskan Savli
 */
function DecimalFormat(pattern) {
	"use strict";

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
	for (var i = 0; i < pattern.length; i++) {
		if (pattern.charAt(i) == '#' || pattern.charAt(i) == '0') {
			this.prefix = pattern.substring(0, i);
			pattern = pattern.substring(i);
			break;
		}
	}

	// get suffix
	this.suffix = pattern.replace(/[#]|[0]|[,]|[.]/g, '');

	// get number as string
	var snum = pattern.replace(/[^0#,.]/g, '');

	var sint = '',
		frac = '',
		point = snum.indexOf('.');
	if (point != -1) {
		sint = snum.substring(0, point);
		frac = snum.substring(point + 1);
	} else {
		sint = snum;
	}

	var comma = sint.lastIndexOf(',');
	if (comma != -1) {
		this.comma = sint.length - 1 - comma;
	}

	sint = sint.replace(/[,]/g, ''); // remove commas

	frac = frac.replace(/[,]|[.]+/g, '');

	this.maxFrac = frac.length;
	var tmp = sint.replace(/[^0]/g, ''); // remove all except zero
	if (tmp.length > this.minInt) {
		this.minInt = tmp.length;
	}
	tmp = frac.replace(/[^0]/g, '');
	this.minFrac = tmp.length;
}

(function() {
	"use strict";

	/**
	 * @description Formats given value
	 * @methodOf DecimalFormat
	 * @param {String} num
	 * @return {String} Formatted number
	 * @author Oskan Savli
	 */
	DecimalFormat.prototype.format = function(num) {
		// remove prefix, suffix and commas
		var snum = this.parse(num).toLowerCase();

		// do not format if not a number
		if (isNaN(snum) || snum.length == 0) {
			return num;
		}

		// scientific numbers
		if (i = snum.indexOf("e") != -1) {
			var n = Number(snum);
			if (n == "Infinity" || n == "-Infinity") {
				return snum;
			}

			snum = n + "";
			if (snum.indexOf('e') != -1) {
				return snum;
			}
		}

		// remove sign
		var negative = false;
		if (snum.charAt(0) == '-') {
			negative = true;
			snum = snum.substring(1);
		} else if (snum.charAt(0) == '+') {
			snum = snum.substring(1);
		}

		var point = snum.indexOf('.'); // position of point character
		var sint = '';
		var frac = '';
		if (point != -1) {
			sint = snum.substring(0, point);
			frac = snum.substring(point + 1);
		} else {
			sint = snum;
		}
		frac = frac.replace(/[.]/, ''); // remove other point characters

		var isPercentage = this.suffix && this.suffix.charAt(0) === '%';
		// if percentage, number will be multiplied by 100.
		var minInt = this.minInt, minFrac = this.minFrac, maxFrac = this.maxFrac;
		if (isPercentage) {
			minInt -= 2;
			minFrac += 2;
			maxFrac += 2;
		}

		if (frac.length > maxFrac) { // round
			// case 6143
			var num = new Number('0.' + frac);
			num = (maxFrac == 0) ? Math.round(num) : num.toFixed(maxFrac);
			// toFixed method has bugs on IE (0.7 --> 0)
			frac = num.toString(10).substring(2);
			var c = (num >= 1) ? 1 : 0; // carry
			var x, i = sint.length - 1;
			while (c) { // increment sint
				if (i == -1) {
					sint = '1' + sint;
					break;
				}

				x = sint.charAt(i);
				if (x == 9) {
					x = '0';
					c = 1;
				} else {
					x = (++x) + '';
					c = 0;
				}
				sint = sint.substring(0, i) + x + sint.substring(i + 1, sint.length);
				i--;
			}
		}

		for (var i = frac.length; i < minFrac; i++) {
			// if minFrac=4 then 1.12 --> 1.1200
			frac = frac + '0';
		}

		while (frac.length > minFrac && frac.charAt(frac.length - 1) == '0') {
			// if minInt=4 then 00034 --> 0034)
			frac = frac.substring(0, frac.length - 1);
		}

		for (var i = sint.length; i < minInt; i++) {
			// if minInt=4 then 034 --> 0034
			sint = '0' + sint;
		}

		while (sint.length > minInt && sint.charAt(0) == '0') {
			// if minInt=4 then 00034--> 0034)
			sint = sint.substring(1);
		}

		if (isPercentage) {
			// multiply by 100
			sint += frac.substring(0, 2);
			frac = frac.substring(2);
		}

		var j = 0;
		for (var i = sint.length; i > 0; i--) {
			// add commas
			if (j != 0 && j % this.comma == 0) {
				sint = sint.substring(0, i) + ',' + sint.substring(i);
				j = 0;
			}
			j++;
		}

		var result = this.prefix + sint + (frac.length > 0 ? '.' + frac : '') + this.suffix;
		if (negative) {
			result = '-' + result;
		}

		return result;
	}


	/**
	 * @description Converts formatted value back to non-formatted value
	 * @methodOf DecimalFormat
	 * @param {String} snum Formatted number
	 * @return {String} Original number
	 * @author Oskan Savli
	 */
	DecimalFormat.prototype.parse = function(snum) {
		// $1,223.06 --> 1223.06
		snum += ''; // ensure it is string
		if (!snum) {
			return ''; // do not return undefined or null
		}

		if (!isNaN(snum)) {
			return this.getNumericString(snum);
		}

		var anum = snum, negative = false;
		if (snum.charAt(0) == '-') {
			anum = anum.substring(1);
			negative = true;
		}

		var pIndex = anum.indexOf(this.prefix);
		var sIndex = (this.suffix == '') ? anum.length : anum.indexOf(this.suffix, this.prefix.length + 1);

		if (pIndex == 0 && sIndex > 0) {
			// remove suffix
			anum = anum.substring(0, sIndex);
			// remove prefix
			anum = anum.substring(this.prefix.length);
			// remove commas
			anum = anum.replace(/,/g, '');
			if (negative) {
				anum = '-' + anum;
			}
			if (!isNaN(anum)) {
				return this.getNumericString(anum);
			}
		}
		return snum;
	}

	/**
	 * @description We shouldn't return strings like 1.000 in parse method.
	 *              However, using only Number(str) is not enough, because it omits .
	 *              in big numbers like 23423423423342234.34 => 23423423423342236 .
	 *              There's a conflict in cases 6143 and 6541.
	 * @methodOf DecimalFormat
	 * @param {String}  str Numberic string
	 * @return {String} Corrected numeric string
	 * @author Serdar Bicer
	 */
	DecimalFormat.prototype.getNumericString = function(str) {
		// first convert to number string
		var snum = '' + new Number(str);

		// check if there is a missing dot
		if (str.indexOf('.') > -1 && snum.indexOf('.') < 0) {
			// check if original string has all zeros after dot or not
			for (var i = str.indexOf('.') + 1; i < str.length; i++) {
				// if not, this means we lost precision
				if (str.charAt(i) !== '0') {
					return str;
				}
			}
			return snum;
		}
		return str;
	}

	//--------------------------------------------------
	if (typeof Number.trim != "function") {
		Number.trim = function(s) {
			if (typeof (s) != 'string') {
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

	if (typeof Number.format != "function") {
		Number.format = function(pattern, n) {
			return (new DecimalFormat(pattern)).format(n);
		};
	}

	if (typeof Number.comma != "function") {
		var CDF = new DecimalFormat('###,###.#########');
		Number.comma = function(n) {
			return CDF.format(n);
		};
	}

	if (typeof Number.humanSize != "function") {
		var UNITS = [ "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" ];

		Number.humanSize = function(n, p) {
			var i = 0, l = UNITS.length - 1;
			while (n >= 1024 && i < l) {
				n /= 1024
				i++
			}

			p = Math.pow(10, p || 2);
			return Math.round(n * p) / p + UNITS[i];
		};
	}
})();

(function() {
	"use strict";

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
		var re = /^[\s\u3000]+/;
		String.prototype.stripLeft = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.stripRight != "function") {
		var re = /[\s\u3000]+$/;
		String.prototype.stripRight = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.strip != "function") {
		var re = /^[\s\u3000]+|[\s\u3000]+$/g;
		String.prototype.strip = function() {
			return this.replace(re, "");
		};
	}
	if (typeof String.prototype.left != "function") {
		String.prototype.left = function(len) {
			return this.slice(0, len);
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
		String.prototype.leftPad = function(sz, ch) {
			ch = ch || ' ';
			var r = this;
			while (r.length < sz) {
				r = ch + r;
			}
			return r;
		};
	}
	if (typeof String.prototype.rightPad != "function") {
		String.prototype.rightPad = function(sz, ch) {
			ch = ch || ' ';
			var r = this;
			while (r.length < sz) {
				r += ch;
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
			return this.charAt(0).toUpperCase() + this.slice(1);
		};
	}
	if (typeof String.prototype.uncapitalize != "function") {
		String.prototype.uncapitalize = function() {
			return this.charAt(0).toLowerCase() + this.slice(1);
		};
	}


	if (typeof String.prototype.snakeCase != "function") {
		String.prototype.snakeCase = function(c) {
			c = c || '_';
			var s = this.camelCase();
			return s.replace(/[A-Z]/g, function(m) {
				return c + m.charAt(0).toLowerCase();
			});
		};
	}
	if (typeof String.prototype.camelCase != "function") {
		String.prototype.camelCase = function() {
			s = this.charAt(0).toLowerCase() + this.slice(1);
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
					return this.slice(0, i) + '...';
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
	if (typeof String.prototype.prettifyXML != "function") {
		String.prototype.prettifyXML = function() {
			var xml = '';
			var pad = 0;
			var ss = this.replace(/(>)(<)(\/*)/g, '$1\r\n$2$3').split('\r\n');
			for (var i = 0; i < ss.length; i++) {
				var s = ss[i];
				var indent = 0;
				if (s.match(/.+<\/\w[^>]*>$/)) {
				}
				else if (s.match(/^<\/\w/)) {
					if (pad > 0) {
						pad -= 1;
					}
				}
				else if (s.match(/^<\w[^>]*[^\/]*>.*$/)) {
					indent = 1;
				}

				xml += ('').leftPad(pad * 2) + s + '\r\n';
				pad += indent;
			}

			return xml;
		};
	}
	if (typeof String.prototype.encodeUTF8 != "function") {
		String.prototype.encodeUTF8 = function() {
			string = this.replace(/rn/g, "\n");
			var utftext = "";

			for (var n = 0; n < string.length; n++) {

				var c = string.charCodeAt(n);

				if (c < 128) {
					utftext += String.fromCharCode(c);
				}
				else if ((c > 127) && (c < 2048)) {
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
	if (typeof String.prototype.decodeUTF8 != "function") {
		String.prototype.decodeUTF8 = function() {
			var s = "", i = 0, c = 0, c2 = 0;

			while (i < this.length) {
				c = this.charCodeAt(i);

				if (c < 128) {
					s += String.fromCharCode(c);
					i++;
				} else if (c > 191 && c < 224) {
					c2 = this.charCodeAt(i + 1);
					s += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
					i += 2;
				} else {
					c2 = this.charCodeAt(i + 1);
					c3 = this.charCodeAt(i + 2);
					s += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
					i += 3;
				}
			}

			return s;
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
			while (this.length >= i + 3) {
				bits = (this.charCodeAt(i++) & 0xff) << 16 | (this.charCodeAt(i++) & 0xff) << 8 | this.charCodeAt(i++) & 0xff;
				encOut += base64s.charAt((bits & 0x00fc0000) >> 18) + base64s.charAt((bits & 0x0003f000) >> 12) + base64s.charAt((bits & 0x00000fc0) >> 6) + base64s.charAt((bits & 0x0000003f));
			}
			if (this.length - i > 0 && this.length - i < 3) {
				dual = Boolean(this.length - i - 1);
				bits = ((this.charCodeAt(i++) & 0xff) << 16) | (dual ? (this.charCodeAt(i) & 0xff) << 8 : 0);
				encOut += base64s.charAt((bits & 0x00fc0000) >> 18) + base64s.charAt((bits & 0x0003f000) >> 12) + (dual ? base64s.charAt((bits & 0x00000fc0) >> 6) : '=') + '=';
			}
			return (encOut);
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
				bits = (base64s.indexOf(this.charAt(i)) & 0xff) << 18 | (base64s.indexOf(this.charAt(i + 1)) & 0xff) << 12 | (base64s.indexOf(this.charAt(i + 2)) & 0xff) << 6 | base64s.indexOf(this.charAt(i + 3)) & 0xff;
				decOut += String.fromCharCode((bits & 0xff0000) >> 16, (bits & 0xff00) >> 8, bits & 0xff);
			}
			if (this.charCodeAt(i - 2) == 61) {
				return (decOut.slice(0, decOut.length - 2));
			}
			else if (this.charCodeAt(i - 1) == 61) {
				return (decOut.slice(0, decOut.length - 1));
			}
			else {
				return (decOut);
			}
		};
	}
})();

