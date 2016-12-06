package panda.lang.collection;

import java.util.Set;

import junit.framework.Test;

public class CaseInsensitiveSetTest<E> extends AbstractSetTest<E> {

    public CaseInsensitiveSetTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(CaseInsensitiveSetTest.class);
    }

    @Override
    public Set<E> makeObject() {
        return new CaseInsensitiveSet<E>();
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //-------------------------------------------------------------------------
}
