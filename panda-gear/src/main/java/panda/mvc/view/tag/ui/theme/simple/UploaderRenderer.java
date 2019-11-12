package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import panda.bind.json.JsonObject;
import panda.io.FileNames;
import panda.lang.Collections;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.html.HTMLEntities;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.net.URLBuilder;
import panda.vfs.FileItem;

public class UploaderRenderer extends AbstractTagRenderer<Uploader> {
	public UploaderRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	public void renderStart() throws IOException {
		Attributes attr = new Attributes();
		
		String pul = tag.getUploadLink();
		String pun = tag.getUploadName();
		String pud = tag.getUploadData();
		String pdl = tag.getDnloadLink();
		String pdn = tag.getDnloadName();
		String pdd = tag.getDnloadData();

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
			;
		
		if (!(tag.isReadonly() || tag.isDisabled())) {
			attr.data("spy", "puploader");
		}
		stag("div", attr);

		attr.clear()
			.type("file")
			.css(this, "p-uploader-file p-hidden")
			.add("name", "")
			.add("value", "")
			.disabled(tag)
			.size(tag)
			.accept(tag)
			.multiple(tag)
			.tabindex(tag);
		xtag("input", attr);

		if (!(tag.isReadonly() || tag.isDisabled())) {
			write("<a class=\"btn btn-sm btn-default p-uploader-btn\">");
			write(html(getText("btn-select-file", "Browse...")));
			write("</a>");
		}
		write("<div class=\"p-uploader-sep\"></div>");
		
		if (Collections.isEmpty(tag.getFileItems())) {
			String pel = tag.getDefaultLink();
			if (tag.isDefaultEnable() && Strings.isNotEmpty(pel)) {
				write("<div class=\"p-uploader-item\">");
				
				boolean bImg = Strings.startsWith(tag.getAccept(), "image/");
				String pet = tag.getDefaultText();
				if (!bImg && Strings.isEmpty(pet)) {
					pet = context.getText().getText("uploader-text-download", "");
				}

				if (Strings.isNotEmpty(pet)) {
					writeLabel(pel, pet, bImg);
				}
				if (bImg) {
					writeImage(pel);
				}
				write("</div>");
			}
		}
		else {
			for (FileItem fi : tag.getFileItems()) {
				writeFileItem(fi, pdn, pdl, pdd);
			}
		}
	}
	
	@Override
	public void renderEnd() throws IOException {
		etag("div");
	}
	
	private void writeFileItem(FileItem fi, String pdn, String pdl, String pdd) throws IOException {
		write("<div class=\"p-uploader-item\">");
		
		Attributes a = new Attributes();
		a.type("hidden")
		 .cssClass("p-uploader-fid")
		 .disabled(tag);
		if (tag.getName() != null) {
			a.name(tag.getName());
			a.value(Strings.defaultString(fi.getName()));
		}
		xtag("input", a);

		String durl = null;
		boolean isImg = Strings.startsWith(fi.getType(), "image/") || Strings.startsWith(tag.getAccept(), "image/");

		write("<span class=\"p-uploader-text\">");
		if (fi.isExists()) {
			if (Strings.isNotEmpty(pdl)) {
				Map<String, Object> ps = JsonObject.fromJson(pdd);
				if (ps == null) {
					ps = new HashMap<String, Object>();
				}
				ps.put(pdn, fi.getName());
				
				durl = URLBuilder.buildURL(pdl, ps, true);
				
				writeLabel(durl, html(FileNames.getName(fi.getName())) + " " + filesize(fi.getSize()), isImg);
			}
			else {
				write("<span>");
				write(xicon("icon-check p-uploader-icon"));
				write(html(FileNames.getName(fi.getName())));
				write(" ");
				write(filesize(fi.getSize()));
				write("</span>");
			}
		}
		else {
			write(HTMLEntities.NBSP);
		}
		write("</span>");

		if (isImg && Strings.isNotEmpty(durl)) {
			writeImage(durl);
		}
		write("</div>");
	}
	
	private void writeLabel(String link, String text, boolean isImg) throws IOException {
		write("<a href=\"");
		write(link);
		write("\">");
		write(xicon((isImg ? "icon-image" : "icon-attachment") + " p-uploader-icon"));
		write(" ");
		write(text);
		write("</a>");
	}

	private void writeImage(String iu) throws IOException {
		write("<div class=\"p-uploader-image\">");
		write("<a href=\"");
		write(iu);
		write("\"><img class=\"img-thumbnail\" src=\"");
		write(iu);
		write("\"/></a>");
		write("</div>");
	}
	
	private String filesize(Integer fs) {
		String sz = Numbers.formatSize(fs);

		if (sz.length() > 0) {
			sz = '(' + sz + ')';
		}
		return sz;
	}
}
