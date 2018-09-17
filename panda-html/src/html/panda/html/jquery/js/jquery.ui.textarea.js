(function($) {
	function enterfire() {
		$('textarea[enterfire]').each(function() {
			var f = $(this).attr("enterfire");
			if (f) {
				$(this).removeAttr("enterfire").keyup(function(evt) {
					if (evt.ctrlKey && evt.which == 13) {
						if (f == "form") {
							$(this).closest("form").submit();
						}
						else {
							$(f).click();
						}
					}
				});
			}
		});
	}

	$(window).on('load', function() {
		enterfire();
	});
})(jQuery);
