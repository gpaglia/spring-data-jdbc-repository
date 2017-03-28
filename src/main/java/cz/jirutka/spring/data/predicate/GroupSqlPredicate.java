package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class GroupSqlPredicate extends SqlPredicate {
	private SqlPredicate child;
	
	GroupSqlPredicate(SqlPredicate child) {
		this.child = child;
	}
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return "(" + child.toSql(tableNameMapper) + ")";
	}

}
