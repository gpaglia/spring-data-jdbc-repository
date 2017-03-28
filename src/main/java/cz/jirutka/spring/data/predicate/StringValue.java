package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class StringValue extends SqlValue {

	public StringValue(String value) {
		super(value);
	}
	
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return "'" + value + "'";
	}

}
