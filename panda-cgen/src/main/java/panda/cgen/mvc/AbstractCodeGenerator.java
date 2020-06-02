package panda.cgen.mvc;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import panda.args.Option;
import panda.cgen.AbstractCommandTool;
import panda.cgen.HandledException;
import panda.cgen.mvc.bean.EntityProperty;
import panda.cgen.mvc.bean.Module;
import panda.cgen.mvc.bean.Param;
import panda.io.FileNames;
import panda.io.Settings;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.mvc.annotation.validate.BinaryValidate;
import panda.mvc.annotation.validate.CIDRValidate;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.CreditCardNoValidate;
import panda.mvc.annotation.validate.DateValidate;
import panda.mvc.annotation.validate.DecimalValidate;
import panda.mvc.annotation.validate.ELValidate;
import panda.mvc.annotation.validate.EmailValidate;
import panda.mvc.annotation.validate.EmptyValidate;
import panda.mvc.annotation.validate.FileValidate;
import panda.mvc.annotation.validate.FilenameValidate;
import panda.mvc.annotation.validate.ImageValidate;
import panda.mvc.annotation.validate.ImailValidate;
import panda.mvc.annotation.validate.NumberValidate;
import panda.mvc.annotation.validate.ProhibitedValidate;
import panda.mvc.annotation.validate.RegexValidate;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.annotation.validate.URLValidate;
import panda.mvc.annotation.validate.VisitValidate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Base class for code generator.
 */
public abstract class AbstractCodeGenerator extends AbstractCommandTool {
	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected File dir;
	protected String[] includes;
	protected String[] excludes;

	protected File out;
	protected int verbose = 0;
	protected int cntModule = 0;
	protected int cntFile = 0;
	protected Validator validator;
	
	/**
	 * Constructor
	 */
	public AbstractCodeGenerator() {
		dir = new File(".");
		out = new File(".");
	}

	/**
	 * @param dir the dir to set
	 */
	@Option(opt='d', option="dir", arg="DIR", usage="The directory which contains configuration files.")
	public void setDir(File dir) {
		this.dir = dir;
	}

	/**
	 * @param includes the includes to set
	 */
	@Option(opt='i', option="includes", arg="PATTERN", usage="The pattern of source files to import")
	public void setIncludes(String includes) {
		this.includes = Strings.trimAll(Strings.split(includes, ','));
	}

	/**
	 * @param excludes the excludes to set
	 */
	@Option(opt='e', option="excludes", arg="PATTERN", usage="The pattern of source files to exclude")
	public void setExcludes(String excludes) {
		this.excludes = Strings.trimAll(Strings.split(excludes, ','));
	}

	/**
	 * @param out the output directory to set
	 */
	@Option(opt='o', option="out", arg="DIR", usage="Output directory. (default is current directory.)")
	public void setOut(File out) {
		this.out = out;
	}

	/**
	 * @param verbose the verbose to set
	 */
	@Option(opt='v', option="verbose", arg="LEVEL", usage="Print information level (1-5)")
	public void setVerbose(int verbose) {
		this.verbose = verbose;
	}

	protected boolean needTranslate(String val) {
		return Strings.isNotEmpty(val) && val.indexOf("${") >= 0;
	}

	protected String translateValue(String val, Settings properties) throws Exception {
		return Texts.translate(val, properties);
	}

	protected void translateProps(Settings props) throws Exception {
		return;
//		boolean t = true;
//		while (t) {
//			t = false;
//			for (String key : props.keySet()) {
//				String val = props.get(key);
//				if (needTranslate(val)) {
//					System.out.println(">>> translate: " + val);
//					String nv = translateValue(val, props);
//					if (val.equals(nv)) {
//						throw new RuntimeException(nv);
//					}
//					props.put(key, nv);
//					t = true;
//				}
//			}
//		}
	}

	protected void translateDom(Node node, Settings properties) throws Exception {
		NamedNodeMap nnm = node.getAttributes();
		for (int i = 0; nnm != null && i < nnm.getLength(); i++) {
			Node n = nnm.item(i);
			String val = n.getNodeValue();
			if (needTranslate(val)) {
				val = translateValue(val, properties);
				n.setNodeValue(val);
			}
		}
		
		NodeList nl = node.getChildNodes();
		for (int i = 0; nl != null && i < nl.getLength(); i++) {
			Node n = nl.item(i);
			translateDom(n, properties);
		}
		
		String val = node.getNodeValue();
		if (needTranslate(val)) {
			val = translateValue(val, properties);
			node.setNodeValue(val);
		}
	}
	
	protected void initValidator() throws Exception {
		InputStream is = AbstractCodeGenerator.class.getResourceAsStream("Module.xsd");
		
		if (is == null) {
			throw new RuntimeException("Failed to load resource Module.xsd");
		}
		
		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		Schema schema = factory.newSchema(new StreamSource(is));

		// create a Validator instance, which can be used to validate an instance document
		validator = schema.newValidator();
		
		Streams.safeClose(is);
	}

	protected void validateDom(String file, Node node) throws Exception {
		try {
			// validate the DOM tree
			validator.validate(new DOMSource(node));
		}
		catch (SAXParseException e) {
			throw new RuntimeException(file + ": " + e.getMessage(), e);
		}
	}

	protected void includeDom(String base, Document doc, Settings properties) throws Exception {
		List<Node> incList = new ArrayList<Node>();
		
		Element el = doc.getDocumentElement();
		NodeList nl = el.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node in = nl.item(i);
			if (in.getNodeType() != Node.ELEMENT_NODE || !"include".equals(in.getNodeName())) {
				continue;
			}
			
			Node tn = in.getFirstChild();
			if (tn == null) {
				throw new Exception("<include> mush have a text node");
			}

			String inc = tn.getNodeValue();
			if (needTranslate(inc)) {
				inc = translateValue(inc, properties);
			}

			if (inc.startsWith("*")) {
				inc = inc.substring(1);
				if (inc.endsWith(".properties")) {
					properties.load(getClass().getResourceAsStream(inc), inc);
				}
				else if (inc.endsWith(".xml")) {
					Document idoc = loadDocument(inc, properties);
					Node iec = doc.importNode(idoc.getDocumentElement(), true);
					for (Node fc = iec.getFirstChild(); fc != null; fc = iec.getFirstChild()) {
						el.appendChild(iec.removeChild(fc));
					}
				}
			}
			else {
				File fi = new File(FileNames.concat(base, inc));
				if (inc.endsWith(".properties")) {
					properties.load(fi);
				}
				else if (inc.endsWith(".xml")) {
					Document idoc = loadDocument(fi, properties);
					Node iec = doc.importNode(idoc.getDocumentElement(), true);
					for (Node fc = iec.getFirstChild(); fc != null; fc = iec.getFirstChild()) {
						el.appendChild(iec.removeChild(fc));
					}
				}
			}
			incList.add(in);
		}
		
		for (Node n : incList) {
			n.getParentNode().removeChild(n);
		}
	}

	protected Document loadDocument(File file, Settings properties) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		includeDom(file.getParent(), doc, properties);
		validateDom(file.getPath(), doc);
		
		return doc;
	}

	protected Document loadDocument(String resource, Settings properties) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(getClass().getResourceAsStream(resource));
		
		includeDom(resource, doc, properties);
		validateDom(resource, doc);
		
		return doc;
	}
	
	protected Module parseModule(File f) throws Exception {
		Settings properties = new Settings();
		
		properties.load(getClass().getResourceAsStream("default.properties"), "default.properties");

		Document doc = loadDocument(f, properties);
		translateProps(properties);
		translateDom(doc, properties);
		
		JAXBContext context = JAXBContext.newInstance(Classes.getPackageName(Module.class), getClass().getClassLoader());
		Unmarshaller unmarshaller = context.createUnmarshaller();
 
		@SuppressWarnings("rawtypes")
		JAXBElement je = (JAXBElement)(unmarshaller.unmarshal(doc));
		Module module = (Module)je.getValue();
		
		module.setProps(properties);
		
		return module;
	}

	protected Configuration ftlConfig;

	protected void loadTemplates(Configuration cfg) throws Exception {
	}

	@SuppressWarnings("deprecation")
	protected void initTemplate() throws Exception {
		ftlConfig = new Configuration();

		ftlConfig.setClassForTemplateLoading(this.getClass(), "");

		DefaultObjectWrapper ow = new DefaultObjectWrapper();

		ftlConfig.setObjectWrapper(ow);

		loadTemplates(ftlConfig);
	}

	/**
	 * execute
	 */
	public void execute() {
		try {
			cntFile = 0;
			cntModule = 0;

			checkParameters();
			
			initValidator();

			initTemplate();
	
			preProcess();
			
			scan(dir);
			
			postProcess();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new HandledException(e);
		}
	}

	protected void checkParameters() throws Exception {
		AbstractCommandTool.checkRequired(includes, "includes");
		AbstractCommandTool.checkRequired(out, "out");

		for (int i = 0; i < includes.length; i++) {
			String s = includes[i];
			includes[i] = Strings.replaceChars(s, '/', File.separatorChar);
		}
	}
	
	protected boolean isExclude(File f) {
		if (dir.equals(f)) {
			return false;
		}
		if (excludes != null) {
			for (String s : excludes) {
				if (FileNames.pathMatch(FileNames.removeLeadingPath(dir, f), s)) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean isInclude(File f) {
		if (dir.equals(f)) {
			return true;
		}
		for (String s : includes) {
			if (FileNames.pathMatch(FileNames.removeLeadingPath(dir, f), s)) {
				return true;
			}
		}
		return false;
	}
	
	protected void scan(File f) throws Exception {
		if (f.isHidden()) {
			return;
		}
		else if (f.isDirectory()) {
			if (!isExclude(f)) {
				File[] sfs = f.listFiles();
				for (File sf : sfs) {
					scan(sf);
				}
			}
		}
		else if (f.isFile()) {
			if (!isExclude(f) && isInclude(f)) {
				cntFile++;
				process(f);
			}
		}
	}

	protected void preProcess() throws Exception {
		print0("Processing modules: " + dir.getPath());
	}

	protected void process(File file) throws Exception {
		print1("Processing module: " + file.getName());
		Module m = parseModule(file);
		prepareModule(m);
		processModule(m);
		cntModule++;
	}

	protected void prepareModule(Module module) throws Exception {
		module.prepare();
	}

	protected abstract void processModule(Module m) throws Exception;

	protected void postProcess() throws Exception {
		print0(cntModule + " modules processed, " + cntFile + " files generated successfully.");
	}

	public String println(String s) {
		System.out.println(s);
		return "";
	}

	protected void print0(String s) {
		if (verbose >= 0) {
			System.out.println(s);
		}
	}

	protected void print1(String s) {
		if (verbose >= 1) {
			System.out.println(s);
		}
	}

	protected void print2(String s) {
		if (verbose >= 2) {
			System.out.println(s);
		}
	}

	protected void print3(String s) {
		if (verbose >= 3) {
			System.out.println(s);
		}
	}

	protected void print4(String s) {
		if (verbose >= 4) {
			System.out.println(s);
		}
	}

	protected void print5(String s) {
		if (verbose >= 5) {
			System.out.println(s);
		}
	}
	
	private void processTpl(String pkg, String name, Template tpl, Map<String, Object> context, String charset) throws Exception {
		if (Strings.isBlank(pkg)) {
			throw new Exception("package name of [" + name + "] can not be empty");
		}

		PrintWriter pw = null;
		try {
			File dir = new File(out, pkg.replace('.', '/'));
			dir.mkdirs();

			File file = new File(dir.getPath(), name);
			print3("Generate - " + file.getPath());

			if (charset == null) {
				pw = new PrintWriter(file);
			}
			else {
				pw = new PrintWriter(file, charset);
			}

			tpl.process(context, pw);
			pw.flush();

			cntFile++;
		}
		catch (Exception e) {
			System.err.println("========== PROPS ============");
			Map props = (Map)context.get("props");
			@SuppressWarnings("unchecked")
			TreeMap tm = new TreeMap(props);
			for (Object en : tm.entrySet()) {
				System.err.println(((Entry)en).getKey() + ": " + ((Entry)en).getValue());
			}
			throw e;
		}
		finally {
			Streams.safeClose(pw);
		}
	}
	
	protected void processTpl(String pkg, String name, Map<String, Object> context, Template tpl) throws Exception {
		processTpl(pkg, name, context, tpl, false);
	}

	protected void processTpl(String pkg, String name, Map<String, Object> context, Template tpl, boolean serializable) throws Exception {
		context.put("package", pkg);
		context.put("name", FileNames.removeExtension(name));
		if (serializable) {
			context.put("svuid", 1);

			StringWriter sw = new StringWriter();
			tpl.process(context, sw);
			int svuid = Strings.remove(sw.toString(), '\r').hashCode();
			
			context.put("svuid", svuid);
		}
		
		processTpl(pkg, name, tpl, context, Charsets.UTF_8);

		if (serializable) {
			context.remove("svuid");
		}
	}

	protected void checkLicense(Module m, String pkg) {
//		if (Strings.isBlank(pkg)) {
//			throw new IllegalArgumentException("package is required.");
//		}
//		
//		pkg = Strings.replaceChars(pkg, '/', '.');
//		String license = m.getProps().getProperty("license");
//		try {
//			license = StringCryptoUtils.decrypt(license);
//			String[] ls = Strings.split(license, ',');
//			if (pkg.startsWith(ls[0])) {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//				Date ld = sdf.parse(ls[1]);
//				if (ld.after(Calendar.getInstance().getTime())) {
//					return;
//				}
//			}
//		}
//		catch (Exception e) {
//		}
//
//		throw new IllegalLicenseException("Illegal license: " + pkg);
	}

	/**
	 * add type to set
	 * @param imports import set
	 * @param type java type
	 */
	protected void addImportType(Set<String> imports, String type) {
		if (type.endsWith(Classes.ARRAY_SUFFIX)) {
			type = type.substring(0, type.length() - Classes.ARRAY_SUFFIX.length());
		}
		
		int lt = type.indexOf('<');
		int gt = type.lastIndexOf('>');
		
		if (lt > 0 && gt > 0 && gt > lt) {
			addImportType(imports, type.substring(0, lt));
			type = type.substring(lt + 1, gt);
			String[] ts = Strings.split(type, ", ");
			for (String t : ts) {
				addImportType(imports, t);
			}
		}
		else {
			if (type.indexOf(".") > 0 && !type.startsWith("java.lang.")) {
				imports.add(type);
			}
		}
	}
	
	//---------------------------------------------------------------
	private static Map<String, String> vtmap = new HashMap<String, String>();
	static {
		vtmap.put("cast", CastErrorValidate.class.getSimpleName());
		vtmap.put("required", RequiredValidate.class.getSimpleName());
		vtmap.put("empty", EmptyValidate.class.getSimpleName());

		vtmap.put("el", ELValidate.class.getSimpleName());
		vtmap.put("regex", RegexValidate.class.getSimpleName());
		vtmap.put("email", EmailValidate.class.getSimpleName());
		vtmap.put("imail", ImailValidate.class.getSimpleName());
		vtmap.put("url", URLValidate.class.getSimpleName());
		vtmap.put("filename", FilenameValidate.class.getSimpleName());
		vtmap.put("creditcardno", CreditCardNoValidate.class.getSimpleName());

		vtmap.put("binary", BinaryValidate.class.getSimpleName());
		vtmap.put("cidr", CIDRValidate.class.getSimpleName());
		vtmap.put("date", DateValidate.class.getSimpleName());

		vtmap.put("byte", NumberValidate.class.getSimpleName());
		vtmap.put("short", NumberValidate.class.getSimpleName());
		vtmap.put("int", NumberValidate.class.getSimpleName());
		vtmap.put("long", NumberValidate.class.getSimpleName());
		vtmap.put("float", NumberValidate.class.getSimpleName());
		vtmap.put("double", NumberValidate.class.getSimpleName());
		vtmap.put("number", NumberValidate.class.getSimpleName());

		vtmap.put("string", StringValidate.class.getSimpleName());
		vtmap.put("stringlength", StringValidate.class.getSimpleName());
		vtmap.put("stringtype", StringValidate.class.getSimpleName());

		vtmap.put("decimal", DecimalValidate.class.getSimpleName());

		vtmap.put("file", FileValidate.class.getSimpleName());
		vtmap.put("image", ImageValidate.class.getSimpleName());

		vtmap.put("constant", ConstantValidate.class.getSimpleName());
		vtmap.put("prohibited", ProhibitedValidate.class.getSimpleName());

		vtmap.put("visit", VisitValidate.class.getSimpleName());
	}

	private static Set<String> castErrorTypes = Arrays.toSet(
		"boolean", "Boolean", 
		"byte", "Byte", 
		"short", "Short",
		"int", "Integer",
		"long", "Long",
		"float", "Float",
		"double", "Double",
		"Number", "BigDecimal", "BigInteger",
		"Date", "Calendar",
		"URL", "EmailAddress");
	
	public boolean castErrorType(String type) {
		return castErrorTypes.contains(type);
	}
	
	public boolean castErrorProperty(EntityProperty ep) {
		return !"byte[]".equals(ep.getSimpleJavaType()) && castErrorType(ep.getElementType());
	}
	
	public String validatorAnnotation(panda.cgen.mvc.bean.Validator v) {
		StringBuilder sb = new StringBuilder();
		
		String type = validatorType(v.getType());
		sb.append(type);
		
		sb.append(validatorParams(v, type));
		return sb.toString();
	}
	
	protected String validatorType(String alias) {
		if (Strings.endsWith(alias, "Validate")) {
			return alias;
		}

		String vt = vtmap.get(alias);
		if (vt != null) {
			return vt;
		}
		
		vt = Strings.capitalize(alias);
		return vt;
	}

	private String validatorParams(panda.cgen.mvc.bean.Validator v, String type) {
		StringBuilder sb = new StringBuilder();
		
		if (Strings.isNotEmpty(v.getRefer())) {
			sb.append("refer=\"").append(StringEscapes.escapeJava(v.getRefer())).append('"');
		}
		if (v.isHasParams()) {
			for (Param p : v.getParamList()) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(p.getName()).append('=');
				String q = quoteValidatorParam(type, p);
				sb.append(q);
				sb.append(p.getValue());
				sb.append(q);
			}
		}
		if (Strings.isNotEmpty(v.getMessage())) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("message=\"").append(StringEscapes.escapeJava(v.getMessage())).append('"');
		}
		if (Strings.isNotEmpty(v.getMsgId())) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("msgId=").append(validatorMsgId(v.getMsgId()));
		}
		if (Strings.isNotEmpty(v.getShortCircuit())) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append("shortCircuit=").append(v.getShortCircuit());
		}
		
		if (sb.length() > 0) {
			sb.insert(0, '(').append(')');
		}
		return sb.toString();
	}

	private String quoteValidatorParam(String type, Param p) {
		if (ConstantValidate.class.getSimpleName().equals(type)) {
			return ("list".equals(p.getName())) ? "\"" : "";
		}
		if (CreditCardNoValidate.class.getSimpleName().equals(type)) {
			return ("list".equals(p.getName())) ? "\"" : "";
		}
		if (DateValidate.class.getSimpleName().equals(type)) {
			return "\"";
		}
		if (DecimalValidate.class.getSimpleName().equals(type)) {
			return ("type".equals(p.getName())) ? "'" : "";
		}
		if (ELValidate.class.getSimpleName().equals(type)) {
			return "\"";
		}
		if (NumberValidate.class.getSimpleName().equals(type)) {
			return "\"";
		}
		if (ProhibitedValidate.class.getSimpleName().equals(type)) {
			return ("list".equals(p.getName())) ? "\"" : "";
		}
		if (RegexValidate.class.getSimpleName().equals(type)) {
			return ("regex".equals(p.getName())) ? "\"" : "";
		}
		if (RequiredValidate.class.getSimpleName().equals(type)) {
			return ("fields".equals(p.getName())) ? "" : "\"";
		}
		if (StringValidate.class.getSimpleName().equals(type)) {
			return ("type".equals(p.getName())) ? "'" : "";
		}
		
		return "";
	}
	
	private static Map<String, String> vmmap = new HashMap<String, String>();
	static {
		vmmap.put("cast-boolean", "Validators.MSGID_BOOLEAN");
		vmmap.put("cast-Boolean", "Validators.MSGID_BOOLEAN");

		vmmap.put("cast-byte", "Validators.MSGID_INTEGER");
		vmmap.put("cast-Byte", "Validators.MSGID_INTEGER");
		vmmap.put("cast-short", "Validators.MSGID_INTEGER");
		vmmap.put("cast-Short", "Validators.MSGID_INTEGER");
		vmmap.put("cast-int", "Validators.MSGID_INTEGER");
		vmmap.put("cast-Integer", "Validators.MSGID_INTEGER");
		vmmap.put("cast-long", "Validators.MSGID_INTEGER");
		vmmap.put("cast-Long", "Validators.MSGID_INTEGER");
		vmmap.put("cast-BigInteger", "Validators.MSGID_INTEGER");

		vmmap.put("cast-float", "Validators.MSGID_DECIMAL");
		vmmap.put("cast-Float", "Validators.MSGID_DECIMAL");
		vmmap.put("cast-double", "Validators.MSGID_DECIMAL");
		vmmap.put("cast-Double", "Validators.MSGID_DECIMAL");
		vmmap.put("cast-BigDecimal", "Validators.MSGID_DECIMAL");

		vmmap.put("cast-Date", "Validators.MSGID_DATE");
		vmmap.put("cast-Calendar", "Validators.MSGID_DATE");
		vmmap.put("cast-File", "Validators.MSGID_FILE");
		vmmap.put("cast-FileItem", "Validators.MSGID_FILE");
		vmmap.put("cast-URL", "Validators.MSGID_URL");
		vmmap.put("cast-Email", "Validators.MSGID_EMAIL");

		vmmap.put("password", "Validators.MSGID_PASSWORD");
		vmmap.put("byte", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("short", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("int", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("long", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("float", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("double", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("decimal", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("number", "Validators.MSGID_NUMBER_RANGE");

		vmmap.put("date", "Validators.MSGID_DATE");
		vmmap.put("time", "Validators.MSGID_TIME");
		vmmap.put("datetime", "Validators.MSGID_DATETIME");
		
		vmmap.put("locale", "Validators.MSGID_LOCALE");
		vmmap.put("incorrect", "Validators.MSGID_INCORRECT");
	}

	public boolean useValidatorsMsgId(String alias) {
		if (Strings.startsWithChar(alias, '"')) {
			return false;
		}

		if (Strings.startsWith(alias, "MSGID_")) {
			return true;
		}
		
		String vm = vmmap.get(alias);
		return (vm != null);
	}
	
	public String validatorMsgId(String alias) {
		if (Strings.startsWithChar(alias, '"')) {
			return alias;
		}

		if (Strings.startsWith(alias, "MSGID_")) {
			return "Validators." + alias;
		}
		
		String vm = vmmap.get(alias);
		if (vm != null) {
			return vm;
		}
		return '"' + alias + '"';
	}
}
