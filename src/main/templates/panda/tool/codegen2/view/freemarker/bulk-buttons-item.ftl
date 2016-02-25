	<#assign a = gen.stripStartMark(t)/>
	<#if t == '@refresh'>
		${s}@p.a btn="default" icon="icon-refresh" href="javascript:location.reload(true)" label="#(button-refresh)"/>
	<#elseif t?starts_with('@')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid buttons item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#assign aq = gen.getActionQuery(a[1])/>
		<#if aq?has_content>
	${s}#if r?has_content>
		${s}@p.a btn="default" icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}"><#rt/>
				<#lt/>${s}@p.param name="${aq}"><#rt/>
					<#lt/>${s}@p.url action="${a[2]}" forceAddSchemeHostAndPort="true" escapeAmp="false" includeParams="all"/><#rt/>
				<#lt/>${s}/@p.param><#rt/>
			<#lt/>${s}@p.text name='button-${an}'/>${s}/@p.a>
	${s}/#if>
		<#else>
		${s}@p.a btn="default" icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(button-${an})'/>
		</#if>
	<#else>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid buttons item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
	${s}#if a.hasPermission("${ap}")>
		${s}@p.a btn="default" icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(button-${an})"/>
	${s}/#if>
	</#if>
