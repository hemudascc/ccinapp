package net.mycomp.common.inapp.tmt;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_inapp_tmt_otp_send")
public class InappTmtOtpSend implements Serializable{

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
	@Column(name = "trx_id")
	private String trxId;
	@Column(name = "send_otp_url")
	private String sendOtpUrl;
	@Column(name = "send_otp_resp")
	private String sendOtpResp;			
	@Column(name = "send_otp")
	private Boolean sendOtp;	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
    public InappTmtOtpSend(){}
	
	public InappTmtOtpSend(boolean status){
		this.status=status;
		this.sendOtp=false;
		this.createTime=new Timestamp(System.currentTimeMillis());
		//this.id=InappTmtConstant.otpSendIdAtomicInteger.incrementAndGet();
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
	
	
	public String getSendOtpUrl() {
		return sendOtpUrl;
	}
	public void setSendOtpUrl(String sendOtpUrl) {
		this.sendOtpUrl = sendOtpUrl;
	}
	public String getSendOtpResp() {
		return sendOtpResp;
	}
	public void setSendOtpResp(String sendOtpResp) {
		this.sendOtpResp = sendOtpResp;
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

public Boolean getSendOtp() {
	return sendOtp;
}

public void setSendOtp(Boolean sendOtp) {
	this.sendOtp = sendOtp;
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