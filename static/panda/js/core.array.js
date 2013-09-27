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
