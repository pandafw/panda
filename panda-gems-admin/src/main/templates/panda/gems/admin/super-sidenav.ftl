<#macro navis ap ac ic tx>
	<#if a.canAccess(ac)>
		<li<#if path?starts_with(ap)> class="active"</#if>><@p.a action=ac><i class="${a.getText(ic)}"></i> <span><@p.text name=tx/></span></@p.a></li>
	</#if>
</#macro>
<div id="navi_super" class="panel panel-warning side-navi">
	<div class="panel-heading">
		<i class="fa fa-wrench"></i> <span><@p.text name="navi-super"/></span>
	</div>
	<div class="panel-body">
		<ul class="nav nav-stacked">
			<@navis ap="/super/cmd"       ac="/super/cmd"           ic="icon-super-cmdexec"    tx="navi-super-cmdexec"/>
			<@navis ap="/super/el"        ac="/super/el"            ic="icon-super-eleval"     tx="navi-super-eleval"/>
			<@navis ap="/super/hash"      ac="/super/hash"          ic="icon-super-hash"       tx="navi-super-hash"/>
			<@navis ap="/super/html2pdf"  ac="/super/html2pdf"      ic="icon-super-html2pdf"   tx="navi-super-html2pdf"/>
			<@navis ap="/super/sendmail"  ac="/super/sendmail"      ic="icon-super-sendmail"   tx="navi-super-sendmail"/>
		<#if !assist.gaeSupport>
			<@navis ap="/super/sql"       ac="/super/sql"           ic="icon-super-sqlexec"    tx="navi-super-sqlexec"/>
		</#if>
			<@navis ap="/super/sysdump"   ac="/super/sysdump"       ic="icon-super-sysdump"    tx="navi-super-sysdump"/>

			<@navis ap="/super/dataexp"   ac="/super/dataexp"       ic="icon-super-dataexp"    tx="navi-super-dataexp"/>
			<@navis ap="/super/dataimp"   ac="/super/dataimp"       ic="icon-super-dataimp"    tx="navi-super-dataimp"/>
			<@navis ap="/super/datacpy"   ac="/super/datacpy"       ic="icon-super-datacpy"    tx="navi-super-datacpy"/>

			<@navis ap="/super/crons"     ac="/super/crons"         ic="icon-super-crons"      tx="navi-super-crons"/>

			<@navis ap="/super/fileitem/" ac="/super/fileitem/list" ic="icon-super-fileitem"   tx="navi-super-fileitem"/>
			<@navis ap="/super/property/" ac="/super/property/list" ic="icon-super-property"   tx="navi-super-property"/>
			<@navis ap="/super/resource/" ac="/super/resource/list" ic="icon-super-resource"   tx="navi-super-resource"/>
			<@navis ap="/super/template/" ac="/super/template/list" ic="icon-super-template"   tx="navi-super-template"/>
		</ul>
	</div>
</div><!-- end of navi_super -->
