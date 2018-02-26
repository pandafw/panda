<html>
<head>
<title><@p.text name="title"/></title>
</head>

<body>
<style type="text/css">
	.a-debug th {
		white-space: nowrap;
	}
	.a-debug th.thn {
		width: 200px;
	}
	.a-debug td {
		word-break: break-all;
	}
</style>
<div class="p-section a-debug">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>

	<#include "/panda/mvc/view/action-alert-debug.ftl"/>

	<div class="panel panel-success">
		<div class="panel-heading">REQUEST HTTP HEADER:</div>
		<table class="table table-striped">
		<thead>
			<tr><th width="200">Name</th><th>Value</th></tr>
		</thead>
		<tbody>
<#list reqHeader?keys as k> 
			<tr><td>${k?html}</td><td><#list reqHeader[k] as v>${v?html}<br/></#list></td></tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">REQUEST HTTP COOKIES:</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
				<th>Domain</th>
				<th>Path</th>
				<th>MaxAge</th>
				<th>Secure</th>
				<th>Version</th>
			</tr>
		</thead>
		<tbody>
<#list request.cookies![] as _c>
			<tr>
				<td>${_c.name?default("")?html}</td>
				<td>${_c.value?default("")?html}</td>
				<td>${_c.domain?default("")?html}</td>
				<td>${_c.path?default("")?html}</td>
				<td>${_c.maxAge?default("")?html}</td>
				<td>${_c.secure?string?html}</td>
				<td>${_c.version?default("")?html}</td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">CLIENT COOKIES:</div>
		<table class="table table-striped">
		<tbody>
		<tr><td><script type="text/javascript">
			if (document.cookie) {
				document.write(document.cookie.replace("; ", "<br/>"));
			}
			</script></td>
		</tr>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">REQUEST INFORMATION:</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
			<tr><td>AuthType</td><td>${request.authType?default("")?html}</td></tr>
			<tr><td>CharacterEncoding</td><td>${request.characterEncoding?default("")?html}</td></tr>
			<tr><td>ContentLength</td><td>${request.contentLength?html}</td></tr>
			<tr><td>ContentType</td><td>${request.contentType?default("")?html}</td></tr>
			<tr><td>ContextPath</td><td>${request.contextPath?default("")?html}</td></tr>
			<tr><td>isSecure</td><td>${request.secure?string?html}</td></tr>
			<tr><td>isRequestedSessionIdFromCookie</td><td>${request.requestedSessionIdFromCookie?string?html}</td></tr>
			<tr><td>isRequestedSessionIdFromURL</td><td>${request.requestedSessionIdFromURL?string?html}</td></tr>
			<tr><td>isRequestedSessionIdValid</td><td>${request.requestedSessionIdValid?string?html}</td></tr>
			<tr><td>Locale</td><td>${request.locale?default("")?html}</td></tr>
			<tr><td>LocalAddr</td><td>${request.localAddr?default("")?html}</td></tr>
			<tr><td>LocalName</td><td>${request.localName?default("")?html}</td></tr>
			<tr><td>LocalPort</td><td>${request.localPort?html}</td></tr>
			<tr><td>Method</td><td>${request.method?default("")?html}</td></tr>
			<tr><td>PathInfo</td><td>${request.pathInfo?default("")?html}</td></tr>
			<tr><td>PathTranslated</td><td>${request.pathTranslated?default("")?html}</td></tr>
			<tr><td>Protocol</td><td>${request.protocol?default("")?html}</td></tr>
			<tr><td>QueryString</td><td>${request.queryString?default("")?html}</td></tr>
			<tr><td>RemoteAddr</td><td>${request.remoteAddr?default("")?html}</td></tr>
			<tr><td>RemotePort</td><td>${request.remotePort?html}</td></tr>
			<tr><td>RequestedSessionId</td><td>${request.requestedSessionId?default("")?html}</td></tr>
			<tr><td>RequestURI</td><td>${request.requestURI?default("")?html}</td></tr>
			<tr><td>RequestURL</td><td>${request.requestURL?default("")?html}</td></tr>
			<tr><td>Scheme</td><td>${request.scheme?default("")?html}</td></tr>
			<tr><td>ServerName</td><td>${request.serverName?default("")?html}</td></tr>
			<tr><td>ServerPort</td><td>${request.serverPort?html}</td></tr>
			<tr><td>ServletPath</td><td>${request.servletPath?default("")?html}</td></tr>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">REQUEST PARAMETERS:</div>
		<table class="table table-striped">
		<thead>
			<tr><th width="200">Name</th><th>Values</th></tr>
		</thead>
		<tbody>
<#list reqParams?keys as k>
			<tr><td>${k?html}</td><td>${reqParams[k].toString()?html}</td></tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">REQUEST ATTRIBUTES:</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
<#list req?keys as k>
			<tr><td>${k?html}</td>
				<td><#if req[k]??>${req[k].class.name?html}<br/>
					${assist.escapePhtml(req[k].toString())}
				</#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">SESSION: [ <#if session??>${session.id}</#if> ]</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
<#list ses?keys as k>
			<tr><td>${k?html}</td>
				<td><#if ses[k]??>${ses[k].class.name?html}<br/>
					${assist.escapePhtml(ses[k].toString())}
				</#if></td>
			</tr>
</#list>
<#assign _sic = assist.findValue("'panda.mvc.ioc.SessionIocContext'@dump(session)")/>
<#if _sic??>
			<tr><td>SessionIocContext</td>
				<td>${_sic?html}</td>
			</tr>
</#if>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">APPLICATION: [ ${servlet.servletContextName?html} ] ( ${servlet.serverInfo?html} )</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
<#list app?keys as k>
			<tr><td>${k?html}</td>
				<td><#if app[k]??>${app[k].class.name?html}<br/></#if>
					${assist.escapePhtml(app[k].toString())}
				</td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">System.Properties</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
<#assign _sps = assist.findValue("'java.lang.System'@getProperties()")/>
<#list _sps?keys as k>
			<tr>
				<td>${k?html}</td>
				<td><#if _sps[k]??>${_sps[k].toString()?html}</#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>

	<div class="panel panel-success">
		<div class="panel-heading">System.Environment</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
<#assign _ses = assist.findValue("'java.lang.System'@getenv()")/>
<#list _ses?keys as k>
			<tr>
				<td>${k?html}</td>
				<td><#if _ses[k]??>${_ses[k].toString()?html}</#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>
</div>
</body>
</html>
