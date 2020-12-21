<#assign _sc = (assist.servletErrorCode!500) />
<#assign _tt = action.getText("servlet-error-title-" + _sc, "")/>
<#if !(_tt?has_content)><#assign _tt = assist.getServletErrorReason(_sc)/></#if>
<#assign _tt = _sc + " " + _tt/>
<#assign _msg = action.getText("servlet-error-message-" + _sc, "")/>
<#if !(_msg?has_content)><#assign _msg = action.getText("servlet-error-message", "", [_sc])/></#if>
<#if !(_msg?has_content)><#assign _msg = assist.servletErrorMessage!''/></#if>
<html>
<head>
<title>${_tt}</title>
</head>
<body>
<div class="p-section">
	<div class="p-header">
		<h3 class="p-error"><i class="fa fa-exclamation-circle"></i>
			${_tt}
		</h3>
	</div>
	<div class="alert alert-danger">
		<ul class="fa-ul p-servlet-errors">
			<li class="p-servlet-error"><i class="fa-li fa fa-exclamation-circle"></i> ${_msg}<br/>
<#assign _url = assist.escapeHtml(assist.requestLink)/>${action.getText('servlet-error-request-url', _url, _url)}
			</li>
		</ul>
	</div>
<#if assist.servletException??>
	<#if appDebug>
	<div class="alert alert-warning p-exception">
		<h5>${action.getText('servlet-error-exception')}</h5>
		<div>${assist.escapePhtml(assist.servletExceptionStackTrace)}</div>
	</div>
	</#if>
</#if>
</div>
</body>
</html>
