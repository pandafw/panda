	<#assign _a2 = gen.stripStartMark(_a)/>
	<#if _a == '@refresh'>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-refresh",
					"onclick": "location.reload(true); return false;",
					"text": "button-refresh"
				}]/>
	<#elseif _a?starts_with('@')>
		<#assign j = _a2?index_of(':')/>
		<#if j &gt; 0>
			<#assign _a3 = action.name + '_' + _a2?substring(j + 1)/>
			<#assign _a2 = _a2?substring(0, j)/>
		<#else>
			<#assign _a3 = action.name + '_' + _a2/>
		</#if>
			${s}#if action.hasPermission('${_a2}')>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-${_a2}",
					"action": "${_a3}",
					"text": "button-${_a2}"
				}]/>
			${s}/#if>
	<#else>
		<#assign ac = ''/>
		<#assign an = ''/>
		<#assign _a3 = ''/>
		<#assign j = _a2?index_of(':')/>
		<#if j &gt; 0>
			<#assign _a3 = _a2?substring(j + 1)/>
			<#if _a3?starts_with('/')>
				<#assign ac = gen.getActionContext(_a3)/>
				<#assign an = gen.getActionName(_a3)/>
			<#else>
				<#assign _a3 = action.name + '_' + _a3/>
				<#assign an = _a3/>
			</#if>
			<#assign _a2 = _a2?substring(0, j)/>
		<#else>
			<#assign _a3 = action.name + '_' + _a2/>
			<#assign an = _a3/>
		</#if>
		<#if _a?contains('%')>
			${s}#if action.hasDataPermission(${actionDataFieldName}!, '${_a3}')>
				${s}@p.url var="_u_" action="${_a3}" escapeAmp="false">
<#list entity.primaryKeyList as p>
					${s}@s.param name="${actionDataFieldName}.${p.name}" value="${actionDataFieldName}.${p.name}"/>
</#list>
				${s}/@p.url>
		<#else>
			${s}#if action.hasPermission('${_a3}')>
				${s}@p.url var="_u_" action="${an}"<#if ac?has_content> namespace="${ac}"</#if>/>
		</#if>
				${s}#assign _buttons_ = _buttons_ + [{
					"icon": "icon-${_a2}",
		<#if _a?contains('^')>
					"onclick": "window.open('${d}{_u_?js_string}'); return false;",
		<#else>
					"onclick": "location.href='${d}{_u_?js_string}'; return false;",
		</#if>
					"text": "button-${_a2}"
				}]/>
			${s}/#if>
	</#if>
