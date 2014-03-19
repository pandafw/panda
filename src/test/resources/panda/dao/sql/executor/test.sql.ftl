SELECT * FROM TEST 
WHERE 
<#if id?? && id gt 0> ID=:id </#if>
<#if intList?has_content> AND ID IN (:intList) </#if>
<#if fstr??> AND FSTR=:fstr </#if>
ORDER BY ${orderCol!"ID"} ${orderDir!}
