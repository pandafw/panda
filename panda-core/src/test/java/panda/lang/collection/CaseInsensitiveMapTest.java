package panda.lang.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;

public class CaseInsensitiveMapTest<K, V> extends AbstractMapTest<K, V> {

    public CaseInsensitiveMapTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(CaseInsensitiveMapTest.class);
    }

    @Override
    public Map<K, V> makeObject() {
        return new CaseInsensitiveMap<K, V>();
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //-------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void testCaseInsensitive() {
        final Map<K, V> map = makeObject();
        map.put((K) "One", (V) "One");
        map.put((K) "Two", (V) "Two");
        assertEquals("One", map.get("one"));
        assertEquals("One", map.get("oNe"));
        map.put((K) "two", (V) "Three");
        assertEquals("Three", map.get("Two"));
    }

    @SuppressWarnings("unchecked")
    public void testNullHandling() {
        final Map<K, V> map = makeObject();
        map.put((K) "One", (V) "One");
        map.put((K) "Two", (V) "Two");
        map.put(null, (V) "Three");
        assertEquals("Three", map.get(null));
        map.put(null, (V) "Four");
        assertEquals("Four", map.get(null));
        final Set<K> keys = map.keySet();
        assertTrue(keys.contains("one"));
        assertTrue(keys.contains("two"));
        assertTrue(keys.contains(null));
        assertEquals(3, keys.size());
    }

    public void testPutAll() {
        final Map<Object, String> map = new HashMap<Object, String>();
        map.put("One", "One");
        map.put("Two", "Two");
        map.put("one", "Three");
        map.put(null, "Four");
        map.put(Integer.valueOf(20), "Five");
        final Map<Object, String> caseInsensitiveMap = new CaseInsensitiveMap<Object, String>(map);
        assertEquals(4, caseInsensitiveMap.size()); // ones collapsed
        final Set<Object> keys = caseInsensitiveMap.keySet();
        assertTrue(keys.contains("one"));
        assertTrue(keys.contains("two"));
        assertTrue(keys.contains(null));
        assertTrue(keys.contains(Integer.valueOf(20)));
        assertEquals(4, keys.size());
        assertTrue(!caseInsensitiveMap.containsValue("One")
            || !caseInsensitiveMap.containsValue("Three")); // ones collaped
        assertEquals("Four", caseInsensitiveMap.get(null));
    }

    @SuppressWarnings("unchecked")
    public void testClone() {
        final CaseInsensitiveMap<K, V> map = new CaseInsensitiveMap<K, V>();
        map.put((K) "1", (V) "1");
        final CaseInsensitiveMap<K, V> cloned = map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get("1"), cloned.get("1"));
    }

}
