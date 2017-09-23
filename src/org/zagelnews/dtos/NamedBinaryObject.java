package org.zagelnews.dtos;

public class NamedBinaryObject {
	
	private byte [] binaryObj;
	private String binaryName;
	
	public NamedBinaryObject(byte[] binaryObj, String binaryName) {
		this.binaryObj = binaryObj;
		this.binaryName = binaryName;
	}
	
	public byte[] getBinaryObj() {
		return binaryObj;
	}
	public void setBinaryObj(byte[] binaryObj) {
		this.binaryObj = binaryObj;
	}
	public String getBinaryName() {
		return binaryName;
	}
	public void setBinaryName(String binaryName) {
		this.binaryName = binaryName;
	}
	
}
