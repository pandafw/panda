<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_login_input">
	<div class="p-header">
		<h3><i class="fa fa-sign-in"></i> <@p.text name="title"/></h3>
	</div>

	<div style="text-align: center">
	<div style="display: inline-block">
	<div style="text-align: left">
		<h4 class="caption"><@p.text name="text-unregistered"/></h4>
		<br/>
		<div class="indent2">
			<@p.url var='_ri' action='/user/register/input'>
				<@p.param name='redirect' value='redirect'/>
			</@p.url>
			<@p.a icon="pencil-square-o" href="${v._ri}" label="#(link-register)"/>
		</div>
		
		<br/><br/>
		<div class="hr"></div>
	
		<h4 class="caption"><@p.text name="text-registered"/></h4>
		<br/>
	
		<div class="indent2">
			<h4><@p.text name="text-input"/></h4>
	
			<#include "/action-alert.ftl"/>
			<br/>
	
			<@p.form id="login" action="./login" method="post" 
				cssClass="p-eform" focusme="true" theme="bs3" loadmask="false"
				onsubmit="return onLoginSubmit();">
				<@p.hidden name="redirect"/>
				<@p.textfield key="username" size="30" maxlength="80" autocorrect="off" autocapitalize="off" required="true"/>
				<@p.password key="password" size="30" maxlength="16" autocorrect="off" autocapitalize="off" required="true"/>
				<@p.checkbox name="autosave" fieldLabel="#(p.autosave)" fieldValue="true"/>
				<@p.div>
					<@p.submit icon="sign-in" label="#(btn-login)"/>
				</@p.div>
			</@p.form>
			
			<br/>
			<@p.a icon="next" action="/user/password/reset/" label="#(text-password-forgot)"/>
		</div>
		<br/>
	</div>
	</div>
	</div>

	<br/>
</div>

<script type="text/javascript">
	function onLoginSubmit() {
		$("#login").loadmask('<@p.text name="btn-login" escape="js"/>...');
	}
</script>

</body>
</html>
