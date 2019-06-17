<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_passwordreset_input">
	<div class="p-header">
		<h3><i class="fa fa-key"></i> <@p.text name="title"/></h3>
	</div>

	<div style="text-align: center">
	<div style="display: inline-block">
	<div style="text-align: left">
		<div class="p-well"><@p.text name="welcome"/></div>

		<#include "/action-alert.ftl"/>
		<br/>
	
		<@p.form id="pwdreset" action="./send" method="post" 
			cssClass="p-eform" focusme="true" theme="bs3">
			<@p.textfield key="email" size="30" maxlength="80" autocorrect="off" autocapitalize="off" required="true"/>
			<@p.div>
				<@p.submit icon="send" label="#(btn-send)"/>
			</@p.div>
		</@p.form>
	</div>
	</div>
	</div>
</div>

</body>
</html>
