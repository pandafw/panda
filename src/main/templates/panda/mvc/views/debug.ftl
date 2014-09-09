<html>
<head>
</head>

<body>

Parameters: <#if Parameters??>${Parameters.class!'class'}</#if><br/>
Request: <#if Request??>${Request.class!'class'}</#if><br/>
request: <#if request??>${request.class!'class'}</#if><br/>
Response: <#if Response??>${Response.class!'class'}</#if><br/>
response: <#if response??>${response.class!'class'}</#if><br/>
Application: <#if Application??>${Application.class!'class'}</#if><br/>
application: <#if application??>${application.class!'class'}</#if><br/>
Session: <#if Session??>${Session.class!'class'}</#if><br/>
session: <#if session??>${session.class!'class'}</#if><br/>
Action: <#if Action??>${Action.class!'class'}</#if><br/>
action: <#if action??>${action.class!'class'}</#if><br/>
Stack: <#if Stack??>${Stack.class!'class'}</#if><br/>
stack: <#if stack??>${stack.class!'class'}</#if><br/>
Base: <#if Base??>${Base.class!'class'}</#if><br/>
base: <#if base??>${base.class!'class'}</#if><br/>
Ognl: <#if Ognl??>${Ognl.class!'class'}</#if><br/>
ognl: <#if ognl??>${ognl.class!'class'}</#if><br/>

<#if Parameters?? && Parameters.error??>${error}</#if>

</body>
</html>
