package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class NumericValue extends SqlValue {

	public NumericValue(int value) {
		super(String.valueOf(value));
	}
	
	public NumericValue(double value) {
		super(String.valueOf(value));
	}
	
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return value;
	}

}
