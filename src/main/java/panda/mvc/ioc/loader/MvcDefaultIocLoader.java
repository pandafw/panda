package panda.mvc.ioc.loader;

import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.mvc.aware.ActionAwareSupport;
import panda.mvc.aware.ApplicationAwareSupport;
import panda.mvc.aware.ParamAwareSupport;
import panda.mvc.aware.SessionAwareSupport;
import panda.mvc.impl.DefaultActionChainMaker;
import panda.mvc.impl.DefaultUrlMapping;
import panda.mvc.processor.AdaptProcessor;
import panda.mvc.processor.FatalProcessor;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.processor.LocaleProcessor;
import panda.mvc.processor.InvokeProcessor;
import panda.mvc.processor.ValidateProcessor;
import panda.mvc.processor.ViewProcessor;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.DefaultTextProvider;
import panda.mvc.validation.DefaultValidators;
import panda.mvc.validation.validator.BinaryValidator;
import panda.mvc.validation.validator.CastErrorValidator;
import panda.mvc.validation.validator.ConstantValidator;
import panda.mvc.validation.validator.CreditCardNoValidator;
import panda.mvc.validation.validator.DateValidator;
import panda.mvc.validation.validator.DecimalValidator;
import panda.mvc.validation.validator.ElValidator;
import panda.mvc.validation.validator.EmailValidator;
import panda.mvc.validation.validator.EmptyValidator;
import panda.mvc.validation.validator.FileValidator;
import panda.mvc.validation.validator.FilenameValidator;
import panda.mvc.validation.validator.ImageValidator;
import panda.mvc.validation.validator.NumberValidator;
import panda.mvc.validation.validator.ProhibitedValidator;
import panda.mvc.validation.validator.RegexValidator;
import panda.mvc.validation.validator.RequiredValidator;
import panda.mvc.validation.validator.StringValidator;
import panda.mvc.validation.validator.VisitValidator;
import panda.mvc.view.ftl.MvcFreemarkerHelper;
import panda.mvc.view.ftl.MvcFreemarkerManager;
import panda.mvc.view.ftl.MvcFreemarkerTemplateLoader;
import panda.mvc.view.tag.CBoolean;
import panda.mvc.view.tag.CDate;
import panda.mvc.view.tag.CLog;
import panda.mvc.view.tag.CNumber;
import panda.mvc.view.tag.CUrl;
import panda.mvc.view.tag.Csv;
import panda.mvc.view.tag.Head;
import panda.mvc.view.tag.Param;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.Text;
import panda.mvc.view.tag.ui.ActionConfirm;
import panda.mvc.view.tag.ui.ActionError;
import panda.mvc.view.tag.ui.ActionMessage;
import panda.mvc.view.tag.ui.ActionWarning;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.Button;
import panda.mvc.view.tag.ui.Checkbox;
import panda.mvc.view.tag.ui.CheckboxList;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.FieldError;
import panda.mvc.view.tag.ui.File;
import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.Hidden;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.OptGroup;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.Password;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.Reset;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.Submit;
import panda.mvc.view.tag.ui.TextArea;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.ViewField;
import panda.mvc.view.tag.ui.theme.ThemeRenderEngine;
import panda.mvc.view.taglib.TagLibraryManager;

public class MvcDefaultIocLoader extends MvcAnnotationIocLoader {
	protected static final Object[] DEFAULTS = {
		DefaultUrlMapping.class,
		DefaultActionChainMaker.class,
		
		// file pool used by Upload
		LocalFilePool.class,
		
		// utility
		ResourceBundleLoader.class,
		CookieStateProvider.class,
		DefaultTextProvider.class,

		// processor
		AdaptProcessor.class,
		FatalProcessor.class,
		LayoutProcessor.class,
		LocaleProcessor.class,
		InvokeProcessor.class,
		ValidateProcessor.class,
		ViewProcessor.class,
		
		// validation
		ActionAwareSupport.class,
		ParamAwareSupport.class,
		SessionAwareSupport.class,
		ApplicationAwareSupport.class,
		
		// validator
		DefaultValidators.class,
		
		// validators
		BinaryValidator.class,
		CastErrorValidator.class,
		ConstantValidator.class,
		CreditCardNoValidator.class,
		DateValidator.class,
		DecimalValidator.class,
		ElValidator.class,
		EmailValidator.class,
		EmptyValidator.class,
		FilenameValidator.class,
		FileValidator.class,
		ImageValidator.class,
		NumberValidator.class,
		ProhibitedValidator.class,
		RegexValidator.class,
		RequiredValidator.class,
		StringValidator.class,
		VisitValidator.class,
		
		// View
		ThemeRenderEngine.class,

		// Tags
		CBoolean.class,
		CDate.class,
		CLog.class,
		CNumber.class,
		Csv.class,
		CUrl.class,
		Head.class,
		Param.class,
		Property.class,
		Text.class,
		
		// UI Tags
		ActionConfirm.class,
		ActionError.class,
		ActionMessage.class,
		ActionWarning.class,
		Anchor.class,
		Button.class,
		Checkbox.class,
		CheckboxList.class,
		DatePicker.class,
		DateTimePicker.class,
		Div.class,
		FieldError.class,
		File.class,
		Form.class,
		Hidden.class,
		Link.class,
		ListView.class,
		OptGroup.class,
		Pager.class,
		Password.class,
		Radio.class,
		Reset.class,
		Select.class,
		Submit.class,
		TextArea.class,
		TextField.class,
		TimePicker.class,
		TriggerField.class,
		Uploader.class,
		ViewField.class,

		// taglibs
		TagLibraryManager.class,
		
		// Freemarker
		MvcFreemarkerTemplateLoader.class,
		MvcFreemarkerManager.class,
		MvcFreemarkerHelper.class
	};
	
	public MvcDefaultIocLoader() {
		super(DEFAULTS);
	}
}
