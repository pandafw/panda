<#include "../lib.ftl"/>
<#assign actionDataFieldName = action.dataFieldName!'da'/>
<#assign actionDataListFieldName = action.dataListFieldName!'dl'/>
<#assign actionPackage = package_name(action.fullActionClass)/>
<#assign actionClass = class_name(action.fullActionClass)/>
<#assign actionBasePackage = package_name(action.actionBaseClass)/>
<#assign actionBaseClass = class_name(action.actionBaseClass)/>
<#assign modelBeanPackage = package_name(model.modelBeanClass)/>
<#assign modelBeanClass = class_name(model.modelBeanClass)/>
<#assign modelMetaDataPackage = package_name(model.modelMetaDataClass)/>
<#assign modelMetaDataClass = class_name(model.modelMetaDataClass)/>
<#assign modelExamplePackage = package_name(model.modelExampleClass)/>
<#assign modelExampleClass = class_name(model.modelExampleClass)/>
<#assign modelDaoPackage = package_name(model.modelDaoClass)/> 
<#assign modelDaoClass = class_name(model.modelDaoClass)/>
<#assign modelSqlmapPackage = package_name(model.modelSqlmapClass)/> 
<#assign modelSqlmapClass = class_name(model.modelSqlmapClass)/>
