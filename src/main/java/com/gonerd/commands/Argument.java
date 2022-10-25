package com.gonerd.commands;

public class Argument {

	private final String missingMessage;
	private final DataType dataType;
	private String value;
	private String allowOf;
	private float fValue;
	private int iValue;

	public Argument(String missingMessage, DataType dataType, String allowOf, String value) {
		this(missingMessage, dataType, value);
		this.allowOf = allowOf;
	}

	public Argument(String value) {
		this(null, null, value);
	}

	public Argument(String missingMessage, DataType dataType) {
		this(missingMessage, dataType, null);
		this.allowOf = "";
	}

	public Argument(String missingMessage, DataType dataType, String value) {
		this.value = value;
		this.missingMessage = missingMessage;
		this.dataType = dataType;
	}

	public String getAllowOf() {
		return allowOf;
	}

	public String getMissingMessage() {
		return missingMessage;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public float getFValue() {
		return fValue;
	}

	public void setFValue(float fValue) {
		this.fValue = fValue;
	}

	public int getIValue() {
		return iValue;
	}

	public void setIValue(int iValue) {
		this.iValue = iValue;
	}

}
