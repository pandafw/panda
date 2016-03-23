(function() {
	$.fn.ptrigger = function(option) {
		option = $.extend({ 'icon' : 'fa fa-remove' }, option);
		return this.each(function() {
			var $t = $(this);
			var f = option.onclick || $t.data('trigger');
			if (f == 'false') {
				return;
			}

			var $i = $('<i class="p-trigger ' + option.icon + '"></i>');
			$i.insertAfter($t);

			if (f && f != "true") {
				panda_call(f, $t.get(0));
			}
			else {
				$i.click(function() {
					$t.val('');
				});
			}
		});
	};
	
	// DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-trigger]').ptrigger();
	});
})();
