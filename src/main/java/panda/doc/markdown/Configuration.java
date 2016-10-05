package panda.doc.markdown;

import java.util.ArrayList;
import java.util.List;

import panda.doc.markdown.html.DefaultDecorator;

/**
 * Txtmark configuration.
 */
public class Configuration {
	final boolean safeMode;
	final String encoding;
	final Decorator decorator;
	final BlockEmitter codeBlockEmitter;
	final boolean forceExtendedProfile;
	final boolean convertNewline2Br;
	final SpanEmitter specialLinkEmitter;
	final List<Plugin> plugins;

	/**
	 * <p>
	 * This is the default configuration for txtmark's <code>process</code> methods
	 * </p>
	 * <ul>
	 * <li><code>safeMode = false</code></li>
	 * <li><code>encoding = UTF-8</code></li>
	 * <li><code>decorator = DefaultDecorator</code></li>
	 * <li><code>codeBlockEmitter = null</code></li>
	 * </ul>
	 */
	public final static Configuration DEFAULT = Configuration.builder().build();

	/**
	 * <p>
	 * Default safe configuration
	 * </p>
	 * <ul>
	 * <li><code>safeMode = true</code></li>
	 * <li><code>encoding = UTF-8</code></li>
	 * <li><code>decorator = DefaultDecorator</code></li>
	 * <li><code>codeBlockEmitter = null</code></li>
	 * </ul>
	 */
	public final static Configuration DEFAULT_SAFE = Configuration.builder().enableSafeMode().build();

	/**
	 * Constructor.
	 * 
	 * @param safeMode
	 * @param encoding
	 * @param decorator
	 */
	Configuration(boolean safeMode, String encoding, Decorator decorator, BlockEmitter codeBlockEmitter,
			boolean forceExtendedProfile, boolean convertNewline2Br, SpanEmitter specialLinkEmitter,
			List<Plugin> plugins) {
		this.safeMode = safeMode;
		this.encoding = encoding;
		this.decorator = decorator;
		this.codeBlockEmitter = codeBlockEmitter;
		this.convertNewline2Br = convertNewline2Br;
		this.forceExtendedProfile = forceExtendedProfile;
		this.specialLinkEmitter = specialLinkEmitter;
		this.plugins = plugins;
	}

	/**
	 * Creates a new Builder instance.
	 * 
	 * @return A new Builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Configuration builder.
	 * 
	 * @author René Jeschke <rene_jeschke@yahoo.de>
	 */
	public static class Builder {
		private boolean safeMode = false;
		private boolean forceExtendedProfile = false;
		private boolean convertNewline2Br = false;
		private String encoding = "UTF-8";
		private Decorator decorator = new DefaultDecorator();
		private BlockEmitter codeBlockEmitter = null;
		private SpanEmitter specialLinkEmitter = null;
		private List<Plugin> plugins = new ArrayList<Plugin>();

		/**
		 * Constructor.
		 */
		Builder() {
			// empty
		}

		/**
		 * Enables HTML safe mode. Default: <code>false</code>
		 * 
		 * @return This builder
		 */
		public Builder enableSafeMode() {
			this.safeMode = true;
			return this;
		}

		/**
		 * Forces extened profile to be enabled by default.
		 * 
		 * @return This builder.
		 */
		public Builder forceExtentedProfile() {
			this.forceExtendedProfile = true;
			return this;
		}

		/**
		 * convertNewline2Br.
		 * 
		 * @return This builder.
		 */
		public Builder convertNewline2Br() {
			this.convertNewline2Br = true;
			return this;
		}

		/**
		 * Sets the HTML safe mode flag. Default: <code>false</code>
		 * 
		 * @param flag <code>true</code> to enable safe mode
		 * @return This builder
		 */
		public Builder setSafeMode(boolean flag) {
			this.safeMode = flag;
			return this;
		}

		/**
		 * Sets the character encoding for txtmark. Default: <code>&quot;UTF-8&quot;</code>
		 * 
		 * @param encoding The encoding
		 * @return This builder
		 * @since 0.7
		 */
		public Builder setEncoding(String encoding) {
			this.encoding = encoding;
			return this;
		}

		/**
		 * Sets the decorator for txtmark. Default: <code>DefaultDecorator()</code>
		 * 
		 * @param decorator The decorator
		 * @return This builder
		 * @see DefaultDecorator
		 */
		public Builder setDecorator(Decorator decorator) {
			this.decorator = decorator;
			return this;
		}

		/**
		 * Sets the code block emitter. Default: <code>null</code>
		 * 
		 * @param emitter The BlockEmitter
		 * @return This builder
		 * @see BlockEmitter
		 * @since 0.7
		 */
		public Builder setCodeBlockEmitter(BlockEmitter emitter) {
			this.codeBlockEmitter = emitter;
			return this;
		}

		/**
		 * Sets the emitter for special link spans ([[ ... ]]).
		 * 
		 * @param emitter The emitter.
		 * @return This builder.
		 */
		public Builder setSpecialLinkEmitter(SpanEmitter emitter) {
			this.specialLinkEmitter = emitter;
			return this;
		}

		/**
		 * Sets the plugins.
		 * 
		 * @param plugins The plugins.
		 * @return This builder.
		 */
		public Builder registerPlugins(Plugin... plugins) {
			for (Plugin plugin : plugins) {
				this.plugins.add(plugin);
			}
			return this;
		}

		/**
		 * Builds a configuration instance.
		 * 
		 * @return a Configuration instance
		 */
		public Configuration build() {
			return new Configuration(this.safeMode, this.encoding, this.decorator, this.codeBlockEmitter,
				this.forceExtendedProfile, this.convertNewline2Br, this.specialLinkEmitter, this.plugins);
		}

		public Decorator getDecorator() {
			return decorator;
		}
	}
}
