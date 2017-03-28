package cz.jirutka.spring.data.jdbc.ext;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration()
public class Config {
	@Bean DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .addScript("ext/ext_schema_derby.sql")
            .setType(EmbeddedDatabaseType.DERBY)
            .build();
    }
}
