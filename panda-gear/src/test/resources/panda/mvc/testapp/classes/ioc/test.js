{
	// default resources
	"panda.resources.default": [ "panda.mvc.Resource" ],

	// allowed locales
	"panda.locale.allowed": [ "en" ],

	// default locale
	"panda.locale.default": "en",

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
