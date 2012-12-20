package org.springframework.data.jdbc;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/20/12, 10:56 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AbstractJdbcRepositoryTestConfig.class)
@Transactional
public abstract class AbstractIntegrationTest {
}
