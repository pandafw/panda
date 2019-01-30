<html>
<head>
	<title><@p.text name="title-expo_csv"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-expo_csv"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>
<#assign _well = a.getText("well-expo_csv", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>

	<#include "/action-alert.ftl"/>
</div>

</body>
</html>
