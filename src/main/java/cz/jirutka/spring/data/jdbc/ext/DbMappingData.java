package cz.jirutka.spring.data.jdbc.ext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbMappingData {
	private static final Logger LOG = LoggerFactory.getLogger(DbMappingData.class);
	
	private DataSource dataSource;
	private String schemaName;
	
	private Map<String, TableMappingData> tableMappings = new HashMap<>();
	
	DbMappingData(DataSource dataSource, String schemaName, String[] tableNames) throws SQLException {
		this(dataSource, schemaName, new HashSet<>(Arrays.asList(tableNames)));
	}

	
	DbMappingData(DataSource dataSource, String schemaName, Set<String> tableNames) throws SQLException {
		this.dataSource = dataSource;
		this.schemaName = schemaName;
		readDbMetadata(schemaName, tableNames);
	}
	
	public String getSchemaName() {
		return this.schemaName;
	}
	
	public Map<String, TableMappingData> getTableMappings() {
		return new HashMap<>(tableMappings);
	}
	
	//
	
	private void readDbMetadata(String schemaName, Set<String> tableNames) throws SQLException {
		
		try (ResultSet rs = dataSource
				.getConnection()
				.getMetaData()
				.getTables(null, schemaName, "%", new String[] { "TABLE"})) {

			while (rs.next()) {
				String t = rs.getString("TABLE_NAME");
				String sch = rs.getString("TABLE_SCHEM");
				if (tableNames.contains(t)) {
					LOG.debug("Getting mapping for table {} schema {}", t, sch);
					TableMappingData td = new TableMappingData(schemaName, t, dataSource);	
					tableMappings.put(t,  td);
				}
			}

		}

	}
}
