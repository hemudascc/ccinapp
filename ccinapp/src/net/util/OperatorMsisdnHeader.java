package net.util;

public enum OperatorMsisdnHeader {

	QATAR_OPERATOR_ID(0,"",false),
	;
	
	OperatorMsisdnHeader(int opId,String msisdnHeaderName,boolean opIdentifierHeader){
		this.opId=opId;
		this.msisdnHeaderName=msisdnHeaderName;
		this.opIdentifierHeader= opIdentifierHeader;
	}
	
	private int opId;
	private String msisdnHeaderName;
	private boolean opIdentifierHeader;
	
	public int getOpId() {
		return opId;
	}
	public void setOpId(int opId) {
		this.opId = opId;
	}
	public String getMsisdnHeaderName() {
		return msisdnHeaderName;
	}
	public void setMsisdnHeaderName(String msisdnHeaderName) {
		this.msisdnHeaderName = msisdnHeaderName;
	}
	public boolean isOpIdentifierHeader() {
		return opIdentifierHeader;
	}
	public void setOpIdentifierHeader(boolean opIdentifierHeader) {
		this.opIdentifierHeader = opIdentifierHeader;
	}
	
}
