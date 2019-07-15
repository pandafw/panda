<html>
<head>
	<title><@p.text name="title-list"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-list"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-list", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./expo.csv")><li><@p.a action="./expo.csv" includeParams="all" icon="icon-csv" label="#(btn-csv)" target="_blank"/>
</li></#if><#if a.canAccess("./expo.json")><li><@p.a action="./expo.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("./expo.xml")><li><@p.a action="./expo.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if><#if a.canAccess("./list.json")><li><@p.a action="./list.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("./list.xml")><li><@p.a action="./list.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if><#if a.canAccess("./list.print")><li><@p.a action="./list.print" includeParams="all" icon="icon-print" label="#(btn-print)" target="_blank"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"fixed": true,
		"header": a.getText("listview-th-number", "")
	}] />



	<#assign _columns_ = _columns_ + [{
			"name" : "id",
			"pkey" : true,
			"value": true,
			"header": a.getFieldLabel("id"),
			"display": a.displayField("id"),
			"filter": {
				"type": "number",
				"enable": a.filterField("id")
			},
			"hidden": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name" : "name",
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"filter": {
				"type": "string",
				"fixed": true,
				"enable": a.filterField("name")
			},
			"sortable": true,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name" : "kind",
			"header": a.getFieldLabel("kind"),
			"display": a.displayField("kind"),
			"format": {
				"codemap": consts.tagKindMap,
				"type": "code"
			},
			"filter": {
				"type": "checklist",
				"list": consts.tagKindMap,
				"enable": a.filterField("kind")
			},
			"sortable": true,
			"tooltip": a.getFieldTooltip("kind")
		}, {
			"name" : "code",
			"header": a.getFieldLabel("code"),
			"display": a.displayField("code"),
			"filter": {
				"type": "string",
				"enable": a.filterField("code")
			},
			"sortable": true,
			"tooltip": a.getFieldTooltip("code")
		}] />



	<@p.queryer id="tags_list_qr" action="./list"
		columns=_columns_
	/>

	<@p.listview id="tags_list" action="./list"
		list=result.list columns=_columns_
		cssTable="table-hover table-striped"
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
	--></script>
</div>

</body>
</html>
