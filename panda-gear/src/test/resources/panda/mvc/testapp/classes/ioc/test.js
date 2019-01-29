{
	// default resources
	"panda.mvc.resources": [ "panda.mvc.Resource" ],

	// allowed locales
	"panda.locale.allowed": [ "en" ],

	// default locale
	"panda.locale.default": "en",

	"javax.sql.DataSource" : {
		type : "panda.dao.sql.dbcp.SimpleDataSource",
		events : {
			depose : 'close'
		},
		fields : {
			jdbc: {
				driver : 'org.hsqldb.jdbcDriver',
				url : 'jdbc:hsqldb:mem:mvc',
				username : 'sa',
				password : 'sa'
			}
		}
	}
}
