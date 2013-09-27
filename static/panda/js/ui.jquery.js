(function($) {
	$.datepicker._triggerClass = 'n-icon n-icon-date_picker ui-datepicker-trigger';
	$.datetimepicker.defaults.triggerClass = 'n-icon n-icon-datetime_picker ui-datetimepicker-trigger';
	$.timepicker.defaults.triggerClass = 'n-icon n-icon-time_picker ui-timepicker-trigger';
	
	$.trim = function(text) { return text == null ? "" : text.strip(); };
})(jQuery);
