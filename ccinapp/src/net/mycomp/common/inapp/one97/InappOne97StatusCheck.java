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
@Table(name = "tb_inapp_one97_status_check")
public class InappOne97StatusCheck implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="action")
	private String action;
	@Column(name = "cmpid")
	private Integer cmpId;	
	@Column(name = "request_id")
	private Integer requestId;
	@Column(name = "cg_token")
	private String cgToken;	
	
	private String msisdn;
	
	@Column(name = "status_check_url")
	private String statusCheckUrl;
	@Column(name = "status_check_resp")
	private String statusCheckResp;		
	@Column(name = "sub_status")
	private String subStatus;	
	@Column(name = "description")
	private String description;
	@Column(name = "trx_id")
	private String trxId;
	@Column(name = "charge_status")
	private String chargeStatus;
	@Column(name="retry_counter")	
	private Integer retryCounter;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public InappOne97StatusCheck(){}
	
	public InappOne97StatusCheck(boolean status){
		this.status=status;
		
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
	
	public String getStatusCheckUrl() {
		return statusCheckUrl;
	}
	public void setStatusCheckUrl(String statusCheckUrl) {
		this.statusCheckUrl = statusCheckUrl;
	}
	public String getStatusCheckResp() {
		return statusCheckResp;
	}
	public void setStatusCheckResp(String statusCheckResp) {
		this.statusCheckResp = statusCheckResp;
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

public String getAction() {
	return action;
}

public void setAction(String action) {
	this.action = action;
}

public String getSubStatus() {
	return subStatus;
}

public void setSubStatus(String subStatus) {
	this.subStatus = subStatus;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getTrxId() {
	return trxId;
}

public void setTrxId(String trxId) {
	this.trxId = trxId;
}


public Integer getRetryCounter() {
	return retryCounter;
}

public void setRetryCounter(Integer retryCounter) {
	this.retryCounter = retryCounter;
}

public String getChargeStatus() {
	return chargeStatus;
}

public void setChargeStatus(String chargeStatus) {
	this.chargeStatus = chargeStatus;
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
	


}
