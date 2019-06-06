package panda.tube.sendgrid;

import java.util.List;

import panda.bind.json.Jsons;

public class ASM {
	public Integer group_id;
	public List<Integer> groups_to_display;

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
