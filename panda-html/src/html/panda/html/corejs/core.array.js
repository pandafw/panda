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
