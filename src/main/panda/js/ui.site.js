//------------------------------------------------------
function s_preload() {
	$('body').append(
		'<div id="preload" class="p-vhidden">'
			+ '<div class="ui-loadmask"></div>'
			+ '<div class="p-loader-large-snake"></div>'
		+ '</div>');
}

function s_submit_form() {
	var form = this;
	var $f = $(form);
	var $c = $f.closest('.p-popup, .p-inner');
	var lm = ($f.height() > 20 && $f.attr('loadmask') != 'false');
	if ($c.length > 0) {
		setTimeout(function() {
			var data = $f.serializeArray();
			if ($c.hasClass('p-inner')) {
				data.push({ name: '__inner', value: 'true' });
			}
			else {
				data.push({ name: '__popup', value: 'true' });
			}
			if (lm) {
				$c.parent().loadmask();
			}
			$.ajax({
				url: form.action,
				data: data,
				dataType: 'html',
				success: function(html, ts, xhr) {
					$c.parent().html(html);
				},
				error: function(xhr, ts, err) {
					$c.parent().html(xhr.responseText);
				},
				complete: function(xhr, ts) {
					$c.parent().unloadmask();
				}
			});
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
		$('#' + id + '_so_c').val(co[0]);
		$('#' + id + '_so_d').val(co[1]);
		sl_submit(id);
	}
}
function sl_sorta(id, name, dir) {
	sl_sortn(id, name, dir.toLowerCase() == "asc" ? "desc" : "asc");
}
function sl_sortn(id, name, dir) {
	$('#' + id + '_so_c').val(name);
	$('#' + id + '_so_d').val(dir);
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
	var $pi = $form.closest('.p-inner');
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

function s_addScript(url) {
	$.jscript(url);
}

//------------------------------------------------------
// google analytics
var _gaq = [];
function s_google_analytics(c) {
	if (c.google_analytics) {
		_gaq.push(['_setAccount', c.google_analytics]);
		if (c.google_analytics_domain) {
			_gaq.push(['_setDomainName', c.google_analytics_domain]);
		}
		_gaq.push(['_trackPageview']);
		
		s_addScript(('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js');
	}
}


//------------------------------------------------------
// facebook comments
function s_facebook_comments(i, h, w) {
	h = h || document.location;
	$(i).attr({
		"data-href": h || document.location,
		"data-num-posts": '2',
		"data-width": w || '500'
	});
	s_addScript("http://connect.facebook.net/ja_JP/all.js#xfbml=1");
}

//------------------------------------------------------
// google plus one
function s_google_plusone() {
	s_addScript("https://apis.google.com/js/plusone.js");
}

//------------------------------------------------------
// site vars
var site = {
	statics: '/static'
};

function s_setbase(c) {
	c = $.extend(site, c);
	$.cookie.defaults = c.cookie || {};
	return site;
}

//------------------------------------------------------
//clipboard
function s_copyToClipboard(s) {
	try {
		// ie
		clipboardData.setData('Text', s);
	}
	catch (e) {
		var swf = document.createElement('embed');
		swf.src = site.statics + "/panda/swf/clipboard.swf";
		swf.setAttribute('FlashVars','code=' + encodeURIComponent(s));
		swf.type = 'application/x-shockwave-flash';
		swf.width = '0';
		swf.height = '0';
		$('body').append(swf);
	}
}


//------------------------------------------------------
function s_decorate(selector) {
	$(selector).each(function() {
		var $w = $(this);
		s_hook_forms($w);
	});
}

function s_init(c) {
	var p = [ 's:statics', 's:base', 's:google_analytics' ];
	var m = { body: 'body' };
	$('meta').each(function() {
		var $t = $(this);
		var a = $t.attr('property');
		if (p.contains(a)) {
			var v = $t.attr('content');
			if (v) {
				m[a.substring(2)] = v;
			}
		}
	});

	c = $.extend(m, c);
	c = s_setbase(c);

	// document - onload
	$(function() {
		s_preload();
		
		var $w = $(c.body);
		s_hook_forms($w);
		s_ie6_hack_forms($w);

		// google analytics
		s_google_analytics(c);
	});
}

// set default
s_setbase({
	base: '',
	cookie: { expires: 180 }
});

