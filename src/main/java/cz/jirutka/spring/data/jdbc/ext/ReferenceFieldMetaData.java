package cz.jirutka.spring.data.jdbc.ext;

import cz.jirutka.spring.data.jdbc.ext.ReferenceType;

public class ReferenceFieldMetaData extends AbstractFieldMetaData {
	private Class<?> refObjectClass;
	private String refFieldName;
	private ReferenceType referenceType;
	
	public ReferenceFieldMetaData(ObjectMetaData objectMetaData, String fieldName, ReferenceType referenceType, Class<?> refObjectClass, String refFieldName) {
		super(objectMetaData, fieldName);
		this.refObjectClass = refObjectClass;
		this.refFieldName = refFieldName;
		this.referenceType = referenceType;
	}
	public Class<?> getRefObjectClass() {
		return refObjectClass;
	}
	public String getRefFieldName() {
		return refFieldName;
	}
	public ReferenceType getReferenceType() {
		return referenceType;
	}
}
