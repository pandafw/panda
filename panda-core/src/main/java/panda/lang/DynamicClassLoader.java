package panda.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import panda.io.Files;
import panda.io.stream.CharSequenceReader;
import panda.log.Log;
import panda.log.Logs;

/**
 * 
 *
 */
public class DynamicClassLoader extends ClassLoader {
	private static Log log = Logs.getLog(DynamicClassLoader.class);

	private static class SourceJavaFileObject extends SimpleJavaFileObject {
		public final static String SCHEME = "source://";
		private String source;

		public SourceJavaFileObject(String name, Kind kind) {
			super(URI.create(SCHEME + '/' + name.replace('.', '/') + kind.extension), kind);
		}

		public SourceJavaFileObject(String className, String source) {
			super(URI.create(SCHEME + '/' + className.replace('.', '/') + Kind.SOURCE.extension),
					Kind.SOURCE);
			this.source = source;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return source;
		}
	}

	private static class ClassJavaFileObject extends SimpleJavaFileObject {
		public ClassJavaFileObject(DynamicClassLoader loader, String name, Kind kind) {
			super(toURI(loader, name, kind), kind);
		}

		private static URI toURI(DynamicClassLoader loader, String name, Kind kind) {
			File path = new File(loader.getBinFolder(), name.replace('.', '/') + kind.extension);
			return path.toURI();
		}
		
		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			throw Exceptions.unsupported("getCharContent");
		}

		@Override
		public InputStream openInputStream() throws IOException {
			return new FileInputStream(this.toUri().getPath());
		}

		@Override
		public OutputStream openOutputStream() throws IOException {
			File file = new File(toUri().getPath());
			Files.makeParents(file);
			return new FileOutputStream(file);
		}

		public byte[] getBinary() throws IOException {
			return Files.readToBytes(new File(toUri().getPath()));
		}
	}

	private static class DynamicJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
		private DynamicClassLoader classLoader;
		
		public DynamicJavaFileManager(JavaCompiler compiler,
				DiagnosticListener<? super JavaFileObject> listener,
				DynamicClassLoader classLoader) {
			super(compiler.getStandardFileManager(listener, null, null));
			this.classLoader = classLoader;
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind,
				FileObject sibling) throws IOException {
			synchronized (classLoader.javaObjects) {
				ClassJavaFileObject javaObj = new ClassJavaFileObject(classLoader, className, kind);
				classLoader.javaObjects.put(className, javaObj);
				return javaObj;
			}
		}

		@Override
		public ClassLoader getClassLoader(Location location) {
			return classLoader.delegate;
		}
	}
	
	private static void logSource(CharSequence className, String source) throws Exception {
		if (!log.isTraceEnabled()) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Compile: ").append(className).append('\n');

		LineNumberReader lnr = new LineNumberReader(new CharSequenceReader(source));
		String line;
		while ((line = lnr.readLine()) != null) {
			sb.append(Strings.rightPad(String.valueOf(lnr.getLineNumber()), 5));
			sb.append(":  ").append(line).append('\n');
		}
		lnr.close();
		log.trace(sb.toString());
	}
	
	private class JavaObjectClassLoader extends ClassLoader {
		public JavaObjectClassLoader(final ClassLoader pParent) {
			super(pParent);
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			//TODO: find from workdir
			synchronized (javaObjects) {
				ClassJavaFileObject obj = javaObjects.get(name);
				if (obj == null) {
					throw new ClassNotFoundException(name);
				}

				try {
					byte[] b = obj.getBinary();
					Class<?> clazz = super.defineClass(name, b, 0, b.length);
					return clazz;
				}
				catch (IOException e) {
					throw new ClassNotFoundException(name, e);
				}
			}
		}
	}

	private JavaCompiler compiler;
	private File workdir;
	private List<String> options;
	private ClassLoader parent;
	private ClassLoader delegate;
	private Map<String, ClassJavaFileObject> javaObjects = new HashMap<String, ClassJavaFileObject>();

	public DynamicClassLoader() {
		this(DynamicClassLoader.class.getClassLoader(), null);
		compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw Exceptions.unsupported("Failed to getSystemJavaCompiler()");
		}
	}

	public DynamicClassLoader(final ClassLoader parent, final File workdir) {
		super(parent);
		init(parent, workdir);
	}

	protected void init(final ClassLoader parent, final File workdir) {
		// runtime test
		new SourceJavaFileObject("test", Kind.SOURCE);

		this.parent = parent;
		this.delegate = new JavaObjectClassLoader(parent);

		if (workdir == null) {
			this.workdir = new File(Systems.getJavaIoTmpDir(), Objects.identityToString(this));
		}
		setCompileOptions();
	}

	public synchronized void clear() {
		try {
			Files.deleteDir(workdir);
		}
		catch (IOException e) {
			log.warn("Failed delete: " + workdir, e);
		}
	}

	protected synchronized void reset() {
		delegate = new JavaObjectClassLoader(parent);
		clear();
	}

	protected File getBinFolder() {
		return new File(workdir, "bin");
	}
	
	protected void setCompileOptions() {
		options = new ArrayList<String>();
		if (log.isTraceEnabled()) {
			options.add("-verbose");
		}

		try {
			StringBuilder buf = new StringBuilder(1000);
			
			buf.append('.');
			buf.append(File.pathSeparatorChar).append(getBinFolder().getCanonicalPath());

			Set<URL> urls = ClassLoaders.getAllClassLoaderURLs();
			for (URL url : urls) {
				buf.append(File.pathSeparatorChar).append(url.getFile());
			}
			options.add("-classpath");
			options.add(buf.toString());
		}
		catch (Exception e) {
			log.warn("Failed to set classpath", e);
		}
	}

	/**
	 * Compile source & load class
	 * @param className class name
	 * @param source source
	 * @return class
	 * @throws Exception if an error occurs
	 */
	public Class<?> loadClass(String className, String source) throws Exception {
		defineClass(className, source);
		return loadClass(className);
	}

	/**
	 * Compile java source
	 * @param className class name
	 * @param source source
	 * @throws Exception if an error occurs
	 */
	public synchronized void defineClass(String className, String source) throws Exception {
		try {
			delegate.loadClass(className);
			log.debug("Reload class - " + className);
			reset();
		}
		catch (ClassNotFoundException e) {
		}
		
		logSource(className, source);
		
		SourceJavaFileObject srcObj = new SourceJavaFileObject(className, source);
		
		DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
		JavaFileManager fileManager = new DynamicJavaFileManager(compiler, collector, this);

		try {
			List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
			sources.add(srcObj);

			JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector,
					options, null, sources);

			if (!task.call()) {
				StringBuilder error = new StringBuilder();
				error.append("---- Compile ERROR ----\n");
				for (Diagnostic<? extends JavaFileObject> diagnostic : collector.getDiagnostics()) {
					error.append(diagnostic.getMessage(Locale.getDefault())).append('\n');
				}
				error.append("---- Source ----\n").append(source);
				throw new RuntimeException(error.toString());
			}
		}
		finally {
			try {
				fileManager.close();
			}
			catch (Exception e) {
				// ignore
			}
		}
	}
	
	public void clearAssertionStatus() {
		delegate.clearAssertionStatus();
	}

	public URL getResource(String name) {
		return delegate.getResource(name);
	}

	public InputStream getResourceAsStream(String name) {
		return delegate.getResourceAsStream(name);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return delegate.loadClass(name);
	}

	public void setClassAssertionStatus(String className, boolean enabled) {
		delegate.setClassAssertionStatus(className, enabled);
	}

	public void setDefaultAssertionStatus(boolean enabled) {
		delegate.setDefaultAssertionStatus(enabled);
	}

	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		delegate.setPackageAssertionStatus(packageName, enabled);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clear();
	}
}

