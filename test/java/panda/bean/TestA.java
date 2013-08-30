package panda.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TestA
 */
public class TestA {
	/**
	 * TestB
	 */
	public static class TestB extends TestA {
		protected TestA testA;

		/**
		 * @return the testA
		 */
		public TestA getTestA() {
			return testA;
		}

		/**
		 * @param testA the testA to set
		 */
		public void setTestA(TestA testA) {
			this.testA = testA;
		}
	}

	protected boolean boolField;
	protected byte byteField;
	protected char charField;
	protected double doubleField;
	protected float floatField;
	protected int intField;
	protected long longField;
	protected short shortField;

	protected boolean[] boolArray;
	protected byte[] byteArray;
	protected char[] charArray;
	protected double[] doubleArray;
	protected float[] floatArray;
	protected int[] intArray;
	protected long[] longArray;
	protected short[] shortArray;

	protected Boolean boolWrap;
	protected Byte byteWrap;
	protected Character charWrap;
	protected Double doubleWrap;
	protected Float floatWrap;
	protected Integer intWrap;
	protected Long longWrap;
	protected Short shortWrap;

	protected String stringField;
	protected String[] stringArray;
	protected List<String> stringList;
	
	protected Date dateField;
	protected Date[] dateArray;
	protected Map<String, Date> dateMap;
	protected List<Map<String, Date>> dateMapList;
	
	protected Map<?, ? extends Number> numExtendMap;
	protected Map<?, int[]> intArrayMap;

	protected String getField;
	protected String setField;

	protected TestB testB;

	/**
	 * @return the boolField
	 */
	public boolean isBoolField() {
		return boolField;
	}

	/**
	 * @param boolField the boolField to set
	 */
	public void setBoolField(boolean boolField) {
		this.boolField = boolField;
	}

	/**
	 * @return the byteField
	 */
	public byte getByteField() {
		return byteField;
	}

	/**
	 * @param byteField the byteField to set
	 */
	public void setByteField(byte byteField) {
		this.byteField = byteField;
	}

	/**
	 * @return the charField
	 */
	public char getCharField() {
		return charField;
	}

	/**
	 * @param charField the charField to set
	 */
	public void setCharField(char charField) {
		this.charField = charField;
	}

	/**
	 * @return the doubleField
	 */
	public double getDoubleField() {
		return doubleField;
	}

	/**
	 * @param doubleField the doubleField to set
	 */
	public void setDoubleField(double doubleField) {
		this.doubleField = doubleField;
	}

	/**
	 * @return the floatField
	 */
	public float getFloatField() {
		return floatField;
	}

	/**
	 * @param floatField the floatField to set
	 */
	public void setFloatField(float floatField) {
		this.floatField = floatField;
	}

	/**
	 * @return the intField
	 */
	public int getIntField() {
		return intField;
	}

	/**
	 * @param intField the intField to set
	 */
	public void setIntField(int intField) {
		this.intField = intField;
	}

	/**
	 * @return the longField
	 */
	public long getLongField() {
		return longField;
	}

	/**
	 * @param longField the longField to set
	 */
	public void setLongField(long longField) {
		this.longField = longField;
	}

	/**
	 * @return the shortField
	 */
	public short getShortField() {
		return shortField;
	}

	/**
	 * @param shortField the shortField to set
	 */
	public void setShortField(short shortField) {
		this.shortField = shortField;
	}

	/**
	 * @return the boolArray
	 */
	public boolean[] getBoolArray() {
		return boolArray;
	}

	/**
	 * @param boolArray the boolArray to set
	 */
	public void setBoolArray(boolean[] boolArray) {
		this.boolArray = boolArray;
	}

	/**
	 * @return the byteArray
	 */
	public byte[] getByteArray() {
		return byteArray;
	}

	/**
	 * @param byteArray the byteArray to set
	 */
	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	/**
	 * @return the charArray
	 */
	public char[] getCharArray() {
		return charArray;
	}

	/**
	 * @param charArray the charArray to set
	 */
	public void setCharArray(char[] charArray) {
		this.charArray = charArray;
	}

	/**
	 * @return the doubleArray
	 */
	public double[] getDoubleArray() {
		return doubleArray;
	}

	/**
	 * @param doubleArray the doubleArray to set
	 */
	public void setDoubleArray(double[] doubleArray) {
		this.doubleArray = doubleArray;
	}

	/**
	 * @return the floatArray
	 */
	public float[] getFloatArray() {
		return floatArray;
	}

	/**
	 * @param floatArray the floatArray to set
	 */
	public void setFloatArray(float[] floatArray) {
		this.floatArray = floatArray;
	}

	/**
	 * @return the intArray
	 */
	public int[] getIntArray() {
		return intArray;
	}

	/**
	 * @param intArray the intArray to set
	 */
	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}

	/**
	 * @return the longArray
	 */
	public long[] getLongArray() {
		return longArray;
	}

	/**
	 * @param longArray the longArray to set
	 */
	public void setLongArray(long[] longArray) {
		this.longArray = longArray;
	}

	/**
	 * @return the shortArray
	 */
	public short[] getShortArray() {
		return shortArray;
	}

	/**
	 * @param shortArray the shortArray to set
	 */
	public void setShortArray(short[] shortArray) {
		this.shortArray = shortArray;
	}

	/**
	 * @return the boolWrap
	 */
	public Boolean getBoolWrap() {
		return boolWrap;
	}

	/**
	 * @param boolWrap the boolWrap to set
	 */
	public void setBoolWrap(Boolean boolWrap) {
		this.boolWrap = boolWrap;
	}

	/**
	 * @return the byteWrap
	 */
	public Byte getByteWrap() {
		return byteWrap;
	}

	/**
	 * @param byteWrap the byteWrap to set
	 */
	public void setByteWrap(Byte byteWrap) {
		this.byteWrap = byteWrap;
	}

	/**
	 * @return the charWrap
	 */
	public Character getCharWrap() {
		return charWrap;
	}

	/**
	 * @param charWrap the charWrap to set
	 */
	public void setCharWrap(Character charWrap) {
		this.charWrap = charWrap;
	}

	/**
	 * @return the doubleWrap
	 */
	public Double getDoubleWrap() {
		return doubleWrap;
	}

	/**
	 * @param doubleWrap the doubleWrap to set
	 */
	public void setDoubleWrap(Double doubleWrap) {
		this.doubleWrap = doubleWrap;
	}

	/**
	 * @return the floatWrap
	 */
	public Float getFloatWrap() {
		return floatWrap;
	}

	/**
	 * @param floatWrap the floatWrap to set
	 */
	public void setFloatWrap(Float floatWrap) {
		this.floatWrap = floatWrap;
	}

	/**
	 * @return the intWrap
	 */
	public Integer getIntWrap() {
		return intWrap;
	}

	/**
	 * @param intWrap the intWrap to set
	 */
	public void setIntWrap(Integer intWrap) {
		this.intWrap = intWrap;
	}

	/**
	 * @return the longWrap
	 */
	public Long getLongWrap() {
		return longWrap;
	}

	/**
	 * @param longWrap the longWrap to set
	 */
	public void setLongWrap(Long longWrap) {
		this.longWrap = longWrap;
	}

	/**
	 * @return the shortWrap
	 */
	public Short getShortWrap() {
		return shortWrap;
	}

	/**
	 * @param shortWrap the shortWrap to set
	 */
	public void setShortWrap(Short shortWrap) {
		this.shortWrap = shortWrap;
	}

	/**
	 * @return the stringField
	 */
	public String getStringField() {
		return stringField;
	}

	/**
	 * @param stringField the stringField to set
	 */
	public void setStringField(String stringField) {
		this.stringField = stringField;
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

	/**
	 * @return the stringArray
	 */
	public String[] getStringArray() {
		return stringArray;
	}

	/**
	 * @param stringArray the stringArray to set
	 */
	public void setStringArray(String[] stringArray) {
		this.stringArray = stringArray;
	}

	/**
	 * @return the dateArray
	 */
	public Date[] getDateArray() {
		return dateArray;
	}

	/**
	 * @param dateArray the dateArray to set
	 */
	public void setDateArray(Date[] dateArray) {
		this.dateArray = dateArray;
	}

	public List<String> getStringList() {
		return stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

	public Map<String, Date> getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map<String, Date> dateMap) {
		this.dateMap = dateMap;
	}

	public List<Map<String, Date>> getDateMapList() {
		return dateMapList;
	}

	public void setDateMapList(List<Map<String, Date>> dateMapList) {
		this.dateMapList = dateMapList;
	}

	public Map<?, ? extends Number> getNumExtendMap() {
		return numExtendMap;
	}

	public void setNumExtendMap(Map<?, ? extends Number> numExtendMap) {
		this.numExtendMap = numExtendMap;
	}

	public Map<?, int[]> getIntArrayMap() {
		return intArrayMap;
	}

	public void setIntArrayMap(Map<?, int[]> intArrayMap) {
		this.intArrayMap = intArrayMap;
	}

	/**
	 * @return the getField
	 */
	public String getGetField() {
		return getField;
	}

	/**
	 * @param setField the setField to set
	 */
	public void setSetField(String setField) {
		this.setField = setField;
	}

	/**
	 * @return the testB
	 */
	public TestB getTestB() {
		return testB;
	}

	/**
	 * @param testB the testB to set
	 */
	public void setTestB(TestB testB) {
		this.testB = testB;
	}

}
