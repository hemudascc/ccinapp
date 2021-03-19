package net.persist.bean;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.persistence.*;


/**
 * The persistent class for the tb_adnetwork_operator_config database table.
 * 
 */
@Entity
@Table(name="tb_adnetwork_operator_config")
//@NamedQuery(name="TbAdnetworkOperatorConfig.findAll", query="SELECT t FROM AdnetworkOperatorConfig t")
@NamedQuery(name = "AdnetworkOperatorConfig.findAdnetworkOperatorConfigById", 
query = "SELECT b FROM AdnetworkOperatorConfig b where b.adnetworkOperatorConfigId=:adnetworkOperatorConfigId ")

public class AdnetworkOperatorConfig  {
	
	@Id
	@Column(name="adnetwork_operator_config_id", unique = true, nullable = false)
	@GeneratedValue
	private Integer adnetworkOperatorConfigId;

	
	@Column(name="ad_network_id")
	private Integer adNetworkId;

	@Column(name="operator_id")
	private Integer operatorId;
	
	@Column(name="skip_number")
	private Integer skipNumber;
	@Column(name="status")
	private Boolean status;
	
	@Column(name="op_cpa_value")
	private Double opCpaValue;
	
	@Column(name="duplicate_block_status")
	private Boolean duplicateBlockStatus=Boolean.FALSE;
	
	
	@Column(name="ad_block_status")
	private Boolean adBlockStatus;
	
	@Transient
	public AtomicIntegerArray atomicActSkipNumber=new AtomicIntegerArray(3);
	
	public Integer getAdnetworkOperatorConfigId() {
		return adnetworkOperatorConfigId;
	}



	public void setAdnetworkOperatorConfigId(Integer adnetworkOperatorConfigId) {
		this.adnetworkOperatorConfigId = adnetworkOperatorConfigId;
	}



	public Integer getAdNetworkId() {
		return adNetworkId;
	}



	public void setAdNetworkId(Integer adNetworkId) {
		this.adNetworkId = adNetworkId;
	}



	public Integer getOperatorId() {
		return operatorId;
	}



	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}



	public Integer getSkipNumber() {
		return skipNumber;
	}



	public void setSkipNumber(Integer skipNumber) {
		this.skipNumber = skipNumber;
	}



	public Boolean getStatus() {
		return status;
	}



	public void setStatus(Boolean status) {
		this.status = status;
	}



	public Double getOpCpaValue() {
		return opCpaValue;
	}



	public void setOpCpaValue(Double opCpaValue) {
		this.opCpaValue = opCpaValue;
	}



	public Boolean getDuplicateBlockStatus() {
		return duplicateBlockStatus;
	}



	public void setDuplicateBlockStatus(Boolean duplicateBlockStatus) {
		this.duplicateBlockStatus = duplicateBlockStatus;
	}


	public AtomicIntegerArray getAtomicActSkipNumber() {
		return atomicActSkipNumber;
	}



	public void setAtomicActSkipNumber(AtomicIntegerArray atomicActSkipNumber) {
		this.atomicActSkipNumber = atomicActSkipNumber;
	}



	public Boolean getAdBlockStatus() {
		return adBlockStatus;
	}



	public void setAdBlockStatus(Boolean adBlockStatus) {
		this.adBlockStatus = adBlockStatus;
	}



	
	
	
	}