package panda.mvc.testapp.classes.bean;

import java.sql.SQLException;

import javax.sql.DataSource;

import panda.dao.DaoClient;
import panda.dao.sql.SqlDaoClient;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean(type=DaoClient.class)
public class TestDaoClient extends SqlDaoClient {

	@Override
	@IocInject
	public void setDataSource(DataSource dataSource) throws SQLException {
		super.setDataSource(dataSource);
	}

}
