package panda.bind.xmlrpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Objects;
import panda.lang.builder.EqualsBuilder;
import panda.lang.time.DateTimes;

/**
 */
public class XmlRpcBinderTest {
	public static class A {
		private Object obj = null;
		private boolean bol = false;
		private Integer num = 1;
		private String str = "A";
		private A[] ary;
		private List<A> lst;
		private Map<String, A> map;
		private A aaa;
		private byte[] bin;

		public A() {
		}
		public A(boolean b, String s, Integer n) {
			bol = b;
			str = s;
			num = n;
		}
		
		public A getAaa() {
			return aaa;
		}
		public void setAaa(A aaa) {
			this.aaa = aaa;
		}
		public Object getObj() {
			return obj;
		}
		public void setObj(Object obj) {
			this.obj = obj;
		}
		public boolean isBol() {
			return bol;
		}
		public void setBol(boolean bol) {
			this.bol = bol;
		}
		public Integer getNum() {
			return num;
		}
		public void setNum(Integer num) {
			this.num = num;
		}
		public String getStr() {
			return str;
		}
		public void setStr(String str) {
			this.str = str;
		}
		public A[] getAry() {
			return ary;
		}
		public void setAry(A[] ary) {
			this.ary = ary;
		}
		public List<A> getLst() {
			return lst;
		}
		public void setLst(List<A> lst) {
			this.lst = lst;
		}
		public Map<String, A> getMap() {
			return map;
		}
		public void setMap(Map<String, A> map) {
			this.map = map;
		}
		public byte[] getBin() {
			return bin;
		}
		public void setBin(byte[] bin) {
			this.bin = bin;
		}
		
		@Override
		public String toString() {
			return Objects.toStringBuilder()
					.append("obj", obj)
					.append("bol", bol)
					.append("num", num)
					.append("str", str)
					.append("ary", ary)
					.append("lst", lst)
					.append("map", map)
					.append("aaa", aaa)
					.append("bin", bin)
					.toString();
		}
		
		@Override
		public boolean equals(Object rhs) {
			if (this == rhs) {
				return true;
			}
			if (rhs == null) {
				return false;
			}
			if (getClass() != rhs.getClass()) {
				return false;
			}

			A a = (A)rhs;
			return new EqualsBuilder()
				.append(obj, a.obj)
				.append(bol, a.bol)
				.append(str, a.str)
				.append(num, a.num)
				.append(ary, a.ary)
				.append(lst, a.lst)
				.append(map, a.map)
				.append(aaa, a.aaa)
				.append(bin, a.bin)
				.build();
		}
	}
	
	public static class Xdo extends XmlRpcDocument<Object> {
	}
	
	public static class Xda extends XmlRpcDocument<A> {

		/**
		 * Override is required for GenericType
		 */
		@Override
		public List<A> getParams() {
			return super.getParams();
		}

		/**
		 * Override is required for GenericType
		 */
		@Override
		public void setParams(List<A> params) {
			super.setParams(params);
		}
	}
	
	
	@Test
	public void testFault() throws Exception {
		String xml = "<methodResponse>\n"
				+ "	<fault>\n"
				+ "		<struct>\n"
				+ "			<member>\n"
				+ "				<name>faultCode</name>\n"
				+ "				<value><int>999</int></value>\n"
				+ "			</member>\n"
				+ "			<member>\n"
				+ "				<name>faultString</name>\n"
				+ "				<value><string>FAULT</string></value>\n"
				+ "			</member>\n"
				+ "		</struct>\n"
				+ "	</fault>\n"
				+ "</methodResponse>";

		Xdo obj = new Xdo();
		XmlRpcFault xrf = new XmlRpcFault();
		xrf.setFaultCode(999);
		xrf.setFaultString("FAULT");
		obj.setFault(xrf);
		
		Xdo oa = XmlRpcs.fromXml(xml, Xdo.class);
		Assert.assertEquals(obj, oa);

		String sa = XmlRpcs.toXml(obj, false, true);
//		System.out.print(sa);
		Assert.assertEquals(xml, sa);
	}

	@Test
	public void testParamSimple() throws Exception {
		String xml = "<methodCall>\n"
				+ "	<methodName>call</methodName>\n"
				+ "	<params>\n"
				+ "		<param>\n"
				+ "			<value><int>10</int></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><string>str</string></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><double>12.5</double></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><dateTime.iso8601>20101001T12:00:01</dateTime.iso8601></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><base64>SGVsbG8gV29ybGQh</base64></value>\n"
				+ "		</param>\n"
				+ "	</params>\n"
				+ "</methodCall>";

		Xdo obj = new Xdo();
		obj.setMethodName("call");
		obj.setParams(new ArrayList<Object>());
		obj.getParams().add(10);
		obj.getParams().add("str");
		obj.getParams().add(12.5);
		obj.getParams().add(DateTimes.datetimeFormat().parse("2010-10-01 12:00:01"));
		obj.getParams().add("Hello World!".getBytes());

		Xdo oa = XmlRpcs.fromXml(xml, Xdo.class);
		Assert.assertEquals(obj, oa);

		String sa = XmlRpcs.toXml(obj, true, true);
		Assert.assertEquals(xml, sa);
	}


	@Test
	public void testParamSimpleArray() throws Exception {
		String xml = "<methodCall>\n"
				+ "	<methodName>call</methodName>\n"
				+ "	<params>\n"
				+ "		<param>\n"
				+ "			<value><int>10</int></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><string>str</string></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><double>12.5</double></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value><dateTime.iso8601>20101001T12:00:01</dateTime.iso8601></value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value>\n"
				+ "				<array>\n"
				+ "					<data>\n"
				+ "						<value><int>1</int></value>\n"
				+ "						<value><string>s</string></value>\n"
				+ "						<value><int>3</int></value>\n"
				+ "					</data>\n"
				+ "				</array>\n"
				+ "			</value>\n"
				+ "		</param>\n"
				+ "	</params>\n"
				+ "</methodCall>";

		Xdo obj = new Xdo();
		obj.setMethodName("call");
		obj.setParams(new ArrayList<Object>());
		obj.getParams().add(10);
		obj.getParams().add("str");
		obj.getParams().add(12.5);
		obj.getParams().add(DateTimes.datetimeFormat().parse("2010-10-01 12:00:01"));
		obj.getParams().add(Arrays.asList(new Object[] { 1, "s", 3 }));

		Xdo oa = XmlRpcs.fromXml(xml, Xdo.class);
		Assert.assertEquals(obj, oa);

		String sa = XmlRpcs.toXml(obj, true, true);
//		System.out.print(sa);
		Assert.assertEquals(xml, sa);
	}

	@Test
	public void testParamStruct() throws Exception {
		String xml = "<methodCall>\n"
				+ "	<methodName>call</methodName>\n"
				+ "	<params>\n"
				+ "		<param>\n"
				+ "			<value>\n"
				+ "				<struct>\n"
				+ "					<member>\n"
				+ "						<name>bol</name>\n"
				+ "						<value><boolean>0</boolean></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>num</name>\n"
				+ "						<value><int>11</int></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>str</name>\n"
				+ "						<value><string>a1s1</string></value>\n"
				+ "					</member>\n"
				+ "				</struct>\n"
				+ "			</value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value>\n"
				+ "				<struct>\n"
				+ "					<member>\n"
				+ "						<name>bol</name>\n"
				+ "						<value><boolean>0</boolean></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>num</name>\n"
				+ "						<value><int>22</int></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>str</name>\n"
				+ "						<value><string>a2s2</string></value>\n"
				+ "					</member>\n"
				+ "				</struct>\n"
				+ "			</value>\n"
				+ "		</param>\n"
				+ "	</params>\n"
				+ "</methodCall>";

		Xda obj = new Xda();
		obj.setMethodName("call");
		obj.setParams(new ArrayList<A>());
		
		A a1 = new A();
		a1.setStr("a1s1");
		a1.setNum(11);
		obj.getParams().add(a1);
		
		A a2 = new A();
		a2.setStr("a2s2");
		a2.setNum(22);
		obj.getParams().add(a2);

		Xda oa = XmlRpcs.fromXml(xml, Xda.class);
		Assert.assertEquals(obj, oa);

		String sa = XmlRpcs.toXml(obj, true, true);
//		System.out.print(sa);
		Assert.assertEquals(xml, sa);
	}

	@Test
	public void testParamStructArray() throws Exception {
		String xml = "<methodCall>\n"
				+ "	<methodName>call</methodName>\n"
				+ "	<params>\n"
				+ "		<param>\n"
				+ "			<value>\n"
				+ "				<struct>\n"
				+ "					<member>\n"
				+ "						<name>bol</name>\n"
				+ "						<value><boolean>0</boolean></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>num</name>\n"
				+ "						<value><int>11</int></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>str</name>\n"
				+ "						<value><string>a1s1</string></value>\n"
				+ "					</member>\n"
				+ "				</struct>\n"
				+ "			</value>\n"
				+ "		</param>\n"
				+ "		<param>\n"
				+ "			<value>\n"
				+ "				<struct>\n"
				+ "					<member>\n"
				+ "						<name>ary</name>\n"
				+ "						<value>\n"
				+ "							<array>\n"
				+ "								<data>\n"
				+ "									<value>\n"
				+ "										<struct>\n"
				+ "											<member>\n"
				+ "												<name>bol</name>\n"
				+ "												<value><boolean>0</boolean></value>\n"
				+ "											</member>\n"
				+ "											<member>\n"
				+ "												<name>num</name>\n"
				+ "												<value><int>1</int></value>\n"
				+ "											</member>\n"
				+ "											<member>\n"
				+ "												<name>str</name>\n"
				+ "												<value><string>a21</string></value>\n"
				+ "											</member>\n"
				+ "										</struct>\n"
				+ "									</value>\n"
				+ "									<value>\n"
				+ "										<struct>\n"
				+ "											<member>\n"
				+ "												<name>bol</name>\n"
				+ "												<value><boolean>0</boolean></value>\n"
				+ "											</member>\n"
				+ "											<member>\n"
				+ "												<name>num</name>\n"
				+ "												<value><int>222</int></value>\n"
				+ "											</member>\n"
				+ "											<member>\n"
				+ "												<name>str</name>\n"
				+ "												<value><string>A</string></value>\n"
				+ "											</member>\n"
				+ "										</struct>\n"
				+ "									</value>\n"
				+ "								</data>\n"
				+ "							</array>\n"
				+ "						</value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>bol</name>\n"
				+ "						<value><boolean>0</boolean></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>num</name>\n"
				+ "						<value><int>22</int></value>\n"
				+ "					</member>\n"
				+ "					<member>\n"
				+ "						<name>str</name>\n"
				+ "						<value><string>a2s2</string></value>\n"
				+ "					</member>\n"
				+ "				</struct>\n"
				+ "			</value>\n"
				+ "		</param>\n"
				+ "	</params>\n"
				+ "</methodCall>";

		Xda obj = new Xda();
		obj.setMethodName("call");
		obj.setParams(new ArrayList<A>());
		
		A a1 = new A();
		a1.setStr("a1s1");
		a1.setNum(11);
		obj.getParams().add(a1);
		
		A a2 = new A();
		a2.setStr("a2s2");
		a2.setNum(22);
		
		A a21 = new A();
		a21.setStr("a21");
		A a22 = new A();
		a22.setNum(222);
		
		a2.setAry(new A[] { a21, a22 });

		obj.getParams().add(a2);

		
		Xda oa = XmlRpcs.fromXml(xml, Xda.class);
		Assert.assertEquals(obj, oa);

		String sa = XmlRpcs.toXml(obj, true, true);
//		System.out.print(sa);
		Assert.assertEquals(xml, sa);
	}
}
