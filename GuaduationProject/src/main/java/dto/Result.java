package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 数据传输实体类
 * 用于返回至前端
 * @author tc
 *
 */
public class Result {

	private String plan;		//接地设计
	@JsonProperty
	private double R;			//工频接地电阻
	@JsonProperty
	private double Ri;			//冲击接地电阻
	private String money;//消耗材料预算(元)
	
	public Result() {
	}

	/**
	 * 
	 * @param plan		
	 * @param R			
	 * @param Ri			
	 * @param money
	 */
	public Result(String plan, double R, double Ri, String money) {
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

	public String getmoney() {
		return money;
	}

	public void setmoney(String money) {
		this.money = money;
	}

}
