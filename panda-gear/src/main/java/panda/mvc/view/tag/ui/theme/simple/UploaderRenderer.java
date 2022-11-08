package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.io.FileNames;
import panda.lang.Collections;
import panda.lang.Numbers;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.html.HTMLEntities;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.vfs.FileItem;

public class UploaderRenderer extends AbstractTagRenderer<Uploader> {
	public UploaderRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	public void renderStart() throws IOException {
		Attributes attr = new Attributes();

		String puu = tag.getUploadUrl();
		String pud = tag.getUploadData();
		String pdu = tag.getDnloadUrl();
		String pdh = tag.getDnloadHolder();

		attr.id(tag)
			.cssClass("ui-uploader")
			.tooltip(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag)
			.data("name", tag.getName())
			.data("uploadUrl", puu)
			.data("uploadName", tag.getUploadName())
			.data("uploadData", pud)
			.data("dnloadUrl", pdu)
			.data("dnloadHolder", pdh);

		if (!(tag.isReadonly() || tag.isDisabled())) {
			attr.data("spy", "uploader");
		}
		stag("div", attr);

		attr.clear()
			.type("file")
			.css(this, "ui-uploader-file p-hidden")
			.add("name", "")
			.add("value", "")
			.disabled(tag)
			.size(tag)
			.accept(tag)
			.multiple(tag)
			.tabindex(tag);
		xtag("input", attr);

		if (!(tag.isReadonly() || tag.isDisabled())) {
			write("<a class=\"btn btn-sm btn-default ui-uploader-btn\">");
			write(html(getText("btn-select-file", "Browse...")));
			write("</a>");
		}
		write("<div class=\"ui-uploader-sep\"></div>");

		if (Collections.isEmpty(tag.getFileItems())) {
			String pel = tag.getDefaultUrl();
			if (tag.isDefaultEnable() && Strings.isNotEmpty(pel)) {
				write("<div class=\"ui-uploader-item\">");

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
		} else {
			for (FileItem fi : tag.getFileItems()) {
				writeFileItem(fi, pdu, pdh);
			}
		}
	}

	@Override
	public void renderEnd() throws IOException {
		etag("div");
	}

	private void writeFileItem(FileItem fi, String pdu, String pdv) throws IOException {
		write("<div class=\"ui-uploader-item\">");

		Attributes a = new Attributes();
		a.type("hidden")
			.cssClass("ui-uploader-fid")
			.disabled(tag);
		if (tag.getName() != null) {
			a.name(tag.getName());
			a.value(Strings.defaultString(fi.getName()));
		}
		xtag("input", a);

		String durl = null;
		boolean isImg = Strings.startsWith(fi.getType(), "image/") || Strings.startsWith(tag.getAccept(), "image/");

		write("<span class=\"ui-uploader-text\">");
		if (fi.isExists()) {
			if (Strings.isNotEmpty(pdu)) {
				if (Strings.isEmpty(pdv)) {
					pdv = "$";
				}
				
				durl = Strings.replace(pdu, pdv, StringEscapes.escapeHTML(fi.getName()));
				writeLabel(durl, html(FileNames.getName(fi.getName())) + " " + filesize(fi.getSize()), isImg);
			} else {
				write("<span>");
				write(xicon("icon-check ui-uploader-icon"));
				write(html(FileNames.getName(fi.getName())));
				write(" ");
				write(filesize(fi.getSize()));
				write("</span>");
			}
		} else {
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
		write(xicon((isImg ? "icon-image" : "icon-attachment") + " ui-uploader-icon"));
		write(" ");
		write(text);
		write("</a>");
	}

	private void writeImage(String iu) throws IOException {
		write("<div class=\"ui-uploader-image\">");
		write("<a href=\"");
		write(iu);
		write("\"><img src=\"");
		write(iu);
		write("\"/></a>");
		write("</div>");
	}

	private String filesize(Integer fs) {
		String sz = Numbers.humanSize(fs);

		if (sz.length() > 0) {
			sz = '(' + sz + ')';
		}
		return sz;
	}
}
