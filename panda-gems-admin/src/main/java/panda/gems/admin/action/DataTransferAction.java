package panda.gems.admin.action;


import java.util.Collection;
import java.util.Map;

import panda.app.action.work.GenericSyncWorkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.SET;
import panda.app.util.AppDaoClientFactory;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DaoIterator;
import panda.io.Settings;
import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/datacpy")
@Auth(AUTH.SUPER)
public class DataTransferAction extends GenericSyncWorkAction {
	public static class Arg {
		public String source;
		public String target;
	}

	@IocInject
	protected AppDaoClientFactory dcfactory;

	@IocInject
	protected Settings settings;
	
	protected Arg arg;

	protected Map<String, String> dataSettings;
	
	private Arg getArguments() {
		Arg arg = new Arg();
		arg.target = Strings.stripToNull(getRequest().getParameter("target"));
		return arg;
	}

	protected Collection<Class<?>> getEntities() {
		return getDaoClient().getEntities().keySet();
	}
	
	public Map<String, String> getDataSettings() {
		if (dataSettings == null) {
			String prefix = SET.DATA + '.';
			
			Map<String, String> dps = Collections.subMap(settings, prefix, false);
			dps.remove(SET.DATA_PREFIX);
			dps.remove(SET.DATA_QUERY_TIMEOUT);
			dps.remove(SET.DATA_SOURCE);
			
			dataSettings = dps;
		}
		
		return dataSettings;
	}
	
	@At("")
	@To(Views.SFTL)
	public void input() {
		getContext().setParams(getArguments());
	}

	@Override
	protected void doExecute() {
		final Arg arg = getArguments();
		
		if (Strings.isEmpty(arg.target)) {
			printError("Parameter [target] is required!");
			return;
		}
		
		DaoClient ddc = dcfactory.buildDaoClient(arg.target);
		DaoClient sdc = getDaoClient();
		if (Strings.isNotEmpty(arg.source)) {
			sdc = dcfactory.buildDaoClient(arg.source);
		}
		
		final Dao sdao = sdc.getDao();
		final Dao ddao = ddc.getDao();
		
		Collection<Class<?>> entities = getEntities();
		status.total = entities.size();
		for (final Class<?> c : entities) {
			printInfo("Copy table for " + c);
			
			final DaoIterator di = sdao.iterate(c);
			try {
				ddao.exec(new Runnable() {
					@Override
					public void run() {
						if (!ddao.exists(c)) {
							printInfo("Create table for " + c);
							ddao.create(c);
							ddao.commit();
						}

						int i = 0;
						while (di.hasNext()) {
							if (getStatus().stop) {
								return;
							}

							Object o = di.next();
							if (ddao.exists(o.getClass(), o)) {
								printDebug("Update [" + c.getSimpleName() + "]: " + o);
								ddao.update(o);
							}
							else {
								printDebug("Insert [" + c.getSimpleName() + "]: " + o);;
								ddao.insert(o);
								if (++i % 100 == 0) {
									ddao.commit();
								}
							}
						}
					}
				});
			}
			catch (DaoException e) {
				printError("Failed to copy table for " + c + Streams.EOL + getStackTrace(e));
			}
			finally {
				Streams.safeClose(di);
			}
			status.count++;
		}
	}
	
}
