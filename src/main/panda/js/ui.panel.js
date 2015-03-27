(function() {
	// ==================
	$(window).on('load', function () {
		$('.p-panel-hscroll .panel-body').mousewheel(function(e, delta) {
			var o = this.scrollLeft;
			this.scrollLeft -= (delta * 40);
			if (o != this.scrollLeft) {
				e.preventDefault();
			}
		});
	});
})();
