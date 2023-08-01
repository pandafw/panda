<html>
<head>
	<title><@p.text name="title-list"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="ui-headline">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-list"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-list", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="ui-toolbar text-right"><ul>
<#if a.canAccess("./add")><li><@p.a action="./add" icon="icon-new" label="#(btn-new)"/>
</li></#if><#if a.canAccess("./import")><li><@p.a action="./import" icon="icon-import" label="#(btn-import)"/>
</li></#if><#if a.canAccess("./expo.csv")><li><@p.a action="./expo.csv" includeParams="all" icon="icon-csv" label="#(btn-csv)" target="_blank"/>
</li></#if><#if a.canAccess("./expo.json")><li><@p.a action="./expo.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("./expo.xml")><li><@p.a action="./expo.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if><#if a.canAccess("./list.json")><li><@p.a action="./list.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("./list.xml")><li><@p.a action="./list.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if><#if a.canAccess("./list.print")><li><@p.a action="./list.print" includeParams="all" icon="icon-print" label="#(btn-print)" target="_blank"/>
</li></#if>	</ul></div>

	<div id="files_alert"><#include "/action-alert.ftl"/></div>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"fixed": true,
		"header": a.getText("listview-th-number", "")
	}] />

	<#assign _actions_ = [] />
	<#assign _ash_ = "" />
	<#if a.canAccess("./add")>
		<@p.url var='_u_' action='./add' escapeAmp='true'/>
		<#assign _ash_ = '<a class="p-lv-ia" href="' + vars._u_ + '" title="' + a.getText('tip-new', '')?html + '"><i class="' + a.getText('icon-new', '') + '"></i>' + a.getText('lbl-new', '') + '</a>'/>
	</#if>
	<#if a.canAccess("./copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "./copy",
			"params": { "name": "%{top.name}" },
			"icon": a.getText("icon-copy"),
			"label": a.getText("lbl-copy", ""),
			"tooltip": a.getText("tip-copy", "")
		}] />
	</#if>
	<#if a.canAccess("./edit")>
		<#assign _actions_ = _actions_ + [{
			"action": "./edit",
			"params": { "name": "%{top.name}" },
			"icon": a.getText("icon-edit"),
			"label": a.getText("lbl-edit", ""),
			"tooltip": a.getText("tip-edit", "")
		}] />
	</#if>
	<#if a.canAccess("./delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "./delete",
			"params": { "name": "%{top.name}" },
			"icon": a.getText("icon-delete"),
			"label": a.getText("lbl-delete", ""),
			"tooltip": a.getText("tip-delete", "")
		}] />
	</#if>
	<#if _actions_?has_content || _ash_?has_content>
		<#if !(_ash_?has_content)>
			<#assign _ash_ = a.getText("listview-th-actions", "")/>
		</#if>
		<#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"fixed": true,
			"header": _ash_,
			"actions": _actions_
		}] />
	</#if>
	<#if a.actionsAlignLeft>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<#if action.canAccess("./bdelete")>
		<#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"fixed": true
		}] />
	</#if>

	<#assign _columns_ = _columns_ + [{
			"name" : "name",
			"pkey" : true,
			"value": true,
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"filter": {
				"type": "string",
				"fixed": true,
				"enable": a.filterField("name")
			},
			"link": true,
			"sortable": true,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name" : "size",
			"header": a.getFieldLabel("size"),
			"display": a.displayField("size"),
			"format": {
				"type": "size"
			},
			"filter": {
				"type": "number",
				"fixed": true,
				"enable": a.filterField("size")
			},
			"sortable": true,
			"tooltip": a.getFieldTooltip("size")
		}, {
			"name" : "date",
			"header": a.getFieldLabel("date"),
			"display": a.displayField("date"),
			"format": {
				"type": "timestamp"
			},
			"filter": {
				"type": "datetime",
				"fixed": true,
				"enable": a.filterField("date")
			},
			"sortable": true,
			"tooltip": a.getFieldTooltip("date")
		}] />


	<#if a.actionsAlignRight>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>
	<@p.set var="lvtools">
		<#if a.canAccess("./bdelete")>
			<@p.b onclick="return files_list_bdelete();" icon="icon-bdelete" label="#(btn-bdelete)"/>
		</#if>
	</@p.set>

	<@p.queryer id="files_list_qr" action="./list"
		columns=_columns_
	/>

	<@p.listview id="files_list" action="./list"
		list=result.list columns=_columns_
		cssTable="table-hover table-striped"
		link={ "action": "./view", "params": { "name": "%{top.name}" } }
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
		function files_list_bdelete() {
			return plv_submitCheckedKeys('files_list', '<@p.url action="./bdelete"/>', null, "");
		}
	--></script>
</div>

</body>
</html>
