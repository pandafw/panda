// jQuery simple color picker
// https://github.com/rachel-carvalho/simple-color-picker
// Modified by Frank Wang

(function($) {
	"use strict";

	$.simpleColorPicker = {
		defaults: {
			colorsPerLine: 8,
			colors: ['#000000', '#444444', '#666666', '#999999', '#cccccc', '#eeeeee', '#f3f3f3', '#ffffff'
				, '#ff0000', '#ff9900', '#ffff00', '#00ff00', '#00ffff', '#0000ff', '#9900ff', '#ff00ff'
				, '#f4cccc', '#fce5cd', '#fff2cc', '#d9ead3', '#d0e0e3', '#cfe2f3', '#d9d2e9', '#ead1dc'
				, '#ea9999', '#f9cb9c', '#ffe599', '#b6d7a8', '#a2c4c9', '#9fc5e8', '#b4a7d6', '#d5a6bd'
				, '#e06666', '#f6b26b', '#ffd966', '#93c47d', '#76a5af', '#6fa8dc', '#8e7cc3', '#c27ba0'
				, '#cc0000', '#e69138', '#f1c232', '#6aa84f', '#45818e', '#3d85c6', '#674ea7', '#a64d79'
				, '#990000', '#b45f06', '#bf9000', '#38761d', '#134f5c', '#0b5394', '#351c75', '#741b47'
				, '#660000', '#783f04', '#7f6000', '#274e13', '#0c343d', '#073763', '#20124d', '#4C1130'],
			showEffect: 'show',
			hideEffect: 'hide',
			onChangeColor: false
		}
	};

	function positionAndShowBox($txt, $box) {
		var pos = $txt.offset(), tw = $txt.outerWidth(), bw = $box.outerWidth();

		var left = tw > bw ? pos.left : pos.left - (bw - tw);

		$box.css({
			left: left < 0 ? 0 : left,
			top: pos.top + $txt.outerHeight()
		});

		showBox($box);
	}

	function showBox($box) {
		var opts = $box.data('opts');
		$box[opts.showEffect]();
		$(document).on('click.simple_color_picker', function() {
			hideBox($box);
		});
	}

	function hideBox($box) {
		var opts = $box.data('opts');
		$box[opts.hideEffect]();
		$(document).off('.simple_color_picker');
	}

	function initBox($txt, opts) {
		var $box = $('<div>', {
			'id': 'color_picker_' + ($txt.attr('id') || new Date().getTime()),
			'class': 'ui-simple-color-picker'
		}).hide().appendTo('body');

		var $ul;
		for (var i = 0; i < opts.colors.length; i++) {
			if (i % opts.colorsPerLine == 0) {
				$ul = $('<ul>');
				$box.append($ul);
			}

			var c = opts.colors[i];
			$ul.append($('<li>', {
				'style': 'background-color: ' + c,
				'title': c
			}));
		}

		$box.data('opts', opts);
		$txt.data('simpleColorPicker', $box);
		return $box;
	}

	var api = {
		destroy: function() {
			this.each(function() {
				var $box = $(this).data('simpleColorPicker');
				if ($box) {
					$box.remove();
				}
			}).off('.simple_color_picker').removeData('simpleColorPicker');
		}
	};

	$.fn.simpleColorPicker = function(options) {
		// Methods
		if (typeof options == 'string') {
			api[options].apply(this);
			return this;
		}

		return this.each(function() {
			var $txt = $(this);

			var opts = $.extend({}, $.simpleColorPicker.defaults, options);
			if (!opts.onChangeColor) {
				var occ = $txt.attr('onChangeColor');
				if (occ) {
					opts.onChangeColor = new Function(occ);
				}
			}

			var $box = initBox($txt, opts);

			$box.find('li').click(function() {
				if ($txt.is('input')) {
					$txt.val($(this).attr('title'));
				}
				if ($.isFunction(opts.onChangeColor)) {
					opts.onChangeColor.call($txt, $(this).attr('title'));
				}
				hideBox($box);
			});

			$box.click(function(evt) {
				evt.stopPropagation();
			});

			$txt.on('click.simple_color_picker', function(evt) {
				setTimeout(function() {
					positionAndShowBox($txt, $box);
				})
			});

			if ($txt.is('input')) {
				$txt.on('focus.simple_color_picker', function() {
					positionAndShowBox($txt, $box);
				});
			}
		});
	};

	// COLOR-PICKER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="simpleColorPicker"]').simpleColorPicker();
	});

}(jQuery));
