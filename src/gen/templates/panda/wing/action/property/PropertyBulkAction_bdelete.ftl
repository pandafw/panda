<html>
<head>
	<title><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-bdelete"/></li>
			<li class="active"><@p.text name="step-bdelete-confirm"/></li>
		</ol>
	</div>
<#if text.getText("well-bdelete", "")?has_content>
	<div class="p-well"><@p.text name="well-bdelete"/></div>
</#if>


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
		}] />
<#if a.displayColumn("id")>
	<#assign _columns_ = _columns_ + [{
			"name": "id",
			"pkey" : true,
			"value": true,
			"header": text.getText("a.t.id"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.id-tip", "")
		}] />
</#if>
<#if a.displayColumn("clazz")>
	<#assign _columns_ = _columns_ + [{
			"name": "clazz",
			"header": text.getText("a.t.clazz"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.clazz-tip", "")
		}] />
</#if>
<#if a.displayColumn("language")>
	<#assign _columns_ = _columns_ + [{
			"name": "language",
			"header": text.getText("a.t.language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
				},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.language-tip", "")
		}] />
</#if>
<#if a.displayColumn("country")>
	<#assign _columns_ = _columns_ + [{
			"name": "country",
			"header": text.getText("a.t.country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
				},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.country-tip", "")
		}] />
</#if>
<#if a.displayColumn("name")>
	<#assign _columns_ = _columns_ + [{
			"name": "name",
			"header": text.getText("a.t.name"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.name-tip", "")
		}] />
</#if>
<#if a.displayColumn("value")>
	<#assign _columns_ = _columns_ + [{
			"name": "value",
			"header": text.getText("a.t.value"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.value-tip", "")
		}] />
</#if>
<#if a.displayColumn("memo")>
	<#assign _columns_ = _columns_ + [{
			"name": "memo",
			"header": text.getText("a.t.memo"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.memo-tip", "")
		}] />
</#if>
<#if a.displayColumn("status")>
	<#assign _columns_ = _columns_ + [{
			"name": "status",
			"header": text.getText("a.t.status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
				},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.status-tip", "")
		}] />
</#if>
<#if a.displayColumn("uusid")>
	<#assign _columns_ = _columns_ + [{
			"name": "uusid",
			"header": text.getText("a.t.uusid"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.uusid-tip", "")
		}] />
</#if>
<#if a.displayColumn("utime")>
	<#assign _columns_ = _columns_ + [{
			"name": "utime",
			"header": text.getText("a.t.utime"),
			"format": {
				"type": "datetime"
				},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.utime-tip", "")
		}] />
</#if>

	<@p.listview id="property_bdelete"
		action="~/bdelete_execute" method="post"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter">
		<@p.submit icon="icon-bdelete-execute" onclick="return property_bdelete_submit();" theme="simple"><@p.text name="button-bdelete-execute"/></@p.submit>
		
	<#if a.hasPermission("~/list")>
		<@p.a btn="default" icon="icon-list" action="~/list" label="#(button-list)"/>
	</#if>

		<script type="text/javascript"><!--
			function property_bdelete_submit() {
				return plv_submitCheckedKeys('property_bdelete');
			}
			
			function onPageLoad() {
				plv_checkAll('property_bdelete');
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
