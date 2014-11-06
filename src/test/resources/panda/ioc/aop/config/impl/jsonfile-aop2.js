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
			aopItemList : [
				{ name: '.+', method: 'toString', interceptor: '$log' },
				{ name: '.+', method: '.+', interceptor: '$myMI' },
				{ name: 'com\\.service\\..+', method: '.+', interceptor: '$log' },
				{ name: 'com\\.service\\.auth\\..+', method: '.+', interceptor: '$txSERIALIZABLE' },
				{ name: 'com\\.service\\.blog\\..+', method: '(get|save|update|delete).+', interceptor: '$txREPEATABLE_READ' },
				{ name: 'com\\.service\\.news\\..+', method: '(get|set).+', interceptor: '$txREAD_COMMITTED' },
				{ name: 'com\\.service\\.media\\..+', method: '(get|set).+', interceptor: '$txREAD_UNCOMMITTED' },
				{ name: 'com\\.service\\.status\\..+', method: '(get|set).+', interceptor: '$txNONE' }
			]
		}
	}
}