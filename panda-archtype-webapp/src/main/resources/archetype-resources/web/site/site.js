panda.enable_loadmask_form = true;

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
	statics: '/static'
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

	$('#sidenav i').each(function() {
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

