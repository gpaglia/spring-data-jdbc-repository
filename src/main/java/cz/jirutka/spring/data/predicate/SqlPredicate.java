package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public abstract class SqlPredicate {
	
	SqlPredicate() {}
	
	public abstract String toSql(Function<String, String> tableNameMapper);
	
	
	public SqlPredicate or(SqlPredicate p) {
		return new OrSqlPredicate(this, p);
	}
	public SqlPredicate and(SqlPredicate p) {
		return new AndSqlPredicate(this, p);
	}
	public static NumericValue number(int v) {
		return new NumericValue(v);
	}
	public static NumericValue number(double v) {
		return new NumericValue(v);
	}
	public static StringValue string(String v) {
		return new StringValue(v);
	}
	public static ColumnReference field(String v) {
		return new ColumnReference(v);
	}
	public static ColumnReference field(String t, String f) {
		return new ColumnReference(t, f);
	}
	public static SqlPredicate not(SqlPredicate p) {
		return new NotSqlPredicate(p);
	}
	public static SqlPredicate group(SqlPredicate p) {
		return new GroupSqlPredicate(p);
	}
	public static SqlPredicate and(SqlPredicate p1, SqlPredicate p2) {
		return new AndSqlPredicate(p1, p2);
	}
	public static SqlPredicate or(SqlPredicate p1, SqlPredicate p2) {
		return new OrSqlPredicate(p1, p2);
	}
}
