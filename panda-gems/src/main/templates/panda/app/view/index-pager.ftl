<div class="index-pager">
	<@p.pager pager="%{p.p}"
		linkHref=_index_pager_link_ 
		onLinkClick="site.page_loading()" 
		onLimitChange="site.page_limit(this.value)"/>
</div>
