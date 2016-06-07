	<#assign a = gen.stripStartMark(t)/>
	<#if t == '!back'>
		<li>${s}@p.a icon="icon-back" href="javascript:window.history.back()" label="#(button-back)"/>
</li><#rt/>
	<#elseif t == '!refresh'>
		<li>${s}@p.a icon="icon-refresh" href="javascript:location.reload(true)" label="#(button-refresh)"/>
</li><#rt/>
	<#elseif t?starts_with('!')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<li>${s}@p.a icon="icon-a[0]" href="javascript:a[1]" label="#(button-a[0])"/>
</li><#rt/>
	<#elseif t?starts_with('@')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#assign aq = gen.getActionQuery(a[1])/>
		<#if aq?has_content>
	<#lt/>${s}#if r?has_content><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}" label="#(button-${an})"<#if t?contains('^')> target="_blank"</#if> action="${ap}"><#rt/>
				<#lt/>${s}@p.param name="${aq}"><#rt/>
					<#lt/>${s}@p.url action="${a[2]}" forceAddSchemeHostAndPort="true" escapeAmp="false" includeParams="all"/><#rt/>
				<#lt/>${s}/@p.param><#rt/>
			<#lt/>${s}/@p.a>
</li>${s}/#if><#rt/>
		<#else>
	<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(button-${an})"/>
</li><#rt/>
		</#if>
	<#elseif t?starts_with('&')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
${s}#if a.canAccess("${ap}")><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" includeParams="all" label="#(button-${an})"/>
</li>${s}/#if><#rt/>
	<#else>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
${s}#if a.canAccess("${ap}")><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(button-${an})"/>
</li>${s}/#if><#rt/>
	</#if>
