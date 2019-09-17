package ${package}.tool;

import panda.app.AppHelper;
import panda.dao.Dao;
import ${package}.WebSetup;



/**
 */
public class CreateTables {
	/**
	 * main
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new CreateTables().execute();
	}

	public void execute() {
		AppConsole ac = AppConsole.i();
		try {
			Dao dao = ac.getDao();
			AppHelper.createTables(dao, WebSetup.ENTITIES);
		}
		finally {
			ac.destroy();
		}
	}
}
