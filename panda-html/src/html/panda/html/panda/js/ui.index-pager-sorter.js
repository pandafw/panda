if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.loading = function() {
	$('body').loadmask({ mask: false, window: true });
};

panda.page_sort_reverse = function(name, dir) {
	return sl_sortn(name, dir.toLowerCase() == "asc" ? "desc" : "asc");
};
panda.page_sort = function(name, dir) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 's.c': name, 's.d': dir });
	return false;
};
panda.page_goto = function(s) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 'p.s': s });
	return false;
};
panda.page_limit = function(l) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 'p.l': l });
	return false;
};
