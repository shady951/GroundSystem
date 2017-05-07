package dto;

public class Data {

	private int style;		//建筑类型
	private double p;		//(上层)土壤电阻率
	private double H;		//上层土壤深度(0为单层)
	private double p1;	//下层土壤电阻率(0为单层)
	private double S;		//占地面积
	private double Rk;	//工频电阻要求值
	private int type;			//防雷建筑分类
	private boolean city;//土地资源是否受限
	
	public Data() {
	}

	/**
	 * @param style
	 * @param p
	 * @param H
	 * @param p1
	 * @param S
	 * @param Rk
	 * @param type
	 * @param city
	 */
	public Data(int style, double p, double H, double p1, double S, double Rk, int type, boolean city) {
		this.style = style;
		this.p = p;
		this.H = H;
		this.p1 = p1;
		this.S = S;
		this.Rk = Rk;
		this.type = type;
		this.city = city;
	}

	public int getstyle() {
		return style;
	}

	public void setstyle(int style) {
		this.style = style;
	}

	public double getp() {
		return p;
	}

	public void setp(double p) {
		this.p = p;
	}

	public double getH() {
		return H;
	}

	public void setH(double H) {
		this.H = H;
	}

	public double getp1() {
		return p1;
	}

	public void setp1(double p1) {
		this.p1 = p1;
	}

	public double getS() {
		return S;
	}

	public void setS(double S) {
		this.S = S;
	}

	public double getRk() {
		return Rk;
	}

	public void setRk(double Rk) {
		this.Rk = Rk;
	}

	public int gettype() {
		return type;
	}

	public void settype(int type) {
		this.type = type;
	}

	public boolean iscity() {
		return city;
	}

	public void setcity(boolean city) {
		this.city = city;
	}
	
}
