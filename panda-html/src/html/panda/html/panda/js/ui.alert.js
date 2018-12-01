(function($) {
	function setAlertType($p, s, t) {
		for (var i in s.types) {
			$p.removeClass(s.types[i]);
		}
		$p.addClass(s.types[t]);
	}

	function msg_li(tc, ic, m) {
		return $('<li>').addClass(tc).append($('<i>').addClass(ic)).append($('<span>').text(m));
	}

	function addMsg($p, s, m, t) {
		var ic = s.icons[t];
		var tc = s.texts[t];
		$p.append($('<ul>').addClass(s.css).append(msg_li(tc, ic, m)));
		setAlertType($p, s, t);
	}

	function addMsgs($p, s, m, t) {
		if (m) {
			var ic = s.icons[t];
			var tc = s.texts[t];
			var $u = $('<ul>').addClass(s.css);
			if ($.isArray(m)) {
				for (var i = 0; i < m.length; i++) {
					$u.append(msg_li(tc, ic, m[i]));
				}
			}
			else {
				for (var n in m) {
					var v = m[n];
					if ($.isArray(v)) {
						for (var i = 0; i < v.length; i++) {
							$u.append(msg_li(tc, ic, s.label ? (n + s.label + v[i]) : v[i]));
						}
					}
					else {
						$u.append(msg_li(tc, ic, s.label ? (n + s.label + v) : v));
					}
				}
			}
			$p.append($u);
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
		css: 'fa-ul',
		label: false, //': ',
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
		
		if ($.notify) {
			if (!$.notify.getStyle('palert')) {
				$.notify.addStyle('palert', {
					html: '<div data-notify-html="html"></div>'
				});
			}
			$.notify({ html: $p}, ns);
		}
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
})(jQuery);
