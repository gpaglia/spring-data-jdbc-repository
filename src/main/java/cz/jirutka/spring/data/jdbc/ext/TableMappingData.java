package cz.jirutka.spring.data.jdbc.ext;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableMappingData {
	private static final Logger LOG = LoggerFactory.getLogger(TableMappingData.class);
	
	private String schemaName;
	private String tableName;
    private Map<String, ColumnInfo> columns = new HashMap<>();
    private List<String> pkColumns = new ArrayList<>();
    private Map<String, FkInfo> fkList = new HashMap<>();
    
    public static class ColumnInfo {
    	private String name;
    	private String typeName;
    	private int type;
    	private int size;
    	private int decimalDigits;
    	
		public ColumnInfo(String name, String typeName, int type, int size, int decimalDigits) {
			super();
			this.name = name;
			this.typeName = typeName;
			this.type = type;
			this.size = size;
			this.decimalDigits = decimalDigits;
		}
    	

		public String getName() {
			return name;
		}

		public String getTypeName() {
			return typeName;
		}

		public int getType() {
			return type;
		}

		public int getSize() {
			return size;
		}

		public int getDecimalDigits() {
			return decimalDigits;
		}


		@Override
		public String toString() {
			return "ColumnInfo [name=" + name + ", typeName=" + typeName + ", type=" + type + ", size=" + size
					+ ", decimalDigits=" + decimalDigits + "]";
		}
		

    }
    
    public static class FkInfo {
    	private String tableName;			// the other table
    	private List<String> pkColumns;		// the pk in the other table
    	private List<String> fkColumns;		// the fk in this table
    	
		public FkInfo(String tableName, List<String> pkColumns, List<String> fkColumns) {
			super();
			this.tableName = tableName;
			this.pkColumns = new ArrayList<>(pkColumns);
			this.fkColumns = new ArrayList<>(fkColumns);
		}

		public String getTableName() {
			return tableName;
		}

		public List<String> getPkColumns() {
			return new ArrayList<>(pkColumns);
		}

		public List<String> getFkColumns() {
			return new ArrayList<>(fkColumns);
		}

		@Override
		public String toString() {
			return "FkInfo [tableName=" + tableName + ", pkColumns=" + pkColumns + ", fkColumns=" + fkColumns + "]";
		}
    	
    }
   
    TableMappingData(String schemaName, String tableName, DataSource dataSource) throws SQLException {
    	this.schemaName = schemaName;
    	this.tableName = tableName;
    	initFromDbMetaData(schemaName, tableName, dataSource);
    }
    
    public String getSchemaName() {
    	return this.schemaName;
    }
    
    public String getTableName() {
    	return this.tableName;
    }
    
    public List<ColumnInfo> getColumnInfos() {
    	return new ArrayList<>(columns.values());
    }
    
    public List<String> getPkColumns() {
    	return new ArrayList<>(pkColumns);
    }
    
    public List<FkInfo> getFkInfos() {
    	return new ArrayList<>(fkList.values());
    }
    
    private void initFromDbMetaData(String schemaName, String tableName, DataSource ds) throws SQLException {
    	DatabaseMetaData meta = ds.getConnection().getMetaData();
    	
    	try (ResultSet cols = meta.getColumns(null, schemaName,  tableName, null)) {
    		while(cols.next()) {
    			ColumnInfo ci = new ColumnInfo(
    					cols.getString("COLUMN_NAME"),
    					cols.getString("TYPE_NAME"),
    					cols.getInt("DATA_TYPE"),
    					cols.getInt("COLUMN_SIZE"),
    					cols.getInt("DECIMAL_DIGITS")
    			);
    			columns.put(ci.name, ci);
    			LOG.debug("Found column " + ci.toString());
    		}
    	}
    	
    	try (ResultSet pks = meta.getPrimaryKeys(null, schemaName, tableName)) {
    		TreeMap<Integer, String> map = new TreeMap<>();
    		while(pks.next()) {
    			map.put(pks.getInt("KEY_SEQ"), pks.getString("COLUMN_NAME"));
    		}
    		map.forEach((k, v) -> this.pkColumns.add(v));
    		LOG.debug("Found PK " + pkColumns.toString());
    	}
    	
    	try (ResultSet fks = meta.getImportedKeys(null, schemaName, tableName)) {
    		TreeMap<Integer, String> pkmap = null; 
    		TreeMap<Integer, String> fkmap = null;
    		
    		String tn = null;
    		
    		while(fks.next()) {
    			String tname = fks.getString("PKTABLE_NAME");
    			if (tn == null || !tname.equals(tn)) {
    				if (tn != null) {
    					fkList.put(tn, new FkInfo(tn, new ArrayList<>(pkmap.values()), new ArrayList<>(fkmap.values())));
    					LOG.debug("Found FK " + fkList.get(tn).toString());
    				} else {
    					tn = tname;
    				}
    				pkmap = new TreeMap<>();
    				fkmap = new TreeMap<>();
    			}
    			pkmap.put(fks.getInt("KEY_SEQ"), fks.getString("PKCOLUMN_NAME"));
    			fkmap.put(fks.getInt("KEY_SEQ"), fks.getString("FKCOLUMN_NAME"));
    		}
    		
    		if (tn != null) {
				fkList.put(tn, new FkInfo(tn, new ArrayList<>(pkmap.values()), new ArrayList<>(fkmap.values())));
				LOG.debug("Found FK " + fkList.get(tn).toString());
			}
    	}
    	
    }

}
