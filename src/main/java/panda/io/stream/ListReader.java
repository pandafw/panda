package panda.io.stream;

import java.io.IOException;
import java.util.List;

public interface ListReader<T> {
	List<T> readList() throws IOException;
}
