<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_register_input">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
		</ol>
	</div>

	<div style="text-align: center"><div style="display: inline-block"><div style="text-align: left">
		<div class="p-well"><@p.text name="welcome"/></div>

		<#include "/action-alert.ftl"/>
		<br/>
		
		<@p.form id="register" action="./confirm" method="post" focusme="true" theme="bs3h">
			<@p.hidden name="redirect"/>
			<@p.textfield key="name" size="40" maxlength="32" required="true" label="#(a.t.name)"/>
			<@p.textfield key="email" size="40" maxlength="100" required="true" label="#(a.t.email)"/>
			<@p.password key="password" showPassword="true" size="20" maxlength="12" required="true" label="#(a.t.password)"/>
			<@p.div cssClass="p-buttons">
				<@p.submit icon="icon-confirm" label="#(btn-confirm)"/>
			</@p.div>
		</@p.form>
	</div></div></div>
</div>
</body>
</html>
