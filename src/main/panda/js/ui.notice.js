if (typeof(pw) == "undefined") { pw = {}; }

(function() {
	function setContentType($p, t) {
		if (t == 'error') {
			if (!$p.hasClass('error')) {
				$p.addClass('error').removeClass('warn help info');
			}
		}
		else if (t == 'warn') {
			if (!$p.hasClass('error') && !$p.hasClass('warn')) {
				$p.addClass('warn').removeClass('help info');
			}
		}
		else if (t == 'confirm') {
			if (!$p.hasClass('error') && !$p.hasClass('warn') && !$p.hasClass('help')) {
				$p.addClass('help').removeClass('info');
			}
		}
		else {
			if (!$p.hasClass('error') && !$p.hasClass('warn') && !$p.hasClass('help') && !$p.hasClass('info')) {
				$p.addClass('info');
			}
		}
	}
	
	function addMsg($p, s, m, t) {
		var c = s.baseCls + '-' + t;
		var ic = s.icons[t];
		$p.append('<ul class="' + c + 's"><li class="' + c + '"><i class="' + ic + '"></i>' + m + '</li></ul>');
		setContentType($p, t);
	}

	function addMsgs($p, s, m, t) {
		if (m) {
			var c = s.baseCls + '-' + t;
			var ic = s.icons[t] + ' ' + c;
			var h = '<ul class="' + c + 's">';
			if ($.isArray(m)) {
				for (var i = 0; i < m.length; i++) {
					h += '<li class="' + c + '"><i class="' + ic + '"></i>' + m[i] + '</li>';
				}
			}
			else {
				for (var n in m) {
					var v = m[n];
					for (var i = 0; i < v.length; i++) {
						h += '<li class="' + c + '"><i class="' + ic + '"></i>' + v[i] + '</li>';
					}
				}
			}
			h += '</ul>';
			$p.append(h);
			setContentType($p, t);
		}
	}
	
	pw.notice = function(s) {
		if (typeof(s) == 'string') {
			s = { container: s };
		}
		s = $.extend({}, pw.notice.defaults, s);
		return {
			clear: function() {
				$(s.container).children('.' + s.baseCls + '-notice').remove();
				return this;
			},
			error: function(m) {
				this.add(m, 'error');
				return this;
			},
			warn: function(m) {
				this.add(m, 'warn');
				return this;
			},
			promt: function(m) {
				this.add(m, 'help');
				return this;
			},
			info: function(m) {
				this.add(m, 'info');
				return this;
			},
			add: function(m, t) {
				t = t || 'info';
				var $c = $(s.container);
				var $p = $c.children('.' + s.baseCls + '-notice');
				var a = false;
				if ($p.size() < 1) {
					$p = $('<div></div>').addClass(s.baseCls + '-notice').css('display', 'none');
					$c.prepend($p);
					a = true;
				}
				
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
					addMsgs($p, s, m.actionErrors, "error");
					addMsgs($p, s, m.fieldErrors, "error");
					addMsgs($p, s, m.actionWarnings, "warn");
					addMsgs($p, s, m.actionConfirms, "help");
					addMsgs($p, s, m.actionMessages, "info");
				}
				
				if (a) { 
					$p.slideDown();
				}
				return this;
			}
		}
	};
	
	pw.notice.defaults = {
		baseCls: 'n-action',
		container: 'body',
		icons: {
			'help': 'icon-question-sign',
			'info': 'icon-info',
			'error': 'icon-exclamation-sign',
			'warn': 'icon-warning-sign'
		}
	};
})();
