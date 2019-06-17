<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_passwordreset_reset">
	<div class="p-header">
		<h3><i class="fa fa-key"></i> <@p.text name="title"/></h3>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>
	
	<div class="p-tcenter">
	<#if !(result??)>
		<@p.a icon="back" action="./" label="#(password-reset)"/>
	</#if>
		<@p.a icon="user" action="/" label="#(navi-home)"/>
	</div>
</div>

</body>
</html>
