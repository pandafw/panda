package panda.mvc.view.ftl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import panda.io.FileNames;
import freemarker.cache.TemplateCache;
import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class SafeInclude implements TemplateDirectiveModel {

	private static final String PATH_PARAM = "path";
	public static final String MACRO_NAME = "safeinclude";

	@Override
	@SuppressWarnings("deprecation")
	public void execute(Environment environment, Map map, TemplateModel[] templateModel,
			TemplateDirectiveBody directiveBody) throws TemplateException, IOException {

		if (!map.containsKey(PATH_PARAM)) {
			throw new RuntimeException("missing required parameter '" + PATH_PARAM + "' for macro " + MACRO_NAME);
		}

		// get the current template's parent directory to use when searching for relative paths
		final String currentTemplateName = environment.getTemplate().getName();
		final String currentTemplateDir = FileNames.getPath(currentTemplateName);

		// look up the path relative to the current working directory (this also works for absolute
		// paths)
		final String path = map.get(PATH_PARAM).toString();
		final String fullTemplatePath = TemplateCache.getFullTemplatePath(environment, currentTemplateDir, path);

		try {
			Template tpl = environment.getTemplateForInclusion(fullTemplatePath, null, true);
			environment.include(tpl);
		}
		catch (FileNotFoundException e) {
			// otherwise render the nested content, if there is any
			if (directiveBody != null) {
				directiveBody.render(environment.getOut());
			}
		}
	}
}
