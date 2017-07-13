package panda.mvc.view.sitemesh;

import java.io.IOException;
import java.io.Writer;

public interface Sitemesher {
	boolean needMesh();
	
	void meshup(Writer out, String src) throws IOException;
}
