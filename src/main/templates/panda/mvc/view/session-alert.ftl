<#if sessionErrors?has_content
	|| sessionWarnings?has_content
	|| sessionConfirms?has_content
	|| sessionMessages?has_content>
<div class="alert alert-dismissable<#rt/>
<#if sessionErrors?has_content>
 alert-danger<#rt/>
<#elseif sessionWarnings?has_content>
 alert-warning<#rt/>
<#elseif sessionConfirms?has_content>
 alert-info<#rt/>
<#elseif sessionMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
<#if sessionErrors?has_content>
	<ul class="fa-ul p-session-errors">
	<#list sessionErrors as k>
		<li class="p-session-error" msgId="${k}"><i class="fa-li fa fa-exclamation-circle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
<#if sessionWarnings?has_content>
	<ul class="fa-ul p-session-warnings">
	<#list sessionWarnings as k>
		<li class="p-session-warning" msgId="${k}"><i class="fa-li fa fa-exclamation-triangle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
<#if sessionConfirms?has_content>
	<ul class="fa-ul p-session-confirms">
	<#list sessionConfirms as k>
		<li class="p-session-confirm" msgId="${k}"><i class="fa-li fa fa-question-circle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
<#if sessionMessages?has_content>
	<ul class="fa-ul p-session-messages">
	<#list sessionMessages as k>
		<li class="p-session-message" msgId="${k}"><i class="fa-li fa fa-info-circle"></i><@p.text name=k/></li>
	</#list>
	</ul>
</#if>
</div>
</#if>
