package panda.mvc.init.conf;

import panda.mvc.annotation.Modules;
import panda.mvc.init.module.AtMapModule;
import panda.mvc.init.module.SimpleTestModule;

@Modules({AtMapModule.class, SimpleTestModule.class})
public class MainModuleAtMap {}
