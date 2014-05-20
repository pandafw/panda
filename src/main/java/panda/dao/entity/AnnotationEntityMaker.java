package panda.dao.entity;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.json.JsonObject;
import panda.dao.DaoClient;
import panda.dao.DaoNamings;
import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.ForeignKeys;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.dao.entity.annotation.Join;
import panda.dao.entity.annotation.JoinColumn;
import panda.dao.entity.annotation.Joins;
import panda.dao.entity.annotation.Meta;
import panda.dao.entity.annotation.PK;
import panda.dao.entity.annotation.Post;
import panda.dao.entity.annotation.Prep;
import panda.dao.entity.annotation.Readonly;
import panda.dao.entity.annotation.SQL;
import panda.dao.entity.annotation.Table;
import panda.dao.entity.annotation.View;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.Fields;
import panda.lang.reflect.Methods;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;

/**
 * Create a Entity by Class
 * @author yf.frank.wang@gmail.com
 */
public class AnnotationEntityMaker implements EntityMaker {
	private static final Log log = Logs.getLog(AnnotationEntityMaker.class);

	private DaoClient client;
	
	public AnnotationEntityMaker(DaoClient client) {
		this.client = client; 
	}

	public <T> Entity<T> make(Class<T> type) {
		Entity<T> en = createEntity(type);

		Collection<Field> fields = Fields.getDeclaredFields(type);

		// Is @Column declared
		boolean shouldUseColumn = false;
		for (Field field : fields) {
			if (null != field.getAnnotation(Column.class)) {
				shouldUseColumn = true;
				break;
			}
		}

		// loop for fields
		for (Field field : fields) {
			addEntityField(en, field, shouldUseColumn);
		}

		// loop for methods
		for (Method method : Methods.getAllMethodsList(en.getType())) {
			addEntityField(en, method);
		}

		// check empty
		if (Collections.isEmpty(en.getFields())) {
			throw new IllegalArgumentException(type + " has no Mapping Fields !");
		}

		// find identity field & check primary key
		for (EntityField ef : en.getFields()) {
			if (ef.isIdentity()) {
				if (en.getIdentity() != null) {
					throw new IllegalArgumentException("Allows only a single @Id of " + type);
				}
				en.setIdentity(ef);
			}
			if (ef.isPrimaryKey()) {
				en.addPrimaryKey(ef);
			}
		}

		// check primary keys
		if (Collections.isEmpty(en.getPrimaryKeys())) {
			throw new IllegalArgumentException(type + " has no Primary Key !");
		}

		// fix not null property of primary key
		for (EntityField ef : en.getPrimaryKeys()) {
			ef.setNotNull(true);
		}

		// evaluate indexes
		Indexes annIndexes = Classes.getAnnotation(type, Indexes.class);
		if (annIndexes != null) {
			evalEntityIndexes(en, annIndexes);
		}

		// evaluate foreign keys
		ForeignKeys annFKeys = Classes.getAnnotation(type, ForeignKeys.class);
		if (annFKeys != null) {
			evalEntityFKeys(en, annFKeys);
		}

		// evaluate joins
		Joins annJoins = Classes.getAnnotation(type, Joins.class);
		if (annJoins != null) {
			evalEntityJoins(en, annJoins);
		}

		// done
		return en;
	}

	private <T> Entity<T> createEntity(Class<T> type) {
		Entity<T> en = new Entity<T>(type);

		BeanHandler<T> bh = client.getBeans().getBeanHandler(type);
		if (bh == null) {
			throw new RuntimeException("Failed to get BeanHander for " + type);
		}
		en.setBeanHandler(bh);
		
		// table meta
		Meta annMeta = Classes.getAnnotation(type, Meta.class);
		if (annMeta != null) {
			JsonObject jo = JsonObject.fromJson(annMeta.value());
			en.setTableMeta(jo);
		}

		// table name
		Table annTable = Classes.getAnnotation(type, Table.class);
		if (annTable == null) {
			en.setTableName(DaoNamings.javaName2TableName(type.getSimpleName()));
		}
		else {
			en.setTableName(annTable.value());
		}

		// table view
		View annView = Classes.getAnnotation(type, View.class);
		if (annView == null) {
			en.setViewName(en.getTableName());
		}
		else {
			en.setViewName(annView.value());
		}

		// check table or view
		if (Strings.isEmpty(en.getTableName()) && Strings.isEmpty(en.getViewName())) {
			throw new IllegalArgumentException("@Table or @View of [" + type + "] is not defined");
		}
		
		// table comment
		Comment annComment = Classes.getAnnotation(type, Comment.class);
		if (annComment != null) {
			en.setComment(annComment.value());
		}
		return en;
	}

	/**
	 * guess the entity field jdbc type by java type
	 * 
	 * @param ef entity field
	 */
	public static void guessEntityFieldJdbcType(EntityField ef) {
		Class<?> clazz = Types.getRawType(ef.getType());

		if (Classes.isBoolean(clazz)) {
			ef.setJdbcType(DaoTypes.BOOLEAN);
		}
		else if (Classes.isChar(clazz)) {
			ef.setJdbcType(DaoTypes.CHAR);
			ef.setSize(1);
		}
		else if (Classes.isByte(clazz)) {
			ef.setJdbcType(DaoTypes.TINYINT);
		}
		else if (Classes.isFloat(clazz)) {
			ef.setJdbcType(DaoTypes.FLOAT);
		}
		else if (Classes.isDouble(clazz)) {
			ef.setJdbcType(DaoTypes.DOUBLE);
		}
		else if (Classes.isInt(clazz)) {
			ef.setJdbcType(DaoTypes.INTEGER);
		}
		else if (Classes.isShort(clazz)) {
			ef.setJdbcType(DaoTypes.SMALLINT);
		}
		else if (Classes.isLong(clazz)) {
			ef.setJdbcType(DaoTypes.BIGINT);
		}
		else if (Classes.isCharSequence(clazz)) {
			ef.setJdbcType(DaoTypes.VARCHAR);
			if (ef.getSize() == 0) {
				ef.setSize(50);
			}
		}
		else if (Classes.isEnum(clazz)) {
			ef.setJdbcType(DaoTypes.VARCHAR);
			if (ef.getSize() == 0) {
				ef.setSize(20);
			}
		}
		else if (Classes.isAssignable(clazz, java.sql.Timestamp.class)) {
			ef.setJdbcType(DaoTypes.TIMESTAMP);
		}
		else if (Classes.isAssignable(clazz, java.sql.Date.class)) {
			ef.setJdbcType(DaoTypes.DATE);
		}
		else if (Classes.isAssignable(clazz, java.sql.Time.class)) {
			ef.setJdbcType(DaoTypes.TIME);
		}
		else if (Classes.isAssignable(clazz, Calendar.class) || Classes.isAssignable(clazz, java.util.Date.class)) {
			ef.setJdbcType(DaoTypes.TIMESTAMP);
		}
		else if (Classes.isAssignable(clazz, BigDecimal.class)) {
			ef.setJdbcType(DaoTypes.DECIMAL);
			if (ef.getSize() == 0) {
				ef.setSize(20);
				ef.setScale(2);
			}
		}
		else if (Classes.isAssignable(clazz, Reader.class)) {
			ef.setJdbcType(DaoTypes.CLOB);
		}
		else if (Classes.isAssignable(clazz, InputStream.class) || byte[].class.equals(clazz)) {
			ef.setJdbcType(DaoTypes.BLOB);
		}
		else {
			// default to string
			if (log.isDebugEnabled()) {
				log.debugf("take field '%s(%s)' as VARCHAR(50)", ef.getName(), clazz.toString());
			}
			ef.setJdbcType(DaoTypes.VARCHAR);
			if (ef.getSize() == 0) {
				ef.setSize(50);
			}
		}
	}

	private static class MappingInfo {
		String name;
		Type type;

		Id annId;
		PK annPk;
		FK annFk;
		Index annIndex;
		Column annColumn;
		Comment annComment;
		Readonly annReadonly;
		JoinColumn annJoinColumn;
		Prep annPrep;
		Post annPost;
		
		public static MappingInfo create(Field field, boolean useColumn) {
			MappingInfo mi = new MappingInfo();
			mi.annId = field.getAnnotation(Id.class);
			mi.annPk = field.getAnnotation(PK.class);
			mi.annFk = field.getAnnotation(FK.class);
			mi.annIndex = field.getAnnotation(Index.class);
			mi.annColumn = field.getAnnotation(Column.class);
			mi.annJoinColumn = field.getAnnotation(JoinColumn.class);

			if (Modifier.isTransient(field.getModifiers())) {
				useColumn = true;
			}

			if (useColumn && mi.annColumn == null && mi.annId == null && mi.annPk == null && mi.annFk == null
					&& mi.annIndex == null && mi.annJoinColumn == null) {
				return null;
			}

			mi.name = field.getName();
			mi.type = field.getGenericType();
			mi.annComment = field.getAnnotation(Comment.class);
			mi.annReadonly = field.getAnnotation(Readonly.class);
			mi.annPrep = field.getAnnotation(Prep.class);
			mi.annPost = field.getAnnotation(Post.class);
			return mi;
		}

		public static MappingInfo create(Method method) {
			MappingInfo mi = new MappingInfo();
			mi.annId = method.getAnnotation(Id.class);
			mi.annPk = method.getAnnotation(PK.class);
			mi.annFk = method.getAnnotation(FK.class);
			mi.annIndex = method.getAnnotation(Index.class);
			mi.annColumn = method.getAnnotation(Column.class);
			mi.annJoinColumn = method.getAnnotation(JoinColumn.class);

			if (mi.annColumn == null && mi.annId == null && mi.annPk == null && mi.annFk == null 
					&& mi.annIndex == null && mi.annJoinColumn == null) {
				return null;
			}

			String name = Beans.getBeanName(method);
			if (Strings.isEmpty(name)) {
				throw Exceptions.makeThrow("Method '%s'(%s) can not add '@Column', it MUST be a setter or getter!",
					method.getName(), method.getDeclaringClass().getName());
			}

			mi.name = name;
			mi.type = method.getGenericReturnType();
			mi.annComment = method.getAnnotation(Comment.class);
			mi.annReadonly = method.getAnnotation(Readonly.class);
			mi.annPrep = method.getAnnotation(Prep.class);
			mi.annPost = method.getAnnotation(Post.class);
			return mi;
		}
	}
	
	private void addEntityField(Entity<?> entity, MappingInfo mi) {
		EntityField ef = new EntityField();

		if (mi.annId != null) {
			ef.setIdentity(true);
			ef.setAutoIncrement(mi.annId.auto());
			ef.setStartWith(mi.annId.start());
			ef.setPrimaryKey(true);
		}
		if (mi.annPk != null) {
			ef.setPrimaryKey(true);
		}
		if (mi.annReadonly != null) {
			ef.setReadonly(true);
		}

		ef.setName(mi.name);
		ef.setType(mi.type);
		if (mi.annColumn != null) {
			ef.setColumn(mi.annColumn.value());
			ef.setJdbcType(mi.annColumn.type());
			ef.setSize(mi.annColumn.size());
			ef.setScale(mi.annColumn.scale());
			ef.setUnsigned(mi.annColumn.unsigned());
			ef.setNotNull(mi.annColumn.notNull());
			ef.setNativeType(mi.annColumn.nativeType());
			ef.setDefaultValue(mi.annColumn.defaults());
		}

		if (mi.annJoinColumn != null) {
			ef.setJoinName(mi.annJoinColumn.name());
			ef.setJoinField(mi.annJoinColumn.field());
			ef.setColumn(null);
		}
		else {
			if (Strings.isBlank(ef.getColumn())) {
				ef.setColumn(mi.name);
			}
		}
		
		if (Strings.isBlank(ef.getJdbcType())) {
			guessEntityFieldJdbcType(ef);
		}

		if (mi.annComment != null && Strings.isNotBlank(mi.annComment.value())) {
			ef.setComment(mi.annComment.value());
		}

		if (mi.annPrep != null) {
			for (SQL s : mi.annPrep.value()) {
				entity.addPrepSql(s.db(), s.value());
			}
		}
		if (mi.annPost != null) {
			for (SQL s : mi.annPost.value()) {
				entity.addPostSql(s.db(), s.value());
			}
		}

		if (mi.annFk != null) {
			evalEntityFKey(entity, mi.annFk.name(), mi.annFk.target(), ef);
		}
		
		if (mi.annIndex != null) {
			evalEntityIndex(entity, mi.annIndex.name(), ef, mi.annIndex.unique());
		}

		entity.addField(ef);
	}

	private void addEntityField(Entity<?> entity, Field field, boolean useColumn) {
		MappingInfo mi = MappingInfo.create(field, useColumn);
		if (mi == null) {
			return;
		}
	
		addEntityField(entity, mi);
	}

	private void addEntityField(Entity<?> entity, Method method) {
		MappingInfo mi = MappingInfo.create(method);
		if (mi == null) {
			return;
		}
	
		addEntityField(entity, mi);
	}

	private void evalEntityIndex(Entity<?> en, String name, EntityField field, boolean unique) {
		EntityIndex ei = new EntityIndex();
		ei.setUnique(unique);
		ei.setName(Strings.isEmpty(name) ? field.getName() : name);
		ei.addField(field);
		en.addIndex(ei);
	}

	private void evalEntityIndex(Entity<?> en, Index idx) {
		EntityIndex ei = new EntityIndex();
		
		ei.setUnique(idx.unique());
		ei.setReal(idx.real());
		ei.setName(Strings.isEmpty(idx.name()) ? Strings.join(idx.fields(), '_') : idx.name());
		if (Arrays.isEmpty(idx.fields())) {
			throw Exceptions.makeThrow("Empty fields for @Index(%s: %s)", 
				ei.getName(), Strings.join(idx.fields(), '|'));
		}
		for (String in : idx.fields()) {
			EntityField ef = en.getField(in);
			if (null == ef) {
				throw Exceptions.makeThrow("Failed to find field '%s' in '%s' for @Index(%s: %s)", 
					in, en.getType(), ei.getName(), Strings.join(idx.fields(), '|'));
			}
			ei.addField(ef);
		}
		en.addIndex(ei);
	}

	private void evalEntityIndexes(Entity<?> en, Indexes indexes) {
		for (Index idx : indexes.value()) {
			evalEntityIndex(en, idx);
		}
	}

	private Entity<?> getTargetEntity(Entity<?> en, Class<?> target) {
		if (en.getType().equals(target)) {
			return en;
		}
		return client.getEntity(target);
	}

	private void evalEntityFKey(Entity<?> en, String name, Class<?> target, String[] fields) {
		EntityFKey efk = new EntityFKey();
		Entity<?> ref = getTargetEntity(en, target);
		if (ref == null) {
			throw new IllegalArgumentException("Failed to find target entity for " + target);
		}
		efk.setReference(ref);
		efk.setName(Strings.isEmpty(name) ? ref.getTableName() : name);

		if (fields == null || fields.length == 0) {
			throw Exceptions.makeThrow("Empty fields for @FK(%s: %s)", efk.getName(), Strings.join(fields, '|'));
		}
		
		List<EntityField> rpks = ref.getPrimaryKeys();
		if (fields.length != rpks.size()) {
			throw Exceptions.makeThrow("Invalid fields for @FK(%s: %s)", efk.getName(), Strings.join(fields, '|'));
		}
		
		for (int i = 0; i < fields.length; i++) {
			String fn = fields[i];
			
			EntityField ef = en.getField(fn);
			if (ef == null) {
				throw Exceptions.makeThrow("Failed to find field '%s' in '%s' for @FK(%s: %s)", 
					fn, en.getType(), efk.getName(), Strings.join(fields, '|'));
			}
			
			EntityField rf = rpks.get(i);
			
			if (!Types.equals(ef.getType(), rf.getType())) {
				throw Exceptions.makeThrow(
					"The type '%s' of field '%s' is not equals to the field '%s' of reference entity '%s' for '%s'@FK(%s: %s)", 
					ef.getType(), fn, rf.getName(), ref.getType(), en.getType(), efk.getName(), Strings.join(fields, '|'));
			}
			efk.addField(ef);
		}
		en.addForeignKey(efk);
	}

	private void evalEntityFKey(Entity<?> en, String name, Class<?> target, EntityField field) {
		EntityFKey efk = new EntityFKey();
		Entity<?> ref = getTargetEntity(en, target);
		if (null == ref) {
			throw new IllegalArgumentException("Failed to find target entity for " + target);
		}
		efk.setReference(ref);
		efk.setName(Strings.isEmpty(name) ? ref.getTableName() : name);

		efk.addField(field);
		en.addForeignKey(efk);
	}

	private void evalEntityFKeys(Entity<?> en, ForeignKeys fks) {
		for (FK fk: fks.value()) {
			evalEntityFKey(en, fk.name(), fk.target(), fk.fields());
		}
	}

	private void evalEntityJoin(Entity<?> en, String name, String type, Class<?> target, String[] keys, String[] refs) {
		EntityJoin ej = new EntityJoin();
		Entity<?> ref = getTargetEntity(en, target);
		if (ref == null) {
			throw new IllegalArgumentException("Failed to find target entity for " + target);
		}

		ej.setName(Strings.isEmpty(name) ? ref.getTableName() : name);
		ej.setType(type);
		ej.setTarget(ref);

		if (keys == null || keys.length == 0) {
			throw Exceptions.makeThrow("Empty keys for @Join(%s: %s)", ej.getName(), Strings.join(keys, '|'));
		}

		if (refs == null || refs.length == 0) {
			throw Exceptions.makeThrow("Empty refs for @Join(%s: %s)", ej.getName(), Strings.join(refs, '|'));
		}

		if (keys.length != refs.length) {
			throw Exceptions.makeThrow("keys & refs for @Join(%s: %s, %s) is not valid", ej.getName(), Strings.join(keys, '|'), Strings.join(refs, '|'));
		}
		
		for (int i = 0; i < keys.length; i++) {
			String kn = keys[i];
			String rn = refs[i];
			
			EntityField ef = en.getField(kn);
			if (ef == null) {
				throw Exceptions.makeThrow("Failed to find key field '%s' in '%s' for @Join(%s: %s)", 
					kn, en.getType(), ej.getName(), Strings.join(keys, '|'));
			}
			
			EntityField rf = ref.getField(rn);
			
			if (!Types.equals(ef.getType(), rf.getType())) {
				throw Exceptions.makeThrow(
					"The type '%s' of field '%s' is not equals to the field '%s' of target entity '%s' for '%s'@Join(%s: %s, %s)", 
					ef.getType(), kn, rf.getName(), ref.getType(), en.getType(), ej.getName(), 
					Strings.join(keys, '|'), Strings.join(refs, '|'));
			}
			
			ej.addKeyField(ef);
			ej.addRefField(rf);
		}
		en.addJoin(ej);
	}

	private void evalEntityJoins(Entity<?> en, Joins joins) {
		for (Join join: joins.value()) {
			evalEntityJoin(en, join.name(), join.type(), join.target(), join.keys(), join.refs());
		}
	}
}
