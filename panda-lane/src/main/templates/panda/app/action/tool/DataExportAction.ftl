<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_dataimp">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<@p.form id="dataexp" cssClass="p-eform" method="post" theme="bs3h">
		<@p.select key="target" list="%{action.targets}"/>
		<@p.textfield key="start" size="20" />
		<@p.textfield key="limit" size="20" />
		<@p.radio key="format" list=['CSV', 'TSV', 'XLS', 'XLSX' ] />
		<@p.div cssClass="p-buttons">
			<@p.submit icon="download" label="#(btn-download)"/>
		</@p.div>
	</@p.form>
</div>

</body>
</html>
