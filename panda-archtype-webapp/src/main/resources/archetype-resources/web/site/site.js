//------------------------------------------------------
function s_setbase(c) {
	c = $.extend(site, c);
	$.cookie.defaults = c.cookie || {};
	return site;
}

//------------------------------------------------------
//site vars
//
var site = {
	statics: '/static',
	page_loading: function(timeout) {
		$('body').loadmask({ mask: false, fixed: true, timeout: timeout || 1000 });
	},
	page_sort: function(name, dir) {
		site.page_loading();
		location.href = $.addQueryParams(location.href, { 's.c': name, 's.d': dir});
		return false;
	},
	page_sort_reverse: function(name, dir) {
		return site.page_sort(name, dir.toLowerCase() == "asc" ? "desc" : "asc");
	},
	page_goto_start: function(s) {
		site.page_loading();
		location.href = $.addQueryParams(location.href, { 'p.s': s });
		return false;
	},
	page_goto_page: function(p) {
		site.page_loading();
		location.href = $.addQueryParams(location.href, { 'p.p': p });
		return false;
	},
	page_limit: function(l) {
		site.page_loading();
		location.href = $.addQueryParams(location.href, { 'p.l': l });
		return false;
	}
};

//set default
s_setbase({
	base: '',
	cookie: { expires: 180 }
});

//------------------------------------------------------
$(function() {
	// enable script cache
	$.enableScriptCache();
	
	$('[data-toggle=offcanvas]').click(function() {
		$('.row-offcanvas').toggleClass('active');
	});
	$('[data-toggle=tooltip]').tooltip();
	$('[data-toggle=popover]').popover();

	$('#sidenavi i').each(function() {
		$(this).attr('title', $(this).next('span').text());
	})

	s_setbase($.extend({ body: 'body' }, panda.meta_props()));

	s_preload();

	// google analytics
	s_google_analytics(site);
});

//------------------------------------------------------
function s_preload() {
	(new Image()).src = site.statics + '/panda/img/loader/fountain.gif';
}

