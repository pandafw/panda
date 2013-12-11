	<#assign a = gen.stripStartMark(t)/>
	<#if t == '@refresh'>
		<li>${s}@p.a icon="icon-refresh" href="javascript:location.reload(true)">${s}@p.text name='button-refresh'/>${s}/@p.a></li>
	<#elseif t?starts_with('@')>
		<#assign a = a?split(':')/>
		<#if a[0]?has_content>
			<#assign ac = gen.getActionContext(a[0])/>
			<#assign an = gen.getActionName(a[0])/>
			<#assign ap = gen.getActionParam(a[0])/>
		<li>${s}@p.a icon="icon-${an}-${a[1]}"<#if t?contains('^')> target="_blank"</#if> action="${an}"<#if ac?has_content> namespace="${ac}"</#if>><#rt/>
			<#if ap?has_content>${s}@s.param name="${ap}">${s}@p.url action='${action.name}_${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'>
				<#list entity.primaryKeyList as p>
					${s}@s.param name="${actionDataFieldName}.${p.name}" value="${actionDataFieldName}.${p.name}"/>
				</#list>
			${s}/@p.url>${s}/@s.param></#if>${s}@p.text name='button-${an}-${a[1]}'/>${s}/@p.a></li>
		</#if>
	<#else>
		<#assign ac = ''/>
		<#assign an = ''/>
		<#assign j = a?index_of(':')/>
		<#if j &gt; 0>
			<#assign a2 = a?substring(j + 1)/>
			<#if a2?starts_with('/')>
				<#assign ac = gen.getActionContext(a2)/>
				<#assign an = gen.getActionName(a2)/>
			<#else>
				<#assign a2 = action.name + '_' + a2/>
				<#assign an = a2/>
			</#if>
			<#assign a = a?substring(0, j)/>
		<#else>
			<#assign a2 = action.name + '_' + a/>
			<#assign an = a2/>
		</#if>
	${s}#if <#if t?contains('%')>action.hasDataPermission(${actionDataFieldName}!, "${a2}")<#else>action.hasPermission("${a2}")</#if>>
		<li>${s}@p.a icon="icon-${a}"<#if t?contains('^')> target="_blank"</#if> action="${an}"<#if ac?has_content> namespace="${ac}"</#if>><#rt/>
<#if t?contains('%')><#list entity.primaryKeyList as p>
${s}@s.param name="${actionDataFieldName}.${p.name}" value="${actionDataFieldName}.${p.name}"/><#rt/>
</#list></#if>${s}@p.text name='button-${a}'/>${s}/@p.a></li>
	${s}/#if>
	</#if>
