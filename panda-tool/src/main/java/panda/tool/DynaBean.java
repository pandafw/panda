package panda.tool;

import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Classes;
import panda.lang.DynamicClassLoader;
import panda.lang.Strings;


/**
 * utility class for bean 
 */
public class DynaBean {
	private static DynamicClassLoader dynamicClassLoader = new DynamicClassLoader();
	
	/**
	 * create dynamic bean class 
	 * @param className class name
	 * @param properties properties
	 * @return bean class
	 * @throws Exception if an error occurs
	 */
	public static Class createBeanClass(String className, Map<String, String> properties) throws Exception {
		String pkg = Classes.getPackageName(className);
		String cls = Classes.getShortClassName(className);
		
		StringBuilder src = new StringBuilder();
		
		src.append("package ").append(pkg).append(";\n\n");
		src.append("public class ").append(cls).append(" {\n");

		for (Entry<String, String> e : properties.entrySet()) {
			String n = e.getKey();
			String cn = Strings.capitalize(n);
			String t = e.getValue();

			src.append("  private ").append(t).append(' ').append(n).append(";\n");

			src.append("  public ").append(t).append(' ').append("get").append(cn)
				.append("() { return this.").append(n).append("; }\n");

			src.append("  public void ").append("set").append(cn)
				.append('(').append(t).append(" value) { this.")
				.append(n).append(" = value; }\n");
			
			src.append("\n");
		}
		
		src.append("}\n");
		
		return dynamicClassLoader.loadClass(className, src.toString());
	}
}
