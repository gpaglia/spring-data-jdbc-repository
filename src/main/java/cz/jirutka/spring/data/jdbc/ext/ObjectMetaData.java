package cz.jirutka.spring.data.jdbc.ext;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ObjectMetaData {
	private Class<?> objectClass;
	private String tableName;
	private Map<String, AbstractFieldMetaData> fieldsMetaData;
	
	public static class FieldInfo {
		String fieldName;
		FieldType fieldType;
		String[] columnNames;
		
		public FieldInfo(String fieldName, FieldType fieldType, String[] columnNames) {
			this.fieldName = fieldName;
			this.fieldType = fieldType;
			this.columnNames = columnNames;
		}
		public FieldInfo(String fieldName, FieldType fieldType, String columnName) {
			this(fieldName, fieldType, new String[] { columnName });
		}
		
		
	}
	
	public static class ReferenceInfo {
		String fieldName;
		ReferenceType referenceType;
		Class<?> refObjectClass;
		String refFieldName;
		public ReferenceInfo(String fieldName, ReferenceType referenceType, Class<?> refObjectClass, String refFieldName) {
			super();
			this.fieldName = fieldName;
			this.referenceType = referenceType;
			this.refObjectClass = refObjectClass;
			this.refFieldName = refFieldName;
		}
		
	}
	
	public ObjectMetaData(Class<?> objectClass, String tableName, List<FieldInfo> fieldInfoList, List<ReferenceInfo> referenceInfoList) {
		super();
		this.objectClass = objectClass;
		this.tableName = tableName;
		Stream<AbstractFieldMetaData> fields = fieldInfoList.stream().map(fi -> new BasicFieldMetaData(this, fi.fieldName, fi.fieldType, fi.columnNames));
		Stream<AbstractFieldMetaData> references = referenceInfoList.stream().map(ri -> new ReferenceFieldMetaData(this, ri.fieldName, ri.referenceType, ri.refObjectClass, ri.refFieldName));
		this.fieldsMetaData = Stream.concat(fields,  references).collect(toMap(f -> f.getFieldName(), g -> g));
	}
	
	public Class<?> getObjectClass() {
		return objectClass;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public Map<String, AbstractFieldMetaData> getFieldsMetaData() {
		return new HashMap<>(fieldsMetaData);
	}

}
