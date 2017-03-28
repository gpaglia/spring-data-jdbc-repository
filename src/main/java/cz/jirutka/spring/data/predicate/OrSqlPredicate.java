package cz.jirutka.spring.data.predicate;

import java.util.function.Function;

public class OrSqlPredicate extends SqlPredicate {
	private SqlPredicate child1;
	private SqlPredicate child2;
	
	OrSqlPredicate(SqlPredicate child1, SqlPredicate child2) {
		this.child1 = child1;
		this.child2 = child2;
	}
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return "(" + child1.toSql(tableNameMapper) + ") OR (" + child2.toSql(tableNameMapper) + ")";
	}

}
