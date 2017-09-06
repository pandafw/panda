package panda.dao.sql.expert;

import panda.dao.DB;
import panda.dao.entity.Entity;
import panda.lang.Strings;

public class MariadbSqlExpert extends MysqlSqlExpert {
	@Override
	public DB getDatabaseType() {
		return DB.MARIADB;
	}

	protected String getTableOption(Entity<?> entity, String name, String defv) {
		String v = getEntityOptionString(entity, "mariadb-" + name, defv);
		if (Strings.isEmpty(v)) {
			return super.getTableOption(entity, name, defv);
		}
		return v;
	}
}
