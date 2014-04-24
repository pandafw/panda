${props['ajax.basePackage']}.${action.package}.${action.name?cap_first}Module = Ext.extend(${props['ajax.baseSliduModule']}, {

	id: '${props['ajax.basePackage']}.${action.package}.${action.name?cap_first}Module',
	
	urlPattern: '${action.name}_*.${props["ajax.actionExtension"]}',
	
	initConfig: function() {
		this.fieldsConfig = {
<#list ui.columnList as c>
	<#assign p = ""/>
	<#list model.propertyList as mp>
		<#if mp.name == c.name>
			<#assign p = mp/>
			<#break/>
		</#if>
	</#list>
			"${c.name}": { 
		<#if p?has_content>
			<#switch p.simpleJavaType>
			<#case "Boolean">
				type: 'boolean',
				<#break>
			<#case "Byte">
			<#case "Short">
			<#case "Integer">
			<#case "Long">
			<#case "BigInteger">
				type: 'int',
				<#break>
			<#case "Float">
			<#case "Double">
			<#case "BigDecimal">
				type: 'float',
				<#break>
			<#case "Date">
				type: 'date',
				<#break>
			<#case "UploadFile">
				type: 'file',
				<#break>
			<#case "UploadImage">
				type: 'image',
				<#break>
			<#default>
				type: 'string',
				<#break>
			</#switch>
		<#else>
				type: 'string',
		</#if>
		<#if !(c.display!true) && (c.hidden!false)>
				list: false,
		<#else>
				list: { <#if c.width?has_content>width: "${c.width}", </#if><#rt/>
hidden: ${(c.hidden!false)?string}, sortable: ${(c.sortable!false)?string} },
		</#if>
		<#if c.format??>
			<#if p?has_content && p.simpleJavaType == "Date">
				dateFormat: this.getText('dateFormat.${c.format.type}'), 
			<#elseif c.format.type == "code">
				editor: { xtype: 'combo', editable: false, forceSelection: true, mode: 'local', triggerAction: 'all',
					store: new Ext.data.JsonStore({
						autoLoad: true,
						fields: [ 'code', 'text' ],
						root: 'data',
						url: '${props["ajax.codemapAction"]}' + escape('<#list c.format.paramList as fp>${fp.value?replace('#', '\\x23')}<#if fp_has_next>,</#if></#list>')
					}),
					displayField: 'text',
					valueField: 'code'
				}, 
			</#if>
	 	</#if>
		<#if p?has_content>
			<#switch p.simpleJavaType>
			<#case "UploadFile">
			<#case "UploadImage">
				editor: { name: '${c.name}.file' },
				<#break>
			</#switch>
		</#if>
	 			filter: true
			}<#if c_has_next>, </#if>
</#list>
		};
		
		${props['ajax.basePackage']}.${action.package}.${action.name?cap_first}Module.superclass.initConfig.call(this);
	}
});
