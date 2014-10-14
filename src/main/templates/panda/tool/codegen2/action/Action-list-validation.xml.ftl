<#include "common.ftl"/>
<!DOCTYPE validators PUBLIC
		"-//Apache Struts//XWork Validator 1.0.3//EN"
		"http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">

<validators>
	<field name="pg">
		<field-validator type="visitor">
			<param name="appendPrefix">true</param>
			<message key="validation-visitor">%{""}</message>
		</field-validator>
	</field>
	<field name="so">
		<field-validator type="visitor">
			<param name="appendPrefix">true</param>
			<message key="validation-visitor">%{""}</message>
		</field-validator>
	</field>
	<field name="qf">
		<field-validator type="visitor">
			<param name="appendPrefix">true</param>
			<message key="validation-visitor">%{""}</message>
		</field-validator>
	</field>
	<field name="qm">
		<field-validator type="constant">
			<param name="ignoreCase">false</param>
			<param name="list">{ "and", "or" }</param>
			<message key="validation-constant"/>
		</field-validator>
	</field>
</validators>
