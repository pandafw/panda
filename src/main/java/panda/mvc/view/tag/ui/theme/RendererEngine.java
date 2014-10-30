package panda.mvc.view.tag.ui.theme;


/**
 * Any template language which wants to support UI tag templating needs to provide an implementation
 * of this interface to handle rendering the template
 */
public interface RendererEngine {

	/**
	 * Renders the template
	 * 
	 * @param templateContext context for the given template.
	 * @throws Exception is thrown if there is a failure when rendering.
	 */
	void start(RenderingContext templateContext) throws Exception;

	/**
	 * Renders the template
	 * 
	 * @param templateContext context for the given template.
	 * @throws Exception is thrown if there is a failure when rendering.
	 */
	void end(RenderingContext templateContext) throws Exception;
}
