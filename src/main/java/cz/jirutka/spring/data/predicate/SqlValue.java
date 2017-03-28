package cz.jirutka.spring.data.predicate;

import java.util.List;
import java.util.function.Function;

public abstract class SqlValue {
	protected String value;
	
	protected SqlValue(String value) {
		this.value = value;
	}
	
	public abstract String toSql(Function<String, String> tableNameMapper);
	
	public SqlPredicate in(List<SqlValue> values) {
		return new InSqlPredicate(this, values);
	}
	public SqlPredicate in(SqlValue... values) {
		return new InSqlPredicate(this, values);
	}
	public SqlPredicate between(SqlValue value1, SqlValue value2) {
		return new BetweenSqlPredicate(this, value1, value2);
	}
	public SqlPredicate gt(SqlValue value) {
		return new BinarySqlPredicate(this, value, ">");
	}
	public SqlPredicate ge(SqlValue value) {
		return new BinarySqlPredicate(this, value, ">=");
	}
	public SqlPredicate lt(SqlValue value) {
		return new BinarySqlPredicate(this, value, "<");
	}
	public SqlPredicate le(SqlValue value) {
		return new BinarySqlPredicate(this, value, "<=");
	}
	public SqlPredicate ne(SqlValue value) {
		return new BinarySqlPredicate(this, value, "<>");
	}
	public SqlPredicate eq(SqlValue value) {
		return new BinarySqlPredicate(this, value, "=");
	}
}
