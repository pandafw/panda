<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section a-html2pdf">
	<div class="ui-headline">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<@p.form id="html2pdf" method="get" target="_blank" theme="bs3h" loadmask="false">
		<@p.select key="charset" list="%{consts.avaliableCharsets}" emptyOption="true"/>
		<@p.textarea key="url" rows="5" enterfire="#html2pdf_submit">
			<@p.param name="after">(Ctrl+Enter to generate PDF)<br/></@p.param>
		</@p.textarea>
		<@p.div cssClass="p-buttons">
			<@p.submit id="html2pdf_submit" icon="icon-pdf" label="#(btn-pdf)"/>
		</@p.div>
	</@p.form>

</div>

</body>
</html>
