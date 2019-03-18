(function($) {
	function createIFrame(s) {
		var id = "ajaf_if_" + s.id;
		return $('<iframe id="' + id + '" name="' + id + '" src="' + s.secureUrl + '"></iframe>')
			.css({
				position: 'absolute',
				top: '-9999px',
				left: '-9999px'
			})
			.appendTo('body');
	}
	
	function createForm(s) {
		var id = 'ajaf_form_' + s.id;

		var $form = $('<form></form>')
			.attr({
				id: id,
				name: id,
				action: s.url,
				method: 'POST',
				target: 'ajaf_if_' + s.id
			})
			.css({
				position: 'absolute',
				top: '-9999px',
				left: '-9999px'
			})
			.appendTo('body');

		$form.files = [];
		
		function addFile($f, n) {
			var $c = $f.clone();
			$c.insertAfter($f);

			n = n || $f.attr('name');
			$f.attr({
				id: '',
				name: n
			})
			.appendTo($form);
			
			$form.files[$form.files.length] = { real: $f, clon: $c};
		}
		
		if (s.file) {
			$form.attr({
				encoding: 'multipart/form-data',
				enctype: 'multipart/form-data'
			});

			if (typeof(s.file) == "string") {
				addFile($(s.file));
			}
			else if ($.isArray(s.file)) {
				for (var i = 0; i < s.file.length; i++) {
					addFile($(s.file[i]));
				}
			}
			else {
				for (var n in s.file) {
					addFile($(s.file[n]), n);
				}
			}
		}
		
		function addParam(n, v) {
			$('<input type="hidden">')
				.attr('name', n)
				.val(v)
				.appendTo($form);
		}

		function addParams(n, v) {
			if ($.isArray(v)) {
				for (var i = 0; i < v.length; i++) {
					addParam(n, v[i]);
				}
			}
			else {
				addParam(n, v);
			}
		}

		if (s.data) {
			if ($.isArray(s.data)) {
				for (var i = 0; i < s.data.length; i++) {
					addParams(s.data[i].name, s.data[i].value);
				}
			}
			else {
				for (var n in s.data) {
					var v = s.data[n];
					if (v) {
						addParams(n, v)
					}
				}
			}
		}

		return $form;
	}

	function httpData(xhr, type) {
		var data = type == "xml" ? xhr.responseXML : xhr.responseText;
		
		if (type == "script") {
			// If the type is "script", eval it in global context
			$.globalEval(data);
		}
		else if (type == "json") {
			// Get the JavaScript object, if JSON is used.
			data = $.parseJSON(data);
		}
		else if (type == "html") {
			// evaluate scripts within html
			$("<div>").html(data).evalScripts();
		}
		
		return data;
	}

	$.ajaf = function(s) {
		// TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
		s = $.extend({
				id: new Date().getTime(),
				secureUrl: 'javascript:false'
			}, s);
		
		var $if = createIFrame(s);
		var $form = createForm(s);
		
		// Watch for a new set of requests
		if (s.start) {
			s.start();
		}

		var done = false, xhr = {};

		// Wait for a response to come back
		var callback = function(timeout) {
			if (done) {
				return;
			}
			done = true;

			var status = timeout == "timeout" ? "error" : "success";
			try {
				var ioe = $if.get(0);
				var	doc = ioe.contentWindow.document || ioe.contentDocument || window.frames[ioe.id].document;
				if (doc && doc.body) {
					if (s.selector) {
						xhr.responseText = $(doc.body).find(s.selector).html();
					}
					else {
						var fc = doc.body.firstChild;
						var tn = (fc && fc.tagName) ? fc.tagName.toUpperCase() : "";
						if (tn == "TEXTAREA") {
							xhr.responseText = fc.value;
						}
						else if (tn == "PRE") {
							xhr.responseText = $(fc).text();
						}
						else {
							xhr.responseText = doc.body.innerHTML;
						}
					}
				}
				xhr.responseXML = (doc && doc.XMLDocument) ? doc.XMLDocument : doc;

				if (typeof(console) != "undefined") {
					console.debug("jquery.ajaf(" + s.url + "): " + (xhr.responseText || xhr.responseXML));
				}
			}
			catch(e) {
				status = "error";
				if (s.error) {
					s.error(xhr, status, e);
				}
			}

			// Revert files
			for (var i = 0; i < $form.files.length; i++) {
				var f = $form.files[i];
				f.real.attr({
					id: f.clon.attr('id'),
					name: f.clon.attr('name')
				}).insertAfter(f.clon);
				f.clon.remove();
			}
			$form.remove();	

			if (status == "timeout") {
				if (s.error) {
					s.error(xhr, status);
				}
			}
			else if (status == "success") {
				// Make sure that the request was successful or not modified
				try {
					// process the data (runs the xhr through httpData regardless of callback)
					var data = httpData(xhr, s.dataType);

					// If a local callback was specified, fire it and pass it the data
					if (s.success) {
						s.success(data, xhr);
					}
				}
				catch(e) {
					if (s.error) {
						s.error(xhr, status, e);
					}
				}
			}

			try {
				// The request was completed
				if (s.complete) {
					s.complete(xhr, status);
				}
			}
			finally {
				//clear up the created iframe after file uploaded.
				$if.unbind();
				setTimeout(function() {
					try {
						$if.remove();
					}
					catch(e) {
						if (typeof(console) != "undefined") {
							console.debug("jquery.ajaf(" + s.url + "): failed to remove " + $if);
						}
					}
				}, 100);
				xhr = null;
			}
		};
		
		if (s.send) {
			s.send(xhr, s);
		}

		// Timeout checker
		if (s.timeout > 0) {
			setTimeout(function(){
				// Check to see if the request is still happening
				if (!done) {
					callback("timeout");
				}
			}, s.timeout);
		}
		
		try {
			$form.submit();
		}
		catch(e) {
			if (s.error) {
				s.error(xhr, "send", e);
			}
		}
		
		$if.load(callback);

		return { abort: function(){} };	
	};
})(jQuery);

(function($) {
	// Use of jQuery.browser is frowned upon.
	// More details: http://api.jquery.com/jQuery.browser
	// jQuery.uaMatch maintained for back-compat
	$.uaMatch = function( ua ) {
		ua = ua.toLowerCase();

		var m = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
			/(webkit)[ \/]([\w.]+)/.exec( ua ) ||
			/(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
			/(msie) ([\w.]+)/.exec( ua ) ||
			ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
			[];

		return {
			b: m[ 1 ] || "",
			v: m[ 2 ] || "0"
		};
	};

	var m = $.uaMatch( navigator.userAgent );
	var b = { version: 0 };

	if (m.b) {
		b[m.b] = true;
		b.version = m.v;
	}

	// Chrome is Webkit, but Webkit is also Safari.
	if (b.chrome) {
		b.webkit = true;
	}
	else if (b.webkit) {
		b.safari = true;
	}

	$.browser = $.extend(b, {
		width: function() {
			return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
		},
		height: function() {
			return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
		},
		ipad: /ipad/i.test(navigator.userAgent),
		ipod: /ipod/i.test(navigator.userAgent),
		iphone: /iphone/i.test(navigator.userAgent),
		ios: /iphone|ipad|ipod/i.test(navigator.userAgent),
		android: /android/i.test(navigator.userAgent),
		majorVersion: parseInt(b.version, 10)
	});
})(jQuery);
(function($) {
	$.copyToClipboard = function(s) {
		if (window.clipboardData) {
			// ie
			clipboardData.setData('Text', s);
			return;
		}

		var $t = $('<textarea>').css({ 'width' : '0px', 'height': '0px' }).html(s.escapeHtml());
		var l = $t.val().length;
		var t = $t.get(0);
		
		t.setSelectionRange(0, l);
		document.execCommand('copy');
		t.blur();
	}
})(jQuery);

/**
 * Cookie plugin
 *
 * Copyright (c) 2006 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */

/**
 * Create a cookie with the given name and value and other optional parameters.
 *
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Set the value of a cookie.
 * @example $.cookie('the_cookie', 'the_value', { expires: 7, path: '/', domain: 'jquery.com', secure: true });
 * @desc Create a cookie with all available options.
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Create a session cookie.
 * @example $.cookie('the_cookie', null);
 * @desc Delete a cookie by passing null as value. Keep in mind that you have to use the same path and domain
 *       used when the cookie was set.
 *
 * @param String name The name of the cookie.
 * @param String value The value of the cookie.
 * @param Object options An object literal containing key/value pairs to provide optional cookie attributes.
 * @option Number|Date expires Either an integer specifying the expiration date from now on in days or a Date object.
 *                             If a negative value is specified (e.g. a date in the past), the cookie will be deleted.
 *                             If set to null or omitted, the cookie will be a session cookie and will not be retained
 *                             when the the browser exits.
 * @option String path The value of the path atribute of the cookie (default: path of page that created the cookie).
 * @option String domain The value of the domain attribute of the cookie (default: domain of page that created the cookie).
 * @option Boolean secure If true, the secure attribute of the cookie will be set and the cookie transmission will
 *                        require a secure protocol (like HTTPS).
 * @type undefined
 *
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */

/**
 * Get the value of a cookie with the given name.
 *
 * @example $.cookie('the_cookie');
 * @desc Get the value of a cookie.
 *
 * @param String name The name of the cookie.
 * @return The value of the cookie.
 * @type String
 *
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */
jQuery.cookie = function(name, value, options) {
	options = $.extend({}, $.cookie.defaults, options);
	if (typeof value != 'undefined') { // name and value given, set cookie
		if (value === null) {
			value = '';
			options.expires = -1;
		}
		var expires = '';
		if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
			var date;
			if (typeof options.expires == 'number') {
				date = new Date();
				date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
			}
			else {
				date = options.expires;
			}
			expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
		}
		// NOTE Needed to parenthesize options.path and options.domain
		// in the following expressions, otherwise they evaluate to undefined
		// in the packed version for some reason...
		var path = options.path ? '; path=' + (options.path) : '';
		var domain = options.domain ? '; domain=' + (options.domain) : '';
		var secure = options.secure ? '; secure' : '';
		document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
	}
	else { // only name given, get cookie
		var cookieValue = null;
		if (document.cookie && document.cookie != '') {
			var cookies = document.cookie.split(';');
			for (var i = 0; i < cookies.length; i++) {
				var cookie = cookies[i].replace(/^[\s\u3000\u0022]+|[\s\u3000\u0022]+$/g, '');
				// Does this cookie string begin with the name we want?
				if (cookie.substring(0, name.length + 1) == (name + '=')) {
					cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
					break;
				}
			}
		}
		return cookieValue;
	}
};
jQuery.cookie.defaults = {};

jQuery.jcookie = function(name, value, options) {
	if (typeof value != 'undefined') { // name and value given, set cookie
		$.cookie(name, String.encodeBase64(JSON.stringify(value)), options);
	}
	else {
		try {
			return JSON.parse(String.decodeBase64($.cookie(name)));
		}
		catch (ex) {
			return {};
		}
	}
};

(function ($) {
	$.fn.disable = function(state) {
		return this.each(function() {
			this.disabled = state;
		});
	};
})(jQuery);
(function($) {
	$.jcss = function(url) {
		if ($('link[href="' + url + '"]').size()) {
			return false;
		}
		$('<link>').attr({ type: 'text/css', rel: 'stylesheet', href: url}).appendTo('head');
		return true;
	};
})(jQuery);

(function($) {
	$.jscript = function(url, async) {
		if ($('script[src="' + url + '"]').size()) {
			return false;
		}

		var s = document.createElement('script');
		s.type = 'text/javascript';
		s.async = async;
		s.src = url;
		$('body').append(s);
		return true;
	};
	
	// enable script cache
	$.enableScriptCache = function() {
		$.ajaxPrefilter(function(options, org, xhr) {
			if (options.dataType == 'script' || org.dataType == 'script') {
				options.cache = true;
			}
		});
	}
})(jQuery);

(function($) {
	$.queryArrays = function(s, f) {
		var qs = [], i = s.indexOf('#');
		if (i >= 0) {
			s = s.substring(0, i);
		}

		i = s.indexOf('?');
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

	$.queryParams = function(s) {
		var qs = {}, i = s.indexOf('#');
		if (i >= 0) {
			s = s.substring(0, i);
		}
		
		i = s.indexOf('?');
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

	$.addQueryParams = function(u, p) {
		var i = u.indexOf('#');
		if (i >= 0) {
			u = u.substring(0, i);
		}

		i = u.indexOf('?');
		if (i >= 0) {
			p = $.extend($.queryParams(u), p);
			u = u.substring(0, i);
		}
		return u + '?' + $.param(p);
	};
	
})(jQuery);


/*!
	Autosize 1.18.18
	license: MIT
	http://www.jacklmoore.com/autosize
*/
(function ($) {
	var
	defaults = {
		className: 'autosizejs',
		id: 'autosizejs',
		append: '\n',
		callback: false,
		resizeDelay: 10,
		placeholder: true
	},

	// line-height is conditionally included because IE7/IE8/old Opera do not return the correct value.
	typographyStyles = [
		'fontFamily',
		'fontSize',
		'fontWeight',
		'fontStyle',
		'letterSpacing',
		'textTransform',
		'wordSpacing',
		'textIndent',
		'whiteSpace'
	],

	// to keep track which textarea is being mirrored when adjust() is called.
	mirrored,

	// the mirror element, which is used to calculate what size the mirrored element should be.
	mirror = $('<textarea aria-hidden="true" tabindex="-1"/>').data('autosize', true)[0];

	// border:0 is unnecessary, but avoids a bug in Firefox on OSX
	mirror.style.cssText = "position:absolute; top:-999px; left:0; right:auto; bottom:auto; border:0; padding: 0; -moz-box-sizing:content-box; -webkit-box-sizing:content-box; box-sizing:content-box; word-wrap:break-word; height:0 !important; min-height:0 !important; overflow:hidden; transition:none; -webkit-transition:none; -moz-transition:none;";

	// test that line-height can be accurately copied.
	mirror.style.lineHeight = '99px';
	if ($(mirror).css('lineHeight') === '99px') {
		typographyStyles.push('lineHeight');
	}
	mirror.style.lineHeight = '';

	$.fn.autosize = function (options) {
		if (!this.length) {
			return this;
		}

		options = $.extend({}, defaults, options || {});

		if (mirror.parentNode !== document.body) {
			$(document.body).append(mirror);
		}

		return this.each(function () {
			var
			ta = this,
			$ta = $(ta),
			maxHeight,
			minHeight,
			boxOffset = 0,
			callback = $.isFunction(options.callback),
			originalStyles = {
				height: ta.style.height,
				overflow: ta.style.overflow,
				overflowY: ta.style.overflowY,
				wordWrap: ta.style.wordWrap,
				resize: ta.style.resize
			},
			timeout,
			width = $ta.width(),
			taResize = $ta.css('resize');

			if ($ta.data('autosize')) {
				// exit if autosize has already been applied, or if the textarea is the mirror element.
				return;
			}
			$ta.data('autosize', true);

			if ($ta.css('box-sizing') === 'border-box' || $ta.css('-moz-box-sizing') === 'border-box' || $ta.css('-webkit-box-sizing') === 'border-box'){
				boxOffset = $ta.outerHeight() - $ta.height();
			}

			// IE8 and lower return 'auto', which parses to NaN, if no min-height is set.
			minHeight = Math.max(parseFloat($ta.css('minHeight')) - boxOffset || 0, $ta.height());

			$ta.css({
				overflow: 'hidden',
				overflowY: 'hidden',
				wordWrap: 'break-word' // horizontal overflow is hidden, so break-word is necessary for handling words longer than the textarea width
			});

			if (taResize === 'vertical') {
				$ta.css('resize','none');
			} else if (taResize === 'both') {
				$ta.css('resize', 'horizontal');
			}

			// getComputedStyle is preferred here because it preserves sub-pixel values, while jQuery's .width() rounds to an integer.
			function setWidth() {
				var width;
				var style = window.getComputedStyle ? window.getComputedStyle(ta, null) : null;

				if (style) {
					width = parseFloat(style.width);
					if (style.boxSizing === 'border-box' || style.webkitBoxSizing === 'border-box' || style.mozBoxSizing === 'border-box') {
						$.each(['paddingLeft', 'paddingRight', 'borderLeftWidth', 'borderRightWidth'], function(i,val){
							width -= parseFloat(style[val]);
						});
					}
				} else {
					width = $ta.width();
				}

				mirror.style.width = Math.max(width,0) + 'px';
			}

			function initMirror() {
				var styles = {};

				mirrored = ta;
				mirror.className = options.className;
				mirror.id = options.id;
				maxHeight = parseFloat($ta.css('maxHeight'));

				// mirror is a duplicate textarea located off-screen that
				// is automatically updated to contain the same text as the
				// original textarea.  mirror always has a height of 0.
				// This gives a cross-browser supported way getting the actual
				// height of the text, through the scrollTop property.
				$.each(typographyStyles, function(i,val){
					styles[val] = $ta.css(val);
				});

				$(mirror).css(styles).attr('wrap', $ta.attr('wrap'));

				setWidth();

				// Chrome-specific fix:
				// When the textarea y-overflow is hidden, Chrome doesn't reflow the text to account for the space
				// made available by removing the scrollbar. This workaround triggers the reflow for Chrome.
				if (window.chrome) {
					var width = ta.style.width;
					ta.style.width = '0px';
					var ignore = ta.offsetWidth;
					ta.style.width = width;
				}
			}

			// Using mainly bare JS in this function because it is going
			// to fire very often while typing, and needs to very efficient.
			function adjust() {
				var height, originalHeight;

				if (mirrored !== ta) {
					initMirror();
				} else {
					setWidth();
				}

				if (!ta.value && options.placeholder) {
					// If the textarea is empty, copy the placeholder text into
					// the mirror control and use that for sizing so that we
					// don't end up with placeholder getting trimmed.
					mirror.value = ($ta.attr("placeholder") || '');
				} else {
					mirror.value = ta.value;
				}

				mirror.value += options.append || '';
				mirror.style.overflowY = ta.style.overflowY;
				originalHeight = parseFloat(ta.style.height) || 0;

				// Setting scrollTop to zero is needed in IE8 and lower for the next step to be accurately applied
				mirror.scrollTop = 0;

				mirror.scrollTop = 9e4;

				// Using scrollTop rather than scrollHeight because scrollHeight is non-standard and includes padding.
				height = mirror.scrollTop;

				if (maxHeight && height > maxHeight) {
					ta.style.overflowY = 'scroll';
					height = maxHeight;
				} else {
					ta.style.overflowY = 'hidden';
					if (height < minHeight) {
						height = minHeight;
					}
				}

				height += boxOffset;

				if (Math.abs(originalHeight - height) > 1/100) {
					ta.style.height = height + 'px';

					// Trigger a repaint for IE8 for when ta is nested 2 or more levels inside an inline-block
					mirror.className = mirror.className;

					if (callback) {
						options.callback.call(ta,ta);
					}
					$ta.trigger('autosize.resized');
				}
			}

			function resize () {
				clearTimeout(timeout);
				timeout = setTimeout(function(){
					var newWidth = $ta.width();

					if (newWidth !== width) {
						width = newWidth;
						adjust();
					}
				}, parseInt(options.resizeDelay,10));
			}

			if ('onpropertychange' in ta) {
				if ('oninput' in ta) {
					// Detects IE9.  IE9 does not fire onpropertychange or oninput for deletions,
					// so binding to onkeyup to catch most of those occasions.  There is no way that I
					// know of to detect something like 'cut' in IE9.
					$ta.on('input.autosize keyup.autosize', adjust);
				} else {
					// IE7 / IE8
					$ta.on('propertychange.autosize', function(){
						if(event.propertyName === 'value'){
							adjust();
						}
					});
				}
			} else {
				// Modern Browsers
				$ta.on('input.autosize', adjust);
			}

			// Set options.resizeDelay to false if using fixed-width textarea elements.
			// Uses a timeout and width check to reduce the amount of times adjust needs to be called after window resize.

			if (options.resizeDelay !== false) {
				$(window).on('resize.autosize', resize);
			}

			// Event for manual triggering if needed.
			// Should only be needed when the value of the textarea is changed through JavaScript rather than user input.
			$ta.on('autosize.resize', adjust);

			// Event for manual triggering that also forces the styles to update as well.
			// Should only be needed if one of typography styles of the textarea change, and the textarea is already the target of the adjust method.
			$ta.on('autosize.resizeIncludeStyle', function() {
				mirrored = null;
				adjust();
			});

			$ta.on('autosize.destroy', function(){
				mirrored = null;
				clearTimeout(timeout);
				$(window).off('resize', resize);
				$ta
					.off('autosize')
					.off('.autosize')
					.css(originalStyles)
					.removeData('autosize');
			});

			// Call adjust in case the textarea already contains text.
			adjust();
		});
	};
	
	// ==================
	$(window).on('load', function () {
		$('textarea[autosize="true"]').autosize();
	});

}(jQuery || $)); // jQuery or jQuery-like library, such as Zepto
(function($) {
	/**
	 * Move the selected element(s) to the center position of parent element (default: body)
	 */
	$.fn.center = function(c) {
		c = c || {};
		return this.each(function() {
			var $s = $(this);
				$t = c.target ? $(c.target) : $s.parent();

			var tl = $t.scrollLeft(),
				tt = $t.scrollTop(),
				tw = $t.width(),
				th = $t.height(),
				ow = $s.outerWidth(),
				oh = $s.outerHeight();
			
			try {
				if (c.relative === false || (c.relative !== true && $t.css('position') != 'relative')) {
					var r = $t.offset();
					tl = r.left;
					tt = r.top;
				}
			}
			catch (e) {
			}

			var ot = Math.round(tt + (th - oh) / 2),
				ol = Math.round(tl + (tw - ow) / 2);
			$s.css({
				top: ot + "px",
				left: ol + "px"
			});
		});
	};
})(jQuery);
/**
 * jQuery Editable Select
 * Indri Muska <indrimuska@gmail.com>
 *
 * Source on GitHub @ https://github.com/indrimuska/jquery-editable-select
 */

+(function ($) {
	// jQuery Editable Select
	EditableSelect = function (select, options) {
		var that     = this;
		
		this.options = options;
		this.$select = $(select);
		this.$input  = $('<input type="text" autocomplete="off">');
		this.$list   = $('<ul class="es-list">');
		this.utility = new EditableSelectUtility(this);
		
		if (['focus', 'manual'].indexOf(this.options.trigger) < 0) this.options.trigger = 'focus';
		if (['default', 'fade', 'slide'].indexOf(this.options.effects) < 0) this.options.effects = 'default';
		if (isNaN(this.options.duration) && ['fast', 'slow'].indexOf(this.options.duration) < 0) this.options.duration = 'fast';
		
		// create text input
		this.$select.replaceWith(this.$input);
		this.$list.appendTo(this.options.appendTo || this.$input.parent());
		
		// initalization
		this.utility.initialize();
		this.utility.initializeList();
		this.utility.initializeInput();
		this.utility.trigger('created');
	}
	EditableSelect.DEFAULTS = { filter: true, effects: 'default', duration: 'fast', trigger: 'focus' };
	EditableSelect.prototype.filter = function () {
		var hiddens = 0;
		var search  = this.$input.val().toLowerCase().trim();
		
		this.$list.find('li').addClass('es-visible').show();
		if (this.options.filter) {
			hiddens = this.$list.find('li').filter(function (i, li) { return $(li).text().toLowerCase().indexOf(search) < 0; }).hide().removeClass('es-visible').length;
			if (this.$list.find('li').length == hiddens) this.hide();
		}
	};
	EditableSelect.prototype.show = function () {
		this.$list.css({
			top:   this.$input.position().top + this.$input.outerHeight() - 1,
			left:  this.$input.position().left,
			width: this.$input.outerWidth()
		});
		
		if (!this.$list.is(':visible') && this.$list.find('li.es-visible').length > 0) {
			var fns = { 'default': 'show', fade: 'fadeIn', slide: 'slideDown' };
			var fn  = fns[this.options.effects];
			
			this.utility.trigger('show');
			this.$input.addClass('open');
			this.$list[fn](this.options.duration, $.proxy(this.utility.trigger, this.utility, 'shown'));
		}
	};
	EditableSelect.prototype.hide = function () {
		var fns = { 'default': 'hide', fade: 'fadeOut', slide: 'slideUp' };
		var fn  = fns[this.options.effects];
		
		this.utility.trigger('hide');
		this.$input.removeClass('open');
		this.$list[fn](this.options.duration, $.proxy(this.utility.trigger, this.utility, 'hidden'));
	};
	EditableSelect.prototype.select = function ($li) {
		if (!this.$list.has($li) || !$li.is('li.es-visible:not([disabled])')) return;
		this.$input.val($li.text());
		if (this.options.filter) this.hide();
		this.filter();
		this.utility.trigger('select', $li);
	};
	EditableSelect.prototype.add = function (text, index, attrs, data) {
		var $li     = $('<li>').html(text);
		var $option = $('<option>').text(text);
		var last    = this.$list.find('li').length;
		
		if (isNaN(index)) index = last;
		else index = Math.min(Math.max(0, index), last);
		if (index == 0) {
		  this.$list.prepend($li);
		  this.$select.prepend($option);
		} else {
		  this.$list.find('li').eq(index - 1).after($li);
		  this.$select.find('option').eq(index - 1).after($option);
		}
		this.utility.setAttributes($li, attrs, data);
		this.utility.setAttributes($option, attrs, data);
		this.filter();
	};
	EditableSelect.prototype.remove = function (index) {
		var last = this.$list.find('li').length;
		
		if (isNaN(index)) index = last;
		else index = Math.min(Math.max(0, index), last - 1);
		this.$list.find('li').eq(index).remove();
		this.$select.find('option').eq(index).remove();
		this.filter();
	};
	EditableSelect.prototype.clear = function () {
		this.$list.find('li').remove();
		this.$select.find('option').remove();
		this.filter();
	};
	EditableSelect.prototype.destroy = function () {
		this.$list.off('mousemove mousedown mouseup');
		this.$input.off('focus blur input keydown');
		this.$input.replaceWith(this.$select);
		this.$list.remove();
		this.$select.removeData('editable-select');
	};
	
	// Utility
	EditableSelectUtility = function (es) {
		this.es = es;
	}
	EditableSelectUtility.prototype.initialize = function () {
		var that = this;
		that.setAttributes(that.es.$input, that.es.$select[0].attributes, that.es.$select.data());
		that.es.$input.addClass('es-input').data('editable-select', that.es);
		that.es.$select.find('option').each(function (i, option) {
			var $option = $(option).remove();
			that.es.add($option.text(), i, option.attributes, $option.data());
			if ($option.attr('selected')) that.es.$input.val($option.text());
		});
		that.es.filter();
	};
	EditableSelectUtility.prototype.initializeList = function () {
		var that = this;
		that.es.$list
			.on('mousemove', 'li:not([disabled])', function () {
				that.es.$list.find('.selected').removeClass('selected');
				$(this).addClass('selected');
			})
			.on('mousedown', 'li', function (e) {
				if ($(this).is('[disabled]')) e.preventDefault();
				else that.es.select($(this));
			})
			.on('mouseup', function () {
				that.es.$list.find('li.selected').removeClass('selected');
			});
	};
	EditableSelectUtility.prototype.initializeInput = function () {
		var that = this;
		switch (this.es.options.trigger) {
			default:
			case 'focus':
				that.es.$input
					.on('focus', $.proxy(that.es.show, that.es))
					.on('blur', $.proxy(that.es.hide, that.es));
				break;
			case 'manual':
				break;
		}
		that.es.$input.on('input keydown', function (e) {
			switch (e.keyCode) {
				case 38: // Up
					var visibles = that.es.$list.find('li.es-visible:not([disabled])');
					var selectedIndex = visibles.index(visibles.filter('li.selected'));
					that.highlight(selectedIndex - 1);
					e.preventDefault();
					break;
				case 40: // Down
					var visibles = that.es.$list.find('li.es-visible:not([disabled])');
					var selectedIndex = visibles.index(visibles.filter('li.selected'));
					that.highlight(selectedIndex + 1);
					e.preventDefault();
					break;
				case 13: // Enter
					if (that.es.$list.is(':visible')) {
						that.es.select(that.es.$list.find('li.selected'));
						e.preventDefault();
					}
					break;
				case 9:  // Tab
				case 27: // Esc
					that.es.hide();
					break;
				default:
					that.es.filter();
					that.highlight(0);
					break;
			}
		});
	};
	EditableSelectUtility.prototype.highlight = function (index) {
		var that = this;
		that.es.show();
		setTimeout(function () {
			var visibles         = that.es.$list.find('li.es-visible');
			var oldSelected      = that.es.$list.find('li.selected').removeClass('selected');
			var oldSelectedIndex = visibles.index(oldSelected);
			
			if (visibles.length > 0) {
				var selectedIndex = (visibles.length + index) % visibles.length;
				var selected      = visibles.eq(selectedIndex);
				var top           = selected.position().top;
				
				selected.addClass('selected');
				if (selectedIndex < oldSelectedIndex && top < 0)
					that.es.$list.scrollTop(that.es.$list.scrollTop() + top);
				if (selectedIndex > oldSelectedIndex && top + selected.outerHeight() > that.es.$list.outerHeight())
					that.es.$list.scrollTop(that.es.$list.scrollTop() + selected.outerHeight() + 2 * (top - that.es.$list.outerHeight()));
			}
		});
	};
	EditableSelectUtility.prototype.setAttributes = function ($element, attrs, data) {
		$.each(attrs || {}, function (i, attr) { $element.attr(attr.name, attr.value); });
		$element.data(data);
	};
	EditableSelectUtility.prototype.trigger = function (event) {
		var params = Array.prototype.slice.call(arguments, 1);
		var args   = [event + '.editable-select'];
		args.push(params);
		this.es.$select.trigger.apply(this.es.$select, args);
		this.es.$input.trigger.apply(this.es.$input, args);
	};
	
	// Plugin
	Plugin = function (option) {
		var args = Array.prototype.slice.call(arguments, 1);
		return this.each(function () {
			var $this   = $(this);
			var data    = $this.data('editable-select');
			var options = $.extend({}, EditableSelect.DEFAULTS, $this.data(), typeof option == 'object' && option);
			
			if (!data) data = new EditableSelect(this, options);
			if (typeof option == 'string') data[option].apply(data, args);
		});
	}
	$.fn.editableSelect             = Plugin;
	$.fn.editableSelect.Constructor = EditableSelect;
	

	// EDITABLE-SELECT DATA-API
	// ==================
	$(window).on('load', function () {
		$('select[data-spy="editable-select"]').editableSelect();
	});
})(jQuery);(function($) {
	function collapse($el) {
		if (!$el.hasClass('ui-collapsed')) {
			$el.addClass('ui-collapsed')
				.children(':not(legend)').slideUp().end()
				.find('legend>i.ui-fieldset-icon')
					.removeClass('fa-caret-down')
					.addClass('fa-caret-right');
		}
	}
	
	function expand($el) {
		if ($el.hasClass('ui-collapsed')) {
			$el.removeClass('ui-collapsed')
				.children(':not(legend)').slideDown().end()
				.find('legend>i.ui-fieldset-icon')
					.removeClass('fa-caret-right')
					.addClass('fa-caret-down');
		}
	}
	
	$.fn.fieldset = function(config) {
		config = config || {};
		return this.each(function() {
			var $t = $(this);
			if (!$t.data('fieldset')) {
				var c = config.collapsed && !($t.hasClass('ui-collapsed'));
				$t.data('fieldset', true)
					.addClass('ui-collapsible' + (c ? ' ui-collapsed' : ''))
					.children('legend')
						.click(function() {
							var $el = $(this).closest('fieldset');
							if ($el.hasClass('ui-collapsed')) {
								expand($el);
							}
							else {
								collapse($el);
							}
						});

				c = $t.hasClass('ui-collapsed');
				if (config.icon !== false && $t.find('legend>i.ui-fieldset-icon').size() == 0) {
					$t.children('legend')
						.prepend('<i class="ui-fieldset-icon fa fa-caret-'
								+ (c ? 'right' : 'down')
								+ '"></i>');
				}
				$t.children(':not(legend)')[c ? 'hide' : 'show']();
			}
			
			switch(config) {
			case 'collapse':
				collapse($t);
				break;
			case 'expand':
				expand($t);
				break;
			}
		});
	};

	// FIELDSET DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="fieldset"]').fieldset();
	});
})(jQuery);
(function($) {
	function focusme() {
		var f = false;
		$('[focusme]').each(function() {
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
				if ($a.length < 1) {
					$a = $i.find('a').not(':hidden,:disabled').eq(0);
					if ($a.length < 1) {
						$a = $i;
					}
				}
			}
			else if (a != '' && a != 'false') {
				$a = $i.find(a).eq(0);
			}
			
			if ($a && $a.length) {
				f = true;
				var $w = $(window), st = $w.scrollTop(), sl = $w.scrollLeft();
				$a.focus();
				$(window).scrollTop(st).scrollLeft(sl);
			}
		});
	}

	$(window).on('load', function() {
		focusme();
	});
})(jQuery);
(function($) {
	function clearMaskTimeout($el) {
		//if this element has delayed mask scheduled then remove it and display the new one
		var t = $el.data("_mask_timeout");
		if (t) {
			clearTimeout(t);
			$el.removeData("_mask_timeout");
		}
	}
	
	function maskElement($el, c) {
		clearMaskTimeout($el);
		
		if ($el.isLoadMasked()) {
			unmaskElement($el);
		}
		
		if ($el.css("position") == "static") {
			$el.addClass("ui-loadmasked-relative");
		}
		$el.addClass("ui-loadmasked");

		if (c.mask !== false) {
			var $m = $('<div class="ui-loadmask-mask"></div>');
			//auto height fix for IE
			if ($.browser.msie) {
				$m.height($el.height() + parseInt($el.css("padding-top")) + parseInt($el.css("padding-bottom")));
				$m.width($el.width() + parseInt($el.css("padding-left")) + parseInt($el.css("padding-right")));
			}
			$el.append($m);
		}
		
		//fix for z-index bug with selects in IE6
		if (parseInt($.browser.version,10) < 7) {
			$el.find("select").addClass("ui-loadmasked-hidden");
		}
		
		var $mb = $('<div class="ui-loadmask" style="display:none;"><div class="ui-loadmask-img"></div></div>');
		if (c.cssClass) {
			$mb.addClass(c.cssClass);
		}
		if (c.html) {
			$mb.html(html);
		}
		else if (c.label) {
			$mb.addClass('ui-loadmask-hasmsg').append('<table class="ui-loadmask-msg"><tr><td>' + c.label + '</td></tr></table>');
		}
		$el.append($mb);

		if (c.window) {
			$mb.addClass('ui-loadmask-fixed');
		}
		else {
			$mb.center();
		}
		$mb.show();
	}
	
	function unmaskElement($el) {
		clearMaskTimeout($el);

		$el.find(".ui-loadmask-mask, .ui-loadmask").remove();
		$el.removeClass("ui-loadmasked ui-loadmasked-relative");
		$el.find("select").removeClass("ui-loadmasked-hidden");
	}
 
	/**
	 * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
	 * @param cssClass css class for the mask element
	 * @param html  html content for the mask body
	 * @param label text message that will be display
	 * @param delay Delay in milliseconds before element is masked (optional). If unloadmask() is called 
	 *              before the delay times out, no mask is displayed. This can be used to prevent unnecessary 
	 *              mask display for quick processes.
	 */
	$.fn.loadmask = function(c) {
		if (typeof(c) == 'string') {
			c = { label: c };
		}
		else {
			c = c || {};
		}
		return this.each(function() {
			if (c.delay !== undefined && c.delay > 0) {
				var $el = $(this);
				$el.data("_mask_timeout", setTimeout(function() {
					maskElement($el, c);
				}, c.delay));
			}
			else {
				maskElement($(this), c);
			}
		});
	};
	
	/**
	 * Removes mask from the element(s). Accepts both single and multiple selectors.
	 */
	$.fn.unloadmask = function() {
		return this.each(function() {
			unmaskElement($(this));
		});
	};
	
	/**
	 * Checks if a single element is masked. Returns false if mask is delayed or not displayed. 
	 */
	$.fn.isLoadMasked = function() {
		return this.hasClass("ui-loadmasked");
	};
})(jQuery);
/* Notify.js - http://notifyjs.com/ Copyright (c) 2015 MIT */
(function (factory) {
	// UMD start
	// https://github.com/umdjs/umd/blob/master/jqueryPluginCommonjs.js
	if (typeof define === 'function' && define.amd) {
		// AMD. Register as an anonymous module.
		define(['jquery'], factory);
	} else if (typeof module === 'object' && module.exports) {
		// Node/CommonJS
		module.exports = function( root, jQuery ) {
			if ( jQuery === undefined ) {
				// require('jQuery') returns a factory that requires window to
				// build a jQuery instance, we normalize how we use modules
				// that require this pattern but the window provided is a noop
				// if it's defined (how jquery works)
				if ( typeof window !== 'undefined' ) {
					jQuery = require('jquery');
				}
				else {
					jQuery = require('jquery')(root);
				}
			}
			factory(jQuery);
			return jQuery;
		};
	} else {
		// Browser globals
		factory(jQuery);
	}
}(function ($) {
	//IE8 indexOf polyfill
	var indexOf = [].indexOf || function(item) {
		for (var i = 0, l = this.length; i < l; i++) {
			if (i in this && this[i] === item) {
				return i;
			}
		}
		return -1;
	};

	var pluginName = "notify";
	var pluginClassName = pluginName + "js";
	var blankFieldName = pluginName + "!blank";

	var positions = {
		t: "top",
		m: "middle",
		b: "bottom",
		l: "left",
		c: "center",
		r: "right"
	};
	var hAligns = ["l", "c", "r"];
	var vAligns = ["t", "m", "b"];
	var mainPositions = ["t", "b", "l", "r"];
	var opposites = {
		t: "b",
		m: null,
		b: "t",
		l: "r",
		c: null,
		r: "l"
	};

	var parsePosition = function(str) {
		var pos;
		pos = [];
		$.each(str.split(/\W+/), function(i, word) {
			var w;
			w = word.toLowerCase().charAt(0);
			if (positions[w]) {
				return pos.push(w);
			}
		});
		return pos;
	};

	var styles = {};

	var coreStyle = {
		name: "core",
		html: "<div class=\"" + pluginClassName + "-wrapper\">\n	<div class=\"" + pluginClassName + "-arrow\"></div>\n	<div class=\"" + pluginClassName + "-container\"></div>\n</div>",
		css: "." + pluginClassName + "-corner {\n	position: fixed;\n	margin: 5px;\n	z-index: 1050;\n}\n\n." + pluginClassName + "-corner ." + pluginClassName + "-wrapper,\n." + pluginClassName + "-corner ." + pluginClassName + "-container {\n	position: relative;\n	display: block;\n	height: inherit;\n	width: inherit;\n	margin: 3px;\n}\n\n." + pluginClassName + "-wrapper {\n	z-index: 1;\n	position: absolute;\n	display: inline-block;\n	height: 0;\n	width: 0;\n}\n\n." + pluginClassName + "-container {\n	display: none;\n	z-index: 1;\n	position: absolute;\n}\n\n." + pluginClassName + "-hidable {\n	cursor: pointer;\n}\n\n[data-notify-text],[data-notify-html] {\n	position: relative;\n}\n\n." + pluginClassName + "-arrow {\n	position: absolute;\n	z-index: 2;\n	width: 0;\n	height: 0;\n}"
	};

	var stylePrefixes = {
		"border-radius": ["-webkit-", "-moz-"]
	};

	var getStyle = function(name) {
		return styles[name];
	};

	var removeStyle = function(name) {
		if (!name) {
			throw "Missing Style name";
		}
		if (styles[name]) {
			delete styles[name];
		}
	};

	var addStyle = function(name, def) {
		if (!name) {
			throw "Missing Style name";
		}
		if (!def) {
			throw "Missing Style definition";
		}
		if (!def.html) {
			throw "Missing Style HTML";
		}
		//remove existing style
		var existing = styles[name];
		if (existing && existing.cssElem) {
			if (window.console) {
				console.warn(pluginName + ": overwriting style '" + name + "'");
			}
			styles[name].cssElem.remove();
		}
		def.name = name;
		styles[name] = def;
		var cssText = "";
		if (def.classes) {
			$.each(def.classes, function(className, props) {
				cssText += "." + pluginClassName + "-" + def.name + "-" + className + " {\n";
				$.each(props, function(name, val) {
					if (stylePrefixes[name]) {
						$.each(stylePrefixes[name], function(i, prefix) {
							return cssText += "	" + prefix + name + ": " + val + ";\n";
						});
					}
					return cssText += "	" + name + ": " + val + ";\n";
				});
				return cssText += "}\n";
			});
		}
		if (def.css) {
			cssText += "/* styles for " + def.name + " */\n" + def.css;
		}
		if (cssText) {
			def.cssElem = insertCSS(cssText);
			def.cssElem.attr("id", "notify-" + def.name);
		}
		var fields = {};
		var elem = $(def.html);
		findFields("html", elem, fields);
		findFields("text", elem, fields);
		findFields("attr", elem, fields);
		def.fields = fields;
	};

	var insertCSS = function(cssText) {
		var elem = createElem("style");
		elem.attr("type", 'text/css');
		$("head").append(elem);
		try {
			elem.html(cssText);
		} catch (_) {
			elem[0].styleSheet.cssText = cssText;
		}
		return elem;
	};

	var findFields = function(type, elem, fields) {
		var attr = "data-notify-" + type;
		return find(elem, "[" + attr + "]").each(function() {
			var name = $(this).attr(attr);
			if (!name) {
				name = blankFieldName;
			}
			fields[name] = type;
		});
	};

	var find = function(elem, selector) {
		if (elem.is(selector)) {
			return elem;
		} else {
			return elem.find(selector);
		}
	};

	var pluginOptions = {
		prepend: true,
		clickToHide: true,
		autoHide: true,
		autoHideDelay: 5000,
		arrowShow: true,
		arrowSize: 5,
		breakNewLines: true,
		elementPosition: "bottom",
		globalPosition: "top right",
		style: "bootstrap",
		className: "info",
		showAnimation: "slideDown",
		showDuration: 400,
		hideAnimation: "slideUp",
		hideDuration: 200,
		gap: 5
	};

	var inherit = function(a, b) {
		var F;
		F = function() {};
		F.prototype = a;
		return $.extend(true, new F(), b);
	};

	var defaults = function(opts) {
		return $.extend(pluginOptions, opts);
	};

	var createElem = function(tag) {
		return $("<" + tag + "></" + tag + ">");
	};

	var globalAnchors = {};

	var getAnchorElement = function(element) {
		var radios;
		if (element.is('[type=radio]')) {
			radios = element.parents('form:first').find('[type=radio]').filter(function(i, e) {
				return $(e).attr("name") === element.attr("name");
			});
			element = radios.first();
		}
		return element;
	};

	var incr = function(obj, pos, val) {
		var opp, temp;
		if (typeof val === "string") {
			val = parseInt(val, 10);
		} else if (typeof val !== "number") {
			return;
		}
		if (isNaN(val)) {
			return;
		}
		opp = positions[opposites[pos.charAt(0)]];
		temp = pos;
		if (obj[opp] !== undefined) {
			pos = positions[opp.charAt(0)];
			val = -val;
		}
		if (obj[pos] === undefined) {
			obj[pos] = val;
		} else {
			obj[pos] += val;
		}
		return null;
	};

	var realign = function(alignment, inner, outer) {
		if (alignment === "l" || alignment === "t") {
			return 0;
		} else if (alignment === "c" || alignment === "m") {
			return outer / 2 - inner / 2;
		} else if (alignment === "r" || alignment === "b") {
			return outer - inner;
		}
		throw "Invalid alignment";
	};

	var encode = function(text) {
		encode.e = encode.e || createElem("div");
		return encode.e.text(text).html();
	};

	function Notification(elem, data, options) {
		if (typeof options === "string") {
			options = {
				className: options
			};
		}
		this.options = inherit(pluginOptions, $.isPlainObject(options) ? options : {});
		this.loadHTML();
		this.wrapper = $(coreStyle.html);
		if (this.options.clickToHide) {
			this.wrapper.addClass(pluginClassName + "-hidable");
		}
		this.wrapper.data(pluginClassName, this);
		this.arrow = this.wrapper.find("." + pluginClassName + "-arrow");
		this.container = this.wrapper.find("." + pluginClassName + "-container");
		this.container.append(this.userContainer);
		if (elem && elem.length) {
			this.elementType = elem.attr("type");
			this.originalElement = elem;
			this.elem = getAnchorElement(elem);
			this.elem.data(pluginClassName, this);
			this.elem.before(this.wrapper);
		}
		this.container.hide();
		this.run(data);
	}

	Notification.prototype.loadHTML = function() {
		var style;
		style = this.getStyle();
		this.userContainer = $(style.html);
		this.userFields = style.fields;
	};

	Notification.prototype.show = function(show, userCallback) {
		var args, callback, elems, fn, hidden;
		callback = (function(_this) {
			return function() {
				if (!show && !_this.elem) {
					_this.destroy();
				}
				if (userCallback) {
					return userCallback();
				}
			};
		})(this);
		hidden = this.container.parent().parents(':hidden').length > 0;
		elems = this.container.add(this.arrow);
		args = [];
		if (hidden && show) {
			fn = "show";
		} else if (hidden && !show) {
			fn = "hide";
		} else if (!hidden && show) {
			fn = this.options.showAnimation;
			args.push(this.options.showDuration);
		} else if (!hidden && !show) {
			fn = this.options.hideAnimation;
			args.push(this.options.hideDuration);
		} else {
			return callback();
		}
		args.push(callback);
		return elems[fn].apply(elems, args);
	};

	Notification.prototype.setGlobalPosition = function() {
		var p = this.getPosition();
		var pMain = p[0];
		var pAlign = p[1];
		var main = positions[pMain];
		var align = positions[pAlign];
		var key = pMain + "|" + pAlign;
		var anchor = globalAnchors[key];
		if (!anchor || !document.body.contains(anchor[0])) {
			anchor = globalAnchors[key] = createElem("div");
			var css = {};
			css[main] = 0;
			if (align === "middle") {
				css.top = '45%';
			} else if (align === "center") {
				css.left = '45%';
			} else {
				css[align] = 0;
			}
			anchor.css(css).addClass(pluginClassName + "-corner");
			$("body").append(anchor);
		}
		return anchor[this.options.prepend ? "prepend" : "append"](this.wrapper);
	};

	Notification.prototype.setElementPosition = function() {
		var arrowColor, arrowCss, arrowSize, color, contH, contW, css, elemH, elemIH, elemIW, elemPos, elemW, gap, j, k, len, len1, mainFull, margin, opp, oppFull, pAlign, pArrow, pMain, pos, posFull, position, ref, wrapPos;
		position = this.getPosition();
		pMain = position[0];
		pAlign = position[1];
		pArrow = position[2];
		elemPos = this.elem.position();
		elemH = this.elem.outerHeight();
		elemW = this.elem.outerWidth();
		elemIH = this.elem.innerHeight();
		elemIW = this.elem.innerWidth();
		wrapPos = this.wrapper.position();
		contH = this.container.height();
		contW = this.container.width();
		mainFull = positions[pMain];
		opp = opposites[pMain];
		oppFull = positions[opp];
		css = {};
		css[oppFull] = pMain === "b" ? elemH : pMain === "r" ? elemW : 0;
		incr(css, "top", elemPos.top - wrapPos.top);
		incr(css, "left", elemPos.left - wrapPos.left);
		ref = ["top", "left"];
		for (j = 0, len = ref.length; j < len; j++) {
			pos = ref[j];
			margin = parseInt(this.elem.css("margin-" + pos), 10);
			if (margin) {
				incr(css, pos, margin);
			}
		}
		gap = Math.max(0, this.options.gap - (this.options.arrowShow ? arrowSize : 0));
		incr(css, oppFull, gap);
		if (!this.options.arrowShow) {
			this.arrow.hide();
		} else {
			arrowSize = this.options.arrowSize;
			arrowCss = $.extend({}, css);
			arrowColor = this.userContainer.css("border-color") || this.userContainer.css("border-top-color") || this.userContainer.css("background-color") || "white";
			for (k = 0, len1 = mainPositions.length; k < len1; k++) {
				pos = mainPositions[k];
				posFull = positions[pos];
				if (pos === opp) {
					continue;
				}
				color = posFull === mainFull ? arrowColor : "transparent";
				arrowCss["border-" + posFull] = arrowSize + "px solid " + color;
			}
			incr(css, positions[opp], arrowSize);
			if (indexOf.call(mainPositions, pAlign) >= 0) {
				incr(arrowCss, positions[pAlign], arrowSize * 2);
			}
		}
		if (indexOf.call(vAligns, pMain) >= 0) {
			incr(css, "left", realign(pAlign, contW, elemW));
			if (arrowCss) {
				incr(arrowCss, "left", realign(pAlign, arrowSize, elemIW));
			}
		} else if (indexOf.call(hAligns, pMain) >= 0) {
			incr(css, "top", realign(pAlign, contH, elemH));
			if (arrowCss) {
				incr(arrowCss, "top", realign(pAlign, arrowSize, elemIH));
			}
		}
		if (this.container.is(":visible")) {
			css.display = "block";
		}
		this.container.removeAttr("style").css(css);
		if (arrowCss) {
			return this.arrow.removeAttr("style").css(arrowCss);
		}
	};

	Notification.prototype.getPosition = function() {
		var pos, ref, ref1, ref2, ref3, ref4, ref5, text;
		text = this.options.position || (this.elem ? this.options.elementPosition : this.options.globalPosition);
		pos = parsePosition(text);
		if (pos.length === 0) {
			pos[0] = "b";
		}
		if (ref = pos[0], indexOf.call(mainPositions, ref) < 0) {
			throw "Must be one of [" + mainPositions + "]";
		}
		if (pos.length === 1 || ((ref1 = pos[0], indexOf.call(vAligns, ref1) >= 0) && (ref2 = pos[1], indexOf.call(hAligns, ref2) < 0)) || ((ref3 = pos[0], indexOf.call(hAligns, ref3) >= 0) && (ref4 = pos[1], indexOf.call(vAligns, ref4) < 0))) {
			pos[1] = (ref5 = pos[0], indexOf.call(hAligns, ref5) >= 0) ? "m" : "l";
		}
		if (pos.length === 2) {
			pos[2] = pos[1];
		}
		return pos;
	};

	Notification.prototype.getStyle = function(name) {
		var style;
		if (!name) {
			name = this.options.style;
		}
		if (!name) {
			name = "default";
		}
		style = styles[name];
		if (!style) {
			throw "Missing style: " + name;
		}
		return style;
	};

	Notification.prototype.updateClasses = function() {
		var classes, style;
		classes = ["base"];
		if ($.isArray(this.options.className)) {
			classes = classes.concat(this.options.className);
		} else if (this.options.className) {
			classes.push(this.options.className);
		}
		style = this.getStyle();
		classes = $.map(classes, function(n) {
			return pluginClassName + "-" + style.name + "-" + n;
		}).join(" ");
		return this.userContainer.attr("class", classes);
	};

	Notification.prototype.run = function(data, options) {
		if ($.isPlainObject(options)) {
			$.extend(this.options, options);
		}
		else if ($.type(options) === "string") {
			this.options.className = options;
		}
		if (this.container && !data) {
			this.show(false);
			return;
		}
		if (!this.container && !data) {
			return;
		}
		
		var datas = {};
		if ($.isPlainObject(data)) {
			datas = data;
		}
		else {
			datas[blankFieldName] = data;
		}
		if (!datas['class']) {
			datas['class'] = this.options.className;
		}
		
		for (var name in datas) {
			var d = datas[name];
			var t = this.userFields[name];
			if (!t) {
				continue;
			}

			var v = name === blankFieldName ? '' : '=' + name;
			var $e = find(this.userContainer, "[data-notify-" + t + v + "]");
			if (t === "attr") {
				$e.attr(name, d);
			}
			else if (t === "text") {
				d = encode(d);
				if (this.options.breakNewLines) {
					d = d.replace(/\n/g, '<br/>');
				}
				$e.html(d);
			}
			else {
				$e.html(d);
			}
		}
		
		this.updateClasses();
		if (this.elem) {
			this.setElementPosition();
		}
		else {
			this.setGlobalPosition();
		}

		this.show(true);
		if (this.options.autoHide) {
			clearTimeout(this.autohideTimer);
			this.autohideTimer = setTimeout(this.show.bind(this, false), this.options.autoHideDelay);
		}
	};

	Notification.prototype.destroy = function() {
		this.wrapper.data(pluginClassName, null);
		this.wrapper.remove();
	};

	$[pluginName] = function(elem, data, options) {
		if ((elem && elem.nodeName) || elem.jquery) {
			$(elem)[pluginName](data, options);
		} else {
			options = data;
			data = elem;
			new Notification(null, data, options);
		}
		return elem;
	};

	$.fn[pluginName] = function(data, options) {
		$(this).each(function() {
			var prev = getAnchorElement($(this)).data(pluginClassName);
			if (prev) {
				prev.destroy();
			}
			var curr = new Notification($(this), data, options);
		});
		return this;
	};

	$.extend($[pluginName], {
		defaults: defaults,
		addStyle: addStyle,
		removeStyle: removeStyle,
		pluginOptions: pluginOptions,
		getStyle: getStyle,
		insertCSS: insertCSS
	});

	//always include the default bootstrap style
	addStyle("bootstrap", {
		html: '<div>\n<span data-notify-text></span>\n</div>',
		classes: {
			base: {
				"font-weight": "bold",
				"padding": "12px 20px 12px 42px",
				"text-shadow": "0 1px 0 rgba(255, 255, 255, 0.5)",
				"background-color": "#fcf8e3",
				"border": "1px solid #fbeed5",
				"border-radius": "4px",
				"white-space": "nowrap",
				"background-repeat": "no-repeat",
				"background-position": "12px 12px"
			},
			error: {
				"color": "#B94A48",
				"background-color": "#F2DEDE",
				"border-color": "#EED3D7",
				"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAtRJREFUeNqkVc1u00AQHq+dOD+0poIQfkIjalW0SEGqRMuRnHos3DjwAH0ArlyQeANOOSMeAA5VjyBxKBQhgSpVUKKQNGloFdw4cWw2jtfMOna6JOUArDTazXi/b3dm55socPqQhFka++aHBsI8GsopRJERNFlY88FCEk9Yiwf8RhgRyaHFQpPHCDmZG5oX2ui2yilkcTT1AcDsbYC1NMAyOi7zTX2Agx7A9luAl88BauiiQ/cJaZQfIpAlngDcvZZMrl8vFPK5+XktrWlx3/ehZ5r9+t6e+WVnp1pxnNIjgBe4/6dAysQc8dsmHwPcW9C0h3fW1hans1ltwJhy0GxK7XZbUlMp5Ww2eyan6+ft/f2FAqXGK4CvQk5HueFz7D6GOZtIrK+srupdx1GRBBqNBtzc2AiMr7nPplRdKhb1q6q6zjFhrklEFOUutoQ50xcX86ZlqaZpQrfbBdu2R6/G19zX6XSgh6RX5ubyHCM8nqSID6ICrGiZjGYYxojEsiw4PDwMSL5VKsC8Yf4VRYFzMzMaxwjlJSlCyAQ9l0CW44PBADzXhe7xMdi9HtTrdYjFYkDQL0cn4Xdq2/EAE+InCnvADTf2eah4Sx9vExQjkqXT6aAERICMewd/UAp/IeYANM2joxt+q5VI+ieq2i0Wg3l6DNzHwTERPgo1ko7XBXj3vdlsT2F+UuhIhYkp7u7CarkcrFOCtR3H5JiwbAIeImjT/YQKKBtGjRFCU5IUgFRe7fF4cCNVIPMYo3VKqxwjyNAXNepuopyqnld602qVsfRpEkkz+GFL1wPj6ySXBpJtWVa5xlhpcyhBNwpZHmtX8AGgfIExo0ZpzkWVTBGiXCSEaHh62/PoR0p/vHaczxXGnj4bSo+G78lELU80h1uogBwWLf5YlsPmgDEd4M236xjm+8nm4IuE/9u+/PH2JXZfbwz4zw1WbO+SQPpXfwG/BBgAhCNZiSb/pOQAAAAASUVORK5CYII=)"
			},
			success: {
				"color": "#468847",
				"background-color": "#DFF0D8",
				"border-color": "#D6E9C6",
				"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAutJREFUeNq0lctPE0Ecx38zu/RFS1EryqtgJFA08YCiMZIAQQ4eRG8eDGdPJiYeTIwHTfwPiAcvXIwXLwoXPaDxkWgQ6islKlJLSQWLUraPLTv7Gme32zoF9KSTfLO7v53vZ3d/M7/fIth+IO6INt2jjoA7bjHCJoAlzCRw59YwHYjBnfMPqAKWQYKjGkfCJqAF0xwZjipQtA3MxeSG87VhOOYegVrUCy7UZM9S6TLIdAamySTclZdYhFhRHloGYg7mgZv1Zzztvgud7V1tbQ2twYA34LJmF4p5dXF1KTufnE+SxeJtuCZNsLDCQU0+RyKTF27Unw101l8e6hns3u0PBalORVVVkcaEKBJDgV3+cGM4tKKmI+ohlIGnygKX00rSBfszz/n2uXv81wd6+rt1orsZCHRdr1Imk2F2Kob3hutSxW8thsd8AXNaln9D7CTfA6O+0UgkMuwVvEFFUbbAcrkcTA8+AtOk8E6KiQiDmMFSDqZItAzEVQviRkdDdaFgPp8HSZKAEAL5Qh7Sq2lIJBJwv2scUqkUnKoZgNhcDKhKg5aH+1IkcouCAdFGAQsuWZYhOjwFHQ96oagWgRoUov1T9kRBEODAwxM2QtEUl+Wp+Ln9VRo6BcMw4ErHRYjH4/B26AlQoQQTRdHWwcd9AH57+UAXddvDD37DmrBBV34WfqiXPl61g+vr6xA9zsGeM9gOdsNXkgpEtTwVvwOklXLKm6+/p5ezwk4B+j6droBs2CsGa/gNs6RIxazl4Tc25mpTgw/apPR1LYlNRFAzgsOxkyXYLIM1V8NMwyAkJSctD1eGVKiq5wWjSPdjmeTkiKvVW4f2YPHWl3GAVq6ymcyCTgovM3FzyRiDe2TaKcEKsLpJvNHjZgPNqEtyi6mZIm4SRFyLMUsONSSdkPeFtY1n0mczoY3BHTLhwPRy9/lzcziCw9ACI+yql0VLzcGAZbYSM5CCSZg1/9oc/nn7+i8N9p/8An4JMADxhH+xHfuiKwAAAABJRU5ErkJggg==)"
			},
			info: {
				"color": "#3A87AD",
				"background-color": "#D9EDF7",
				"border-color": "#BCE8F1",
				"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3QYFAhkSsdes/QAAA8dJREFUOMvVlGtMW2UYx//POaWHXg6lLaW0ypAtw1UCgbniNOLcVOLmAjHZolOYlxmTGXVZdAnRfXQm+7SoU4mXaOaiZsEpC9FkiQs6Z6bdCnNYruM6KNBw6YWewzl9z+sHImEWv+vz7XmT95f/+3/+7wP814v+efDOV3/SoX3lHAA+6ODeUFfMfjOWMADgdk+eEKz0pF7aQdMAcOKLLjrcVMVX3xdWN29/GhYP7SvnP0cWfS8caSkfHZsPE9Fgnt02JNutQ0QYHB2dDz9/pKX8QjjuO9xUxd/66HdxTeCHZ3rojQObGQBcuNjfplkD3b19Y/6MrimSaKgSMmpGU5WevmE/swa6Oy73tQHA0Rdr2Mmv/6A1n9w9suQ7097Z9lM4FlTgTDrzZTu4StXVfpiI48rVcUDM5cmEksrFnHxfpTtU/3BFQzCQF/2bYVoNbH7zmItbSoMj40JSzmMyX5qDvriA7QdrIIpA+3cdsMpu0nXI8cV0MtKXCPZev+gCEM1S2NHPvWfP/hL+7FSr3+0p5RBEyhEN5JCKYr8XnASMT0xBNyzQGQeI8fjsGD39RMPk7se2bd5ZtTyoFYXftF6y37gx7NeUtJJOTFlAHDZLDuILU3j3+H5oOrD3yWbIztugaAzgnBKJuBLpGfQrS8wO4FZgV+c1IxaLgWVU0tMLEETCos4xMzEIv9cJXQcyagIwigDGwJgOAtHAwAhisQUjy0ORGERiELgG4iakkzo4MYAxcM5hAMi1WWG1yYCJIcMUaBkVRLdGeSU2995TLWzcUAzONJ7J6FBVBYIggMzmFbvdBV44Corg8vjhzC+EJEl8U1kJtgYrhCzgc/vvTwXKSib1paRFVRVORDAJAsw5FuTaJEhWM2SHB3mOAlhkNxwuLzeJsGwqWzf5TFNdKgtY5qHp6ZFf67Y/sAVadCaVY5YACDDb3Oi4NIjLnWMw2QthCBIsVhsUTU9tvXsjeq9+X1d75/KEs4LNOfcdf/+HthMnvwxOD0wmHaXr7ZItn2wuH2SnBzbZAbPJwpPx+VQuzcm7dgRCB57a1uBzUDRL4bfnI0RE0eaXd9W89mpjqHZnUI5Hh2l2dkZZUhOqpi2qSmpOmZ64Tuu9qlz/SEXo6MEHa3wOip46F1n7633eekV8ds8Wxjn37Wl63VVa+ej5oeEZ/82ZBETJjpJ1Rbij2D3Z/1trXUvLsblCK0XfOx0SX2kMsn9dX+d+7Kf6h8o4AIykuffjT8L20LU+w4AZd5VvEPY+XpWqLV327HR7DzXuDnD8r+ovkBehJ8i+y8YAAAAASUVORK5CYII=)"
			},
			warn: {
				"color": "#C09853",
				"background-color": "#FCF8E3",
				"border-color": "#FBEED5",
				"background-image": "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAAG4AAABuASPlPqcAAAAZdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuMjHxIGmVAAACE0lEQVQ4T62UzUsbQRjG7aVnb15Kz0L/gBATcJLNbhsbt+7G1GSziTuzaXroQRQvtQhSb6UgeqhgoLWFHHpRT5p66aEFRaRBkRTEY0H0llooKOR1Jml0N/NCoXXhBzPPzPPwzs5HFwDcKKj4P6BiJ4P54t0Aid/BxjpBxTb8u2VatJRnFAzjAYSiaklo2Nw2qNgmabHi9HMGP78V4HeVwcxYAsIRrYjNbYOKgiHH6X6cp6c/vhQAvrc4+2pDf0w9JYR0Yx4BKgrMjDP3dp41gz5/dGGtZAHUCrD8chBCRJvDPAJUTGacXvqEXvyqtiqbGGeQy+nN9vm2DQMD2kWQaL2YVxIEZpZtfCq7V0v1BooqN+cfQSiibWBeSTCyNCECGtyIBnIaOzkYHbkPYaIlOv2+TiqVus2PyeFB5bo6LFBUefDegL6oeniPe7wZvkDDopOvZlsb4eXdAoPXM8N+fceGF0/j/GzGJr0ZVw09U+hJ52n9ZMtj+kND4PkFTXj/ZMWEqKrVA4rSIwUmLWex/EauTrD+wYXyQloe41UuTT3kx0hdlALNLD0+4zdCMnFqmy7sruXksZoL9dUhsePHcqBFK/vr/s34K3sOVJd0fh3VihRoZFgwZTtH4u7OTstMPNN9TI3pMO7GoV+JHfUpSlAKbHb4S5JM08Cw7RAvpsUiYRIjEhEt0Pn6+AJvAlT8d6DrEoDfQUJHAQyyAAAAAElFTkSuQmCC)"
			}
		}
	});

	$(function() {
		insertCSS(coreStyle.css).attr("id", "core-notify");
		$(document).on("click", "." + pluginClassName + "-hidable", function(e) {
			$(this).trigger("notify-hide");
		});
		$(document).on("notify-hide", "." + pluginClassName + "-wrapper", function(e) {
			var elem = $(this).data(pluginClassName);
			if(elem) {
				elem.show(false);
			}
		});
	});

}));
(function($) {
	var __inited = false;
	var __active = null;
	var __tclose = null;
	var __tenter = null;

	function __emptyFn() {
	}
	
	function __init() {
		if (!__inited) {
			__inited = true;
			$('<div id="ui_popup_loader" style="display:none"></div>').appendTo('body');
			$('<div id="ui_popup_shadow" style="display:none">'
				+ ($.browser.msie && parseInt($.browser.version, 10) < 7 ?
						'<iframe src="javascript:false;" frameborder="0"></iframe>' : '')
				+ '</div>').appendTo('body');
			$('<i id="ui_popup_closer" class="fa fa-times-circle" style="display:none"></i>')
				.appendTo('body')
				.mousedown(__mousedown);

			$(document).mousedown(__mousedown);
		}
	}

	function __mousedown(e) {
		if (__active && !__tclose) {
			__tclose = setTimeout(function() { 
				__tclose = null;
				__close(e.target); 
			}, 200);
		}
	}

	function __center($p) {
		var $w = $(window);
		$p.css({
			top: ($w.scrollTop() + 30) + "px",
			left: ($w.scrollLeft() + 30) + "px",
			width: ($.browser.width() - 60) + "px",
			height: ($.browser.height() - 60) + "px"
		});
	}

	function __shadow() {
		if (__active) {
			var $p = $('#' + __active);
			if ($p.hasClass('ui-popup-center')) {
				__center($p);
			}
			var p = $p[0];
			$('#ui_popup_shadow').css({
				top: p.offsetTop + 'px',
				left: p.offsetLeft + 'px',
				width: p.offsetWidth + 'px',
				height: p.offsetHeight + 'px',
				display: 'block'
			}).children('iframe').css({
				width: p.firstChild.offsetWidth + 'px',
				height: p.firstChild.offsetHeight + 'px'
			});
			$('#ui_popup_closer').css({
				top: p.offsetTop - 4 + 'px',
				left: p.offsetLeft + p.offsetWidth - 12 + 'px',
				display: 'block'
			})
			setTimeout(__shadow, 100);
		}
		else {
			$('#ui_popup_shadow, #ui_popup_closer').css('display', 'none');
		}
	}

	function __close(el) {
		if (!__active || (el && el.tagName == "HTML")) {
			return;
		}

		if (el.id == 'ui_popup_closer') {
			_hide();
			return;
		}
		
		var $p = $("#" + __active), p = $p.get(0);
		var elt = $p.data('popup').trigger;

		// is self or trigger clicked?
		while (el && el.tagName != 'BODY') {
			if (el === p || el === elt || $(el).css('position') == 'absolute') {
				return;
			}
			el = el.parentNode;
		}
		
		if (el) {
			_hide({silent: true});
		}
	}
	
	function __align(c, $el) {
		var $t = $(c.target || c.trigger);
		if (c.popover || $t.length < 1) {
			$el.addClass('ui-popup-center');
			return;
		}

		var p = $t.offset();
		p.top += $t.outerHeight();

		var bw = $.browser.width();
		var ow = $el.outerWidth();
		if (p.left + ow > bw) {
			p.left = bw - ow - 20;
			if (p.left < 0) {
				p.left = 0;
			}
		}
		
		$el.css({
			top: p.top + "px",
			left: p.left + "px"
		}).removeClass('ui-popup-center');
	}
	
	function __activeTarget($t) {
		if ($t.length > 0) {
			var el = $t.get(0);
			if (el.setActive) {
				el.setActive();
			}
			else {
				el.focus();
			}
		}
	}

	function __clearTimer() {
		if (__tclose) {
			clearTimeout(__tclose);
			__tclose = null;
		}
	}
	
	function _toggle(c) {
		c = $.extend({ id: __active }, c);
		if (!c.id) {
			return this;
		}
	
		__clearTimer();
		
		if (c.id == __active) {
			var $p = $("#" + c.id), trigger = $p.data('popup').trigger;
			if (c.trigger === trigger) {
				c.silent = true;
			}
			_hide(c);
			return this;
		}

		_show(c);
		return this;
	}

	function __popup($pc, c) {
		$pc.css({
			top : "0px",
			left : "0px",
		}).addClass('ui-popup-opacity0').show();

		__align(c, $pc.parent(), true);
		
		$pc.hide().removeClass('ui-popup-opacity0').slideDown('fast');
		
		__shadow();
	}
	
	function _show(c) {
		c = $.extend({ id: __active }, c);
		if (!c.id) {
			return this;
		}
	
		__clearTimer();

		if (c.id == __active) {
			return this;
		}
		
		_hide({silent: true});

		__active = c.id;
	
		__activeTarget($(c.target || c.trigger));
	
		var $p = $("#" + c.id), $pc = $p.children(".ui-popup-content");

		c = $.extend($p.data('popup'), {
			silent: null,
			target: null,
			trigger: null,
			callback: null
		}, c);

		if (c.autoClear) {
			c.loaded = null;
		}
		
		if (c.loaded) {
			__popup($pc, c);
		}
		else if (c.loaded !== false){
			_load(c);
		}
		return this;
	}
	
	function _hide(c) {
		c = $.extend({ id: __active }, c);
		if (c.id) {
			__clearTimer();

			__active = null;
			__shadow();
		
			var $p = $("#" + c.id); 

			$p.css({
				top : "-999999px",
				left : "-999999px"
			});
		
			c = $.extend($p.data('popup'), {
				silent: null,
				callback: null
			}, c);

			if (c.silent != true) {
				__activeTarget($(c.target || c.trigger));
			}
		}
		$('body').removeClass('ui-popop-over');
		return this;
	}

	function __loadPage(c, html, e) {
		html = (html || "").trim();
		
		var $p = $("#" + c.id);
		var $pc = $p.children(".ui-popup-content");

		if (c.prepare) {
			var $c = c.prepare(html, e);
			if ($c) {
				$pc.append($c);
				$p.data('popup').loaded = true;
				return $pc;
			}
		}
		else {
			if (html) {
				$pc.html(html);
				$p.data('popup').loaded = true;
				return $pc;
			}
		}
		$p.data('popup').loaded = false;
		return null;
	}
	
	function _load(c) {
		c = $.extend({ id: __active }, c);
		if (c.id) {
			__align(c, $("#ui_popup_loader").show(), false);

			var $pc = null;
			$.ajax({
				url: c.url, 
				data: c.params,
				dataType: 'html',
				success: function(html) {
					$pc = __loadPage(c, html);
				},
				error: function(xhr, status, e) {
					$pc = __loadPage(c, null, e);
				},
				complete: function(xhr, status) {
					if (c.id == __active) {
						$("#ui_popup_loader").hide();
						if ($pc) {
							if ($pc.is(':hidden')) {
								__popup($pc, c);
							}
							else {
								__align(c, $pc.parent(), true);
							}
						}
					}
				}
			});
		}
		return this;
	}
	
	function _callback(data) {
		if (__active) {
			var pd = $('#' + __active).data('popup');
			if (pd && pd.callback) {
				pd.callback(data, pd.trigger);
			}
		}
	}

	var api = {
			load: _load,
			callback: _callback,
			toggle: _toggle,
			show: _show,
			hide: _hide
		};

	$.popup = function(c) {
		__init();
		
		c = c || {};
		if (c.id) {
			var $p = $('#' + c.id);
			if ($p.length < 1) {
				$p = $('<div id="' + c.id + '" class="ui-popup">'
					+ '<div class="ui-popup-content"></div></div>').appendTo('body');

				if (c.cssClass) {
					$p.addClass(c.cssClass);
				}
				$p.data('popup', c);
				if (c.content) {
					var $pc = $p.children(".ui-popup-content");
					$(c.content).detach().appendTo($pc);
					c.loaded = true;
					delete c.content;
				}
				else if (c.autoload) {
					$.ajax({
						url: c.url, 
						data: c.params, 
						dataType: 'html',
						success: function(html) {
							__loadPage(c, html);
						},
						error: function(xhr, status, e) {
							__loadPage(c, null, e);
						}
					});
				}
			}
		}

		return api; 
	};
	
	function __onclick() {
		$.popup().toggle($.extend({trigger: this}, $(this).data('popup')));
		return false;
	}
	function __onleave() {
		if (__tenter) {
			clearTimeout(__tenter);
			__tenter = null;
		}
	}
	function __onenter() {
		__onleave();

		var c = $.extend({trigger: this}, $(this).data('popup'));
		if (c.mouseenter === true) {
			$.popup().show(c);
		}
		else {
			__tenter = setTimeout(function() {
				$.popup().show(c);
			}, c.mouseenter);
		}
		return false;
	}
	
	$.fn.popup = function(c) {
		c = c || {};

		this.data('popup', c)
			.unbind('click', __onclick)
			.unbind('mouseenter', __onenter)
			.unbind('mouseleave', __onleave);
		
		if (c.mouseclick !== false) {
			this.click(__onclick);
		}
		if (c.mouseenter === true) {
			this.mouseenter(__onenter);
		}
		else if (c.mouseenter > 0) {
			this.mouseenter(__onenter).mouseleave(__onleave);
		}
		
		return this;
	};
})(jQuery);
(function($) {
	$.fn.scrollIntoView = function(c) {
		if (!this.length) {
			return this;
		}
		
		var $e = this.first(), $w = $(window), eo = $e.offset();
		var wh = $w.height(), ww = $w.width();
		var st = $w.scrollTop(), sb = st + wh, sl = $w.scrollLeft(), sr = sl + ww;
		var et = eo.top, eh = $e.outerHeight(), eb = et + eh;
		var el = eo.left, ew = $e.outerWidth(), er = el + ew;
		
		var x = sl > er ? el : (sr < el ? (ew > ww ? el : el - (ww - ew)) : -1);
		var y = st > eb ? et : (sb < et ? (eh >= wh ? et : et - (wh - eh)) : -1);
		
		if (x >= 0 || y >= 0) {
			var ss = {};
			if (x >= 0) { ss.scrollLeft = x; }
			if (y >= 0) { ss.scrollTop = y; }
			var $hb = $('html,body');
			if (c) {
				$hb.animate(ss, c);
			}
			else {
				$hb.prop(ss);
			}
		}
		return this;
	};
})(jQuery);
(function($) {
	$.fn.selectText = function() {
		var $t = $(this);
		if ($t.length) {
			var doc = document;
			var el = $t.get(0);
			if (doc.body.createTextRange) {
				var r = doc.body.createTextRange();
				r.moveToElementText(el);
				r.select();
			}
			else {
				if (window.getSelection) {
					var ws = window.getSelection();
					var r = doc.createRange();
					r.selectNodeContents(el);
					ws.removeAllRanges();
					ws.addRange(r);
				}
			}
		}
	};
})(jQuery);
(function($) {
	function enterfire() {
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

	$(window).on('load', function() {
		enterfire();
	});
})(jQuery);
