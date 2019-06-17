<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_profile_success">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-success"/></li>
		</ol>
	</div>

	<#include "/action-alert.ftl"/>

	<@p.form id="profile" method="post" cssClass="p-vform" focusme="true" theme="bs3h">
		<@p.viewfield key="name"  label="#(a.t.name)"/>
		<@p.viewfield key="email" label="#(a.t.email)"/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="user" onclick="return toHome();" label="#(navi-home)"/>
		</@p.div>
	</@p.form>

	<script type="text/javascript">
		function toHome() {
			location.href = "${base}";
			return false; 
		}
	</script>
</div>

</body>
</html>
