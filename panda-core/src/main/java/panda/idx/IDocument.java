package panda.idx;

import java.util.Date;
import java.util.List;

public interface IDocument {
	String getId();
	void setId(String id);
	
	String getTextField(String name);
	List<String> getTextFields(String name);
	void addTextField(String name, String text);
	
	Date getDateField(String name);
	List<Date> getDateFields(String name);
	void addDateField(String name, Date date);

	Number getNumberField(String name);
	List<Number> getNumberFields(String name);
	void addNumberField(String name, Number num);

	String getAtomField(String name);
	List<String> getAtomFields(String name);
	void addAtomField(String name, String str);
}
