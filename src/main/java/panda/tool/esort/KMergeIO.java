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
package panda.tool.esort;

import java.io.IOException;

/**
 * k-merge I/O interface
 * @param <T> object type
 */
public interface KMergeIO<T> {

	/** 
	 * Read object by the given segmentNo
	 * @param segmentNo segment number
	 * @return object
	 * @throws IOException if an I/O error occurs
	 */
	T read(int segmentNo) throws IOException;
	
	/**
	 * Write object 
	 * @param obj object
	 * @throws IOException if an I/O error occurs
	 */
	void write(T obj) throws IOException;

}
