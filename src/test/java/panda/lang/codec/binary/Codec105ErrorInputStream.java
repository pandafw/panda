package panda.lang.codec.binary;

import java.io.IOException;
import java.io.InputStream;

/**
 * Emits three line-feeds '\n' in a row, one at a time, and then EOF.
 *
 * Recreates the bug described in CODEC-105.
 *
 */
public class Codec105ErrorInputStream extends InputStream {
    private static final int EOF = -1;

    int countdown = 3;

    @Override
    public int read() throws IOException {
        if (this.countdown-- > 0) {
            return '\n';
        } else {
            return EOF;
        }
    }

    @Override
    public int read(final byte b[], final int pos, final int len) throws IOException {
        if (this.countdown-- > 0) {
            b[pos] = '\n';
            return 1;
        } else {
            return EOF;
        }
    }
}
