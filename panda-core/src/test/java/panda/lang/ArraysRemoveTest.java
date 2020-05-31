package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests Arrays remove and removeElement methods.
 * 
 */
public class ArraysRemoveTest {

    @Test
    public void testRemoveObjectArray() {
        Object[] array;
        array = Arrays.remove(new Object[] {"a"}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.remove(new Object[] {"a", "b"}, 0);
        assertTrue(Arrays.equals(new Object[] {"b"}, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.remove(new Object[] {"a", "b"}, 1);
        assertTrue(Arrays.equals(new Object[] {"a"}, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.remove(new Object[] {"a", "b", "c"}, 1);
        assertTrue(Arrays.equals(new Object[] {"a", "c"}, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        try {
            Arrays.remove(new Object[] {"a", "b"}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new Object[] {"a", "b"}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((Object[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }

    @Test
    public void testRemoveNumberArray(){
        final Number[] inarray = {Integer.valueOf(1),Long.valueOf(2),Byte.valueOf((byte) 3)};
        assertEquals(3, inarray.length);
        Number[] outarray;
        outarray = Arrays.remove(inarray, 1);
        assertEquals(2, outarray.length);
        assertEquals(Number.class, outarray.getClass().getComponentType());
        outarray = Arrays.remove(outarray, 1);
        assertEquals(1, outarray.length);
        assertEquals(Number.class, outarray.getClass().getComponentType());
        outarray = Arrays.remove(outarray, 0);
        assertEquals(0, outarray.length);
        assertEquals(Number.class, outarray.getClass().getComponentType());
    }

    @Test
    public void testRemoveBooleanArray() {
        boolean[] array;
        array = Arrays.remove(new boolean[] {true}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new boolean[] {true, false}, 0);
        assertTrue(Arrays.equals(new boolean[] {false}, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new boolean[] {true, false}, 1);
        assertTrue(Arrays.equals(new boolean[] {true}, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new boolean[] {true, false, true}, 1);
        assertTrue(Arrays.equals(new boolean[] {true, true}, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new boolean[] {true, false}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new boolean[] {true, false}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((boolean[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveByteArray() {
        byte[] array;
        array = Arrays.remove(new byte[] {1}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new byte[] {1, 2}, 0);
        assertTrue(Arrays.equals(new byte[] {2}, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new byte[] {1, 2}, 1);
        assertTrue(Arrays.equals(new byte[] {1}, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new byte[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new byte[] {1, 1}, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new byte[] {1, 2}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new byte[] {1, 2}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((byte[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveCharArray() {
        char[] array;
        array = Arrays.remove(new char[] {'a'}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new char[] {'a', 'b'}, 0);
        assertTrue(Arrays.equals(new char[] {'b'}, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new char[] {'a', 'b'}, 1);
        assertTrue(Arrays.equals(new char[] {'a'}, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new char[] {'a', 'b', 'c'}, 1);
        assertTrue(Arrays.equals(new char[] {'a', 'c'}, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new char[] {'a', 'b'}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new char[] {'a', 'b'}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((char[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveDoubleArray() {
        double[] array;
        array = Arrays.remove(new double[] {1}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new double[] {1, 2}, 0);
        assertTrue(Arrays.equals(new double[] {2}, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new double[] {1, 2}, 1);
        assertTrue(Arrays.equals(new double[] {1}, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new double[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new double[] {1, 1}, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new double[] {1, 2}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new double[] {1, 2}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((double[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveFloatArray() {
        float[] array;
        array = Arrays.remove(new float[] {1}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new float[] {1, 2}, 0);
        assertTrue(Arrays.equals(new float[] {2}, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new float[] {1, 2}, 1);
        assertTrue(Arrays.equals(new float[] {1}, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new float[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new float[] {1, 1}, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new float[] {1, 2}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new float[] {1, 2}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((float[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveIntArray() {
        int[] array;
        array = Arrays.remove(new int[] {1}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new int[] {1, 2}, 0);
        assertTrue(Arrays.equals(new int[] {2}, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new int[] {1, 2}, 1);
        assertTrue(Arrays.equals(new int[] {1}, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new int[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new int[] {1, 1}, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new int[] {1, 2}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new int[] {1, 2}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((int[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveLongArray() {
        long[] array;
        array = Arrays.remove(new long[] {1}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new long[] {1, 2}, 0);
        assertTrue(Arrays.equals(new long[] {2}, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new long[] {1, 2}, 1);
        assertTrue(Arrays.equals(new long[] {1}, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new long[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new long[] {1, 1}, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new long[] {1, 2}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new long[] {1, 2}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((long[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveShortArray() {
        short[] array;
        array = Arrays.remove(new short[] {1}, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new short[] {1, 2}, 0);
        assertTrue(Arrays.equals(new short[] {2}, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new short[] {1, 2}, 1);
        assertTrue(Arrays.equals(new short[] {1}, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.remove(new short[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new short[] {1, 1}, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        try {
            Arrays.remove(new short[] {1, 2}, -1);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove(new short[] {1, 2}, 2);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
        try {
            Arrays.remove((short[]) null, 0);
            fail("IndexOutOfBoundsException expected");
        } catch (final IndexOutOfBoundsException e) {}
    }
    
    @Test
    public void testRemoveElementObjectArray() {
        Object[] array;
        array = Arrays.removeElement((Object[]) null, "a");
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_OBJECT_ARRAY, "a");
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElement(new Object[] {"a"}, "a");
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElement(new Object[] {"a", "b"}, "a");
        assertTrue(Arrays.equals(new Object[] {"b"}, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElement(new Object[] {"a", "b", "a"}, "a");
        assertTrue(Arrays.equals(new Object[] {"b", "a"}, array));
        assertEquals(Object.class, array.getClass().getComponentType());
    }
    
    @Test
    public void testRemoveElementBooleanArray() {
        boolean[] array;
        array = Arrays.removeElement((boolean[]) null, true);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_BOOLEAN_ARRAY, true);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new boolean[] {true}, true);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new boolean[] {true, false}, true);
        assertTrue(Arrays.equals(new boolean[] {false}, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new boolean[] {true, false, true}, true);
        assertTrue(Arrays.equals(new boolean[] {false, true}, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    public void testRemoveElementByteArray() {
        byte[] array;
        array = Arrays.removeElement((byte[]) null, (byte) 1);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_BYTE_ARRAY, (byte) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new byte[] {1}, (byte) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new byte[] {1, 2}, (byte) 1);
        assertTrue(Arrays.equals(new byte[] {2}, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new byte[] {1, 2, 1}, (byte) 1);
        assertTrue(Arrays.equals(new byte[] {2, 1}, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    public void testRemoveElementCharArray() {
        char[] array;
        array = Arrays.removeElement((char[]) null, 'a');
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_CHAR_ARRAY, 'a');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new char[] {'a'}, 'a');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new char[] {'a', 'b'}, 'a');
        assertTrue(Arrays.equals(new char[] {'b'}, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new char[] {'a', 'b', 'a'}, 'a');
        assertTrue(Arrays.equals(new char[] {'b', 'a'}, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    @SuppressWarnings("cast")
    public void testRemoveElementDoubleArray() {
        double[] array;
        array = Arrays.removeElement((double[]) null, (double) 1);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_DOUBLE_ARRAY, (double) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new double[] {1}, (double) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new double[] {1, 2}, (double) 1);
        assertTrue(Arrays.equals(new double[] {2}, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new double[] {1, 2, 1}, (double) 1);
        assertTrue(Arrays.equals(new double[] {2, 1}, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    @SuppressWarnings("cast")
    public void testRemoveElementFloatArray() {
        float[] array;
        array = Arrays.removeElement((float[]) null, (float) 1);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_FLOAT_ARRAY, (float) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new float[] {1}, (float) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new float[] {1, 2}, (float) 1);
        assertTrue(Arrays.equals(new float[] {2}, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new float[] {1, 2, 1}, (float) 1);
        assertTrue(Arrays.equals(new float[] {2, 1}, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    public void testRemoveElementIntArray() {
        int[] array;
        array = Arrays.removeElement((int[]) null, 1);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_INT_ARRAY, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new int[] {1}, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new int[] {1, 2}, 1);
        assertTrue(Arrays.equals(new int[] {2}, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new int[] {1, 2, 1}, 1);
        assertTrue(Arrays.equals(new int[] {2, 1}, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    @SuppressWarnings("cast")
    public void testRemoveElementLongArray() {
        long[] array;
        array = Arrays.removeElement((long[]) null, (long) 1);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_LONG_ARRAY, (long) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new long[] {1}, (long) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new long[] {1, 2}, (long) 1);
        assertTrue(Arrays.equals(new long[] {2}, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new long[] {1, 2, 1}, (long) 1);
        assertTrue(Arrays.equals(new long[] {2, 1}, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
    }
    
    @Test
    public void testRemoveElementShortArray() {
        short[] array;
        array = Arrays.removeElement((short[]) null, (short) 1);
        assertNull(array);
        array = Arrays.removeElement(Arrays.EMPTY_SHORT_ARRAY, (short) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new short[] {1}, (short) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new short[] {1, 2}, (short) 1);
        assertTrue(Arrays.equals(new short[] {2}, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElement(new short[] {1, 2, 1}, (short) 1);
        assertTrue(Arrays.equals(new short[] {2, 1}, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
    }
    
}
