function panda_call(f, p) {
	switch (typeof(f)) {
	case "function":
		f.call(p);
		break;
	case "string":
		f = new Function(f);
		f.call(p);
		break;
	}
}

if (typeof Function.prototype.createCallback != "function") {
	/**
	 * Creates a callback that passes arguments[0], arguments[1], arguments[2], ...
	 * Call directly on any function. Example: <code>myFunction.createCallback(arg1, arg2)</code>
	 * Will create a function that is bound to those 2 args. <b>If a specific scope is required in the
	 * callback, use {@link #createDelegate} instead.</b> The function returned by createCallback always
	 * executes in the window scope.
	 * <p>This method is required when you want to pass arguments to a callback function.  If no arguments
	 * are needed, you can simply pass a reference to the function as a callback (e.g., callback: myFn).
	 * However, if you tried to pass a function with arguments (e.g., callback: myFn(arg1, arg2)) the function
	 * would simply execute immediately when the code is parsed. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}
		
		// clicking the button alerts "Hi, Fred"
		new Ext.Button({
			text: 'Say Hi',
			renderTo: Ext.getBody(),
			handler: sayHi.createCallback('Fred')
		});
		</code></pre>
	 * @return {Function} The new function
	 */
	Function.prototype.createCallback = function(/*args...*/){
		// make args available, in function below
		var args = arguments;
		var method = this;
		return function() {
			return method.apply(window, args);
		};
	};
}

if (typeof Function.prototype.createDelegate != "function") {
	/**
	 * Creates a delegate (callback) that sets the scope to obj.
	 * Call directly on any function. Example: <code>this.myFunction.createDelegate(this, [arg1, arg2])</code>
	 * Will create a function that is automatically scoped to obj so that the <tt>this</tt> variable inside the
	 * callback points to obj. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			// Note this use of "this.text" here.  This function expects to
			// execute within a scope that contains a text property.  In this
			// example, the "this" variable is pointing to the btn object that
			// was passed in createDelegate below.
			alert('Hi, ' + name + '. You clicked the "' + this.text + '" button.');
		}

		var btn = new Ext.Button({
			text: 'Say Hi',
			renderTo: Ext.getBody()
		});

		// This callback will execute in the scope of the
		// button instance. Clicking the button alerts
		// "Hi, Fred. You clicked the "Say Hi" button."
		btn.on('click', sayHi.createDelegate(btn, ['Fred']));
		</code></pre>
	 * @param {Object} obj (optional) The object for which the scope is set
	 * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
	 * @param {Boolean/Number} appendArgs (optional) if True args are appended to call args instead of overriding,
	 *											   if a number the args are inserted at the specified position
	 * @return {Function} The new function
	 */
	Function.prototype.createDelegate = function(obj, args, appendArgs) {
		var method = this;
		return function() {
			var callArgs = args || arguments;
			if(appendArgs === true){
				callArgs = Array.prototype.slice.call(arguments, 0);
				callArgs = callArgs.concat(args);
			}else if(typeof appendArgs == "number"){
				callArgs = Array.prototype.slice.call(arguments, 0); // copy arguments first
				var applyArgs = [appendArgs, 0].concat(args); // create method call params
				Array.prototype.splice.apply(callArgs, applyArgs); // splice them in
			}
			return method.apply(obj || window, callArgs);
		};
	};
}

if (typeof Function.prototype.defer != "function") {
	/**
	 * Calls this function after the number of millseconds specified, optionally in a specific scope. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		// executes immediately:
		sayHi('Fred');

		// executes after 2 seconds:
		sayHi.defer(2000, this, ['Fred']);

		// this syntax is sometimes useful for deferring
		// execution of an anonymous function:
		(function(){
			alert('Anonymous');
		}).defer(100);
		</code></pre>
	 * @param {Number} millis The number of milliseconds for the setTimeout call (if 0 the function is executed immediately)
	 * @param {Object} obj (optional) The object for which the scope is set
	 * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
	 * @param {Boolean/Number} appendArgs (optional) if True args are appended to call args instead of overriding,
	 *											   if a number the args are inserted at the specified position
	 * @return {Number} The timeout id that can be used with clearTimeout
	 */
	Function.prototype.defer = function(millis, obj, args, appendArgs) {
		var fn = this.createDelegate(obj, args, appendArgs);
		if(millis){
			return setTimeout(fn, millis);
		}
		fn();
		return 0;
	};
}

if (typeof Function.prototype.createSequence != "function") {
	/**
	 * Create a combined function call sequence of the original function + the passed function.
	 * The resulting function returns the results of the original function.
	 * The passed fcn is called with the parameters of the original function. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		sayHi('Fred'); // alerts "Hi, Fred"

		var sayGoodbye = sayHi.createSequence(function(name){
			alert('Bye, ' + name);
		});

		sayGoodbye('Fred'); // both alerts show
		</code></pre>
	 * @param {Function} fcn The function to sequence
	 * @param {Object} scope (optional) The scope of the passed fcn (Defaults to scope of original function or window)
	 * @return {Function} The new function
	 */
	Function.prototype.createSequence = function(fcn, scope) {
		if(typeof fcn != "function"){
			return this;
		}
		var method = this;
		return function() {
			var retval = method.apply(this || window, arguments);
			fcn.apply(scope || this || window, arguments);
			return retval;
		};
	};
}

if (typeof Function.prototype.createInterceptor != "function") {
	/**
	 * Creates an interceptor function. The passed fcn is called before the original one. If it returns false,
	 * the original one is not called. The resulting function returns the results of the original function.
	 * The passed fcn is called with the parameters of the original function. Example usage:
	 * <pre><code>
		var sayHi = function(name){
			alert('Hi, ' + name);
		}

		sayHi('Fred'); // alerts "Hi, Fred"

		// create a new function that validates input without
		// directly modifying the original function:
		var sayHiToFriend = sayHi.createInterceptor(function(name){
			return name == 'Brian';
		});

		sayHiToFriend('Fred');	// no alert
		sayHiToFriend('Brian'); // alerts "Hi, Brian"
		</code></pre>
	 * @param {Function} fcn The function to call before the original
	 * @param {Object} scope (optional) The scope of the passed fcn (Defaults to scope of original function or window)
	 * @return {Function} The new function
	 */
	Function.prototype.createInterceptor = function(fcn, scope) {
		if(typeof fcn != "function"){
			return this;
		}
		var method = this;
		return function() {
			fcn.target = this;
			fcn.method = method;
			if(fcn.apply(scope || this || window, arguments) === false){
				return;
			}
			return method.apply(this || window, arguments);
		};
	};
}

