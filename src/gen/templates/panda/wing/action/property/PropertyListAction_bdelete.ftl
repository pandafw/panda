<html>
<head>
	<title><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/panda/exts/struts2/views/action-alert.ftl"/>
	<br/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, {
		"name": "_check_",
		"type": "check",
		"nowrap": true,
		"fixed": true
	}{
		"name": "id",
		"pkey" : true,
		"header": action.getText("bdelete-column-id", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-id-tip", "")			
	}, {
		"name": "clazz",
		"header": action.getText("bdelete-column-clazz", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-clazz-tip", "")			
	}, {
		"name": "language",
		"header": action.getText("bdelete-column-language", ""), 
		"format": {
			"type": "code",
			"codemap": "consts.localeLanguageMap"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-language-tip", "")			
	}, {
		"name": "country",
		"header": action.getText("bdelete-column-country", ""), 
		"format": {
			"type": "code",
			"codemap": "consts.localeCountryMap"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-country-tip", "")			
	}, {
		"name": "name",
		"header": action.getText("bdelete-column-name", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-name-tip", "")			
	}, {
		"name": "value",
		"header": action.getText("bdelete-column-value", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-value-tip", "")			
	}, {
		"name": "memo",
		"header": action.getText("bdelete-column-memo", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-memo-tip", "")			
	}, {
		"name": "status",
		"header": action.getText("bdelete-column-status", ""), 
		"format": {
			"type": "code",
			"codemap": "consts.dataStatusMap"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-status-tip", "")			
	}, {
		"name": "uusid",
		"header": action.getText("bdelete-column-uusid", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-uusid-tip", "")			
	}, {
		"name": "utime",
		"header": action.getText("bdelete-column-utime", ""), 
		"format": {
			"type": "datetime"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("bdelete-column-utime-tip", "")			
	}] />

	<@p.listview id="property_bdelete"
		action="property_bdelete_execute" method="post"
		list="ds" columns=_columns_ cssColumn="status"
	>
	</@p.listview>
	
	<br/>
	<div class="p-tcenter">
		<@p.submit icon="icon-bdelete-execute" onclick="return property_bdelete_submit();" theme="simple"><@p.text name="button-bdelete-execute"/></@p.submit>
		<#if action.hasPermission("property_list")>
			<@p.url var="_u_" action='property_list'/>
			<@p.submit icon="icon-list" onclick="location.href='${_u_}';return false;" theme="simple"><@p.text name='button-list'/></@p.submit>
		</#if>

		<script type="text/javascript"><!--
			function property_bdelete_submit() {
				return nlv_submitCheckedKeys('property_bdelete');
			}
			
			$(function() {
				nlv_checkAll('property_bdelete');
			});
		--></script>
	</div>
</div>

</body>
</html>
