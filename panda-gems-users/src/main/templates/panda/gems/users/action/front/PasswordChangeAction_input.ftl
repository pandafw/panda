<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_passwordchange_input">
	<div class="p-header">
		<h3><i class="fa fa-key"></i> <@p.text name="title"/></h3>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>
	
	<@p.form id="pwdchange" action="./update" method="post" cssClass="p-eform" focusme="true" theme="bs3h">
		<@p.password key="opwd" showPassword="true" size="30" maxlength="16" required="true" tooltip="#(opwd-tip')"/>
		<@p.password key="npwd" showPassword="true" size="30" maxlength="16" required="true" tooltip="#(npwd-tip')"/>
		<@p.password key="cpwd" showPassword="true" size="30" maxlength="16" required="true" tooltip="#(cpwd-tip')"/>
		<@p.div>
			<@p.submit icon="change" label="#(btn-change)"/>
		</@p.div>
	</@p.form>
</div>

</body>
</html>
