<#assign status_code = (req["javax.servlet.error.status_code"]!500) />
<#assign title = action.getText("servlet-error-title-" + status_code, "")/>
<#if !(title?has_content)><#assign title = assist.getServletErrorReason(status_code)/></#if>
<#assign title = status_code + " " + title/>
<#assign message = action.getText("servlet-error-message-" + status_code, "")/>
<#if !(message?has_content)><#assign message = action.getText("servlet-error-message", "", [status_code])/></#if>
<#if !(message?has_content)><#assign message = req["javax.servlet-error-message"]!''/></#if>
<html>
<head>
<title>${title}</title>
</head>
<body>
<div class="p-section">
	<div class="p-header">
		<h3 class="p-error"><i class="fa fa-exclamation-circle"></i>
			${title}
		</h3>
	</div>
	<div class="alert alert-danger">
		<ul class="fa-ul p-servlet-errors">
			<li class="p-servlet-error"><i class="fa-li fa fa-exclamation-circle"></i> ${message}<br/>
					${action.getText('servlet-error-request-url', assist.requestLink, assist.requestLink )} 
			</li>
		</ul>
	</div>
<#if req['javax.servlet.error.exception']??>
	<#if assist.debugEnabled>
	<div class="alert alert-warning p-exception">
		<h5>${action.getText('servlet-error-exception')}</h5>
		<div>${assist.escapePhtml(assist.servletExceptionStackTrace)}</div>
	</div>
	</#if>
</#if>
</div>
</body>
</html>
