<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	${s}#include "/action-alert.ftl"/>

	<#include "list-print-columns.ftl"/>


	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-striped" autosize="false" script="false" header=" " footer=" "
	/>
	<@safeinc step=""/>
</div>

<@footer/>
