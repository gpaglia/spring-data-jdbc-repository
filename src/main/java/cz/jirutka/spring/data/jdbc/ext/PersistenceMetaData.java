package cz.jirutka.spring.data.jdbc.ext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import static java.util.stream.Collectors.*;

public class PersistenceMetaData {
	private String schemaName;
	private DataSource dataSource;
	private Map<Class<?>, ObjectMetaData> objectsMetaData;
	
	public PersistenceMetaData(DataSource dataSource, String schemaName, List<ObjectMetaData> objects) {
		this.schemaName = schemaName;
		this.dataSource = dataSource;
		this.objectsMetaData = objects.stream().collect(toMap(o -> o.getClass(), o -> o));
	}

	public String getSchemaName() {
		return schemaName;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public Map<Class<?>, ObjectMetaData> getObjectsMetaData() {
		return new HashMap<>(objectsMetaData);
	}
	
	
}
