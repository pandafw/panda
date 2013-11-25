<#--
/*
 * This file is part of Nuts Framework.
 * Copyright(C) 2009-2012 Nuts Develop Team.
 *
 * Nuts Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 * 
 * Nuts Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nuts Framework. If not, see <http://www.gnu.org/licenses/>.
 */
-->
<#macro restrict p cn ca>
	/**
	 * @return restriction of ${p.name}
	 */
	<#if p.fieldKind == "boolean"> 
	public BooleanRestriction<${modelExampleClass}> ${p.name}() {
		return new BooleanRestriction<${modelExampleClass}>(this, ${modelMetaDataClass}.PN_${p.uname}, ${modelMetaDataClass}.CN_${cn?upper_case}, ${modelMetaDataClass}.CA_${ca?upper_case});
	}
	<#elseif p.fieldKind == "date" || p.fieldKind == "number"> 
	public ComparableRestriction<${modelExampleClass}, ${p.simpleJavaType}> ${p.name}() {
		return new ComparableRestriction<${modelExampleClass}, ${p.simpleJavaType}>(this, ${modelMetaDataClass}.PN_${p.uname}, ${modelMetaDataClass}.CN_${cn?upper_case}, ${modelMetaDataClass}.CA_${ca?upper_case});
	}
	<#elseif p.fieldKind == "string"> 
	public LikeableRestriction<${modelExampleClass}, ${p.simpleJavaType}> ${p.name}() {
		return new LikeableRestriction<${modelExampleClass}, ${p.simpleJavaType}>(this, ${modelMetaDataClass}.PN_${p.uname}, ${modelMetaDataClass}.CN_${cn?upper_case}, ${modelMetaDataClass}.CA_${ca?upper_case});
	}
	<#else>
	public ObjectRestriction<${modelExampleClass}> ${p.name}() {
		return new ObjectRestriction<${modelExampleClass}>(this, ${modelMetaDataClass}.PN_${p.uname}, ${modelMetaDataClass}.CN_${cn?upper_case}, ${modelMetaDataClass}.CA_${ca?upper_case});
	}
	</#if> 

</#macro>
