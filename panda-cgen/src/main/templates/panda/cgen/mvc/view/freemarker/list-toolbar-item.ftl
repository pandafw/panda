	<#assign a = gen.stripStartMark(t)/>
	<#if t == '!back'>
		<li>${s}@p.a href="javascript:window.history.back()" icon="icon-back" label="#(btn-back)"/>
</li><#rt/>
	<#elseif t == '!refresh'>
		<li>${s}@p.a href="javascript:location.reload(true)" icon="icon-refresh" label="#(btn-refresh)"/>
</li><#rt/>
	<#elseif t?starts_with('!')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<li>${s}@p.a href="javascript:a[1]" icon="icon-a[0]" label="#(btn-a[0])"/>
</li><#rt/>
	<#else>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid toolbar item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if><#t/>
		<#assign an = a[0]/>
		<#assign ap = a[1]/>
${s}#if a.canAccess("${gen.getActionPath(ap)}")><#rt/>
		<#lt/><li>${s}@p.a action="${ap}"<#if t?starts_with('&')> includeParams="all"</#if> icon="icon-${an}" label="#(btn-${an})"<#if t?contains('^')> target="_blank"</#if>/>
</li>${s}/#if><#rt/>
	</#if>
