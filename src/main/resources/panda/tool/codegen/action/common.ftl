<#include "../lib.ftl"/>
<#assign actionDataFieldName = action.dataFieldName!'d'/>
<#assign actionDataListFieldName = action.dataListFieldName!'ds'/>
<#assign actionPackage = package_name(action.actionClass)/>
<#assign actionClass = class_name(action.actionClass)/>
<#assign actionBasePackage = package_name(action.actionBaseClass)/>
<#assign actionBaseClass = class_name(action.actionBaseClass)/>
<#assign entityBeanPackage = entity.package/>
<#assign entityBeanClass = entity.simpleName/>
