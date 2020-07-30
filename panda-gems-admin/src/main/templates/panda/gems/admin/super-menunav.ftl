<#macro sitem ac ic tx>
	<#if a.canAccess(ac)>
		<@p.a cssClass="p-menu-item" action=ac>
			<i class="fa-4x ${a.getText(ic)}"></i>
			<div><@p.text name=tx/></div>
		</@p.a>
	</#if>
</#macro>

		<@sitem ac="/super/crons"           ic="icon-super-crons"        tx="navi-super-crons"/>

		<@sitem ac="/super/dataexp"         ic="icon-super-dataexp"      tx="navi-super-dataexp"/>
		<@sitem ac="/super/dataimp"         ic="icon-super-dataimp"      tx="navi-super-dataimp"/>
		<@sitem ac="/super/datacpy"         ic="icon-super-datacpy"      tx="navi-super-datacpy"/>

		<@sitem ac="/super/property/list"   ic="icon-super-property"     tx="navi-super-property"/>
		<@sitem ac="/super/resource/list"   ic="icon-super-resource"     tx="navi-super-resource"/>
		<@sitem ac="/super/template/list"   ic="icon-super-template"     tx="navi-super-template"/>

		<@sitem ac="/super/pwhash"          ic="icon-super-pwhash"       tx="navi-super-pwhash"/>
		<@sitem ac="/super/hash"            ic="icon-super-hash"         tx="navi-super-hash"/>
		<@sitem ac="/super/html2pdf"        ic="icon-super-html2pdf"     tx="navi-super-html2pdf"/>
		<@sitem ac="/super/sendmail"        ic="icon-super-sendmail"     tx="navi-super-sendmail"/>

		<@sitem ac="/super/sysdump"         ic="icon-super-sysdump"      tx="navi-super-sysdump"/>
		<@sitem ac="/super/el"              ic="icon-super-eleval"       tx="navi-super-eleval"/>
		<@sitem ac="/super/shell"           ic="icon-super-shell"        tx="navi-super-shell"/>
	<#if !assist.gaeSupport>
		<@sitem ac="/super/sql"             ic="icon-super-sqlexec"      tx="navi-super-sqlexec"/>
	</#if>

