	<#assign _a2 = gen.stripStartMark(_a)/>
	<#if _a == '!back'>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-back",
					"onclick": "window.history.back(); return false;",
					"text": "button-back"
				}]/>
	<#elseif _a == '!refresh'>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-refresh",
					"onclick": "location.reload(true); return false;",
					"text": "button-refresh"
				}]/>
	<#elseif _a == '!reset'>
				${s}#assign _buttons_ = _buttons_ + [{
					"type": "reset",
					"icon": "icon-reset",
					"text": "button-reset"
				}]/>
	<#elseif _a?starts_with('!')>
		<#assign _as = _a2?split(':')/>
		<#if _as[0] == '' || (_as[1]!'') == ''>${action.error("Invalid toolbar item [" + _a + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = _as[0]/>
			${s}#assign _buttons_ = _buttons_ + [{
				"icon": "icon-${an}",
				"onclick": "_as[1]",
				"text": "button-${an}"
			}]/>
	<#elseif _a?starts_with('@')>
		<#assign _as = _a2?split(':')/>
		<#if _as[0] == '' || (_as[1]!'') == ''>${action.error("Invalid toolbar item [" + _a + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = _as[0]/>
		<#assign ap = gen.getActionPath(_as[1])/>
			${s}#if a.hasPermission('${ap}')>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-${an}",
					"action": "${ap}",
					"text": "button-${an}"
				}]/>
			${s}/#if>
	<#else>
		<#assign _as = _a2?split(':')/>
		<#if _as[0] == '' || (_as[1]!'') == ''>${action.error("Invalid toolbar item [" + _a + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = _as[0]/>
		<#assign ap = gen.getActionPath(_as[1])/>
		<#if _a?contains('%')>
			${s}#if r?? && a.hasDataPermission(r, '${ap}')>
				${s}@p.url var="_u_" action="${ap}" escapeAmp="false">
<#list entity.primaryKeyList as p>
					${s}@p.param name="${p.name}" value="%{r.${p.name}}"/>
</#list>
				${s}/@p.url>
		<#else>
			${s}#if a.hasPermission('${ap}')>
				${s}@p.url var="_u_" action="${ap}"/>
		</#if>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-${an}",
		<#if _a?contains('^')>
					"onclick": "window.open('${d}{vars._u_?js_string}'); return false;",
		<#else>
					"onclick": "location.href='${d}{vars._u_?js_string}'; return false;",
		</#if>
					"text": "button-${an}"
				}]/>
			${s}/#if>
	</#if>
