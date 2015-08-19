<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader/>
	<#include "success-toolbar.ftl"/>

	${s}#include "/panda/mvc/view/action-alert.ftl"/>

	${s}#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": text.getText("_number_", ""),
		"nowrap": true,
		"fixed": true
	}] />

<#if ui.params.actions?has_content>
	${s}#assign _actions_ = [] />
	${s}#assign _ash_ = "" />
	<#assign _uas = ui.params.actions?split(' ')/>
	<#list _uas as _a>
		<#assign _aa = _a?split(':')/>
		<#assign _a0 = _aa[0]/>
		<#assign _a1 = _aa[1]!_a0/>
		<#if _a0?starts_with('@')>
			<#assign _a0 = _a0?substring(1)/>
			<#if _a0?has_content>
				<#assign _a1 = _aa[1]!_a0/>
	${s}#if action.hasPermission("${_a1}")>
		${s}@p.url var='_u_' action='${_a1}'/>
		${s}@p.text var='_icon_' name='icon-${_a0}'/>
		${s}#assign _ash_ = '<a class="n-lv-ia" href="' + vars._u_ + '" title="' + text.getText("tooltip-${_a0}", "")?html + '">'/>
		${s}#if _icon_?has_content>
			${s}#assign _ash_ = _ash_ + '<i class="fa ' + _icon_ + '"></i>'/>
		${s}/#if>
		${s}#assign _ash_ = _ash_ + text.getText("label-${_a0}", "") + '</a>'/>
	${s}/#if>
			</#if>
		<#else>
			<#if _a0 == "">${action.error("Invalid ui action [" + _a + "] of action [" + action.name + "]")}</#if><#t/>
	${s}#if action.hasPermission("${_a1}")>
		${s}#assign _actions_ = _actions_ + [{
			"action": "${_a1}",
			"icon": text.getText("icon-${_a0}"),
			"label": text.getText("label-${_a0}", ""),
			"tooltip": text.getText("tooltip-${_a0}", "")
		}] />
	${s}/#if>
		</#if>
	</#list>
	${s}#if _actions_?has_content || _ash_?has_content>
		${s}#if !(_ash_?has_content)>
			${s}#assign _ash_ = text.getText("_actions_", "")/>
		${s}/#if>
		${s}#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"header": _ash_,
			"actions": _actions_,
			"nowrap": true,
			"fixed": true
		}] />
	${s}/#if>
	${s}#if text.getText("listview-actions-align", "left") == "left">
		${s}#assign _columns_ = _columns_ + _actionc_![]/>
	${s}/#if>
</#if>
<#assign _ops = [] />
<#if ui.params.options?has_content>
	<#assign _ops = (ui.params.options!"")?split(' ') />
</#if>

<#if "true" == ui.params.check!"">
	${s}#assign _columns_ = _columns_ + [{
		"name": "_check_",
		"type": "check",
		"nowrap": true,
		"fixed": true
	}] />
<#else>
	<#assign _opse = false/>
	<#list _ops as _op><#if !gen.startsWithMark(_op)><#assign _opse = true/></#if></#list>
	<#if _opse>
	${s}#if <#list _ops as _op><#assign a = gen.stripStartMark(_op)?split(':')
/><#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid listview tool action name [" + _op + "] of action [" + action.name + "]")}
</#if><#if !gen.startsWithMark(_op)><#if _opse><#assign _opse = false/><#else> || </#if>action.hasPermission("${a[1]}")</#if></#list>>
		${s}#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"nowrap": true,
			"fixed": true
		}] />
	${s}/#if>
	</#if>
</#if>

	${s}#assign _columns_ = _columns_ + [<#rt/>
<#list ui.orderedColumnList as c>
{
			"name" : "${c.name}",
		<#if entity.isPrimaryKey(c.name)>
			"pkey" : true,
		</#if>
			"header": text.getText("a.t.${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
				"type": "${c.format.type?replace('#', '\\x23')}"
			},
 		</#if>
		<#if c.filter??>
			"filter": {
			<#if c.filter.display??>
				"display": ${c.filter.display?string},
			</#if>
			<#if c.filter.label??>
				"label": "${c.filter.label}",
			</#if>
			<#if c.filter.tooltip??>
				"tooltip": "${c.filter.tooltip}",
			</#if>
			<#if c.filter.fixed??>
				"fixed": ${c.filter.fixed?string},
			</#if>
			<#list c.filter.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
				"type": "${c.filter.type?replace('#', '\\x23')}"
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
		<#if c.link?has_content>
			"link": ${c.link},
		</#if>
		<#if c.nowrap??>
			"nowrap": ${c.nowrap?string},
		</#if>
		<#if c.sortable??>
			"sortable": ${c.sortable?string},
		</#if>
		<#if c.filterable??>
			"filterable": ${c.filterable?string},
		</#if>
		<#if c.width?has_content>
			"width": "${c.width}",
		</#if>
			"tooltip": text.getText("a.t.${c.name}-tip", "")
		}<#if c_has_next>, </#if><#rt/>
</#list>

	] />

<#if ui.params.actions?has_content>
	${s}#if text.getText("listview-actions-align", "") == "right">
		${s}#assign _columns_ = _columns_ + _actionc_![]/>
	${s}/#if>
</#if>

	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if> pager="true"
	<#if ui.params.method?has_content>
		method="${ui.params.method}"
	</#if>
	<#if ui.params.target?has_content>
		target="${ui.params.target}"
	</#if>
	<#if ui.params.linkAction?has_content || actionViewName?has_content>
		link={ "action": "${action.name}_${ui.params.linkAction!(actionViewName)}", "params": { <#list entity.primaryKeyList as p>"${actionDataFieldName}.${p.name}": "${p.name}"<#if p_has_next>, </#if></#list> } }
	</#if>
	>
		${s}@p.param name="tools">
		<#list _ops as _op>
			<#assign a = gen.stripStartMark(_op)?split(':')/>
			<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid listview tool action name [" + _op + "] of action [" + action.name + "]")}</#if><#t/>
			<#if _op?starts_with('@') || _op?starts_with('%')>
				<#if a[0]?has_content>
					<#assign an = gen.getActionName(a[0])/>
			${s}#if action.hasPermission("${a[1]}")>
				${s}@p.submit icon="icon-${an}" onclick="return ${action.name}_${ui.name}_${an}();" theme="simple">${s}@p.text name="button-${an}"/>${s}/@p.submit>
			${s}/#if>
				</#if>
			<#else>
			${s}#if action.hasPermission("${a[1]}")>
				<#if _op?contains('^')>
				${s}@p.submit icon="icon-${a[0]}" onclick="return ${action.name}_${ui.name}_${a[0]}();" theme="simple">${s}@p.text name="button-${a[0]}"/>${s}/@p.submit>
				<#else>
				${s}@p.submit icon="icon-${a[0]}" action="${action.name}_${a[0]}" onclick="return ${action.name}_${ui.name}_${a[0]}();" theme="simple">${s}@p.text name="button-${a[0]}"/>${s}/@p.submit>
				</#if>
			${s}/#if>
		</#if>
		</#list>
		${s}/@p.param>
		<#if ui.params.addon?has_content>
		${s}@p.param name="addon">${ui.params.addon}${s}/@p.param>
		</#if>
	${s}/@p.listview>

	<script type="text/javascript"><!--
	<#list _ops as _op>
		<#assign a = gen.stripStartMark(_op)?split(':')/>
		<#if _op?starts_with('@')>
			<#assign an = gen.getActionName(a[0])/>
			<#assign ap = gen.getActionParam(a[0])/>
		function ${action.name}_${ui.name}_${an}() {
			<#if _op?contains('^')>
			window.open("${s}@p.url action='${an}' escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search));
			<#else>
			location.href = "${s}@p.url action='${an}' escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search);
			</#if>
			return false;
		}
		<#elseif _op?starts_with('%')>
			<#assign an = gen.getActionName(a[0])/>
			<#assign ap = gen.getActionParam(a[0])/>
		function ${action.name}_${ui.name}_${an}}() {
			if (nlv_enableCheckedKeys('${action.name}_${ui.name}') > 0) {
				var qs = $(nlv_getBForm('${action.name}_${ui.name}')).serialize();
				<#if _op?contains('^')>
				window.open("${s}@p.url action='${an}' escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>?" + qs));
				<#else>
				location.href = "${s}@p.url action='${an}' escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>?" + qs);
				</#if>
			}
			return false;
		}
		<#else>
		function ${action.name}_${ui.name}_${a[0]}() {
			<#if _op?contains('^')>
			window.open("${s}@p.url action='${a[1]}' includeParams='all' escapeAmp='false'/>");
			return false;
			<#else>
			return nlv_submitCheckedKeys('${action.name}_${ui.name}', '${s}@p.url action="${a[1]}"/>');
			</#if>
		}
		</#if>
	</#list>
	--></script>
</div>

<@footer/>
