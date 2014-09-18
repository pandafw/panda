package panda.bind.xmlrpc;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import panda.bind.xmlrpc.XmlRpcDeserializer.SaxHandler;
import panda.bind.xmlrpc.XmlRpcDeserializer.XomHandler;
import panda.lang.Exceptions;
import panda.lang.time.DateTimes;

/**
 */
public class XmlRpcSaxHandlerTest {
	public static class TestXomHandler extends XomHandler {

		private StringBuilder out = new StringBuilder();
		private List<String> tags = new ArrayList<String>();
		
		public TestXomHandler() {
			super(null, null);
		}

		@Override
		public void startElement(String name) {
			tags.add(name);
			out.append("<").append(name).append(">");
		}

		@Override
		public void endElement(Object value) {
			if (value instanceof Date) {
				value = DateTimes.datetimeFormat().format((Date)value);
			}
			else if (value instanceof byte[]) {
				value = new String((byte[])value);
			}
			if (value != null) {
				out.append(value.toString());
			}
			out.append("</").append(tags.remove(tags.size() - 1)).append(">");
		}
		
		public String text() {
			return out.toString();
		}
	}

	protected void convert(String src, String exp) {
		TestXomHandler txh = new TestXomHandler();
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();

			SaxHandler handler = new SaxHandler(txh);
			xr.setContentHandler(handler);

			xr.parse(new InputSource(new StringReader(src)));
		}
		catch (Exception e) {
			System.err.println(txh.text());
			throw Exceptions.wrapThrow(e);
		}
		
		String actual = txh.text();
		Assert.assertEquals(exp, actual);
	}
	
	@Test
	public void testFault() {
		String src = "<methodResponse>"
				+ "  <fault>                                                        "
				+ "    <value>                                                      "
				+ "      <struct>                                                   "
				+ "        <member>                                                 "
				+ "          <name>faultCode</name>                                 "
				+ "          <value><int>4</int></value>                            "
				+ "        </member>                                                "
				+ "        <member>                                                 "
				+ "          <name>faultString</name>                               "
				+ "          <value><string>error</string></value>   "
				+ "        </member>                                                "
				+ "      </struct>                                                  "
				+ "    </value>                                                     "
				+ "  </fault>                                                       "
				+ "</methodResponse>";

		String exp = "<methodResponse><fault><faultCode>4</faultCode><faultString>error</faultString></fault></methodResponse>";
		
		convert(src, exp);
	}

	
	@Test
	public void testParam1() {
		String src = "<methodCall>"
				+ "  <methodName>call</methodName>"
				+ "  <params>                                      "
				+ "    <param>                                     "
				+ "        <value><i4>40</i4></value>              "
				+ "    </param>                                    "
				+ "  </params>                                     "
				+ "</methodCall>                                   ";

		String exp = "<methodCall><methodName>call</methodName><params><param>40</param></params></methodCall>";
		
		convert(src, exp);
	}
	
	@Test
	public void testParam2() {
		String src = "<methodCall>"
				+ "  <methodName>call</methodName>"
				+ "  <params>                                      "
				+ "    <param>                                     "
				+ "        <value><i4>40</i4></value>              "
				+ "    </param>                                    "
				+ "    <param>                                     "
				+ "        <value>str</value>              "
				+ "    </param>                                    "
				+ "  </params>                                     "
				+ "</methodCall>                                   ";

		String exp = "<methodCall><methodName>call</methodName><params><param>40</param><param>str</param></params></methodCall>";
		
		convert(src, exp);
	}

	
	@Test
	public void testParam3() {
		String src = "<methodCall>"
				+ "  <methodName>call</methodName>"
				+ "  <params>                                      "
				+ "    <param>                                     "
				+ "        <value><i4>40</i4></value>              "
				+ "    </param>                                    "
				+ "    <param>                                     "
				+ "        <value>"
				+ "          <array><data>"
				+ "            <value><int>11</int></value>"
				+ "            <value><string>12</string></value>"
				+ "            <value><boolean>1</boolean></value>"
				+ "            <value><double>-12.53</double></value>"
				+ "            <value><dateTime.iso8601>19980717T14:08:55</dateTime.iso8601></value>"
				+ "            <value><base64>eW91IGNhbid0IHJlYWQgdGhpcyE=</base64></value>"
				+ "          </data></array>"
				+ "        </value>              "
				+ "    </param>                                    "
				+ "  </params>                                     "
				+ "</methodCall>                                   ";

		String exp = "<methodCall><methodName>call</methodName><params><param>40</param>"
				+ "<param>"
				+ "<data>11</data>"
				+ "<data>12</data>"
				+ "<data>true</data>"
				+ "<data>-12.53</data>"
				+ "<data>1998-07-17 14:08:55</data>"
				+ "<data>you can't read this!</data>"
				+ "</param>"
				+ "</params></methodCall>";
		
		convert(src, exp);
	}
	
	@Test
	public void testParam4() {
		String src = "<methodCall>"
				+ "  <methodName>call</methodName>"
				+ "  <params>                                      "
				+ "    <param>                                     "
				+ "        <value><i4>40</i4></value>              "
				+ "    </param>                                    "
				+ "    <param>                                     "
				+ "        <value>"
				+ "          <array><data>"
				+ "            <value><int>11</int></value>"
				+ "            <value><struct>"
				+ "              <member><name>m1</name><value>s1</value></member>"
				+ "              <member><name>m2</name><value>" 
				+ "                <array><data><value>1</value><value>2</value></data></array>"
				+ "              </value></member>"
				+ "              <member><name>m3</name><value>" 
				+ "                <struct><member><name>n1</name><value>n1-v</value></member></struct>"
				+ "              </value></member>"
				+ "            </struct></value>"
				+ "          </data></array>"
				+ "        </value>              "
				+ "    </param>                                    "
				+ "  </params>                                     "
				+ "</methodCall>                                   ";

		String exp = "<methodCall><methodName>call</methodName><params><param>40</param>"
				+ "<param>"
				+ "<data>11</data>"
				+ "<data>"
					+ "<m1>s1</m1>"
					+ "<m2><data>1</data><data>2</data></m2>"
					+ "<m3><n1>n1-v</n1></m3>"
				+ "</data>"
				+ "</param>"
				+ "</params></methodCall>";
		
		convert(src, exp);
	}

}
