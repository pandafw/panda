package panda.tool.poi.ppt;

import org.apache.poi.hslf.model.Comment;
import org.apache.poi.hslf.model.Notes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;


/**
 * 
 */
public abstract class PptTextProcessor {
	/**
	 * Constructor
	 */
	public PptTextProcessor() {
	}

	protected abstract void handleSlideShow(SlideShow ss);
	protected abstract void handleMasterSlide(String key, SlideMaster master);
	protected abstract void handleSlide(String key, Slide slide);
	protected abstract void handleComment(String key, Comment comment);
	protected abstract void handleText(String key, TextRun textRun);
	protected abstract void handleNote(String key, TextRun textRun);
	
	protected void process(SlideShow ss) {
		handleSlideShow(ss);
		
		SlideMaster[] sms = ss.getSlidesMasters();
		for (int i = 0; i < sms.length; i++) {
			SlideMaster sm = sms[i];
			handleMasterSlide(String.valueOf(i), sm);

			TextRun[] texts = sm.getTextRuns();
			if (texts != null) {
				for (int j = 0; j < texts.length; j++) {
					handleText(String.valueOf(j), texts[j]);
				}
			}
			
//			for (Shape sh : sm.getShapes()) {
//				if (sh instanceof TextShape) {
//					if (MasterSheet.isPlaceholder(sh)) {
//						// don't bother about boiler
//						// plate text on master
//						// sheets
//						continue;
//					}
//					TextShape tsh = (TextShape) sh;
//					String text = tsh.getText();
//					ret.append(text);
//					if (!text.endsWith("\n")) {
//						ret.append("\n");
//					}
//				}
//			}
		}
		
		Slide[] sa = ss.getSlides();
		for (int i = 0; i < sa.length; i++) {
			Slide slide = sa[i];
			handleSlide(String.valueOf(i), slide);

			TextRun[] texts = slide.getTextRuns();
			if (texts != null) {
				for (int j = 0; j < texts.length; j++) {
					handleText(String.valueOf(j), texts[j]);
				}
			}

			Comment[] comments = slide.getComments();
			if (comments != null) {
				for (int j = 0; j < comments.length; j++) {
					handleComment(String.valueOf(j), comments[j]);
				}
			}
			
			Notes notes = slide.getNotesSheet();
			if (notes != null) {
				TextRun[] ntexts = notes.getTextRuns();
				if (ntexts != null) {
					for (int j = 0; j < ntexts.length; j++) {
						handleNote(String.valueOf(j), ntexts[j]);
					}
				}
			}
		}
	}
}
