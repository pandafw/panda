if (typeof(panda) == "undefined") { panda = {}; }

panda.call = function(f, p) {
	switch (typeof(f)) {
	case "function":
		f.call(p);
		break;
	case "string":
		f = new Function(f);
		f.call(p);
		break;
	}
};
