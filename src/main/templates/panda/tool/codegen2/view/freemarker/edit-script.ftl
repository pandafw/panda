		<script type="text/javascript"><!--
		<#list ui.displayFieldList as f>
			<#assign _popup = {}/>
			<#if f.editTag??>
				<#list f.editTag.paramList as p>
					<#if p.name == "~popup">
						<#assign _popup = p.values/>
						<#break/>
					</#if>
				</#list>
			</#if>
			<#if _popup?has_content>
			function ${action.name}_${f.name}_onPopupCallback(sd) {
				<#if _popup.fields?has_content>
				<#list _popup.fields?keys?sort as fk>
					<#list ui.fieldList as f2><#if f2.name == fk>
						<#if f2.editTag?? && f2.editTag.name?ends_with(".viewfield")>
				panda.viewfield("#${action.name}<#if f2.actionField>_a</#if>_${fk}").val(sd.${_popup.fields[fk]});
						<#else>
				$("#${action.name}<#if f2.actionField>_a</#if>_${fk}").val(sd.${_popup.fields[fk]});
						</#if>
						<#break/>
					</#if></#list>
				</#list>
				</#if>
				$.popup().hide();
			}

			</#if>
		</#list>
		
			function onPageLoad() {
		<#list ui.displayFieldList as f>
			<#assign _popup = {}/>
			<#if f.editTag??>
				<#list f.editTag.paramList as p>
					<#if p.name == "~popup">
						<#assign _popup = p.values/>
						<#break/>
					</#if>
				</#list>
			</#if>
			<#if _popup?has_content>
			<#assign pid = ""/>
			<#if _popup.ref?has_content>
				<#assign f2 = ui.getFieldByName(_popup.ref)/>
				<#assign pid = "popup_" + action.name + f2.actionField?string("_a", "") + "_" + f2.name/>
			<#else>
				<#assign pid = "popup_" + action.name + f.actionField?string("_a", "") + "_" + f.name/>
				$.popup({
					id: "${pid}",
					url: "${s}@p.url action="${_popup.action}" escapeAmp="false"><#rt>
				<#if _popup.params?has_content>
					<#list _popup.params?keys as pn>
${s}@s.param name="${pn}" value="${_popup.params[pn]}"/><#rt>
					</#list>
				</#if>
${s}/@p.url>"
				});

			</#if>
				$('#${action.name}<#if f.actionField>_a</#if>_${f.name}').next().popup({
					id: "${pid}",
					target: "#${action.name}<#if f.actionField>_a</#if>_${f.name}",
					callback: ${action.name}_${f.name}_onPopupCallback
				});
			
			</#if>
		</#list>
			}
		--></script>
