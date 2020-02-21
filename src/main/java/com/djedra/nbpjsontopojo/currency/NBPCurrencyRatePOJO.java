package com.djedra.nbpjsontopojo.currency;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//@Data
//@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "no", "effectiveDate", "mid" })
public class NBPCurrencyRatePOJO {

	@JsonProperty("no")
	private String no;
	@JsonProperty("effectiveDate")
	private LocalDate effectiveDate;
	@JsonProperty("mid")
	private BigDecimal mid;


	@JsonProperty("no")
	public String getNo() {
		return no;
	}

	@JsonProperty("no")
	public void setNo(String no) {
		this.no = no;
	}

	@JsonProperty("effectiveDate")
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	@JsonProperty("effectiveDate")
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@JsonProperty("mid")
	public BigDecimal getMid() {
		return mid;
	}

	@JsonProperty("mid")
	public void setMid(BigDecimal mid) {
		this.mid = mid;
	}
}
