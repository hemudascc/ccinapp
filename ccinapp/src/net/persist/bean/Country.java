package net.persist.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_country")
public class Country {

	@Id
	@Column(name = "Id")
	@GeneratedValue
	private Integer Id;
	private String name;	
	private Boolean status;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Country [Id=" + Id + ", name=" + name + ", status=" + status + "]";
	}


}
