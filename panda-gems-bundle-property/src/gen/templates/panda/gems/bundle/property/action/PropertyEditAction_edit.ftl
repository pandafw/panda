<html>
<head>
	<title><@p.text name="title-edit"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="ui-headline">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-edit"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-edit", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="ui-toolbar text-right"><ul>
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul></div>

	<div id="property_alert"><#include "/action-alert.ftl"/></div>

	<@p.form cssClass="p-eform" id="property" method="post">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
				required="true"
			/>
	</#if>
	<#if a.displayField("clazz")>
			<@p.textfield
				key="clazz"
				value="%{r.clazz}"
				required="true"
				maxlength="100"
			/>
	</#if>
	<#if a.displayField("locale")>
			<@p.select
				key="locale"
				value="%{r.locale}"
				required="true"
				emptyOption="false"
				list="%{consts.appLocaleMap}"
			/>
	</#if>
	<#if a.displayField("name")>
			<@p.textfield
				key="name"
				value="%{r.name}"
				required="true"
				maxlength="50"
			/>
	</#if>
	<#if a.displayField("value")>
			<@p.textarea
				key="value"
				value="%{r.value}"
				maxlength="5000"
				cols=""
				rows="8"
				cssClass="p-code"
			/>
	</#if>
	<#if a.displayField("memo")>
			<@p.textarea
				key="memo"
				value="%{r.memo}"
				maxlength="1000"
				cols=""
				rows="5"
			/>
	</#if>
	<#if a.displayField("status")>
			<@p.radio
				key="status"
				value="%{r.status}"
				emptyOption="true"
				list="%{consts.dataStatusMap}"
			/>
	</#if>
	<#if a.displayField("updatedAt")>
			<@p.viewfield
				key="updatedAt"
				value="%{r.updatedAt}"
				format="datetime"
			/>
	</#if>
	<#if a.displayField("updatedBy")>
			<@p.hidden
				name="updatedBy"
				value="%{r.updatedBy}"
			/>
	</#if>
	<#if a.displayField("updatedByName")>
			<@p.hidden
				name="updatedByName"
				value="%{r.updatedByName}"
			/>
	</#if>
	<#if a.displayField("updatedByUser")>
			<@p.viewfield
				key="updatedByUser"
				value="%{r.updatedByUser}"
			/>
	</#if>
		<#assign _buttons_ = [] />
	<#if a.inputConfirm>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-edit-confirm",
			"action": "./edit.confirm",
			"text": "btn-edit-confirm"
		}]/>
	<#else>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-edit-execute",
			"action": "./edit.execute",
			"text": "btn-edit-execute"
		}]/>
	</#if>
			<#if a.canAccess('./list')>
				<@p.url var="_u_" action="./list"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-list",
					"text": "btn-list",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#include "/panda/mvc/view/form-buttons.ftl"/>
			<@form_buttons buttons=_buttons_/>
	</@p.form>

		<script type="text/javascript"><!--
		
			function onPageLoad() {
			}
		--></script>
</div>

</body>
</html>
