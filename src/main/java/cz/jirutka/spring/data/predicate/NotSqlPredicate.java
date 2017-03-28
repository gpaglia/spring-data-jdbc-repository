package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class NotSqlPredicate extends SqlPredicate {
	private SqlPredicate child;
	
	NotSqlPredicate(SqlPredicate child) {
		this.child = child;
	}
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return "NOT (" + child.toSql(tableNameMapper) + ")";
	}

}
