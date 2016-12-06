package panda.lang.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSetTest<E> extends AbstractCollectionTest<E> {

    /**
     * JUnit constructor.
     *
     * @param name  name for test
     */
    public AbstractSetTest(final String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    /**
     * Provides additional verifications for sets.
     */
    @Override
    public void verify() {
        super.verify();

        assertEquals("Sets should be equal", getConfirmed(), getCollection());
        assertEquals("Sets should have equal hashCodes",
                     getConfirmed().hashCode(), getCollection().hashCode());
        final Collection<E> set = makeConfirmedCollection();
        for (E element : getCollection()) {
            assertTrue("Set.iterator should only return unique elements", set.add(element));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Set equals method is defined.
     */
    @Override
    public boolean isEqualsCheckable() {
        return true;
    }

    /**
     * Returns an empty Set for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<E> makeConfirmedCollection() {
        return new HashSet<E>();
    }

    /**
     * Returns a full Set for use in modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<E> makeConfirmedFullCollection() {
        final Collection<E> set = makeConfirmedCollection();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    /**
     * Makes an empty set.  The returned set should have no elements.
     *
     * @return an empty set
     */
    @Override
    public abstract Set<E> makeObject();

    /**
     * Makes a full set by first creating an empty set and then adding
     * all the elements returned by {@link #getFullElements()}.
     *
     * Override if your set does not support the add operation.
     *
     * @return a full set
     */
    @Override
    public Set<E> makeFullCollection() {
        final Set<E> set = makeObject();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    //-----------------------------------------------------------------------
    /**
     * Return the {@link AbstractCollectionTest#collection} fixture, but cast as a Set.
     */
    @Override
    public Set<E> getCollection() {
        return (Set<E>) super.getCollection();
    }

    /**
     * Return the {@link AbstractCollectionTest#confirmed} fixture, but cast as a Set.
     */
    @Override
    public Set<E> getConfirmed() {
        return (Set<E>) super.getConfirmed();
    }

    //-----------------------------------------------------------------------
    /**
     * Tests {@link Set#equals(Object)}.
     */
    @SuppressWarnings("unchecked")
    public void testSetEquals() {
        resetEmpty();
        assertEquals("Empty sets should be equal", getCollection(), getConfirmed());
        verify();

        final Collection<E> set2 = makeConfirmedCollection();
        set2.add((E) "foo");
        assertTrue("Empty set shouldn't equal nonempty set", !getCollection().equals(set2));

        resetFull();
        assertEquals("Full sets should be equal", getCollection(), getConfirmed());
        verify();

        set2.clear();
        set2.addAll(Arrays.asList(getOtherElements()));
        assertTrue("Sets with different contents shouldn't be equal", !getCollection().equals(set2));
    }

    /**
     * Tests {@link Set#hashCode()}.
     */
    public void testSetHashCode() {
        resetEmpty();
        assertEquals("Empty sets have equal hashCodes",
                getCollection().hashCode(), getConfirmed().hashCode());

        resetFull();
        assertEquals("Equal sets have equal hashCodes",
                getCollection().hashCode(), getConfirmed().hashCode());
    }

}
