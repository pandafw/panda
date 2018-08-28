<div class="a-debug">
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
		.a-debug td>div {
			max-height: 300px;
			overflow-y: auto;
		}
	</style>

	<ul class="nav nav-pills">
		<li class="active"><a href="#d_params" data-toggle="pill">Parameters</a></li>
		<li><a href="#d_headers" data-toggle="pill">Headers</a></li>
		<li><a href="#d_cookies" data-toggle="pill">Cookies</a></li>
		<li><a href="#d_request" data-toggle="pill">Request</a></li>
		<li><a href="#d_session" data-toggle="pill">Session</a></li>
		<li><a href="#d_servlet" data-toggle="pill">Servlet</a></li>
		<li><a href="#d_system" data-toggle="pill">System</a></li>
		<li><a href="#d_enviroment" data-toggle="pill">Envrioment</a></li>
		<li><a href="#d_settings" data-toggle="pill">Settings</a></li>
	</ul>
	<br>

<div class="tab-content">
<div id="d_params" class="tab-pane active">
	<div class="panel panel-success">
		<div class="panel-heading">REQUEST PARAMETERS:</div>
		<table class="table table-striped">
		<thead>
			<tr><th width="200">Name</th><th>Values</th></tr>
		</thead>
		<tbody>
<#list reqParams?keys?sort as k>
			<tr><td>${k!''?html}</td><td>${assist.escapePhtml(reqParams[k]!)}</td></tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>
</div>

<div id="d_headers" class="tab-pane">
	<div class="panel panel-success">
		<div class="panel-heading">REQUEST HTTP HEADER:</div>
		<table class="table table-striped">
		<thead>
			<tr><th width="200">Name</th><th>Value</th></tr>
		</thead>
		<tbody>
<#list reqHeader?keys?sort as k> 
			<tr><td>${k!''?html}</td><td><#list reqHeader[k]![] as v>${v!''?html}<br/></#list></td></tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>
</div>

<div id="d_cookies" class="tab-pane">
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
				<td>${(_c.name)!''?html}</td>
				<td>${(_c.value)!''?html}</td>
				<td>${(_c.domain)!''?html}</td>
				<td>${(_c.path)!''?html}</td>
				<td>${(_c.maxAge)!''?html}</td>
				<td>${(_c.secure)?string?html}</td>
				<td>${(_c.version)!''?html}</td>
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
</div>

<div id="d_request" class="tab-pane">
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
			<tr><td>AuthType</td><td>${(request.authType)!''?html}</td></tr>
			<tr><td>CharacterEncoding</td><td>${(request.characterEncoding)!''?html}</td></tr>
			<tr><td>ContentLength</td><td>${(request.contentLength)?html}</td></tr>
			<tr><td>ContentType</td><td>${(request.contentType)!''?html}</td></tr>
			<tr><td>ContextPath</td><td>${(request.contextPath)!''?html}</td></tr>
			<tr><td>isSecure</td><td>${(request.secure?string)!''?html}</td></tr>
			<tr><td>isRequestedSessionIdFromCookie</td><td>${(request.requestedSessionIdFromCookie?string)!''?html}</td></tr>
			<tr><td>isRequestedSessionIdFromURL</td><td>${(request.requestedSessionIdFromURL?string)!''?html}</td></tr>
			<tr><td>isRequestedSessionIdValid</td><td>${(request.requestedSessionIdValid?string)!''?html}</td></tr>
			<tr><td>Locale</td><td>${(request.locale)!''?html}</td></tr>
			<tr><td>LocalAddr</td><td>${(request.localAddr)!''?html}</td></tr>
			<tr><td>LocalName</td><td>${(request.localName)!''?html}</td></tr>
			<tr><td>LocalPort</td><td>${(request.localPort)?html}</td></tr>
			<tr><td>Method</td><td>${(request.method)!''?html}</td></tr>
			<tr><td>PathInfo</td><td>${(request.pathInfo)!''?html}</td></tr>
			<tr><td>PathTranslated</td><td>${(request.pathTranslated)!''?html}</td></tr>
			<tr><td>Protocol</td><td>${(request.protocol)!''?html}</td></tr>
			<tr><td>QueryString</td><td>${(request.queryString)!''?html}</td></tr>
			<tr><td>RemoteAddr</td><td>${(request.remoteAddr)!''?html}</td></tr>
			<tr><td>RemotePort</td><td>${(request.remotePort)?html}</td></tr>
			<tr><td>RequestedSessionId</td><td>${(request.requestedSessionId)!''?html}</td></tr>
			<tr><td>RequestURI</td><td>${(request.requestURI)!''?html}</td></tr>
			<tr><td>RequestURL</td><td>${(request.requestURL)!''?html}</td></tr>
			<tr><td>Scheme</td><td>${(request.scheme)!''?html}</td></tr>
			<tr><td>ServerName</td><td>${(request.serverName)!''?html}</td></tr>
			<tr><td>ServerPort</td><td>${(request.serverPort?string)!''?html}</td></tr>
			<tr><td>ServletPath</td><td>${(request.servletPath)!''?html}</td></tr>
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
<#list req?keys?sort as k>
			<tr><td>${k!''?html}</td>
				<td><#if req[k]??><i>${(req[k].class.name)!''?html}</i>
					<div>${assist.escapePhtml(req[k]!)}</div>
				</#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>
</div>

<div id="d_session" class="tab-pane">
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
<#list ses?keys?sort as k>
			<tr><td>${k?html}</td>
				<td><#if ses[k]??><i>${(ses[k].class.name)!''?html}</i>
					</div>${assist.escapePhtml(ses[k]!)}</div>
				</#if></td>
			</tr>
</#list>
<#assign _sic = assist.findValue("'panda.mvc.ioc.SessionIocContext'@dump(session)")/>
<#if _sic??>
			<tr><td>SessionIocContext</td>
				<td><div>${_sic?html}<div></td>
			</tr>
</#if>
		</tbody>
		</table>
	</div>
	<br/>
</div>

<div id="d_servlet" class="tab-pane">
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
<#list app?keys?sort as k>
			<tr><td>${k?html}</td>
				<td><#if app[k]??><i>${(app[k].class.name)!''?html}</i>
					<div>${assist.escapePhtml(app[k]!)}</div>
				</#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>
</div>

<div id="d_system" class="tab-pane">
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
<#list _sps?keys?sort as k>
			<tr>
				<td>${(k!'')?html}</td>
				<td><#if _sps[k]??><div>${(_sps[k]?string)!''?html}</div></#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
	<br/>
</div>

<div id="d_enviroment" class="tab-pane">
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
<#list _ses?keys?sort as k>
			<tr>
				<td>${k?html}</td>
				<td><#if _ses[k]??><div>${(_ses[k]?string)!''?html}</div></#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
</div>

<div id="d_settings" class="tab-pane">
	<div class="panel panel-success">
		<div class="panel-heading">Settings</div>
		<table class="table table-striped">
		<thead>
			<tr>
				<th class="thn">Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
<#assign _ses = assist.findValue("'java.lang.System'@getenv()")/>
<#list s?keys?sort as k>
			<tr>
				<td>${k?html}</td>
				<td><#if s[k]??><div>${(s[k]?string)!''?html}</div></#if></td>
			</tr>
</#list>
		</tbody>
		</table>
	</div>
</div>

</div>
</div>
