<#if actionErrors?has_content
	|| actionWarnings?has_content
	|| actionConfirms?has_content
	|| actionMessages?has_content
	|| paramErrors?has_content>
<div class="alert alert-dismissable<#rt/>
<#if actionErrors?has_content || paramErrors?has_content>
 alert-danger<#rt/>
<#elseif actionWarnings?has_content>
 alert-warning<#rt/>
<#elseif actionConfirms?has_content>
 alert-info<#rt/>
<#elseif actionMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
<@p.actionerror/>
<#if paramErrors?has_content>
	<#if !(actionErrors?has_content)>
	<ul class="p-action-errors fa-ul">
		<li><i class="fa-li fa fa-exclamation-circle"></i>
			<@p.text name="error-input"/>
			<@p.a cssClass="p-action-errors-caret" sicon="caret-down" href="#" onclick="return $.palert.toggleFieldErrors(this);"><@p.text name="error-input-detail"/></@p.a>
		</li>
	</ul>
	</#if>
	<@p.fielderror cssStyle="display:none" label="true"/>
</#if>
<@p.actionwarning/>
<@p.actionmessage/>
<@p.actionconfirm/>
</div>
</#if>