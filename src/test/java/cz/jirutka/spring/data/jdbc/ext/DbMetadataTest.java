package cz.jirutka.spring.data.jdbc.ext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cz.jirutka.spring.data.jdbc.ext.ObjectMetaData.*;
import cz.jirutka.spring.data.jdbc.ext.fixtures.EUser;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Config.class)
public class DbMetadataTest {
	
	private static final String[] TABLES = {
			"USERS",
			"COMMENTS",
			"BOARDING_PASS"
	};
	
	@Autowired
	private DataSource dataSource;

	@Test()
	public void readDbMetadataTest() {
		try {
			DbMappingData dbMapping = new DbMappingData(dataSource, null, TABLES);
			
			assertThat(dbMapping.getTableMappings().keySet(), Matchers.containsInAnyOrder(TABLES));
		} catch (SQLException e) {
			fail(e.toString());
		}
	}
	
	@Test()
	public void readObjectMetaDataTest() {
		ObjectMetaData userMD = new ObjectMetaData(
				EUser.class,
				"USERS",
				Arrays.asList(
						new FieldInfo("userName", FieldType.ID, "user_name"),
						new FieldInfo("dateOfBirth", FieldType.BASIC, "date_of_birth"),
						new FieldInfo("reputation", FieldType.BASIC, "reputation"),
						new FieldInfo("enabled", FieldType.BASIC, "enabled")
				),
				new ArrayList<ReferenceInfo>()
		);
		
		assertThat(userMD.getTableName(), is(equalTo("USERS")));
		assertThat(userMD.getObjectClass(), is(equalTo(EUser.class)));
		assertThat(userMD.getFieldsMetaData().entrySet().size(), is(equalTo(4)));
		assertThat(userMD.getFieldsMetaData().get("userName").getFieldClass(), is(equalTo(String.class)));
		assertThat(userMD.getFieldsMetaData().get("dateOfBirth").getFieldClass(), is(equalTo(Date.class)));
		assertThat(userMD.getFieldsMetaData().get("reputation").getFieldClass(), is(equalTo(int.class)));
		assertThat(userMD.getFieldsMetaData().get("enabled").getFieldClass(), is(equalTo(boolean.class)));
		
		Date d = new Date();
		EUser u = new EUser("name", d, 12, true);
		
		assertThat(userMD.getFieldsMetaData().get("userName").getValue(u), is(equalTo("name")));
		assertThat(userMD.getFieldsMetaData().get("dateOfBirth").getValue(u), is(equalTo(d)));
		assertThat(userMD.getFieldsMetaData().get("reputation").getValue(u), is(equalTo(12)));
		assertThat(userMD.getFieldsMetaData().get("enabled").getValue(u), is(equalTo(true)));
		
		
	}
}
