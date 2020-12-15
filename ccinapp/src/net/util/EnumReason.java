package net.util;

public enum EnumReason {
     
	SUCCESS(0,"SUCCESS"),
	INVALID_REQUEST(1,"INVALID_REQUEST"),
	BLOCKED(2,"BLOCKED"),
	ADVERTISER_BLOCK(3,"ADVERTISER_BLOCK"),
	ADVERTISER_DUPLICATE(4,"ADVERTISER_DUPLICATE"),
	ALREADY_SUBSCRIBED(5,"ALREADY_SUBSCRIBED"),
	PIN_SEND_FAILED(6,"PIN_SEND_FAILED"),
	PIN_VALIDATION_FAILED(7,"PIN_VALIDATION_FAILED"),
	OTP_NOT_SUBMITTED(8,"Otp not submitted"),
	MSISDN_NOT_FOUND(9,"MSISDN not Found"),
	INSUFFICIENT_FUNDS(10,"Insufficient Funds"),
	WRONG_PIN(11,"Wrong PIN"),
	MAX_ATTEMPT_EXCEEDED(12,"Max attempt exceeded"),
	MAX_PIN_SENT_PER_MSISDN(13,"Max pin sent per MSISDN in 1min reached"),
	BLACKLISTED(14,"Number not allowed. Blacklisted for all services"),
	MSISDN_OTP_MISSSING(15,"Msisdn or OTP missing."),
	FAIL(16,"FAIL"),
	;
	
	
	public final int sttausCode;
	public final String reason;
	EnumReason(int statusCode,final String reason){
		this.sttausCode=statusCode;
		this.reason=reason;
	}
	
	public static EnumReason findReason(String reason){
		for(EnumReason enumReason:values()){
			if(enumReason.reason.equalsIgnoreCase(reason)){
				return enumReason;
			}
		}
		return FAIL;
	}
	
	public static EnumReason findCuase(String errorMessage){
		try{
		for(EnumReason enumReason:values()){
			if(errorMessage.contains(enumReason.reason)){
				return enumReason;
			}
		}
		}catch(Exception ex){
			
		}
		return FAIL;
	}
	
	
}
