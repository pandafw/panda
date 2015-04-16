package panda.wing.action.tool;

import java.util.List;

import panda.ioc.annotation.IocInject;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;
import panda.wing.AppConstants;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.task.CronEntry;

@At("/admin/crons")
@Auth(AUTH.SYSADMIN)
public class CronsAction extends AbstractAction {
	@IocInject(value=AppConstants.CRONS, required=false)
	private List<CronEntry> crons;
	
	/**
	 * execute
	 * 
	 * @return SUCCESS
	 * @throws Exception if an error occurs
	 */
	@At("")
	@Ok(View.FREEMARKER)
	public List<CronEntry> execute() throws Exception {
		return crons;
	}
}
