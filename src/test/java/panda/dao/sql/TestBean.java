package panda.dao.sql;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import panda.lang.Objects;

/**
 * TestA
 */
public class TestBean implements Cloneable {
	private Integer id;
	
	private Boolean fbit;
	private boolean fbool;

	private char fchar;
	private String fstr;

	private byte ftinyint;
	private short fsmallint;
	private int finteger;
	private long fbigint;

	private double freal;
	private float ffloat;
	private double fdouble;
	private BigDecimal fdecimal;
	private BigDecimal fnumeric;

	private Date fdate;
	private Date ftime;
	private Calendar ftimestamp;

	private String fclob;
	private String flongvarchar;

	private byte[] fblob;
	private byte[] fbinary;
	private byte[] fvarbinary;
	private byte[] flongvarbinary;

	// ---------------------------------------
	private int[] intArray;
	private List<Integer> intList;
	
	private String[] strArray;
	private List<String> strList;
	
	private String orderCol;
	private String orderDir;
	
	private TestBean a;

	//-----------------------------------------------------------
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the fbit
	 */
	public Boolean getFbit() {
		return fbit;
	}

	/**
	 * @param fbit the fbit to set
	 */
	public void setFbit(Boolean fbit) {
		this.fbit = fbit;
	}




	/**
	 * @return the fbool
	 */
	public boolean isFbool() {
		return fbool;
	}




	/**
	 * @param fbool the fbool to set
	 */
	public void setFbool(boolean fbool) {
		this.fbool = fbool;
	}




	/**
	 * @return the fchar
	 */
	public char getFchar() {
		return fchar;
	}




	/**
	 * @param fchar the fchar to set
	 */
	public void setFchar(char fchar) {
		this.fchar = fchar;
	}




	/**
	 * @return the fstr
	 */
	public String getFstr() {
		return fstr;
	}




	/**
	 * @param fstr the fstr to set
	 */
	public void setFstr(String fstr) {
		this.fstr = fstr;
	}




	/**
	 * @return the ftinyint
	 */
	public byte getFtinyint() {
		return ftinyint;
	}




	/**
	 * @param ftinyint the ftinyint to set
	 */
	public void setFtinyint(byte ftinyint) {
		this.ftinyint = ftinyint;
	}




	/**
	 * @return the fsmallint
	 */
	public short getFsmallint() {
		return fsmallint;
	}




	/**
	 * @param fsmallint the fsmallint to set
	 */
	public void setFsmallint(short fsmallint) {
		this.fsmallint = fsmallint;
	}




	/**
	 * @return the finteger
	 */
	public int getFinteger() {
		return finteger;
	}




	/**
	 * @param finteger the finteger to set
	 */
	public void setFinteger(int finteger) {
		this.finteger = finteger;
	}




	/**
	 * @return the fbigint
	 */
	public long getFbigint() {
		return fbigint;
	}




	/**
	 * @param fbigint the fbigint to set
	 */
	public void setFbigint(long fbigint) {
		this.fbigint = fbigint;
	}


	/**
	 * @return the freal
	 */
	public double getFreal() {
		return freal;
	}

	/**
	 * @param freal the freal to set
	 */
	public void setFreal(double freal) {
		this.freal = freal;
	}



	/**
	 * @return the ffloat
	 */
	public float getFfloat() {
		return ffloat;
	}




	/**
	 * @param ffloat the ffloat to set
	 */
	public void setFfloat(float ffloat) {
		this.ffloat = ffloat;
	}




	/**
	 * @return the fdouble
	 */
	public double getFdouble() {
		return fdouble;
	}




	/**
	 * @param fdouble the fdouble to set
	 */
	public void setFdouble(double fdouble) {
		this.fdouble = fdouble;
	}




	/**
	 * @return the fdecimal
	 */
	public BigDecimal getFdecimal() {
		return fdecimal;
	}




	/**
	 * @param fdecimal the fdecimal to set
	 */
	public void setFdecimal(BigDecimal fdecimal) {
		this.fdecimal = fdecimal;
	}




	/**
	 * @return the fnumeric
	 */
	public BigDecimal getFnumeric() {
		return fnumeric;
	}




	/**
	 * @param fnumeric the fnumeric to set
	 */
	public void setFnumeric(BigDecimal fnumeric) {
		this.fnumeric = fnumeric;
	}




	/**
	 * @return the fdate
	 */
	public Date getFdate() {
		return fdate;
	}




	/**
	 * @param fdate the fdate to set
	 */
	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}




	/**
	 * @return the ftime
	 */
	public Date getFtime() {
		return ftime;
	}




	/**
	 * @param ftime the ftime to set
	 */
	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}




	/**
	 * @return the ftimestamp
	 */
	public Calendar getFtimestamp() {
		return ftimestamp;
	}




	/**
	 * @param ftimestamp the ftimestamp to set
	 */
	public void setFtimestamp(Calendar ftimestamp) {
		this.ftimestamp = ftimestamp;
	}




	/**
	 * @return the fclob
	 */
	public String getFclob() {
		return fclob;
	}




	/**
	 * @param fclob the fclob to set
	 */
	public void setFclob(String fclob) {
		this.fclob = fclob;
	}




	/**
	 * @return the flongvarchar
	 */
	public String getFlongvarchar() {
		return flongvarchar;
	}




	/**
	 * @param flongvarchar the flongvarchar to set
	 */
	public void setFlongvarchar(String flongvarchar) {
		this.flongvarchar = flongvarchar;
	}




	/**
	 * @return the fblob
	 */
	public byte[] getFblob() {
		return fblob;
	}




	/**
	 * @param fblob the fblob to set
	 */
	public void setFblob(byte[] fblob) {
		this.fblob = fblob;
	}




	/**
	 * @return the fbinary
	 */
	public byte[] getFbinary() {
		return fbinary;
	}




	/**
	 * @param fbinary the fbinary to set
	 */
	public void setFbinary(byte[] fbinary) {
		this.fbinary = fbinary;
	}




	/**
	 * @return the fvarbinary
	 */
	public byte[] getFvarbinary() {
		return fvarbinary;
	}




	/**
	 * @param fvarbinary the fvarbinary to set
	 */
	public void setFvarbinary(byte[] fvarbinary) {
		this.fvarbinary = fvarbinary;
	}




	/**
	 * @return the flongvarbinary
	 */
	public byte[] getFlongvarbinary() {
		return flongvarbinary;
	}




	/**
	 * @param flongvarbinary the flongvarbinary to set
	 */
	public void setFlongvarbinary(byte[] flongvarbinary) {
		this.flongvarbinary = flongvarbinary;
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
	 * @return the intList
	 */
	public List<Integer> getIntList() {
		return intList;
	}




	/**
	 * @param intList the intList to set
	 */
	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}




	/**
	 * @return the strArray
	 */
	public String[] getStrArray() {
		return strArray;
	}




	/**
	 * @param strArray the strArray to set
	 */
	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}




	/**
	 * @return the strList
	 */
	public List<String> getStrList() {
		return strList;
	}




	/**
	 * @param strList the strList to set
	 */
	public void setStrList(List<String> strList) {
		this.strList = strList;
	}




	/**
	 * @return the orderCol
	 */
	public String getOrderCol() {
		return orderCol;
	}




	/**
	 * @param orderCol the orderCol to set
	 */
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}




	/**
	 * @return the orderDir
	 */
	public String getOrderDir() {
		return orderDir;
	}




	/**
	 * @param orderDir the orderDir to set
	 */
	public void setOrderDir(String orderDir) {
		this.orderDir = orderDir;
	}




	/**
	 * @return the a
	 */
	public TestBean getA() {
		return a;
	}




	/**
	 * @param a the a to set
	 */
	public void setA(TestBean a) {
		this.a = a;
	}




	//-----------------------------------------------------------
	/**
	 * @return true if id > 0
	 */
	public boolean isValidId() {
		return id > 0;
	}
	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("id", id)
				.append("fbit", fbit)
				.append("fbool", fbool)
				.append("fchar", fchar)
				.append("fstr", fstr)
				.append("ftinyint", ftinyint)
				.append("fsmallint", fsmallint)
				.append("finteger", finteger)
				.append("fbigint", fbigint)
				.append("freal", freal)
				.append("ffloat", ffloat)
				.append("fdouble", fdouble)
				.append("fdecimal", fdecimal)
				.append("fnumeric", fnumeric)
				.append("fdate", fdate)
				.append("ftime", ftime)
				.append("ftimestamp", ftimestamp)
				.append("fclob", fclob)
				.append("flongvarchar", flongvarchar)
				.append("fblob", fblob)
				.append("fbinary", fbinary)
				.append("fvarbinary", fvarbinary)
				.append("flongvarbinary", flongvarbinary)
				.append("intArray", intArray)
				.append("intList", intList)
				.append("strArray", strArray)
				.append("strList", strList)
				.append("orderCol", orderCol)
				.append("orderDir", orderDir)
				.append("a", a)
				.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final TestBean rhs = (TestBean) obj;
		return Objects.equalsBuilder()
				.append(id, rhs.id)
				.append(fbit, rhs.fbit)
				.append(fbool, rhs.fbool)
				.append(fchar, rhs.fchar)
				.append(fstr, rhs.fstr)
				.append(ftinyint, rhs.ftinyint)
				.append(fsmallint, rhs.fsmallint)
				.append(finteger, rhs.finteger)
				.append(fbigint, rhs.fbigint)
				.append(freal, rhs.freal)
				.append(ffloat, rhs.ffloat)
				.append(fdouble, rhs.fdouble)
				.append(fdecimal, rhs.fdecimal)
				.append(fnumeric, rhs.fnumeric)
				.append(fdate, rhs.fdate)
				.append(ftime, rhs.ftime)
				.append(ftimestamp, rhs.ftimestamp)
				.append(fclob, rhs.fclob)
				.append(flongvarchar, rhs.flongvarchar)
				.append(fblob, rhs.fblob)
				.append(fbinary, rhs.fbinary)
				.append(fvarbinary, rhs.fvarbinary)
				.append(flongvarbinary, rhs.flongvarbinary)
				.append(intArray, rhs.intArray)
				.append(intList, rhs.intList)
				.append(strArray, rhs.strArray)
				.append(strList, rhs.strList)
				.append(orderCol, rhs.orderCol)
				.append(orderDir, rhs.orderDir)
				.append(a, rhs.a)
				.isEquals();
	}

	public TestBean clone() {
		TestBean a = new TestBean();
		
		a.id = id;
		a.fbit = fbit;
		a.fbool = fbool;
		a.fchar = fchar;
		a.fstr = fstr;
		a.ftinyint = ftinyint;
		a.fsmallint = fsmallint;
		a.finteger = finteger;
		a.fbigint = fbigint;
		a.freal = freal;
		a.ffloat = ffloat;
		a.fdouble = fdouble;
		a.fdecimal = fdecimal;
		a.fnumeric = fnumeric;
		a.fdate = fdate;
		a.ftime = ftime;
		a.ftimestamp = ftimestamp;
		a.fclob = fclob;
		a.flongvarchar = flongvarchar;
		a.fblob = fblob;
		a.fbinary = fbinary;
		a.fvarbinary = fvarbinary;
		a.flongvarbinary = flongvarbinary;
		a.intArray = intArray;
		a.intList = intList;
		a.strArray = strArray;
		a.strList = strList;
		a.orderCol = orderCol;
		a.orderDir = orderDir;
		a.a = this.a;

		return a;
	}
}
