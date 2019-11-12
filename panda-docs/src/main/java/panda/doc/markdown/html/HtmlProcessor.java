package panda.doc.markdown.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import panda.doc.markdown.Configuration;
import panda.doc.markdown.IncludePlugin;
import panda.doc.markdown.Plugin;
import panda.doc.markdown.Processor;
import panda.doc.markdown.Configuration.Builder;

public class HtmlProcessor {
	
	private Builder builder;
	
	private CustomizeDecorator decorator;
	
	public HtmlProcessor() {
		this.builder = builder();
	}
	
	private Builder builder() {
		decorator = new CustomizeDecorator();
		return Configuration.builder().forceExtentedProfile().registerPlugins(new YumlPlugin(), new WebSequencePlugin(), new IncludePlugin()).setDecorator(decorator).setCodeBlockEmitter(new CodeBlockEmitter());
	}

	public HtmlProcessor registerPlugins(Plugin ... plugins) {
		builder.registerPlugins(plugins);
		return this;
	}
	
	public HtmlProcessor setDecorator(CustomizeDecorator decorator) {
		this.decorator = decorator;
		builder.setDecorator(decorator);
		return this;
	}
	
	public HtmlProcessor addHtmlAttribute(String name, String value, String ... tags) {
		decorator.addAttribute(name, value, tags);
		return this;
	}
	
	public HtmlProcessor addCssClass(String styleClass, String ... tags) {
		decorator.addCssClass(styleClass, tags);
		return this;
	}
	
	public String process(File file) throws IOException {
		return Processor.process(file, builder.build());
	}
	
	public String process(InputStream input) throws IOException {
		return Processor.process(input);
	}
	
	public String process(Reader reader) throws IOException {
		return Processor.process(reader, builder.build());
	}
	
	public String process(String input) throws IOException {
		return Processor.process(input, builder.build());
	}
}
