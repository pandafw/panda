package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import panda.bind.json.JsonObject;
import panda.doc.html.HTMLEntities;
import panda.io.Files;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.net.URLBuilder;
import panda.vfs.FileItem;

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
			.data("name", tag.getName())
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
		 .multiple(tag)
		 .tabindex(tag);
		xtag("input", a);

		if (!(tag.isReadonly() || tag.isDisabled())) {
			write("<button class=\"btn btn-sm btn-default p-uploader-btn\">");
			write(html(getText("btn-select-file", "Browse...")));
			write("</button>");
		}
		write("<div class=\"p-uploader-sep\"></div>");
		
		if (Collections.isEmpty(tag.getFileItems())) {
			if (tag.isDefaultEnable() && Strings.isNotEmpty(pel)) {
				write("<div class=\"p-uploader-item\">");
				write("<a href=\"");
				write(pel);
				write("\">");
				write(xicon((Strings.startsWith(tag.getAccept(), "image/") ? "icon-image" : "icon-attachment") + " p-uploader-icon"));
				write(" ");
				write(pet);
				write("</a>");
				write("</div>");
			}
		}
		else {
			for (FileItem fi : tag.getFileItems()) {
				writeFileItem(fi, pdn, pdl, pdd, pel);
			}
		}
		
		etag("div");
	}
	
	private void writeFileItem(FileItem fi, String pdn, String pdl, String pdd, String pel) throws IOException {
		write("<div class=\"p-uploader-item\">");
		
		Attributes a = new Attributes();
		a.type("hidden")
		 .cssClass("p-uploader-fid")
		 .disabled(tag);
		if (tag.getName() != null) {
			a.name(tag.getName());
			a.value(Strings.defaultString(fi.getId()));
		}
		xtag("input", a);

		String durl = null;
		boolean isImg = Strings.startsWith(fi.getContentType(), "image/") || Strings.startsWith(tag.getAccept(), "image/");

		write("<span class=\"p-uploader-text\">");
		if (fi.isExists()) {
			if (Strings.isNotEmpty(pdl)) {
				Map<String, Object> ps = JsonObject.fromJson(pdd);
				if (ps == null) {
					ps = new HashMap<String, Object>();
				}
				ps.put(pdn, fi.getId());
				
				durl = URLBuilder.buildURL(pdl, ps, true);
				
				Anchor at = newTag(Anchor.class);
				at.setCssClass("p-uploader-icon");
				at.setHref(durl);
				at.setIcon("icon-check");
				at.start(writer);
				at.end(writer, html(fi.getName()) + " " + filesize(fi.getSize()));
			}
			else {
				write("<span>");
				write(xicon("icon-check p-uploader-icon"));
				write(html(fi.getName()));
				write(" ");
				write(filesize(fi.getSize()));
				write("</span>");
			}
		}
		else {
			write(HTMLEntities.NBSP);
		}
		write("</span>");

		write("<div class=\"p-uploader-image\">");
		if (isImg) {
			String iu = null;
			if (Strings.isNotEmpty(durl)) {
				iu = durl;
			}
			else if (tag.isDefaultEnable() && Strings.isNotEmpty(pel)) {
				iu = pel;
			}
			write("<a href=\"");
			write(iu);
			write("\"><img class=\"img-thumbnail\" src=\"");
			write(iu);
			write("\"/></a>");
		}
		write("</div>");
		write("</div>");
	}
	
	private String filesize(Integer fs) {
		String sz = Files.toDisplaySize(fs);

		if (sz.length() > 0) {
			sz = '(' + sz + ')';
		}
		return sz;
	}
}
