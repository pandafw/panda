if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.loading = function(timeout) {
	$('body').loadmask({ mask: false, window: true, timeout: timeout || 1000 });
};

panda.page_sort = function(name, dir) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 's.c': name, 's.d': dir });
	return false;
};
panda.page_sort_reverse = function(name, dir) {
	return panda.page_sort(name, dir.toLowerCase() == "asc" ? "desc" : "asc");
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
