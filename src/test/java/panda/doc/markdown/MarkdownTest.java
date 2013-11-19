package panda.doc.markdown;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import panda.doc.markdown.Processor;

@RunWith(value = Parameterized.class)
public class MarkdownTest {

	private final static String MARKDOWN_TEST_DIR = "html";

	String test;
	String dir;

	@SuppressWarnings("unchecked")
	@Parameters(name="{index}: {1}")
	public static Collection<Object[]> markdownTests() {
		List list = new ArrayList<Object[]>();
		URL fileUrl = MarkdownTest.class.getResource(MARKDOWN_TEST_DIR);
		File dir = new File(fileUrl.getFile());
		File[] dirEntries = dir.listFiles();

		for (int i = 0; i < dirEntries.length; i++) {
			File dirEntry = dirEntries[i];
			String fileName = dirEntry.getName();
			if (fileName.endsWith(".text")) {
				String testName = fileName.substring(0, fileName.lastIndexOf('.'));
				list.add(new Object[] { MARKDOWN_TEST_DIR, testName });
			}
		}

		return list;
	}

	public MarkdownTest(String dir, String test) {
		this.test = test;
		this.dir = dir;
	}

	@Test
	public void runTest() throws IOException {
		String testText = slurp(dir + File.separator + test + ".text");
		String htmlText = slurp(dir + File.separator + test + ".html");
		String markdownText = Processor.process(testText);
		assertEquals(test, htmlText.trim(), markdownText.trim());
	}

	private String slurp(String fileName) throws IOException {
		URL fileUrl = this.getClass().getResource(fileName);
		File file = new File(URLDecoder.decode(fileUrl.getFile(), "UTF-8"));
		FileReader in = new FileReader(file);
		StringBuffer sb = new StringBuffer();
		int ch;
		while ((ch = in.read()) != -1) {
			sb.append((char)ch);
		}
		in.close();
		return sb.toString();
	}
}
