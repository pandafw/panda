SELECT * FROM TEST 
WHERE 
<#if id gt 0> ID=:id </#if>
<#if idList?has_content> AND ID IN (:idList) </#if>
<#if name??> AND NAME=:name </#if>
ORDER BY ${orderCol!"ID"} ${orderDir!}
