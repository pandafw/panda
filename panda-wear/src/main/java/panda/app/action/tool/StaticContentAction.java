package panda.app.action.tool;

import panda.mvc.annotation.At;
import panda.mvc.util.StaticResourceAction;

@At("${!!static_path|||'/static'}")
public class StaticContentAction extends StaticResourceAction {
}
