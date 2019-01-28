package panda.mvc.ioc.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.lang.Collections;
import panda.mvc.MvcConfig;
import panda.mvc.adaptor.DefaultParamAdaptor;
import panda.mvc.adaptor.ejector.FormParamEjector;
import panda.mvc.adaptor.ejector.JsonParamEjector;
import panda.mvc.adaptor.ejector.MultiPartParamEjector;
import panda.mvc.adaptor.ejector.StreamParamEjector;
import panda.mvc.adaptor.ejector.XmlParamEjector;
import panda.mvc.alert.ActionAlertSupport;
import panda.mvc.alert.ParamAlertSupport;
import panda.mvc.annotation.Modules;
import panda.mvc.filter.DecodingFilter;
import panda.mvc.filter.DispatchFilter;
import panda.mvc.filter.HttpDumpFilter;
import panda.mvc.filter.LoggingFilter;
import panda.mvc.impl.DefaultActionChainCreator;
import panda.mvc.impl.DefaultServletChain;
import panda.mvc.impl.DefaultValidateHandler;
import panda.mvc.impl.DefaultValidatorCreator;
import panda.mvc.impl.DefaultViewCreator;
import panda.mvc.impl.RegexActionMapping;
import panda.mvc.processor.AdaptProcessor;
import panda.mvc.processor.FatalProcessor;
import panda.mvc.processor.InvokeProcessor;
import panda.mvc.processor.LayoutProcessor;
import panda.mvc.processor.LocaleProcessor;
import panda.mvc.processor.PrepareProcessor;
import panda.mvc.processor.RedirectProcessor;
import panda.mvc.processor.TokenProcessor;
import panda.mvc.processor.ValidateProcessor;
import panda.mvc.processor.ViewProcessor;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.ActionConsts;
import panda.mvc.util.ActionTextProvider;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.MvcCryptor;
import panda.mvc.util.MvcResourceLoader;
import panda.mvc.util.MvcSettings;
import panda.mvc.util.MvcURLBuilder;
import panda.mvc.validator.BinaryValidator;
import panda.mvc.validator.CIDRValidator;
import panda.mvc.validator.CastErrorValidator;
import panda.mvc.validator.ConstantValidator;
import panda.mvc.validator.CreditCardNoValidator;
import panda.mvc.validator.DateValidator;
import panda.mvc.validator.DecimalValidator;
import panda.mvc.validator.ElValidator;
import panda.mvc.validator.EmailValidator;
import panda.mvc.validator.EmptyValidator;
import panda.mvc.validator.FileValidator;
import panda.mvc.validator.FilenameValidator;
import panda.mvc.validator.ImageValidator;
import panda.mvc.validator.ImailValidator;
import panda.mvc.validator.NumberValidator;
import panda.mvc.validator.ProhibitedValidator;
import panda.mvc.validator.RegexValidator;
import panda.mvc.validator.RequiredValidator;
import panda.mvc.validator.StringValidator;
import panda.mvc.validator.URLValidator;
import panda.mvc.validator.VisitValidator;
import panda.mvc.vfs.MvcLocalFileStore;
import panda.mvc.view.AltView;
import panda.mvc.view.CsvView;
import panda.mvc.view.DataView;
import panda.mvc.view.ForwardView;
import panda.mvc.view.FreemarkerView;
import panda.mvc.view.HttpStatusView;
import panda.mvc.view.JsonView;
import panda.mvc.view.JspView;
import panda.mvc.view.RedirectView;
import panda.mvc.view.ResourceView;
import panda.mvc.view.ServletErrorView;
import panda.mvc.view.SitemeshForwardView;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.mvc.view.SitemeshJsonView;
import panda.mvc.view.SitemeshJspView;
import panda.mvc.view.SitemeshResourceView;
import panda.mvc.view.SitemeshXmlView;
import panda.mvc.view.TsvView;
import panda.mvc.view.VoidView;
import panda.mvc.view.XlsView;
import panda.mvc.view.XlsxView;
import panda.mvc.view.XmlView;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.mvc.view.ftl.FreemarkerManager;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.mvc.view.sitemesh.FreemarkerSitemesher;
import panda.mvc.view.sitemesh.SitemeshManager;
import panda.mvc.view.tag.CBoolean;
import panda.mvc.view.tag.CDate;
import panda.mvc.view.tag.CLog;
import panda.mvc.view.tag.CNumber;
import panda.mvc.view.tag.CSet;
import panda.mvc.view.tag.CUrl;
import panda.mvc.view.tag.Csv;
import panda.mvc.view.tag.Head;
import panda.mvc.view.tag.Param;
import panda.mvc.view.tag.Property;
import panda.mvc.view.tag.Text;
import panda.mvc.view.tag.Tsv;
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
import panda.mvc.view.tag.ui.HtmlEditor;
import panda.mvc.view.tag.ui.Icon;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.ListView;
import panda.mvc.view.tag.ui.OptGroup;
import panda.mvc.view.tag.ui.Pager;
import panda.mvc.view.tag.ui.Password;
import panda.mvc.view.tag.ui.Queryer;
import panda.mvc.view.tag.ui.Radio;
import panda.mvc.view.tag.ui.Reset;
import panda.mvc.view.tag.ui.Select;
import panda.mvc.view.tag.ui.Submit;
import panda.mvc.view.tag.ui.TextArea;
import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.Token;
import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.Uploader;
import panda.mvc.view.tag.ui.ViewField;
import panda.mvc.view.tag.ui.theme.ThemeRenderEngine;
import panda.mvc.view.taglib.TagLibraryManager;
import panda.mvc.view.util.CsvExporter;
import panda.mvc.view.util.HttpRangeDownloader;
import panda.mvc.view.util.TsvExporter;
import panda.mvc.view.util.XlsExporter;
import panda.mvc.view.util.XlsxExporter;

public class MvcDefaultIocLoader extends MvcAnnotationIocLoader {
	
	public MvcDefaultIocLoader(MvcConfig config) {
		super();
		init(getDefaults(config));
	}
	
	protected List<Object> getDefaults(MvcConfig config) {
		List<Object> ss = new ArrayList<Object>();
		
		addDefaults(ss);
		addModules(config, ss);
		return ss;
	}

	protected void addModules(MvcConfig config, List<Object> pkgs) {
		Class<?> mm = config.getMainModule();

		Modules ms = mm.getAnnotation(Modules.class);
		if (ms == null) {
			pkgs.add(mm);
			return;
		}
		
		for (String s : ms.packages()) {
			pkgs.add(s);
		}
		
		for (Class<?> c : ms.value()) {
			pkgs.add(c);
		}

		if (ms.scan()) {
			pkgs.add(mm.getPackage().getName());
		}
		else {
			pkgs.add(mm);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void addDefaults(Collection<Object> ss) {
		Collections.addAll(ss,
			RegexActionMapping.class,
			DefaultActionChainCreator.class,
			
			// file pool used by Upload
			MvcLocalFileStore.class,
			
			// utilities
			ActionAssist.class,
			ActionConsts.class,
			CookieStateProvider.class,
			ActionTextProvider.class,
			MvcResourceLoader.class,
			MvcSettings.class,
			MvcCryptor.class,
			MvcURLBuilder.class,
	
			// filter
			DefaultServletChain.class,
			DecodingFilter.class,
			HttpDumpFilter.class,
			LoggingFilter.class,
			DispatchFilter.class,

			// processor
			AdaptProcessor.class,
			FatalProcessor.class,
			InvokeProcessor.class,
			LayoutProcessor.class,
			LocaleProcessor.class,
			PrepareProcessor.class,
			RedirectProcessor.class,
			TokenProcessor.class,
			ValidateProcessor.class,
			ViewProcessor.class,

			// adaptor
			DefaultParamAdaptor.class,
			
			// ejectors
			FormParamEjector.class,
			JsonParamEjector.class,
			MultiPartParamEjector.class,
			StreamParamEjector.class,
			XmlParamEjector.class,
			
			// alerts
			ActionAlertSupport.class,
			ParamAlertSupport.class,
			
			// validator
			DefaultValidateHandler.class,
			DefaultValidatorCreator.class,
			
			// validators
			BinaryValidator.class,
			CIDRValidator.class,
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
			ImailValidator.class,
			NumberValidator.class,
			ProhibitedValidator.class,
			RegexValidator.class,
			RequiredValidator.class,
			StringValidator.class,
			URLValidator.class,
			VisitValidator.class,
			
			// ViewCreator
			DefaultViewCreator.class,

			// Views
			AltView.class,
			CsvView.class,
			DataView.class,
			ForwardView.class,
			FreemarkerView.class,
			HttpStatusView.class,
			JsonView.class,
			JspView.class,
			RedirectView.class,
			ResourceView.class,
			ServletErrorView.class,
			TsvView.class,
			XlsView.class,
			XlsxView.class,
			XmlView.class,
			SitemeshForwardView.class,
			SitemeshFreemarkerView.class,
			SitemeshJsonView.class,
			SitemeshJspView.class,
			SitemeshResourceView.class,
			SitemeshXmlView.class,
			VoidView.class,
			
			// View Theme
			ThemeRenderEngine.class,

			// View Utils
			CsvExporter.class,
			TsvExporter.class,
			XlsExporter.class,
			XlsxExporter.class,
			HttpRangeDownloader.class,
			
			// Taglibs
			TagLibraryManager.class,
	
			// Tags
			CBoolean.class,
			CDate.class,
			CLog.class,
			CNumber.class,
			CSet.class,
			Csv.class,
			Tsv.class,
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
			HtmlEditor.class,
			Icon.class,
			Link.class,
			ListView.class,
			OptGroup.class,
			Pager.class,
			Password.class,
			Queryer.class,
			Radio.class,
			Reset.class,
			Select.class,
			Submit.class,
			TextArea.class,
			TextField.class,
			TimePicker.class,
			Token.class,
			TriggerField.class,
			Uploader.class,
			ViewField.class,
			
			// Freemarker
			FreemarkerTemplateLoader.class,
			FreemarkerManager.class,
			FreemarkerHelper.class,
			
			// Sitemesh
			SitemeshManager.class,
			FreemarkerSitemesher.class
		);
	};
}
