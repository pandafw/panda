<html>
<head>
	<title><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-bdelete"/></li>
		</ol>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>

<#if result?has_content>
	<#assign _columns_ = [{
		"name": "_rownum_",
		"type": "rownum",
		"header": text.getText("listview-th-rownum", ""),
		"fixed": true
	}, {
		"name": "_check_",
		"type": "check",
		"fixed": true
	}{
		"name": "id",
		"pkey" : true,
		"value": true,
		"header": text.getText("a.t.id"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.id-tip", "")
	}, {
		"name": "name",
		"header": text.getText("a.t.name"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.name-tip", "")
	}, {
		"name": "language",
		"header": text.getText("a.t.language"),
		"format": {
			"codemap": consts.localeLanguageMap,
			"type": "code"
			},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.language-tip", "")
	}, {
		"name": "country",
		"header": text.getText("a.t.country"),
		"format": {
			"codemap": consts.localeCountryMap,
			"type": "code"
			},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.country-tip", "")
	}, {
		"name": "source",
		"header": text.getText("a.t.source"),
		"hidden": true,
		"sortable": false,
		"tooltip": text.getText("a.t.source-tip", "")
	}, {
		"name": "status",
		"header": text.getText("a.t.status"),
		"format": {
			"codemap": consts.dataStatusMap,
			"type": "code"
			},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.status-tip", "")
	}, {
		"name": "uusid",
		"header": text.getText("a.t.uusid"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.uusid-tip", "")
	}, {
		"name": "utime",
		"header": text.getText("a.t.utime"),
		"format": {
			"type": "datetime"
			},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.utime-tip", "")
	}] />

	<@p.listview id="template_bdelete"
		action="~/bdelete_execute" method="post"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter">
		<@p.submit icon="icon-bdelete-execute" onclick="return template_bdelete_submit();" theme="simple"><@p.text name="button-bdelete-execute"/></@p.submit>
		
	<#if action.hasPermission("~/list")>
		<@p.a btn="default" icon="icon-list" action="~/list" label="#(button-list)"/>
	</#if>

		<script type="text/javascript"><!--
			function template_bdelete_submit() {
				return plv_submitCheckedKeys('template_bdelete');
			}
			
			function onPageLoad() {
				plv_checkAll('template_bdelete');
			}
		--></script>
	</div>
<#else>
	<div class="p-tcenter">
		<@p.a btn="default" icon="back" href="#" onclick="window.history.back();return false;" label="#(button-back)"/>
	</div>
</#if>
</div>

</body>
</html>
