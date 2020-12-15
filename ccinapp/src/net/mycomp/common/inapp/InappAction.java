package net.mycomp.common.inapp;

public enum InappAction {
	
	SEND_PIN("SEND_PIN"),
	PIN_VALIDATION("PIN_VALIDATION"),
	STATUS_CHECK("STATUS_CHECK"),
	PORTAL_URL("PORTAL_URL"),
	DCT("DCT")
	;
	
   String action;
  InappAction(String action){
	  this.action=action;
  }
public String getAction() {
	return action;
}
public void setAction(String action) {
	this.action = action;
}
}
