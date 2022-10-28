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

