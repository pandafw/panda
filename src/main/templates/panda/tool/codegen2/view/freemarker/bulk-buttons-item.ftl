		<#assign a0 = gen.stripStart(t, "%*^")/>
		<#if a0?starts_with('@')>
			<#assign a = a0?substring(1)/>
			<#assign j = a?index_of(':')/>
			<#if j &gt; 0>
				<#assign a2 = action.name + '_' + a?substring(j + 1)/>
				<#assign a = a?substring(0, j)/>
			<#else>
				<#assign a2 = action.name + '_' + a/>
			</#if>
		${s}#if action.hasPermission("${a2}")>
			${s}@p.submit icon="icon-${a}" action="${a2}" theme="simple">${s}@p.text name='button-${a}'/>${s}/@p.submit>
		${s}/#if>
		<#else>
			<#assign a = a0/>
			<#assign j = a?index_of(':')/>
			<#if j &gt; 0>
				<#assign a2 = action.name + '_' + a?substring(j + 1)/>
				<#assign a = a?substring(0, j)/>
			<#else>
				<#assign a2 = action.name + '_' + a/>
			</#if>
		${s}#if action.hasPermission("${a2}")>
			${s}@p.url var="_u_" action='${a2}'/>
		<#if t?contains('^')>
			${s}@p.submit icon="icon-${a}" onclick="window.open('${d}{_u_}');return false;" theme="simple">${s}@p.text name='button-${a}'/>${s}/@p.submit>
		<#else>
			${s}@p.submit icon="icon-${a}" onclick="location.href='${d}{_u_}';return false;" theme="simple">${s}@p.text name='button-${a}'/>${s}/@p.submit>
		</#if>
		${s}/#if>
		</#if>
