package cz.jirutka.spring.data.jdbc.ext;

import java.util.Arrays;

import cz.jirutka.spring.data.jdbc.ext.FieldType;

public class BasicFieldMetaData extends AbstractFieldMetaData {
	private String[] columnNames;
	private FieldType fieldType;

	public BasicFieldMetaData(ObjectMetaData objectMetaData, String fieldName, FieldType fieldType, String[] columnNames) {
		super(objectMetaData, fieldName);
		this.fieldType = fieldType;
		this.columnNames = Arrays.copyOf(columnNames, columnNames.length);
	}
	
	public BasicFieldMetaData(ObjectMetaData objectMetaData, String fieldName, FieldType fieldType, String columnName) {
		this(objectMetaData, fieldName, fieldType, new String[] { columnName });
	}

	public String[] getColumnNames() {
		return Arrays.copyOf(columnNames, columnNames.length);
	}

	public FieldType getFieldType() {
		return fieldType;
	}
	
}
