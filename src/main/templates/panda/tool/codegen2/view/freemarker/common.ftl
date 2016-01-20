<#assign s = "<"/>
<#assign e = ">"/>
<#assign d = "$"/>
<#assign actionDataFieldName = action.dataFieldName!'d'/>
<#assign actionDataListFieldName = action.dataListFieldName!'ds'/>
<#if ui??>
	<#if ui.params.actionList??><#assign actionList = ui.params.actionList/></#if>
	<#if ui.params.actionCopy??><#assign actionCopy = ui.params.actionCopy/></#if>
	<#if ui.params.actionView??><#assign actionView = ui.params.actionView/></#if>
	<#if ui.params.actionPrint??><#assign actionPrint = ui.params.actionPrint/></#if>
	<#if ui.params.actionInsert??><#assign actionInsert = ui.params.actionInsert/></#if>
	<#if ui.params.actionUpdate??><#assign actionUpdate = ui.params.actionUpdate/></#if>
	<#if ui.params.actionDelete??><#assign actionDelete = ui.params.actionDelete/></#if>
	<#list action.listUIList as ui><#if ui.template?? && ui.generate!false>
		<#if ui.templates?seq_contains("list")>
			<#assign actionList = ui.name/>
		<#elseif ui.templates?seq_contains("bdelete") 
			|| ui.templates?seq_contains("bupdate")
			|| ui.templates?seq_contains("bedit")>
			<#assign actionBulkNames = (actionBulkNames![]) + [ ui.name ]/>
		</#if>
	</#if></#list>
	<#list action.inputUIList as ui><#if ui.template?? && ui.generate!false>
		<#if ui.templates?seq_contains("view")>
			<#if !(actionView??)>
				<#assign actionView = '~/' + ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("print")>
			<#if !(actionPrint??)>
				<#assign actionPrint = '~/' + ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("insert")>
			<#if !(actionInsert??)>
				<#assign actionInsert = '~/' + ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("copy")>
			<#if !(actionCopy??)>
				<#assign actionCopy = '~/' + ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("update")>
			<#if !(actionUpdate??)>
				<#assign actionUpdate = '~/' + ui.name/>
			</#if>
		<#elseif ui.templates?seq_contains("delete")>
			<#if !(actionDelete??)>
				<#assign actionDelete = '~/' + ui.name/>
			</#if>
		</#if>
	</#if></#list>
	<#macro header>
<html>
<head>
	<title>${s}@p.text name="title-${ui.name}">${s}@p.param name="title" value="#(title)"/>${s}/@p.text></title>
</head>
<body>
<#if ui.header?has_content>
	${ui.header}
</#if>
	</#macro>
	<#macro sheader steps=[]>
		<#if steps?has_content>
	<div class="p-header">
		<ol class="breadcrumb">
			<li>${s}@p.i icon="icon"/> ${s}@p.text name="title"/></li>
		<#list steps as st>
			<li<#if !st_has_next> class="active"</#if>>${s}@p.text name="step-${st}"/></li>
		</#list>
		</ol>
	</div>
		<#else>
	<div class="p-header">
		<h3>${s}@p.i icon="icon"/> ${s}@p.text name="title-${ui.name}">${s}@p.param name="title" value="#(title)"/>${s}/@p.text></h3>
	</div>
		</#if>
	</#macro>
	<#macro sback>
	<div class="p-tcenter">
		${s}@p.a btn="default" icon="back" href="#" onclick="window.history.back();return false;" label="#(button-back)"/>
	</div>
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
 escapeAmp='false'><#rt/>
				<#if au.params?has_content>
					<#list au.params?keys as pn>
						<#assign _dpv = au.params[pn]?string/>
${s}@p.param name="${pn}" value="<#if _dpv?starts_with('.')>${actionDataFieldName}</#if>${_dpv}"/><#rt/>
					</#list>
				</#if>
${s}/@p.url><#rt/>
	</#macro>
</#if>
