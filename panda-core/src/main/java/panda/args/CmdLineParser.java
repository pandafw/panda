package panda.args;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.cast.CastException;
import panda.cast.Castors;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.collection.KeyValue;
import panda.lang.reflect.FieldInjector;
import panda.lang.reflect.Fields;
import panda.lang.reflect.Injector;
import panda.lang.reflect.MethodInjector;
import panda.lang.reflect.Methods;

public class CmdLineParser {
	private Castors castors;
	private Object bean;
	
	private Map<Argument, Injector> argInjectors;
	private Map<Option, Injector> optInjectors;

	private Map<Argument, Object> argValues;
	private Map<Option, Object> optValues;
	
	private List<String> cmdLineArgs;

	/**
	 * Creates a new command line owner that parses arguments/options and set them into the given
	 * object.
	 *
	 * @param bean instance of a class annotated by {@link Option} and {@link Argument}. this object
	 *            will receive values. If this is {@code null}, the processing will be skipped,
	 *            which is useful if you'd like to feed metadata from other sources.
	 * @throws IllegalArgumentException if the option bean class is using args4j annotations
	 *             incorrectly.
	 */
	public CmdLineParser(Object bean) {
		this.bean = bean;
		castors = Castors.i();

		initArguments();
		initOptions();
	}
	
	private void initArguments() {
		List<KeyValue<Argument, Injector>> args = new ArrayList<KeyValue<Argument, Injector>>();

		Collection<Field> fields = Fields.getAnnotationFields(bean.getClass(), Argument.class);
		for (Field field : fields) {
			Argument a = field.getAnnotation(Argument.class);
			args.add(new KeyValue<Argument, Injector>(a, new FieldInjector(field)));
		}

		Collection<Method> methods = Methods.getAnnotationMethods(bean.getClass(), Argument.class);
		for (Method method : methods) {
			Argument a = method.getAnnotation(Argument.class);
			args.add(new KeyValue<Argument, Injector>(a, new MethodInjector(method)));
		}
		
		// sort
		Collections.sort(args, new Comparator<KeyValue<Argument, Injector>>() {
			@Override
			public int compare(KeyValue<Argument, Injector> e1, KeyValue<Argument, Injector> e2) {
				Argument a1 = e1.getKey();
				Argument a2 = e2.getKey();
				if (a1.index() == a2.index()) {
					return 0;
				}
				if (a1.index() < 0) {
					return 1;
				}
				if (a2.index() < 0) {
					return -1;
				}
				return a1.index() - a2.index();
			}
		});

		argInjectors = new LinkedHashMap<Argument, Injector>();
		for (KeyValue<Argument, Injector> e : args) {
			argInjectors.put(e.getKey(), e.getValue());
		}
		argValues = new HashMap<Argument, Object>();
	}

	private void initOptions() {
		List<KeyValue<Option, Injector>> opts = new ArrayList<KeyValue<Option, Injector>>();
		
		Collection<Field> fields = Fields.getAnnotationFields(bean.getClass(), Option.class);
		for (Field field : fields) {
			Option o = field.getAnnotation(Option.class);
			opts.add(new KeyValue<Option, Injector>(o, new FieldInjector(field)));
		}

		Collection<Method> methods = Methods.getAnnotationMethods(bean.getClass(), Option.class);
		for (Method method : methods) {
			Option o = method.getAnnotation(Option.class);
			opts.add(new KeyValue<Option, Injector>(o, new MethodInjector(method)));
		}

		// check duplicated
		Set<Character> flags = new HashSet<Character>();
		Set<String> longs = new HashSet<String>();
		for (KeyValue<Option, Injector> en : opts) {
			Option o = en.getKey();
			if (o.opt() != ' ' && !flags.add(o.opt())) {
				throw new IllegalArgumentException("Duplicated option -" + o.opt());
			}
			if (Strings.isNotEmpty(o.option()) && !longs.add(o.option())) {
				throw new IllegalArgumentException("Duplicated option --" + o.option());
			}
		}

		// sort
		Collections.sort(opts, new Comparator<KeyValue<Option, Injector>>() {
			@Override
			public int compare(KeyValue<Option, Injector> e1, KeyValue<Option, Injector> e2) {
				Option o1 = e1.getKey();
				Option o2 = e2.getKey();
				
				if (o1.opt() == ' ') {
					if (o2.opt() == ' ') {
						return o1.option().compareTo(o2.option());
					}
					return o1.option().compareTo(String.valueOf(o2.opt()));
				}

				if (o2.opt() == ' ') {
					return String.valueOf(o1.opt()).compareTo(o2.option());
				}
				return o1.opt() - o2.opt();
			}
		});

		optInjectors = new LinkedHashMap<Option, Injector>();
		for (KeyValue<Option, Injector> e : opts) {
			optInjectors.put(e.getKey(), e.getValue());
		}
		optValues = new HashMap<Option, Object>();
	}

	protected Option findOption(String name, String a) throws CmdLineException {
		for (Option o : optInjectors.keySet()) {
			if (o.option().equals(name)) {
				return o;
			}
		}
		throw new CmdLineException("Invalid option " + a);
	}
	
	protected Option findOption(char flag, String a) throws CmdLineException {
		for (Option o : optInjectors.keySet()) {
			if (o.opt() != ' ' && o.opt() == flag) {
				return o;
			}
		}
		throw new CmdLineException("Invalid option " + a);
	}
	
	protected void inject(Iterator<String> it, Option opt, String a, Object v) throws CmdLineException {
		if (Strings.isEmpty(opt.arg())) {
			v = true;
		}
		else if (v == null) {
			if (it == null) {
				throw new CmdLineException("Invalid option " + a);
			}
			if (!it.hasNext()) {
				throw new CmdLineException("Missing option argument for " + a);
			}
			v = it.next();
		}

		try {
			Injector inj = optInjectors.get(opt);
			Object o = castors.cast(v, inj.type(bean));
			optValues.put(opt, o);
			inj.inject(bean, o);
		}
		catch (CastException e) {
			throw new CmdLineException("Failed to inject option " + a, e);
		}
	}

	protected List<String> injectOptions(String[] args) throws CmdLineException {
		List<String> as = new ArrayList<String>();

		Iterator<String> it = Arrays.asList(args).iterator();
		while (it.hasNext()) {
			String a = it.next();
			if (Strings.isEmpty(a)) {
				continue;
			}

			if (Strings.startsWithChar(a, '-')) {
				if (a.equals("--")) {
					// end of options
					while (it.hasNext()) {
						as.add(it.next());
					}
					break;
				}
				
				if (Strings.startsWith(a, "--")) {
					String s = a.substring(2);
					String v = null;
					
					int d = s.indexOf('=');
					if (d > 0) {
						// handle --arg=value
						v = s.substring(d + 1);
						s = s.substring(0, d);
					}

					Option opt = findOption(s, a);
					inject(it, opt, a, v);
					continue;
				}

				if (a.length() == 2) {
					// handle -b
					char c = a.charAt(1);
					Option opt = findOption(c, a);
					inject(it, opt, a, null);
					continue;
				}

				if (a.length() > 2) {
					// handle -barg
					char c = a.charAt(1);
					Option opt = findOption(c, a);
					if (Strings.isNotEmpty(opt.arg())) {
						String v = a.substring(2);
						inject(it, opt, a, v);
						continue;
					}
					
					// handle -abcd
					inject(it, opt, a, null);
					for (int i = 2; i < a.length(); i++) {
						c = a.charAt(i);
						opt = findOption(c, a);
						inject(null, opt, a, null);
					}
					continue;
				}
			}

			// add to arguments
			as.add(a);
		}

		return as;
	}

	protected void inject(Argument a, Object v) throws CmdLineException {
		try {
			Injector inj = argInjectors.get(a);
			Object o = castors.cast(v, inj.type(bean));
			argValues.put(a, o);
			inj.inject(bean, o);
		}
		catch (CastException e) {
			throw new CmdLineException("Failed to inject argument [" + a.name() + "]", e);
		}
	}
	
	protected void injectArguments(List<String> as) throws CmdLineException {
		if (Collections.isEmpty(as) || !needArguments()) {
			return;
		}

		// indexed arguments
		int idx = -1;
		for (Argument a : argInjectors.keySet()) {
			int i = a.index();
			if (i >= 0 && i < as.size()) {
				inject(a, as.get(i));
			}
			if (i > idx) {
				idx = i;
			}
		}
		
		if (++idx < as.size()) {
			// remained no-index arguments
			String[] ss = as.subList(idx, as.size()).toArray(new String[0]);
			for (Argument a : argInjectors.keySet()) {
				if (a.index() < 0) {
					inject(a, ss);
				}
			}
		}
	}

	protected void validateOptions() throws CmdLineException {
		for (Option o : optInjectors.keySet()) {
			if (o.required()) {
				if (!optValues.containsKey(o)) {
					StringBuilder sb = new StringBuilder();
					sb.append("The option ");
					if (o.opt() != ' ') {
						sb.append('-').append(o.opt()).append(',');
					}
					sb.append("--").append(o.option());
					sb.append(" is required!");
					throw new CmdLineException(sb.toString());
				}
			}
		}
	}

	protected void validateArguments() throws CmdLineException {
		if (needArguments()) {
			for (Argument a : argInjectors.keySet()) {
				if (a.required()) {
					if (!argValues.containsKey(a)) {
						StringBuilder sb = new StringBuilder();
						sb.append("The argument <");
						sb.append(a.name());
						sb.append("> is required!");
						throw new CmdLineException(sb.toString());
					}
				}
			}
		}
		else {
			if (Collections.isNotEmpty(cmdLineArgs)) {
				throw new CmdLineException("Invalid arguments - " + Strings.join(cmdLineArgs, ' '));
			}
		}
	}

	public void parse(String[] args) throws CmdLineException {
		cmdLineArgs = injectOptions(args);
		injectArguments(cmdLineArgs);
	}

	public void validate() throws CmdLineException {
		validateArguments();
		validateOptions();
	}

	public boolean needArguments() {
		for (Argument a : argInjectors.keySet()) {
			if (a.required()) {
				return true;
			}
		}
		return false;
	}

	public String usage() {
		return usage("java " + bean.getClass().getName());
	}
	
	public String usage(String cmd) {
		List<Option> opts = getDisplayOptions();
		List<Argument> args = getDisplayArguments();

		StringBuilder sb = new StringBuilder();

		sb.append("Usage: " + cmd);
		if (Collections.isNotEmpty(opts)) {
			sb.append(" [OPTIONS]");
		}
		if (Collections.isNotEmpty(args)) {
			for (Argument a : args) {
				sb.append(' ');
				sb.append(a.required() ? '<' : '[');
				sb.append(a.name());
				sb.append(a.required() ? '>' : ']');
			}
		}
		sb.append(Streams.EOL);
		
		if (Collections.isNotEmpty(args)) {
			sb.append(Streams.EOL).append("Arguments: ").append(Streams.EOL);
			
			int len = 0;
			for (Argument a : args) {
				if (a.name().length() > len) {
					len = a.name().length();
				}
			}
			
			len += 6; // []____
			for (Argument a : args) {
				String n = (a.required() ? '<' : '[') + a.name() + (a.required() ? '>' : ']');
				n = Strings.rightPad(n, len);
				sb.append("  ").append(n).append(a.usage()).append(Streams.EOL);
			}
		}

		if (Collections.isNotEmpty(opts)) {
			sb.append(Streams.EOL).append("Options: ").append(Streams.EOL);
			
			int len = 0;
			for (Option o : opts) {
				int l = o.option().length();
				if (o.arg().length() > 0) {
					l += o.arg().length() + 1;
				}
				if (l > len) {
					len = l;
				}
			}
			
			len += 6; // --xxx=yyy____
			for (Option o : opts) {
				sb.append("  ");
				if (o.opt() == ' ') {
					sb.append("    ");
				}
				else {
					sb.append('-').append(o.opt()).append(", ");
				}

				String n = "";
				if (o.option().length() > 0) {
					n = "--" + o.option();
					if (o.arg().length() > 0) {
						n += '=' + o.arg();
					}
				}
				n = Strings.rightPad(n, len);
				sb.append(n).append(o.usage()).append(Streams.EOL);
			}
		}
		return sb.toString();
	}

	private List<Option> getDisplayOptions() {
		List<Option> opts = new ArrayList<Option>();
		
		// add display option
		for (Option o : optInjectors.keySet()) {
			if (!o.hidden()) {
				opts.add(o);
			}
		}

		return opts;
	}

	private List<Argument> getDisplayArguments() {
		List<Argument> args = new ArrayList<Argument>();
		
		// add display option
		for (Argument a : argInjectors.keySet()) {
			if (!a.hidden()) {
				args.add(a);
			}
		}

		return args;
	}
}
