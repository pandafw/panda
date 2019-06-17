<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_register_success">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-success"/></li>
		</ol>
	</div>

	<#include "/action-alert.ftl"/>

	<br/>
	<div class="p-tcenter">
	<#if params.redirect?has_content>
		<#assign _t = text.getTextAsInt("redirect-timeout", 0) />
		<@p.a id="redirect" href="%{p.redirect}"><@p.text name="link-redirect"/> ... [<code id="time">${_t}</code>]</@p.a>
		<script type="text/javascript">
		<#if _t lt 1>
			location.href = document.getElementById("redirect").href;
		<#else>
			var seconds = ${_t}; 
		
			var timer = setInterval(function() {
				seconds--;
		
				document.getElementById("time").innerHTML = seconds;
				
				if (seconds < 1) {
					clearInterval(timer);
					location.href = document.getElementById("redirect").href;
				}
			}, 1000);
		</#if>
		</script>
	</#if>
		<@p.a icon="home" href="${base}/" label="#(navi-home)"/>
	</div>

</div>

</body>
</html>
