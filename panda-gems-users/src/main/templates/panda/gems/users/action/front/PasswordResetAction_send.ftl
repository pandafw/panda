<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_passwordreset_send">
	<div class="p-header">
		<h3><i class="fa fa-key"></i> <@p.text name="title"/></h3>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>

	<div class="p-tcenter">
		<@p.a icon="home" href="${base}/" label="#(navi-home)"/>
	</div>

</div>

</body>
</html>
