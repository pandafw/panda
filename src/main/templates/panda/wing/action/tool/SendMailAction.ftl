<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section a-sendmail">
	<div class="p-header">
		<h3><i class="fa fa-envelope-o"></i> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<@p.form id="sendmail" method="post" action="~/send" theme="bs3h" loadmask="true">
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
		<@p.textarea key="message" rows="10" enterfire="form"/>
		<@p.checkbox key="html"/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-email" label="#(button-send)"/>
		</@p.div>
	</@p.form>

	</script>
</div>

</body>
</html>
