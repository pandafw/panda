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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nuts Framework. If not, see <http://www.gnu.org/licenses/>.
 */
package panda.mvc.fileupload;

import panda.bind.filters.IncludePropertyFilter;

/**
 */
public class UploadFilePropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public UploadFilePropertyFilter() {
		super();
		includes.add("contentType");
		includes.add("fileName");
		includes.add("fileSize");
		includes.add("saveName");
		includes.add("saveTime");
	}
}

