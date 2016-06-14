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
<#assign _well = a.getText("well-bdelete", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("~/list")><li><@p.a icon="icon-list" action="~/list" label="#(button-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>
	<br/>

<#if result?has_content>
	<#assign _columns_ = [{
			"name": "_rownum_",
			"type": "rownum",
			"header": a.getText("listview-th-rownum", ""),
			"fixed": true
		}, {
			"name": "_check_",
			"type": "check",
			"fixed": true
		}] />
<#if a.displayField("id")>
	<#assign _columns_ = _columns_ + [{
			"name": "id",
			"pkey" : true,
			"value": true,
			"header": a.getFieldLabel("id"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("id")
		}] />
</#if>
<#if a.displayField("name")>
	<#assign _columns_ = _columns_ + [{
			"name": "name",
			"header": a.getFieldLabel("name"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}] />
</#if>
<#if a.displayField("size")>
	<#assign _columns_ = _columns_ + [{
			"name": "size",
			"header": a.getFieldLabel("size"),
			"format": {
				"type": "integer"
				},
			"sortable": false,
			"tooltip": a.getFieldTooltip("size")
		}] />
</#if>
<#if a.displayField("date")>
	<#assign _columns_ = _columns_ + [{
			"name": "date",
			"header": a.getFieldLabel("date"),
			"format": {
				"type": "timestamp"
				},
			"sortable": false,
			"tooltip": a.getFieldTooltip("date")
		}] />
</#if>
<#if a.displayField("flag")>
	<#assign _columns_ = _columns_ + [{
			"name": "flag",
			"header": a.getFieldLabel("flag"),
			"format": {
				"type": "integer"
				},
			"sortable": false,
			"tooltip": a.getFieldTooltip("flag")
		}] />
</#if>

	<@p.listview id="filepool_bdelete"
		action="~/bdelete_execute" method="post"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter">
		<@p.submit icon="icon-bdelete-execute" onclick="return filepool_bdelete_submit();" label="#(button-bdelete-execute)"/>

		<@p.a btn="default" icon="icon-back" href="javascript:window.history.back()" label="#(button-back)"/>

		<script type="text/javascript"><!--
			function filepool_bdelete_submit() {
				return plv_submitCheckedKeys('filepool_bdelete');
			}
			
			function onPageLoad() {
				plv_checkAll('filepool_bdelete');
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
