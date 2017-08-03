package panda.idx.gae;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;

import panda.idx.IDocument;

public class GaeDocument implements IDocument {
	protected Document.Builder db;
	protected Document doc;

	public GaeDocument(Locale locale) {
		this(locale, null);
	}

	public GaeDocument(Document doc) {
		this(null, doc);
	}

	public GaeDocument(Locale locale, Document doc) {
		this.doc = doc;
		this.db = Document.newBuilder();
		if (locale != null) {
			this.db.setLocale(locale);
		}
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return doc.getId();
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		db.setId(id);
	}

	/**
	 * @return the doc
	 */
	public Document getDocument() {
		return doc;
	}

	/**
	 * @return the db
	 */
	public Document.Builder getBuilder() {
		return db;
	}
	
	/**
	 * @param doc the doc to set
	 */
	public void setDocoment(Document doc) {
		this.doc = doc;
	}

	protected Field getField(String name) {
		try {
			return doc.getOnlyField(name);
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}

	protected Iterable<Field> getFields(String name) {
		try {
			return doc.getFields(name);
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public String getTextField(String name) {
		Field f = getField(name);
		return f == null ? null : f.getText();
	}

	@Override
	public List<String> getTextFields(String name) {
		Iterable<Field> fs = getFields(name);
		if (fs == null) {
			return null;
		}
		
		List<String> ss = new ArrayList<String>();
		for (Field f : fs) {
			ss.add(f.getText());
		}
		return ss;
	}

	@Override
	public void addTextField(String name, String text) {
		if (text == null) {
			return;
		}
		db.addField(Field.newBuilder().setName(name).setText(text).build());
	}

	@Override
	public Date getDateField(String name) {
		Field f = getField(name);
		return f == null ? null : f.getDate();
	}

	@Override
	public List<Date> getDateFields(String name) {
		Iterable<Field> fs = getFields(name);
		if (fs == null) {
			return null;
		}
		
		List<Date> ds = new ArrayList<Date>();
		for (Field f : fs) {
			ds.add(f.getDate());
		}
		return ds;
	}

	@Override
	public void addDateField(String name, Date date) {
		if (date == null) {
			return;
		}
		db.addField(Field.newBuilder().setName(name).setDate(date).build());
	}

	@Override
	public Number getNumberField(String name) {
		Field f = getField(name);
		return f == null ? null : f.getNumber();
	}

	@Override
	public List<Number> getNumberFields(String name) {
		Iterable<Field> fs = getFields(name);
		if (fs == null) {
			return null;
		}
		
		List<Number> ns = new ArrayList<Number>();
		for (Field f : fs) {
			ns.add(f.getNumber());
		}
		return ns;
	}

	/**
	 * @param name field name
	 * @param num must be less than or equal to 2147483647.000000
	 */
	@Override
	public void addNumberField(String name, Number num) {
		if (num == null) {
			return;
		}
		
		db.addField(Field.newBuilder().setName(name).setNumber(num.doubleValue()).build());
	}

	@Override
	public String getAtomField(String name) {
		Field f = getField(name);
		return f == null ? null : f.getAtom();
	}

	@Override
	public List<String> getAtomFields(String name) {
		Iterable<Field> fs = getFields(name);
		if (fs == null) {
			return null;
		}
		
		List<String> ss = new ArrayList<String>();
		for (Field f : fs) {
			ss.add(f.getAtom());
		}
		return ss;
	}
	
	@Override
	public void addAtomField(String name, String str) {
		if (str == null) {
			return;
		}
		db.addField(Field.newBuilder().setName(name).setAtom(str).build());
	}

	@Override
	public String toString() {
		return "GaeDocument [db=" + db + ", doc=" + doc + "]";
	}

}
