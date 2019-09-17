<html>
<head>
	<title><@p.text name="title-copy"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-copy"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-copy", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<@p.form cssClass="p-eform" id="pet" method="post">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("name")>
			<@p.textfield
				key="name"
				value="%{r.name}"
				required="true"
				maxlength="100"
			/>
	</#if>
	<#if a.displayField("gender")>
			<@p.radio
				key="gender"
				value="%{r.gender}"
				emptyOption="true"
				list="%{consts.petGenderMap}"
			/>
	</#if>
	<#if a.displayField("birthday")>
			<@p.datepicker
				key="birthday"
				value="%{r.birthday}"
				maxlength="10"
				size="12"
			/>
	</#if>
	<#if a.displayField("amount")>
			<@p.textfield
				key="amount"
				value="%{r.amount}"
				required="true"
				maxlength="14"
				size="16"
			/>
	</#if>
	<#if a.displayField("price")>
			<@p.textfield
				key="price"
				value="%{r.price}"
				maxlength="14"
			/>
	</#if>
	<#if a.displayField("shopName")>
			<@p.textfield
				key="shopName"
				value="%{r.shopName}"
				maxlength="100"
			/>
	</#if>
	<#if a.displayField("shopTelephone")>
			<@p.textfield
				key="shopTelephone"
				value="%{r.shopTelephone}"
				maxlength="20"
				size="25"
			/>
	</#if>
	<#if a.displayField("shopLink")>
			<@p.textfield
				key="shopLink"
				value="%{r.shopLink}"
				maxlength="100"
			/>
	</#if>
	<#if a.displayField("description")>
			<@p.htmleditor
				key="description"
				value="%{r.description}"
				maxlength="5000"
				cols=""
				rows="15"
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
			"icon": "icon-copy-confirm",
			"action": "./copy.confirm",
			"text": "btn-copy-confirm"
		}]/>
	<#else>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-copy-execute",
			"action": "./copy.execute",
			"text": "btn-copy-execute"
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
