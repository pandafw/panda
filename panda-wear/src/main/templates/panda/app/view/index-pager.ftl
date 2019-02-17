<div class="index-pager">
	<@p.pager pager="%{p.p}"
		linkHref=_index_pager_link_ 
		onLinkClick="panda.loading()" 
		onLimitChange="panda.page_limit(this.value)"/>
</div>
