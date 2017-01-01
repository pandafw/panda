package panda.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.lang.Arrays;

/**
 * Tests Arrays remove and removeElement methods.
 * 
 */
public class ArraysRemoveMultipleTest {

    @Test
    public void testRemoveAllObjectArray() {
        Object[] array;
        array = Arrays.removeAll(new Object[] { "a" }, 0);
        assertArrayEquals(Arrays.EMPTY_OBJECT_ARRAY, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b" }, 0, 1);
        assertArrayEquals(Arrays.EMPTY_OBJECT_ARRAY, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c" }, 1, 2);
        assertArrayEquals(new Object[] { "a" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d" }, 1, 2);
        assertArrayEquals(new Object[] { "a", "d" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d" }, 0, 3);
        assertArrayEquals(new Object[] { "b", "c" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d" }, 0, 1, 3);
        assertArrayEquals(new Object[] { "c" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d", "e" }, 0, 1, 3);
        assertArrayEquals(new Object[] { "c", "e" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d", "e" }, 0, 2, 4);
        assertArrayEquals(new Object[] { "b", "d" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d" }, 0, 1, 3, 0, 1, 3);
        assertArrayEquals(new Object[] { "c" }, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d" }, 2, 1, 0, 3);
        assertArrayEquals(Arrays.EMPTY_OBJECT_ARRAY, array);
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeAll(new Object[] { "a", "b", "c", "d" }, 2, 0, 1, 3, 0, 2, 1, 3);
        assertArrayEquals(Arrays.EMPTY_OBJECT_ARRAY, array);
        assertEquals(Object.class, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllObjectArrayRemoveNone() {
        final Object[] array1 = new Object[] { "foo", "bar", "baz" };
        final Object[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
        assertEquals(Object.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllObjectArrayNegativeIndex() {
        Arrays.removeAll(new Object[] { "a", "b" }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllObjectArrayOutOfBoundsIndex() {
        Arrays.removeAll(new Object[] { "a", "b" }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullObjectArray() {
        Arrays.remove((Object[]) null, 0);
    }

    @Test
    public void testRemoveAllNumberArray() {
        final Number[] inarray = { Integer.valueOf(1), Long.valueOf(2L), Byte.valueOf((byte) 3) };
        assertEquals(3, inarray.length);
        Number[] outarray;
        outarray = Arrays.removeAll(inarray, 1);
        assertArrayEquals(new Number[] { Integer.valueOf(1), Byte.valueOf((byte) 3) }, outarray);
        assertEquals(Number.class, outarray.getClass().getComponentType());
        outarray = Arrays.removeAll(outarray, 1);
        assertArrayEquals(new Number[] { Integer.valueOf(1) }, outarray);
        assertEquals(Number.class, outarray.getClass().getComponentType());
        outarray = Arrays.removeAll(outarray, 0);
        assertEquals(0, outarray.length);
        assertEquals(Number.class, outarray.getClass().getComponentType());

        outarray = Arrays.removeAll(inarray, 0, 1);
        assertArrayEquals(new Number[] { Byte.valueOf((byte) 3) }, outarray);
        assertEquals(Number.class, outarray.getClass().getComponentType());
        outarray = Arrays.removeAll(inarray, 0, 2);
        assertArrayEquals(new Number[] { Long.valueOf(2L) }, outarray);
        assertEquals(Number.class, outarray.getClass().getComponentType());
        outarray = Arrays.removeAll(inarray, 1, 2);
        assertArrayEquals(new Number[] { Integer.valueOf(1) }, outarray);
        assertEquals(Number.class, outarray.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllBooleanArray() {
        boolean[] array;
        array = Arrays.removeAll(new boolean[] { true }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false }, 0);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false }, 1);
        assertTrue(Arrays.equals(new boolean[] { true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true }, 1);
        assertTrue(Arrays.equals(new boolean[] { true, true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new boolean[] { true, false }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, false }, 0, 1);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, false }, 0, 2);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, false }, 1, 2);
        assertTrue(Arrays.equals(new boolean[] { true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true, false, true }, 0, 2, 4);
        assertTrue(Arrays.equals(new boolean[] { false, false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true, false, true }, 1, 3);
        assertTrue(Arrays.equals(new boolean[] { true, true, true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true, false, true }, 1, 3, 4);
        assertTrue(Arrays.equals(new boolean[] { true, true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true, false, true, false, true }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new boolean[] { false, false, false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true, false, true, false, true }, 1, 3, 5);
        assertTrue(Arrays.equals(new boolean[] { true, true, true, true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new boolean[] { true, false, true, false, true, false, true }, 0, 1, 2);
        assertTrue(Arrays.equals(new boolean[] { false, true, false, true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllBooleanArrayRemoveNone() {
        final boolean[] array1 = new boolean[] { true, false };
        final boolean[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertTrue(Arrays.equals(array1, array2));
        assertEquals(boolean.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllBooleanArrayNegativeIndex() {
        Arrays.removeAll(new boolean[] { true, false }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllBooleanArrayOutOfBoundsIndex() {
        Arrays.removeAll(new boolean[] { true, false }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullBooleanArray() {
        Arrays.removeAll((boolean[]) null, 0);
    }

    @Test
    public void testRemoveAllByteArray() {
        byte[] array;
        array = Arrays.removeAll(new byte[] { 1 }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2 }, 0);
        assertTrue(Arrays.equals(new byte[] { 2 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new byte[] { 1 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new byte[] { 1, 1 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new byte[] { 1, 2 }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3 }, 0, 1);
        assertTrue(Arrays.equals(new byte[] { 3 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3 }, 1, 2);
        assertTrue(Arrays.equals(new byte[] { 1 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3 }, 0, 2);
        assertTrue(Arrays.equals(new byte[] { 2 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3, 4, 5 }, 1, 3);
        assertTrue(Arrays.equals(new byte[] { 1, 3, 5 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3, 4, 5 }, 0, 2, 4);
        assertTrue(Arrays.equals(new byte[] { 2, 4 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, 1, 3, 5);
        assertTrue(Arrays.equals(new byte[] { 1, 3, 5, 7 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new byte[] { 2, 4, 6 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllByteArrayRemoveNone() {
        final byte[] array1 = new byte[] { 1, 2 };
        final byte[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
        assertEquals(byte.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllByteArrayNegativeIndex() {
        Arrays.removeAll(new byte[] { 1, 2 }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllByteArrayOutOfBoundsIndex() {
        Arrays.removeAll(new byte[] { 1, 2 }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullByteArray() {
        Arrays.removeAll((byte[]) null, 0);
    }

    @Test
    public void testRemoveAllCharArray() {
        char[] array;
        array = Arrays.removeAll(new char[] { 'a' }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b' }, 0);
        assertTrue(Arrays.equals(new char[] { 'b' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b' }, 1);
        assertTrue(Arrays.equals(new char[] { 'a' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c' }, 1);
        assertTrue(Arrays.equals(new char[] { 'a', 'c' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new char[] { 'a', 'b' }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c' }, 0, 1);
        assertTrue(Arrays.equals(new char[] { 'c' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c' }, 1, 2);
        assertTrue(Arrays.equals(new char[] { 'a' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c' }, 0, 2);
        assertTrue(Arrays.equals(new char[] { 'b' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c', 'd', 'e' }, 1, 3);
        assertTrue(Arrays.equals(new char[] { 'a', 'c', 'e' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c', 'd', 'e' }, 0, 2, 4);
        assertTrue(Arrays.equals(new char[] { 'b', 'd' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' }, 1, 3, 5);
        assertTrue(Arrays.equals(new char[] { 'a', 'c', 'e', 'g' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new char[] { 'b', 'd', 'f' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllCharArrayRemoveNone() {
        final char[] array1 = new char[] { 'a', 'b' };
        final char[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
        assertEquals(char.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllCharArrayNegativeIndex() {
        Arrays.removeAll(new char[] { 'a', 'b' }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllCharArrayOutOfBoundsIndex() {
        Arrays.removeAll(new char[] { 'a', 'b' }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullCharArray() {
        Arrays.removeAll((char[]) null, 0);
    }

    @Test
    public void testRemoveAllDoubleArray() {
        double[] array;
        array = Arrays.removeAll(new double[] { 1 }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2 }, 0);
        assertTrue(Arrays.equals(new double[] { 2 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new double[] { 1 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new double[] { 1, 1 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new double[] { 1, 2 }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3 }, 0, 1);
        assertTrue(Arrays.equals(new double[] { 3 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3 }, 1, 2);
        assertTrue(Arrays.equals(new double[] { 1 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3 }, 0, 2);
        assertTrue(Arrays.equals(new double[] { 2 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3, 4, 5 }, 1, 3);
        assertTrue(Arrays.equals(new double[] { 1, 3, 5 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3, 4, 5 }, 0, 2, 4);
        assertTrue(Arrays.equals(new double[] { 2, 4 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3, 4, 5, 6, 7 }, 1, 3, 5);
        assertTrue(Arrays.equals(new double[] { 1, 3, 5, 7 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new double[] { 1, 2, 3, 4, 5, 6, 7 }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new double[] { 2, 4, 6 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllDoubleArrayRemoveNone() {
        final double[] array1 = new double[] { 1, 2 };
        final double[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertTrue(Arrays.equals(array1, array2));
        assertEquals(double.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllDoubleArrayNegativeIndex() {
        Arrays.removeAll(new double[] { 1, 2 }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllDoubleArrayOutOfBoundsIndex() {
        Arrays.removeAll(new double[] { 1, 2 }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullDoubleArray() {
        Arrays.removeAll((double[]) null, 0);
    }

    @Test
    public void testRemoveAllFloatArray() {
        float[] array;
        array = Arrays.removeAll(new float[] { 1 }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2 }, 0);
        assertTrue(Arrays.equals(new float[] { 2 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new float[] { 1 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new float[] { 1, 1 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new float[] { 1, 2 }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3 }, 0, 1);
        assertTrue(Arrays.equals(new float[] { 3 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3 }, 1, 2);
        assertTrue(Arrays.equals(new float[] { 1 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3 }, 0, 2);
        assertTrue(Arrays.equals(new float[] { 2 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3, 4, 5 }, 1, 3);
        assertTrue(Arrays.equals(new float[] { 1, 3, 5 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3, 4, 5 }, 0, 2, 4);
        assertTrue(Arrays.equals(new float[] { 2, 4 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3, 4, 5, 6, 7 }, 1, 3, 5);
        assertTrue(Arrays.equals(new float[] { 1, 3, 5, 7 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new float[] { 1, 2, 3, 4, 5, 6, 7 }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new float[] { 2, 4, 6 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllFloatArrayRemoveNone() {
        final float[] array1 = new float[] { 1, 2 };
        final float[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertTrue(Arrays.equals(array1, array2));
        assertEquals(float.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllFloatArrayNegativeIndex() {
        Arrays.removeAll(new float[] { 1, 2 }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllFloatArrayOutOfBoundsIndex() {
        Arrays.removeAll(new float[] { 1, 2 }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullFloatArray() {
        Arrays.removeAll((float[]) null, 0);
    }

    @Test
    public void testRemoveAllIntArray() {
        int[] array;
        array = Arrays.removeAll(Arrays.EMPTY_INT_ARRAY, Arrays.EMPTY_INT_ARRAY);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        array = Arrays.removeAll(new int[] { 1 }, Arrays.EMPTY_INT_ARRAY);
        assertTrue(Arrays.equals(new int[] { 1 }, array));
        array = Arrays.removeAll(new int[] { 1 }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2 }, 0);
        assertTrue(Arrays.equals(new int[] { 2 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new int[] { 1 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new int[] { 1, 1 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new int[] { 1, 2 }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3 }, 0, 1);
        assertTrue(Arrays.equals(new int[] { 3 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3 }, 1, 2);
        assertTrue(Arrays.equals(new int[] { 1 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3 }, 0, 2);
        assertTrue(Arrays.equals(new int[] { 2 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3, 4, 5 }, 1, 3);
        assertTrue(Arrays.equals(new int[] { 1, 3, 5 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3, 4, 5 }, 0, 2, 4);
        assertTrue(Arrays.equals(new int[] { 2, 4 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3, 4, 5, 6, 7 }, 1, 3, 5);
        assertTrue(Arrays.equals(new int[] { 1, 3, 5, 7 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new int[] { 1, 2, 3, 4, 5, 6, 7 }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new int[] { 2, 4, 6 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllIntArrayRemoveNone() {
        final int[] array1 = new int[] { 1, 2 };
        final int[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
        assertEquals(int.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllIntArrayNegativeIndex() {
        Arrays.removeAll(new int[] { 1, 2 }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllIntArrayOutOfBoundsIndex() {
        Arrays.removeAll(new int[] { 1, 2 }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullIntArray() {
        Arrays.removeAll((int[]) null, 0);
    }

    @Test
    public void testRemoveAllLongArray() {
        long[] array;
        array = Arrays.removeAll(new long[] { 1 }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2 }, 0);
        assertTrue(Arrays.equals(new long[] { 2 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new long[] { 1 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new long[] { 1, 1 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new long[] { 1, 2 }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3 }, 0, 1);
        assertTrue(Arrays.equals(new long[] { 3 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3 }, 1, 2);
        assertTrue(Arrays.equals(new long[] { 1 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3 }, 0, 2);
        assertTrue(Arrays.equals(new long[] { 2 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3, 4, 5 }, 1, 3);
        assertTrue(Arrays.equals(new long[] { 1, 3, 5 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3, 4, 5 }, 0, 2, 4);
        assertTrue(Arrays.equals(new long[] { 2, 4 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3, 4, 5, 6, 7 }, 1, 3, 5);
        assertTrue(Arrays.equals(new long[] { 1, 3, 5, 7 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new long[] { 1, 2, 3, 4, 5, 6, 7 }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new long[] { 2, 4, 6 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllLongArrayRemoveNone() {
        final long[] array1 = new long[] { 1, 2 };
        final long[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
        assertEquals(long.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllLongArrayNegativeIndex() {
        Arrays.removeAll(new long[] { 1, 2 }, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllLongArrayOutOfBoundsIndex() {
        Arrays.removeAll(new long[] { 1, 2 }, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullLongArray() {
        Arrays.removeAll((long[]) null, 0);
    }

    @Test
    public void testRemoveAllShortArray() {
        short[] array;
        array = Arrays.removeAll(new short[] { 1 }, 0);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2 }, 0);
        assertTrue(Arrays.equals(new short[] { 2 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new short[] { 1 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new short[] { 1, 1 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());

        array = Arrays.removeAll(new short[] { 1, 2 }, 0, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3 }, 0, 1);
        assertTrue(Arrays.equals(new short[] { 3 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3 }, 1, 2);
        assertTrue(Arrays.equals(new short[] { 1 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3 }, 0, 2);
        assertTrue(Arrays.equals(new short[] { 2 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3, 4, 5 }, 1, 3);
        assertTrue(Arrays.equals(new short[] { 1, 3, 5 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3, 4, 5 }, 0, 2, 4);
        assertTrue(Arrays.equals(new short[] { 2, 4 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3, 4, 5, 6, 7 }, 1, 3, 5);
        assertTrue(Arrays.equals(new short[] { 1, 3, 5, 7 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeAll(new short[] { 1, 2, 3, 4, 5, 6, 7 }, 0, 2, 4, 6);
        assertTrue(Arrays.equals(new short[] { 2, 4, 6 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveAllShortArrayRemoveNone() {
        final short[] array1 = new short[] { 1, 2 };
        final short[] array2 = Arrays.removeAll(array1);
        assertNotSame(array1, array2);
        assertArrayEquals(array1, array2);
        assertEquals(short.class, array2.getClass().getComponentType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllShortArrayNegativeIndex() {
        Arrays.removeAll(new short[] { 1, 2 }, -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllShortArrayOutOfBoundsIndex() {
        Arrays.removeAll(new short[] { 1, 2 }, 2, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAllNullShortArray() {
        Arrays.removeAll((short[]) null, 0);
    }

    @Test
    public void testRemoveElementsObjectArray() {
        Object[] array;
        array = Arrays.removeElements((Object[]) null, "a");
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_OBJECT_ARRAY, "a");
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a" }, "a");
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b" }, "a");
        assertTrue(Arrays.equals(new Object[] { "b" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b", "a" }, "a");
        assertTrue(Arrays.equals(new Object[] { "b", "a" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());

        array = Arrays.removeElements((Object[]) null, "a", "b");
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_OBJECT_ARRAY, "a", "b");
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a" }, "a", "b");
        assertTrue(Arrays.equals(Arrays.EMPTY_OBJECT_ARRAY, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b" }, "a", "c");
        assertTrue(Arrays.equals(new Object[] { "b" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b", "a" }, "a");
        assertTrue(Arrays.equals(new Object[] { "b", "a" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b", "a" }, "a", "b");
        assertTrue(Arrays.equals(new Object[] { "a" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b", "a" }, "a", "a");
        assertTrue(Arrays.equals(new Object[] { "b" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());
        array = Arrays.removeElements(new Object[] { "a", "b", "a" }, "a", "a", "a", "a");
        assertTrue(Arrays.equals(new Object[] { "b" }, array));
        assertEquals(Object.class, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveElementBooleanArray() {
        boolean[] array;
        array = Arrays.removeElements((boolean[]) null, true);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_BOOLEAN_ARRAY, true);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true }, true);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false }, true);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false, true }, true);
        assertTrue(Arrays.equals(new boolean[] { false, true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((boolean[]) null, true, false);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_BOOLEAN_ARRAY, true, false);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true }, true, false);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false }, true, false);
        assertTrue(Arrays.equals(Arrays.EMPTY_BOOLEAN_ARRAY, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false }, true, true);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false, true }, true, false);
        assertTrue(Arrays.equals(new boolean[] { true }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false, true }, true, true);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new boolean[] { true, false, true }, true, true, true, true);
        assertTrue(Arrays.equals(new boolean[] { false }, array));
        assertEquals(Boolean.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveElementByteArray() {
        byte[] array;
        array = Arrays.removeElements((byte[]) null, (byte) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_BYTE_ARRAY, (byte) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1 }, (byte) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2 }, (byte) 1);
        assertTrue(Arrays.equals(new byte[] { 2 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2, 1 }, (byte) 1);
        assertTrue(Arrays.equals(new byte[] { 2, 1 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((byte[]) null, (byte) 1, (byte) 2);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_BYTE_ARRAY, (byte) 1, (byte) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1 }, (byte) 1, (byte) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2 }, (byte) 1, (byte) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_BYTE_ARRAY, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2 }, (byte) 1, (byte) 1);
        assertTrue(Arrays.equals(new byte[] { 2 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2, 1 }, (byte) 1, (byte) 2);
        assertTrue(Arrays.equals(new byte[] { 1 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2, 1 }, (byte) 1, (byte) 1);
        assertTrue(Arrays.equals(new byte[] { 2 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new byte[] { 1, 2, 1 }, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
        assertTrue(Arrays.equals(new byte[] { 2 }, array));
        assertEquals(Byte.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveElementCharArray() {
        char[] array;
        array = Arrays.removeElements((char[]) null, 'a');
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_CHAR_ARRAY, 'a');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a' }, 'a');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b' }, 'a');
        assertTrue(Arrays.equals(new char[] { 'b' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b', 'a' }, 'a');
        assertTrue(Arrays.equals(new char[] { 'b', 'a' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((char[]) null, 'a', 'b');
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_CHAR_ARRAY, 'a', 'b');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a' }, 'a', 'b');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b' }, 'a', 'b');
        assertTrue(Arrays.equals(Arrays.EMPTY_CHAR_ARRAY, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b' }, 'a', 'a');
        assertTrue(Arrays.equals(new char[] { 'b' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b', 'a' }, 'a', 'b');
        assertTrue(Arrays.equals(new char[] { 'a' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b', 'a' }, 'a', 'a');
        assertTrue(Arrays.equals(new char[] { 'b' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new char[] { 'a', 'b', 'a' }, 'a', 'a', 'a', 'a');
        assertTrue(Arrays.equals(new char[] { 'b' }, array));
        assertEquals(Character.TYPE, array.getClass().getComponentType());
    }

    @Test
    @SuppressWarnings("cast")
    public void testRemoveElementDoubleArray() {
        double[] array;
        array = Arrays.removeElements((double[]) null, (double) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_DOUBLE_ARRAY, (double) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1 }, (double) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2 }, (double) 1);
        assertTrue(Arrays.equals(new double[] { 2 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2, 1 }, (double) 1);
        assertTrue(Arrays.equals(new double[] { 2, 1 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((double[]) null, (double) 1, (double) 2);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_DOUBLE_ARRAY, (double) 1, (double) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1 }, (double) 1, (double) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2 }, (double) 1, (double) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_DOUBLE_ARRAY, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2 }, (double) 1, (double) 1);
        assertTrue(Arrays.equals(new double[] { 2 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2, 1 }, (double) 1, (double) 2);
        assertTrue(Arrays.equals(new double[] { 1 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2, 1 }, (double) 1, (double) 1);
        assertTrue(Arrays.equals(new double[] { 2 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new double[] { 1, 2, 1 }, (double) 1, (double) 1, (double) 1, (double) 1);
        assertTrue(Arrays.equals(new double[] { 2 }, array));
        assertEquals(Double.TYPE, array.getClass().getComponentType());
    }

    @Test
    @SuppressWarnings("cast")
    public void testRemoveElementFloatArray() {
        float[] array;
        array = Arrays.removeElements((float[]) null, (float) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_FLOAT_ARRAY, (float) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1 }, (float) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2 }, (float) 1);
        assertTrue(Arrays.equals(new float[] { 2 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2, 1 }, (float) 1);
        assertTrue(Arrays.equals(new float[] { 2, 1 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((float[]) null, (float) 1, (float) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_FLOAT_ARRAY, (float) 1, (float) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1 }, (float) 1, (float) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2 }, (float) 1, (float) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_FLOAT_ARRAY, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2 }, (float) 1, (float) 1);
        assertTrue(Arrays.equals(new float[] { 2 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2, 1 }, (float) 1, (float) 1);
        assertTrue(Arrays.equals(new float[] { 2 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2, 1 }, (float) 1, (float) 2);
        assertTrue(Arrays.equals(new float[] { 1 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new float[] { 1, 2, 1 }, (float) 1, (float) 1, (float) 1, (float) 1);
        assertTrue(Arrays.equals(new float[] { 2 }, array));
        assertEquals(Float.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveElementIntArray() {
        int[] array;
        array = Arrays.removeElements((int[]) null, 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_INT_ARRAY, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1 }, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2 }, 1);
        assertTrue(Arrays.equals(new int[] { 2 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2, 1 }, 1);
        assertTrue(Arrays.equals(new int[] { 2, 1 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((int[]) null, 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_INT_ARRAY, 1, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1 }, 1, 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2 }, 1, 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_INT_ARRAY, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2 }, 1, 1);
        assertTrue(Arrays.equals(new int[] { 2 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2, 1 }, 1, 2);
        assertTrue(Arrays.equals(new int[] { 1 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2, 1 }, 1, 1);
        assertTrue(Arrays.equals(new int[] { 2 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new int[] { 1, 2, 1 }, 1, 1, 1, 1);
        assertTrue(Arrays.equals(new int[] { 2 }, array));
        assertEquals(Integer.TYPE, array.getClass().getComponentType());
    }

    @Test
    @SuppressWarnings("cast")
    public void testRemoveElementLongArray() {
        long[] array;
        array = Arrays.removeElements((long[]) null, (long) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_LONG_ARRAY, (long) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1 }, (long) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2 }, (long) 1);
        assertTrue(Arrays.equals(new long[] { 2 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2, 1 }, (long) 1);
        assertTrue(Arrays.equals(new long[] { 2, 1 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((long[]) null, (long) 1, (long) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_LONG_ARRAY, (long) 1, (long) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1 }, (long) 1, (long) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2 }, (long) 1, (long) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_LONG_ARRAY, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2 }, (long) 1, (long) 1);
        assertTrue(Arrays.equals(new long[] { 2 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2, 1 }, (long) 1, (long) 1);
        assertTrue(Arrays.equals(new long[] { 2 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2, 1 }, (long) 1, (long) 2);
        assertTrue(Arrays.equals(new long[] { 1 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new long[] { 1, 2, 1 }, (long) 1, (long) 1, (long) 1, (long) 1);
        assertTrue(Arrays.equals(new long[] { 2 }, array));
        assertEquals(Long.TYPE, array.getClass().getComponentType());
    }

    @Test
    public void testRemoveElementShortArray() {
        short[] array;
        array = Arrays.removeElements((short[]) null, (short) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_SHORT_ARRAY, (short) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1 }, (short) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2 }, (short) 1);
        assertTrue(Arrays.equals(new short[] { 2 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2, 1 }, (short) 1);
        assertTrue(Arrays.equals(new short[] { 2, 1 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());

        array = Arrays.removeElements((short[]) null, (short) 1, (short) 1);
        assertNull(array);
        array = Arrays.removeElements(Arrays.EMPTY_SHORT_ARRAY, (short) 1, (short) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1 }, (short) 1, (short) 1);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2 }, (short) 1, (short) 2);
        assertTrue(Arrays.equals(Arrays.EMPTY_SHORT_ARRAY, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2 }, (short) 1, (short) 1);
        assertTrue(Arrays.equals(new short[] { 2 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2, 1 }, (short) 1, (short) 1);
        assertTrue(Arrays.equals(new short[] { 2 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2, 1 }, (short) 1, (short) 2);
        assertTrue(Arrays.equals(new short[] { 1 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
        array = Arrays.removeElements(new short[] { 1, 2, 1 }, (short) 1, (short) 1, (short) 1, (short) 1);
        assertTrue(Arrays.equals(new short[] { 2 }, array));
        assertEquals(Short.TYPE, array.getClass().getComponentType());
    }

}
