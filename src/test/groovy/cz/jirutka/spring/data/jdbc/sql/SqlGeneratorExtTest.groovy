/*
 * Copyright 2012-2014 Tomasz Nurkiewicz <nurkiewicz@gmail.com>.
 * Copyright 2016 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License')
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.jirutka.spring.data.jdbc.sql

import cz.jirutka.spring.data.jdbc.TableDescription
import cz.jirutka.spring.data.predicate.SqlPredicate
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.data.domain.Sort.Direction.ASC
import static org.springframework.data.domain.Sort.Direction.DESC

import static cz.jirutka.spring.data.predicate.SqlPredicate.*;
import static cz.jirutka.spring.data.predicate.SqlValue.*;


@Unroll
class SqlGeneratorExtTest extends Specification {

    final ANY = new Object()

    def table = new TableDescription (
        tableName: 'tab',
        selectClause: 'a, b',
        fromClause: 'tabx',
        pkColumns: ['tid']
    )
	
    def getSqlGenerator() { new DefaultSqlGenerator() }


    def 'count() wpred'() {
		setup:
			def pred = field('a').eq(number(12)).and(field('b').gt(number(2)))
        expect:
            sqlGenerator.count(table, pred) == "SELECT count(*) FROM tabx WHERE ( (A = 12) AND (B > 2) )"
    }


    def 'deleteAll() wpred'() {
		setup:
			def pred = field('a').eq(number(12)).and(field('b').gt(number(2)))
        expect:
            sqlGenerator.deleteAll(table, pred) == "DELETE FROM tab WHERE ( (A = 12) AND (B > 2) )"
    }


    def 'selectAll() wpred'() {
		setup:
			def pred = field('a').eq(number(12)).and(field('b').gt(number(2)))
        expect:
            sqlGenerator.selectAll(table, pred) == "SELECT a, b FROM tabx WHERE ( (A = 12) AND (B > 2) )"
    }

    def "selectAll(Pageable) wpred: #desc"() {
        setup:
            table.pkColumns = pkColumns(pkSize)
			def pred = field('a').eq(number(12)).and(field('b').gt(number(2)))
            def expected = expectedPaginatedQuery(table, pageable, pred)
        expect:
            sqlGenerator.selectAll(table, pageable, pred) == expected
        where:
            pkSize | pageable                      || desc
            1      | page(0, 10)                   || 'when simple key and requested first page'
            1      | page(20, 10)                  || 'when simple key and requested third page'
            1      | page(0, 10, order(ASC, 'a'))  || 'when simple key and requested first page with sort'
            3      | page(0, 10)                   || 'when composite key and requested first page'
            3      | page(20, 10, order(ASC, 'a')) || 'when composite key and requested third page with sort'
    }

    def 'selectAll(Sort) wpred: #expected'() {
        when:
			def pred = field('a').eq(number(12)).and(field('b').gt(number(2)))
            def actual = sqlGenerator.selectAll(table, new Sort(orders), pred)
        then:
            actual == "SELECT a, b FROM tabx WHERE ( (A = 12) AND (B > 2) ) ${expected}"
        where:
            orders                              || expected
            [order(ASC, 'a')]                   || 'ORDER BY a ASC'
            [order(DESC, 'a')]                  || 'ORDER BY a DESC'
            [order(ASC, 'a'), order(DESC, 'b')] || 'ORDER BY a ASC, b DESC'
    }


    def 'update() wpred: with #desc'() {
        setup:
			def pred = field('a').eq(number(12)).and(field('b').gt(number(2)))
            table.pkColumns = pkColumns(idsCount)
        when:
            def actual = sqlGenerator.update(table, [x: ANY, y: ANY, z: ANY], pred)
        then:
            actual == "UPDATE tab SET x = ?, y = ?, z = ? WHERE ( ${pkPredicate(idsCount)} ) AND ( (A = 12) AND (B > 2) )"
        where:
            idsCount || desc
            1        || 'simple PK'
            2        || 'composite PK'
    }


    def expectedPaginatedQuery(TableDescription table, Pageable page, SqlPredicate pred) {

        // If sort is not specified, then it should be sorted by primary key columns.
        def sort = page.sort ?: new Sort(ASC, table.pkColumns)

        def firstIndex = page.offset + 1
        def lastIndex = page.offset + page.pageSize
		def psql = pred.toSql(null);
		
        """
            SELECT t2__.* FROM (
                SELECT row_number() OVER (${orderBy(sort)}) AS rn__, t1__.* FROM (
                    SELECT ${table.selectClause} FROM ${table.fromClause}
                ) t1__
            ) t2__ WHERE ( t2__.rn__ BETWEEN ${firstIndex} AND ${lastIndex} ) AND ( ${psql} )
        """.trim().replaceAll(/\s+/, ' ')
    }


    def page(int offset, int limit, Order... orders) {
        def sort = orders.length > 0 ? new Sort(orders) : null
        new PageRequest(offset / limit as int, limit, sort)
    }

    def order(Direction dir, String property) {
        new Order(dir, property)
    }

    def orderBy(Sort sort) {
        'ORDER BY ' + sort.collect { "${it.property} ${it.direction.name()}" }.join(', ')
    }

    static pkColumns(count) {
        (1..count).collect { "id${it}" }*.toString()
    }

    static pkPredicate(count) {
        pkColumns(count).collect { "$it = ?" }.join(' AND ')
    }
}
