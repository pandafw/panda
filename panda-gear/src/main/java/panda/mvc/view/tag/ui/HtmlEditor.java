package panda.mvc.view.tag.ui;

import java.util.Locale;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.SetConstants;
import panda.mvc.util.MvcURLBuilder;


/**
 * <!-- START SNIPPET: javadoc --> Render HTML textarea tag.</p> <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;p:htmleditor label=&quot;Comments&quot; name=&quot;comments&quot; cols=&quot;30&quot; rows=&quot;8&quot;/&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class HtmlEditor extends TextArea {
	protected String editor;
	protected Locale locale;
	protected Boolean cdn;
	protected Boolean debug;
	
	protected MvcURLBuilder mediaUrl;

	protected String mediaHref;
	protected String mediaIcon;
	protected String mediaText;

	/**
	 * @param urlbuilder the urlbuilder to set
	 */
	@IocInject
	protected void setMediaUrl(MvcURLBuilder urlbuilder) {
		this.mediaUrl = urlbuilder;
		urlbuilder.setEscapeAmp(true);
	}

	/**
	 * Evaluate extra parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		if (locale == null) {
			locale = context.getLocale();
		}
		if (cdn == null) {
			cdn = Mvcs.isUseCdn(context);
		}
		if (debug == null) {
			debug = context.isAppDebug();
		}
		if (Strings.isEmpty(editor)) {
			editor = context.getSettings().getProperty(SetConstants.MVC_TAG_HTML_EDITOR, "summernote");
		}

		if (mediaHref == null) {
			mediaHref = mediaUrl.build();
		}

		if (Strings.isEmpty(mediaIcon)) {
			mediaIcon = context.getText().getText("icon-media", "fa fa-list-alt");
		}
		
		if (Strings.isEmpty(mediaText)) {
			mediaText = context.getText().getText("btn-media", "Media");
		}
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * @return the cdn
	 */
	public boolean useCdn() {
		return cdn;
	}

	/**
	 * @return the cdn
	 */
	public Boolean getCdn() {
		return cdn;
	}

	/**
	 * @param cdn the cdn to set
	 */
	public void setCdn(Boolean cdn) {
		this.cdn = cdn;
	}

	/**
	 * @return the debug
	 */
	public boolean useDebug() {
		return debug;
	}

	/**
	 * @return the debug
	 */
	public Boolean getDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public void setMediaAction(String mediaAction) {
		this.mediaUrl.setAction(mediaAction);
	}

	public String getMediaHref() {
		return mediaHref;
	}

	public void setMediaHref(String mediaHref) {
		this.mediaHref = mediaHref;
	}

	public String getMediaIcon() {
		return mediaIcon;
	}

	public void setMediaIcon(String mediaIcon) {
		this.mediaIcon = mediaIcon;
	}

	public String getMediaText() {
		return mediaText;
	}

	public void setMediaText(String mediaText) {
		this.mediaText = mediaText;
	}
	
}
