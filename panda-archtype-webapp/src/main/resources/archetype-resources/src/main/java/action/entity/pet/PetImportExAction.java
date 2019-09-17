package ${package}.action.entity.pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panda.dao.entity.EntityField;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.annotation.At;

import ${package}.entity.Pet;

@At("/pet")
public class PetImportExAction extends PetImportAction {
	private Map<String, String> genders;

	public PetImportExAction() {
		// set update key { 'name', 'gender' }
		setUpdateKey(new String[] { Pet.NAME, Pet.GENDER });
	}

	@SuppressWarnings("unchecked")
	private String getGender(String p) {
		if (genders == null) {
			genders = Collections.swapMap(consts().getMap("petGenderMap"));
		}
		return genders.get(p);
	}

	@Override
	protected void trimData(Pet data) {
		List<EntityField> efs = new ArrayList<EntityField>();
		
		if (Strings.isNotEmpty(data.getGender())) {
			String g = getGender(data.getGender());
			if (Strings.isEmpty(g)) {
				efs.add(getEntity().getField(Pet.GENDER));
			}
			else {
				data.setGender(g);
			}
		}
		
		if (Collections.isNotEmpty(efs)) {
			throw new IllegalArgumentException(dataIncorrectError(data, efs));
		}
		super.trimData(data);
	}
}
