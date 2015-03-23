package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.io.Files;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class UploaderRenderer extends AbstractEndRenderer<Uploader> {
	public UploaderRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attr = new Attributes();
		
		boolean readonly = Boolean.TRUE.equals(tag.getReadonly());
		boolean disabled = Boolean.TRUE.equals(tag.getDisabled());
		
		String pul = tag.getUploadLink();
		String pun = tag.getUploadName();
		String pdl = tag.getDnloadLink();

		attr.id(tag)
			.data("uploadLink", pul)
			.data("uploadName", pun)
			.data("dnloadLink", pdl)
			.cssClass("p-uploader")
			.title(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		
		if (!(readonly || disabled)) {
			attr.data("spy", "puploader");
		}
		stag("div", attr);

		Attributes a = new Attributes();
		a.add("type", "file");
		if (readonly) {
			a.css(this, "p-uploader-file p-hidden");
		}
		else {
			a.css(this, "p-uploader-file");
		}
		a.add("name", "")
		 .add("value", "")
		 .disabled(tag)
		 .size(tag)
		 .accept(tag)
		 .tabindex(tag);
		xtag("input", a);

		String name = tag.getName();
		String uct = defs(tag.getFileContentType());
		Integer ufs = tag.getFileSize();
		
		boolean isImg = uct.startsWith("image");
		
		a = new Attributes();
		a.type("hidden")
		 .cssClass("p-uploader-fid")
		 .disabled(disabled);
		if (name != null) {
			a.addIfExists("name", name);
			a.addIfExists("value", tag.getFileId());
		}
		xtag("input", a);

		write("<span class=\"p-uploader-text\">");
		if (tag.isFileExits()) {
			if (Strings.isNotEmpty(pdl)) {
				Anchor at = newTag(Anchor.class);
				at.setCssClass("p-uploader-icon"); 
				at.setHref(Strings.replace(pdl, "#", tag.getFileId().toString()));
				at.setIcon("icon-check");
				at.start(writer);
				at.end(writer, html(tag.getFileName()) + " " + filesize(ufs));
			}
			else {
				write("<span>");
				write(xicon("icon-check p-uploader-icon"));
				write(html(tag.getFileName()));
				write(" ");
				write(filesize(ufs));
				write("</span>");
			}
		}
		write("</span>");

		write("<div class=\"p-uploader-image\">");
		if (isImg && tag.isFileExits()) {
			if (Strings.isNotEmpty(pdl)) {
				write("<img class=\"img-thumbnail\" src=\"" + Strings.replace(pdl, "#", tag.getFileId().toString()) + "\"/>");
			}
		}
		write("</div>");
		
		etag("div");
	}

	private String filesize(Integer fs) {
		String sz = Files.toDisplaySize(fs);

		if (sz.length() > 0) {
			sz = '(' + sz + ')';
		}
		return sz;
	}
}
