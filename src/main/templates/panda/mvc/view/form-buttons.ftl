<#macro form_buttons buttons>
	<#if buttons?has_content>
<@p.div cssClass="p-buttons">
		<#list buttons as b>
			<#if "reset" == (b.type!'')>
	<@p.reset cssClass="${b.css!}" btype="${b.btype!}" icon="${b.icon!}" sicon="${b.sicon!}" action="${b.action!}" onclick="${b.onclick!}" label="${b.text!}"/>
			<#elseif "anchor" == (b.type!'')>
	<@p.b cssClass="${b.css!}" btype="${b.btype!}" icon="${b.icon!}" sicon="${b.sicon!}" action="${b.action!}" href="${b.href!}" target="${b.target!}" onclick="${b.onclick!}" label="${b.text!}"/>
			<#else>
	<@p.submit cssClass="${b.css!}" btype="${b.btype!}" icon="${b.icon!}" sicon="${b.sicon!}" action="${b.action!}" onclick="${b.onclick!}" label="${b.text!}"/>
			</#if>
		</#list>
</@p.div>
	</#if>
</#macro>
