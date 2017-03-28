package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class BetweenSqlPredicate extends SqlPredicate {
	private SqlValue value;

	private SqlValue value1;
	private SqlValue value2;
	
	BetweenSqlPredicate(SqlValue value, SqlValue value1, SqlValue value2) {
		this.value = value;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return value.toSql(tableNameMapper) + " BETWEEN " + value1.toSql(tableNameMapper) + " AND " + value2.toSql(tableNameMapper);
	}

}
