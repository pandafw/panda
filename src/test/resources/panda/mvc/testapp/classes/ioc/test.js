{
	"javax.sql.DataSource" : {
		type : "panda.dao.sql.SimpleDataSource",
		events : {
				depose : 'close'
		},
		args : [{
			jdbc: {
				driver : 'org.hsqldb.jdbcDriver',
				url : 'jdbc:hsqldb:mem:mvc',
				username : 'sa',
				password : 'sa'
			}
		}]
	}
}
