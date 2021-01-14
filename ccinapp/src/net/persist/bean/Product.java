package net.persist.bean;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_product")
public class Product {

	@Id
	private Integer id;
	@Column(name = "product_name")
	private String productName;	
	private Boolean status;
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	@Override
	public String toString() {
		return "Product [id=" + id + ", productName=" + productName + ", status=" + status + "]";
	}
	
	
}
