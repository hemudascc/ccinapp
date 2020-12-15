package net.mycomp.publisher;

public enum EnumMobilartsPublisherResponseStatus {
    
	SUCCCES("0","ok"),
	ERROR("1",null),
	INVALID_MSISDN("2","Invalid MSISDN"),
	USER_ALREADY_SUBSCRIBED("3","User already subscribed"),
	INSUFFICIENT_FUNDS("4","Insufficient Funds"),
	BLACKLISTED_MSISDN("5","Blacklisted MSISDN"),
	INVALID_PINCODE("6","Invalid Pincode"),;
	
	 String status;
	 String description;
	 
	 EnumMobilartsPublisherResponseStatus(String status,String description){
		 this.status=status;
		 this.description=description;
	 }

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}
}
