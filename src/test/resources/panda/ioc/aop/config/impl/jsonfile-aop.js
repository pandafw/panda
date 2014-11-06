{
	log : {
		type :'panda.aop.interceptor.LoggingMethodInterceptor'
	},
	myMI : {
		type : 'panda.ioc.aop.config.impl.MyMI'
	},
	pet2 : {
		type : "panda.ioc.aop.config.impl.Pet2"
	},

	$aop : {
		type : 'panda.ioc.aop.config.impl.JsonAopConfigration',
		fields : {
			itemList : [
				['.+',                'toString',    '$log'],
				['.+',    '.+', '$myMI'],
				['com\\.service\\..+','.+','$log'],
				['com\\.service\\.auth\\..+','.+','$txSERIALIZABLE'],
				['com\\.service\\.blog\\..+','(get|save|update|delete).+','$txREPEATABLE_READ'],
				['com\\.service\\.news\\..+','(get|set).+','$txREAD_COMMITTED'],
				['com\\.service\\.media\\..+','(get|set).+','$txREAD_UNCOMMITTED'],
				['com\\.service\\.status\\..+','(get|set).+','$txNONE']
			]
		}
	}
}