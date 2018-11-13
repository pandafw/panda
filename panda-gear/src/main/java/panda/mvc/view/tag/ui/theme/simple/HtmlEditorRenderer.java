package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.HtmlEditor;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class HtmlEditorRenderer extends AbstractEndRenderer<HtmlEditor> {
	private static final String KEY = HtmlEditorRenderer.class.getName() + ".summernote";
	private static final String SUMMERNOTE_VERSION = "0.8.9";
	private static final String SUMMERNOTE_CDN = "http://cdnjs.cloudflare.com/ajax/libs/summernote/" + SUMMERNOTE_VERSION + "/summernote";
	private static final String SUMMERNOTE_PATH = "/summernote/summernote";

	public HtmlEditorRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attrs = new Attributes();

		attrs.id(tag)
			.name(tag)
			.css(this, "p-htmleditor")
			.cols(tag)
			.rows(tag)
			.wrap(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.maxlength(tag)
			.tooltip(tag)
			.placeholder(tag)
//			.data("summernoteCss", summernoteCss())
			.data("summernoteJs", summernoteJs())
			.commons(tag)
			.events(tag)
			.dynamics(tag);

		if (!Boolean.TRUE.equals(context.getReq().get(KEY))) {
			context.getReq().put(KEY, true);
			writeCss(summernoteCss());
//			writeJs(summernoteJs());
		}

		stag("textarea", attrs);
		write(formatValue(tag.getValue(), tag.getFormat()));
		etag("textarea");
	}
	
	private String summernoteCss() {
		if (tag.useCdn()) {
			return SUMMERNOTE_CDN + ".css";
		}
		return suri(SUMMERNOTE_PATH + ".css?v=" + SUMMERNOTE_VERSION);
	}
	
	private String summernoteJs() {
		if (tag.useCdn()) {
			return SUMMERNOTE_CDN + debug() + ".js";
		}
		return suri(SUMMERNOTE_PATH + debug() + ".js?v=" + SUMMERNOTE_VERSION);
	}

	private String debug() {
		return (tag.useDebug() ? "" : ".min");
	}
}
