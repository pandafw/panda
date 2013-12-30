<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<div class="p-header">
		<h3>${s}@p.text name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text></h3>
	</div>
	<#include "success-toolbar.ftl"/>

	${s}#include "/panda/exts/struts2/views/action-alert.ftl"/>

	${s}#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": action.getText("_number_", ""),
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
	${s}#if action.hasPermission("${action.name}_${_a1}")>
		${s}@p.url var="_u_" action="${action.name}_${_a1}"/>
		${s}@p.text var="_icon_" name="icon-${_a0}"/>
		${s}#assign _ash_ = "<a class=\"n-lv-ia\" href=\"" + _u_
				+ "\" title=\"" + action.getText("tooltip-${_a0}", "")?html + "\">"/>
		${s}#if _icon_?has_content>
			${s}#assign _ash_ = _ash_ + "<img class=\"n-lv-i n-icon " + _icon_ 
				+ "\" src=\"" + static + "/nuts/images/s.gif\"></img>"/>
		${s}/#if>
		${s}#assign _ash_ = _ash_ + action.getText("label-${_a0}", "") + "</a>"/>
	${s}/#if>
			</#if>
		<#else>
			<#if _a0 == "">${action.error("Invalid ui action [" + _a + "] of action [" + action.name + "]")}</#if><#t/>
	${s}#if action.hasPermission("${action.name}_${_a1}")>
		${s}#assign _actions_ = _actions_ + [{
			"action": "${action.name}_${_a1}",
			"icon": action.getText("icon-${_a0}"),
			"label": action.getText("label-${_a0}", ""),
			"tooltip": action.getText("tooltip-${_a0}", "")
		}] />
	${s}/#if>
		</#if>
	</#list>
	${s}#if _actions_?has_content || _ash_?has_content>
		${s}#if !(_ash_?has_content)>
			${s}#assign _ash_ = action.getText("_actions_", "")/>
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
	${s}#if action.getText("listview-actions-align", "left") == "left">
		${s}#assign _columns_ = _columns_ + _actionc_![]/>
	${s}/#if>
</#if>
<#assign _pos = [] />
<#if ui.params.options?has_content>
	<#assign _pos = (ui.params.options!"")?split(' ') />
</#if>

<#if "true" == ui.params.check!"">
	${s}#assign _columns_ = _columns_ + [{
		"name": "_check_",
		"type": "check",
		"nowrap": true,
		"fixed": true
	}] />
<#else>
	<#assign _pose = false/>
	<#list _pos as a><#if !gen.startsWithMark(a)><#assign _pose = true/></#if></#list>
	<#if _pose>
	${s}#if <#list _pos as a><#if !gen.startsWithMark(a)><#if _pose><#assign _pose = false/><#else> || </#if>action.hasPermission("${action.name}_${a}")</#if></#list>>
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
			"header": action.getText("${actionDataFieldName}.${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": "${fp.value?replace('#', '\\x23')}",
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
				"${fp.name}": "${fp.value?replace('#', '\\x23')}",
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
			"tooltip": action.getText("${actionDataFieldName}.${c.name}-tip", "")
		}<#if c_has_next>, </#if><#rt/>
</#list>

	] />

<#if ui.params.actions?has_content>
	${s}#if action.getText("listview-actions-align", "") == "right">
		${s}#assign _columns_ = _columns_ + _actionc_![]/>
	${s}/#if>
</#if>

	${s}@p.listview id="${action.name}_${ui.name}" action="${action.name}_${ui.name}" 
		list="${actionDataListFieldName}" columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		start="pg.s" limit="pg.l" total="pg.t" sort="so.c" dir="so.d" filters="qf" filterm="qm" pager="true"
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
		${s}@s.param name="tools">
		<#list _pos as _po>
			<#assign a = gen.stripStartMark(_po)?split(':')/>
			<#if _po?starts_with('@') || _po?starts_with('%')>
				<#if a[0]?has_content>
					<#assign an = gen.getActionName(a[0])/>
			${s}#if action.hasPermission("${action.name}_${a[1]}")>
				${s}@p.submit icon="icon-${an}-${a[1]}" onclick="return ${action.name}_${ui.name}_${an}_${a[1]}();" theme="simple">${s}@p.text name="button-${an}-${a[1]}"/>${s}/@p.submit>
			${s}/#if>
				</#if>
			<#else>
				<#assign ac = gen.getActionContext(a[1]!(a[0]))/>
				<#assign an = gen.getActionName(a[1]!(a[0]))/>
				<#if an == "">${action.error("Invalid listview tool action name [" + _po + "] of action [" + action.name + "]")}</#if><#t/>
				<#assign ap = gen.getActionParam(a[1]!(a[0]))/>
				<#if ac?has_content>
			${s}#if action.hasPermission("${ac}/${an}")>
				<#else>
			${s}#if action.hasPermission("${action.name}_${an}")>
				</#if>
				<#if _po?contains('^')>
				${s}@p.submit icon="icon-${a[0]}" onclick="return ${action.name}_${ui.name}_${a[0]}();" theme="simple">${s}@p.text name="button-${a[0]}"/>${s}/@p.submit>
				<#else>
				${s}@p.submit icon="icon-${a[0]}" action="${action.name}_${an}" onclick="return ${action.name}_${ui.name}_${a[0]}();" theme="simple">${s}@p.text name="button-${a[0]}"/>${s}/@p.submit>
				</#if>
			${s}/#if>
		</#if>
		</#list>
		${s}/@s.param>
		<#if ui.params.addon?has_content>
		${s}@s.param name="addon">${ui.params.addon}${s}/@s.param>
		</#if>
	${s}/@p.listview>

	<script type="text/javascript"><!--
	<#list _pos as _po>
		<#assign a = gen.stripStartMark(_po)?split(':')/>
		<#if _po?starts_with('@')>
			<#if a[0]?has_content>
				<#assign ac = gen.getActionContext(a[0])/>
				<#assign an = gen.getActionName(a[0])/>
				<#assign ap = gen.getActionParam(a[0])/>
		function ${action.name}_${ui.name}_${an}_${a[1]}() {
			<#if _po?contains('^')>
			window.open("${s}@p.url action='${an}'<#if ac?has_content> namespace='${ac}'</#if> escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${action.name}_${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search));
			<#else>
			location.href = "${s}@p.url action='${an}'<#if ac?has_content> namespace='${ac}'</#if> escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${action.name}_${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search);
			</#if>
			return false;
		}
			</#if>
		<#elseif _po?starts_with('%')>
			<#if a[0]?has_content>
				<#assign ac = gen.getActionContext(a[0])/>
				<#assign an = gen.getActionName(a[0])/>
				<#if an == "">${action.error("Invalid listview tool action name [" + _po + "] of action [" + action.name + "]")}</#if><#t/>
				<#assign ap = gen.getActionParam(a[0])/>
		function ${action.name}_${ui.name}_${an}_${a[1]}() {
			if (nlv_enableCheckedKeys('${action.name}_${ui.name}') > 0) {
				var qs = $(nlv_getBForm('${action.name}_${ui.name}')).serialize();
			<#if _po?contains('^')>
				window.open("${s}@p.url action='${an}'<#if ac?has_content> namespace='${ac}'</#if> escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${action.name}_${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>?" + qs));
			<#else>
				location.href = "${s}@p.url action='${an}'<#if ac?has_content> namespace='${ac}'</#if> escapeAmp='false'/>?<#if ap?has_content>${ap}=</#if>" + encodeURIComponent("${s}@p.url action='${action.name}_${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>?" + qs);
			</#if>
			}
			return false;
		}
			</#if>
		<#else>
			<#assign ac = gen.getActionContext(a[1]!(a[0]))/>
			<#assign an = gen.getActionName(a[1]!(a[0]))/>
			<#assign ap = gen.getActionParam(a[1]!(a[0]))/>
		function ${action.name}_${ui.name}_${a[0]}() {
			<#if _po?contains('^')>
				<#if ac?has_content>
			window.open("${s}@p.url action='${an}' namespace='${ac}' includeParams='all' escapeAmp='false'/>");
				<#else>
			window.open("${s}@p.url action='${action.name}_${an}' includeParams='all' escapeAmp='false'/>");
				</#if>
			return false;
			<#else>
				<#if ac?has_content>
			return nlv_submitCheckedKeys('${action.name}_${ui.name}', '${s}@p.url action="${an}" namespace='${ac}'/>');
				<#else>
			return nlv_submitCheckedKeys('${action.name}_${ui.name}', '${s}@p.url action="${action.name}_${an}"/>');
				</#if>
			</#if>
		}
		</#if>
	</#list>
	--></script>
</div>

<@footer/>
