package panda.doc.markdown;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import panda.doc.markdown.html.HtmlProcessor;

@RunWith(value = Parameterized.class)
public class MarkupFileTest {
	public static class TestResultPair {
		private String name;
		private String test;
		private String result;

		public TestResultPair(String name, String test, String result) {
			this.name = name;
			this.test = test;
			this.result = result;
		}

		public String getTest() {
			return test;
		}

		public String getResult() {
			return result;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private final static String[] TEST_FILENAMES = new String[] { "dingus.txt", "paragraphs.txt", "snippets.txt",
			"lists.txt" };

	TestResultPair pair;

	@Parameters(name="{index}: {0}/{1}")
	public static Collection<Object[]> testResultPairs() throws IOException {
		List<Object[]> fullResultPairList = new ArrayList<Object[]>();
		for (String filename : TEST_FILENAMES) {
			addTestResultPairList(fullResultPairList, filename);
		}

		return fullResultPairList;
	}

	public MarkupFileTest(String file, String id, String test, String result) {
		this.pair = new TestResultPair(id, test, result);
	}

	public static void addTestResultPairList(List<Object[]> list, String filename) throws IOException {
		URL fileUrl = MarkupFileTest.class.getResource("markup/" + filename);
		FileReader file = new FileReader(fileUrl.getFile());
		BufferedReader in = new BufferedReader(file);
		StringBuffer test = null;
		StringBuffer result = null;

		Pattern pTest = Pattern.compile("# Test (\\w+) \\((.*)\\)");
		Pattern pResult = Pattern.compile("# Result (\\w+)");
		String line;
		int lineNumber = 0;

		String testNumber = null;
		String testName = null;
		StringBuffer curbuf = null;
		try {
			while ((line = in.readLine()) != null) {
				lineNumber++;
				Matcher mTest = pTest.matcher(line);
				Matcher mResult = pResult.matcher(line);
	
				if (mTest.matches()) { // # Test
					addTestResultPair(list, test, result, testNumber, testName, filename);
					testNumber = mTest.group(1);
					testName = mTest.group(2);
					test = new StringBuffer();
					result = new StringBuffer();
					curbuf = test;
				}
				else if (mResult.matches()) { // # Result
					if (testNumber == null) {
						throw new RuntimeException("Test file has result without a test (line " + lineNumber + ")");
					}
					String resultNumber = mResult.group(1);
					if (!testNumber.equals(resultNumber)) {
						throw new RuntimeException("Result " + resultNumber + " test " + testNumber + " (line "
								+ lineNumber + ")");
					}
	
					curbuf = result;
				}
				else {
					curbuf.append(line);
					curbuf.append("\n");
				}
			}
	
			addTestResultPair(list, test, result, testNumber, testName, filename);
		}
		finally {
			in.close();
		}
	}

	private static void addTestResultPair(List<Object[]> list, StringBuffer testbuf, StringBuffer resultbuf,
			String testNumber, String testName, String fileName) {
		if (testbuf == null || resultbuf == null) {
			return;
		}

		String test = chomp(testbuf.toString());
		String result = chomp(resultbuf.toString());

		String id = testNumber + "(" + testName + ")";

		list.add(new Object[] { fileName, id, test, result });
	}

	private static String chomp(String s) {
		int lastPos = s.length() - 1;
		while (s.charAt(lastPos) == '\n' || s.charAt(lastPos) == '\r') {
			lastPos--;
		}
		return s.substring(0, lastPos + 1);
	}

	HtmlProcessor mk = new HtmlProcessor();
	
	@Test
	public void runTest() throws IOException {
		assertEquals(pair.toString(), pair.getResult().trim(), mk.process(pair.getTest()).trim());
	}
}
