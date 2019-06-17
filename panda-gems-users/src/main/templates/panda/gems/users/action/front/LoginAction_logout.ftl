<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_login_logout">
	<div class="p-header">
		<h3><i class="fa fa-sign-out"></i> <@p.text name="title"/></h3>
	</div>

	<#include "/action-alert.ftl"/>
	<br/>

	<br/>
	<div class="p-tcenter">
		<#assign _t = text.getTextAsInt("redirect-timeout", 0) />
		<@p.a id="redirect" icon="home" href="${base}/"><@p.text name="btn-to-home"/> ... [<code id="time">${_t}</code>]</@p.a>
		<script type="text/javascript">
		<#if _t < 1>
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
	</div>
</div>

</body>
</html>
