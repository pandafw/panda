package panda.tool.poi.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import panda.codec.binary.Hex;
import panda.lang.Strings;

/**
 * 
 */
public abstract class DocTextProcessor {
	protected static final byte BEL_MARK = 0x07;

	protected static final byte FIELD_BEGIN_MARK = 0x13;

	protected static final byte FIELD_SEPARATOR_MARK = 0x14;

	protected static final byte FIELD_END_MARK = 0x15;

	protected static final Pattern PATTERN_HYPERLINK = 
			Pattern.compile( "^[ \\t\\r\\n]*HYPERLINK .*$" );

	protected static final Pattern PATTERN_HYPERLINK_EXTERNAL = 
			Pattern.compile( "^[ \\t\\r\\n]*HYPERLINK \"(.*)\".*$" );

	protected static final Pattern PATTERN_HYPERLINK_LOCAL = 
			Pattern.compile( "^[ \\t\\r\\n]*HYPERLINK \\\\l \"(.*)\"[ ](.*)$" );

	protected static final Pattern PATTERN_PAGEREF = 
			Pattern.compile( "^[ \\t\\r\\n]*PAGEREF ([^ ]*)[ \\t\\r\\n]*\\\\h.*$" );

	protected static final char UNICODECHAR_NONBREAKING_HYPHEN = '\u2011';

	protected static final char UNICODECHAR_ZERO_WIDTH_SPACE = '\u200b';

	/**
	 * Constructor
	 */
	public DocTextProcessor() {
	}

	protected abstract void handleDocument(HWPFDocument doc);
	protected abstract boolean handleRange(String name, HWPFDocument doc, Range range);
	protected abstract boolean handleParagraph(Range range, Paragraph paragraph, int index);
	protected abstract boolean handleText(Range range, Paragraph paragraph, CharacterRun crun, int index, ECharRun ecrun);
	
	protected void process(HWPFDocument doc) {
		handleDocument(doc);
		
		processRange("main", doc, doc.getRange());
		processRange("comments", doc, doc.getCommentsRange());
		processRange("footnote", doc, doc.getFootnoteRange());
		processRange("endnote", doc, doc.getEndnoteRange());
		processRange("header", doc, doc.getHeaderStoryRange());
		processRange("textbox", doc, doc.getMainTextboxRange());
	}

	protected void processRange(String name, HWPFDocument doc, Range range) {
		if (range == null) {
			return;
		}
		
		if (!handleRange(name, doc, range)) {
			return;
		}

		// Get the number of Paragraph(s) in the overall range and iterate
		// through them
		int numParagraphs = range.numParagraphs();
		for (int i = 0; i < numParagraphs; i++) {
			Paragraph paragraph = range.getParagraph(i);
			String text = paragraph.text();

			if (Strings.isPrintable(text)) {
				if (!handleParagraph(range, paragraph, i)) {
					continue;
				}

				int fields = 0;
				List<CharacterRun> cruns = new ArrayList<CharacterRun>();
				
				for (int j = 0; j < paragraph.numCharacterRuns(); j++) {
					CharacterRun crun = paragraph.getCharacterRun(j);
					String ctext = crun.text();
					switch (ctext.charAt(0)) {
					case FIELD_BEGIN_MARK:
						fields++;
						cruns.add(crun);
						continue;
					case FIELD_END_MARK:
						fields--;
						cruns.add(crun);
						if (fields < 1) {
							handleField(range, paragraph, cruns, j + 1 - cruns.size());
							cruns.clear();
						}
						continue;
					}
					
					if (fields > 0) {
						cruns.add(crun);
						continue;
					}
					
					if (handleCharacter(range, paragraph, crun, j, false)) {
						paragraph = range.getParagraph(i);
					}
				}
			}
		}
	}

	protected void handleField(Range range, Paragraph paragraph, List<CharacterRun> cruns, int index) {
		boolean ignore = true;
		for (int i = 0; i < cruns.size(); i++) {
			handleCharacter(range, paragraph, cruns.get(i), index + i, ignore);

			CharacterRun crun = cruns.get(i);
			String ctext = crun.text();
			if (ctext.charAt(0) == FIELD_SEPARATOR_MARK) {
				ignore = false;
			}
		}
	}

	protected boolean handleCharacter(Range range, Paragraph paragraph, CharacterRun crun, int index, boolean ignore) {
		boolean encode = true;
		ECharRun ecr = new ECharRun();
		if (crun.isSpecialCharacter()) {
			ecr.setType(ECharRun.ECR_SPECIAL);
		}
		else if (crun.isObj()) {
			ecr.setType(ECharRun.ECR_OBJECT);
		}
		else if (crun.isOle2()) {
			ecr.setType(ECharRun.ECR_OLE2);
		}
		else {
			encode = false;
			ecr.setType(ignore ? ECharRun.ECR_IGNORE : ECharRun.ECR_TEXT);
		}
		
		String ctext = crun.text();
		if (encode) {
			ctext = Hex.encodeHexString(ctext);
		}
		else {
			if (Strings.isControl(ctext)) {
				ecr.setType(ECharRun.ECR_MARK);
				ctext = Hex.encodeHexString(ctext);
			}
			else {
				ctext = trimText(ctext);
			}
		}
		ecr.setText(ctext);
		
		return handleText(range, paragraph, crun, index, ecr);
	}

	protected String trimText(String text) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);

			if (ch == 30) {
				// Non-breaking hyphens are stored as ASCII 30
				sb.append(UNICODECHAR_NONBREAKING_HYPHEN);
			} 
			else if (ch == 31) {
				// Non-required hyphens to zero-width space
				sb.append(UNICODECHAR_ZERO_WIDTH_SPACE);
			}
			// else if ( charChar >= 0x20 || charChar == 0x09
			// || charChar == 0x0A || charChar == 0x0D )
			else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}
