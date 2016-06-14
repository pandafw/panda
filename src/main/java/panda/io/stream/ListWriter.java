package panda.io.stream;

import java.io.IOException;
import java.util.Collection;

public interface ListWriter {
	void writeList(Collection<?> list) throws IOException;
}
