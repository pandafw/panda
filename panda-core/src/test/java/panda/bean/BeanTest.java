package panda.bean;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import panda.log.Log;
import panda.log.Logs;
import junit.framework.TestCase;

public class BeanTest extends TestCase {

	private static Log log = Logs.getLog(BeanTest.class);

	private final static int limit = 5000000;

	public static class TestBean {
		private String strField;
		private Integer intField;
		private Long longField;
		private Float floatField;
		private Double doubleField;
		private Date dateField;
		protected Map<String, Integer> strIntMap;
		protected List<String> strList;

		/**
		 * @return the strField
		 */
		public String getStrField() {
			return strField;
		}
		/**
		 * @param strField the strField to set
		 */
		public void setStrField(String strField) {
			this.strField = strField;
		}
		/**
		 * @return the intField
		 */
		public Integer getIntField() {
			return intField;
		}
		/**
		 * @param intField the intField to set
		 */
		public void setIntField(Integer intField) {
			this.intField = intField;
		}
		/**
		 * @return the longField
		 */
		public Long getLongField() {
			return longField;
		}
		/**
		 * @param longField the longField to set
		 */
		public void setLongField(Long longField) {
			this.longField = longField;
		}
		/**
		 * @return the floatField
		 */
		public Float getFloatField() {
			return floatField;
		}
		/**
		 * @param floatField the floatField to set
		 */
		public void setFloatField(Float floatField) {
			this.floatField = floatField;
		}
		/**
		 * @return the doubleField
		 */
		public Double getDoubleField() {
			return doubleField;
		}
		/**
		 * @param doubleField the doubleField to set
		 */
		public void setDoubleField(Double doubleField) {
			this.doubleField = doubleField;
		}
		/**
		 * @return the dateField
		 */
		public Date getDateField() {
			return dateField;
		}
		/**
		 * @param dateField the dateField to set
		 */
		public void setDateField(Date dateField) {
			this.dateField = dateField;
		}
		public Map<String, Integer> getStrIntMap() {
			return strIntMap;
		}
		public void setStrIntMap(Map<String, Integer> strIntMap) {
			this.strIntMap = strIntMap;
		}
		public List<String> getStrList() {
			return strList;
		}
		public void setStrList(List<String> strList) {
			this.strList = strList;
		}
		
	}

	public void testDirect() {
		long st = System.currentTimeMillis();

		TestBean tb = new TestBean();
		for (int i = 0; i < limit; i++) {
			tb.setDateField(null);
			tb.setDoubleField(0.0);
			tb.setFloatField(0.0F);
			tb.setIntField(0);
			tb.setLongField(0L);
			tb.setStrField("");
		}
		
		long et = System.currentTimeMillis();
		
		log.info("testDirect - " + (et - st));
	}
	
	public void testReflect() throws Exception {
		Method setDateField = TestBean.class.getMethod("setDateField", Date.class);
		Method setDoubleField = TestBean.class.getMethod("setDoubleField", Double.class);
		Method setFloatField = TestBean.class.getMethod("setFloatField", Float.class);
		Method setIntField = TestBean.class.getMethod("setIntField", Integer.class);
		Method setLongField = TestBean.class.getMethod("setLongField", Long.class);
		Method setStrField = TestBean.class.getMethod("setStrField", String.class);

		long st = System.currentTimeMillis();

		TestBean tb = new TestBean();
		for (int i = 0; i < limit; i++) {
			setDateField.invoke(tb, (Date)null);
			setDoubleField.invoke(tb, 0.0);
			setFloatField.invoke(tb, 0.0F);
			setIntField.invoke(tb, 0);
			setLongField.invoke(tb, 0L);
			setStrField.invoke(tb, "");
		}
		
		long et = System.currentTimeMillis();
		
		log.info("testReflect - " + (et - st));
	}
	
	private void strCallMethod(TestBean tb, String m, Object p) {
		if ("dateField".equals(m)) {
			tb.setDateField((Date)p);
		}
		else if ("doubleField".equals(m)) {
			tb.setDoubleField((Double)p);
		}
		else if ("floatField".equals(m)) {
			tb.setFloatField((Float)p);
		}
		else if ("intField".equals(m)) {
			tb.setIntField((Integer)p);
		}
		else if ("longField".equals(m)) {
			tb.setLongField((Long)p);
		}
		else if ("strField".equals(m)) {
			tb.setStrField((String)p);
		}
	}
	
	public void testStrCall() throws Exception {
		long st = System.currentTimeMillis();

		TestBean tb = new TestBean();
		for (int i = 0; i < limit; i++) {
			strCallMethod(tb, "dateField", (Date)null);
			strCallMethod(tb, "doubleField", 0.0);
			strCallMethod(tb, "floatField", 0.0F);
			strCallMethod(tb, "intField", 0);
			strCallMethod(tb, "longField", 0L);
			strCallMethod(tb, "strField", "");
		}
		
		long et = System.currentTimeMillis();
		
		log.info("testStrCall - " + (et - st));
	}

	private static Map<String, Integer> mm = new HashMap<String, Integer>();
	
	static {
		mm.put("dateField", 1);
		mm.put("doubleField", 2);
		mm.put("floatField", 3);
		mm.put("intField", 4);
		mm.put("longField", 5);
		mm.put("strField", 6);
	}
	
	private void mapCallMethod(TestBean tb, String m, Object p) {
		Integer i = mm.get(m);
		
		switch (i) {
		case 1:
			tb.setDateField((Date)p);
			break;
		case 2:
			tb.setDoubleField((Double)p);
			break;
		case 3:
			tb.setFloatField((Float)p);
			break;
		case 4:
			tb.setIntField((Integer)p);
			break;
		case 5:
			tb.setLongField((Long)p);
			break;
		case 6:
			tb.setStrField((String)p);
		}
	}
	
	public void testMapCall() throws Exception {
		long st = System.currentTimeMillis();

		TestBean tb = new TestBean();
		for (int i = 0; i < limit; i++) {
			mapCallMethod(tb, "dateField", (Date)null);
			mapCallMethod(tb, "doubleField", 0.0);
			mapCallMethod(tb, "floatField", 0.0F);
			mapCallMethod(tb, "intField", 0);
			mapCallMethod(tb, "longField", 0L);
			mapCallMethod(tb, "strField", "");
		}
		
		long et = System.currentTimeMillis();
		
		log.info("testMapCall - " + (et - st));
	}
}
