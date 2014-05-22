package panda.ioc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import panda.ioc.aop.config.impl.AllAopConfigration;
import panda.ioc.json.AllJsonIoc;
import panda.ioc.loader.AllLoader;
import panda.ioc.val.AllVal;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AllJsonIoc.class, AllLoader.class, AllVal.class,
		AllAopConfigration.class, SimpleIocTest.class })
public class AllIoc {
}
