if (typeof(panda) == "undefined") { panda = {}; }

panda.focus_form = function($i) {
	$i = $i.find('input,select,textarea,button');
	$i = $i.not(':hidden,:disabled,[readonly]').eq(0);
	if ($i.length > 0) {
		$i.focus();
		$('body').scrollTop(0).scrollLeft(0);
	}
}

$(window).on('load', function() {
	var $i = $w.find('form[init-focus="true"]').eq(0);
	panda.focus_form($i);
});
