package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import panda.lang.Arrays;

/**
 * Tests Arrays add methods.
 *
 */
public class ArraysAddTest {

    @Test
    public void testJira567(){
        Number[] n;
        // Valid array construction
        n = Arrays.addAll(new Number[]{Integer.valueOf(1)}, new Long[]{Long.valueOf(2)});
        assertEquals(2,n.length);
        assertEquals(Number.class,n.getClass().getComponentType());
        try {
            // Invalid - can't store Long in Integer array
               n = Arrays.addAll(new Integer[]{Integer.valueOf(1)}, new Long[]{Long.valueOf(2)});
               fail("Should have generated IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testAddObjectArrayBoolean() {
        boolean[] newArray;
        newArray = Arrays.add((boolean[])null, false);
        assertTrue(Arrays.equals(new boolean[]{false}, newArray));
        assertEquals(Boolean.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((boolean[])null, true);
        assertTrue(Arrays.equals(new boolean[]{true}, newArray));
        assertEquals(Boolean.TYPE, newArray.getClass().getComponentType());
        final boolean[] array1 = new boolean[]{true, false, true};
        newArray = Arrays.add(array1, false);
        assertTrue(Arrays.equals(new boolean[]{true, false, true, false}, newArray));
        assertEquals(Boolean.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayByte() {
        byte[] newArray;
        newArray = Arrays.add((byte[])null, (byte)0);
        assertTrue(Arrays.equals(new byte[]{0}, newArray));
        assertEquals(Byte.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((byte[])null, (byte)1);
        assertTrue(Arrays.equals(new byte[]{1}, newArray));
        assertEquals(Byte.TYPE, newArray.getClass().getComponentType());
        final byte[] array1 = new byte[]{1, 2, 3};
        newArray = Arrays.add(array1, (byte)0);
        assertTrue(Arrays.equals(new byte[]{1, 2, 3, 0}, newArray));
        assertEquals(Byte.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, (byte)4);
        assertTrue(Arrays.equals(new byte[]{1, 2, 3, 4}, newArray));
        assertEquals(Byte.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayChar() {
        char[] newArray;
        newArray = Arrays.add((char[])null, (char)0);
        assertTrue(Arrays.equals(new char[]{0}, newArray));
        assertEquals(Character.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((char[])null, (char)1);
        assertTrue(Arrays.equals(new char[]{1}, newArray));
        assertEquals(Character.TYPE, newArray.getClass().getComponentType());
        final char[] array1 = new char[]{1, 2, 3};
        newArray = Arrays.add(array1, (char)0);
        assertTrue(Arrays.equals(new char[]{1, 2, 3, 0}, newArray));
        assertEquals(Character.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, (char)4);
        assertTrue(Arrays.equals(new char[]{1, 2, 3, 4}, newArray));
        assertEquals(Character.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayDouble() {
        double[] newArray;
        newArray = Arrays.add((double[])null, 0);
        assertTrue(Arrays.equals(new double[]{0}, newArray));
        assertEquals(Double.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((double[])null, 1);
        assertTrue(Arrays.equals(new double[]{1}, newArray));
        assertEquals(Double.TYPE, newArray.getClass().getComponentType());
        final double[] array1 = new double[]{1, 2, 3};
        newArray = Arrays.add(array1, 0);
        assertTrue(Arrays.equals(new double[]{1, 2, 3, 0}, newArray));
        assertEquals(Double.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, 4);
        assertTrue(Arrays.equals(new double[]{1, 2, 3, 4}, newArray));
        assertEquals(Double.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayFloat() {
        float[] newArray;
        newArray = Arrays.add((float[])null, 0);
        assertTrue(Arrays.equals(new float[]{0}, newArray));
        assertEquals(Float.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((float[])null, 1);
        assertTrue(Arrays.equals(new float[]{1}, newArray));
        assertEquals(Float.TYPE, newArray.getClass().getComponentType());
        final float[] array1 = new float[]{1, 2, 3};
        newArray = Arrays.add(array1, 0);
        assertTrue(Arrays.equals(new float[]{1, 2, 3, 0}, newArray));
        assertEquals(Float.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, 4);
        assertTrue(Arrays.equals(new float[]{1, 2, 3, 4}, newArray));
        assertEquals(Float.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayInt() {
        int[] newArray;
        newArray = Arrays.add((int[])null, 0);
        assertTrue(Arrays.equals(new int[]{0}, newArray));
        assertEquals(Integer.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((int[])null, 1);
        assertTrue(Arrays.equals(new int[]{1}, newArray));
        assertEquals(Integer.TYPE, newArray.getClass().getComponentType());
        final int[] array1 = new int[]{1, 2, 3};
        newArray = Arrays.add(array1, 0);
        assertTrue(Arrays.equals(new int[]{1, 2, 3, 0}, newArray));
        assertEquals(Integer.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, 4);
        assertTrue(Arrays.equals(new int[]{1, 2, 3, 4}, newArray));
        assertEquals(Integer.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayLong() {
        long[] newArray;
        newArray = Arrays.add((long[])null, 0);
        assertTrue(Arrays.equals(new long[]{0}, newArray));
        assertEquals(Long.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((long[])null, 1);
        assertTrue(Arrays.equals(new long[]{1}, newArray));
        assertEquals(Long.TYPE, newArray.getClass().getComponentType());
        final long[] array1 = new long[]{1, 2, 3};
        newArray = Arrays.add(array1, 0);
        assertTrue(Arrays.equals(new long[]{1, 2, 3, 0}, newArray));
        assertEquals(Long.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, 4);
        assertTrue(Arrays.equals(new long[]{1, 2, 3, 4}, newArray));
        assertEquals(Long.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayShort() {
        short[] newArray;
        newArray = Arrays.add((short[])null, (short)0);
        assertTrue(Arrays.equals(new short[]{0}, newArray));
        assertEquals(Short.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add((short[])null, (short)1);
        assertTrue(Arrays.equals(new short[]{1}, newArray));
        assertEquals(Short.TYPE, newArray.getClass().getComponentType());
        final short[] array1 = new short[]{1, 2, 3};
        newArray = Arrays.add(array1, (short)0);
        assertTrue(Arrays.equals(new short[]{1, 2, 3, 0}, newArray));
        assertEquals(Short.TYPE, newArray.getClass().getComponentType());
        newArray = Arrays.add(array1, (short)4);
        assertTrue(Arrays.equals(new short[]{1, 2, 3, 4}, newArray));
        assertEquals(Short.TYPE, newArray.getClass().getComponentType());
    }

    @Test
    public void testAddObjectArrayObject() {
        Object[] newArray;

        //show that not casting is okay
        newArray = Arrays.add((Object[])null, "a");
        assertTrue(Arrays.equals(new String[]{"a"}, newArray));
        assertTrue(!Arrays.equals(new Object[]{"a"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());

        //show that not casting to Object[] is okay and will assume String based on "a"
        final String[] newStringArray = Arrays.add(null, "a");
        assertTrue(Arrays.equals(new String[]{"a"}, newStringArray));
        assertTrue(!Arrays.equals(new Object[]{"a"}, newStringArray));
        assertEquals(String.class, newStringArray.getClass().getComponentType());

        final String[] stringArray1 = new String[]{"a", "b", "c"};
        newArray = Arrays.add(stringArray1, null);
        assertTrue(Arrays.equals(new String[]{"a", "b", "c", null}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());

        newArray = Arrays.add(stringArray1, "d");
        assertTrue(Arrays.equals(new String[]{"a", "b", "c", "d"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());

        Number[] numberArray1 = new Number[]{Integer.valueOf(1), Double.valueOf(2)};
        newArray = Arrays.add(numberArray1, Float.valueOf(3));
        assertTrue(Arrays.equals(new Number[]{Integer.valueOf(1), Double.valueOf(2), Float.valueOf(3)}, newArray));
        assertEquals(Number.class, newArray.getClass().getComponentType());

        numberArray1 = null;
        newArray = Arrays.add(numberArray1, Float.valueOf(3));
        assertTrue(Arrays.equals(new Float[]{Float.valueOf(3)}, newArray));
        assertEquals(Float.class, newArray.getClass().getComponentType());
    }
    
    @Test
    public void testLANG571(){
        final String[] stringArray=null;
        final String aString=null;
        try {
            @SuppressWarnings("unused")
            final
            String[] sa = Arrays.add(stringArray, aString);
            fail("Should have caused IllegalArgumentException");
        } catch (final IllegalArgumentException iae){
            //expected
        }
        try {
            @SuppressWarnings("unused")
            final
            String[] sa = Arrays.add(stringArray, 0, aString);
            fail("Should have caused IllegalArgumentException");
        } catch (final IllegalArgumentException iae){
            //expected
        }
    }

    @Test
    public void testAddObjectArrayToObjectArray() {
        assertNull(Arrays.addAll((Object[]) null, (Object[]) null));
        Object[] newArray;
        final String[] stringArray1 = new String[]{"a", "b", "c"};
        final String[] stringArray2 = new String[]{"1", "2", "3"};
        newArray = Arrays.addAll(stringArray1, (String[]) null);
        assertNotSame(stringArray1, newArray);
        assertTrue(Arrays.equals(stringArray1, newArray));
        assertTrue(Arrays.equals(new String[]{"a", "b", "c"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.addAll(null, stringArray2);
        assertNotSame(stringArray2, newArray);
        assertTrue(Arrays.equals(stringArray2, newArray));
        assertTrue(Arrays.equals(new String[]{"1", "2", "3"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.addAll(stringArray1, stringArray2);
        assertTrue(Arrays.equals(new String[]{"a", "b", "c", "1", "2", "3"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.addAll(Arrays.EMPTY_STRING_ARRAY, (String[]) null);
        assertTrue(Arrays.equals(Arrays.EMPTY_STRING_ARRAY, newArray));
        assertTrue(Arrays.equals(new String[]{}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.addAll(null, Arrays.EMPTY_STRING_ARRAY);
        assertTrue(Arrays.equals(Arrays.EMPTY_STRING_ARRAY, newArray));
        assertTrue(Arrays.equals(new String[]{}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.addAll(Arrays.EMPTY_STRING_ARRAY, Arrays.EMPTY_STRING_ARRAY);
        assertTrue(Arrays.equals(Arrays.EMPTY_STRING_ARRAY, newArray));
        assertTrue(Arrays.equals(new String[]{}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        final String[] stringArrayNull = new String []{null};
        newArray = Arrays.addAll(stringArrayNull, stringArrayNull);
        assertTrue(Arrays.equals(new String[]{null, null}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());

        // boolean
        assertTrue( Arrays.equals( new boolean[] { true, false, false, true },
            Arrays.addAll( new boolean[] { true, false }, new boolean[] { false, true } ) ) );

        assertTrue( Arrays.equals( new boolean[] { false, true },
            Arrays.addAll( null, new boolean[] { false, true } ) ) );

        assertTrue( Arrays.equals( new boolean[] { true, false },
            Arrays.addAll( new boolean[] { true, false }, null ) ) );

        // char
        assertTrue( Arrays.equals( new char[] { 'a', 'b', 'c', 'd' },
            Arrays.addAll( new char[] { 'a', 'b' }, new char[] { 'c', 'd' } ) ) );

        assertTrue( Arrays.equals( new char[] { 'c', 'd' },
            Arrays.addAll( null, new char[] { 'c', 'd' } ) ) );

        assertTrue( Arrays.equals( new char[] { 'a', 'b' },
            Arrays.addAll( new char[] { 'a', 'b' }, null ) ) );

        // byte
        assertTrue( Arrays.equals( new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3 },
            Arrays.addAll( new byte[] { (byte) 0, (byte) 1 }, new byte[] { (byte) 2, (byte) 3 } ) ) );

        assertTrue( Arrays.equals( new byte[] { (byte) 2, (byte) 3 },
            Arrays.addAll( null, new byte[] { (byte) 2, (byte) 3 } ) ) );

        assertTrue( Arrays.equals( new byte[] { (byte) 0, (byte) 1 },
            Arrays.addAll( new byte[] { (byte) 0, (byte) 1 }, null ) ) );

        // short
        assertTrue( Arrays.equals( new short[] { (short) 10, (short) 20, (short) 30, (short) 40 },
            Arrays.addAll( new short[] { (short) 10, (short) 20 }, new short[] { (short) 30, (short) 40 } ) ) );

        assertTrue( Arrays.equals( new short[] { (short) 30, (short) 40 },
            Arrays.addAll( null, new short[] { (short) 30, (short) 40 } ) ) );

        assertTrue( Arrays.equals( new short[] { (short) 10, (short) 20 },
            Arrays.addAll( new short[] { (short) 10, (short) 20 }, null ) ) );

        // int
        assertTrue( Arrays.equals( new int[] { 1, 1000, -1000, -1 },
            Arrays.addAll( new int[] { 1, 1000 }, new int[] { -1000, -1 } ) ) );

        assertTrue( Arrays.equals( new int[] { -1000, -1 },
            Arrays.addAll( null, new int[] { -1000, -1 } ) ) );

        assertTrue( Arrays.equals( new int[] { 1, 1000 },
            Arrays.addAll( new int[] { 1, 1000 }, null ) ) );

        // long
        assertTrue( Arrays.equals( new long[] { 1L, -1L, 1000L, -1000L },
            Arrays.addAll( new long[] { 1L, -1L }, new long[] { 1000L, -1000L } ) ) );

        assertTrue( Arrays.equals( new long[] { 1000L, -1000L },
            Arrays.addAll( null, new long[] { 1000L, -1000L } ) ) );

        assertTrue( Arrays.equals( new long[] { 1L, -1L },
            Arrays.addAll( new long[] { 1L, -1L }, null ) ) );

        // float
        assertTrue( Arrays.equals( new float[] { 10.5f, 10.1f, 1.6f, 0.01f },
            Arrays.addAll( new float[] { 10.5f, 10.1f }, new float[] { 1.6f, 0.01f } ) ) );

        assertTrue( Arrays.equals( new float[] { 1.6f, 0.01f },
            Arrays.addAll( null, new float[] { 1.6f, 0.01f } ) ) );

        assertTrue( Arrays.equals( new float[] { 10.5f, 10.1f },
            Arrays.addAll( new float[] { 10.5f, 10.1f }, null ) ) );

        // double
        assertTrue( Arrays.equals( new double[] { Math.PI, -Math.PI, 0, 9.99 },
            Arrays.addAll( new double[] { Math.PI, -Math.PI }, new double[] { 0, 9.99 } ) ) );

        assertTrue( Arrays.equals( new double[] { 0, 9.99 },
            Arrays.addAll( null, new double[] { 0, 9.99 } ) ) );

        assertTrue( Arrays.equals( new double[] { Math.PI, -Math.PI },
            Arrays.addAll( new double[] { Math.PI, -Math.PI }, null ) ) );

    }

    @Test
    public void testAddObjectAtIndex() {
        Object[] newArray;
        newArray = Arrays.add((Object[])null, 0, "a");
        assertTrue(Arrays.equals(new String[]{"a"}, newArray));
        assertTrue(!Arrays.equals(new Object[]{"a"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        final String[] stringArray1 = new String[]{"a", "b", "c"};
        newArray = Arrays.add(stringArray1, 0, null);
        assertTrue(Arrays.equals(new String[]{null, "a", "b", "c"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.add(stringArray1, 1, null);
        assertTrue(Arrays.equals(new String[]{"a", null, "b", "c"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.add(stringArray1, 3, null);
        assertTrue(Arrays.equals(new String[]{"a", "b", "c", null}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        newArray = Arrays.add(stringArray1, 3, "d");
        assertTrue(Arrays.equals(new String[]{"a", "b", "c", "d"}, newArray));
        assertEquals(String.class, newArray.getClass().getComponentType());
        assertEquals(String.class, newArray.getClass().getComponentType());

        final Object[] o = new Object[] {"1", "2", "4"};
        final Object[] result = Arrays.add(o, 2, "3");
        final Object[] result2 = Arrays.add(o, 3, "5");

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("1", result[0]);
        assertEquals("2", result[1]);
        assertEquals("3", result[2]);
        assertEquals("4", result[3]);
        assertNotNull(result2);
        assertEquals(4, result2.length);
        assertEquals("1", result2[0]);
        assertEquals("2", result2[1]);
        assertEquals("4", result2[2]);
        assertEquals("5", result2[3]);

        // boolean tests
        boolean[] booleanArray = Arrays.add( null, 0, true );
        assertTrue( Arrays.equals( new boolean[] { true }, booleanArray ) );
        try {
            booleanArray = Arrays.add( null, -1, true );
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        booleanArray = Arrays.add( new boolean[] { true }, 0, false);
        assertTrue( Arrays.equals( new boolean[] { false, true }, booleanArray ) );
        booleanArray = Arrays.add( new boolean[] { false }, 1, true);
        assertTrue( Arrays.equals( new boolean[] { false, true }, booleanArray ) );
        booleanArray = Arrays.add( new boolean[] { true, false }, 1, true);
        assertTrue( Arrays.equals( new boolean[] { true, true, false }, booleanArray ) );
        try {
            booleanArray = Arrays.add( new boolean[] { true, false }, 4, true);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            booleanArray = Arrays.add( new boolean[] { true, false }, -1, true);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // char tests
        char[] charArray = Arrays.add( (char[]) null, 0, 'a' );
        assertTrue( Arrays.equals( new char[] { 'a' }, charArray ) );
        try {
            charArray = Arrays.add( (char[]) null, -1, 'a' );
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        charArray = Arrays.add( new char[] { 'a' }, 0, 'b');
        assertTrue( Arrays.equals( new char[] { 'b', 'a' }, charArray ) );
        charArray = Arrays.add( new char[] { 'a', 'b' }, 0, 'c');
        assertTrue( Arrays.equals( new char[] { 'c', 'a', 'b' }, charArray ) );
        charArray = Arrays.add( new char[] { 'a', 'b' }, 1, 'k');
        assertTrue( Arrays.equals( new char[] { 'a', 'k', 'b' }, charArray ) );
        charArray = Arrays.add( new char[] { 'a', 'b', 'c' }, 1, 't');
        assertTrue( Arrays.equals( new char[] { 'a', 't', 'b', 'c' }, charArray ) );
        try {
            charArray = Arrays.add( new char[] { 'a', 'b' }, 4, 'c');
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            charArray = Arrays.add( new char[] { 'a', 'b' }, -1, 'c');
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // short tests
        short[] shortArray = Arrays.add( new short[] { 1 }, 0, (short) 2);
        assertTrue( Arrays.equals( new short[] { 2, 1 }, shortArray ) );
        try {
            shortArray = Arrays.add( (short[]) null, -1, (short) 2);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        shortArray = Arrays.add( new short[] { 2, 6 }, 2, (short) 10);
        assertTrue( Arrays.equals( new short[] { 2, 6, 10 }, shortArray ) );
        shortArray = Arrays.add( new short[] { 2, 6 }, 0, (short) -4);
        assertTrue( Arrays.equals( new short[] { -4, 2, 6 }, shortArray ) );
        shortArray = Arrays.add( new short[] { 2, 6, 3 }, 2, (short) 1);
        assertTrue( Arrays.equals( new short[] { 2, 6, 1, 3 }, shortArray ) );
        try {
            shortArray = Arrays.add( new short[] { 2, 6 }, 4, (short) 10);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            shortArray = Arrays.add( new short[] { 2, 6 }, -1, (short) 10);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // byte tests
        byte[] byteArray = Arrays.add( new byte[] { 1 }, 0, (byte) 2);
        assertTrue( Arrays.equals( new byte[] { 2, 1 }, byteArray ) );
        try {
            byteArray = Arrays.add( (byte[]) null, -1, (byte) 2);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        byteArray = Arrays.add( new byte[] { 2, 6 }, 2, (byte) 3);
        assertTrue( Arrays.equals( new byte[] { 2, 6, 3 }, byteArray ) );
        byteArray = Arrays.add( new byte[] { 2, 6 }, 0, (byte) 1);
        assertTrue( Arrays.equals( new byte[] { 1, 2, 6 }, byteArray ) );
        byteArray = Arrays.add( new byte[] { 2, 6, 3 }, 2, (byte) 1);
        assertTrue( Arrays.equals( new byte[] { 2, 6, 1, 3 }, byteArray ) );
        try {
            byteArray = Arrays.add( new byte[] { 2, 6 }, 4, (byte) 3);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            byteArray = Arrays.add( new byte[] { 2, 6 }, -1, (byte) 3);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // int tests
        int[] intArray = Arrays.add( new int[] { 1 }, 0, 2);
        assertTrue( Arrays.equals( new int[] { 2, 1 }, intArray ) );
        try {
            intArray = Arrays.add( (int[]) null, -1, 2);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        intArray = Arrays.add( new int[] { 2, 6 }, 2, 10);
        assertTrue( Arrays.equals( new int[] { 2, 6, 10 }, intArray ) );
        intArray = Arrays.add( new int[] { 2, 6 }, 0, -4);
        assertTrue( Arrays.equals( new int[] { -4, 2, 6 }, intArray ) );
        intArray = Arrays.add( new int[] { 2, 6, 3 }, 2, 1);
        assertTrue( Arrays.equals( new int[] { 2, 6, 1, 3 }, intArray ) );
        try {
            intArray = Arrays.add( new int[] { 2, 6 }, 4, 10);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            intArray = Arrays.add( new int[] { 2, 6 }, -1, 10);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // long tests
        long[] longArray = Arrays.add( new long[] { 1L }, 0, 2L);
        assertTrue( Arrays.equals( new long[] { 2L, 1L }, longArray ) );
        try {
            longArray = Arrays.add( (long[]) null, -1, 2L);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        longArray = Arrays.add( new long[] { 2L, 6L }, 2, 10L);
        assertTrue( Arrays.equals( new long[] { 2L, 6L, 10L }, longArray ) );
        longArray = Arrays.add( new long[] { 2L, 6L }, 0, -4L);
        assertTrue( Arrays.equals( new long[] { -4L, 2L, 6L }, longArray ) );
        longArray = Arrays.add( new long[] { 2L, 6L, 3L }, 2, 1L);
        assertTrue( Arrays.equals( new long[] { 2L, 6L, 1L, 3L }, longArray ) );
        try {
            longArray = Arrays.add( new long[] { 2L, 6L }, 4, 10L);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            longArray = Arrays.add( new long[] { 2L, 6L }, -1, 10L);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // float tests
        float[] floatArray = Arrays.add( new float[] { 1.1f }, 0, 2.2f);
        assertTrue( Arrays.equals( new float[] { 2.2f, 1.1f }, floatArray ) );
        try {
            floatArray = Arrays.add( (float[]) null, -1, 2.2f);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        floatArray = Arrays.add( new float[] { 2.3f, 6.4f }, 2, 10.5f);
        assertTrue( Arrays.equals( new float[] { 2.3f, 6.4f, 10.5f }, floatArray ) );
        floatArray = Arrays.add( new float[] { 2.6f, 6.7f }, 0, -4.8f);
        assertTrue( Arrays.equals( new float[] { -4.8f, 2.6f, 6.7f }, floatArray ) );
        floatArray = Arrays.add( new float[] { 2.9f, 6.0f, 0.3f }, 2, 1.0f);
        assertTrue( Arrays.equals( new float[] { 2.9f, 6.0f, 1.0f, 0.3f }, floatArray ) );
        try {
            floatArray = Arrays.add( new float[] { 2.3f, 6.4f }, 4, 10.5f);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            floatArray = Arrays.add( new float[] { 2.3f, 6.4f }, -1, 10.5f);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }

        // double tests
        double[] doubleArray = Arrays.add( new double[] { 1.1 }, 0, 2.2);
        assertTrue( Arrays.equals( new double[] { 2.2, 1.1 }, doubleArray ) );
        try {
          doubleArray = Arrays.add( (double[]) null, -1, 2.2);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 0", e.getMessage());
        }
        doubleArray = Arrays.add( new double[] { 2.3, 6.4 }, 2, 10.5);
        assertTrue( Arrays.equals( new double[] { 2.3, 6.4, 10.5 }, doubleArray ) );
        doubleArray = Arrays.add( new double[] { 2.6, 6.7 }, 0, -4.8);
        assertTrue( Arrays.equals( new double[] { -4.8, 2.6, 6.7 }, doubleArray ) );
        doubleArray = Arrays.add( new double[] { 2.9, 6.0, 0.3 }, 2, 1.0);
        assertTrue( Arrays.equals( new double[] { 2.9, 6.0, 1.0, 0.3 }, doubleArray ) );
        try {
            doubleArray = Arrays.add( new double[] { 2.3, 6.4 }, 4, 10.5);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: 4, Length: 2", e.getMessage());
        }
        try {
            doubleArray = Arrays.add( new double[] { 2.3, 6.4 }, -1, 10.5);
        } catch(final IndexOutOfBoundsException e) {
            assertEquals("Index: -1, Length: 2", e.getMessage());
        }
    }

}
