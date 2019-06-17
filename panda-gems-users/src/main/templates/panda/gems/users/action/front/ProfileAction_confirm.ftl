<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_profile_confirm">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-confirm"/></li>
		</ol>
	</div>

	<#include "/action-alert.ftl"/>

	<@p.form id="profile" action="./execute" method="post" cssClass="p-vform" focusme="true" theme="bs3h">
		<@p.viewfield key="name"  label="#(a.t.name)"/>
		<@p.viewfield key="email" label="#(a.t.email)"/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-save" label="#(btn-save)"/>
			<@p.submit icon="icon-back" action="./input" label="#(btn-back)"/>
		</@p.div>
	</@p.form>
</div>

</body>
</html>
