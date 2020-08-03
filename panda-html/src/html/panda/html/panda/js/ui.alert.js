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
				var d = xhr.responseJSON;
				if (d && (d.alerts || d.exception)) {
					return this.actionAlert(d);
				}
			
				msg = '';
				if (e) {
					msg += e + '\n';
				}
				
				if (xhr) {
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
