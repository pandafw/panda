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

import panda.bean.Beans;
import panda.bind.json.JsonObject;
import panda.dao.entity.annotation.ColDefine;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.PK;
import panda.dao.entity.annotation.Readonly;
import panda.dao.entity.annotation.Table;
import panda.dao.entity.annotation.TableIndexes;
import panda.dao.entity.annotation.TableMeta;
import panda.dao.entity.annotation.View;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.SqlNamings;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.Types;
import panda.log.Log;
import panda.log.Logs;

/**
 * Create a Entity by Class
 * @author yf.frank.wang@gmail.com
 */
public class AnnotationEntityMaker implements EntityMaker {

	private static final Log log = Logs.getLog(AnnotationEntityMaker.class);

	public AnnotationEntityMaker() {
	}

	public <T> Entity<T> make(Class<T> type) {
		Entity<T> en = createEntity(type);

		Collection<Field> fields = Classes.getFields(type);

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
			EntityField ef = createEntityField(field, shouldUseColumn);
			if (ef != null) {
				en.addField(ef);
			}
		}

		// loop for methods
		for (Method method : Classes.getMethods(en.getType())) {
			EntityField ef = createEntityField(method);
			if (ef != null) {
				en.addField(ef);
			}
		}

		// check empty
		if (en.getFields().isEmpty()) {
			throw new IllegalArgumentException(type + " has no Mapping Field!!");
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

		// evaluate indexes
		TableIndexes annIndexes = Classes.getAnnotation(type, TableIndexes.class);
		if (annIndexes != null) {
			evalEntityIndexes(en, annIndexes);
		}

		// done
		return en;
	}

	private <T> Entity<T> createEntity(Class<T> type) {
		Entity<T> en = new Entity<T>(type);

		// Table meta
		TableMeta annMeta = Classes.getAnnotation(type, TableMeta.class);
		if (annMeta != null) {
			JsonObject jo = JsonObject.fromJson(annMeta.value());
			en.getMetas().putAll(jo);
		}

		Table annTable = Classes.getAnnotation(type, Table.class);
		// table name
		if (annTable == null) {
			en.setTableName(SqlNamings.javaName2TableName(type.getSimpleName()));
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
	 * 根据字段现有的信息，尽可能猜测一下字段的数据库类型
	 * 
	 * @param ef 映射字段
	 */
	public static void guessEntityFieldJdbcType(EntityField ef) {
		Class<?> clazz = Types.getRawType(ef.getType());

		if (Classes.isBoolean(clazz)) {
			ef.setJdbcType(JdbcTypes.BOOLEAN);
			ef.setSize(1);
		}
		else if (Classes.isChar(clazz)) {
			ef.setJdbcType(JdbcTypes.CHAR);
			ef.setSize(1);
		}
		else if (Classes.isByte(clazz)) {
			ef.setJdbcType(JdbcTypes.TINYINT);
		}
		else if (Classes.isFloat(clazz)) {
			ef.setJdbcType(JdbcTypes.FLOAT);
		}
		else if (Classes.isDouble(clazz)) {
			ef.setJdbcType(JdbcTypes.DOUBLE);
		}
		else if (Classes.isInt(clazz)) {
			ef.setJdbcType(JdbcTypes.INTEGER);
		}
		else if (Classes.isShort(clazz)) {
			ef.setJdbcType(JdbcTypes.SMALLINT);
		}
		else if (Classes.isLong(clazz)) {
			ef.setJdbcType(JdbcTypes.BIGINT);
		}
		else if (Classes.isStringLike(clazz)) {
			ef.setJdbcType(JdbcTypes.VARCHAR);
			ef.setSize(50);
		}
		else if (Classes.isEnum(clazz)) {
			ef.setJdbcType(JdbcTypes.VARCHAR);
			ef.setSize(20);
		}
		else if (Classes.isAssignable(clazz, java.sql.Timestamp.class)) {
			ef.setJdbcType(JdbcTypes.TIMESTAMP);
		}
		else if (Classes.isAssignable(clazz, java.sql.Date.class)) {
			ef.setJdbcType(JdbcTypes.DATE);
		}
		else if (Classes.isAssignable(clazz, java.sql.Time.class)) {
			ef.setJdbcType(JdbcTypes.TIME);
		}
		else if (Classes.isAssignable(clazz, Calendar.class) || Classes.isAssignable(clazz, java.util.Date.class)) {
			ef.setJdbcType(JdbcTypes.TIMESTAMP);
		}
		else if (Classes.isAssignable(clazz, BigDecimal.class)) {
			ef.setJdbcType(JdbcTypes.NUMERIC);
			ef.setSize(32);
		}
		else if (Classes.isAssignable(clazz, Reader.class)) {
			ef.setJdbcType(JdbcTypes.CLOB);
		}
		else if (Classes.isAssignable(clazz, InputStream.class) || byte[].class.equals(clazz)) {
			ef.setJdbcType(JdbcTypes.BLOB);
		}
		else {
			// default to string
			if (log.isDebugEnabled()) {
				log.debugf("take field '%s(%s)' as VARCHAR(50)", ef.getName(), clazz.toString());
			}
			ef.setJdbcType(JdbcTypes.VARCHAR);
			ef.setSize(50);
		}
	}

	private static class MappingInfo {
		String name;
		Type type;

		Id annId;
		PK annPk;
		Column annColumn;
		Comment annComment;
		ColDefine annDefine;
		Readonly annReadonly;
		
		public static MappingInfo create(Field field, boolean useColumn) {
			Id annId = field.getAnnotation(Id.class);
			PK annPk = field.getAnnotation(PK.class);
			Column annColumn = field.getAnnotation(Column.class);

			if (Modifier.isTransient(field.getModifiers())) {
				useColumn = true;
			}

			if (useColumn && annColumn == null && annId == null && annPk == null) {
				return null;
			}

			MappingInfo mi = new MappingInfo();
			mi.name = field.getName();
			mi.type = field.getGenericType();
			mi.annId = annId;
			mi.annPk = annPk;
			mi.annColumn = annColumn;
			mi.annComment = field.getAnnotation(Comment.class);
			mi.annDefine = field.getAnnotation(ColDefine.class);
			mi.annReadonly = field.getAnnotation(Readonly.class);
			return mi;
		}

		public static MappingInfo create(Method method) {
			Id annId = method.getAnnotation(Id.class);
			PK annPk = method.getAnnotation(PK.class);
			Column annColumn = method.getAnnotation(Column.class);

			if (annColumn == null && annId == null && annPk == null) {
				return null;
			}

			String name = Beans.getBeanName(method);
			if (Strings.isEmpty(name)) {
				throw Exceptions.makeThrow("Method '%s'(%s) can not add '@Column', it MUST be a setter or getter!",
					method.getName(), method.getDeclaringClass().getName());
			}

			MappingInfo mi = new MappingInfo();
			mi.name = name;
			mi.type = method.getGenericReturnType();
			mi.annId = annId;
			mi.annPk = annPk;
			mi.annColumn = annColumn;
			mi.annComment = method.getAnnotation(Comment.class);
			mi.annDefine = method.getAnnotation(ColDefine.class);
			mi.annReadonly = method.getAnnotation(Readonly.class);
			return mi;
		}
	}
	
	private EntityField createEntityField(MappingInfo mi) {
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
		if (mi.annColumn == null || Strings.isBlank(mi.annColumn.value())) {
			ef.setColumn(mi.annColumn.value());
		}
		else {
			ef.setColumn(mi.name);
		}

		if (mi.annComment != null && Strings.isNotBlank(mi.annComment.value())) {
			ef.setComment(mi.annComment.value());
		}

		if (mi.annDefine != null) {
			ef.setJdbcType(mi.annDefine.type());
			ef.setSize(mi.annDefine.size());
			ef.setScale(mi.annDefine.scale());
			ef.setUnsigned(mi.annDefine.unsigned());
			ef.setNotNull(mi.annDefine.notNull());

			if (Strings.isNotBlank(mi.annDefine.dbType())) {
				ef.setDbType(mi.annDefine.dbType());
			}
		}
		else {
			guessEntityFieldJdbcType(ef);
		}
		return ef;
	}

	private EntityField createEntityField(Field field, boolean useColumn) {
		MappingInfo mi = MappingInfo.create(field, useColumn);
		if (mi == null) {
			return null;
		}
	
		return createEntityField(mi);
	}

	private EntityField createEntityField(Method method) {
		MappingInfo mi = MappingInfo.create(method);
		if (mi == null) {
			return null;
		}
	
		return createEntityField(mi);
	}

	private void evalEntityIndexes(Entity<?> en, TableIndexes indexes) {
		for (Index idx : indexes.value()) {
			EntityIndex index = new EntityIndex();
			index.setUnique(idx.unique());
			index.setName(idx.name());
			for (String indexName : idx.fields()) {
				EntityField ef = en.getField(indexName);
				if (null == ef) {
					throw Exceptions.makeThrow("Fail to find field '%s' in '%s' by @Index(%s:%s)", indexName, en
						.getType().getName(), index.getName(), Strings.join(idx.fields(), ", "));
				}
				index.addField(ef);
			}
			en.addIndex(index);
		}
	}
}
