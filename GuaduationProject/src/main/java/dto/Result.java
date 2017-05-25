package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

	private String plan;		//接地设计
	@JsonProperty
	private double R;			//工频接地电阻
	@JsonProperty
	private double Ri;			//冲击接地电阻
	private double money;//消耗材料预算(元)
	
	public Result() {
	}

	/**
	 * 
	 * @param plan		接地设计
	 * @param R			工频接地电阻
	 * @param Ri			冲击接地电阻
	 * @param money消耗材料预算(元)
	 */
	public Result(String plan, double R, double Ri, double money) {
		this.plan = plan;
		this.R = R;
		this.Ri = Ri;
		this.money = money;
	}

	public String getplan() {
		return plan;
	}

	public void setplan(String plan) {
		this.plan = plan;
	}

	@JsonIgnore
	public double getR() {
		return R;
	}

	@JsonIgnore
	public void setR(double R) {
		this.R = R;
	}

	@JsonIgnore
	public double getRi() {
		return Ri;
	}

	@JsonIgnore
	public void setRi(double Ri) {
		this.Ri = Ri;
	}

	public double getmoney() {
		return money;
	}

	public void setmoney(double money) {
		this.money = money;
	}

}
