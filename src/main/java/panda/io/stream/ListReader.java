package panda.io.stream;

import java.io.IOException;
import java.util.List;

public interface ListReader {
	List<String> readList() throws IOException;
}
