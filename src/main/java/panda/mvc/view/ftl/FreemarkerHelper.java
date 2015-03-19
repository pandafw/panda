package panda.mvc.view.ftl;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import panda.io.stream.StringBuilderWriter;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;

@IocBean(scope=Scope.REQUEST)
public class FreemarkerHelper {
	@IocInject
	protected FreemarkerManager fm;

	@IocInject
	protected ActionContext ac;

	/**
	 * @param name template name
	 * @return template
	 * @throws IOException 
	 */
	public Template getTemplate(String name) throws IOException {
		return fm.getConfiguration().getTemplate(name, ac.getLocale());
	}

	/**
	 * hasTemplate
	 * @param name template name
	 * @return true if template exists
	 */
	public boolean hasTemplate(String name) {
		try {
			getTemplate(name);
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	/**
	 * process template
	 * @param name template name
	 * @param writer writer
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public void execTemplate(Writer writer, String name) throws TemplateException, IOException {
		execTemplate(writer, name, null);
	}
	
	/**
	 * process template
	 * @param name template name
	 * @param model model
	 * @param writer writer
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public void execTemplate(Writer writer, String name, Object model) throws TemplateException, IOException {
		Template template = getTemplate(name);
		execTemplate(writer, template, model);
	}
	
	/**
	 * process template
	 * @param name template name
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String execTemplate(String name) throws TemplateException, IOException {
		return execTemplate(name, null);
	}

	/**
	 * process template
	 * @param name template name
	 * @param model model
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String execTemplate(String name, Object model) throws TemplateException, IOException {
		StringBuilderWriter sw = new StringBuilderWriter();
		execTemplate(sw, name, model);
		return sw.toString();
	}

	/**
	 * process template
	 * @param template template
	 * @param model model
	 * @param writer writer
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public void execTemplate(Writer writer, Template template, Object model) throws TemplateException, IOException {
		ActionHash hash = fm.buildTemplateModel(ac);
		hash.setModel(model);
		template.process(hash, writer);
	}
	
	/**
	 * process template
	 * @param template template
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String execTemplate(Template template) throws TemplateException, IOException {
		return execTemplate(template, null);
	}

	/**
	 * process template
	 * @param template template
	 * @param model model
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String execTemplate(Template template, Object model) throws TemplateException, IOException {
		StringBuilderWriter sw = new StringBuilderWriter();
		execTemplate(sw, template, model);
		return sw.toString();
	}

	/**
	 * process template
	 * @param string template string
	 * @param writer writer
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public void evalTemplate(Writer writer, String string) throws TemplateException, IOException {
		evalTemplate(writer, string, null);
	}
	
	/**
	 * process template
	 * @param string template string
	 * @param model model
	 * @param writer writer
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public void evalTemplate(Writer writer, String string, Object model) throws TemplateException, IOException {
		Template template = new Template("string", new StringReader(string), fm.getConfiguration());
		execTemplate(writer, template, model);
	}
	
	/**
	 * process template
	 * @param string template string
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String evalTemplate(String string) throws TemplateException, IOException {
		return evalTemplate(string, null);
	}

	/**
	 * process template
	 * @param string template string
	 * @param model model
	 * @return result
	 * @throws TemplateException if a template error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public String evalTemplate(String string, Object model) throws TemplateException, IOException {
		StringBuilderWriter sw = new StringBuilderWriter();
		evalTemplate(sw, string, model);
		return sw.toString();
	}
}
