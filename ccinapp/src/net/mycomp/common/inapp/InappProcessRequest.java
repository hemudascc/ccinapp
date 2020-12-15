package net.mycomp.common.inapp;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;

import net.persist.bean.VWCampaignDetail;
import net.util.EnumReason;
import net.util.JpaConverterJson;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_inapp_process_request")
public class InappProcessRequest implements Serializable{

	@Id
	private Integer id;
	private String action;	
	private String msisdn;
	private Integer cmpid;	
	@Column(name = "service_id")
	private Integer serviceId;	
	private String pin;
	@Column(name = "ip")
	private String ip;
	private String txid;
	@Column(name = "cg_token")
	private String cgToken;
	@Column(name = "query_str")
	private String queryStr;	
	private Boolean success;	
	@Column(name = "pin_request_count")
	private Integer pinRequestCount;	
	@Column(name = "send_pin_count")
	private Integer sendPinCount;	
	@Column(name = "pin_validation_request_count")
	private Integer pinValidationRequestCount;	
	@Column(name = "pin_validate_count")
	private Integer pinValidateCount;	
	@Column(name = "pin_validate_amount")
	private Double pinValidateAmount;	
	@Column(name = "status_check_count")
	private Integer statusCheckCount;	
	@Column(name = "conversion_count")
	private Integer conversionCount;		
	@Column(name = "conversion_send_to_adenetwork")
	private Boolean conversionSendToAdenetwork;	
	@Column(name="response_object")
	@Convert(converter=JpaConverterJson.class)
	private Object responseObject;
	@Column(name="advertiser_api_comment")
	private String advertiserApiComment;	
	@Column(name="reason")
	private String reason;		
	@Column(name="advertiser_api_request")
	private String advertiserApiRequest;
	@Column(name="advertiser_api_response_code")
	private Integer advertiserApiResponseCode;	
	@Column(name="advertiser_api_response")
	private String advertiserApiResponse;	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	@Transient		
	public transient  VWCampaignDetail vwCampaignDetail;
	
	@Transient		
	public transient  EnumReason enumReason;
	
	@Transient
	private transient  Map<String,String> requestMap;	
	
	public InappProcessRequest(){		
	}
   
	public InappProcessRequest(boolean status){
		
		this.id=InappConstant.inappProcessRequestId.incrementAndGet();
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.status=status;
		this.success=false;
		this.conversionSendToAdenetwork=false;
		this.pinRequestCount=0;
		this.sendPinCount=0;
		this.pinValidationRequestCount=0;
		this.pinValidateCount=0;
		this.pinValidateAmount=0d;
		this.statusCheckCount=0;
		this.conversionCount=0;
		this.conversionSendToAdenetwork=false;
		
	}
	
	
public String toString() {
		
        Field[] fields = this.getClass().getDeclaredFields();
        String str = this.getClass().getName();
        try {
            for (Field field : fields) {
                str += field.getName() + "=" + field.get(this) + ",";
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        } catch (IllegalAccessException ex) {
            System.out.println(ex);
        }
        return str;
    }
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public Integer getCmpid() {
		return cmpid;
	}
	public void setCmpid(Integer cmpid) {
		this.cmpid = cmpid;
	}

	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	
	

	public Map<String, String> getRequestMap() {
		return requestMap;
	}
	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getCgToken() {
		return cgToken;
	}

	public void setCgToken(String cgToken) {
		this.cgToken = cgToken;
	}

	public Boolean getConversionSendToAdenetwork() {
		return conversionSendToAdenetwork;
	}

	public void setConversionSendToAdenetwork(Boolean conversionSendToAdenetwork) {
		this.conversionSendToAdenetwork = conversionSendToAdenetwork;
	}

	public Integer getPinRequestCount() {
		return pinRequestCount;
	}

	public void setPinRequestCount(Integer pinRequestCount) {
		this.pinRequestCount = pinRequestCount;
	}

	public Integer getSendPinCount() {
		return sendPinCount;
	}

	public void setSendPinCount(Integer sendPinCount) {
		this.sendPinCount = sendPinCount;
	}

	public Integer getPinValidationRequestCount() {
		return pinValidationRequestCount;
	}

	public void setPinValidationRequestCount(Integer pinValidationRequestCount) {
		this.pinValidationRequestCount = pinValidationRequestCount;
	}

	

	public Integer getConversionCount() {
		return conversionCount;
	}

	public void setConversionCount(Integer conversionCount) {
		this.conversionCount = conversionCount;
	}

	public Integer getPinValidateCount() {
		return pinValidateCount;
	}

	public void setPinValidateCount(Integer pinValidateCount) {
		this.pinValidateCount = pinValidateCount;
	}

	public Integer getStatusCheckCount() {
		return statusCheckCount;
	}

	public void setStatusCheckCount(Integer statusCheckCount) {
		this.statusCheckCount = statusCheckCount;
	}

	public Double getPinValidateAmount() {
		return pinValidateAmount;
	}

	public void setPinValidateAmount(Double pinValidateAmount) {
		this.pinValidateAmount = pinValidateAmount;
	}

	public String getAdvertiserApiComment() {
		return advertiserApiComment;
	}

	public void setAdvertiserApiComment(String advertiserApiComment) {
		this.advertiserApiComment = advertiserApiComment;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAdvertiserApiRequest() {
		return advertiserApiRequest;
	}

	public void setAdvertiserApiRequest(String advertiserApiRequest) {
		this.advertiserApiRequest = advertiserApiRequest;
	}

	public Integer getAdvertiserApiResponseCode() {
		return advertiserApiResponseCode;
	}

	public void setAdvertiserApiResponseCode(Integer advertiserApiResponseCode) {
		this.advertiserApiResponseCode = advertiserApiResponseCode;
	}

	public String getAdvertiserApiResponse() {
		return advertiserApiResponse;
	}

	public void setAdvertiserApiResponse(String advertiserApiResponse) {
		this.advertiserApiResponse = advertiserApiResponse;
	}
	
}
