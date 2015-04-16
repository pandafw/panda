<div class="index-pager">
	<@p.pager pager="%{p.p}"
		linkSize="${assist.mobileLayout?string('1', '3')}" 
		linkStyle="${assist.mobileLayout?string('hfp#nl', 'hp1#n')}" 
		linkHref=_index_pager_link_ 
		onLinkClick="s_loadmask()" 
		onLimitChange="sl_limit('index', this)"/>
</div>
