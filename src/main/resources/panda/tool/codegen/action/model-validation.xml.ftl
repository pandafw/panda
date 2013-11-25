	<field name="${actionDataFieldName}">
<#--
		<field-validator type="required">
			<message key="validation-required"/>
		</field-validator>
-->
		<field-validator type="visitor">
			<param name="appendPrefix">true</param>
			<message key="validation-visitor">%{""}</message>
		</field-validator>
	</field>
