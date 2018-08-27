package panda.app.action.tool;


import panda.app.action.work.GenericSyncWorkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.MVC;
import panda.app.util.AppDaoClientFactory;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DaoIterator;
import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.validator.Validators;
import panda.mvc.view.Views;

@At("${super_path}/datacpy")
@Auth(AUTH.SUPER)
public class DataTransferAction extends GenericSyncWorkAction {
	public static class Arg {
		public String target;
	}

	@IocInject(MVC.DATA_ENTITIES)
	protected Class<?>[] ENTITIES;
	
	@IocInject
	protected AppDaoClientFactory dcfactory;
	
	protected Arg arg;
	
	@At("")
	@To(Views.SFTL)
	public void input() {
		getContext().setParams(getArguments());
	}

	private Arg getArguments() {
		Arg arg = new Arg();
		arg.target = Strings.stripToNull(getRequest().getParameter("target"));
		return arg;
	}

	@Override
	protected void doExecute() {
		final Arg arg = getArguments();
		
		if (Strings.isEmpty(arg.target)) {
			printError("Parameter [target] is required!");
			addFieldError("target", getText(Validators.MSGID_REQUIRED));
			return;
		}
		
		final DaoClient ddc = dcfactory.buildDaoClient(arg.target);
		final Dao sdao = getDaoClient().getDao();
		final Dao ddao = ddc.getDao();
		
		status.total = ENTITIES.length;
		for (final Class<?> c : ENTITIES) {
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
				printError("Failed to copy table for " + c + Streams.LINE_SEPARATOR + getStackTrace(e));
			}
			finally {
				Streams.safeClose(di);
			}
			status.count++;
		}
	}
	
}
