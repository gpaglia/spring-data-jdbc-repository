package cz.jirutka.spring.data.jdbc.ext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AbstractFieldMetaData {
	private ObjectMetaData objectMetaData;
	private String fieldName;
	private Class<?> fieldClass;
	private Method getter;
	
	public AbstractFieldMetaData(ObjectMetaData objectMetaData, String fieldName) {
		super();
		this.objectMetaData = objectMetaData;
		this.fieldName = fieldName;
		getFieldDataByReflection(objectMetaData.getObjectClass(), fieldName);
	}
	
	public ObjectMetaData getObjectMetaData() {
		return objectMetaData;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public Class<?> getFieldClass() {
		return fieldClass;
	}
	
	public Object getValue(Object obj) throws MetaDataException {
		try {
			return this.getter.invoke(obj);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new MetaDataException(e);
		}
	}
	
	private void getFieldDataByReflection(Class<?> objectClass, String fn) {
		String getterName = "get" + Character.toUpperCase(fn.charAt(0)) + fn.substring(1);
		String getterName2 = "is" + getterName.substring(3);
		
		// String setterName = "s" + getterName.substring(1);
		
		// look for a public method with given name and no args
		// start with public methods (incl inherited
		Method m;
		try {
			m = objectClass.getMethod(getterName, new Class<?>[] {});
		} catch (NoSuchMethodException e) {
			m = null;
		}
	
		if (m == null) {
			try {
				m = objectClass.getDeclaredMethod(getterName2, new Class<?>[] {});
			} catch (NoSuchMethodException e) {
				m = null;
			}
		}

		// TODO: Add lookup for field name
		if (m == null) {
			throw new MetaDataException("No getter " + getterName);
		}
		
		this.fieldClass = m.getReturnType();
		this.getter = m; 
		
	}
}
