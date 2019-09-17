package ${package};

import panda.dao.Dao;
import ${package}.tool.AppConsole;


public abstract class AppTestCase {
	protected Dao getDao() {
		return AppConsole.i().getDao();
	}
}
