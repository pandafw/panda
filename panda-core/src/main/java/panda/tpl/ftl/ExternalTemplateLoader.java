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
	protected String languageColumn;
	protected String countryColumn;
	protected String variantColumn;
	protected String sourceColumn;
	protected String timestampColumn;
	protected String emptyString = "_";
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
	 * @return the languageColumn
	 */
	public String getLanguageColumn() {
		return languageColumn;
	}

	/**
	 * @param languageColumn the languageColumn to set
	 */
	public void setLanguageColumn(String languageColumn) {
		this.languageColumn = languageColumn;
	}

	/**
	 * @return the countryColumn
	 */
	public String getCountryColumn() {
		return countryColumn;
	}

	/**
	 * @param countryColumn the countryColumn to set
	 */
	public void setCountryColumn(String countryColumn) {
		this.countryColumn = countryColumn;
	}

	/**
	 * @return the variantColumn
	 */
	public String getVariantColumn() {
		return variantColumn;
	}

	/**
	 * @param variantColumn the variantColumn to set
	 */
	public void setVariantColumn(String variantColumn) {
		this.variantColumn = variantColumn;
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
			String language = null;
			String country = null;
			String variant = null;
			
			name = (String)bh.getPropertyValue(o, nameColumn);
			if (Strings.isNotEmpty(languageColumn)) {
				language = (String)bh.getPropertyValue(o, languageColumn);
			}
			if (Strings.isNotEmpty(countryColumn)) {
				country = (String)bh.getPropertyValue(o, countryColumn);
			}
			if (Strings.isNotEmpty(variantColumn)) {
				variant = (String)bh.getPropertyValue(o, variantColumn);
			}

			String templateName = buildTemplateName(name, language, country, variant); 
			String templateSource = (String)bh.getPropertyValue(o, sourceColumn);
			templates.put(name, new StringTemplateSource(name, templateSource, lastModified));

			if (log.isDebugEnabled()) {
				log.debug("load template - " + templateName);
			}
		}
		setTemplates(templates);
	}

	private void appendLocalePart(StringBuilder name, String v) {
		if (emptyString != null && emptyString.equals(v)) {
			v = null;
		}
		if (Strings.isNotEmpty(v)) {
			name.append('_').append(v);
		}
	}

	/**
	 * @param name name
	 * @param language language
	 * @param country country
	 * @param variant variant
	 * @return template name
	 */
	protected String buildTemplateName(String name, String language, String country, String variant) {
		StringBuilder templateName = new StringBuilder();
		
		templateName.append(name);
		appendLocalePart(templateName, language);
		appendLocalePart(templateName, country);
		appendLocalePart(templateName, variant);
		
		if (Strings.isNotEmpty(extension)) {
			templateName.append(extension);
		}

		return templateName.toString();
	}

	/**
	 * Puts a template into the loader. A call to this method is identical to the call to the
	 * {@link #putTemplate(String, String, String, String, String, long)} passing
	 * <tt>System.currentTimeMillis()</tt> as the third argument.
	 * 
	 * @param name the name of the template.
	 * @param language language
	 * @param country country
	 * @param variant variant
	 * @param templateSource the source code of the template.
	 */
	public void putTemplate(String name, String language, String country, String variant, String templateSource) {
		putTemplate(name, language, country, variant, templateSource, System.currentTimeMillis());
	}

	/**
	 * Puts a template into the loader. A call to this method is identical to the call to the
	 * {@link #putTemplate(String, String, String, String, String, long)} passing
	 * <tt>System.currentTimeMillis()</tt> as the third argument.
	 * 
	 * @param name the name of the template.
	 * @param language language
	 * @param country country
	 * @param variant variant
	 * @param templateSource the source code of the template.
	 * @param lastModified the time of last modification of the template in terms of
	 *            <tt>System.currentTimeMillis()</tt>
	 */
	public void putTemplate(String name, String language, String country, String variant, String templateSource,
			long lastModified) {
		putTemplate(buildTemplateName(name, language, country, variant), templateSource, lastModified);
	}
}
