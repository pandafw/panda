<#assign s = "<"/>
<#assign e = ">"/>
<#assign d = "$"/>
<#assign actionDataFieldName = action.dataFieldName!'d'/>
<#assign actionDataListFieldName = action.dataListFieldName!'ds'/>
<#if ui??>
	<#if ui.params.actionListName??><#assign actionListName = ui.params.actionListName/></#if>
	<#if ui.params.actionCopyName??><#assign actionCopyName = ui.params.actionCopyName/></#if>
	<#if ui.params.actionViewName??><#assign actionViewName = ui.params.actionViewName/></#if>
	<#if ui.params.actionPrintName??><#assign actionPrintName = ui.params.actionPrintName/></#if>
	<#if ui.params.actionInsertName??><#assign actionInsertName = ui.params.actionInsertName/></#if>
	<#if ui.params.actionUpdateName??><#assign actionUpdateName = ui.params.actionUpdateName/></#if>
	<#if ui.params.actionDeleteName??><#assign actionDeleteName = ui.params.actionDeleteName/></#if>
	<#list action.listUIList as ui><#if ui.template?? && ui.generate!false>
		<#if ui.templates?seq_contains("list")>
			<#assign actionListName = ui.name/>
		<#elseif ui.templates?seq_contains("bdelete") 
			|| ui.templates?seq_contains("bupdate")
			|| ui.templates?seq_contains("bedit")>
			<#assign actionBulkNames = (actionBulkNames![]) + [ ui.name ]/>
		</#if>
	</#if></#list>
	<#list action.inputUIList as ui><#if ui.template?? && ui.generate!false>
		<#if ui.templates?seq_contains("view")>
			<#if !(actionViewName??)>
				<#assign actionViewName = ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("print")>
			<#if !(actionPrintName??)>
				<#assign actionPrintName = ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("insert")>
			<#if !(actionInsertName??)>
				<#assign actionInsertName = ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("copy")>
			<#if !(actionCopyName??)>
				<#assign actionCopyName = ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("update")>
			<#if !(actionUpdateName??)>
				<#assign actionUpdateName = ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("delete")>
			<#if !(actionDeleteName??)>
				<#assign actionDeleteName = ui.name/>
			</#if>
		</#if>
	</#if></#list>
	<#macro header>
<html>
<head>
	<title>${s}@p.text name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text></title>
</head>
<body>
<#if ui.header?has_content>
	${ui.header}
</#if>
	</#macro>
	<#macro footer>
<#if ui.footer?has_content>
	${ui.footer}
</#if>
</body>
</html>
	</#macro>
	<#macro aurl au>${s}@p.url<#rt/>
				<#if au.value??>
 value='${au.value}'<#rt/>
				</#if>
				<#if au.action??>
 action='${au.action}'<#rt/>
				</#if>
				<#if au.namespace??>
 namespace='${au.namespace}'<#rt/>
				</#if>
 escapeAmp='false'><#rt/>
				<#if au.params?has_content>
					<#list au.params?keys as pn>
						<#assign _dpv = au.params[pn]?string/>
${s}@s.param name="${pn}" value="<#if _dpv?starts_with('.')>${actionDataFieldName}</#if>${_dpv}"/><#rt/>
					</#list>
				</#if>
${s}/@p.url><#rt/>
	</#macro>
</#if>
