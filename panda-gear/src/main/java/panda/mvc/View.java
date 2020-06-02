package panda.mvc;

/**
 * View
 */
public interface View {
	void setArgument(String arg);
	
	void render(ActionContext ac);
}
