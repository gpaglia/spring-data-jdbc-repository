package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class ColumnReference extends SqlValue {
	private String fieldName;
	private String tableName;
	
	public ColumnReference(String value) {
		super(value);
		String[] items = value.split("\\.");
		if (items.length == 2) {
			tableName = items[0];
			fieldName = items[1];
		} else {
			fieldName = value;
			tableName = null;
		}
	}
	
	public ColumnReference(String tableName, String fieldName) {
		super(tableName + "."  + fieldName);
		this.fieldName = fieldName;
		this.tableName = tableName;
	}
	
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		if (tableNameMapper == null) {
			return value.toUpperCase();
		} else {
			return (tableNameMapper.apply(tableName) + "." + fieldName).toUpperCase();
		}
	}

}
