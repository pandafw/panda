package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import panda.bind.json.JsonObject;
import panda.doc.html.HTMLEntities;
import panda.io.Files;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.net.URLBuilder;

public class UploaderRenderer extends AbstractEndRenderer<Uploader> {
	public UploaderRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attr = new Attributes();
		
		String pul = tag.getUploadLink();
		String pun = tag.getUploadName();
		String pud = tag.getUploadData();
		String pdl = tag.getDnloadLink();
		String pdn = tag.getDnloadName();
		String pdd = tag.getDnloadData();
		String pel = tag.getDefaultLink();
		String pet = tag.getDefaultText();

		attr.id(tag)
			.cssClass("p-uploader")
			.tooltip(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag)
			.data("uploadLink", pul)
			.data("uploadName", pun)
			.data("uploadData", pud)
			.data("dnloadLink", pdl)
			.data("dnloadName", pdn)
			.data("dnloadData", pdd)
			.data("defaultLink", pel)
			.data("defaultText", pet)
			;
		
		if (!(tag.isReadonly() || tag.isDisabled())) {
			attr.data("spy", "puploader");
		}
		stag("div", attr);

		Attributes a = new Attributes();
		a.type("file").css(this, "p-uploader-file");
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
		
		boolean isImg = uct.startsWith("image/") || Strings.startsWith(tag.getAccept(), "image/");
		
		a = new Attributes();
		a.type("hidden")
		 .cssClass("p-uploader-fid")
		 .disabled(tag);
		if (name != null) {
			a.name(name);
			a.value(Strings.defaultString(tag.getFileId()));
		}
		xtag("input", a);

		String durl = null;
		
		write("<button class=\"btn btn-sm btn-default p-uploader-btn\">");
		write(html(getText("btn-select-file", "Browse...")));
		write("</button>");

		write("<span class=\"p-uploader-text\">");
		if (tag.isFileExits()) {
			if (Strings.isNotEmpty(pdl)) {
				Map<String, Object> ps = JsonObject.fromJson(pdd);
				if (ps == null) {
					ps = new HashMap<String, Object>();
				}
				ps.put(pdn, tag.getFileId());
				
				durl = URLBuilder.buildURL(pdl, ps, true);
				
				Anchor at = newTag(Anchor.class);
				at.setCssClass("p-uploader-icon");
				at.setHref(durl);
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
		else if (tag.isDefaultEnable() && Strings.isNotEmpty(pel)) {
			write("<a href=\"" + pel + "\">");
			write(xicon((isImg ? "icon-image" : "icon-attachment") + " p-uploader-icon"));
			write(" ");
			write(pet);
			write("</a>");
		}
		else {
			write(HTMLEntities.NBSP);
		}
		write("</span>");

		write("<div class=\"p-uploader-image\">");
		if (isImg) {
			if (Strings.isNotEmpty(durl)) {
				write("<img class=\"img-thumbnail\" src=\"" + durl + "\"/>");
			}
			else if (tag.isDefaultEnable() && Strings.isNotEmpty(pel)) {
				write("<img class=\"img-thumbnail\" src=\"" + pel + "\"/>");
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
