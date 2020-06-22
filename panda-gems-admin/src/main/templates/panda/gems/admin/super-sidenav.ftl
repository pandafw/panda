<#macro snavi ap ac ic tx>
	<#if a.canAccess(ac)>
		<li class="nav-item<#if path?starts_with(ap)> active</#if>"><@p.a cssClass="nav-link" action=ac><i class="${a.getText(ic)}"></i> <span><@p.text name=tx/></span></@p.a></li>
	</#if>
</#macro>
<div id="navi_super" class="panel panel-danger card side-navi">
	<div class="panel-heading card-header bg-danger text-white">
		<i class="fa fa-wrench"></i> <span><@p.text name="navi-super"/></span>
	</div>
	<div class="panel-body card-body">
		<ul class="nav nav-stacked flex-column">
			<@snavi ap="/super/cmd"       ac="/super/cmd"           ic="icon-super-cmdexec"    tx="navi-super-cmdexec"/>
			<@snavi ap="/super/el"        ac="/super/el"            ic="icon-super-eleval"     tx="navi-super-eleval"/>
			<@snavi ap="/super/hash"      ac="/super/hash"          ic="icon-super-hash"       tx="navi-super-hash"/>
			<@snavi ap="/super/html2pdf"  ac="/super/html2pdf"      ic="icon-super-html2pdf"   tx="navi-super-html2pdf"/>
			<@snavi ap="/super/sendmail"  ac="/super/sendmail"      ic="icon-super-sendmail"   tx="navi-super-sendmail"/>
		<#if !assist.gaeSupport>
			<@snavi ap="/super/sql"       ac="/super/sql"           ic="icon-super-sqlexec"    tx="navi-super-sqlexec"/>
		</#if>
			<@snavi ap="/super/sysdump"   ac="/super/sysdump"       ic="icon-super-sysdump"    tx="navi-super-sysdump"/>

			<@snavi ap="/super/dataexp"   ac="/super/dataexp"       ic="icon-super-dataexp"    tx="navi-super-dataexp"/>
			<@snavi ap="/super/dataimp"   ac="/super/dataimp"       ic="icon-super-dataimp"    tx="navi-super-dataimp"/>
			<@snavi ap="/super/datacpy"   ac="/super/datacpy"       ic="icon-super-datacpy"    tx="navi-super-datacpy"/>

			<@snavi ap="/super/crons"     ac="/super/crons"         ic="icon-super-crons"      tx="navi-super-crons"/>

			<@snavi ap="/super/property/" ac="/super/property/list" ic="icon-super-property"   tx="navi-super-property"/>
			<@snavi ap="/super/resource/" ac="/super/resource/list" ic="icon-super-resource"   tx="navi-super-resource"/>
			<@snavi ap="/super/template/" ac="/super/template/list" ic="icon-super-template"   tx="navi-super-template"/>
		</ul>
	</div>
</div><!-- end of navi_super -->
