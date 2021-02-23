package net.mycomp.common.inapp;

import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.Aggregator;
import net.persist.bean.CampaignDetails;
import net.persist.bean.Country;
import net.persist.bean.Operator;
import net.persist.bean.Product;
import net.persist.bean.TrafficRouting;

public class InappAutomatedProcessRequest{

	private Integer advertiserId;
	private Integer id;
	private Integer serviceId;
	private Integer productId;
	private String  operatorName;
	private Integer campId;
	private Boolean status;
	private String  pinGenerationUrl;
	private String  pinVerificationUrl;
	private String  statusCheckUrl;
	private String  portalUrl;
	private Double  pricePoint;
	private Integer validity;
	private String  authorization;
	private String  resendPinUrl;
	private String  pinSendUrl;
	private String  pinValidationUrl;
	private String  dctUrl;
	private Double  amount;
	private String  operatorDetail;
	private String  checkSubUrl;
	private String  portalUrl2;

	private CampaignDetails campaignDetails;
	private Country country;
	private Aggregator aggregator;
	private Operator operator;
	private Product product;
	private net.persist.bean.Service service;
	private AdnetworkOperatorConfig adnetworkOperatorConfig;
	private TrafficRouting trafficRouting;
	
	
	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Integer getCampId() {
		return campId;
	}

	public void setCampId(Integer campId) {
		this.campId = campId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPinGenerationUrl() {
		return pinGenerationUrl;
	}

	public void setPinGenerationUrl(String pinGenerationUrl) {
		this.pinGenerationUrl = pinGenerationUrl;
	}

	public String getPinVerificationUrl() {
		return pinVerificationUrl;
	}

	public void setPinVerificationUrl(String pinVerificationUrl) {
		this.pinVerificationUrl = pinVerificationUrl;
	}

	public String getStatusCheckUrl() {
		return statusCheckUrl;
	}

	public void setStatusCheckUrl(String statusCheckUrl) {
		this.statusCheckUrl = statusCheckUrl;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public Double getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getResendPinUrl() {
		return resendPinUrl;
	}

	public void setResendPinUrl(String resendPinUrl) {
		this.resendPinUrl = resendPinUrl;
	}

	public String getPinSendUrl() {
		return pinSendUrl;
	}

	public void setPinSendUrl(String pinSendUrl) {
		this.pinSendUrl = pinSendUrl;
	}

	public String getPinValidationUrl() {
		return pinValidationUrl;
	}

	public void setPinValidationUrl(String pinValidationUrl) {
		this.pinValidationUrl = pinValidationUrl;
	}

	public String getDctUrl() {
		return dctUrl;
	}

	public void setDctUrl(String dctUrl) {
		this.dctUrl = dctUrl;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOperatorDetail() {
		return operatorDetail;
	}

	public void setOperatorDetail(String operatorDetail) {
		this.operatorDetail = operatorDetail;
	}

	public String getCheckSubUrl() {
		return checkSubUrl;
	}

	public void setCheckSubUrl(String checkSubUrl) {
		this.checkSubUrl = checkSubUrl;
	}

	public String getPortalUrl2() {
		return portalUrl2;
	}

	public void setPortalUrl2(String portalUrl2) {
		this.portalUrl2 = portalUrl2;
	}

	public CampaignDetails getCampaignDetails() {
		return campaignDetails;
	}

	public void setCampaignDetails(CampaignDetails campaignDetails) {
		this.campaignDetails = campaignDetails;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Aggregator getAggregator() {
		return aggregator;
	}

	public void setAggregator(Aggregator aggregator) {
		this.aggregator = aggregator;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public net.persist.bean.Service getService() {
		return service;
	}

	public void setService(net.persist.bean.Service service) {
		this.service = service;
	}

	public AdnetworkOperatorConfig getAdnetworkOperatorConfig() {
		return adnetworkOperatorConfig;
	}

	public void setAdnetworkOperatorConfig(AdnetworkOperatorConfig adnetworkOperatorConfig) {
		this.adnetworkOperatorConfig = adnetworkOperatorConfig;
	}

	public TrafficRouting getTrafficRouting() {
		return trafficRouting;
	}

	public void setTrafficRouting(TrafficRouting trafficRouting) {
		this.trafficRouting = trafficRouting;
	}


	
}
