package panda.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import panda.bean.handlers.AbstractJavaBeanHandler;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.DynamicClassLoader;
import panda.lang.Types;
import panda.log.Log;
import panda.log.Logs;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class FastBeans extends Beans {
	/**
	 * log
	 */
	private static Log log = Logs.getLog(FastBeans.class);

	/**
	 * dynamicClassLoader
	 */
	private DynamicClassLoader dynamicClassLoader = new DynamicClassLoader();

	
	/**
	 * clear bean handlers
	 */
	@Override
	public void clear() {
		super.clear();
		dynamicClassLoader.clear();
	}

	/**
	 * create java bean handler
	 * @param type bean type
	 * @return BeanHandler
	 */
	@Override
	protected BeanHandler createJavaBeanHandler(Type type) {
		try {
			return createFastBeanHandler(Types.getRawType(type));
		}
		catch (Throwable e) {
			log.warn("Failed to create FastBeanHandler, use default JavaBeanHandler instead.", e);
			return super.createJavaBeanHandler(type);
		}
	}

	private synchronized BeanHandler createFastBeanHandler(Class type) throws Exception {
		String packageName = "panda.runtime." + type.getPackage().getName();
		String simpleName = type.getSimpleName() + "BeanHandler";
		String className = packageName + "." + simpleName;
		String typeName = type.getName().replace('$', '.');

		try {
			Class clazz = dynamicClassLoader.loadClass(className);
			return (BeanHandler)(Classes.newInstance(clazz, this, Beans.class));
		}
		catch (ClassNotFoundException e) {
		}

		StringBuilder src = new StringBuilder();

		src.append("package ").append(packageName).append(";\n");
		src.append("import ").append(Type.class.getName()).append(";\n");
		src.append("import ").append(Types.class.getName()).append(";\n");
		src.append("public class ")
			.append(simpleName)
			.append(" extends ")
			.append(AbstractJavaBeanHandler.class.getName())
			.append('<').append(typeName).append('>')
			.append(" {\n");

		src.append("  public ").append(simpleName).append("(")
			.append(Beans.class.getName()).append(" factory) {\n");
		src.append("    super(factory, ").append(typeName).append(".class);\n");
		src.append("  }\n");

		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		boolean first = true;
		src.append("  private static String[] rpns = new String[] {\n");
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			if (pd.getReadMethod() != null) {
				src.append("    ").append(first ? "" : ", ")
					.append("\"").append(propName).append("\"\n");
				first = false;
			}

			if (Character.isUpperCase(propName.charAt(0))) {
				propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
				if (pd.getReadMethod() != null) {
					src.append("    ").append(first ? "" : ", ")
						.append("\"").append(propName).append("\"\n");
				}
			}
		}
		src.append("  };\n");

		first = true;
		src.append("  private static String[] wpns = new String[] {\n");
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			if (pd.getWriteMethod() != null) {
				src.append("    ").append(first ? "" : ", ")
					.append("\"").append(propName).append("\"\n");
				first = false;
			}

			if (Character.isUpperCase(propName.charAt(0))) {
				propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
				if (pd.getWriteMethod() != null) {
					src.append("    ").append(first ? "" : ", ")
						.append("\"").append(propName).append("\"\n");
				}
			}
		}
		src.append("  };\n");

		int i = 0;
		src.append("  private static java.util.Map<String, Integer> mm = new java.util.HashMap<String, Integer>();\n");
		src.append("  static {\n");
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			src.append("    mm.put(\"").append(propName).append("\", ").append(++i).append(");\n");

			if (Character.isUpperCase(propName.charAt(0))) {
				propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
				src.append("    mm.put(\"").append(propName).append("\", ").append(++i).append(");\n");
			}
		}
		src.append("  }\n");

		src.append("  public String[] getReadPropertyNames(").append(typeName).append(" bo) {\n");
		src.append("    return rpns;\n");
		src.append("  }\n");
		
		src.append("  public String[] getWritePropertyNames(").append(typeName).append(" bo) {\n");
		src.append("    return wpns;\n");
		src.append("  }\n");
		
		src.append("  public Type getPropertyType(").append(typeName).append(" bo, String pn) {\n");
		src.append("    Integer i = mm.get(pn);\n");
		src.append("    if (i == null) {\n");
		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("    }\n");
		src.append("    switch (i) {\n");
		i = 0;
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			String propType = genPropertyType(pd);

			src.append("    case ").append(++i)
				.append(": return ")
				.append(propType).append(";\n");

			if (Character.isUpperCase(propName.charAt(0))) {
				propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
				src.append("    case ").append(++i)
					.append(": return ")
					.append(propType).append(";\n");
			}
		}
		src.append("    default:\n");
		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("    }\n");
		src.append("  }\n");

		src.append("  public Object getPropertyValue(").append(typeName).append(" bo, String pn) {\n");
		src.append("    Integer i = mm.get(pn);\n");
		src.append("    if (i == null) {\n");
		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("    }\n");
		src.append("    switch (i) {\n");
		i = 0;
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			String getter = null;
			if (pd.getReadMethod() != null) {
				getter = pd.getReadMethod().getName();
			}

			src.append("    case ").append(++i).append(": ");
			if (getter == null) {
				src.append("throw noGetterMethodException(pn);\n");
			}
			else {
				src.append("return bo.").append(getter).append("();\n");
			}

			if (Character.isUpperCase(propName.charAt(0))) {
				propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
				src.append("    case ").append(++i).append(": ");
				if (getter == null) {
					src.append("throw noGetterMethodException(pn);\n");
				}
				else {
					src.append("return bo.").append(getter).append("();\n");
				}
			}
		}
		src.append("    default:\n");
		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("    }\n");
		src.append("  }\n");

		src.append("  public void setPropertyValue(").append(typeName).append(" bo, String pn, Object value) {\n");
		src.append("    Integer i = mm.get(pn);\n");
		src.append("    if (i == null) {\n");
		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("    }\n");
		src.append("    switch (i) {\n");
		i = 0;
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propName = pd.getName();
			Type pt = Types.getPropertyType(pd);
			String propType = Types.getCastableClassName(pt);
			String setter = null;
			
			if (pd.getWriteMethod() != null) {
				setter = pd.getWriteMethod().getName();
			}

			src.append("    case ").append(++i).append(": ");
			if (setter == null) {
				src.append("throw noSetterMethodException(pn);\n");
			}
			else {
				src.append("bo.").append(setter).append("((")
					.append(propType).append(")value); return;\n");
			}

			if (Character.isUpperCase(propName.charAt(0))) {
				propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
				src.append("    case ").append(++i).append(": ");
				if (setter == null) {
					src.append("throw noSetterMethodException(pn);\n");
				}
				else {
					src.append("bo.").append(setter).append("((")
						.append(propType).append(")value); return;\n");
				}
			}
		}
		src.append("    default:\n");
		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("    }\n");
		src.append("  }\n");

		src.append("}\n");

		Class clazz = dynamicClassLoader.loadClass(className, src.toString());
		BeanHandler bh = (BeanHandler)(Classes.newInstance(clazz, this, Beans.class));

		return bh;
	}

	private void addPropertyType(StringBuilder propType, Type type) {
		if (type instanceof Class) {
			propType.append(Classes.getQualifiedClassName((Class)type)).append(".class");
		}
		else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)type;
			
			propType.append(Types.class.getSimpleName());
			propType.append(".paramTypeOfOwner(");
			if (pt.getOwnerType() == null) {
				propType.append("null, ");
			}
			else {
				addPropertyType(propType, pt.getOwnerType());
				propType.append(", ");
			}
			addPropertyType(propType, pt.getRawType());
			for (Type a : pt.getActualTypeArguments()) {
				propType.append(", ");
				addPropertyType(propType, a);
			}
			propType.append(")");
		}
		else if (type instanceof WildcardType) {
			WildcardType wt = (WildcardType)type;

			propType.append(Types.class.getSimpleName());
			if (Arrays.isNotEmpty(wt.getLowerBounds())) {
				propType.append(".superTypeOf(");
				addPropertyType(propType, wt.getLowerBounds()[0]);
				propType.append(")");
			}
			else if (Arrays.isNotEmpty(wt.getUpperBounds())) {
				propType.append(".subTypeOf(");
				addPropertyType(propType, wt.getUpperBounds()[0]);
				propType.append(")");
			}
		}
		else if (type instanceof GenericArrayType) {
			GenericArrayType at = (GenericArrayType)type;

			propType.append(Types.class.getSimpleName());
			propType.append(".arrayTypeOf(");
			addPropertyType(propType, at.getGenericComponentType());
			propType.append(")");
		}
		else if (type instanceof TypeVariable) {
			Type[] bounds = Types.getImplicitBounds((TypeVariable<?>)type);
			addPropertyType(propType, bounds[0]);
		}
		else {
			propType.append(type.toString()).append(".class");
		}
	}

	private String genPropertyType(PropertyDescriptor descriptor) {
		Type type = Types.getPropertyType(descriptor);

		StringBuilder propType = new StringBuilder();
		addPropertyType(propType, type);
		
		return propType.toString();
	}
}
