//------------------------------------------------------
function s_preload() {
	$('body').append(
		'<div id="preload" class="p-vhidden">'
			+ '<div class="ui-loadmask"></div>'
			+ '<div class="p-loader-large-snake"></div>'
		+ '</div>');
}

function s_submit_form() {
	var form = this, 
		$f = $(this), 
		$b = $(this).closest('.popup, .inner'),
		lm = ($f.height() > 20 && $f.attr('loadmask') != 'false');
	if ($b.length > 0) {
		setTimeout(function() {
			var data = $f.serializeArray();
			if ($b.hasClass('inner')) {
				data.push({ name: '__inner', value: 'true' });
			}
			else {
				data.push({ name: '__popup', value: 'true' });
			}
			if (lm) {
				$b.parent().loadmask();
			}
			$.get(form.action, data, function(html) {
				$b.parent().unloadmask().html(html);
			}, 'html');
		}, 10);
		return false;
	}
	else {
		if (lm) {
			$f.loadmask();
		}
		return true;
	}
}

function s_hook_forms($w) {
	$w.find('form[hooked!=true]').each(function() {
		var $t = $(this);
		$t.attr('hooked', 'true');
		if (this.target == '' || this.target == '_self'
			|| this.target == '_top' || this.target == '_parent') {
			$t.submit(s_submit_form);
		}
	});
}

function s_ie6_submit_onclick() {
	var t = this;
	$(this).closest('form').find('input[type=submit],button').each(function() {
		if (this != t) {
			this.disabled = true;
		}
	});
};

function s_ie6_hack_forms($w) {
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$w.find('form[hacked!=true]')
			.find('button[type=submit]').each(function() {
				if (!this.onclick) {
					$(this).click(s_ie6_submit_onclick);
				}
			}).end()
			.attr('hacked', 'true');
	}
}

//------------------------------------------------------
function sl_sort(id, el) {
	var co = el.value.split(' ');
	if (co.length == 2) {
		$('#' + id + '_st_c').val(co[0]);
		$('#' + id + '_st_d').val(co[1]);
		sl_submit(id);
	}
}
function sl_sorta(id, name, dir) {
	sl_sortn(id, name, dir.toLowerCase() == "asc" ? "desc" : "asc");
}
function sl_sortn(id, name, dir) {
	$('#' + id + '_st_c').val(name);
	$('#' + id + '_st_d').val(dir);
	sl_submit(id);
}
function sl_goto(id, s) {
	$('#' + id + '_pg_s').val(s);
	sl_submit(id);
}
function sl_limit(id, el) {
	$('#' + id + '_pg_l').val(el.value);
	sl_submit(id);
}
function sl_submit(id) {
	var $form = $('#' + id);
	var $pi = $form.closest('.inner');
	if ($pi.size() > 0) {
		var data = $form.serializeArray();
		data[data.length] = { name: '__inner', value: 'true' };
		
		$pi.parent()
			.loadmask({ cssClass: 'p-loader-large-snake' })
			.load($form.attr('action'), data, function() {
				$pi.parent().unloadmask();
			});
	}
	else {
		s_loadmask();
		$form.submit();
	}
}

function s_loadmask() {
	$('body').loadmask({
		cssClass: 'p-loader-large-snake',
		mask: false,
		window: true
	});
}
function s_resize() {
	$(window).trigger('resize');
}

function s_getLinkMark() {
	var i = location.href.lastIndexOf('#');
	if (i > 0) {
		return location.href.substring(i);
	}
	else {
		return "";
	}
}

function s_setTitle(title) {
	var d = document.title.indexOf(' - ');
	if (d < 0) {
		d = document.title.indexOf(' | ');
	}
	if (d < 0) {
		document.title = title;
	}
	else {
		document.title = title + document.title.substring(d);
	}
}

function s_setLang(v) {
	var u = location.href, i = u.indexOf('?');
	if (i > 0) {
		var qs = $.param(u.queryParams('__locale'));
		u = u.substring(0, i);
		if (qs.length > 0) {
			location.href = u + '?' + qs + '&__locale=' + v;
			return;
		}
	}
	location.href = u + '?__locale=' + v;
}

//------------------------------------------------------
//google analytics
var _gaq = [];
function s_ga_analytics(c) {
	if (c.ga_account) {
		_gaq.push(['_setAccount', c.ga_account]);
		if (c.ga_domain) {
			_gaq.push(['_setDomainName', c.ga_domain]);
		}
		_gaq.push(['_trackPageview']);
		
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		$('body').append(ga);
	}
}


//------------------------------------------------------
// site vars
var site = {
	statics: 'static'
};

function s_setbase(c) {
	c = $.extend(site, c);
	$.cookie.defaults = c.cookie;
	return site;
}

//------------------------------------------------------
function s_decorate(selector) {
	$(selector).each(function() {
		var $w = $(this);
		s_hook_forms($w);
	});
}

function s_main(c) {
	c = $.extend({ main: "#main, #admin" }, c);
	c = s_setbase(c);

	// document - onload
	$(function() {
		s_preload();
		
		var $w = $(c.main);
		s_hook_forms($w);
		s_ie6_hack_forms($w);

		// google analytics
		s_ga_analytics(c);
	});
}

// set default
s_setbase({
	base: '',
	cookie: { expires: 180 }
});

// invoke onPageLoad function
$(function() {
	if (window.onPageLoad) {
		window.onPageLoad();
	}
});
