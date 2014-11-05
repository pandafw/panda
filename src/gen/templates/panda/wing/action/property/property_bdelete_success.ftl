<html>
<head>
	<title><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<ul class="p-toolbar">
		<li><@p.a icon="icon-refresh" href="javascript:location.reload(true)"><@p.text name='button-refresh'/></@p.a></li>
	</ul>

	<#include "/panda/exts/struts2/views/action-alert.ftl"/>
	<br/>
	
	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, {
		"name": "id",
		"header": action.getText("bdelete-column-id", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-id-tip", ""),
		"value": false		
	}, {
		"name": "clazz",
		"header": action.getText("bdelete-column-clazz", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-clazz-tip", ""),
		"value": false		
	}, {
		"name": "language",
		"header": action.getText("bdelete-column-language", ""), 
		"format": {
			"type": "code",
			"codemap": "consts.localeLanguageMap"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-language-tip", ""),
		"value": false		
	}, {
		"name": "country",
		"header": action.getText("bdelete-column-country", ""), 
		"format": {
			"type": "code",
			"codemap": "consts.localeCountryMap"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-country-tip", ""),
		"value": false		
	}, {
		"name": "name",
		"header": action.getText("bdelete-column-name", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-name-tip", ""),
		"value": false		
	}, {
		"name": "value",
		"header": action.getText("bdelete-column-value", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-value-tip", ""),
		"value": false		
	}, {
		"name": "memo",
		"header": action.getText("bdelete-column-memo", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-memo-tip", ""),
		"value": false		
	}, {
		"name": "status",
		"header": action.getText("bdelete-column-status", ""), 
		"format": {
			"type": "code",
			"codemap": "consts.dataStatusMap"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-status-tip", ""),
		"value": false		
	}, {
		"name": "cusid",
		"header": action.getText("bdelete-column-cusid", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-cusid-tip", ""),
		"value": false		
	}, {
		"name": "ctime",
		"header": action.getText("bdelete-column-ctime", ""), 
		"format": {
			"type": "datetime"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-ctime-tip", ""),
		"value": false		
	}, {
		"name": "uusid",
		"header": action.getText("bdelete-column-uusid", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-uusid-tip", ""),
		"value": false		
	}, {
		"name": "utime",
		"header": action.getText("bdelete-column-utime", ""), 
		"format": {
			"type": "datetime"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-utime-tip", ""),
		"value": false		
	}] />

	<@p.listview id="property_bdelete"
		list="ds" columns=_columns_ cssColumn="status"
	>
	</@p.listview>
	
	<br/>
	<div class="p-tcenter">
		<#if action.hasPermission("property_list")>
			<@p.url var="_u_" action='property_list'/>
			<@p.submit icon="icon-list" onclick="location.href='${_u_}';return false;" theme="simple"><@p.text name='button-list'/></@p.submit>
		</#if>
	</div>
</div>

</body>
</html>
