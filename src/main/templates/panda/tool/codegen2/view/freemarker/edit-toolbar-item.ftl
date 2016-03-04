	<#assign a = gen.stripStartMark(t)/>
	<#if t == '@refresh'>
		<li>${s}@p.a icon="icon-refresh" href="javascript:location.reload(true)" label="#(button-refresh)"/>
</li><#rt/>
	<#elseif t?starts_with('@')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#assign aq = gen.getActionQuery(a[1])/>
		<#if aq?has_content>
	<#lt/>${s}#if r??><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}" label="#(button-${an})"<#if t?contains('^')> target="_blank"</#if> action="${ap}"><#rt/>
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
	<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(button-${an})'/>
</li><#rt/>
		</#if>
	<#else>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = gen.getActionPath(a[1])/>
		<#if t?contains('%')>
${s}#if r?? && a.hasDataPermission(r, "${ap}")><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}" label="#(button-${an})"<#if t?contains('^')> target="_blank"</#if> action="${ap}"><#rt/>
	<#list entity.primaryKeyList as p>
${s}@p.param name="${p.name}" value="%{r.${p.name}}"/><#rt/>
</#list>${s}/@p.a>
</li>${s}/#if><#rt/>
		<#else>
${s}#if a.hasPermission("${ap}")><#rt/>
		<#lt/><li>${s}@p.a icon="icon-${an}"<#if t?contains('^')> target="_blank"</#if> action="${ap}" label="#(button-${an})"/>
</li>${s}/#if><#rt/>
		</#if>
	</#if>
