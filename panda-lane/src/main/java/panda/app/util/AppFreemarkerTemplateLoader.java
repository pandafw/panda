package panda.app.util;

import java.util.List;

import panda.app.constant.MVC;
import panda.app.constant.VAL;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.tpl.ftl.ExternalTemplateLoader;
import panda.app.entity.Template;
import panda.app.entity.query.TemplateQuery;

import freemarker.cache.TemplateLoader;

@IocBean(type=TemplateLoader.class, create="initialize")
public class AppFreemarkerTemplateLoader extends FreemarkerTemplateLoader {
	private static final Log log = Logs.getLog(AppFreemarkerTemplateLoader.class);

	public AppFreemarkerTemplateLoader() {
	}

	@IocInject(value=MVC.DATABASE_TEMPLATE, required=false)
	private boolean enable;

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
		if (enable) {
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
			tq.status().equalTo(VAL.STATUS_ACTIVE);
			List<Template> list = dao.select(tq);
	
			databaseTemplateLoader.loadTemplates(list);
		}
	}
}
