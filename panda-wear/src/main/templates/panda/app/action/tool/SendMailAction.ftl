<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section a-sendmail">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<@p.form id="sendmail" cssClass="p-eform" method="post" action="~/send" theme="bs3h" loadmask="true">
		<@p.radio key="client" list="!['auto', 'smtp', 'java']"/>
		<@p.textfield key="host"/>
		<@p.textfield key="port"/>
		<@p.textfield key="username"/>
		<@p.textfield key="password"/>
		<@p.textfield key="connTimeout"/>
		<@p.textfield key="readTimeout"/>
		<@p.textfield key="dkimSelector"/>
		<@p.textarea key="dkimPrivateKey" rows="5" enterfire="form"/>
		<@p.textfield key="from" required="true"/>
		<@p.textfield key="to" required="true"/>
		<@p.textfield key="cc"/>
		<@p.textfield key="subject"/>
		<@p.htmleditor key="message" height="200"/>
		<@p.uploader
			key="files"
			size="30"
			multiple="true"
			uploadAction="%{b['super_path'] + '/tmp/uploads'}"
			uploadName="files"
			dnloadAction="%{b['super_path'] + '/tmp/download'}"
			dnloadName="file"
		/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-sendmail" label="#(btn-send)"/>
		</@p.div>
	</@p.form>

	</script>
</div>

</body>
</html>
