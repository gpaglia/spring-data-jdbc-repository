package cz.jirutka.spring.data.predicate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Test;

import static cz.jirutka.spring.data.predicate.SqlPredicate.*;

public class PredicateTest {
	
	@Test()
	public void valueTest() {
		
		assertThat(number(12345).toSql(null), is(equalTo("12345")));
		assertThat(number(12345.67).toSql(null), is(equalTo("12345.67")));
		assertThat(number(1.7e2).toSql(null), is(equalTo("170.0")));
		assertThat(string("ABcde").toSql(null), is(equalTo("'ABcde'")));
		assertThat(field("ciao").toSql(null), is(equalTo("CIAO")));
		assertThat(field("tab", "field").toSql(null), is(equalTo("TAB.FIELD")));
		assertThat(field("tab.field").toSql(null), is(equalTo("TAB.FIELD")));

	}
	
	@Test()
	public void predicateTest() {
		assertThat(number(12345).gt(number(32)).toSql(null), is(equalTo("12345 > 32")));
		assertThat(number(12345).ge(number(32)).toSql(null), is(equalTo("12345 >= 32")));
		assertThat(number(12345).lt(number(32)).toSql(null), is(equalTo("12345 < 32")));
		assertThat(number(12345).le(number(32)).toSql(null), is(equalTo("12345 <= 32")));
		assertThat(number(12345).ne(number(32)).toSql(null), is(equalTo("12345 <> 32")));
		assertThat(number(12345).eq(number(32)).toSql(null), is(equalTo("12345 = 32")));
		
	}
	
	@Test()
	public void predicateTest2() {
		assertThat(string("32").eq(number(32)).toSql(null), is(equalTo("'32' = 32")));
		assertThat(field("TAB", "field").eq(number(32)).toSql(null), is(equalTo("TAB.FIELD = 32")));
		assertThat(field("TAB", "field").in(number(1), number(2), number(3)).toSql(null), is(equalTo("TAB.FIELD IN (1, 2, 3)")));
		assertThat(field("TAB", "field").between(number(1), number(2)).toSql(null), is(equalTo("TAB.FIELD BETWEEN 1 AND 2")));

	}
	
	@Test()
	public void predicateTest3() {
		SqlPredicate p = field("TAB", "field").eq(number(32)).and(field("TAB", "field").in(number(1), number(2), number(3)));
		
		assertThat(p.toSql(null), is(equalTo("(TAB.FIELD = 32) AND (TAB.FIELD IN (1, 2, 3))")));
		
	}
	
	@Test()
	public void predicateTest4() {
		SqlPredicate p = field("TAB", "field").eq(number(32)).or(field("TAB", "field").between(number(1), number(2)));
		
		assertThat(p.toSql(null), is(equalTo("(TAB.FIELD = 32) OR (TAB.FIELD BETWEEN 1 AND 2)")));
		
	}
	
	@Test()
	public void predicateTest5() {
		SqlPredicate p = not(field("TAB", "field").eq(number(32)).or(field("TAB", "field").between(number(1), number(2))));
		
		assertThat(p.toSql(null), is(equalTo("NOT ((TAB.FIELD = 32) OR (TAB.FIELD BETWEEN 1 AND 2))")));
		
	}
	
	@Test()
	public void predicateTest6() {
		SqlPredicate p = group(field("TAB", "field").eq(number(32)).or(field("TAB", "field").between(number(1), number(2))));
		
		assertThat(p.toSql(null), is(equalTo("((TAB.FIELD = 32) OR (TAB.FIELD BETWEEN 1 AND 2))")));
		
	}
	
	@Test()
	public void predicateNameTest() {
		Function<String, String> mapper = (s) -> {
			if (s == null) {
				return "NULL_TABLE";
			} else {
				return s + "_TABLE";
			}
		};
		
		assertThat(field("ciao").toSql(mapper), is(equalTo("NULL_TABLE.CIAO")));
		assertThat(field("tab", "field").toSql(mapper), is(equalTo("TAB_TABLE.FIELD")));
		assertThat(field("tab.field").toSql(mapper), is(equalTo("TAB_TABLE.FIELD")));
		
		SqlPredicate p = field("TAB", "field").eq(number(32)).and(field("TAB", "field").in(number(1), number(2), number(3)));
		
		assertThat(p.toSql(mapper), is(equalTo("(TAB_TABLE.FIELD = 32) AND (TAB_TABLE.FIELD IN (1, 2, 3))")));

	}
}
