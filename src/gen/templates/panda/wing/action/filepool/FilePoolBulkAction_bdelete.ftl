<html>
<head>
	<title><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/action-alert.ftl"/>
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
		"sortable": false,
		"tooltip": action.getText("bdelete-column-id-tip", "")			
	}, {
		"name": "name",
		"header": action.getText("bdelete-column-name", ""), 
		"sortable": false,
		"tooltip": action.getText("bdelete-column-name-tip", "")			
	}, {
		"name": "size",
		"header": action.getText("bdelete-column-size", ""), 
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": action.getText("bdelete-column-size-tip", "")			
	}, {
		"name": "date",
		"header": action.getText("bdelete-column-date", ""), 
		"format": {
			"type": "timestamp"
		},
		"sortable": false,
		"tooltip": action.getText("bdelete-column-date-tip", "")			
	}, {
		"name": "flag",
		"header": action.getText("bdelete-column-flag", ""), 
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": action.getText("bdelete-column-flag-tip", "")			
	}] />

	<@p.listview id="filepool_bdelete"
		action="filepool_bdelete_execute" method="post"
		list="ds" columns=_columns_ cssColumn="status"
	>
	</@p.listview>
	
	<br/>
	<div class="p-tcenter">
		<@p.submit icon="icon-bdelete-execute" onclick="return filepool_bdelete_submit();" theme="simple"><@p.text name="button-bdelete-execute"/></@p.submit>

		<script type="text/javascript"><!--
			function filepool_bdelete_submit() {
				return nlv_submitCheckedKeys('filepool_bdelete');
			}
			
			$(function() {
				nlv_checkAll('filepool_bdelete');
			});
		--></script>
	</div>
</div>

</body>
</html>
