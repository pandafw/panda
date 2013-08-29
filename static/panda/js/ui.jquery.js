/*
 * This file is part of Pandaw.
 * Copyright(C) 2013 Pandaw Develop Team.
 *
 * Pandaw is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 * 
 * Pandaw is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pandaw. If not, see <http://www.gnu.org/licenses/>.
 */
(function($) {
	$.datepicker._triggerClass = 'n-icon n-icon-date_picker ui-datepicker-trigger';
	$.datetimepicker.defaults.triggerClass = 'n-icon n-icon-datetime_picker ui-datetimepicker-trigger';
	$.timepicker.defaults.triggerClass = 'n-icon n-icon-time_picker ui-timepicker-trigger';
	
	$.trim = function(text) { return text == null ? "" : text.strip(); };
})(jQuery);
