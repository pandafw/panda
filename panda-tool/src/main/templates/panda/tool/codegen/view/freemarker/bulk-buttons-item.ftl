	<#assign a = gen.stripStartMark(t)/>
	<#if t == '!back'>
		${s}@p.a href="javascript:window.history.back()" btn="default" icon="icon-back" label="#(btn-back)"/>
	<#elseif t == '!refresh'>
		${s}@p.a href="javascript:location.reload(true)" btn="default" icon="icon-refresh" label="#(btn-refresh)"/>
	<#elseif t?starts_with('!')>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid buttons item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if>
		${s}@p.a href="javascript:${a[1]}" btn="default" icon="icon-${a[0]}" label="#(btn-${a[0]})"/>
	<#else>
		<#assign a = a?split(':')/>
		<#if a[0] == '' || (a[1]!'') == ''>${action.error("Invalid buttons item [" + t + "] of action [" + action.name + "] ui [" + ui.name + "]")}</#if>
		<#assign an = a[0]/>
		<#assign ap = a[1]/>
	${s}#if a.canAccess("${gen.getActionPath(ap)}")>
		${s}@p.a action="${ap}"<#if t?starts_with('&')> includeParams="all"</#if> btn="default" icon="icon-${an}" label="#(btn-${an})"<#if t?contains('^')> target="_blank"</#if>/>
	${s}/#if>
	</#if>
