package com.djedra.entity.tableType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.djedra.entity.IEntityHead;

import lombok.Data;

@Data
@Entity
//@Table(name = "example_table")
public class Example{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String tableeee;
	@Column
	private String no;
	@Column
	private String effectiveDate;
	@Column
	private String tradingDate;
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "example", fetch = FetchType.EAGER)
//	private List<RateExample> rates = null;
}
