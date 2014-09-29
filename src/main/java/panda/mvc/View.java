package panda.mvc;


/**
 * 视图接口
 */
public interface View {
	void render(ActionContext ac) throws Throwable;
}
