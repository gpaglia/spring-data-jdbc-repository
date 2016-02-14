/*
 * Copyright 2016 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nurkiewicz.jdbcrepository

import com.nurkiewicz.jdbcrepository.config.AbstractTestConfig
import com.zaxxer.hikari.HikariDataSource
import groovy.transform.AnnotationCollector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement
import spock.lang.Requires

import javax.sql.DataSource

import static com.nurkiewicz.jdbcrepository.TestUtils.*

@MariaDbTestContext
class MariaDbJdbcRepositoryCompoundPkIT extends JdbcRepositoryCompoundPkIT {}

@MariaDbTestContext
class MariaDbJdbcRepositoryGeneratedKeyIT extends JdbcRepositoryGeneratedKeyIT {}

@MariaDbTestContext
class MariaDbJdbcRepositoryManualKeyIT extends JdbcRepositoryManualKeyIT {}

@MariaDbTestContext
class MariaDbJdbcRepositoryManyToOneIT extends JdbcRepositoryManyToOneIT {}

@AnnotationCollector
@Requires({ env('CI') ? env('DB').equals('mariadb') : isPortInUse('localhost', 3306) })
@ContextConfiguration(classes = MariaDbTestConfig)
@interface MariaDbTestContext {}

@Configuration
@EnableTransactionManagement
class MariaDbTestConfig extends AbstractTestConfig {

    @Bean(destroyMethod = 'shutdown')
    def DataSource dataSource() {
        new HikariDataSource(
            dataSourceClassName: 'org.mariadb.jdbc.MariaDbDataSource',
            dataSourceProperties: [
                serverName: 'localhost',
                portNumber: 3306,
                user: prop('mariadb.user', 'root'),
                password: prop('mariadb.password', ''),
                databaseName: DATABASE_NAME,
            ]
        )
    }
}
