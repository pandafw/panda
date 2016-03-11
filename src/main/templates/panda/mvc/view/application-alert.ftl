<#if applicationErrors?has_content
	|| applicationWarnings?has_content
	|| applicationConfirms?has_content
	|| applicationMessages?has_content>
<div class="alert alert-dismissable fade in<#rt/>
<#if applicationErrors?has_content>
 alert-danger<#rt/>
<#elseif applicationWarnings?has_content>
 alert-warning<#rt/>
<#elseif applicationConfirms?has_content>
 alert-info<#rt/>
<#elseif applicationMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
<#if applicationErrors?has_content>
	<ul class="fa-ul p-application-errors">
	<#list applicationErrors as k>
		<li class="p-application-error" msgId="${k}"><i class="fa-li fa fa-exclamation-circle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
<#if applicationWarnings?has_content>
	<ul class="fa-ul p-application-warnings">
	<#list applicationWarnings as k>
		<li class="p-application-warning" msgId="${k}"><i class="fa-li fa fa-exclamation-triangle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
<#if applicationConfirms?has_content>
	<ul class="fa-ul p-application-confirms">
	<#list applicationConfirms as k>
		<li class="p-application-confirm" msgId="${k}"><i class="fa-li fa fa-question-circle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
<#if applicationMessages?has_content>
	<ul class="fa-ul p-application-messages">
	<#list applicationMessages as k>
		<li class="p-application-message" msgId="${k}"><i class="fa-li fa fa-info-circle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
</div>
</#if>
