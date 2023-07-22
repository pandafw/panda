(function($) {	
	"use strict";

	$(window).on('load', function() {
		$('.p-viewfield').prev('input').change(function() {
			var v = $(this).val();
			$(this).next('.p-viewfield').text(v == '' ? '\u3000' : v);
		});
		
		$('.p-viewfield[data-format="html"]').each(function() {
			$('<a href="#" class="p-vf-code"><i class="fa fa-code"></i></a>').appendTo($(this).parent());
		});
		
		$('.p-vf-code').click(function() {
			var $t = $(this);
			var $p = $t.toggleClass('active').parent();
			var v = $p.find('input').val();
			$p.find('.p-viewfield')[$t.hasClass('active') ? 'text' : 'html'](v);
			return false;
		});
	});
})(jQuery);
