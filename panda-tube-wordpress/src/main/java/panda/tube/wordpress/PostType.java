package panda.tube.wordpress;

import java.util.List;

import panda.bind.json.Jsons;

public class PostType {
	public String name;
	public String label;
	public boolean hierarchical;
	public boolean _public;
	public boolean show_ui;
	public boolean _builtin;
	public boolean has_archive;
	/**: Features supported by the theme as keys, values always true. See post_type_supports.*/
	public Object supports;
	public Object labels;
	public Object cap;
	public boolean map_meta_cap;
	int menu_position;
	public String menu_icon;
	public boolean show_in_menu;
	public List<Object> taxonomies;
	/**
	 * @return the _public
	 */
	public boolean isPublic() {
		return _public;
	}
	/**
	 * @param _public the _public to set
	 */
	public void setPublic(boolean _public) {
		this._public = _public;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
