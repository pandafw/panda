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
<#if a.canAccess("~/add")><li><@p.a icon="icon-new" action="~/add" label="#(button-new)"/>
</li></#if><#if a.canAccess("~/list_print")><li><@p.a icon="icon-print" target="_blank" action="~/list_print" includeParams="all" label="#(button-print)"/>
</li></#if><#if a.canAccess("~/list_csv")><li><@p.a icon="icon-csv" target="_blank" action="~/list_csv" includeParams="all" label="#(button-csv)"/>
</li></#if><#if a.canAccess("~/list_json")><li><@p.a icon="icon-json" target="_blank" action="~/list_json" includeParams="all" label="#(button-json)"/>
</li></#if><#if a.canAccess("~/list_xml")><li><@p.a icon="icon-xml" target="_blank" action="~/list_xml" includeParams="all" label="#(button-xml)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": a.getText("listview-th-number", ""),
		"fixed": true
	}] />

	<#assign _actions_ = [] />
	<#assign _ash_ = "" />
	<#if a.canAccess("~/add")>
		<@p.url var='_u_' action='~/add'/>
		<#assign _ash_ = '<a class="p-lv-ia" href="' + vars._u_ + '" title="' + a.getText('tooltip-new', '')?html + '"><i class="' + a.getText('icon-new', '') + '"></i>' + a.getText('label-new', '') + '</a>'/>
	</#if>
	<#if a.canAccess("~/copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/copy",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-copy"),
			"label": a.getText("label-copy", ""),
			"tooltip": a.getText("tooltip-copy", "")
		}] />
	</#if>
	<#if a.canAccess("~/edit")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/edit",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-edit"),
			"label": a.getText("label-edit", ""),
			"tooltip": a.getText("tooltip-edit", "")
		}] />
	</#if>
	<#if a.canAccess("~/delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/delete",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-delete"),
			"label": a.getText("label-delete", ""),
			"tooltip": a.getText("tooltip-delete", "")
		}] />
	</#if>
	<#if _actions_?has_content || _ash_?has_content>
		<#if !(_ash_?has_content)>
			<#assign _ash_ = a.getText("listview-th-actions", "")/>
		</#if>
		<#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"header": _ash_,
			"actions": _actions_,
			"fixed": true
		}] />
	</#if>
	<#if a.actionsAlignLeft>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<#if action.canAccess("~/bdelete")>
		<#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"fixed": true
		}] />
	</#if>

<#if a.displayField("id")>
	<#assign _columns_ = _columns_ + [{
			"name" : "id",
			"pkey" : true,
			"value": true,
			"header": a.getText("a.t.id"),
			"filter": {
				"type": "number"
			},
			"link": true,
			"sortable": true,
			"tooltip": a.getText("a.t.id-tip", "")
		}] />
</#if>
<#if a.displayField("name")>
	<#assign _columns_ = _columns_ + [{
			"name" : "name",
			"header": a.getText("a.t.name"),
			"filter": {
				"type": "string"
			},
			"sortable": true,
			"tooltip": a.getText("a.t.name-tip", "")
		}] />
</#if>
<#if a.displayField("size")>
	<#assign _columns_ = _columns_ + [{
			"name" : "size",
			"header": a.getText("a.t.size"),
			"format": {
				"type": "integer"
			},
			"filter": {
				"type": "number"
			},
			"sortable": true,
			"tooltip": a.getText("a.t.size-tip", "")
		}] />
</#if>
<#if a.displayField("date")>
	<#assign _columns_ = _columns_ + [{
			"name" : "date",
			"header": a.getText("a.t.date"),
			"format": {
				"type": "timestamp"
			},
			"filter": {
				"type": "datetime"
			},
			"sortable": true,
			"tooltip": a.getText("a.t.date-tip", "")
		}] />
</#if>
<#if a.displayField("flag")>
	<#assign _columns_ = _columns_ + [{
			"name" : "flag",
			"header": a.getText("a.t.flag"),
			"format": {
				"type": "integer"
			},
			"filter": {
				"type": "number"
			},
			"sortable": true,
			"tooltip": a.getText("a.t.flag-tip", "")
		}] />
</#if>

	<#if a.actionsAlignRight>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>
	<@p.set var="lvtools">
		<#if a.canAccess("~/bdelete")>
			<@p.b icon="icon-bdelete" onclick="return filepool_list_bdelete();" label="#(button-bdelete)"/>
		</#if>
	</@p.set>

	<@p.listview id="filepool_list" action="~/list" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
		link={ "action": "~/view", "params": { "id": "%{top.id}" } }
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
		function filepool_list_bdelete() {
			return plv_submitCheckedKeys('filepool_list', '<@p.url action="~/bdelete"/>', null, "");
		}
	--></script>
</div>

</body>
</html>
