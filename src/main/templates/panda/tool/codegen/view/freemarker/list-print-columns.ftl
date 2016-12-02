	${s}#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"header": a.getText("listview-th-number", ""),
			"fixed": true
		}] />
<#list ui.displayColumnList as c>
${s}#if a.displayField("${c.name}")>
	${s}#assign _columns_ = _columns_ + [{
			"name": "${c.name}",
			"value": false,
			"header": a.getFieldLabel("${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
				"type": "${c.format.type?replace('#', '\\x23')}"
			},
		</#if>
		<#if c.display??>
			"display": ${c.display?string},
		</#if>
		<#if c.hidden??>
			"hidden": ${c.hidden?string},
		</#if>
		<#if c.group??>
			"group": ${c.group?string},
		</#if>
		<#if c.nowrap??>
			"nowrap": ${c.nowrap?string},
		</#if>
			"sortable": false,
		<#if c.width?has_content>
			"width": "${c.width}",
		</#if>
			"tooltip": a.getFieldTooltip("${c.name}")
		}] />
${s}/#if>
</#list>
