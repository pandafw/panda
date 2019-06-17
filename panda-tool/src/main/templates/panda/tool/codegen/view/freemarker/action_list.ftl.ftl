<#include "common.ftl"/>
<@header/>
<@headinc step=""/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<#include "list-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

	${s}#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"fixed": true,
		"header": a.getText("listview-th-number", "")
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
		<#assign ap = aa[1]/>
	${s}#if a.canAccess("${gen.getActionPath(ap)}")>
		<#if a?starts_with('@')>
		${s}@p.url var='_u_' action='${ap}' escapeAmp='true'/>
		${s}#assign _ash_ = '<a class="p-lv-ia" href="' + vars._u_ + '" title="' + a.getText('tip-${an}', '')?html + '"><i class="' + a.getText('icon-${an}', '') + '"></i>' + a.getText('lbl-${an}', '') + '</a>'/>
		<#else>
		${s}#assign _actions_ = _actions_ + [{
			"action": "${ap}",
		<#if a?contains('%') && entity.primaryKeyList?has_content>
			"params": { <#list entity.primaryKeyList as p>"${p.name}": "%{top.${p.name}}"<#if p_has_next>, </#if></#list> },
		</#if>
			"icon": a.getText("icon-${an}"),
			"label": a.getText("lbl-${an}", ""),
			"tooltip": a.getText("tip-${an}", "")
		}] />
		</#if>
	${s}/#if>
	</#list>
	${s}#if _actions_?has_content || _ash_?has_content>
		${s}#if !(_ash_?has_content)>
			${s}#assign _ash_ = a.getText("listview-th-actions", "")/>
		${s}/#if>
		${s}#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"fixed": true,
			"header": _ash_,
			"actions": _actions_
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
	<#list _ops as _op><#if _op?starts_with('%')><#assign _opse = true/></#if></#list>
	<#if _opse>
	${s}#if <#list _ops as _op><#assign a = gen.stripStartMark(_op)?split(':')
/><#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid listview tool action name [" + _op + "] of action [" + action.name + "]")}
</#if><#assign ap = gen.getActionPath(a[1])
/><#if _op?starts_with('%')><#if _opse><#assign _opse = false/><#else> || </#if>action.canAccess("${ap}")</#if></#list>>
		${s}#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"fixed": true
		}] />
	${s}/#if>
	</#if>
</#if>

	${s}#assign _columns_ = _columns_ + [<#list ui.orderedColumnList as c>{
			"name" : "${c.name}",
		<#if entity.isPrimaryKey(c.name)>
			"pkey" : true,
			"value": true,
		<#elseif c.value??>
			"value": ${c.value?string},
		</#if>
			"header": a.getFieldLabel("${c.name}"),
			"display": a.displayField("${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
			<#if c.format.type?has_content>
				"type": "${c.format.type?replace('#', '\\x23')}"<#if c.format.pattern?has_content>,</#if>
			</#if>
			<#if c.format.pattern?has_content>
				"pattern": "${c.format.pattern?replace('#', '\\x23')}"
			</#if>
			},
		</#if>
		<#if c.filter?? && c.filter.type != "none" && c.filter.type != "">
			"filter": {
				"type": "${c.filter.type?replace('#', '\\x23')}",
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
				"enable": a.filterField("${c.name}")
			},
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
		<#if c.sortable??>
			"sortable": ${c.sortable?string},
		</#if>
		<#if c.cssClass?has_content>
			"cssClass": "${c.cssClass}",
		</#if>
			"tooltip": a.getFieldTooltip("${c.name}")
		}<#if c_has_next>, </#if></#list>] />


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
		${s}#if a.canAccess("${ap}")>
			${s}@p.b onclick="return ${action.name}_${ui.name}_${an}();" icon="icon-${an}" label="#(btn-${an})"/>
		${s}/#if>
	</#list>
	${s}/@p.set>
</#if>

	${s}@p.queryer id="${action.name}_${gen.trimUiName(ui.name)}_qr" action="./${ui.name}"<#if ui.params.method?has_content> method="${ui.params.method}"</#if><#if ui.params.target?has_content> target="${ui.params.target}"</#if>
		columns=_columns_<#if ui.params.fsexpand?has_content> expand="${ui.params.fsexpand}"</#if>
	/>

	${s}@p.listview id="${action.name}_${gen.trimUiName(ui.name)}" action="./${ui.name}"<#if ui.params.method?has_content> method="${ui.params.method}"</#if><#if ui.params.target?has_content> target="${ui.params.target}"</#if>
		list=result.list columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-hover table-striped"
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
		<#assign ap = a[1]/>
		<#if _op?starts_with('%')>
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
</div>

<@footinc step=""/>
<@footer/>
