<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<#include "list-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

	${s}#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": text.getText("listview-th-number", ""),
		"fixed": true
	}] />

<#if ui.params.actions?has_content>
	${s}#assign _actions_ = [] />
	${s}#assign _ash_ = "" />
	<#assign uas = ui.params.actions?split(' ')/>
	<#list uas as a>
		<#assign t = gen.stripStartMark(a)/>
		<#assign aa = t?split(':')/>
		<#if aa[0] == '' || (aa[1]!'') == ''>${action.error("Invalid list action item [" + a + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = aa[0]/>
		<#assign ap = gen.getActionPath(aa[1])/>
		<#if a?starts_with('@')>
	${s}#if action.hasPermission("${ap}")>
		${s}@p.url var='_u_' action='${ap}'/>
		${s}#assign _ash_ = '<a class="n-lv-ia" href="' + vars._u_ + '" title="' + text.getText('tooltip-${an}', '')?html + '"><i class="' + text.getText('icon-${an}', '') + '"></i>' + text.getText('label-${an}', '') + '</a>'/>
	${s}/#if>
		<#else>
	${s}#if action.hasPermission("${ap}")>
		${s}#assign _actions_ = _actions_ + [{
			"action": "${ap}",
		<#if a?contains('%') && entity.primaryKeyList?has_content>
			"params": { <#list entity.primaryKeyList as p>"${p.name}": "%{top.${p.name}}"<#if p_has_next>, </#if></#list> },
		</#if>
			"icon": text.getText("icon-${an}"),
			"label": text.getText("label-${an}", ""),
			"tooltip": text.getText("tooltip-${an}", "")
		}] />
	${s}/#if>
		</#if>
	</#list>
	${s}#if _actions_?has_content || _ash_?has_content>
		${s}#if !(_ash_?has_content)>
			${s}#assign _ash_ = text.getText("listview-th-actions", "")/>
		${s}/#if>
		${s}#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"header": _ash_,
			"actions": _actions_,
			"fixed": true
		}] />
	${s}/#if>
	${s}#if a.actionsAlignLeft>
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
			"fixed": true
		}] />
	${s}/#if>
	</#if>
</#if>

<#list ui.orderedColumnList as c>
${s}#if a.displayColumn("${c.name}")>
	${s}#assign _columns_ = _columns_ + [{
			"name" : "${c.name}",
		<#if entity.isPrimaryKey(c.name)>
			"pkey" : true,
			"value": true,
		<#elseif c.value??>
			"value": ${c.value?string},
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
		}] />
${s}/#if>
</#list>

<#if ui.params.actions?has_content>
	${s}#if a.actionsAlignRight>
		${s}#assign _columns_ = _columns_ + _actionc_![]/>
	${s}/#if>
</#if>
<#if _ops?has_content>
	${s}@p.set var="lvtools">
	<#list _ops as _op>
		<#assign a = gen.stripStartMark(_op)?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid listview options [" + _op + "] of action [" + action.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		${s}#if action.hasPermission("${ap}")>
			${s}@p.b icon="icon-${an}" onclick="return ${action.name}_${ui.name}_${an}();" label="#(button-${an})"/>
		${s}/#if>
	</#list>
	${s}/@p.set>
</#if>

	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-hover table-striped"
	<#if ui.params.method?has_content>
		method="${ui.params.method}"
	</#if>
	<#if ui.params.target?has_content>
		target="${ui.params.target}"
	</#if>
	<#if actionView?has_content>
		link={ "action": "${actionView}", "params": { <#list entity.primaryKeyList as p>"${p.name}": "%{top.${p.name}}"<#if p_has_next>, </#if></#list> } }
	</#if>
	<#if ui.params.addon?has_content>
		addon="${ui.params.addon}"
	</#if>
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
	<#list _ops as _op>
		<#assign a = gen.stripStartMark(_op)?split(':')/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#assign aq = gen.getActionQuery(a[1])/>
		<#if _op?starts_with('@')>
			<#if (a[2]!'') == ''>${action.error("Invalid listview options [" + _op + "] of action [" + action.name + "]")}</#if><#t/>
		function ${action.name}_${ui.name}_${an}() {
			<#if _op?contains('^')>window.open<#else>location.href = </#if>("${s}@p.url action='${ap}' escapeAmp='false'/>?${aq!}=" + encodeURIComponent("${s}@p.url action='${a[2]}' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search));
			return false;
		}
		<#elseif _op?starts_with('%')>
		function ${action.name}_${ui.name}_${an}() {
			return plv_submitCheckedKeys('${action.name}_${ui.name}', '${s}@p.url action="${ap}"/>', null, "<#if _op?contains('^')>_blank</#if>");
		}
		<#elseif _op?starts_with('&')>
		function ${action.name}_${ui.name}_${an}() {
			<#if _op?contains('^')>window.open<#else>location.href = </#if>("${s}@p.url action='${ap}' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		<#else>
		function ${action.name}_${ui.name}_${an}() {
			<#if _op?contains('^')>window.open<#else>location.href = </#if>("${s}@p.url action='${ap}' includeParams='none' escapeAmp='false'/>");
			return false;
		}
		</#if>
	</#list>
	--></script>
<#if ui.safeInclude??>
	${s}@safeinclude path="<#if ui.safeInclude?has_content>${ui.safeInclude}<#else>${action.simpleActionClass}_${ui.name}-custom.ftl</#if>"/>
</#if>
</div>

<@footer/>
