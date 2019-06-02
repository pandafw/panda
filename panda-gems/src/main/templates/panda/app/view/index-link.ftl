<@p.url var="_url" includeParams="get" suppressParam="empty">
	<@p.param name="p.s" value="*"/>
	<@p.param name="s.c"/>
	<@p.param name="s.d"/>
</@p.url>
<#assign _index_pager_link_ = v._url.replace('p.s=*', 'p.s=!{start}')/>
