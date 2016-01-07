	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<div class="p-toolbar-wrap"><ul class="p-toolbar">
		<li><@p.a icon="icon-refresh" href="javascript:location.reload(true)"><@p.text name='button-refresh'/></@p.a>
</li>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>
	<br/>
	
	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, {
		"name": "id",
		"header": action.getText("bdelete-column-id", ""), 
		"sortable": false,
		"tooltip": action.getText("bdelete-column-id-tip", ""),
		"value": false		
	}, {
		"name": "name",
		"header": action.getText("bdelete-column-name", ""), 
		"sortable": false,
		"tooltip": action.getText("bdelete-column-name-tip", ""),
		"value": false		
	}, {
		"name": "size",
		"header": action.getText("bdelete-column-size", ""), 
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": action.getText("bdelete-column-size-tip", ""),
		"value": false		
	}, {
		"name": "date",
		"header": action.getText("bdelete-column-date", ""), 
		"format": {
			"type": "timestamp"
		},
		"sortable": false,
		"tooltip": action.getText("bdelete-column-date-tip", ""),
		"value": false		
	}, {
		"name": "flag",
		"header": action.getText("bdelete-column-flag", ""), 
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": action.getText("bdelete-column-flag-tip", ""),
		"value": false		
	}] />

	<@p.listview id="filepool_bdelete"
		list="ds" columns=_columns_ cssColumn="status"
	>
	</@p.listview>
	
	<br/>
	<div class="p-tcenter">
		<#if action.hasPermission("filepool_${list.butons}")>
			<@p.url var="_u_" action='filepool_${list.butons}'/>
			<@p.submit icon="icon-${list.butons}" onclick="location.href='${_u_}';return false;" theme="simple"><@p.text name='button-${list.butons}'/></@p.submit>
		</#if>
	</div>
</div>

</body>
</html>
