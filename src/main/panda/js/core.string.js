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
		return this.escapeHtml().replace(/\n/g, "<br/>");
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

