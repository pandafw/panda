(function($) {
	"use strict";

	var _cssHidden = {
		position: 'absolute',
		top: '-9999px',
		left: '-9999px'
	};

	var _xhrOK = (function() {
		var input = document.createElement('input'),
			xhr = new XMLHttpRequest();
		input.type = 'file';
		return ('multiple' in input)
			&& typeof (xhr.upload) != 'undefined'
			&& typeof (FileList) != 'undefined'
			&& typeof (File) != 'undefined';
	})();

	function addFiles(fs, fadd) {
		if (fs) {
			if (typeof (fs) == "string") {
				fs = $(fs);
			}

			if ($.isArray(fs)) {
				$.each(fs, function(i, f) {
					fadd(f);
				});
			} else {
				$.each(fs, function(n, f) {
					if ($.isArray(f)) {
						$.each(f, function(i, f) {
							fadd(f, n);
						});
					} else {
						fadd(f, n);
					}
				});
			}
		}
	}

	function addParams(ps, padd) {
		if (ps) {
			function _addParams(n, v) {
				if ($.isArray(v)) {
					$.each(v, function(i, v) {
						padd(n, v);
					});
				} else {
					padd(n, v);
				}
			}

			if ($.isArray(ps)) {
				$.each(ps, function(i, d) {
					_addParams(d.name, d.value);
				});
			} else {
				$.each(ps, function(n, v) {
					_addParams(n, v)
				});
			}
		}
	}

	// jquery ajax wrapper
	function ajax(s) {
		var data = new FormData();

		addParams(s.data, function(n, v) {
			data.append(n, v);
		});

		addFiles(s.file, function(f, n) {
			if (f instanceof FileList) {
				$.each(f, function(i, f) {
					data.append(n, f);
				});
				return;
			}

			if (f instanceof File) {
				data.append(n, f);
				return;
			}

			var $f = $(f);
			n = n || $f.attr('name');
			$.each($f.prop('files'), function(i, f) {
				data.append(n, f);
			});
		});

		s = $.extend({}, s, {
			cache: false,
			contentType: false,
			processData: false,
			data: data
		});
		delete s.file;

		var xhr = $.ajaxSettings.xhr();
		var ufp = s.uprogress, dfp = s.dprogress;
		if (ufp || dfp) {
			if (ufp) {
				xhr.upload.addEventListener('progress', function(e) {
					if (e.lengthComputable) {
						ufp(e.loaded, e.total);
					}
				});
				delete s.uprogress;
			}

			if (dfp) {
				xhr.addEventListener('progress', function(e) {
					if (e.lengthComputable) {
						dfp(e.loaded, e.total);
					}
				});
				delete s.dprogress;
			}
		}

		xhr.addEventListener('readystatechange', function(e) {
			switch (xhr.readyState) {
			case XMLHttpRequest.HEADERS_RECEIVED:
				var cd = xhr.getResponseHeader('Content-Disposition');
				if (cd) {
					xhr.responseType = 'arraybuffer';
					var cds = cd.split(';');
					$.each(cds, function(i, v) {
						var sp = v.indexOf('=');
						if (sp > 0) {
							let k = v.substring(0, sp).trim().toLowerCase();
							if (k == 'filename' || k == 'filename*') {
								var fn = v.substring(sp+1).trim();
								if (fn.length > 1 && fn.charAt(0) == '"' && fn.charAt(fn.length-1) == '"') {
									fn = fn.substring(1, fn.length-1);
								}
								if (k == 'filename*') {
									var cp = fn.indexOf("''");
									if (sp >= 0) {
										fn = fn.substring(cp + 2);
									}
								}
								fn = decodeURIComponent(fn);
								if (!xhr.download || k == 'filename*') {
									xhr.download = fn;
								}
							}
						}
					});
					if (!xhr.download) {
						xhr.download = cd;
					}
				}
				break;
			case XMLHttpRequest.DONE:
				if (xhr.download) {
					var blob = new Blob([xhr.response]),
						url = window.URL.createObjectURL(blob),
						$a = $('<a>', { download: xhr.download, href: url }).css(_cssHidden);
					
					$('body').append($a);
					$a.get(0).click();
					setTimeout(function() {
						window.URL.revokeObjectURL(url);
						$a.remove();
					}, 200);
				}
				break;
			}
		});
		s.xhr = function() {
			return xhr;
		};

		return $.ajax(s);
	}

	function createIFrame(s) {
		var id = "ajaf_if_" + s.id;
		return $('<iframe>', { id: id, name: id, src: s.secureUrl }).css(_cssHidden).appendTo('body');
	}

	function createForm(s) {
		var id = 'ajaf_form_' + s.id;

		var $form = $('<form>', {
			id: id,
			name: id,
			action: s.url,
			method: s.method,
			target: 'ajaf_if_' + s.id
		}).css(_cssHidden).appendTo('body');

		addParams(s.data, function(n, v) {
			$('<input type="hidden">')
				.attr('name', n)
				.val(v)
				.appendTo($form);
		});

		$form.files = [];
		if (s.file) {
			$form.attr({
				method: 'POST',
				encoding: 'multipart/form-data',
				enctype: 'multipart/form-data'
			});

			addFiles(s.file, function(f, n) {
				var $f = $(f), $c = $f.clone().insertAfter($f);

				n = n || $f.attr('name');
				$f.attr({
					id: '',
					name: n
				}).appendTo($form);

				$form.files.push({ real: $f, copy: $c });
			});
		}

		return $form;
	}

	function httpData(xhr, type) {
		var data = type == "xml" ? xhr.responseXML : xhr.responseText;

		switch (type) {
		case "script":
			// If the type is "script", eval it in global context
			$.globalEval(data);
			break;
		case "json":
			// Get the JavaScript object, if JSON is used.
			data = $.parseJSON(data);
			break;
		case "html":
			// evaluate scripts within html
			$("<div>").html(data).evalScripts();
			break;
		}

		return data;
	}

	function ajaf(s) {
		s = $.extend({
			method: 'POST',
			forceAjaf: false,
			forceAjax: false
		}, s);

		if (s.forceAjax || ((_xhrOK) && !s.forceAjaf)) {
			return ajax(s);
		}

		s = $.extend({
			id: new Date().getTime(),
			secureUrl: 'javascript:false',
		}, s);

		var $if = createIFrame(s),
			$form = createForm(s),
			done = false, xhr = {};

		// Wait for a response to come back
		function callback(timeout) {
			if (done) {
				return;
			}
			done = true;

			var status = timeout == "timeout" ? "error" : "success";
			try {
				var ioe = $if.get(0);
				var doc = ioe.contentWindow.document || ioe.contentDocument || window.frames[ioe.id].document;
				if (doc && doc.body) {
					if (s.selector) {
						xhr.responseText = $(doc.body).find(s.selector).html();
					} else {
						var fc = doc.body.firstChild;
						var tn = (fc && fc.tagName) ? fc.tagName.toUpperCase() : "";
						if (tn == "TEXTAREA") {
							xhr.responseText = fc.value;
						} else if (tn == "PRE") {
							xhr.responseText = $(fc).text();
						} else {
							xhr.responseText = doc.body.innerHTML;
						}
					}
				}
				xhr.responseXML = (doc && doc.XMLDocument) ? doc.XMLDocument : doc;
			} catch (e) {
				status = "error";
				if (s.error) {
					s.error(xhr, status, e);
				}
			}

			// Recover real files
			$.each($form.files, function(i, f) {
				f.real.attr({
					id: f.copy.attr('id'),
					name: f.copy.attr('name')
				}).insertAfter(f.copy);
				f.copy.remove();
			});
			$form.remove();

			switch (status) {
			case "timeout":
				if (s.error) {
					s.error(xhr, status);
				}
				break;
			case "success":
				// Make sure that the request was successful or not modified
				try {
					// process the data (runs the xhr through httpData regardless of callback)
					var data = httpData(xhr, s.dataType);

					// If a local callback was specified, fire it and pass it the data
					if (s.success) {
						s.success(data, xhr);
					}
				} catch (e) {
					if (s.error) {
						s.error(xhr, status, e);
					}
				}
				break;
			}

			try {
				// The request was completed
				if (s.complete) {
					s.complete(xhr, status);
				}
			} finally {
				//clear up the created iframe after file uploaded.
				$if.unbind();
				setTimeout(function() {
					$if.remove();
				}, 100);
				xhr = null;
			}
		};

		// timeout checker
		if (s.timeout > 0) {
			setTimeout(function() {
				// Check to see if the request is still happening
				if (!done) {
					callback("timeout");
				}
			}, s.timeout);
		}

		// fake progress
		var fudp = s.uprogress || s.dprogress;
		if (fudp) {
			var loaded = 0;
			function _fake_progress() {
				fudp(loaded < 95 ? ++loaded : loaded, 100);
				if (!done) {
					setTimeout(_fake_progress, 10 + loaded);
				}
			}
			setTimeout(_fake_progress, 10);
		}

		if (s.beforeSend) {
			s.beforeSend(xhr, s);
		}

		// submit
		try {
			$form.submit();
		} catch (e) {
			if (s.error) {
				s.error(xhr, "send", e);
			}
		}

		$if.on('load', callback);
		return xhr;
	};

	$.ajaf = ajaf;

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
	"use strict";

	$.copyToClipboard = function(s) {
		if (window.clipboardData) {
			// ie
			clipboardData.setData('Text', s);
			return;
		}

		var $t = $('<textarea>')
			.css({ width: 0, height: 0 })
			.text(s)
			.appendTo('body');

		$t.get(0).select();

		document.execCommand('copy');

		$t.remove();
	};
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

(function($) {
	"use strict";

	$.cookie = function(name, value, options) {
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
				} else {
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
		} else { // only name given, get cookie
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

	$.cookie.defaults = {};

	$.jcookie = function(name, value, options) {
		if (typeof value != 'undefined') { // name and value given, set cookie
			$.cookie(name, btoa(JSON.stringify(value)), options);
		} else {
			try {
				return JSON.parse(atob($.cookie(name)));
			} catch (ex) {
				return {};
			}
		}
	};

})(jQuery);

(function($) {
	"use strict";

	$.fn.disable = function(state) {
		return this.each(function() {
			this.disabled = state;
		});
	};
})(jQuery);
(function($) {
	"use strict";

	$.jcss = function(url) {
		if ($('link[href="' + url + '"]').length) {
			return false;
		}
		$('<link>').attr({ type: 'text/css', rel: 'stylesheet', href: url }).appendTo('head');
		return true;
	};
})(jQuery);

(function($) {
	"use strict";

	var jss = {};

	$.jscript = function(url, callback) {
		if (jss[url]) {
			return false;
		}

		$.getScript(url, callback);
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
	"use strict";

	$.queryArrays = function(s, f) {
		var qa = [], ss = s.split('&');

		for (var i = 0; i < ss.length; i++) {
			var p = ss[i].split('='),
				k = decodeURIComponent(p[0]),
				v = p.length > 1 ? decodeURIComponent(p[1]) : '';

			if (!f || f == k) {
				qa.push({
					name: k,
					value: v
				});
			}
		}
		return qa;
	};

	$.queryParams = function(s) {
		var qs = {}, ss = s.split('&');

		for (var i = 0; i < ss.length; i++) {
			var p = ss[i].split('='),
				k = decodeURIComponent(p[0]),
				v = p.length > 1 ? decodeURIComponent(p[1]) : '';
			if (k in qs) {
				if (!$.isArray(qs[k])) {
					qs[k] = [ qs[k] ];
				}
				qs[k].push(v);
			} else {
				qs[k] = v;
			}
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
(function($) {
	"use strict";

	$.fn.replaceClass = function(s, t) {
		return this.removeClass(s).addClass(t);
	};
})(jQuery);
(function($) {
	/**
	 * Move the selected element(s) to the center position of parent element (default: body)
	 */
	$.fn.center = function(c) {
		c = c || {};
		return this.each(function() {
			var $s = $(this),
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
(function($) {
	"use strict";

	var E = 'change', P = 'checked';

	$.fn.checkall = function(s) {
		return this.each(function() {
			var $a = $(this),
				b = s || $a.attr('checkall'),
				t = b, f = '',
				i = b.indexOf(' ');

			if (i > 0) {
				t = b.substring(0, i);
				f = b.substring(i+1);
			}

			$a.on(E, function(evt, sup) {
				if (!sup) {
					var c = $a.prop(P);
					$(b)[c ? 'not' : 'filter'](':checked').each(function() {
						$(this).prop(P, c).trigger(E);
					});
				}
			});

			$(t).on(E, f, function() {
				var $b = $(b), bz = $b.length, cz = $b.filter(':checked').length, ca = (bz > 0 && bz == cz);
				if (ca != $a.prop(P)) {
					$a.prop(P, ca).trigger(E, true);
				}
			});
			
			$(b).first().trigger(E);
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[checkall]').checkall();
	});
})(jQuery);
(function($) {
	"use strict";

	var E = 'click.checkorder',
		DS = 'dragstart.checkorder',
		DE = 'dragend.checkorder',
		DO = 'dragover.checkorder',
		DL = 'dragleave.checkorder',
		DP = 'drop.checkorder';

	var _d;
	function _dragstart() {
		_d = this;
	}
	function _dragend() {
		_d = null;
	}
	function _dragover(e) {
		e.preventDefault();
		$(this).addClass('dragover');
	}
	function _dragleave() {
		$(this).removeClass('dragover');
	}

	function _drop() {
		var $l = $(this);
		if (_d && _d !== this) {
			var $d = $(_d), $p = $l.parent(), $ls = $p.children('label');
			if ($ls.filter(function() { return this === _d; }).length) {
				var c = $l.find(':checkbox').prop('checked');

				var vs = [];
				$ls.each(function() {
					if (this !== _d) {
						if (this === $l[0]) {
							vs.push({ label: _d, drop: true, check: c });
						}
						vs.push({ label: this });
					}
				});

				$p.data('checkorder', '').trigger('dropstart.checkorder', vs);
				if ($p.data('checkorder') != 'cancel') {
					$d.find(':checkbox').prop('checked', c);
					$d.insertBefore($l);
					$p.trigger('dropend.checkorder');
				}
			}
		}
		$l.removeClass('dragover');
	}

	function _click() {
		var $c = $(this), $l = $c.closest('label'), $p = $l.parent();

		$p.data('checkorder', '').trigger('checkclick.checkorder', [ $c[0] ]);
		if ($p.data('checkorder') != 'cancel') {
			$l.fadeOut(200, function() {
				var $h = $c.closest('.ui-checks').find('hr');
				if ($c.prop('checked')) {
					$l.insertBefore($h);
				} else {
					$l.insertAfter($h);
				}
				$l.fadeIn(200);
			});
		}
	}

	$.fn.checkorder = function() {
		var $t = $(this), $h = $t.find('hr');
		if ($h.length == 0) {
			$t.prepend($('<hr>'));
		}
		$t.off('.checkorder')
			.on(E, ":checkbox", _click)
			.on(DS, "label", _dragstart)
			.on(DE, "label", _dragend)
			.on(DO, "label", _dragover)
			.on(DL, "label", _dragleave)
			.on(DP, "label", _drop);
		$t.children('label').prop('draggable', true);
		return this;
	}

	// ==================
	$(window).on('load', function() {
		$('.ui-checks.ordered').checkorder();
	});
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
	"use strict";

	$.fn.enableby = function(s) {
		return this.each(function() {
			var $a = $(this),
				b = s || $a.attr('enableby'),
				t = b, f = '',
				i = b.indexOf(' ');

			if (i > 0) {
				t = b.substring(0, i);
				f = b.substring(i+1);
			}

			$(t).on('change', f, function() {
				$a.prop('disabled', $(b).filter(':checked').length == 0);
			});

			if ($(b).length) {
				$(b).first().trigger('change');
			} else {
				$a.prop('disabled', true);
			}
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[enableby]').enableby();
	});
})(jQuery);
(function($) {
	"use strict";

	function _collapse($f, t) {
		t = t || $f.data('fieldset').hideTransition;
		$f.addClass('collapsing').trigger('collapse.fieldset').children(':not(legend)')[t](function() {
			$f.removeClass('collapsing').addClass('collapsed').trigger('collapsed.fieldset');
		});
	}

	function _expand($f, t) {
		t = t || $f.data('fieldset').showTransition;
		$f.addClass('expanding').trigger('expand.fieldset').children(':not(legend)')[t](function() {
			$f.removeClass('expanding collapsed').trigger('expanded.fieldset');
		});
	}

	function collapse($f, t) {
		if (!$f.hasClass('collapsed')) {
			_collapse($f, t);
		}
	}

	function expand($f, t) {
		if ($f.hasClass('collapsed')) {
			_expand($f, t);
		}
	}

	function toggle($f) {
		$f.hasClass('collapsed') ? _expand($f) : _collapse($f);
	}

	function _click() {
		toggle($(this).closest('fieldset'));
	}

	function _init($f, c) {
		if (!$f.data('fieldset')) {
			c = $.extend({}, $.fieldset.defaults, c);

			var h = c.collapsed || $f.hasClass('collapsed'), e = 'click.fieldset';
	
			$f.data('fieldset', c).addClass('ui-fieldset collapsible' + (h ? ' collapsed' : ''));
			$f.children('legend').off(e).on(e, _click);
			$f.children(':not(legend)').toggle(!h);
		}
	}

	var api = {
		collapse: collapse,
		expand: expand,
		toggle: toggle
	};

	$.fieldset = {
		defaults: {
			showTransition: 'slideDown',
			hideTransition: 'slideUp'
		}
	};

	$.fn.fieldset = function(c) {
		var args = [].slice.call(arguments);

		return this.each(function() {
			var $f = $(this);
			if (typeof(c) == 'string') {
				_init($f);
				args[0] = $f;
				api[c].apply($f, args);
				return;
			}
			_init($f, c);
		});
	};

	// FIELDSET DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="fieldset"]').fieldset();
	});
})(jQuery);
(function($) {
	"use strict";

	$.fn.focusme = function() {
		var done = false;
		return this.each(function() {
			if (done) {
				return;
			}

			var $i = $(this), a = $i.attr('focusme') || 'true', $a;

			if (a == 'true') {
				$a = $i.find('input,select,textarea,button').not(':hidden,:disabled,[readonly]').eq(0);
				if ($a.length < 1) {
					$a = $i.find('a').not(':hidden,:disabled').eq(0);
					if ($a.length < 1) {
						$a = $i;
					}
				}
			} else if (a != 'false') {
				$a = $i.find(a).eq(0);
			}
			
			if ($a && $a.length) {
				var $w = $(window), st = $w.scrollTop(), sl = $w.scrollLeft();
				$a.focus();
				$(window).scrollTop(st).scrollLeft(sl);
				done = true;
			}
		});
	};

	$(window).on('load', function() {
		$('[focusme]').focusme();
	});

})(jQuery);
(function($) {
	"use strict";

	$(window).on('load', function() {
		$('input[data-action], button[data-action]').off('click.action').on('click.action', function() {
			$(this).closest('form').attr('action', $(this).data('action'));
		});
	});

})(jQuery);

(function($) {
	"use strict";

	$.fn.changeValue = function(v) {
		return this.each(function() {
			var $t = $(this), o = $t.val();

			$t.val(v);
			if (o != v) {
				$t.trigger('change');
			}
		});
	};

	$.fn.formClear = function(trigger) {
		this.find('textarea, select')[trigger ? 'changeValue' : 'val']('');
		this.find('input').each(function() {
			var $i = $(this);
			switch ($i.attr('type')) {
			case 'hidden':
			case 'button':
			case 'submit':
			case 'reset':
				break;
			case 'checkbox':
			case 'radio':
				var oc = $i.prop('checked');
				$i.prop('checked', false);
				if (oc && trigger) {
					$i.trigger('change');
				}
				break;
			default:
				$i[trigger ? 'changeValue' : 'val']('');
			}
		});
		return this;
	};

	$.fn.formValues = function(vs, trigger) {
		if (vs) {
			for (var n in vs) {
				var v = vs[n];
				this.find(':input').filter(function() { return this.name == n; }).each(function() {
					var $i = $(this);
					switch ($i.attr('type')) {
					case 'file':
					case 'button':
					case 'submit':
					case 'reset':
						break;
					case 'checkbox':
						var va = $.isArray(v) ? v : [ v ];
						var oc = $i.prop('checked'), nc = $.inArray($i.val(), va) >= 0;
						$i.prop('checked', nc);
						if (trigger && nc != oc) {
							$i.trigger('change');
						}
						break;
					case 'radio':
						var oc = $i.prop('checked'), nc = ($i.val() == v);
						$i.prop('checked', nc);
						if (trigger && nc && !oc) {
							$i.trigger('change');
						}
						break;
					default:
						trigger ? $i.changeValue(v) : $i.val(v);
						break;
					}
				});
			}
			return this;
		}

		var m = {}, a = this.serializeArray();
		$.each(a, function(i, v) {
			var ov = m[v.name];
			if (ov === undefined) {
				m[v.name] = v.value;
				return;
			}
			if ($.isArray(ov)) {
				ov.push(v.value);
				return;
			}
			m[v.name] = [ov, v.value];
		});
		return m;
	};

})(jQuery);

(function($) {
	"use strict";

	$.fn.insertText = function(s, append) {
		return this.each(function() {
			var $t = $(this), tv = $t.val();
			var ss = $t.prop('selectionStart') || (append ? tv.length : 0);
			var tb = tv.substring(0, ss), ta = tv.substring(ss);

			$t.val(tb + s + ta).prop('selectionEnd', tb.length + s.length);
		});
	};

})(jQuery);
/**
 * jQuery lightbox plugin
 * This jQuery plugin was inspired and based on 
 *  Lightbox 2 by Lokesh Dhakar (http://www.huddletogether.com/projects/lightbox2/)
 *  jQuery LightBox by Leandro Vieira Pinho (http://leandrovieira.com/projects/jquery/lightbox/)
 */

(function($) {
	"use strict";

	$.lightbox = {
		// Event to bind
		bindEvent: 'click.lightbox',

		// Configuration related to overlay
		overlayBgColor: '#000',		// (string) Background color to overlay; inform a hexadecimal value like: #RRGGBB. Where RR, GG, and BB are the hexadecimal values for the red, green, and blue values of the color.
		overlayOpacity: 0.8,		// (integer) Opacity value to overlay; inform: 0.X. Where X are number from 0 to 9

		// Configuration related to navigation
		fixedNavigation: false,		// (boolean) Boolean that informs if the navigation (next and prev button) will be fixed or not in the interface.
		loopNavigation: false,		// (boolean) Boolean that loop the navigation.

		// Configuration related to images
		textBtnPrev: '&lsaquo;',		// (string) the text of prev button
		textBtnNext: '&rsaquo;',		// (string) the text of next button
		textBtnClose: '&times;',		// (string) the text of close button

		// Configuration related to container image box
		containerBorderSize: 10,			// (integer) If you adjust the padding in the CSS for the container, #lightbox-imagebox, you will need to update this value
		containerResizeSpeed: 400,		// (integer) Specify the resize duration of container image. These number are miliseconds. 400 is default.

		// Configuration related to texts in caption. For example: 'Image # / $' -> 'Image 2 of 8'.
		textPager: '# / $',	// (string) #: Image No.  $: Total Images

		// Configuration related to keyboard navigation
		keyToClose: 'c',		// (string) (c = close) Letter to close the jQuery lightbox interface. Beyond this letter, the letter X and the SCAPE key is used to.
		keyToPrev: 'p',		// (string) (p = previous) Letter to show the previous image
		keyToNext: 'n'		// (string) (n = next) Letter to show the next image.
	};

	/**
	 * $ is an alias to jQuery object
	 */
	$.fn.lightbox = function(settings) {
		// Settings to configure the jQuery lightbox plugin how you like
		settings = $.extend({}, $.lightbox, settings);

		// Caching the jQuery object with all elements matched
		var $jos = this; // This, in this context, refer to jQuery object

		/**
		 * Initializing the plugin calling the start function
		 *
		 * @return boolean false
		 */
		function _initialize() {
			_start(this, $jos); // This, in this context, refer to object (link) which the user have clicked
			return false; // Avoid the browser following the link
		}

		/**
		 * Start the jQuery lightbox plugin
		 *
		 * @param object objClicked The object (link) whick the user have clicked
		 * @param object $jos The jQuery object with all elements matched
		 */
		function _start(objClicked, $jos) {
			$('body').addClass('lightbox-open');

			// Call the function to create the markup structure; style some elements; assign events in some elements.
			_set_interface();

			// Unset image active information
			settings.images = [];
			settings.active = 0;

			// Add an Array (as many as we have), with href and title atributes, inside the Array that storage the images references		
			for (var i = 0; i < $jos.length; i++) {
				var el = $jos[i];
				if (el.tagName == 'A') {
					settings.images.push([el.getAttribute('href'), el.getAttribute('title')]);
				} else if (el.tagName == 'IMG') {
					settings.images.push([el.getAttribute('src'), el.getAttribute('alt')]);
				}
				if (el == objClicked) {
					settings.active = i;
				}
			}

			// Call the function that prepares image exibition
			_set_image_to_view();
		}

		/**
		 * Create the jQuery lightbox plugin interface
		 */
		function _set_interface() {
			// Apply the HTML markup into body tag
			$('body').append('<div id="lightbox-overlay"></div>'
				+ '<div id="lightbox-lightbox">'
				+ '<div id="lightbox-imagebox">'
				+ '<img id="lightbox-image">'
				+ '<div style="" id="lightbox-nav">'
				+ '<a href="#" id="lightbox-btn-prev">'
				+ '<span id="lightbox-txt-prev">' + settings.textBtnPrev + '</span>'
				+ '</a>'
				+ '<a href="#" id="lightbox-btn-next">'
				+ '<span id="lightbox-txt-next">' + settings.textBtnNext + '</span>'
				+ '</a>'
				+ '</div>'
				+ '<a href="#" id="lightbox-loading"></a>'
				+ '</div>'
				+ '<div id="lightbox-statusbox">'
				+ '<div id="lightbox-image-caption"></div>'
				+ '<div id="lightbox-image-number"></div>'
				+ '<a href="#" id="lightbox-btn-close">' + settings.textBtnClose + '</a>'
				+ '</div>'
				+ '</div>');

			// Style overlay and show it
			$('#lightbox-overlay').css({
				backgroundColor: settings.overlayBgColor,
				opacity: settings.overlayOpacity,
			}).fadeIn();

			// set lightbox-imagebox line-height to center image
			_on_resize();

			// Assigning click events in elements to close overlay
			$('#lightbox-overlay, #lightbox-lightbox').click(_finish);

			// Assign the _finish function to lightbox-loading and lightbox-btn-close objects
			$('#lightbox-loading, #lightbox-btn-close').click(_finish);

			// Assign the prev/next handler to prev/next button
			$('#lightbox-btn-prev').click(_on_prev);
			$('#lightbox-btn-next').click(_on_next);

			// If window was resized, calculate the new overlay dimensions
			$(window).on('resize', _on_resize);

			// Enable keyboard navigation
			$(document).keydown(_keyboard_action);
		}

		/**
		 * set lightbox-imagebox line-height to center image
		 */
		function _on_resize() {
			$('#lightbox-imagebox').css('line-height', ($('#lightbox-imagebox').innerHeight() - 2) + 'px');
		}

		/**
		 * navigate to prev image
		 */
		function _on_prev() {
			if (settings.images.length < 1) {
				return true;
			}

			if (settings.active > 0) {
				settings.active--;
				_set_image_to_view();
				return false;
			}

			if (settings.loopNavigation) {
				settings.active = settings.images.length - 1;
				_set_image_to_view();
				return false;
			}
		}

		/**
		 * navigate to next image
		 */
		function _on_next() {
			if (settings.images.length < 1) {
				return true;
			}

			if (settings.active < settings.images.length - 1) {
				settings.active++;
				_set_image_to_view();
				return false;
			}

			if (settings.loopNavigation) {
				settings.active = 0;
				_set_image_to_view();
				return false;
			}
		}

		/**
		 * Prepares image exibition; doing a image's preloader to calculate it's size
		 */
		function _set_image_to_view() {
			// Show the loading
			$('#lightbox-loading').show();
			$('#lightbox-image, #lightbox-statusbox').hide();
			$('#lightbox-nav')[settings.fixedNavigation ? 'addClass' : 'removeClass']('lightbox-fixed');

			// Image preload process
			var img = new Image();
			img.onload = function() {
				$('#lightbox-image').attr('src', settings.images[settings.active][0]);

				// Perfomance an effect in the image container resizing it
				_show_image();

				//	clear onLoad, IE behaves irratically with animated gifs otherwise
				img.onload = function() { };
			};
			img.src = settings.images[settings.active][0];
		};


		/**
		 * Show the prepared image
		 */
		function _show_image() {
			$('#lightbox-loading').hide();
			$('#lightbox-image').fadeIn(function() {
				_show_image_data();
				_set_navigation();
			});
			_preload_neighbor_images();
		};

		/**
		 * Show the image information
		 */
		function _show_image_data() {
			if (settings.images.length > 0) {
				$('#lightbox-image-caption').html(settings.images[settings.active][1]);

				var tpm = {
					'#': settings.active + 1,
					"$": settings.images.length
				};

				$('#lightbox-image-number').html(settings.textPager.replace(/[\#\$]/g, function(c) {
					return tpm[c];
				}));
			}
			$('#lightbox-statusbox').slideDown('fast');
		}

		/**
		 * Display the button navigations
		 */
		function _set_navigation() {
			// Show the prev button, if not the first image in set
			$('#lightbox-btn-prev')[((settings.loopNavigation && settings.images.length > 1) || settings.active > 0) ? 'addClass' : 'removeClass']('lightbox-has-prev');

			// Show the next button, if not the last image in set
			$('#lightbox-btn-next')[((settings.loopNavigation && settings.images.length > 1) || settings.active < settings.images.length - 1) ? 'addClass' : 'removeClass']('lightbox-has-next');
		}

		/**
		 * Perform the keyboard actions
		 */
		function _keyboard_action(evt) {
			var keycode = evt.keyCode,
				escapeKey = evt.DOM_VK_ESCAPE || 27,
				key = String.fromCharCode(keycode).toLowerCase();

			// Verify the keys to close the ligthBox
			if ((key == settings.keyToClose) || (key == 'x') || (keycode == escapeKey)) {
				return _finish();
			}

			// Verify the key to show the previous image
			if ((key == settings.keyToPrev) || (keycode == 37)) {
				return _on_prev();
			}

			// Verify the key to show the next image
			if ((key == settings.keyToNext) || (keycode == 39)) {
				return _on_next();
			}
		}

		/**
		 * Preload prev and next images being showed
		 */
		function _preload_neighbor_images() {
			if (settings.images.length) {
				var p = settings.active - 1, n = settings.active + 1;
				(new Image()).src = settings.images[p < 0 ? settings.images.length - 1 : p][0];
				(new Image()).src = settings.images[n >= settings.images.length ? 0 : n][0];
			}
		}

		/**
		 * Remove overlay
		 */
		function _remove_overlay() {
			$('#lightbox-overlay').remove();
		}

		/**
		 * Remove jQuery lightbox plugin HTML markup
		 */
		function _finish() {
			$(document).off('keydown', _keyboard_action);
			$(window).off('resize', _on_resize);

			$('#lightbox-lightbox').remove();
			$('#lightbox-overlay').fadeOut(_remove_overlay);

			$('body').removeClass('lightbox-open');
			return false;
		}

		// Return the jQuery object for chaining. The off method is used to avoid click conflict when the plugin is called more than once
		return this.off(settings.bindEvent).on(settings.bindEvent, _initialize);
	};
})(jQuery);
(function($) {
	"use strict";

	function linkify(node, c) {
		switch (node.nodeType) {
		case 3: // Text Node
			c.regexp.lastIndex = 0;
			var r = c.regexp.exec(node.nodeValue);
			if (r) {
				var $a = $('<a>', { target: c.target, href: r[0] }).text(r[0]);
				if (c.prepend) {
					$a.prepend(c.prepend);
				}
				if (c.append) {
					$a.append(c.append);
				}

				var m = node.splitText(r.index);
				m.splitText(r[0].length);
				$(m).replaceWith($a);
				return 1;
			}
			break;
		case 1: // Element Node
			if (node.childNodes && !c.ignore.test(node.tagName)) {
				for (var i = 0; i < node.childNodes.length; i++) {
					i += linkify(node.childNodes[i], c);
				}
			}
			break;
		}
		return 0;
	}

	$.linkify = {
		defaults: {
			ignore: /(script|style|a)/i,
			// URLs starting with http://, https://
			regexp: /https?:\/\/[\w~!@#\$%&\*\(\)_\-\+=\[\]\|:;,\.\?\/']+/i,
			target: '_blank',
			prepend: '',
			append: ''
		}
	};

	$.fn.linkify = function(c) {
		c = $.extend({}, $.linkify.defaults, c);

		return this.each(function() {
			linkify(this, c);
			$(this).removeAttr('linkify');
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[linkify]').linkify();
	});
})(jQuery);
(function($) {
	"use strict";

	function _clearTimeout($el) {
		//if this element has delayed mask scheduled then remove it
		var t = $el.data("_mask_timeout");
		if (t) {
			clearTimeout(t);
			$el.removeData("_mask_timeout");
		}

		//if this element has unmask timeout scheduled then remove it
		t = $el.data("_unmask_timeout");
		if (t) {
			clearTimeout(t);
			$el.removeData("_unmask_timeout");
		}
	}

	function _stopEvent(evt) {
		evt.preventDefault();
		evt.stopPropagation();
	}

	function doMask($el, c) {
		if ($el.isLoadMasked()) {
			unMask($el);
		} else {
			_clearTimeout($el);
		}

		var fs = ($el.prop('tagName') == 'BODY' ? ' fullscreen' : '');

		var $lm = $('<div>', { 'class': "ui-loadmask" + fs });
		if (c.cssClass) {
			$lm.addClass(c.cssClass);
		}

		var $ll = $('<div class="ui-loadmask-load">');
		if (c.content) {
			$lm.append($(c.content));
		} else {
			var $li = $('<div class="ui-loadmask-icon">'),
				$lt = $('<div class="ui-loadmask-text">');

			$ll.append($li).append($lt);

			if (c.html || c.text) {
				$ll.addClass('ui-loadmask-hasmsg');
				if (c.html) {
					$lt.html(c.html);
				} else {
					$lt.text(c.text);
				}
			}
			$lm.append($ll);
		}

		if ($el.css("position") == "static") {
			$el.addClass("ui-loadmasked-relative");
		}
		if (c.mask) {
			var $m = $('<div>', { 'class': "ui-loadmask-mask" + fs });
			$el.append($m);
		}

		$el.append($lm).addClass("ui-loadmasked");

		if (c.timeout > 0) {
			$el.data("_unmask_timeout", setTimeout(function() {
				unMask($el);
			}, c.timeout));
		}
		if (c.keyboard) {
			$el.on('keydown.loadmask', _stopEvent);
		}
	}

	function unMask($el) {
		_clearTimeout($el);

		$el.off('.loadmask');
		$el.find(".ui-loadmask-mask, .ui-loadmask").remove();
		$el.removeClass("ui-loadmasked ui-loadmasked-relative");
	}

	$.loadmask = {
		defaults: {
			cssClass: '',		// css class for the mask element
			mask: true,			// add mask layer
			keyboard: true,		// add keydown event handler for the mask element to prevent input
			delay: 0,			// delay in milliseconds before element is masked. If unloadmask() is called before the delay times out, no mask is displayed. This can be used to prevent unnecessary mask display for quick processes.
			timeout: 0,			// timeout in milliseconds for automatically unloadmask
		}
	};

	/**
	 * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
	 * @param content  html content that will be add to the loadmask
	 * @param html  html message that will be display
	 * @param text  text message that will be display (html tag will be escaped)
	 */
	$.fn.loadmask = function(c) {
		if (typeof (c) == 'string') {
			c = { text: c };
		}
		c = $.extend({}, $.loadmask.defaults, c);
		return this.each(function() {
			var $el = $(this);
			if (c.delay > 0) {
				$el.data("_mask_timeout", setTimeout(function() {
					doMask($el, c);
				}, c.delay));
			} else {
				doMask($el, c);
			}
		});
	};

	/**
	 * Removes mask from the element(s). Accepts both single and multiple selectors.
	 */
	$.fn.unloadmask = function() {
		return this.each(function() {
			unMask($(this));
		});
	};

	/**
	 * Checks if a single element is masked. Returns false if mask is delayed or not displayed. 
	 */
	$.fn.isLoadMasked = function() {
		return this.hasClass("ui-loadmasked");
	};
})(jQuery);
(function($) {
	"use strict";

	var ws = /[\s\u0085\u00a0\u2000\u3000]/g;

	function split(s) {
		var ss = s.split(ws), rs = [];
		for (var i = 0; i < ss.length; i++) {
			if (ss[i].length) {
				rs.push(ss[i])
			}
		}
		return rs;
	}

	function index_any(s, c) {
		var i = 0;
		while (s.length > 0) {
			for (var j = 0; j < c.markups.length; j++) {
				var m = c.markups[j], l = m.length, t = s.substring(0, l);
				if (t == m || (c.caseInsensitive && t.toLowerCase() == m)) {
					return [i, l]
				}
			}
			s = s.substring(1);
			i++;
		}
		return false;
	}

	function markup(node, c) {
		switch (node.nodeType) {
		case 3: // Text Node
			var r = index_any(node.nodeValue, c);
			if (r) {
				var m = node.splitText(r[0]);
				m.splitText(r[1]);
				$(m).wrap(c.wrap);
				return 1;
			}
			break;
		case 1: // Element Node
			if (node.childNodes && !c.ignore.test(node.tagName)) {
				for (var i = 0; i < node.childNodes.length; i++) {
					i += markup(node.childNodes[i], c);
				}
			}
			break;
		}
		return 0;
	}

	$.markup = {
		defaults: {
			caseInsensitive: true,
			ignore: /(script|style|mark)/i,
			wrap: '<mark></mark>',
		}
	};

	$.fn.markup = function(o) {
		if ($.isArray(o)) {
			o = { markups: o };
		} else if (typeof(o) == 'string') {
			o = { markup: o };
		}

		return this.each(function() {
			var $t = $(this), c = $.extend({}, $.markup.defaults, o);

			c.markups ||= split(c.markup || $t.attr('markup') || '');
			if (c.markups.length) {
				if (c.caseInsensitive) {
					for (var i = 0; i < c.markups.length; i++) {
						c.markups[i] = c.markups[i].toLowerCase();
					}
				}
				markup(this, c);
			}
			$t.removeAttr('markup');
		});
	};


	// ==================
	$(window).on('load', function() {
		$('[markup]').markup();
	});
})(jQuery);
// jQuery Nice Select - v1.1.0
// https://github.com/hernansartorio/jquery-nice-select
// Made by Hernán Sartorio
// Modified by Frank Wang

(function($) {
	"use strict";

	function __document_click(evt) {
		if ($(evt.target).closest('.ui-nice-select').length === 0) {
			$('.ui-nice-select').removeClass('open');
		}
	}

	function __dropdown_keydown(evt) {
		var $dropdown = $(this);
		var $focused = ($dropdown.find('.focus') || $dropdown.find('.selected')).first();

		switch (evt.key) {
		case ' ': // Space
		case 'Enter':
			if ($dropdown.hasClass('open')) {
				$focused.trigger('click');
			} else {
				$dropdown.trigger('click');
			}
			return false;
		case 'ArrowDown':
			if (!$dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			} else {
				var $next = ($focused.length > 0 ? $focused.nextAll('li:not(.disabled)') : $dropdown.find('li:not(.disabled)')).first();
				if ($next.length > 0) {
					$dropdown.find('.focus').removeClass('focus');
					$next.addClass('focus').focus();
				}
			}
			return false;
		case 'ArrowUp':
			if (!$dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			} else {
				var $prev = ($focused.length > 0 ? $focused.prevAll('li:not(.disabled)').first() : $dropdown.find('li:not(.disabled)').last());
				if ($prev.length > 0) {
					$dropdown.find('.focus').removeClass('focus');
					$prev.addClass('focus').focus();
				}
			}
			return false;
		case 'Escape':
			if ($dropdown.hasClass('open')) {
				$dropdown.trigger('click');
			}
			break;
		case 'Tab':
			if ($dropdown.hasClass('open')) {
				return false;
			}
		}
	}

	function __dropdown_click(evt) {
		evt.stopPropagation();

		var $dropdown = $(this);

		$('.ui-nice-select').not($dropdown).removeClass('open');
		$dropdown.toggleClass('open');

		if ($dropdown.hasClass('open')) {
			// Close when clicking outside
			$(document).on('click.nice_select', __document_click);
		} else {
			$dropdown.focus();

			// Unbind existing events in case that the plugin has been initialized before
			$(document).off('.nice_select');
		}
	}

	function __dropdown_option_click() {
		var $li = $(this);

		if ($li.hasClass('selected')) {
			return;
		}

		var $dropdown = $li.closest('.ui-nice-select'),
			$select = $dropdown.prev('select'),
			val = $li.attr('value');

		if ($dropdown.hasClass('multiple')) {
			var vs = $select.val() || [];
			vs.push(val);
			val = vs;
		}
		$select.val(val).trigger('change');
	}

	// multiple only
	function __dropdown_current_click() {
		var $t = $(this), val = $t.attr('value'),
			$dropdown = $t.closest('.ui-nice-select'),
			$select = $dropdown.prev('select');

		$t.remove();
		
		$dropdown.find('li').filter(function() { return $(this).attr('value') == val; }).removeClass('selected');
		var val = [];
		$dropdown.find('.current').each(function() {
			val.push($(this).attr('value'));
		})
		$select.val(val);
		return false;
	}

	function __select_change() {
		change.apply($(this));
	}

	function change() {
		this.each(function() {
			var $select = $(this), vs = $select.val();
			var $dropdown = $select.next('.ui-nice-select');

			var eq = $.isArray(vs)
				? function(v, a) {
					for (var i = 0; i < a.length; i++) {
						if (v == a[i]) {
							return true;
						}
					}
					return false;
				}
				: function(v, a) {
					return v == a;
				};

			$dropdown.find('.current').remove();
			$dropdown.find('.selected').removeClass('selected');
			$dropdown.find('li')
				.filter(function() { return eq($(this).attr('value'), vs); })
				.addClass('selected')
				.each(function() {
					var $t = $(this);
					$dropdown.append($('<span>', { 'class': 'current', 'value': $t.attr('value')}).text($t.attr('display') || $t.text()));
				});
		});
	}

	function update() {
		this.each(function() {
			var $select = $(this);
			var $dropdown = $select.next('.ui-nice-select');

			if ($dropdown.length) {
				$dropdown.remove();
				create_nice_select($select);

				if ($dropdown.hasClass('open')) {
					$select.next().trigger('click');
				}
			}
		});
	}

	function update() {
		this.each(function() {
			var $select = $(this);
			var $dropdown = $select.next('.ui-nice-select');

			if ($dropdown.length) {
				$dropdown.remove();
				create_nice_select($select);

				if ($dropdown.hasClass('open')) {
					$select.next().trigger('click');
				}
			}
		});
	}

	function destroy() {
		this.each(function() {
			var $select = $(this);
			var $dropdown = $select.next('.ui-nice-select');

			if ($dropdown.length) {
				$dropdown.remove();
				$select.css('display', '').off('.nice_select');
			}
		});
		if ($('.ui-nice-select').length == 0) {
			$(document).off('.nice_select');
		}
	}

	function no_css_pointer_events() {
		// Detect CSS pointer-events support, for IE <= 10. From Modernizr.
		var style = document.createElement('a').style;
		style.cssText = 'pointer-events:auto';
		if (style.pointerEvents !== 'auto') {
			$('html').addClass('ui-nice-select-no-csspointerevents');
		}
	}

	function create_nice_select($select) {
		var $options = $select.find('option'),
			$selected = $select.find('option:selected'),
			$ul = $('<ul></ul>'),
			$dropdown = $('<div></div>')
				.addClass('ui-nice-select')
				.addClass($select.attr('class') || '')
				.addClass($select.prop('disabled') ? 'disabled' : '')
				.addClass($select.prop('multiple') ? 'multiple' : '')
				.attr('tabindex', $select.prop('disabled') ? null : ($select.attr('tabindex') || '0'))
				.append($ul);

		$selected.each(function() {
			var $op = $(this);
			$dropdown.append($('<span>', { 'class': 'current', 'value': $op.val()}).text($op.attr('display') || $op.text()));
		});

		$options.each(function() {
			var $option = $(this);

			$ul.append($('<li></li>')
				.attr('value', $option.val())
				.attr('display', ($option.attr('display') || ''))
				.addClass(
					($option.is(':selected') ? ' selected' : '') +
					($option.is(':disabled') ? ' disabled' : ''))
				.attr('tabindex', $option.is(':disabled') ? null : '0')
				.text($option.text())
			);
		});

		// Open, close
		$dropdown.click(__dropdown_click);

		// Keyboard events
		$dropdown.keydown(__dropdown_keydown);

		// Option click
		$dropdown.on('click', 'li:not(.disabled)', __dropdown_option_click);

		// multiple
		if ($dropdown.hasClass('multiple')) {
			$dropdown.on('click', '.current', __dropdown_current_click)
		}

		$select.after($dropdown);

		$select.on('change.nice_select', __select_change);
	}

	var api = {
		'update': update,
		'destroy': destroy
	};

	$.fn.niceSelect = function(method) {
		// Methods
		if (typeof method == 'string') {
			api[method].apply(this);
			return this;
		}

		// Hide native select
		this.hide();

		// Create custom markup
		this.each(function() {
			var $s = $(this);
			if ($s.next().hasClass('ui-nice-select')) {
				update.apply($s);
			} else {
				create_nice_select($s);
			}
		});

		return this;
	};

	// niceSelect DATA-API
	// ==================
	$(window).on('load', function() {
		no_css_pointer_events();
		$('[data-spy="niceSelect"]').niceSelect();
	});

})(jQuery);
(function($) {
	var ArrowClasses = {
		'top left': 'dn hr1 vb',
		'top right': 'dn hl1 vb',
		'top center': 'dn hc vb',
		'bottom left': 'up hr1 vt',
		'bottom right': 'up hl1 vt',
		'bottom center': 'up hc vt',
		'left bottom': 'rt hr vt1',
		'left top': 'rt hr vb1',
		'left middle': 'rt hr vm',
		'right bottom': 'lt hl vt1',
		'right top': 'lt hl vb1',
		'right middle': 'lt hl vm'
	};

	function _position($p, $t, position) {
		var tw = $t.outerWidth(), th = $t.outerHeight(), p = $t.offset();
		var pw = $p.outerWidth(), ph = $p.outerHeight();

		switch (position) {
		case 'top left':
			p.top -= (ph + 11);
			p.left -= (pw - 50);
			break;
		case 'top right':
			p.top -= (ph + 11);
			p.left += (tw - 50);
			break;
		case 'top center':
			p.top -= (ph + 11);
			p.left += (tw - pw) / 2;
			break;
		case 'bottom left':
			p.top += th + 11;
			p.left -= (pw - 50);
			break;
		case 'bottom right':
			p.top += th + 11;
			p.left += (tw - 50);
			break;
		case 'bottom center':
			p.top += th + 11;
			p.left += (tw - pw) / 2;
			break;
		case 'left bottom':
			p.left -= (pw + 11);
			p.top -= 20;
			break;
		case 'left top':
			p.left -= (pw + 11);
			p.top += th - ph + 20;
			break;
		case 'left middle':
			p.left -= (pw + 11);
			p.top -= (ph - th) / 2;
			break;
		case 'right bottom':
			p.left += tw + 11;
			p.top -= 20;
			break;
		case 'right top':
			p.left += tw + 11;
			p.top += th - ph + 20;
			break;
		case 'right middle':
			p.left += tw + 11;
			p.top -= (ph - th) / 2;
			break;
		}

		return p;
	}

	function _in_screen($p, p) {
		var $w = $(window),
			wt = $w.scrollTop(), wl = $w.scrollLeft(),
			wb = wt + $w.height(), wr = wl + $w.width(),
			pr = p.left + $p.outerWidth(), pb = p.top + $p.outerHeight();

		return p.left >= wl && p.left <= wr
			&& p.top >= wt && p.top <= wb
			&& pr >= wl && pr <= wr
			&& pb >= wt && pb <= wb;
	}

	function _positions($p, $t, ps) {
		for (var i = 0; i < ps.length; i++) {
			var p = _position($p, $t, ps[i]);
			p.position = ps[i];
			if (_in_screen($p, p)) {
				return p;
			}
			ps[i] = p;
		}
		return ps[0];
	}

	function _center($p, $w) {
		var p = {
			left: $w.scrollLeft() + ($w.outerWidth() - $p.outerWidth()) / 2,
			top: $w.scrollTop() + ($w.outerHeight() - $p.outerHeight()) / 2
		};

		p.left = (p.left < 10 ? 10 : p.left);
		p.top = (p.top < 10 ? 10 : p.top);
		return p;
	}

	function _align($p, trigger, position) {
		$p.css({
			display: 'block',
			visibility: 'hidden'
		});

		var p, ac, $a = $p.find('.ui-popup-arrow').hide();
		if (position == 'center') {
			p = _center($p, $(window));
		} else {
			var $t = $(trigger);

			ac = ArrowClasses[position];
			if (ac) {
				p = _position($p, $t, position);
			} else {
				switch (position) {
				case 'top':
					p = _positions($p, $t, ['top center', 'top left', 'top right']);
					break;
				case 'bottom':
					p = _positions($p, $t, ['bottom center', 'bottom left', 'bottom right']);
					break;
				case 'left':
					p = _positions($p, $t, ['left middle', 'left bottom', 'left top']);
					break;
				case 'right':
					p = _positions($p, $t, ['right middle', 'right bottom', 'right top']);
					break;
				//case 'auto':
				default:
					p = _positions($p, $t, [
						'bottom center', 'bottom left', 'bottom right',
						'right middle', 'right bottom', 'right top',
						'top center', 'top left', 'top right',
						'right middle', 'right bottom', 'right top'
					]);
					break;
				}
				ac = ArrowClasses[p.position];
			}
		}

		$p.css({
			top: p.top,
			left: p.left,
			visibility: 'visible'
		});
		if (ac) {
			$a.attr('class', 'ui-popup-arrow ' + ac).show();
		}
	}

	function _masker() {
		return $('.ui-popup-mask');
	}
	function _active() {
		return $('.ui-popup-wrap:visible>.ui-popup-frame>.ui-popup');
	}
	function _wrapper($c) {
		return $c.parent().parent('.ui-popup-wrap');
	}
	function _data($c) {
		return $c.data('popup');
	}

	function toggle($c, trigger) {
		trigger = trigger || window;
		var $p = _wrapper($c);
		if ($p.is(':hidden')) {
			show($c, trigger);
			return;
		}

		if (_data($c).trigger === trigger) {
			hide($c);
			return;
		}

		show($c, trigger);
	}

	function hide($c) {
		var $p = _wrapper($c);
		if ($p.is(':visible')) {
			$c.trigger('hide.popup');
			$p.hide();
			$('body').removeClass('ui-popup-noscroll');
			$(document).off('.popup');
			$(window).off('.popup');
			$c.trigger('hidden.popup');
		}
		_masker().hide();
	}

	function show($c, trigger) {
		hide(_active());

		var $p = _wrapper($c), c = _data($c);

		if (c.mask) {
			_masker().show();
		}

		if (c.loaded || !c.ajax.url) {
			_show($p, $c, c, trigger);
			return;
		}

		c.showing = trigger || window;
		load($c, c);
	}

	function _bind(c) {
		$(document).off('.popup');
		if (c.mouse) {
			$(document).on('click.popup', __doc_click);
		}
		if (c.keyboard) {
			$(document).on('keydown.popup', __doc_keydown);
		}
		if (c.resize) {
			$(window).on('resize.popup', __doc_resize);
		}
	}

	function _show($p, $c, c, trigger) {
		c.trigger = trigger || window;

		$c.trigger('show.popup', c.trigger);

		if (!c.scroll) {
			$('body').addClass('ui-popup-noscroll');
		}

		$p.find('.ui-popup-closer').toggle(c.closer);

		_align($p, c.trigger, c.position);

		$p.focus().children('.ui-popup-frame').hide()[c.transition](function() {
			_bind(c);
			if (c.focus) {
				$p.find(c.focus).eq(0).focus();
			}
			$c.trigger('shown.popup', c.trigger);
		});
	}

	function __doc_click(evt) {
		if ($(evt.target).closest('.ui-popup-wrap').length) {
			return;
		}
		hide(_active());
	}

	function __doc_keydown(evt) {
		if (evt.keyCode == 27) { // Esc
			hide(_active());
		}
	}

	function __doc_resize() {
		var $c = _active(), $p = _wrapper($c), c = _data($c);
		_align($p, c.trigger, c.position);
	}

	function load($c, c) {
		var $p = _wrapper($c);

		c = $.extend(_data($c), c);

		if (c.loader) {
			$c.html('<div class="ui-popup-loader"></div>');
			_align($p, c.showing, c.position);
		}

		_load($p, $c, c);
	}

	function _load($p, $c, c) {
		var seq = ++c.sequence;

		$p.addClass('loading').find('.ui-popup-closer, .ui-popup-arrow').hide();

		$c.trigger('load.popup');

		$.ajax($.extend({}, c.ajax, {
			success: function(data, status, xhr) {
				if (seq == c.sequence) {
					c.ajaxDone.call($c, data, status, xhr);
					$c.find('[popup-dismiss="true"]').click(function() {
						hide($c);
						return false;
					});
					c.loaded = true;
					$c.trigger('loaded.popup', [ data, status, xhr ]);
				}
			},
			error: function(xhr, status, err) {
				if (seq == c.sequence) {
					c.ajaxFail.call($c, xhr, status, err);
					$c.trigger('failed.popup', [ xhr, status, err ]);
				}
			},
			complete: function() {
				$p.removeClass('loading');
				if (seq == c.sequence && c.showing) {
					_show($p, $c, c, c.showing);
					delete c.showing;
				}
			}
		}));
	}

	function _ajaxFail(xhr, status, err) {
		var $c = $(this), $e = $('<div class="ui-popup-error">');

		var j = xhr.responseJSON, t = xhr.responseText;
		if (j) {
			var e = j.error;
			$e.addClass('text').text(typeof(e) == 'string' ? e : JSON.stringify(j, null, 4));
		} else if (t) {
			$e.html(t);
		} else {
			$e.addClass('text').text((xhr.status ? (xhr.status + ' ') : '')  + (err || status || 'error'));
		}

		$c.empty().append($e);
	}

	function _ajaxDone(data, status, xhr) {
		$(this).html(xhr.responseText);
	}

	function update($c, c) {
		if (c) {
			c = $.extend(_data($c), c);
			var $p = _wrapper($c);
			if (!$p.is(':hidden')) {
				_bind(c);
				_masker().toggle(c.mask);
			}
		}
	}

	function trigger($c, evt) {
		var a = [].slice.call(arguments, 2);
		$(_data($c).trigger).trigger(evt, a);
	}

	function destroy($c) {
		_wrapper($c).remove();
	}

	function _camelCase(s) {
		s = s.charAt(0).toLowerCase() + s.slice(1);
		return s.replace(/[-_](.)/g, function(m, g) {
			return g.toUpperCase();
		});
	}

	function _options($c) {
		var fs = ['ajax-done', 'ajax-fail'];
		var bs = ['loaded', 'autoload', 'mask', 'loader', 'closer', 'mouse', 'keyboard', 'resize', 'scroll'];

		var c = {};
		$.each($c[0].attributes, function(i, a) {
			var p = a.name.substring(0, 6),
				n = a.name.substring(6),
				v = a.value;

			if ('popup-' != p || !v) {
				return;
			}

			if ($.inArray(n, fs) >= 0) {
				c[_camelCase(n)] = new Function(v);
				return;
			}

			if ($.inArray(n, bs) >= 0) {
				v = (v === 'true');
			}

			if ('ajax-' == n.substring(0, 5)) {
				c.ajax ||= {};
				c.ajax[_camelCase(n.substring(5))] = v;
			} else {
				c[_camelCase(n)] = v;
			}
		});
		return c;
	}

	function _init($c, c) {
		if (_masker().length == 0) {
			$('<div class="ui-popup-mask">').appendTo('body');
		}

		var $p = _wrapper($c);
		if ($p.length) {
			update($c, c);
			return;
		}

		c = $.extend({ sequence: 0 }, $.popup.defaults, _options($c), c);

		var $f = $('<div class="ui-popup-frame" tabindex="0">')
			.append($('<div class="ui-popup-arrow">'))
			.append($('<i class="ui-popup-closer"></i>').hide().on('click', function() {
				hide($c);
			}));

		$p = $('<div class="ui-popup-wrap">').append($f).appendTo('body');

		if (c.cssClass) {
			$p.addClass(c.cssClass);
		}

		$c.appendTo($f).data('popup', c).addClass('ui-popup').show();

		if (c.ajax.url) {
			c.loaded = false;
			if (c.autoload) {
				_load($p, $c, c);
			}
		} else {
			c.loaded = true;
			$c.find('[popup-dismiss="true"]').click(function() {
				hide($c);
				return false;
			});
		}
	}

	var api = {
		load: load,
		show: show,
		hide: hide,
		toggle: toggle,
		update: update,
		trigger: trigger,
		destroy: destroy
	};

	$.fn.popup = function(c) {
		var args = [].slice.call(arguments);
		return this.each(function() {
			var $c = $(this);

			if (typeof (c) == 'string') {
				var p = _data($c);
				if (!p) {
					_init($c);
				}
				args[0] = $c;
				api[c].apply($c, args);
				return;
			}

			_init($c, c);
		});
	};

	$.popup = function() {
		var $c = _active();
		$c.popup.apply($c, arguments);
		return $c;
	};

	$.popup.defaults = {
		position: 'auto',
		transition: 'slideDown',
		mask: false,
		loader: false,
		closer: false,
		focus: '',
		mouse: true,
		keyboard: true,
		resize: true,
		scroll: true,
		ajax: {},
		ajaxDone: _ajaxDone,
		ajaxFail: _ajaxFail
	};

	// POPUP DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="popup"]').popup();

		$('body').on('click.popup', '[popup-target]', function(evt) {
			evt.stopPropagation();
			var $t = $(this), c = _options($t);
			$($t.attr('popup-target')).popup(c).popup('toggle', this);
			return false;
		});
	});

})(jQuery);
(function($) {
	"use strict";

	$.fn.scrollIntoView = function(speed, easing, callback) {
		if (!this.length) {
			return this;
		}

		var $e = this.first(), $w = $(window), eo = $e.offset(),
			wh = $w.height(), ww = $w.width(),
			st = $w.scrollTop(), sb = st + wh, sl = $w.scrollLeft(), sr = sl + ww,
			et = eo.top, eh = $e.outerHeight(), eb = et + eh,
			el = eo.left, ew = $e.outerWidth(), er = el + ew,
			x = sl > er ? el : (sr < el ? (ew > ww ? el : el - (ww - ew)) : -1),
			y = st > eb ? et : (sb < et ? (eh >= wh ? et : et - (wh - eh)) : -1);

		var ss = {};
		if (x >= 0) { ss.scrollLeft = x; }
		if (y >= 0) { ss.scrollTop = y; }
		$('html').animate(ss, speed, easing, callback);
		return this;
	};

})(jQuery);
(function($) {
	"use strict";

	$.fn.selectText = function() {
		var $t = $(this);
		if ($t.length) {
			var doc = document, el = $t.get(0);
			if (doc.body.createTextRange) {
				var r = doc.body.createTextRange();
				r.moveToElementText(el);
				r.select();
			} else if (window.getSelection) {
				var ws = window.getSelection(), r = doc.createRange();
				r.selectNodeContents(el);
				ws.removeAllRanges();
				ws.addRange(r);
			}
		}
	};

})(jQuery);
﻿// jQuery simple color picker
// https://github.com/rachel-carvalho/simple-color-picker
// Modified by Frank Wang

(function($) {
	"use strict";

	$.simpleColorPicker = {
		defaults: {
			colorsPerLine: 8,
			colors: ['#000000', '#444444', '#666666', '#999999', '#cccccc', '#eeeeee', '#f3f3f3', '#ffffff'
				, '#ff0000', '#ff9900', '#ffff00', '#00ff00', '#00ffff', '#0000ff', '#9900ff', '#ff00ff'
				, '#f4cccc', '#fce5cd', '#fff2cc', '#d9ead3', '#d0e0e3', '#cfe2f3', '#d9d2e9', '#ead1dc'
				, '#ea9999', '#f9cb9c', '#ffe599', '#b6d7a8', '#a2c4c9', '#9fc5e8', '#b4a7d6', '#d5a6bd'
				, '#e06666', '#f6b26b', '#ffd966', '#93c47d', '#76a5af', '#6fa8dc', '#8e7cc3', '#c27ba0'
				, '#cc0000', '#e69138', '#f1c232', '#6aa84f', '#45818e', '#3d85c6', '#674ea7', '#a64d79'
				, '#990000', '#b45f06', '#bf9000', '#38761d', '#134f5c', '#0b5394', '#351c75', '#741b47'
				, '#660000', '#783f04', '#7f6000', '#274e13', '#0c343d', '#073763', '#20124d', '#4C1130'],
			showEffect: 'show',
			hideEffect: 'hide'
		}
	};

	function positionAndShowBox($txt, $box) {
		var pos = $txt.offset(), tw = $txt.outerWidth(), bw = $box.outerWidth();

		var left = tw > bw ? pos.left : pos.left - (bw - tw);

		$box.css({
			left: left < 0 ? 0 : left,
			top: pos.top + $txt.outerHeight()
		});

		showBox($box);
	}

	function showBox($box) {
		var opts = $box.data('opts');
		$box[opts.showEffect]();
		$(document).on('click.simple_color_picker', function() {
			hideBox($box);
		});
	}

	function hideBox($box) {
		var opts = $box.data('opts');
		$box[opts.hideEffect]();
		$(document).off('.simple_color_picker');
	}

	function initBox($txt, opts) {
		var $box = $('<div>', {
			'id': 'color_picker_' + ($txt.attr('id') || new Date().getTime()),
			'class': 'ui-simple-color-picker'
		}).hide().appendTo('body');

		var $ul;
		for (var i = 0; i < opts.colors.length; i++) {
			if (i % opts.colorsPerLine == 0) {
				$ul = $('<ul>');
				$box.append($ul);
			}

			var c = opts.colors[i];
			$ul.append($('<li>', {
				'style': 'background-color: ' + c,
				'title': c
			}));
		}

		$box.data('opts', opts);
		$txt.data('simpleColorPicker', $box);
		return $box;
	}

	var api = {
		destroy: function() {
			this.each(function() {
				var $box = $(this).data('simpleColorPicker');
				if ($box) {
					$box.remove();
				}
			}).off('.simple_color_picker').removeData('simpleColorPicker');
		}
	};

	$.fn.simpleColorPicker = function(options) {
		// Methods
		if (typeof options == 'string') {
			api[options].apply(this);
			return this;
		}

		return this.each(function() {
			var $txt = $(this),
				opts = $.extend({}, $.simpleColorPicker.defaults, options),
				$box = initBox($txt, opts);

			$box.find('li').click(function() {
				if ($txt.is('input')) {
					$txt.val($(this).attr('title'));
				}
				$txt.trigger('change', $(this).attr('title'));
				hideBox($box);
			});

			$box.click(function(evt) {
				evt.stopPropagation();
			});

			$txt.on('click.simple_color_picker', function() {
				setTimeout(function() {
					positionAndShowBox($txt, $box);
				})
			});

			if ($txt.is('input')) {
				$txt.on('focus.simple_color_picker', function() {
					positionAndShowBox($txt, $box);
				});
			}
		});
	};

	// COLOR-PICKER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="simpleColorPicker"]').simpleColorPicker();
	});

})(jQuery);
(function($) {
	'use strict';

	function sortable_onclick(evt) {
		var $e = $(evt.target),
			col = $e.data('sortCol') || $e.text(),
			dir = $e.data('sortDir') || '';

		if ($e.hasClass('sorted')) {
			dir = $e.hasClass('asc') ? 'desc' : 'asc';
		}

		$(this).trigger('sort.sortable', [ col, dir ]);
	}

	function set_sorted($s, col, dir) {
		$s.find('.sortable').removeClass('sorted desc asc')
			.filter('[data-sort-col="' + col + '"]').addClass('sorted ' + (dir || 'asc'));
	}

	$.fn.sortable = function(api, col, dir) {
		if (api == 'sorted') {
			set_sorted(this, col, dir);
			return this;
		}

		return this.addClass('ui-sortable')
			.off('click.sortable')
			.on('click.sortable', '.sortable', sortable_onclick)
			.each(function() {
				var $t = $(this), c = $t.data('sortedCol');
				if (c) {
					set_sorted($t, c, $t.data('sortedDir'));
				}
			});
	};

	// SORTABLE DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="sortable"]').sortable();
	});
})(jQuery);
(function($) {
	"use strict";

	function _autosize() {
		var $t = $(this);
		$t.css('height', 'auto').outerHeight($t.prop('scrollHeight'));
	}

	var E = 'input.autosize';

	$.fn.autosize = function() {
		return this.off(E).on(E, _autosize).css({
			'overflow-y': 'hidden',
			'resize': 'none'
		}).trigger('input');
	};

	$(window).on('load', function() {
		$('textarea[autosize]').autosize();
	});

})(jQuery);
(function($) {
	"use strict";

	function _enterfire(evt) {
		if (evt.ctrlKey && evt.key == 'Enter') {
			var $t = $(this), ef = $t.attr('enterfire') || 'true';
			if (ef == 'true' || ef == 'form' || ef == 'submit') {
				$t.closest('form').submit();
			} else {
				$(ef).click();
			}
		}
	}

	$.fn.enterfire = function() {
		return this.off('keyup.enterfire').on('keyup.enterfire', _enterfire);
	};

	$(window).on('load', function() {
		$('textarea[enterfire]').enterfire();
	});

})(jQuery);
(function($) {
	"use strict";

	$.fn.textclear = function() {
		return this.each(function() {
			var $t = $(this);
			if ($t.hasClass('ui-has-textclear')) {
				return;
			}

			$t.addClass('ui-has-textclear');

			var $i = $('<i class="ui-close ui-textclear"></i>');
			$i.insertAfter($t).click(function() {
				if ($t.val() != '') {
					$t.val('').trigger('input').trigger('change');
				}
				$t.focus();
				return false;
			}).parent().css('position', 'relative');
		});
	};
	
	// ==================
	$(window).on('load', function () {
		$('[textclear]').textclear();
	});
})(jQuery);
(function($) {
	"use strict";

	var E = 'blur.textstrip', re = /^[\s\u3000]+|[\s\u3000]+$/g;

	function _textstrip() {
		var $t = $(this), a = $t.attr('textstrip') || '';
		if (a != 'false') {
			$t.val(($t.val() || '').replace(re, ''));
		}
	}

	$.fn.textstrip = function() {
		return this.off(E).on(E, _textstrip);
	};
	
	// ==================
	$(window).on('load', function () {
		$('[textstrip]').textstrip();
	});
})(jQuery);
// jQuery toast plugin created by Kamran Ahmed copyright MIT license 2015 (modified by Frank Wang)
(function($) {
	"use strict";

	function setOptions(os, base, options) {
		var o = {};

		if ((typeof options === 'string') || $.isArray(options)) {
			o.message = options;
		} else {
			o = options;
		}
		$.extend(os, base, o);
	}

	function setup($t, os) {
		$t = $t || $('<div class="ui-toast-single"></div>');

		$t.empty();

		// For the loader on top
		$t.append($('<span class="ui-toast-loader"></span>'));

		if (os.closeable) {
			$t.append($('<span class="ui-toast-close">&times;</span>'));
		}

		var m = os.html ? 'html' : 'text';
		if (os.heading) {
			$t.append($('<h4 class="ui-toast-heading">')[m](os.heading));
		}

		var $c = $('<div class="ui-toast-content">').appendTo($t);
		var t = os.message || os.text;
		if ($.isArray(t)) {
			var $ul = $('<ul class="ui-toast-list">');
			$.each(t, function(i, t) {
				if (t) {
					$ul.append($('<li>')[m](t));
				}
			});
			$c.append($ul);
		} else {
			$c.append($('<div class="ui-toast-text">')[m](t));
		}

		if (os.bgColor !== false) {
			$t.css("background-color", os.bgColor);
		}

		if (os.textColor !== false) {
			$t.css("color", os.textColor);
		}

		if (os.textAlign) {
			$t.css('text-align', os.textAlign);
		}

		if (os.icon !== false) {
			$t.addClass('has-icon ' + os.icon);
		}

		if (os['class'] !== false) {
			$t.addClass(os['class'])
		}

		return $t;
	}

	function position(os) {
		var $c = $(".ui-toast-wrap"),
			sp = os.position,
			op = {
				left: 'auto',
				top: 'auto',
				right: 'auto',
				bottom: 'auto'
			};

		if (typeof sp === 'object') {
			$.extend(op, sp);
		} else {
			switch (sp) {
			case 'mid center':
				op.left = ($(window).outerWidth() / 2) - $c.outerWidth() / 2;
				op.top = ($(window).outerHeight() / 2) - $c.outerHeight() / 2;
				break;
			case 'bottom':
				op.bottom = 5;
				op.left = 20;
				op.right = 20;
				break;
			case 'bottom center':
				op.bottom = 5;
				op.left = ($(window).outerWidth() / 2) - $c.outerWidth() / 2;
				break;
			case 'bottom left':
				op.bottom = 5;
				op.left = 20;
				break;
			case 'bottom right':
				op.bottom = 5;
				op.right = 20;
				break;	
			case 'top':
				op.top = 5;
				op.left = 20;
				op.right = 20;
				break;
			case 'top right':
				op.top = 5;
				op.right = 20;
				break;
			case 'top left':
				op.top = 5;
				op.left = 20;
				break;
			// case 'top center':
			default:
				op.top = 5;
				op.left = ($(window).outerWidth() / 2) - $c.outerWidth() / 2;
				break;
			}
		}
		$c.css(op);
	}

	function bindToast($t, os) {
		$t.unbind();

		if (canAutoHide(os)) {
			$t.on('shown.toast', function() {
				showLoader($t, os);
				bindHover($t, os);
			});
		}

		$t.find('.ui-toast-close').on('click', function(e) {
			e.preventDefault();
			transitionOut($t, os);
		});

		if (typeof os.beforeShow == 'function') {
			$t.on('show.toast', function() {
				os.beforeShow($t);
			});
		}

		if (typeof os.afterShown == 'function') {
			$t.on('shown.toast', function() {
				os.afterShown($t);
			});
		}

		if (typeof os.beforeHide == 'function') {
			$t.on('hide.toast', function() {
				os.beforeHide($t);
			});
		}

		if (typeof os.afterHidden == 'function') {
			$t.on('hidden.toast', function() {
				os.afterHidden($t);
			});
		}

		if (typeof os.onClick == 'function') {
			$t.on('click', function() {
				os.onClick($t);
			});
		}
	}

	function addToDom($t, os) {
		var $c = $('.ui-toast-wrap'),
			sn = os.stack;

		if ($c.length === 0) {
			$c = $('<div></div>', {
				"class": "ui-toast-wrap",
				"role": "alert",
				"aria-live": "polite"
			});
			$('body').append($c);

		} else if (!sn || isNaN(parseInt(sn, 10))) {
			$c.empty();
		}

		$c.find('.ui-toast-single:hidden').remove();

		$c.append($t);

		if (sn && !isNaN(parseInt(sn), 10)) {
			var _prevToastCount = $c.find('.ui-toast-single').length,
				_nextToastCount = _prevToastCount - sn;

			if (_nextToastCount > 0) {
				$c.find('.ui-toast-single').slice(0, _nextToastCount).remove();
			}
		}
	}

	function canAutoHide(os) {
		return (os.hideAfter !== false) && !isNaN(parseInt(os.hideAfter, 10));
	}

	function showLoader($t, os) {
		if (os.loader) {
			// 400 is the default time that jquery uses for fade/slide
			// Divide by 1000 for milliseconds to seconds conversion
			var transition = 'width ' + (os.hideAfter - 400) / 1000 + 's ease-in';

			$t.find('.ui-toast-loader').css({
				'width': '100%',
				'transition': transition,
				'background-color': os.loaderBg
			});
		}
	}

	function hideLoader($t, os) {
		if (os.loader) {
			$t.find('.ui-toast-loader').css({
				'width': '0%',
				'transition': 'none'
			});
		}
	}

	function setHideTimer($t, os) {
		$t.data('timer', setTimeout(function() {
			$t.off('mouseenter mouseleave').removeData('timer');
			transitionOut($t, os);
		}, os.hideAfter));
	}

	function clearHideTimer($t) {
		var tm = $t.data('timer');
		if (tm) {
			clearTimeout(tm);
		}
	}

	function bindHover($t, os) {
		if (os.stopHideOnHover) {
			$t.hover(function() {
				clearHideTimer($t);
				hideLoader($t, os);
			}, function() {
				setHideTimer($t, os);
				showLoader($t, os);
			});
		}
	}

	function transitionIn($t, os) {
		var tm = 'show';

		switch (os.transition) {
		case 'fade':
			tm = 'fadeIn';
			break;
		case 'slide':
			tm = 'slideDown';
			break;
		}

		$t.hide().trigger('show.toast')[tm](function() {
			$t.trigger('shown.toast');
		});
	}

	function transitionOut($t, os) {
		var tm = 'hide';

		switch (os.transition) {
		case 'fade':
			tm = 'fadeOut';
			break;
		case 'slide':
			tm = 'slideUp';
			break;
		}

		$t.trigger('hide.toast')[tm](function() {
			$t.trigger('hidden.toast');
		});
	}

	function Toast(options) {
		var os = {}, // options
			$t; // toast-single

		setOptions(os, $.toast.defaults, options);
		$t = setup($t, os);
		addToDom($t, os);
		position(os);
		bindToast($t, os);
		transitionIn($t, os);

		if (canAutoHide(os)) {
			setHideTimer($t, os);
		}

		var api = {
			reset: function(resetWhat) {
				if (resetWhat === 'all') {
					$('.ui-toast-wrap').remove();
				} else {
					$t.remove();
				}
			},

			update: function(options) {
				setOptions(os, {}, options);
				setup($t, os);
				bindToast($t, os);
			},

			clsose: function() {
				transitionOut($t, os);
			}
		};

		return api;
	}

	$.toast = Toast;

	$.toast.defaults = {
		icon: false,
		html: false,
		heading: '',
		message: '',
		loader: true,
		transition: 'fade',
		closeable: true,
		hideAfter: 5000,
		stopHideOnHover: true,
		stack: 5,
		position: 'top center',
		bgColor: false,
		textColor: false,
		textAlign: 'left',
		loaderBg: '#9EC600'
	};

})(jQuery);
(function($) {
	"use strict";

	$.fn.totop = function() {
		return this.each(function() {
			var $t = $(this), $w = $(window);

			$t.click(function() {
				$('html,body').animate({ scrollTop: 0 }, 'slow');
			}).css({ cursor: 'pointer' });

			$w.scroll(function() {
				$t.toggle($w.scrollTop() > $w.height());
			});
		});
	};

	$(window).on('load', function() {
		$('[totop]').totop();
	});

})(jQuery);
﻿(function($) {
	"use strict";

	function init($t) {
		$t.find('li').removeClass('node leaf').children('.item').each(function() {
			var $i = $(this), $n = $i.parent();
			if ($i.next('ul').length) {
				$n.addClass('node');
			} else {
				$n.addClass('leaf');
			}
		});

		$t.off('.treeview').on('click.treeview', '.item', _on_item_click);
	}

	function _on_item_click() {
		var $i = $(this);
		if ($i.next('ul').length) {
			_toggle($i.parent());
		}
	}

	function _collapse($n) {
		$n.addClass('collapsed').children('.item').next().slideUp();
	}

	function _expand($n) {
		$n.removeClass('collapsed').children('.item').next().slideDown();
	}

	function _toggle($n) {
		$n.hasClass('collapsed') ? _expand($n) : _collapse($n);
	}

	function collapse($t, $n) {
		_collapse($n || $t.find('li:not(.collapsed .leaf)'));
	}

	function expand($t, $n) {
		_expand($n || $t.find('li.collapsed'));
	}

	function toggle($t, $n) {
		_toggle($n || $t.find('li:not(.leaf)'));
	}

	function unbind($t) {
		$t.off('.treeview').find('li').removeClass('node');
	}

	var api = {
		'collapse': collapse,
		'expand': expand,
		'toggle': toggle,
		'destroy': unbind
	};

	$.fn.treeview = function(method, target) {
		// Methods
		if (typeof method == 'string') {
			api[method](this, target);
			return this;
		}

		init(this);
		return this;
	};

	// TREEVIEW DATA-API
	// ==================
	$(window).on('load', function() {
		$('ul[data-spy="treeview"]').treeview();
	});

})(jQuery);
(function($) {
	"use strict";

	var isAdvancedUpload = function() {
		var div = document.createElement('div');
		return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
	}();

	var UNITS = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
	function _filesize(n, p) {
		var i = 0, l = UNITS.length - 1;
		while (n >= 1024 && i < l) {
			n = n / 1024
			i++
		}

		p = Math.pow(10, p || 2);
		return '(' + Math.round(n * p) / p + ' ' + UNITS[i] + ')';
	}

	function _filename(fn) {
		var u = fn.lastIndexOf('/'),
			w = fn.lastIndexOf('\\'),
			i = u > w ? u : w;
		return fn.substr(i + 1);
	}

	function _filetype(t, e) {
		if (t) {
			var i = t.indexOf('/'), c = (i >= 0) ? t.slice(0, i) : t;
			return ($.inArray(c, ['image', 'audio', 'video', 'file']) >= 0) ? c : 'file';
		}
		if (e) {
			e = e.toLowerCase();
			if ($.inArray(e, ['.jpg', '.jpeg', '.gif', '.png', '.tif', '.tiff', '.svg', '.bmp', '.webp']) >= 0) {
				return 'image';
			}
			if ($.inArray(e, ['.mp3', '.flac', '.weba', '.wav', '.mid', '.oga', '.wma']) >= 0) {
				return 'audio';
			}
			if ($.inArray(e, ['.avi', '.mpg', '.mpeg', '.mp4', '.m4v', 'mov', '.webm', '.wmv']) >= 0) {
				return 'video';
			}
		}
		return 'file';
	}

	function _item_on_remove() {
		$(this).closest('.ui-uploader-item').removeData('file').fadeOut(function() {
			$(this).remove();
		});
		return false;
	}

	function _create_item($u, f) {
		var uc = $u.data('uploader'),
			fnm = _filename(f.name || f.path || f.id),
			fsz = f.size;

		var $fit = $('<div>', { 'class': 'ui-uploader-item' }).data('file', f);

		$('<input>', { type: 'hidden', name: uc.name, 'class': 'ui-uploader-fid' }).appendTo($fit);
		$('<i>', { 'class': 'ui-close' }).click(_item_on_remove).appendTo($fit);

		$('<span>', { 'class': 'ui-uploader-info' })
			.append($('<i>', { 'class': uc.cssIcons.waiting + ' ui-uploader-icon' }))
			.append($('<span>', { 'class': 'ui-uploader-text' }).text(fnm + ' ' + _filesize(fsz)))
			.appendTo($fit);

		$u.find('.ui-uploader-items').prepend($fit);

		return $fit;
	}

	function _update_item($fit, fi) {
		var $u = $fit.closest('.ui-uploader'),
			uc = $u.data('uploader'),
			fid = fi.id || fi.path || fi.name,
			fnm = _filename(fi.name || fi.path || fi.id),
			fsz = fi.size,
			fct = _filetype(fi.type, fi.ext);

		$fit.find('.ui-uploader-fid').val(fid || '');

		var durl;
		if (uc.dnloadUrl && fid) {
			durl = uc.dnloadUrl.replace(uc.dnloadHolder, uc.dnloadEncode ? encodeURIComponent(fid) : fid);
		}

		$fit.find('.ui-uploader-icon').prop('className', uc.cssIcons[fct] + ' ui-uploader-icon');

		if (fnm) {
			$fit.find('.ui-uploader-text').text(fnm + ' ' + _filesize(fsz));
			if (durl) {
				var $fif = $fit.find('.ui-uploader-info');
				$('<a>', { href: durl }).append($fif).appendTo($fit);
			}
		}

		if (uc.dnloadView && durl && fct == 'image') {
			var $fim = $('<div>').addClass('ui-uploader-image').appendTo($fit);
			$('<a>', { href: durl })
				.append($('<img>', { src: durl }))
				.appendTo($fim)
				.fadeIn();
		}
	}

	function _item_progress($fit, p) {
		if (p < 100) {
			var uc = $fit.closest('.ui-uploader').data('uploader');
			$fit.css('background', 'linear-gradient(to right, ' + uc.pgbarFgcolor + + ' ' + p + '%, ' + uc.pgbarBgcolor + ' ' + (100 - p) + '%)');
		} else {
			$fit.css('background', '').addClass('blinking');
		}
	}

	function _ajaxDone(d) {
		if (d) {
			var r = d.result || d.file;
			if (r) {
				_update_item($(this), r);
			}
		}
	}

	function _ajaxFail(xhr, status, err) {
		var $e = $('<div class="ui-uploader-error">');

		var j = xhr.responseJSON, t = xhr.responseText;
		if (j) {
			var e = j.error;
			$e.addClass('text').text(typeof(e) == 'string' ? e : JSON.stringify(j, null, 4));
		} else if (t) {
			$e.html(t);
		} else {
			$e.addClass('text').text((xhr.status ? (xhr.status + ' ') : '')  + (err || status || 'error'));
		}

		$(this).append($e);
	}

	function _init($u, uc) {
		$u.addClass('ui-uploader').data('uploader', uc);

		var uploads = [],
			$uf = $u.find('.ui-uploader-file'),
			$ub = $u.find('.ui-uploader-btn'),
			$us = $u.find('.ui-uploader-items');

		if ($us.length < 1) {
			$us = $('<div class="ui-uploader-items"></div>');
			$u.append($us);
		}

		uc.name ||= $uf.attr('name');
		uc.uploadName ||= $uf.attr('name') || uc.name;

		function __start_upload($fit) {
			var f = $fit.data('file');
			if (!f) {
				return;
			}

			$fit.addClass('loading');
			$fit.find('.ui-uploader-icon').prop('className', uc.cssIcons.loading + ' ui-uploader-icon');

			$u.trigger('upload.uploader', { item: $fit, file: f });

			$fit.find('.ui-close').hide();

			var data = {};
			$u.find('.ui-uploader-data').each(function() {
				var $i = $(this);
				data[$i.attr('name')] = $i.val();
			});
			$.extend(data, uc.uploadData);

			var file = {}; file[uc.uploadName] = f;

			$.ajaf({
				url: uc.uploadUrl,
				data: data,
				file: file,
				dataType: 'json',
				uprogress: function(loaded, total) {
					_item_progress($fit, Math.round(loaded * 100 / total));
				},
				success: function(data, status, xhr) {
					$fit.css('background', '').addClass('success');
					uc.ajaxDone.call($fit, data, status, xhr);
					$u.trigger('uploaded.uploader', { item: $fit, data: data });
				},
				error: function(xhr, status, e) {
					$fit.addClass('error');
					$fit.find('.ui-uploader-icon').prop('className', uc.cssIcons['error'] + ' ui-uploader-icon');
					uc.ajaxFail.call($fit, xhr, status, e);
				},
				complete: function() {
					$fit.removeClass('loading blinking').removeData('file');
					if (uc.uploadRemover) {
						$fit.find('.ui-close').show();
					}
					if (!$u.find('.ui-uploader-file').prop('multiple')) {
						$ub.prop('disabled', false);
						$uf.prop('disabled', false);
					}
					__proc_uploads();
				}
			});
		}

		function __proc_uploads() {
			while (uploads.length > 0 && $u.find('.ui-uploader-item.loading').length < uc.uploadLimit) {
				__start_upload(uploads.shift());
			}
		}

		function __append_uploads(f) {
			if (!$u.find('.ui-uploader-file').prop('multiple')) {
				$u.find('.ui-uploader-item').remove();
				$ub.prop('disabled', true);
				$uf.prop('disabled', true);
			}

			var ufs = [];
			if (f instanceof FileList) {
				$.each(f, function(i, f) {
					ufs.push(_create_item($u, f));
				});
			} else if (f instanceof File) {
				ufs.push(_create_item($u, f));
			} else {
				$.each(f.prop('files'), function(i, f) {
					ufs.push(_create_item($u, f));
				});
			}

			uploads = uploads.concat(ufs.reverse());
			__proc_uploads();
		}

		function __file_on_change() {
			if ($uf.val() == "") {
				return;
			}

			__append_uploads($uf);
		}

		function __file_on_drop(e) {
			e.preventDefault();

			if (!$uf.prop('disabled')) {
				var fs = e.originalEvent.dataTransfer.files;
				if (fs.length) {
					__append_uploads($uf.prop('multiple') ? fs : fs.item(0));
				}
			}
		}

		// event handler
		$uf.change(function() {
			setTimeout(__file_on_change, 10);
		});

		$ub.click(function(e) {
			e.preventDefault();
			$uf.trigger('click');
			return false;
		});

		// drap & drop
		if (isAdvancedUpload) {
			$u.addClass('ui-uploader-draggable')
				.on('drag dragstart dragend dragover dragenter dragleave drop', function(e) {
					e.preventDefault();
					e.stopPropagation();
				})
				.on('dragover dragenter', function() {
					$u.addClass('ui-uploader-dragover');
				})
				.on('dragleave dragend drop', function() {
					$u.removeClass('ui-uploader-dragover');
				})
				.on('drop', __file_on_drop);
		}
	}

	function _options($u) {
		var ds = ['uploadData'],
			fs = ['ajaxDone', 'ajaxFail'],
			bs = ['dnloadEncode'],
			ps = [
				'name',
				'uploadUrl',
				'uploadName',
				'uploadLimit',
				'uploadRemover',
				'dnloadUrl',
				'dnloadHolder',
				'dnloadView',
				'pgbarFgcolor',
				'pgbarBgcolor'
			],
			ks = [].concat(ds, fs, bs, ps);

		var c = {};
		$.each(ks, function(i, k) {
			var v = $u.data(k);
			if (v) {
				if ($.inArray(k, ds) >= 0) {
					if (typeof (v) == 'string') {
						try {
							v = JSON.parse(v);
						} catch (e) {
							return;
						}
					}
				} else if ($.inArray(k, fs) >= 0) {
					v = new Function(v);
				} else if ($.inArray(k, bs) >= 0) {
					v = (v === 'true');
				}
				c[k] = v;
			}
		});
		return c;
	}

	// UPLOADER FUNCTION
	// ==================
	$.uploader = {
		defaults: {
			// max concurrent upload files
			uploadLimit: 1,

			// show uploader remover icon
			uploadRemover: false,

			// download file id/name placeholder
			dnloadHolder: '$',

			// show image download view
			dnloadView: false,

			// fontawesome4/5/6 css
			cssIcons: {
				file: 'fa fa-file-o far fa-file',
				image: 'fa fa-file-image-o far fa-file-image',
				audio: 'fa fa-file-audio-o far fa-file-audio',
				video: 'fa fa-file-video-o far fa-file-video',
				error: 'fa fa-exclamation-circle fas fa-circle-exclamation',
				waiting: 'fa fas fa-refresh',
				loading: 'fa fas fa-refresh fa-spin'
			},

			pgbarFgcolor: '#ccc',
			pgbarBgcolor: 'transparent',

			ajaxDone: _ajaxDone,
			ajaxFail: _ajaxFail
		}
	};

	$.fn.uploader = function(c) {
		return this.each(function() {
			var $u = $(this), uc = $u.data('uploader');
			if (uc) {
				$.extend(uc, c);
				return;
			}

			uc = $.extend({}, $.uploader.defaults, _options($u), c);
			_init($u, uc);
		});
	};

	// UPLOADER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="uploader"]').uploader();
	});

})(jQuery);
(function($) {
	"use strict";

	$.each({
		zoomIn: { opacity: 'show' },
		zoomOut: { opacity: 'hide' },
	}, function(name, props) {
		$.fn[name] = function(speed, easing, callback) {
			var opt = $.speed(speed, easing, callback);
			var old = opt.step;
			opt.step = function(s) {
				$(this).css({ transform: 'scale(' + s + ')' });
				if (old) {
					old.call(this, s);
				}
			};
			return this.animate(props, opt);
		};
	});

})(jQuery);
(function($) {
	"use strict";

	$(window).on('load', function () {
		$('.navbar-toggle').click(function() {
			$(this).toggleClass('active');
		});
	});
})(jQuery);
(function() {
	"use strict";

	function _onclick(evt) {
		var $pg = $(this), $li = $(evt.target).closest('li'), $a = $li.children('a');

		if ($li.hasClass('disabled')) {
			evt.preventDefault();
			return;
		}

		var pn = $a.attr('pageno'), href = $a.attr('href');
		if (pn >= 0 && href == '#') {
			evt.preventDefault();
			$pg.trigger('goto.pager', pn);
		}
	}

	function _setActivePage($p, n) {
		var $u = $p.children('ul.pagination'),
			$n = $u.children('li.page');

		$u.find('li.active').removeClass('active');

		var m = $p.data('pages'), b = n - Math.floor($n.length / 2);

		if (b + $n.length > m) {
			b = m - $n.length + 1;
		}
		if (b < 1) {
			b = 1;
		}

		var s = $p.data('style');
		if (n > 1) {
			$u.children('li.first, li.prev').removeClass('hidden disabled');
			$u.find('.ui-pager-prev>a').attr('pageno', n - 1);
		} else {
			$u.children('li.first').addClass(s.contains('F') ? 'disabled' : 'hidden');
			$u.children('li.prev').addClass(s.contains('P') ? 'disabled' : 'hidden');
		}

		$u.children('li.eleft')[b > 1 ? 'removeClass' : 'addClass']('hidden');
		$n.each(function() {
			var $li = $(this);
			$li.find('a').attr('pageno', b).text(b);
			if (b == n) {
				$li.addClass('active');
			}
			b++;
		});
		$u.children('li.eright')[b <= m ? 'removeClass' : 'addClass']('hidden');

		if (n < m) {
			$u.children('li.next, li.last').removeClass('hidden disabled');
			$u.children('li.next>a').attr('pageno', n + 1);
		} else {
			$u.children('li.next').addClass(s.contains('N') ? 'disabled' : 'hidden');
			$u.children('li.last').addClass(s.contains('L') ? 'disabled' : 'hidden');
		}
	}

	$.fn.pager = function(api, pno) {
		if (api == 'page') {
			if (pno > 0) {
				return this.each(function() { _setActivePage($(this), pno); });
			}
			return this.find('ul.pagination>li.active>a').attr('pageno');
		}
		return this.off('click.pager').on('click.pager', 'a[pageno]', _onclick);
	};

	// PAGER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="pager"]').pager();
	});
})();
