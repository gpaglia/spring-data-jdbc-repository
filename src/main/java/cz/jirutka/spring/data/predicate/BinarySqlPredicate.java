package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class BinarySqlPredicate extends SqlPredicate {
	private SqlValue value1;
	private SqlValue value2;
	private String operator;
	
	BinarySqlPredicate(SqlValue value1, SqlValue value2, String operator) {
		this.value1 = value1;
		this.value2 = value2;
		this.operator = operator;
	}
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return value1.toSql(tableNameMapper) + " " + operator + " " + value2.toSql(tableNameMapper);
	}

}
