package org.zagelnews.dtos.feeds;

import org.zagelnews.dtos.ZagelnewsDto;
import org.zagelnews.utils.ClientConfiguration;


public class VoteReasonDto extends ZagelnewsDto{

	private Integer reasonKey;

	private String negativeReasonAdesc;

	private String negativeReasonEdesc;

	private String reasonAdesc;

	private Integer reasonCode;

	private String reasonEdesc;

	public Integer getReasonKey() {
		return this.reasonKey;
	}

	public void setReasonKey(Integer reasonKey) {
		this.reasonKey = reasonKey;
	}

	public String getNegativeReasonAdesc() {
		return this.negativeReasonAdesc;
	}

	public void setNegativeReasonAdesc(String negativeReasonAdesc) {
		this.negativeReasonAdesc = negativeReasonAdesc;
	}

	public String getNegativeReasonEdesc() {
		return this.negativeReasonEdesc;
	}

	public void setNegativeReasonEdesc(String negativeReasonEdesc) {
		this.negativeReasonEdesc = negativeReasonEdesc;
	}

	public String getReasonAdesc() {
		return this.reasonAdesc;
	}

	public void setReasonAdesc(String reasonAdesc) {
		this.reasonAdesc = reasonAdesc;
	}

	public Integer getReasonCode() {
		return this.reasonCode;
	}

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonEdesc() {
		return this.reasonEdesc;
	}

	public void setReasonEdesc(String reasonEdesc) {
		this.reasonEdesc = reasonEdesc;
	}

	@Override
	public String toString() {
		if(ClientConfiguration.lang.contains("ar")){
			return reasonAdesc;	
		}else{
			return reasonEdesc;
		}
	}
}