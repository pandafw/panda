<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
		</ol>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>
	
	<@p.form id="profile" action="./confirm" method="post" cssClass="p-eform" focusme="true" theme="bs3h">
		<@p.textfield key="name" value="%{r.name}" size="40" maxlength="32" required="true" label="#(a.t.name)"/>
		<@p.textfield key="email" value="%{r.email}" size="40" maxlength="100" required="true" label="#(a.t.email)"/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-confirm" label="#(btn-confirm)"/>
		</@p.div>
	</@p.form>
</div>

</body>
</html>
