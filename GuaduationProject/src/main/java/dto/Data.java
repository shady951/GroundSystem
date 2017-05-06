package dto;

public class Data {

	private double p;
	private double H;
	private double p1;
	private double S;
	private double Rk;
	private int type;
	private boolean city;
	
	public Data() {
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
