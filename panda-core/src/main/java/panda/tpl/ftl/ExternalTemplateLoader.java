package panda.tpl.ftl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * load template from external
 */
public class ExternalTemplateLoader extends StringTemplateLoader {
	private static final Log log = Logs.getLog(ExternalTemplateLoader.class);

	protected String nameColumn;
	protected String localeColumn;
	protected String sourceColumn;
	protected String timestampColumn;
	protected String emptyString = "*";
	protected String extension = ".ftl";

	/**
	 * @return the nameColumn
	 */
	public String getNameColumn() {
		return nameColumn;
	}

	/**
	 * @param nameColumn the nameColumn to set
	 */
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	/**
	 * @return the localeColumn
	 */
	public String getLocaleColumn() {
		return localeColumn;
	}

	/**
	 * @param localeColumn the localeColumn to set
	 */
	public void setLocaleColumn(String localeColumn) {
		this.localeColumn = localeColumn;
	}

	/**
	 * @return the sourceColumn
	 */
	public String getSourceColumn() {
		return sourceColumn;
	}

	/**
	 * @param sourceColumn the sourceColumn to set
	 */
	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	/**
	 * @param timestampColumn the timestampColumn to set
	 */
	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the emptyString
	 */
	public String getEmptyString() {
		return emptyString;
	}

	/**
	 * @param emptyString the emptyString to set
	 */
	public void setEmptyString(String emptyString) {
		this.emptyString = emptyString;
	}

	/**
	 * Load templates from list.
	 * @param tplList the template list
	 */
	@SuppressWarnings("unchecked")
	public void loadTemplates(List tplList) {
		Map<String, StringTemplateSource> templates = newTemplates();

		for (Object o : tplList) {
			BeanHandler bh = Beans.i().getBeanHandler(o.getClass());
			long lastModified = System.currentTimeMillis();
			if (Strings.isNotEmpty(timestampColumn)) {
				Date v = (Date)bh.getPropertyValue(o, timestampColumn);
				if (v != null) {
					lastModified = v.getTime();
				}
			}

			String name = null;
			String locale = null;
			
			name = (String)bh.getPropertyValue(o, nameColumn);
			if (Strings.isNotEmpty(localeColumn)) {
				locale = (String)bh.getPropertyValue(o, localeColumn);
			}

			String templateName = buildTemplateName(name, locale); 
			String templateSource = (String)bh.getPropertyValue(o, sourceColumn);
			templates.put(name, new StringTemplateSource(name, templateSource, lastModified));

			if (log.isDebugEnabled()) {
				log.debug("load template - " + templateName);
			}
		}
		setTemplates(templates);
	}

	/**
	 * @param name name
	 * @param locale language
	 * @return template name
	 */
	protected String buildTemplateName(String name, String locale) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name);

		if (Strings.isNotEmpty(locale) && !locale.equals(emptyString)) {
			sb.append('_').append(locale);
		}
		
		if (Strings.isNotEmpty(extension)) {
			sb.append(extension);
		}

		return sb.toString();
	}

	/**
	 * Puts a template into the loader. A call to this method is identical to the call to the
	 * {@link #putTemplate(String, String, String, long)} passing
	 * <tt>System.currentTimeMillis()</tt> as the fourth argument.
	 * 
	 * @param name the name of the template.
	 * @param locale locale
	 * @param templateSource the source code of the template.
	 */
	public void putTemplate(String name, String locale, String templateSource) {
		putTemplate(name, locale, templateSource, System.currentTimeMillis());
	}

	/**
	 * Puts a template into the loader. A call to this method is identical to the call to the
	 * {@link #putTemplate(String, String, long)} passing
	 * <tt>System.currentTimeMillis()</tt> as the third argument.
	 * 
	 * @param name the name of the template.
	 * @param locale locale
	 * @param templateSource the source code of the template.
	 * @param lastModified the time of last modification of the template in terms of
	 *            <tt>System.currentTimeMillis()</tt>
	 */
	public void putTemplate(String name, String locale, String templateSource, long lastModified) {
		putTemplate(buildTemplateName(name, locale), templateSource, lastModified);
	}
}
