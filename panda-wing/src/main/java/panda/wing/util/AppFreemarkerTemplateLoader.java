package panda.wing.util;

import java.util.List;

import freemarker.cache.TemplateLoader;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.tpl.ftl.ExternalTemplateLoader;
import panda.wing.constant.SC;
import panda.wing.constant.VC;
import panda.wing.entity.Template;
import panda.wing.entity.query.TemplateQuery;

@IocBean(type=TemplateLoader.class, create="initialize")
public class AppFreemarkerTemplateLoader extends FreemarkerTemplateLoader {
	private static final Log log = Logs.getLog(AppFreemarkerTemplateLoader.class);

	public AppFreemarkerTemplateLoader() {
	}

	@IocInject
	private Settings settings;

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
		if (settings.getPropertyAsBoolean(SC.DATABASE_TEMPLATE)) {
			ExternalTemplateLoader etl = new ExternalTemplateLoader();
			
			etl.setNameColumn(Template.NAME);
			etl.setLanguageColumn(Template.LANGUAGE);
			etl.setCountryColumn(Template.COUNTRY);
			//dtl.setVariantColumn("variant");
			etl.setSourceColumn(Template.SOURCE);
			etl.setTimestampColumn(Template.UTIME);
	
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
			tq.status().equalTo(VC.STATUS_ACTIVE);
			List<Template> list = dao.select(tq);
	
			databaseTemplateLoader.loadTemplates(list);
		}
	}
}
