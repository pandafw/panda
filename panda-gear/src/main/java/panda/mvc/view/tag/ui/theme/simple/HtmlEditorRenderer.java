package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.HtmlEditor;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class HtmlEditorRenderer extends AbstractEndRenderer<HtmlEditor> {
	private static final String SUMMERNOTE_KEY = HtmlEditorRenderer.class.getName() + ".summernote";
	private static final String SUMMERNOTE_VERSION = "0.8.9";
	private static final String SUMMERNOTE_CDN = "http://cdnjs.cloudflare.com/ajax/libs/summernote/" + SUMMERNOTE_VERSION + "/summernote";
	private static final String SUMMERNOTE_PATH = "/summernote/summernote";

	private static final String CLEDITOR_KEY = HtmlEditorRenderer.class.getName() + ".cleditor";
	private static final String CLEDITOR_VERSION = "1.4.5";
	private static final String CLEDITOR_PATH = "/cleditor/jquery.cleditor";

	public HtmlEditorRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attrs = new Attributes();

		attrs.id(tag)
			.name(tag)
			.cols(tag)
			.rows(tag)
			.wrap(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.maxlength(tag)
			.tooltip(tag)
			.placeholder(tag)
			.commons(tag)
			.events(tag);

		if ("cleditor".equalsIgnoreCase(tag.getEditor())) {
			attrs.css(this, "p-htmleditor p-cleditor");
			attrs.data("cleditorJs", cleditorJs());
			attrs.dynamics(tag);

			if (!Boolean.TRUE.equals(context.getReq().get(CLEDITOR_KEY))) {
				context.getReq().put(CLEDITOR_KEY, true);
				writeCss(cleditorCss());
			}
		}
		else {
			attrs.css(this, "p-htmleditor p-summernote");
			attrs.data("summernoteJs", summernoteJs());
			attrs.dynamics(tag);

			if (!Boolean.TRUE.equals(context.getReq().get(SUMMERNOTE_KEY))) {
				context.getReq().put(SUMMERNOTE_KEY, true);
				writeCss(summernoteCss());
			}
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

	private String cleditorCss() {
		return suri(CLEDITOR_PATH + ".css?v=" + CLEDITOR_VERSION);
	}
	
	private String cleditorJs() {
		return suri(CLEDITOR_PATH + debug() + ".js?v=" + CLEDITOR_VERSION);
	}

	private String debug() {
		return (tag.useDebug() ? "" : ".min");
	}
}
