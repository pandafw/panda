if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.meta_props = function() {
	var m = {};
	$('meta').each(function() {
		var $t = $(this);
		var a = $t.attr('property');
		if (a && a.startsWith('s:')) {
			var v = $t.attr('content');
			m[a.substring(2)] = v;
		}
	});
	return m;
};

