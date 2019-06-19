<#if assist.superUser>
<#macro mitem ac ic tx>
	<#if a.canAccess(ac)>
		<@p.a cssClass="p-menu-item" action=ac>
			<i class="fa-4x ${a.getText(ic)}"></i>
			<div><@p.text name=tx/></div>
		</@p.a>
	</#if>
</#macro>
		<@mitem ac="/super/cmd"             ic="icon-super-cmdexec"      tx="navi-super-cmdexec"/>
		<@mitem ac="/super/el"              ic="icon-super-eleval"       tx="navi-super-eleval"/>
		<@mitem ac="/super/hash"            ic="icon-super-hash"         tx="navi-super-hash"/>
		<@mitem ac="/super/html2pdf"        ic="icon-super-html2pdf"     tx="navi-super-html2pdf"/>
		<@mitem ac="/super/sendmail"        ic="icon-super-sendmail"     tx="navi-super-sendmail"/>
	<#if !assist.gaeSupport>
		<@mitem ac="/super/sql"             ic="icon-super-sqlexec"      tx="navi-super-sqlexec"/>
	</#if>
		<@mitem ac="/super/sysdump"         ic="icon-super-sysdump"      tx="navi-super-sysdump"/>

		<@mitem ac="/super/dataexp"         ic="icon-super-dataexp"      tx="navi-super-dataexp"/>
		<@mitem ac="/super/dataimp"         ic="icon-super-dataimp"      tx="navi-super-dataimp"/>
		<@mitem ac="/super/datacpy"         ic="icon-super-datacpy"      tx="navi-super-datacpy"/>

		<@mitem ac="/super/crons"           ic="icon-super-crons"        tx="navi-super-crons"/>

		<@mitem ac="/super/fileitem/list"   ic="icon-super-fileitem"     tx="navi-super-fileitem"/>
		<@mitem ac="/super/property/list"   ic="icon-super-property"     tx="navi-super-property"/>
		<@mitem ac="/super/resource/list"   ic="icon-super-resource"     tx="navi-super-resource"/>
		<@mitem ac="/super/template/list"   ic="icon-super-template"     tx="navi-super-template"/>
</#if>
