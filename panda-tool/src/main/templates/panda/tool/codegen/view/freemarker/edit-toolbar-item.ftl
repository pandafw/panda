	<#assign a = gen.stripStartMark(t)/>
	<#if t == '!back'>
		<li>${s}@p.a icon="icon-back" href="javascript:window.history.back()" label="#(btn-back)"/>
</li><#rt/>
	<#elseif t == '!refresh'>
		<li>${s}@p.a icon="icon-refresh" href="javascript:location.reload(true)" label="#(btn-refresh)"/>
</li><#rt/>
	<#elseif t?starts_with('!')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<li>${s}@p.a icon="icon-a[0]" href="javascript:a[1]" label="#(btn-a[0])"/>
</li><#rt/>
	<#elseif t?starts_with('@')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#assign aq = gen.getActionQuery(a[1])/>
		<#if aq?has_content>
	<#lt/>${s}#if r??><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}" label="#(btn-${an})"<#if t?contains('^')> target="_blank"</#if> action="${ap}"><#rt/>
				<#lt/>${s}@p.param name="${aq}"><#rt/>
					<#lt/>${s}@p.url action="${a[2]}" forceAddSchemeHostAndPort='true' escapeAmp='false'><#rt/>
				<#list entity.primaryKeyList as p>
					<#lt/>${s}@p.param name="${p.name}" value="%{r.${p.name}}"/><#rt/>
				</#list>
					<#lt/>${s}/@p.url><#rt/>
				<#lt/>${s}/@p.param><#rt/>
			<#lt/>${s}/@p.a>
</li>${s}/#if><#rt/>
		<#else>
	<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(btn-${an})'/>
</li><#rt/>
		</#if>
	<#else>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#if t?contains('%')>
${s}#if r?? && a.canAccessData("${ap}", r)><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}" label="#(btn-${an})"<#if t?contains('^')> target="_blank"</#if> action="${ap}"><#rt/>
	<#list entity.primaryKeyList as p>
${s}@p.param name="${p.name}" value="%{r.${p.name}}"/><#rt/>
</#list>${s}/@p.a>
</li>${s}/#if><#rt/>
		<#else>
${s}#if a.canAccess("${ap}")><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(btn-${an})"/>
</li>${s}/#if><#rt/>
		</#if>
	</#if>
