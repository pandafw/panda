/** 
 * Placeholder plugin for jQuery
 * @author Daniel Stocks (http://webcloud.se)
 */
(function($) {
	function Placeholder(input) {
		this.input = input;
		if (input.attr('type') == 'password') {
			this.handlePassword();
		}
		// Prevent placeholder values from submitting
		$(input[0].form).submit(function() {
			if (input.hasClass('placeholder')) {
				input[0].value = '';
			}
		});
	}
	Placeholder.prototype = {
		show : function(loading) {
			// FF and IE saves values when you refresh the page. If the user refreshes the page with 
			// the placeholders showing they will be the default values and the input fields won't be empty.

			if (this.input[0].value === ''
					|| (loading && this.valueIsPlaceholder())) {
				if (this.isPassword) {
					try {
						this.input[0].setAttribute('type', 'text');
					} catch (e) {
						this.input.before(this.fakePassword.show()).hide();
					}
				}
				this.input[0].value = this.input.attr('placeholder');
				this.input.addClass('placeholder');
			}
		},
		hide : function() {
			if (this.valueIsPlaceholder() && this.input.hasClass('placeholder')) {
				if (this.isPassword) {
					try {
						this.input[0].setAttribute('type', 'password');
					} catch (e) {
					}
					// Restore focus for Opera and IE
					this.input.show();
					this.input[0].focus();
				}
				this.input[0].value = '';
				this.input.removeClass('placeholder');
			}
		},
		valueIsPlaceholder : function() {
			return this.input[0].value == this.input.attr('placeholder');
		},
		handlePassword : function() {

			var input = this.input;
			input.attr('realType', 'password');
			this.isPassword = true;
			// IE < 9 doesn't allow changing the type of password inputs
			if ($.browser.msie && input[0].outerHTML) {
				var fake = $(input[0].outerHTML.replace(
						/type=(['"])?password\1/gi, 'type=$1text$1'));
				this.fakePassword = fake.val(input.attr('placeholder'))
						.addClass('placeholder').focus(function() {
							input.trigger('focus');
							$(this).hide();
						});
				$(input[0].form).submit(function() {
					fake.remove();
					input.show()
				})
			}
		}
	};
	var supported = !!("placeholder" in document.createElement("input"));
	$.fn.placeholder = function() {
		return supported ? this : this.each(function() {
			var input = $(this);
			var placeholder = new Placeholder(input);
			placeholder.show(true);
			input.focus(function() {
				placeholder.hide();
			});
			input.blur(function() {
				placeholder.show(false);
			});

			// On page refresh, IE doesn't re-populate user input
			// until the window.onload event is fired.
			if ($.browser.msie) {
				$(window).load(function() {
					if (input.val()) {
						input.removeClass("placeholder");
					}
					placeholder.show(true);
				});
				input.focus(function() {
					if (this.value == "") {
						var a = this.createTextRange();
						a.collapse(!0);
						a.moveStart("character", 0);
						a.select();
					}
				});
			}
		});
	}
})(jQuery);
