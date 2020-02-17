package com.djedra.entity.tableType;

import java.util.List;

import com.djedra.entity.IEntityHead;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Example implements IEntityHead {

	private String table;
	@JsonProperty("no")
	private String id;
	private String effectiveDate;
	private String tradingDate;
	private List<Rate> rates = null;
}
