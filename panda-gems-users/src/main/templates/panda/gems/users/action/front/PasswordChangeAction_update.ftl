<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_passwordchange_update">
	<div class="p-header">
		<h3><i class="fa fa-key"></i> <@p.text name="title"/></h3>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>
	<div class="p-tcenter">
		<@p.a cssClass="btn btn-default" icon="user" action="/" label="#(navi-home)"/>
	</div>
</div>

</body>
</html>
