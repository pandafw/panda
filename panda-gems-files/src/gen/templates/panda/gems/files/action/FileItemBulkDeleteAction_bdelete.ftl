<html>
<head>
	<title><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="ui-headline">
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


	<div class="ui-toolbar text-right"><ul>
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul></div>

	<div id="fileitem_alert"><#include "/action-alert.ftl"/></div>
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
		}, {
			"name": "name",
			"pkey" : true,
			"value": true,
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "size",
			"header": a.getFieldLabel("size"),
			"display": a.displayField("size"),
			"format": {
				"type": "size"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("size")
		}, {
			"name": "date",
			"header": a.getFieldLabel("date"),
			"display": a.displayField("date"),
			"format": {
				"type": "timestamp"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("date")
		}] />


	<@p.listview id="fileitem_bdelete"
		action="./bdelete.execute" method="post"
		list=result columns=_columns_
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter" focusme="true">
		<@p.submit onclick="return fileitem_bdelete_submit();" icon="icon-bdelete-execute" label="#(btn-bdelete-execute)"/>

		<@p.a href="javascript:window.history.back()" btn="default" icon="icon-back" label="#(btn-back)"/>

		<script type="text/javascript"><!--
			function fileitem_bdelete_submit() {
				return plv_submitCheckedKeys('fileitem_bdelete');
			}
			
			function onPageLoad() {
				plv_checkAll('fileitem_bdelete');
			}
		--></script>
	</div>
<#else>
	<div class="p-tcenter">
		<@p.a href="#" onclick="window.history.back();return false;" btn="default" icon="back" label="#(btn-back)"/>
	</div>
</#if>
</div>

</body>
</html>
