package cz.jirutka.spring.data.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InSqlPredicate extends SqlPredicate {
	private SqlValue value;
	private List<SqlValue> values;
	
	InSqlPredicate(SqlValue value, List<SqlValue> values) {
		this.value = value;
		this.values = new ArrayList<>(values);
	}
	
	InSqlPredicate(SqlValue value, SqlValue... values) {
		this.value = value;
		this.values = Arrays.asList(values);
	}
	
	@Override
	public String toSql(Function<String, String> tableNameMapper) {
		return value.toSql(tableNameMapper) + values.stream().map(v -> v.toSql(tableNameMapper)).collect(Collectors.joining(", ", " IN (", ")"));
	}

}
