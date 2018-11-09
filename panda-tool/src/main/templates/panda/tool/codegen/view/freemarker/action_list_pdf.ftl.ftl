<#include "common.ftl"/>
<@header/>
<@headinc step=""/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	${s}#include "/action-alert.ftl"/>

	<#include "list-print-columns.ftl"/>

	${s}@p.listview id="${action.name}_${gen.trimUiName(ui.name)}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-striped" header=" " footer=" "
	/>
</div>

<@footinc step=""/>
<@footer/>
