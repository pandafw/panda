package panda.ioc.loader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import panda.ioc.loader.annotation.AnnotationIocLoaderTest;
import panda.ioc.loader.xml.XmlIocLoaderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({XmlIocLoaderTest.class, AnnotationIocLoaderTest.class})
public class AllLoader {

}
