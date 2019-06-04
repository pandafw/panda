package panda.gems.bundle.template;

import java.util.List;

import freemarker.cache.TemplateLoader;
import panda.app.constant.VAL;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.gems.bundle.template.entity.Template;
import panda.gems.bundle.template.entity.query.TemplateQuery;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.tpl.ftl.ExternalTemplateLoader;

@IocBean(type=TemplateLoader.class, create="initialize")
public class TemplateBundleLoader extends FreemarkerTemplateLoader {
	private static final Log log = Logs.getLog(TemplateBundleLoader.class);

	public static final String DATABASE_TEMPLATE = "database-template-load";

	@IocInject
	protected Settings settings;

	@IocInject
	private DaoClient daoClient;

	private ExternalTemplateLoader databaseTemplateLoader;
	
	/**
	 * @return the databaseTemplateLoader
	 */
	public ExternalTemplateLoader getDatabaseTemplateLoader() {
		return databaseTemplateLoader;
	}

	public void initialize() {
		if (settings.getPropertyAsBoolean(DATABASE_TEMPLATE)) {
			ExternalTemplateLoader etl = new ExternalTemplateLoader();
			
			etl.setNameColumn(Template.NAME);
			etl.setLocaleColumn(Template.LOCALE);
			etl.setSourceColumn(Template.SOURCE);
			etl.setTimestampColumn(Template.UPDATED_AT);
	
			databaseTemplateLoader = etl;
			
			addTemplateLoader(etl);
			
			reload();
		}
		super.initialize();
	}
	
	public void reload() {
		if (databaseTemplateLoader != null) {
			log.info("Loading database templates ...");
	
			Dao dao = daoClient.getDao();
	
			TemplateQuery tq = new TemplateQuery();
			tq.status().eq(VAL.STATUS_ACTIVE);
			List<Template> list = dao.select(tq);
	
			databaseTemplateLoader.loadTemplates(list);
		}
	}
}
