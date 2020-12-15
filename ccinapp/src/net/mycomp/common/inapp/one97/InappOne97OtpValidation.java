package net.mycomp.common.inapp.one97;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_inapp_one97_otp_validation")
public class InappOne97OtpValidation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "cmpid")
	private Integer cmpId;
	@Column(name = "request_id")
	private Integer requestId;
	@Column(name = "cg_token")
	private String cgToken;		
	private String msisdn;
	private String pin;
	@Column(name = "trx_id")
	private String trxId;
	@Column(name = "request_query_str")
	private String requestQueryStr;
	@Column(name = "pin_validation_url")
	private String pinValidationUrl;
	@Column(name = "pin_validation_response")
	private String pinValidationResponse;	
	@Column(name = "pin_validate")
	private Boolean pinValidate;	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
public InappOne97OtpValidation(){}
	
	public InappOne97OtpValidation(boolean status){
		this.status=status;
		this.pinValidate=false;
		this.createTime=new Timestamp(System.currentTimeMillis());
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public String getRequestQueryStr() {
		return requestQueryStr;
	}
	public void setRequestQueryStr(String requestQueryStr) {
		this.requestQueryStr = requestQueryStr;
	}
	public String getPinValidationUrl() {
		return pinValidationUrl;
	}
	public void setPinValidationUrl(String pinValidationUrl) {
		this.pinValidationUrl = pinValidationUrl;
	}
	public String getPinValidationResponse() {
		return pinValidationResponse;
	}
	public void setPinValidationResponse(String pinValidationResponse) {
		this.pinValidationResponse = pinValidationResponse;
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

public Integer getCmpId() {
	return cmpId;
}

public void setCmpId(Integer cmpId) {
	this.cmpId = cmpId;
}

public Boolean getPinValidate() {
	return pinValidate;
}

public void setPinValidate(Boolean pinValidate) {
	this.pinValidate = pinValidate;
}

public Integer getRequestId() {
	return requestId;
}

public void setRequestId(Integer requestId) {
	this.requestId = requestId;
}

public String getCgToken() {
	return cgToken;
}

public void setCgToken(String cgToken) {
	this.cgToken = cgToken;
}

public String getTrxId() {
	return trxId;
}

public void setTrxId(String trxId) {
	this.trxId = trxId;
}
}
