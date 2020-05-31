package panda.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests CharSequences
 *
 */
public class CharSequencesTest {

    
    //-----------------------------------------------------------------------
    @Test
    public void testSubSequence() {
        //
        // null input
        //
        Assert.assertEquals(null, CharSequences.subSequence(null, -1));
        Assert.assertEquals(null, CharSequences.subSequence(null, 0));
        Assert.assertEquals(null, CharSequences.subSequence(null, 1));
        //
        // non-null input
        //
        Assert.assertEquals(Strings.EMPTY, CharSequences.subSequence(Strings.EMPTY, 0));
        Assert.assertEquals("012", CharSequences.subSequence("012", 0));
        Assert.assertEquals("12", CharSequences.subSequence("012", 1));
        Assert.assertEquals("2", CharSequences.subSequence("012", 2));
        Assert.assertEquals(Strings.EMPTY, CharSequences.subSequence("012", 3));
        //
        // Exception expected
        //
        try {
            Assert.assertEquals(null, CharSequences.subSequence(Strings.EMPTY, -1));
            Assert.fail("Expected " + IndexOutOfBoundsException.class.getName());
        } catch (final IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            Assert.assertEquals(null, CharSequences.subSequence(Strings.EMPTY, 1));
            Assert.fail("Expected " + IndexOutOfBoundsException.class.getName());
        } catch (final IndexOutOfBoundsException e) {
            // Expected
        }
    }

}
