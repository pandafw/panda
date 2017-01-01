package panda.mvc.processor;

import panda.io.SizeLimitExceededException;
import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.ParamAdaptor;
import panda.mvc.View;
import panda.mvc.adaptor.multipart.FileSizeLimitExceededException;
import panda.mvc.view.Views;

@IocBean
public class AdaptProcessor extends AbstractProcessor {
	public static final String MULTIPART_BODY_SIZE_EXCEEDED_MSGID = "multipart-body-size-exceeded";
	public static final String MULTIPART_BODY_SIZE_EXCEEDED_DEFAULT = "The request was rejected because its size (${top.displayActualSize}) exceeds the maximum permitted size (${top.displayLimitedSize}).";
	public static final String MULTIPART_FILE_SIZE_EXCEEDED_MSGID = "multipart-file-size-exceeded";
	public static final String MULTIPART_FILE_SIZE_EXCEEDED_DEFAULT = "The request was rejected because the size (${top.displayActualSize}) of file (${top.fileName}) exceeds it's maximum permitted size (${top.displayLimitedSize}).";
	
	@Override
	public void process(ActionContext ac) {
		View view = Views.evalView(ac.getIoc(), ac.getInfo().getErrorView());

		if (view == null) {
			adapt(ac);
			doNext(ac);
			return;
		}
		
		try {
			adapt(ac);
		}
		catch (RuntimeException e) {
			Throwable c = e.getCause();
			if (c instanceof FileSizeLimitExceededException) {
				String msg = ac.getText().getText(MULTIPART_FILE_SIZE_EXCEEDED_MSGID, MULTIPART_FILE_SIZE_EXCEEDED_DEFAULT, c);
				ac.getParamAlert().addError(((FileSizeLimitExceededException)c).getFieldName(), msg);
				view.render(ac);
				return;
			}
			
			if (c instanceof SizeLimitExceededException) {
				String msg = ac.getText().getText(MULTIPART_BODY_SIZE_EXCEEDED_MSGID, MULTIPART_BODY_SIZE_EXCEEDED_DEFAULT, c);
				ac.getActionAlert().addError(msg);
				view.render(ac);
				return;
			}
			
			throw e;
		}
		doNext(ac);
	}
	
	private void adapt(ActionContext ac) {
		ParamAdaptor adaptor = null;
		
		if (ac.getAdaptorType() != null) {
			adaptor = Mvcs.born(ac.getIoc(), ac.getAdaptorType());
		}
		if (adaptor == null) {
			adaptor = ac.getIoc().get(ParamAdaptor.class);
		}

		adaptor.adapt(ac);
	}
}
