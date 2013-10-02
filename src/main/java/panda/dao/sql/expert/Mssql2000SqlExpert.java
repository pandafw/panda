package panda.dao.sql.expert;


public class Mssql2000SqlExpert extends Mssql2005SqlExpert {
	@Override
	public boolean isSupportPaginate() {
		return false;
	}
}
