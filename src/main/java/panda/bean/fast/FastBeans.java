package panda.bean.fast;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map.Entry;
import java.util.Set;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.PropertyAccessor;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.DynamicClassLoader;
import panda.lang.Exceptions;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;


/**
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
	private DynamicClassLoader dynamicClassLoader;

	/**
	 * excludes
	 */
	private Set<String> excludes = Arrays.toSet(
		"javax.servlet.http.Cookie");
	
	/**
	 * 
	 */
	public FastBeans() {
		dynamicClassLoader = new DynamicClassLoader();
	}

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
		Class clazz = Types.getRawType(type);
		if (!Modifier.isPublic(clazz.getModifiers()) || excludes.contains(clazz.getName())) {
			return super.createJavaBeanHandler(type);
		}

		try {
			return createFastBeanHandler(clazz);
		}
		catch (Throwable e) {
			log.warn("Failed to create FastBeanHandler for " 
					+ clazz + " use default JavaBeanHandler instead." 
					+ Streams.LINE_SEPARATOR + e.getMessage());
			return super.createJavaBeanHandler(type);
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized BeanHandler createFastBeanHandler(Class type) throws Exception {
		String typeName = type.getName().replace(Classes.INNER_CLASS_SEPARATOR_CHAR, '.');

		String orgPkgName = Classes.getPackageName(typeName);

		String packageName = "panda.bean.fast" + (orgPkgName.isEmpty() ? "" : ('.' + orgPkgName));
		String simpleName = Classes.getShortClassName(typeName) + "Handler";
		String className = packageName + "." + simpleName;

		try {
			Class clazz = dynamicClassLoader.loadClass(className);
			return (BeanHandler)(Classes.newInstance(clazz, this, Beans.class));
		}
		catch (ClassNotFoundException e) {
		}

		if (log.isDebugEnabled()) {
			log.debug("Build fast bean " + className + " for " + type);
		}

		StringBuilder src = new StringBuilder();

		src.append("package ").append(packageName).append(";\n");
		src.append("import ").append(Type.class.getName()).append(";\n");
		src.append("import ").append(Types.class.getName()).append(";\n");
		src.append("import ").append(Exceptions.class.getName()).append(";\n");
		src.append("public class ")
			.append(simpleName)
			.append(" extends ")
			.append(AbstractFastBeanHandler.class.getName())
			.append('<').append(typeName).append('>')
			.append(" {\n");

		src.append("  public ").append(simpleName).append("(")
			.append(Beans.class.getName()).append(" factory) {\n");
		src.append("    super(factory, ").append(typeName).append(".class);\n");
		src.append("  }\n");

		Set<Entry<String, PropertyAccessor>> accessors = Beans.getPropertyAccessors(type).entrySet();

		boolean first = true;
		src.append("  protected void init() {\n");
		src.append("    rpns = new String[] {\n");
		for (Entry<String, PropertyAccessor> en : accessors) {
			String pn = en.getKey();
			PropertyAccessor pa = en.getValue();
			if (pa.getGetter() != null) {
				src.append("      ").append(first ? "" : ", ").append("\"").append(pn).append("\"\n");
				first = false;
			}
		}
		src.append("    };\n");

		first = true;
		src.append("    wpns = new String[] {\n");
		for (Entry<String, PropertyAccessor> en : accessors) {
			String pn = en.getKey();
			PropertyAccessor pa = en.getValue();
			if (pa.getSetter() != null) {
				src.append("      ").append(first ? "" : ", ").append("\"").append(pn).append("\"\n");
				first = false;
			}
		}
		src.append("    };\n");

		int i = 0;
		src.append("    PropertyInfo pi;\n");
		for (Entry<String, PropertyAccessor> en : accessors) {
			String pn = en.getKey();
			PropertyAccessor pa = en.getValue();

			src.append("    pi = new PropertyInfo();\n");
			src.append("    pi.index = ").append(++i).append(";\n");;
			src.append("    pi.readable = ").append(pa.getGetter() != null).append(";\n");;
			src.append("    pi.writable = ").append(pa.getSetter() != null).append(";\n");;
			src.append("    mm.put(\"").append(pn).append("\", pi);\n");
		}
		src.append("  }\n");

		src.append("  public Type getPropertyType(").append(typeName).append(" bo, String pn) {\n");
		src.append("    PropertyInfo pi = mm.get(pn);\n");
		src.append("    if (pi == null) {\n");
//		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("      return null;\n");
		src.append("    }\n");
		src.append("    switch (pi.index) {\n");
		i = 0;
		for (Entry<String, PropertyAccessor> en : accessors) {
			PropertyAccessor pa = en.getValue();
			String pt = genPropertyType(pa);

			src.append("    case ").append(++i)
				.append(": return ")
				.append(pt).append(";\n");
		}
		src.append("    default:\n");
//		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("      return null;\n");
		src.append("    }\n");
		src.append("  }\n");

		src.append("  public Object getPropertyValue(").append(typeName).append(" bo, String pn) {\n");
		src.append("    PropertyInfo pi = mm.get(pn);\n");
		src.append("    if (pi == null) {\n");
//		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("      return null;\n");
		src.append("    }\n");
		src.append("    try {\n");
		src.append("      switch (pi.index) {\n");
		i = 0;
		for (Entry<String, PropertyAccessor> en : accessors) {
			PropertyAccessor pa = en.getValue();

			src.append("      case ").append(++i).append(": ");
			if (pa.getGetter() instanceof Field) {
				src.append("return bo.").append(pa.getGetter().getName()).append(';');
			}
			else if (pa.getGetter() instanceof Method) {
				src.append("return bo.").append(pa.getGetter().getName()).append("();");
			}
			else {
//				src.append("throw noGetterMethodException(pn);\n");
				src.append("return null;");
			}
			src.append('\n');
		}
		src.append("      default:");
//		src.append("throw noSuchPropertyException(pn);\n");
		src.append("return null;\n");
		src.append("      }\n");
		src.append("    } catch (Throwable e) { throw Exceptions.wrapThrow(e); }\n");
		src.append("  }\n");

		src.append("  public boolean setPropertyValue(").append(typeName).append(" bo, String pn, Object value) {\n");
		src.append("    PropertyInfo pi = mm.get(pn);\n");
		src.append("    if (pi == null) {\n");
//		src.append("      throw noSuchPropertyException(pn);\n");
		src.append("      return false;\n");
		src.append("    }\n");
		src.append("    try {\n");
		src.append("      switch (pi.index) {\n");
		i = 0;
		for (Entry<String, PropertyAccessor> en : accessors) {
			PropertyAccessor pa = en.getValue();
			String pt = Types.getCastableClassName(pa.getType());

			src.append("      case ").append(++i).append(": ");
			if (pa.getSetter() instanceof Field) {
				src.append("bo.").append(pa.getSetter().getName()).append(" = (").append(pt).append(")value; return true;");
			}
			else if (pa.getSetter() instanceof Method) {
				src.append("bo.").append(pa.getSetter().getName()).append("((").append(pt).append(")value); return true;");
			}
			else {
//				src.append("throw noSetterMethodException(pn);\n");
				src.append("return false;");
			}
			src.append('\n');
		}
		src.append("      default:");
//		src.append("throw noSuchPropertyException(pn);\n");
		src.append("return false;\n");
		src.append("      }\n");
		src.append("    } catch (Throwable e) { throw Exceptions.wrapThrow(e); }\n");
		src.append("  }\n");

		src.append("}\n");

		Class clazz = dynamicClassLoader.loadClass(className, src.toString());
		BeanHandler bh = (BeanHandler)(Classes.newInstance(clazz, this, Beans.class));

		return bh;
	}

	private void addPropertyType(StringBuilder propType, Type type) {
		if (type instanceof Class) {
			propType.append(Classes.getCastableClassName((Class)type)).append(".class");
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

	private String genPropertyType(PropertyAccessor pa) {
		StringBuilder pt = new StringBuilder();
		addPropertyType(pt, pa.getType());
		return pt.toString();
	}
}
