package panda.doc.markdown;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import panda.io.Streams;

public class IncludePlugin extends Plugin {

	public IncludePlugin() {
		super("include");
	}

	@Override
	public void emit(final StringBuilder out, final List<String> lines, final Map<String, String> params) {
		String src = params.get("src");
		try {
			String content2 = getContent(src);
			if (content2 != null) {
				out.append(content2);
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Error while rendering " + this.getClass().getName(), e);
		}
	}

	private String getContent(String src) throws IOException, URISyntaxException {
		URL url = new URL(new URL("file:"), src);
		return Streams.toString(url);
	}
}
