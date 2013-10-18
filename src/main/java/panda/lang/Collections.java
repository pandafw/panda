package panda.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import panda.lang.collection.CaseInsensitiveMap;
import panda.lang.collection.SafeMap;

/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("unchecked")
public abstract class Collections {
	/** Constant to avoid repeated object creation */
	private static Integer INTEGER_ONE = new Integer(1);

	/**
	 * Returns a {@link Collection} containing the union of the given {@link Collection}s.
	 * <p>
	 * The cardinality of each element in the returned {@link Collection} will be equal to the
	 * maximum of the cardinality of that element in the two given {@link Collection}s.
	 * 
	 * @param a the first collection, must not be null
	 * @param b the second collection, must not be null
	 * @return the union of the two collections
	 * @see Collection#addAll
	 */
	public static Collection union(final Collection a, final Collection b) {
		ArrayList list = new ArrayList();
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Set elts = new HashSet(a);
		elts.addAll(b);
		Iterator it = elts.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			for (int i = 0, m = Math.max(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Returns a {@link Collection} containing the intersection of the given {@link Collection}s.
	 * <p>
	 * The cardinality of each element in the returned {@link Collection} will be equal to the
	 * minimum of the cardinality of that element in the two given {@link Collection}s.
	 * 
	 * @param a the first collection, must not be null
	 * @param b the second collection, must not be null
	 * @return the intersection of the two collections
	 * @see Collection#retainAll
	 * @see #containsAny
	 */
	public static Collection intersection(final Collection a, final Collection b) {
		ArrayList list = new ArrayList();
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Set elts = new HashSet(a);
		elts.addAll(b);
		Iterator it = elts.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			for (int i = 0, m = Math.min(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Returns a {@link Collection} containing the exclusive disjunction (symmetric difference) of
	 * the given {@link Collection}s.
	 * <p>
	 * The cardinality of each element <i>e</i> in the returned {@link Collection} will be equal to
	 * <tt>max(cardinality(<i>e</i>,<i>a</i>),cardinality(<i>e</i>,<i>b</i>)) - min(cardinality(<i>e</i>,<i>a</i>),cardinality(<i>e</i>,<i>b</i>))</tt>.
	 * <p>
	 * This is equivalent to
	 * <tt>{@link #subtract subtract}({@link #union union(a,b)},{@link #intersection intersection(a,b)})</tt>
	 * or
	 * <tt>{@link #union union}({@link #subtract subtract(a,b)},{@link #subtract subtract(b,a)})</tt>.
	 * 
	 * @param a the first collection, must not be null
	 * @param b the second collection, must not be null
	 * @return the symmetric difference of the two collections
	 */
	public static Collection disjunction(final Collection a, final Collection b) {
		ArrayList list = new ArrayList();
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Set elts = new HashSet(a);
		elts.addAll(b);
		Iterator it = elts.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			for (int i = 0, m = ((Math.max(getFreq(obj, mapa), getFreq(obj, mapb))) - (Math.min(
				getFreq(obj, mapa), getFreq(obj, mapb)))); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Returns a new {@link Collection} containing <tt><i>a</i> - <i>b</i></tt>. The cardinality of
	 * each element <i>e</i> in the returned {@link Collection} will be the cardinality of <i>e</i>
	 * in <i>a</i> minus the cardinality of <i>e</i> in <i>b</i>, or zero, whichever is greater.
	 * 
	 * @param a the collection to subtract from, must not be null
	 * @param b the collection to subtract, must not be null
	 * @return a new collection with the results
	 * @see Collection#removeAll
	 */
	public static Collection subtract(final Collection a, final Collection b) {
		ArrayList list = new ArrayList(a);
		for (Iterator it = b.iterator(); it.hasNext();) {
			list.remove(it.next());
		}
		return list;
	}

	/**
	 * Returns a {@link Map} mapping each unique element in the given {@link Collection} to an
	 * {@link Integer} representing the number of occurrences of that element in the
	 * {@link Collection}.
	 * <p>
	 * Only those elements present in the collection will appear as keys in the map.
	 * 
	 * @param coll the collection to get the cardinality map for, must not be null
	 * @return the populated cardinality map
	 */
	public static Map getCardinalityMap(final Collection coll) {
		Map count = new HashMap();
		for (Iterator it = coll.iterator(); it.hasNext();) {
			Object obj = it.next();
			Integer c = (Integer)(count.get(obj));
			if (c == null) {
				count.put(obj, INTEGER_ONE);
			}
			else {
				count.put(obj, new Integer(c.intValue() + 1));
			}
		}
		return count;
	}

	/**
	 * Returns <tt>true</tt> iff <i>a</i> is a sub-collection of <i>b</i>, that is, iff the
	 * cardinality of <i>e</i> in <i>a</i> is less than or equal to the cardinality of <i>e</i> in
	 * <i>b</i>, for each element <i>e</i> in <i>a</i>.
	 * 
	 * @param a the first (sub?) collection, must not be null
	 * @param b the second (super?) collection, must not be null
	 * @return <code>true</code> iff <i>a</i> is a sub-collection of <i>b</i>
	 * @see #isProperSubCollection
	 * @see Collection#containsAll
	 */
	public static boolean isSubCollection(final Collection a, final Collection b) {
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);
		Iterator it = a.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (getFreq(obj, mapa) > getFreq(obj, mapb)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns <tt>true</tt> iff <i>a</i> is a <i>proper</i> sub-collection of <i>b</i>, that is,
	 * iff the cardinality of <i>e</i> in <i>a</i> is less than or equal to the cardinality of
	 * <i>e</i> in <i>b</i>, for each element <i>e</i> in <i>a</i>, and there is at least one
	 * element <i>f</i> such that the cardinality of <i>f</i> in <i>b</i> is strictly greater than
	 * the cardinality of <i>f</i> in <i>a</i>.
	 * <p>
	 * The implementation assumes
	 * <ul>
	 * <li><code>a.size()</code> and <code>b.size()</code> represent the total cardinality of
	 * <i>a</i> and <i>b</i>, resp.</li>
	 * <li><code>a.size() < Integer.MAXVALUE</code></li>
	 * </ul>
	 * 
	 * @param a the first (sub?) collection, must not be null
	 * @param b the second (super?) collection, must not be null
	 * @return <code>true</code> iff <i>a</i> is a <i>proper</i> sub-collection of <i>b</i>
	 * @see #isSubCollection
	 * @see Collection#containsAll
	 */
	public static boolean isProperSubCollection(final Collection a, final Collection b) {
		return (a.size() < b.size()) && Collections.isSubCollection(a, b);
	}

	/**
	 * Returns <tt>true</tt> iff the given {@link Collection}s contain exactly the same elements
	 * with exactly the same cardinalities.
	 * <p>
	 * That is, iff the cardinality of <i>e</i> in <i>a</i> is equal to the cardinality of <i>e</i>
	 * in <i>b</i>, for each element <i>e</i> in <i>a</i> or <i>b</i>.
	 * 
	 * @param a the first collection, must not be null
	 * @param b the second collection, must not be null
	 * @return <code>true</code> iff the collections contain the same elements with the same
	 *         cardinalities.
	 */
	public static boolean isEqualCollection(final Collection a, final Collection b) {
		if (a.size() != b.size()) {
			return false;
		}
		else {
			Map mapa = getCardinalityMap(a);
			Map mapb = getCardinalityMap(b);
			if (mapa.size() != mapb.size()) {
				return false;
			}
			else {
				Iterator it = mapa.keySet().iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					if (getFreq(obj, mapa) != getFreq(obj, mapb)) {
						return false;
					}
				}
				return true;
			}
		}
	}

	/**
	 * Returns the number of occurrences of <i>obj</i> in <i>coll</i>.
	 * 
	 * @param obj the object to find the cardinality of
	 * @param coll the collection to search
	 * @return the the number of occurrences of obj in coll
	 */
	public static int cardinality(Object obj, final Collection coll) {
		if (coll instanceof Set) {
			return (coll.contains(obj) ? 1 : 0);
		}
		int count = 0;
		if (obj == null) {
			for (Iterator it = coll.iterator(); it.hasNext();) {
				if (it.next() == null) {
					count++;
				}
			}
		}
		else {
			for (Iterator it = coll.iterator(); it.hasNext();) {
				if (obj.equals(it.next())) {
					count++;
				}
			}
		}
		return count;
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds an element to the collection unless the element is null.
	 * 
	 * @param collection the collection to add to, must not be null
	 * @param object the object to add, if null it will not be added
	 * @return true if the collection changed
	 * @throws NullPointerException if the collection is null
	 */
	public static boolean addIgnoreNull(Collection collection, Object object) {
		return (object == null ? false : collection.add(object));
	}

	/**
	 * Adds all elements in the iteration to the given collection.
	 * 
	 * @param collection the collection to add to, must not be null
	 * @param iterator the iterator of elements to add, must not be null
	 * @throws NullPointerException if the collection or iterator is null
	 */
	public static void addAll(Collection collection, Iterator iterator) {
		while (iterator.hasNext()) {
			collection.add(iterator.next());
		}
	}

	/**
	 * Adds all elements in the enumeration to the given collection.
	 * 
	 * @param collection the collection to add to, must not be null
	 * @param enumeration the enumeration of elements to add, must not be null
	 * @throws NullPointerException if the collection or enumeration is null
	 */
	public static void addAll(Collection collection, Enumeration enumeration) {
		while (enumeration.hasMoreElements()) {
			collection.add(enumeration.nextElement());
		}
	}

	/**
	 * Returns the <code>index</code>-th value in <code>object</code>, throwing
	 * <code>IndexOutOfBoundsException</code> if there is no such element or
	 * <code>IllegalArgumentException</code> if <code>object</code> is not an instance of one of the
	 * supported types.
	 * <p>
	 * The supported types, and associated semantics are:
	 * <ul>
	 * <li>Map -- the value returned is the <code>Map.Entry</code> in position <code>index</code> in
	 * the map's <code>entrySet</code> iterator, if there is such an entry.</li>
	 * <li>List -- this method is equivalent to the list's get method.</li>
	 * <li>Array -- the <code>index</code>-th array entry is returned, if there is such an entry;
	 * otherwise an <code>IndexOutOfBoundsException</code> is thrown.</li>
	 * <li>Collection -- the value returned is the <code>index</code>-th object returned by the
	 * collection's default iterator, if there is such an element.</li>
	 * <li>Iterator or Enumeration -- the value returned is the <code>index</code>-th object in the
	 * Iterator/Enumeration, if there is such an element. The Iterator/Enumeration is advanced to
	 * <code>index</code> (or to the end, if <code>index</code> exceeds the number of entries) as a
	 * side effect of this method.</li>
	 * </ul>
	 * 
	 * @param object the object to get a value from
	 * @param index the index to get
	 * @return the object at the specified index
	 * @throws IndexOutOfBoundsException if the index is invalid
	 * @throws IllegalArgumentException if the object type is invalid
	 */
	public static Object get(Object object, int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index cannot be negative: " + index);
		}
		if (object instanceof Map) {
			Map map = (Map)object;
			Iterator iterator = map.entrySet().iterator();
			return get(iterator, index);
		}
		else if (object instanceof List) {
			return ((List)object).get(index);
		}
		else if (object instanceof Object[]) {
			return ((Object[])object)[index];
		}
		else if (object instanceof Iterator) {
			Iterator it = (Iterator)object;
			while (it.hasNext()) {
				index--;
				if (index == -1) {
					return it.next();
				}
				else {
					it.next();
				}
			}
			throw new IndexOutOfBoundsException("Entry does not exist: " + index);
		}
		else if (object instanceof Collection) {
			Iterator iterator = ((Collection)object).iterator();
			return get(iterator, index);
		}
		else if (object instanceof Enumeration) {
			Enumeration it = (Enumeration)object;
			while (it.hasMoreElements()) {
				index--;
				if (index == -1) {
					return it.nextElement();
				}
				else {
					it.nextElement();
				}
			}
			throw new IndexOutOfBoundsException("Entry does not exist: " + index);
		}
		else if (object == null) {
			throw new IllegalArgumentException("Unsupported object type: null");
		}
		else {
			try {
				return Array.get(object, index);
			}
			catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("Unsupported object type: "
						+ object.getClass().getName());
			}
		}
	}

	/**
	 * Gets the size of the collection/iterator specified.
	 * <p>
	 * This method can handles objects as follows
	 * <ul>
	 * <li>Collection - the collection size
	 * <li>Map - the map size
	 * <li>Array - the array size
	 * <li>Iterator - the number of elements remaining in the iterator
	 * <li>Enumeration - the number of elements remaining in the enumeration
	 * </ul>
	 * 
	 * @param object the object to get the size of
	 * @return the size of the specified collection
	 * @throws IllegalArgumentException thrown if object is not recognised or null
	 */
	public static int size(Object object) {
		int total = 0;
		if (object instanceof Map) {
			total = ((Map)object).size();
		}
		else if (object instanceof Collection) {
			total = ((Collection)object).size();
		}
		else if (object instanceof Object[]) {
			total = ((Object[])object).length;
		}
		else if (object instanceof Iterator) {
			Iterator it = (Iterator)object;
			while (it.hasNext()) {
				total++;
				it.next();
			}
		}
		else if (object instanceof Enumeration) {
			Enumeration it = (Enumeration)object;
			while (it.hasMoreElements()) {
				total++;
				it.nextElement();
			}
		}
		else if (object == null) {
			throw new IllegalArgumentException("Unsupported object type: null");
		}
		else {
			try {
				total = Array.getLength(object);
			}
			catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("Unsupported object type: "
						+ object.getClass().getName());
			}
		}
		return total;
	}

	/**
	 * Checks if the specified collection/array/iterator is empty.
	 * <p>
	 * This method can handles objects as follows
	 * <ul>
	 * <li>Collection - via collection isEmpty
	 * <li>Map - via map isEmpty
	 * <li>Array - using array size
	 * <li>Iterator - via hasNext
	 * <li>Enumeration - via hasMoreElements
	 * </ul>
	 * <p>
	 * Note: This method is named to avoid clashing with {@link #isEmpty(Collection)}.
	 * 
	 * @param object the object to get the size of, not null
	 * @return true if empty
	 * @throws IllegalArgumentException thrown if object is not recognised or null
	 */
	public static boolean sizeIsEmpty(Object object) {
		if (object instanceof Collection) {
			return ((Collection)object).isEmpty();
		}
		else if (object instanceof Map) {
			return ((Map)object).isEmpty();
		}
		else if (object instanceof Object[]) {
			return ((Object[])object).length == 0;
		}
		else if (object instanceof Iterator) {
			return ((Iterator)object).hasNext() == false;
		}
		else if (object instanceof Enumeration) {
			return ((Enumeration)object).hasMoreElements() == false;
		}
		else if (object == null) {
			throw new IllegalArgumentException("Unsupported object type: null");
		}
		else {
			try {
				return Array.getLength(object) == 0;
			}
			catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("Unsupported object type: "
						+ object.getClass().getName());
			}
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 * 
	 * @param coll the collection to check, may be null
	 * @return true if empty or null
	 */
	public static boolean isEmpty(Collection coll) {
		return (coll == null || coll.isEmpty());
	}

	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 * 
	 * @param coll the collection to check, may be null
	 * @return true if non-null and non-empty
	 */
	public static boolean isNotEmpty(Collection coll) {
		return !Collections.isEmpty(coll);
	}

	// -----------------------------------------------------------------------
	/**
	 * Reverses the order of the given array.
	 * 
	 * @param array the array to reverse
	 */
	public static void reverseArray(Object[] array) {
		int i = 0;
		int j = array.length - 1;
		Object tmp;

		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	private static final int getFreq(final Object obj, final Map freqMap) {
		Integer count = (Integer)freqMap.get(obj);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns a collection containing all the elements in <code>collection</code> that are also in
	 * <code>retain</code>. The cardinality of an element <code>e</code> in the returned collection
	 * is the same as the cardinality of <code>e</code> in <code>collection</code> unless
	 * <code>retain</code> does not contain <code>e</code>, in which case the cardinality is zero.
	 * This method is useful if you do not wish to modify the collection <code>c</code> and thus
	 * cannot call <code>c.retainAll(retain);</code>.
	 * 
	 * @param collection the collection whose contents are the target of the #retailAll operation
	 * @param retain the collection containing the elements to be retained in the returned
	 *            collection
	 * @return a <code>Collection</code> containing all the elements of <code>collection</code> that
	 *         occur at least once in <code>retain</code>.
	 * @throws NullPointerException if either parameter is null
	 */
	public static Collection retainAll(Collection collection, Collection retain) {
		List list = new ArrayList(Math.min(collection.size(), retain.size()));

		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (retain.contains(obj)) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * Removes the elements in <code>remove</code> from <code>collection</code>. That is, this
	 * method returns a collection containing all the elements in <code>c</code> that are not in
	 * <code>remove</code>. The cardinality of an element <code>e</code> in the returned collection
	 * is the same as the cardinality of <code>e</code> in <code>collection</code> unless
	 * <code>remove</code> contains <code>e</code>, in which case the cardinality is zero. This
	 * method is useful if you do not wish to modify the collection <code>c</code> and thus cannot
	 * call <code>collection.removeAll(remove);</code>.
	 * 
	 * @param collection the collection from which items are removed (in the returned collection)
	 * @param remove the items to be removed from the returned <code>collection</code>
	 * @return a <code>Collection</code> containing all the elements of <code>collection</code>
	 *         except any elements that also occur in <code>remove</code>.
	 * @throws NullPointerException if either parameter is null
	 */
	public static Collection removeAll(Collection collection, Collection remove) {
		List list = new ArrayList();
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (remove.contains(obj) == false) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * @param map the Map to wrap
	 * @return a case insensitive map
	 */
	public static <K, V> Map<K, V> caseInsensitiveMap(Map<K, V> map) {
		return new CaseInsensitiveMap<K, V>(map);
	}

	/**
	 * @param map the Map to wrap
	 * @return a safe map
	 */
	public static <K, V> Map<K, V> safeMap(Map<K, V> map) {
		return new SafeMap<K, V>(map);
	}

	/**
	 * Return <code>true</code> if the supplied Map is <code>null</code> or empty. Otherwise, return
	 * <code>false</code>.
	 * 
	 * @param map the Map to check
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * Return <code>true</code> if the supplied Map is not <code>null</code> or empty. Otherwise,
	 * return <code>false</code>.
	 * 
	 * @param map the Map to check
	 * @return whether the given Map is not empty
	 */
	public static boolean isNotEmpty(Map map) {
		return (map != null && !map.isEmpty());
	}

	/**
	 * Remove null elements in the collection.
	 * 
	 * @param collection the collection to get the input from, may be null
	 */
	public static void removeNull(Collection collection) {
		if (collection != null) {
			for (Iterator it = collection.iterator(); it.hasNext();) {
				if (it.next() == null) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Remove null elements in the map.
	 * 
	 * @param map the map to get the input from, may be null
	 */
	public static void removeNull(Map map) {
		if (map != null) {
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				if (((Entry)it.next()).getValue() == null) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Remove null elements in the collection.
	 * 
	 * @param collection the collection to get the input from, may be null
	 * @return null elements removed collection
	 * @throws IllegalAccessException Class.newInstance
	 * @throws InstantiationException Class.newInstance
	 */
	public static Collection copyNotNull(Collection collection) throws InstantiationException,
			IllegalAccessException {
		if (collection == null) {
			return null;
		}

		Collection nc = collection.getClass().newInstance();
		for (Iterator it = collection.iterator(); it.hasNext();) {
			Object o = it.next();
			if (o != null) {
				nc.add(o);
			}
		}
		return nc;
	}

	/**
	 * Merge the given array into the given Collection.
	 * 
	 * @param array the array to merge (may be <code>null</code>)
	 * @param collection the target Collection to merge the array into
	 */
	public static void mergeArrayIntoCollection(Object array, Collection collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection must not be null");
		}
		Object[] arr = Arrays.toObjectArray(array);
		for (int i = 0; i < arr.length; i++) {
			collection.add(arr[i]);
		}
	}

	/**
	 * Merge the given Properties instance into the given Map, copying all properties (key-value
	 * pairs) over.
	 * <p>
	 * Uses <code>Properties.propertyNames()</code> to even catch default properties linked into the
	 * original Properties instance.
	 * 
	 * @param props the Properties instance to merge (may be <code>null</code>)
	 * @param map the target Map to merge the properties into
	 */
	public static void mergePropertiesIntoMap(Properties props, Map map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String)en.nextElement();
				map.put(key, props.getProperty(key));
			}
		}
	}

	/**
	 * Check whether the given Iterator contains the given element.
	 * 
	 * @param iterator the Iterator to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean contains(Iterator iterator, Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (Objects.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Iterator contains the given element.
	 * 
	 * @param source the source collection
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean contains(Collection source, Object element) {
		if (source != null) {
			return source.contains(element);
		}
		return false;
	}

	/**
	 * Check whether the given Enumeration contains the given element.
	 * 
	 * @param enumeration the Enumeration to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean contains(Enumeration enumeration, Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (Objects.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Collection contains the given element instance.
	 * <p>
	 * Enforces the given instance to be present, rather than returning <code>true</code> for an
	 * equal element as well.
	 * 
	 * @param collection the Collection to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean containsInstance(Collection collection, Object element) {
		if (collection != null) {
			for (Iterator it = collection.iterator(); it.hasNext();) {
				Object candidate = it.next();
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return <code>true</code> if any element in '<code>candidates</code>' is contained in '
	 * <code>source</code>'; otherwise returns <code>false</code>.
	 * 
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return whether any of the candidates has been found
	 */
	public static boolean containsAny(Collection source, Collection candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Iterator it = candidates.iterator(); it.hasNext();) {
			if (source.contains(it.next())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the first element in '<code>candidates</code>' that is contained in '
	 * <code>source</code>'. If no element in '<code>candidates</code>' is present in '
	 * <code>source</code>' returns <code>null</code>. Iteration order is {@link Collection}
	 * implementation specific.
	 * 
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return the first present object, or <code>null</code> if not found
	 */
	public static Object findFirstMatch(Collection source, Collection candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return null;
		}
		for (Iterator it = candidates.iterator(); it.hasNext();) {
			Object candidate = it.next();
			if (source.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Find a single value of the given type in the given Collection.
	 * 
	 * @param collection the Collection to search
	 * @param type the type to look for
	 * @return a value of the given type found if there is a clear match, or <code>null</code> if
	 *         none or more than one such value found
	 */
	public static Object findValueOfType(Collection collection, Class type) {
		if (isEmpty(collection)) {
			return null;
		}
		Object value = null;
		for (Iterator it = collection.iterator(); it.hasNext();) {
			Object obj = it.next();
			if (type == null || type.isInstance(obj)) {
				if (value != null) {
					// More than one value found... no clear single value.
					return null;
				}
				value = obj;
			}
		}
		return value;
	}

	/**
	 * Find a single value of one of the given types in the given Collection: searching the
	 * Collection for a value of the first type, then searching for a value of the second type, etc.
	 * 
	 * @param collection the collection to search
	 * @param types the types to look for, in prioritized order
	 * @return a value of one of the given types found if there is a clear match, or
	 *         <code>null</code> if none or more than one such value found
	 */
	public static Object findValueOfType(Collection collection, Class[] types) {
		if (isEmpty(collection) || Arrays.isEmpty(types)) {
			return null;
		}
		for (int i = 0; i < types.length; i++) {
			Object value = findValueOfType(collection, types[i]);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Determine whether the given Collection only contains a single unique object.
	 * 
	 * @param collection the Collection to check
	 * @return <code>true</code> if the collection contains a single reference or multiple
	 *         references to the same instance, <code>false</code> else
	 */
	public static boolean hasUniqueObject(Collection collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Iterator it = collection.iterator(); it.hasNext();) {
			Object elem = it.next();
			if (!hasCandidate) {
				hasCandidate = true;
				candidate = elem;
			}
			else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	//--------------------------------------------------------
	// @see java.util.Collections
	
	/**
	 * Sorts the specified list into ascending order, according to the <i>natural ordering</i> of
	 * its elements. All elements in the list must implement the <tt>Comparable</tt> interface.
	 * Furthermore, all elements in the list must be <i>mutually comparable</i> (that is,
	 * <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt> for any elements
	 * <tt>e1</tt> and <tt>e2</tt> in the list).
	 * <p>
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.
	 * <p>
	 * The specified list must be modifiable, but need not be resizable.
	 * <p>
	 * The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest
	 * element in the low sublist is less than the lowest element in the high sublist). This
	 * algorithm offers guaranteed n log(n) performance. This implementation dumps the specified
	 * list into an array, sorts the array, and iterates over the list resetting each element from
	 * the corresponding position in the array. This avoids the n<sup>2</sup> log(n) performance
	 * that would result from attempting to sort a linked list in place.
	 * 
	 * @param list the list to be sorted.
	 * @throws ClassCastException if the list contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 * @throws UnsupportedOperationException if the specified list's list-iterator does not support
	 *             the <tt>set</tt> operation.
	 * @see Comparable
	 */
	public static <T extends Comparable<? super T>> void sort(List<T> list) {
		java.util.Collections.sort(list);
	}

	/**
	 * Sorts the specified list according to the order induced by the specified comparator. All
	 * elements in the list must be <i>mutually comparable</i> using the specified comparator (that
	 * is, <tt>c.compare(e1, e2)</tt> must not throw a <tt>ClassCastException</tt> for any elements
	 * <tt>e1</tt> and <tt>e2</tt> in the list).
	 * <p>
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.
	 * <p>
	 * The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest
	 * element in the low sublist is less than the lowest element in the high sublist). This
	 * algorithm offers guaranteed n log(n) performance. The specified list must be modifiable, but
	 * need not be resizable. This implementation dumps the specified list into an array, sorts the
	 * array, and iterates over the list resetting each element from the corresponding position in
	 * the array. This avoids the n<sup>2</sup> log(n) performance that would result from attempting
	 * to sort a linked list in place.
	 * 
	 * @param list the list to be sorted.
	 * @param c the comparator to determine the order of the list. A <tt>null</tt> value indicates
	 *            that the elements' <i>natural ordering</i> should be used.
	 * @throws ClassCastException if the list contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws UnsupportedOperationException if the specified list's list-iterator does not support
	 *             the <tt>set</tt> operation.
	 * @see Comparator
	 */
	public static <T> void sort(List<T> list, Comparator<? super T> c) {
		java.util.Collections.sort(list, c);
	}

	/**
	 * Searches the specified list for the specified object using the binary search algorithm. The
	 * list must be sorted into ascending order according to the {@linkplain Comparable natural
	 * ordering} of its elements (as by the {@link #sort(List)} method) prior to making this call.
	 * If it is not sorted, the results are undefined. If the list contains multiple elements equal
	 * to the specified object, there is no guarantee which one will be found.
	 * <p>
	 * This method runs in log(n) time for a "random access" list (which provides near-constant-time
	 * positional access). If the specified list does not implement the {@link RandomAccess}
	 * interface and is large, this method will do an iterator-based binary search that performs
	 * O(n) link traversals and O(log n) element comparisons.
	 * 
	 * @param list the list to be searched.
	 * @param key the key to be searched for.
	 * @return the index of the search key, if it is contained in the list; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the list: the index of the first
	 *         element greater than the key, or <tt>list.size()</tt> if all elements in the list are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 * @throws ClassCastException if the list contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers), or the search key is not
	 *             mutually comparable with the elements of the list.
	 */
	public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
		return java.util.Collections.binarySearch(list, key);
	}

	/**
	 * Searches the specified list for the specified object using the binary search algorithm. The
	 * list must be sorted into ascending order according to the specified comparator (as by the
	 * {@link #sort(List, Comparator) sort(List, Comparator)} method), prior to making this call. If
	 * it is not sorted, the results are undefined. If the list contains multiple elements equal to
	 * the specified object, there is no guarantee which one will be found.
	 * <p>
	 * This method runs in log(n) time for a "random access" list (which provides near-constant-time
	 * positional access). If the specified list does not implement the {@link RandomAccess}
	 * interface and is large, this method will do an iterator-based binary search that performs
	 * O(n) link traversals and O(log n) element comparisons.
	 * 
	 * @param list the list to be searched.
	 * @param key the key to be searched for.
	 * @param c the comparator by which the list is ordered. A <tt>null</tt> value indicates that
	 *            the elements' {@linkplain Comparable natural ordering} should be used.
	 * @return the index of the search key, if it is contained in the list; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the list: the index of the first
	 *         element greater than the key, or <tt>list.size()</tt> if all elements in the list are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 * @throws ClassCastException if the list contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator, or the search key is not mutually
	 *             comparable with the elements of the list using this comparator.
	 */
	public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c) {
		return java.util.Collections.binarySearch(list, key, c);
	}

	/**
	 * Reverses the order of the elements in the specified list.
	 * <p>
	 * This method runs in linear time.
	 * 
	 * @param list the list whose elements are to be reversed.
	 * @throws UnsupportedOperationException if the specified list or its list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static void reverse(List<?> list) {
		java.util.Collections.reverse(list);
	}

	/**
	 * Randomly permutes the specified list using a default source of randomness. All permutations
	 * occur with approximately equal likelihood.
	 * <p>
	 * The hedge "approximately" is used in the foregoing description because default source of
	 * randomness is only approximately an unbiased source of independently chosen bits. If it were
	 * a perfect source of randomly chosen bits, then the algorithm would choose permutations with
	 * perfect uniformity.
	 * <p>
	 * This implementation traverses the list backwards, from the last element up to the second,
	 * repeatedly swapping a randomly selected element into the "current position". Elements are
	 * randomly selected from the portion of the list that runs from the first element to the
	 * current position, inclusive.
	 * <p>
	 * This method runs in linear time. If the specified list does not implement the
	 * {@link RandomAccess} interface and is large, this implementation dumps the specified list
	 * into an array before shuffling it, and dumps the shuffled array back into the list. This
	 * avoids the quadratic behavior that would result from shuffling a "sequential access" list in
	 * place.
	 * 
	 * @param list the list to be shuffled.
	 * @throws UnsupportedOperationException if the specified list or its list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static void shuffle(List<?> list) {
		java.util.Collections.shuffle(list);
	}

	/**
	 * Randomly permute the specified list using the specified source of randomness. All
	 * permutations occur with equal likelihood assuming that the source of randomness is fair.
	 * <p>
	 * This implementation traverses the list backwards, from the last element up to the second,
	 * repeatedly swapping a randomly selected element into the "current position". Elements are
	 * randomly selected from the portion of the list that runs from the first element to the
	 * current position, inclusive.
	 * <p>
	 * This method runs in linear time. If the specified list does not implement the
	 * {@link RandomAccess} interface and is large, this implementation dumps the specified list
	 * into an array before shuffling it, and dumps the shuffled array back into the list. This
	 * avoids the quadratic behavior that would result from shuffling a "sequential access" list in
	 * place.
	 * 
	 * @param list the list to be shuffled.
	 * @param rnd the source of randomness to use to shuffle the list.
	 * @throws UnsupportedOperationException if the specified list or its list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static void shuffle(List<?> list, Random rnd) {
		java.util.Collections.shuffle(list, rnd);
	}

	/**
	 * Swaps the elements at the specified positions in the specified list. (If the specified
	 * positions are equal, invoking this method leaves the list unchanged.)
	 * 
	 * @param list The list in which to swap elements.
	 * @param i the index of one element to be swapped.
	 * @param j the index of the other element to be swapped.
	 * @throws IndexOutOfBoundsException if either <tt>i</tt> or <tt>j</tt> is out of range (i &lt;
	 *             0 || i &gt;= list.size() || j &lt; 0 || j &gt;= list.size()).
	 */
	public static void swap(List<?> list, int i, int j) {
		java.util.Collections.swap(list, i, j);
	}

	/**
	 * Replaces all of the elements of the specified list with the specified element.
	 * <p>
	 * This method runs in linear time.
	 * 
	 * @param list the list to be filled with the specified element.
	 * @param obj The element with which to fill the specified list.
	 * @throws UnsupportedOperationException if the specified list or its list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static <T> void fill(List<? super T> list, T obj) {
		java.util.Collections.fill(list, obj);
	}

	/**
	 * Copies all of the elements from one list into another. After the operation, the index of each
	 * copied element in the destination list will be identical to its index in the source list. The
	 * destination list must be at least as long as the source list. If it is longer, the remaining
	 * elements in the destination list are unaffected.
	 * <p>
	 * This method runs in linear time.
	 * 
	 * @param dest The destination list.
	 * @param src The source list.
	 * @throws IndexOutOfBoundsException if the destination list is too small to contain the entire
	 *             source List.
	 * @throws UnsupportedOperationException if the destination list's list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static <T> void copy(List<? super T> dest, List<? extends T> src) {
		java.util.Collections.copy(dest, src);
	}

	private final static int COPY_THRESHOLD = 10;
	
	/**
	 * @param src the source list.
	 * @param srcPos starting position in the source list.
	 * @param des the destination list.
	 * @param desPos starting position in the destination data.
	 * @param length the number of list elements to be copied.
	 */
	public static <T> void copy(List<? extends T> src, int srcPos, List<? super T> des, int desPos, int length) {
		Asserts.isTrue(srcPos >= 0, "The srcPos value must be greater or equal zero: %d", srcPos);
		Asserts.isTrue(desPos >= 0, "The desPos value must be greater or equal zero: %d", desPos);

		int srcSize = src.size();
		int desSize = des.size();
		Asserts.isTrue(srcPos + length <= srcSize && desPos + length <= desSize, 
				"The length value is out of bounds: %d", length);

		if (srcSize < COPY_THRESHOLD || (src instanceof RandomAccess && des instanceof RandomAccess)) {
			for (int i = 0; i < length; i++) {
				des.set(desPos + i, src.get(srcPos + i));
			}
		}
		else {
			ListIterator<? super T> di = des.listIterator();
			ListIterator<? extends T> si = src.listIterator();
			for (int i = 0; i < srcPos; i++) {
				si.next();
			}
			for (int i = 0; i < desPos; i++) {
				di.next();
			}
			for (int i = 0; i < length; i++) {
				di.next();
				di.set(si.next());
			}
		}
	}

	/**
	 * Returns the minimum element of the given collection, according to the <i>natural ordering</i>
	 * of its elements. All elements in the collection must implement the <tt>Comparable</tt>
	 * interface. Furthermore, all elements in the collection must be <i>mutually comparable</i>
	 * (that is, <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt> for any
	 * elements <tt>e1</tt> and <tt>e2</tt> in the collection).
	 * <p>
	 * This method iterates over the entire collection, hence it requires time proportional to the
	 * size of the collection.
	 * 
	 * @param coll the collection whose minimum element is to be determined.
	 * @return the minimum element of the given collection, according to the <i>natural ordering</i>
	 *         of its elements.
	 * @throws ClassCastException if the collection contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 * @throws NoSuchElementException if the collection is empty.
	 * @see Comparable
	 */
	public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll) {
		return java.util.Collections.min(coll);
	}

	/**
	 * Returns the minimum element of the given collection, according to the order induced by the
	 * specified comparator. All elements in the collection must be <i>mutually comparable</i> by
	 * the specified comparator (that is, <tt>comp.compare(e1, e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt> in the collection).
	 * <p>
	 * This method iterates over the entire collection, hence it requires time proportional to the
	 * size of the collection.
	 * 
	 * @param coll the collection whose minimum element is to be determined.
	 * @param comp the comparator with which to determine the minimum element. A <tt>null</tt> value
	 *            indicates that the elements' <i>natural ordering</i> should be used.
	 * @return the minimum element of the given collection, according to the specified comparator.
	 * @throws ClassCastException if the collection contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws NoSuchElementException if the collection is empty.
	 * @see Comparable
	 */
	public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
		return java.util.Collections.min(coll, comp);
	}

	/**
	 * Returns the maximum element of the given collection, according to the <i>natural ordering</i>
	 * of its elements. All elements in the collection must implement the <tt>Comparable</tt>
	 * interface. Furthermore, all elements in the collection must be <i>mutually comparable</i>
	 * (that is, <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt> for any
	 * elements <tt>e1</tt> and <tt>e2</tt> in the collection).
	 * <p>
	 * This method iterates over the entire collection, hence it requires time proportional to the
	 * size of the collection.
	 * 
	 * @param coll the collection whose maximum element is to be determined.
	 * @return the maximum element of the given collection, according to the <i>natural ordering</i>
	 *         of its elements.
	 * @throws ClassCastException if the collection contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 * @throws NoSuchElementException if the collection is empty.
	 * @see Comparable
	 */
	public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
		return java.util.Collections.max(coll);
	}

	/**
	 * Returns the maximum element of the given collection, according to the order induced by the
	 * specified comparator. All elements in the collection must be <i>mutually comparable</i> by
	 * the specified comparator (that is, <tt>comp.compare(e1, e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt> in the collection).
	 * <p>
	 * This method iterates over the entire collection, hence it requires time proportional to the
	 * size of the collection.
	 * 
	 * @param coll the collection whose maximum element is to be determined.
	 * @param comp the comparator with which to determine the maximum element. A <tt>null</tt> value
	 *            indicates that the elements' <i>natural ordering</i> should be used.
	 * @return the maximum element of the given collection, according to the specified comparator.
	 * @throws ClassCastException if the collection contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws NoSuchElementException if the collection is empty.
	 * @see Comparable
	 */
	public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
		return java.util.Collections.max(coll, comp);
	}

	/**
	 * Rotates the elements in the specified list by the specified distance. After calling this
	 * method, the element at index <tt>i</tt> will be the element previously at index
	 * <tt>(i - distance)</tt> mod <tt>list.size()</tt>, for all values of <tt>i</tt> between
	 * <tt>0</tt> and <tt>list.size()-1</tt>, inclusive. (This method has no effect on the size of
	 * the list.)
	 * <p>
	 * For example, suppose <tt>list</tt> comprises<tt> [t, a, n, k, s]</tt>. After invoking
	 * <tt>Collections.rotate(list, 1)</tt> (or <tt>Collections.rotate(list, -4)</tt>),
	 * <tt>list</tt> will comprise <tt>[s, t, a, n, k]</tt>.
	 * <p>
	 * Note that this method can usefully be applied to sublists to move one or more elements within
	 * a list while preserving the order of the remaining elements. For example, the following idiom
	 * moves the element at index <tt>j</tt> forward to position <tt>k</tt> (which must be greater
	 * than or equal to <tt>j</tt>):
	 * 
	 * <pre>
	 * Collections.rotate(list.subList(j, k + 1), -1);
	 * </pre>
	 * 
	 * To make this concrete, suppose <tt>list</tt> comprises <tt>[a, b, c, d, e]</tt>. To move the
	 * element at index <tt>1</tt> (<tt>b</tt>) forward two positions, perform the following
	 * invocation:
	 * 
	 * <pre>
	 * Collections.rotate(l.subList(1, 4), -1);
	 * </pre>
	 * 
	 * The resulting list is <tt>[a, c, d, b, e]</tt>.
	 * <p>
	 * To move more than one element forward, increase the absolute value of the rotation distance.
	 * To move elements backward, use a positive shift distance.
	 * <p>
	 * If the specified list is small or implements the {@link RandomAccess} interface, this
	 * implementation exchanges the first element into the location it should go, and then
	 * repeatedly exchanges the displaced element into the location it should go until a displaced
	 * element is swapped into the first element. If necessary, the process is repeated on the
	 * second and successive elements, until the rotation is complete. If the specified list is
	 * large and doesn't implement the <tt>RandomAccess</tt> interface, this implementation breaks
	 * the list into two sublist views around index <tt>-distance mod size</tt>. Then the
	 * {@link #reverse(List)} method is invoked on each sublist view, and finally it is invoked on
	 * the entire list. For a more complete description of both algorithms, see Section 2.3 of Jon
	 * Bentley's <i>Programming Pearls</i> (Addison-Wesley, 1986).
	 * 
	 * @param list the list to be rotated.
	 * @param distance the distance to rotate the list. There are no constraints on this value; it
	 *            may be zero, negative, or greater than <tt>list.size()</tt>.
	 * @throws UnsupportedOperationException if the specified list or its list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static void rotate(List<?> list, int distance) {
		java.util.Collections.rotate(list, distance);
	}

	/**
	 * Replaces all occurrences of one specified value in a list with another. More formally,
	 * replaces with <tt>newVal</tt> each element <tt>e</tt> in <tt>list</tt> such that
	 * <tt>(oldVal==null ? e==null : oldVal.equals(e))</tt>. (This method has no effect on the size
	 * of the list.)
	 * 
	 * @param list the list in which replacement is to occur.
	 * @param oldVal the old value to be replaced.
	 * @param newVal the new value with which <tt>oldVal</tt> is to be replaced.
	 * @return <tt>true</tt> if <tt>list</tt> contained one or more elements <tt>e</tt> such that
	 *         <tt>(oldVal==null ?  e==null : oldVal.equals(e))</tt>.
	 * @throws UnsupportedOperationException if the specified list or its list-iterator does not
	 *             support the <tt>set</tt> operation.
	 */
	public static <T> boolean replaceAll(List<T> list, T oldVal, T newVal) {
		return java.util.Collections.replaceAll(list, oldVal, newVal);
	}

	/**
	 * Returns the starting position of the first occurrence of the specified target list within the
	 * specified source list, or -1 if there is no such occurrence. More formally, returns the
	 * lowest index <tt>i</tt> such that <tt>source.subList(i, i+target.size()).equals(target)</tt>,
	 * or -1 if there is no such index. (Returns -1 if <tt>target.size() > source.size()</tt>.)
	 * <p>
	 * This implementation uses the "brute force" technique of scanning over the source list,
	 * looking for a match with the target at each location in turn.
	 * 
	 * @param source the list in which to search for the first occurrence of <tt>target</tt>.
	 * @param target the list to search for as a subList of <tt>source</tt>.
	 * @return the starting position of the first occurrence of the specified target list within the
	 *         specified source list, or -1 if there is no such occurrence.
	 */
	public static int indexOfSubList(List<?> source, List<?> target) {
		return java.util.Collections.indexOfSubList(source, target);
	}

	/**
	 * Returns the starting position of the last occurrence of the specified target list within the
	 * specified source list, or -1 if there is no such occurrence. More formally, returns the
	 * highest index <tt>i</tt> such that <tt>source.subList(i, i+target.size()).equals(target)</tt>
	 * , or -1 if there is no such index. (Returns -1 if <tt>target.size() > source.size()</tt>.)
	 * <p>
	 * This implementation uses the "brute force" technique of iterating over the source list,
	 * looking for a match with the target at each location in turn.
	 * 
	 * @param source the list in which to search for the last occurrence of <tt>target</tt>.
	 * @param target the list to search for as a subList of <tt>source</tt>.
	 * @return the starting position of the last occurrence of the specified target list within the
	 *         specified source list, or -1 if there is no such occurrence.
	 */
	public static int lastIndexOfSubList(List<?> source, List<?> target) {
		return java.util.Collections.lastIndexOfSubList(source, target);
	}

	// Unmodifiable Wrappers

	/**
	 * Returns an unmodifiable view of the specified collection. This method allows modules to
	 * provide users with "read-only" access to internal collections. Query operations on the
	 * returned collection "read through" to the specified collection, and attempts to modify the
	 * returned collection, whether direct or via its iterator, result in an
	 * <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * The returned collection does <i>not</i> pass the hashCode and equals operations through to
	 * the backing collection, but relies on <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt>
	 * methods. This is necessary to preserve the contracts of these operations in the case that the
	 * backing collection is a set or a list.
	 * <p>
	 * The returned collection will be serializable if the specified collection is serializable.
	 * 
	 * @param c the collection for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified collection.
	 */
	public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c) {
		return java.util.Collections.unmodifiableCollection(c);
	}

	/**
	 * Returns an unmodifiable view of the specified set. This method allows modules to provide
	 * users with "read-only" access to internal sets. Query operations on the returned set
	 * "read through" to the specified set, and attempts to modify the returned set, whether direct
	 * or via its iterator, result in an <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * The returned set will be serializable if the specified set is serializable.
	 * 
	 * @param s the set for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified set.
	 */
	public static <T> Set<T> unmodifiableSet(Set<? extends T> s) {
		return java.util.Collections.unmodifiableSet(s);
	}

	/**
	 * Returns an unmodifiable view of the specified sorted set. This method allows modules to
	 * provide users with "read-only" access to internal sorted sets. Query operations on the
	 * returned sorted set "read through" to the specified sorted set. Attempts to modify the
	 * returned sorted set, whether direct, via its iterator, or via its <tt>subSet</tt>,
	 * <tt>headSet</tt>, or <tt>tailSet</tt> views, result in an
	 * <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * The returned sorted set will be serializable if the specified sorted set is serializable.
	 * 
	 * @param s the sorted set for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified sorted set.
	 */
	public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> s) {
		return java.util.Collections.unmodifiableSortedSet(s);
	}

	/**
	 * Returns an unmodifiable view of the specified list. This method allows modules to provide
	 * users with "read-only" access to internal lists. Query operations on the returned list
	 * "read through" to the specified list, and attempts to modify the returned list, whether
	 * direct or via its iterator, result in an <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * The returned list will be serializable if the specified list is serializable. Similarly, the
	 * returned list will implement {@link RandomAccess} if the specified list does.
	 * 
	 * @param list the list for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified list.
	 */
	public static <T> List<T> unmodifiableList(List<? extends T> list) {
		return java.util.Collections.unmodifiableList(list);
	}

	/**
	 * Returns an unmodifiable view of the specified map. This method allows modules to provide
	 * users with "read-only" access to internal maps. Query operations on the returned map
	 * "read through" to the specified map, and attempts to modify the returned map, whether direct
	 * or via its collection views, result in an <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * The returned map will be serializable if the specified map is serializable.
	 * 
	 * @param m the map for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified map.
	 */
	public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m) {
		return java.util.Collections.unmodifiableMap(m);
	}

	/**
	 * Returns an unmodifiable view of the specified sorted map. This method allows modules to
	 * provide users with "read-only" access to internal sorted maps. Query operations on the
	 * returned sorted map "read through" to the specified sorted map. Attempts to modify the
	 * returned sorted map, whether direct, via its collection views, or via its <tt>subMap</tt>,
	 * <tt>headMap</tt>, or <tt>tailMap</tt> views, result in an
	 * <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * The returned sorted map will be serializable if the specified sorted map is serializable.
	 * 
	 * @param m the sorted map for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified sorted map.
	 */
	public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> m) {
		return java.util.Collections.unmodifiableSortedMap(m);
	}

	/**
	 * Returns a synchronized (thread-safe) collection backed by the specified collection. In order
	 * to guarantee serial access, it is critical that <strong>all</strong> access to the backing
	 * collection is accomplished through the returned collection.
	 * <p>
	 * It is imperative that the user manually synchronize on the returned collection when iterating
	 * over it:
	 * 
	 * <pre>
	 *  Collection c = java.util.Collections.synchronizedCollection(myCollection);
	 *     ...
	 *  synchronized(c) {
	 *      Iterator i = c.iterator(); // Must be in the synchronized block
	 *      while (i.hasNext())
	 *         foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * <p>
	 * The returned collection does <i>not</i> pass the <tt>hashCode</tt> and <tt>equals</tt>
	 * operations through to the backing collection, but relies on <tt>Object</tt>'s equals and
	 * hashCode methods. This is necessary to preserve the contracts of these operations in the case
	 * that the backing collection is a set or a list.
	 * <p>
	 * The returned collection will be serializable if the specified collection is serializable.
	 * 
	 * @param c the collection to be "wrapped" in a synchronized collection.
	 * @return a synchronized view of the specified collection.
	 */
	public static <T> Collection<T> synchronizedCollection(Collection<T> c) {
		return java.util.Collections.synchronizedCollection(c);
	}

	/**
	 * Returns a synchronized (thread-safe) set backed by the specified set. In order to guarantee
	 * serial access, it is critical that <strong>all</strong> access to the backing set is
	 * accomplished through the returned set.
	 * <p>
	 * It is imperative that the user manually synchronize on the returned set when iterating over
	 * it:
	 * 
	 * <pre>
	 *  Set s = java.util.Collections.synchronizedSet(new HashSet());
	 *      ...
	 *  synchronized(s) {
	 *      Iterator i = s.iterator(); // Must be in the synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * <p>
	 * The returned set will be serializable if the specified set is serializable.
	 * 
	 * @param s the set to be "wrapped" in a synchronized set.
	 * @return a synchronized view of the specified set.
	 */
	public static <T> Set<T> synchronizedSet(Set<T> s) {
		return java.util.Collections.synchronizedSet(s);
	}

	/**
	 * Returns a synchronized (thread-safe) sorted set backed by the specified sorted set. In order
	 * to guarantee serial access, it is critical that <strong>all</strong> access to the backing
	 * sorted set is accomplished through the returned sorted set (or its views).
	 * <p>
	 * It is imperative that the user manually synchronize on the returned sorted set when iterating
	 * over it or any of its <tt>subSet</tt>, <tt>headSet</tt>, or <tt>tailSet</tt> views.
	 * 
	 * <pre>
	 *  SortedSet s = java.util.Collections.synchronizedSortedSet(new TreeSet());
	 *      ...
	 *  synchronized(s) {
	 *      Iterator i = s.iterator(); // Must be in the synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * or:
	 * 
	 * <pre>
	 *  SortedSet s = java.util.Collections.synchronizedSortedSet(new TreeSet());
	 *  SortedSet s2 = s.headSet(foo);
	 *      ...
	 *  synchronized(s) {  // Note: s, not s2!!!
	 *      Iterator i = s2.iterator(); // Must be in the synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * <p>
	 * The returned sorted set will be serializable if the specified sorted set is serializable.
	 * 
	 * @param s the sorted set to be "wrapped" in a synchronized sorted set.
	 * @return a synchronized view of the specified sorted set.
	 */
	public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s) {
		return java.util.Collections.synchronizedSortedSet(s);
	}

	/**
	 * Returns a synchronized (thread-safe) list backed by the specified list. In order to guarantee
	 * serial access, it is critical that <strong>all</strong> access to the backing list is
	 * accomplished through the returned list.
	 * <p>
	 * It is imperative that the user manually synchronize on the returned list when iterating over
	 * it:
	 * 
	 * <pre>
	 *  List list = java.util.Collections.synchronizedList(new ArrayList());
	 *      ...
	 *  synchronized(list) {
	 *      Iterator i = list.iterator(); // Must be in synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * <p>
	 * The returned list will be serializable if the specified list is serializable.
	 * 
	 * @param list the list to be "wrapped" in a synchronized list.
	 * @return a synchronized view of the specified list.
	 */
	public static <T> List<T> synchronizedList(List<T> list) {
		return java.util.Collections.synchronizedList(list);
	}

	/**
	 * Returns a synchronized (thread-safe) map backed by the specified map. In order to guarantee
	 * serial access, it is critical that <strong>all</strong> access to the backing map is
	 * accomplished through the returned map.
	 * <p>
	 * It is imperative that the user manually synchronize on the returned map when iterating over
	 * any of its collection views:
	 * 
	 * <pre>
	 *  Map m = java.util.Collections.synchronizedMap(new HashMap());
	 *      ...
	 *  Set s = m.keySet();  // Needn't be in synchronized block
	 *      ...
	 *  synchronized(m) {  // Synchronizing on m, not s!
	 *      Iterator i = s.iterator(); // Must be in synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * <p>
	 * The returned map will be serializable if the specified map is serializable.
	 * 
	 * @param m the map to be "wrapped" in a synchronized map.
	 * @return a synchronized view of the specified map.
	 */
	public static <K, V> Map<K, V> synchronizedMap(Map<K, V> m) {
		return java.util.Collections.synchronizedMap(m);
	}

	/**
	 * Returns a synchronized (thread-safe) sorted map backed by the specified sorted map. In order
	 * to guarantee serial access, it is critical that <strong>all</strong> access to the backing
	 * sorted map is accomplished through the returned sorted map (or its views).
	 * <p>
	 * It is imperative that the user manually synchronize on the returned sorted map when iterating
	 * over any of its collection views, or the collections views of any of its <tt>subMap</tt>,
	 * <tt>headMap</tt> or <tt>tailMap</tt> views.
	 * 
	 * <pre>
	 *  SortedMap m = java.util.Collections.synchronizedSortedMap(new TreeMap());
	 *      ...
	 *  Set s = m.keySet();  // Needn't be in synchronized block
	 *      ...
	 *  synchronized(m) {  // Synchronizing on m, not s!
	 *      Iterator i = s.iterator(); // Must be in synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * or:
	 * 
	 * <pre>
	 *  SortedMap m = java.util.Collections.synchronizedSortedMap(new TreeMap());
	 *  SortedMap m2 = m.subMap(foo, bar);
	 *      ...
	 *  Set s2 = m2.keySet();  // Needn't be in synchronized block
	 *      ...
	 *  synchronized(m) {  // Synchronizing on m, not m2 or s2!
	 *      Iterator i = s.iterator(); // Must be in synchronized block
	 *      while (i.hasNext())
	 *          foo(i.next());
	 *  }
	 * </pre>
	 * 
	 * Failure to follow this advice may result in non-deterministic behavior.
	 * <p>
	 * The returned sorted map will be serializable if the specified sorted map is serializable.
	 * 
	 * @param m the sorted map to be "wrapped" in a synchronized sorted map.
	 * @return a synchronized view of the specified sorted map.
	 */
	public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> m) {
		return java.util.Collections.synchronizedSortedMap(m);
	}

	/**
	 * Returns a dynamically typesafe view of the specified collection. Any attempt to insert an
	 * element of the wrong type will result in an immediate <tt>ClassCastException</tt>. Assuming a
	 * collection contains no incorrectly typed elements prior to the time a dynamically typesafe
	 * view is generated, and that all subsequent access to the collection takes place through the
	 * view, it is <i>guaranteed</i> that the collection cannot contain an incorrectly typed
	 * element.
	 * <p>
	 * The generics mechanism in the language provides compile-time (static) type checking, but it
	 * is possible to defeat this mechanism with unchecked casts. Usually this is not a problem, as
	 * the compiler issues warnings on all such unchecked operations. There are, however, times when
	 * static type checking alone is not sufficient. For example, suppose a collection is passed to
	 * a third-party library and it is imperative that the library code not corrupt the collection
	 * by inserting an element of the wrong type.
	 * <p>
	 * Another use of dynamically typesafe views is debugging. Suppose a program fails with a
	 * <tt>ClassCastException</tt>, indicating that an incorrectly typed element was put into a
	 * parameterized collection. Unfortunately, the exception can occur at any time after the
	 * erroneous element is inserted, so it typically provides little or no information as to the
	 * real source of the problem. If the problem is reproducible, one can quickly determine its
	 * source by temporarily modifying the program to wrap the collection with a dynamically
	 * typesafe view. For example, this declaration:
	 * 
	 * <pre>
	 * Collection&lt;String&gt; c = new HashSet&lt;String&gt;();
	 * </pre>
	 * 
	 * may be replaced temporarily by this one:
	 * 
	 * <pre>
	 * Collection&lt;String&gt; c = java.util.Collections.checkedCollection(new HashSet&lt;String&gt;(), String.class);
	 * </pre>
	 * 
	 * Running the program again will cause it to fail at the point where an incorrectly typed
	 * element is inserted into the collection, clearly identifying the source of the problem. Once
	 * the problem is fixed, the modified declaration may be reverted back to the original.
	 * <p>
	 * The returned collection does <i>not</i> pass the hashCode and equals operations through to
	 * the backing collection, but relies on <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt>
	 * methods. This is necessary to preserve the contracts of these operations in the case that the
	 * backing collection is a set or a list.
	 * <p>
	 * The returned collection will be serializable if the specified collection is serializable.
	 * 
	 * @param c the collection for which a dynamically typesafe view is to be returned
	 * @param type the type of element that <tt>c</tt> is permitted to hold
	 * @return a dynamically typesafe view of the specified collection
	 */
	public static <E> Collection<E> checkedCollection(Collection<E> c, Class<E> type) {
		return java.util.Collections.checkedCollection(c, type);
	}

	/**
	 * Returns a dynamically typesafe view of the specified set. Any attempt to insert an element of
	 * the wrong type will result in an immediate <tt>ClassCastException</tt>. Assuming a set
	 * contains no incorrectly typed elements prior to the time a dynamically typesafe view is
	 * generated, and that all subsequent access to the set takes place through the view, it is
	 * <i>guaranteed</i> that the set cannot contain an incorrectly typed element.
	 * <p>
	 * A discussion of the use of dynamically typesafe views may be found in the documentation for
	 * the {@link #checkedCollection checkedCollection} method.
	 * <p>
	 * The returned set will be serializable if the specified set is serializable.
	 * 
	 * @param s the set for which a dynamically typesafe view is to be returned
	 * @param type the type of element that <tt>s</tt> is permitted to hold
	 * @return a dynamically typesafe view of the specified set
	 */
	public static <E> Set<E> checkedSet(Set<E> s, Class<E> type) {
		return java.util.Collections.checkedSet(s, type);
	}

	/**
	 * Returns a dynamically typesafe view of the specified sorted set. Any attempt to insert an
	 * element of the wrong type will result in an immediate <tt>ClassCastException</tt>. Assuming a
	 * sorted set contains no incorrectly typed elements prior to the time a dynamically typesafe
	 * view is generated, and that all subsequent access to the sorted set takes place through the
	 * view, it is <i>guaranteed</i> that the sorted set cannot contain an incorrectly typed
	 * element.
	 * <p>
	 * A discussion of the use of dynamically typesafe views may be found in the documentation for
	 * the {@link #checkedCollection checkedCollection} method.
	 * <p>
	 * The returned sorted set will be serializable if the specified sorted set is serializable.
	 * 
	 * @param s the sorted set for which a dynamically typesafe view is to be returned
	 * @param type the type of element that <tt>s</tt> is permitted to hold
	 * @return a dynamically typesafe view of the specified sorted set
	 */
	public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> s, Class<E> type) {
		return java.util.Collections.checkedSortedSet(s, type);
	}

	/**
	 * Returns a dynamically typesafe view of the specified list. Any attempt to insert an element
	 * of the wrong type will result in an immediate <tt>ClassCastException</tt>. Assuming a list
	 * contains no incorrectly typed elements prior to the time a dynamically typesafe view is
	 * generated, and that all subsequent access to the list takes place through the view, it is
	 * <i>guaranteed</i> that the list cannot contain an incorrectly typed element.
	 * <p>
	 * A discussion of the use of dynamically typesafe views may be found in the documentation for
	 * the {@link #checkedCollection checkedCollection} method.
	 * <p>
	 * The returned list will be serializable if the specified list is serializable.
	 * 
	 * @param list the list for which a dynamically typesafe view is to be returned
	 * @param type the type of element that <tt>list</tt> is permitted to hold
	 * @return a dynamically typesafe view of the specified list
	 */
	public static <E> List<E> checkedList(List<E> list, Class<E> type) {
		return java.util.Collections.checkedList(list, type);
	}

	/**
	 * Returns a dynamically typesafe view of the specified map. Any attempt to insert a mapping
	 * whose key or value have the wrong type will result in an immediate
	 * <tt>ClassCastException</tt>. Similarly, any attempt to modify the value currently associated
	 * with a key will result in an immediate <tt>ClassCastException</tt>, whether the modification
	 * is attempted directly through the map itself, or through a {@link java.util.Map.Entry} instance
	 * obtained from the map's {@link Map#entrySet() entry set} view.
	 * <p>
	 * Assuming a map contains no incorrectly typed keys or values prior to the time a dynamically
	 * typesafe view is generated, and that all subsequent access to the map takes place through the
	 * view (or one of its collection views), it is <i>guaranteed</i> that the map cannot contain an
	 * incorrectly typed key or value.
	 * <p>
	 * A discussion of the use of dynamically typesafe views may be found in the documentation for
	 * the {@link #checkedCollection checkedCollection} method.
	 * <p>
	 * The returned map will be serializable if the specified map is serializable.
	 * 
	 * @param m the map for which a dynamically typesafe view is to be returned
	 * @param keyType the type of key that <tt>m</tt> is permitted to hold
	 * @param valueType the type of value that <tt>m</tt> is permitted to hold
	 * @return a dynamically typesafe view of the specified map
	 */
	public static <K, V> Map<K, V> checkedMap(Map<K, V> m, Class<K> keyType, Class<V> valueType) {
		return java.util.Collections.checkedMap(m, keyType, valueType);
	}

	/**
	 * Returns a dynamically typesafe view of the specified sorted map. Any attempt to insert a
	 * mapping whose key or value have the wrong type will result in an immediate
	 * <tt>ClassCastException</tt>. Similarly, any attempt to modify the value currently associated
	 * with a key will result in an immediate <tt>ClassCastException</tt>, whether the modification
	 * is attempted directly through the map itself, or through a {@link java.util.Map.Entry} instance
	 * obtained from the map's {@link Map#entrySet() entry set} view.
	 * <p>
	 * Assuming a map contains no incorrectly typed keys or values prior to the time a dynamically
	 * typesafe view is generated, and that all subsequent access to the map takes place through the
	 * view (or one of its collection views), it is <i>guaranteed</i> that the map cannot contain an
	 * incorrectly typed key or value.
	 * <p>
	 * A discussion of the use of dynamically typesafe views may be found in the documentation for
	 * the {@link #checkedCollection checkedCollection} method.
	 * <p>
	 * The returned map will be serializable if the specified map is serializable.
	 * 
	 * @param m the map for which a dynamically typesafe view is to be returned
	 * @param keyType the type of key that <tt>m</tt> is permitted to hold
	 * @param valueType the type of value that <tt>m</tt> is permitted to hold
	 * @return a dynamically typesafe view of the specified map
	 */
	public static <K, V> SortedMap<K, V> checkedSortedMap(SortedMap<K, V> m, Class<K> keyType,
			Class<V> valueType) {
		return java.util.Collections.checkedSortedMap(m, keyType, valueType);
	}

	// Miscellaneous

	/**
	 * The empty set (immutable). This set is serializable.
	 * 
	 * @see #emptySet()
	 */
	public static final Set EMPTY_SET = java.util.Collections.EMPTY_SET;

	/**
	 * Returns the empty set (immutable). This set is serializable. Unlike the like-named field,
	 * this method is parameterized.
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty set:
	 * 
	 * <pre>
	 * Set&lt;String&gt; s = java.util.Collections.emptySet();
	 * </pre>
	 * 
	 * Implementation note: Implementations of this method need not create a separate <tt>Set</tt>
	 * object for each call. Using this method is likely to have comparable cost to using the
	 * like-named field. (Unlike this method, the field does not provide type safety.)
	 * 
	 * @see #EMPTY_SET
	 */
	public static final <T> Set<T> emptySet() {
		return java.util.Collections.emptySet();
	}

	/**
	 * The empty list (immutable). This list is serializable.
	 * 
	 * @see #emptyList()
	 */
	public static final List EMPTY_LIST = java.util.Collections.EMPTY_LIST;

	/**
	 * Returns the empty list (immutable). This list is serializable.
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty list:
	 * 
	 * <pre>
	 * List&lt;String&gt; s = java.util.Collections.emptyList();
	 * </pre>
	 * 
	 * Implementation note: Implementations of this method need not create a separate <tt>List</tt>
	 * object for each call. Using this method is likely to have comparable cost to using the
	 * like-named field. (Unlike this method, the field does not provide type safety.)
	 * 
	 * @see #EMPTY_LIST
	 */
	public static final <T> List<T> emptyList() {
		return java.util.Collections.emptyList();
	}

	/**
	 * The empty map (immutable). This map is serializable.
	 * 
	 * @see #emptyMap()
	 */
	public static final Map EMPTY_MAP = java.util.Collections.EMPTY_MAP;

	/**
	 * Returns the empty map (immutable). This map is serializable.
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty set:
	 * 
	 * <pre>
	 * Map&lt;String, Date&gt; s = java.util.Collections.emptyMap();
	 * </pre>
	 * 
	 * Implementation note: Implementations of this method need not create a separate <tt>Map</tt>
	 * object for each call. Using this method is likely to have comparable cost to using the
	 * like-named field. (Unlike this method, the field does not provide type safety.)
	 * 
	 * @see #EMPTY_MAP
	 */
	public static final <K, V> Map<K, V> emptyMap() {
		return java.util.Collections.emptyMap();
	}

	/**
	 * Returns an immutable list containing only the specified object. The returned list is
	 * serializable.
	 * 
	 * @param o the sole object to be stored in the returned list.
	 * @return an immutable list containing only the specified object.
	 */
	public static <T> Set<T> singleton(T o) {
		return java.util.Collections.singleton(o);
	}

	/**
	 * Returns an immutable list containing only the specified object. The returned list is
	 * serializable.
	 * 
	 * @param o the sole object to be stored in the returned list.
	 * @return an immutable list containing only the specified object.
	 */
	public static <T> List<T> singletonList(T o) {
		return java.util.Collections.singletonList(o);
	}

	/**
	 * Returns an immutable map, mapping only the specified key to the specified value. The returned
	 * map is serializable.
	 * 
	 * @param key the sole key to be stored in the returned map.
	 * @param value the value to which the returned map maps <tt>key</tt>.
	 * @return an immutable map containing only the specified key-value mapping.
	 */
	public static <K, V> Map<K, V> singletonMap(K key, V value) {
		return java.util.Collections.singletonMap(key, value);
	}

	/**
	 * Returns an immutable list consisting of <tt>n</tt> copies of the specified object. The newly
	 * allocated data object is tiny (it contains a single reference to the data object). This
	 * method is useful in combination with the <tt>List.addAll</tt> method to grow lists. The
	 * returned list is serializable.
	 * 
	 * @param n the number of elements in the returned list.
	 * @param o the element to appear repeatedly in the returned list.
	 * @return an immutable list consisting of <tt>n</tt> copies of the specified object.
	 * @throws IllegalArgumentException if n &lt; 0.
	 * @see List#addAll(Collection)
	 * @see List#addAll(int, Collection)
	 */
	public static <T> List<T> nCopies(int n, T o) {
		return java.util.Collections.nCopies(n, o);
	}

	/**
	 * Returns a comparator that imposes the reverse of the <i>natural ordering</i> on a collection
	 * of objects that implement the <tt>Comparable</tt> interface. (The natural ordering is the
	 * ordering imposed by the objects' own <tt>compareTo</tt> method.) This enables a simple idiom
	 * for sorting (or maintaining) collections (or arrays) of objects that implement the
	 * <tt>Comparable</tt> interface in reverse-natural-order. For example, suppose a is an array of
	 * strings. Then:
	 * 
	 * <pre>
	 * Arrays.sort(a, java.util.Collections.reverseOrder());
	 * </pre>
	 * 
	 * sorts the array in reverse-lexicographic (alphabetical) order.
	 * <p>
	 * The returned comparator is serializable.
	 * 
	 * @return a comparator that imposes the reverse of the <i>natural ordering</i> on a collection
	 *         of objects that implement the <tt>Comparable</tt> interface.
	 * @see Comparable
	 */
	public static <T> Comparator<T> reverseOrder() {
		return java.util.Collections.reverseOrder();
	}

	/**
	 * Returns a comparator that imposes the reverse ordering of the specified comparator. If the
	 * specified comparator is null, this method is equivalent to {@link #reverseOrder()} (in other
	 * words, it returns a comparator that imposes the reverse of the <i>natural ordering</i> on a
	 * collection of objects that implement the Comparable interface).
	 * <p>
	 * The returned comparator is serializable (assuming the specified comparator is also
	 * serializable or null).
	 * 
	 * @return a comparator that imposes the reverse ordering of the specified comparator.
	 */
	public static <T> Comparator<T> reverseOrder(Comparator<T> cmp) {
		return java.util.Collections.reverseOrder(cmp);
	}

	/**
	 * Returns an enumeration over the specified collection. This provides interoperability with
	 * legacy APIs that require an enumeration as input.
	 * 
	 * @param c the collection for which an enumeration is to be returned.
	 * @return an enumeration over the specified collection.
	 * @see Enumeration
	 */
	public static <T> Enumeration<T> enumeration(final Collection<T> c) {
		return java.util.Collections.enumeration(c);
	}

	/**
	 * Returns an array list containing the elements returned by the specified enumeration in the
	 * order they are returned by the enumeration. This method provides interoperability between
	 * legacy APIs that return enumerations and new APIs that require collections.
	 * 
	 * @param e enumeration providing elements for the returned array list
	 * @return an array list containing the elements returned by the specified enumeration.
	 * @see Enumeration
	 * @see ArrayList
	 */
	public static <T> ArrayList<T> list(Enumeration<T> e) {
		return java.util.Collections.list(e);
	}

	/**
	 * Returns the number of elements in the specified collection equal to the specified object.
	 * More formally, returns the number of elements <tt>e</tt> in the collection such that
	 * <tt>(o == null ? e == null : o.equals(e))</tt>.
	 * 
	 * @param c the collection in which to determine the frequency of <tt>o</tt>
	 * @param o the object whose frequency is to be determined
	 * @throws NullPointerException if <tt>c</tt> is null
	 */
	public static int frequency(Collection<?> c, Object o) {
		return java.util.Collections.frequency(c, o);
	}

	/**
	 * Returns <tt>true</tt> if the two specified collections have no elements in common.
	 * <p>
	 * Care must be exercised if this method is used on collections that do not comply with the
	 * general contract for <tt>Collection</tt>. Implementations may elect to iterate over either
	 * collection and test for containment in the other collection (or to perform any equivalent
	 * computation). If either collection uses a nonstandard equality test (as does a
	 * {@link SortedSet} whose ordering is not <i>compatible with equals</i>, or the key set of an
	 * {@link IdentityHashMap}), both collections must use the same nonstandard equality test, or
	 * the result of this method is undefined.
	 * <p>
	 * Note that it is permissible to pass the same collection in both parameters, in which case the
	 * method will return true if and only if the collection is empty.
	 * 
	 * @param c1 a collection
	 * @param c2 a collection
	 * @throws NullPointerException if either collection is null
	 */
	public static boolean disjoint(Collection<?> c1, Collection<?> c2) {
		return java.util.Collections.disjoint(c1, c2);
	}

	/**
	 * Adds all of the specified elements to the specified collection. Elements to be added may be
	 * specified individually or as an array. The behavior of this convenience method is identical
	 * to that of <tt>c.addAll(Arrays.asList(elements))</tt>, but this method is likely to run
	 * significantly faster under most implementations.
	 * <p>
	 * When elements are specified individually, this method provides a convenient way to add a few
	 * elements to an existing collection:
	 * 
	 * <pre>
	 * java.util.Collections.addAll(flavors, &quot;Peaches 'n Plutonium&quot;, &quot;Rocky Racoon&quot;);
	 * </pre>
	 * 
	 * @param c the collection into which <tt>elements</tt> are to be inserted
	 * @param elements the elements to insert into <tt>c</tt>
	 * @return <tt>true</tt> if the collection changed as a result of the call
	 * @throws UnsupportedOperationException if <tt>c</tt> does not support the <tt>add</tt>
	 *             operation
	 * @throws NullPointerException if <tt>elements</tt> contains one or more null values and
	 *             <tt>c</tt> does not permit null elements, or if <tt>c</tt> or <tt>elements</tt>
	 *             are <tt>null</tt>
	 * @throws IllegalArgumentException if some property of a value in <tt>elements</tt> prevents it
	 *             from being added to <tt>c</tt>
	 * @see Collection#addAll(Collection)
	 */
	public static <T> boolean addAll(Collection<? super T> c, T... elements) {
		return java.util.Collections.addAll(c, elements);
	}

	/**
	 * Returns a set backed by the specified map. The resulting set displays the same ordering,
	 * concurrency, and performance characteristics as the backing map. In essence, this factory
	 * method provides a {@link Set} implementation corresponding to any {@link Map} implementation.
	 * There is no need to use this method on a {@link Map} implementation that already has a
	 * corresponding {@link Set} implementation (such as {@link HashMap} or {@link TreeMap}).
	 * <p>
	 * Each method invocation on the set returned by this method results in exactly one method
	 * invocation on the backing map or its <tt>keySet</tt> view, with one exception. The
	 * <tt>addAll</tt> method is implemented as a sequence of <tt>put</tt> invocations on the
	 * backing map.
	 * <p>
	 * The specified map must be empty at the time this method is invoked, and should not be
	 * accessed directly after this method returns. These conditions are ensured if the map is
	 * created empty, passed directly to this method, and no reference to the map is retained, as
	 * illustrated in the following code fragment:
	 * 
	 * <pre>
	 * Set&lt;Object&gt; weakHashSet = java.util.Collections.newSetFromMap(new WeakHashMap&lt;Object, Boolean&gt;());
	 * </pre>
	 * 
	 * @param map the backing map
	 * @return the set backed by the map
	 * @throws IllegalArgumentException if <tt>map</tt> is not empty
	 */
	public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
		return java.util.Collections.newSetFromMap(map);
	}

	/**
	 * Returns a view of a {@link Deque} as a Last-in-first-out (Lifo) {@link Queue}. Method
	 * <tt>add</tt> is mapped to <tt>push</tt>, <tt>remove</tt> is mapped to <tt>pop</tt> and so on.
	 * This view can be useful when you would like to use a method requiring a <tt>Queue</tt> but
	 * you need Lifo ordering.
	 * <p>
	 * Each method invocation on the queue returned by this method results in exactly one method
	 * invocation on the backing deque, with one exception. The {@link Queue#addAll addAll} method
	 * is implemented as a sequence of {@link Deque#addFirst addFirst} invocations on the backing
	 * deque.
	 * 
	 * @param deque the deque
	 * @return the queue
	 */
	public static <T> Queue<T> asLifoQueue(Deque<T> deque) {
		return java.util.Collections.asLifoQueue(deque);
	}
}
