/* Japanese initialisation for the jQuery UI lightBox plugin. */
jQuery(function($){
	$.extend($.lightBox, {
		textBtnPrev:			'前へ',		// (string) the text of prev button
		textBtnNext:			'次へ',		// (string) the text of next button
		textBtnClose:			'閉じる×'		// (string) the text of close button
	});
});

/* Japanese initialisation for the jQuery UI loadmask plugin. */
jQuery(function($){
	$.meiomask.masks.date = { mask : '9999/19/39' }; 
	$.meiomask.masks.datetime = { mask : '9999/19/39 29:59:59' }; 
	$.meiomask.masks.timestamp = { mask : '9999/19/39 29:59:59.999' }; 
});
