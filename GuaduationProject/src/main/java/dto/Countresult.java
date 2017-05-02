package dto;

public class Countresult {

	private double R;
	private double Ri;
	private double S;
	private double m;
	private Integer flag;
	private Double lr;
	private Double lv;
	private double n;
	private Double modulecount;
	private Double modulecounti;
	private Double independent;
	private Double l;
	private Double s;
	private Double ni;
	
	public Countresult() {
	}

	public Countresult(Double R, Integer flag, Double lr, Double lv, Double S, Double m, Double n) {
		this.R = R;
		this.flag = flag;
		this.lr = lr;
		this.lv = lv;
		this.S = S;
		this.m = m;
		this.n = n;
	}

	@Override
	public String toString() {
		return "Countresult [R=" + R + ", Ri=" + Ri + ", S=" + S + ", m=" + m + ", flag=" + flag + ", lr=" + lr + ", lv=" + lv + ", n=" + n
				+ ", modulecount=" + modulecount + ", modulecounti=" + modulecounti + ", independent=" + independent + ", l=" + l + ", s="
				+ s + ", ni=" + ni + "]";
	}

	public double getR() {
		return R;
	}

	public void setR(double R) {
		this.R = R;
	}

	public double getRi() {
		return Ri;
	}

	public void setRi(double ri) {
		Ri = ri;
	}

	public double getS() {
		return S;
	}

	public void setS(double S) {
		this.S = S;
	}

	public double getm() {
		return m;
	}

	public void setm(double m) {
		this.m = m;
	}

	public Integer getflag() {
		return flag;
	}

	public void setflag(Integer flag) {
		this.flag = flag;
	}

	public Double getlr() {
		return lr;
	}

	public void setlr(Double lr) {
		this.lr = lr;
	}

	public Double getlv() {
		return lv;
	}

	public void setlv(Double lv) {
		this.lv = lv;
	}

	public double getn() {
		return n;
	}

	public void setn(double n) {
		this.n = n;
	}

	public Double getmodulecount() {
		return modulecount;
	}

	public void setmodulecount(Double modulecount) {
		this.modulecount = modulecount;
	}

	public Double getmodulecounti() {
		return modulecounti;
	}

	public void setmodulecounti(Double modulecounti) {
		this.modulecounti = modulecounti;
	}

	public Double getindependent() {
		return independent;
	}

	public void setindependent(Double independent) {
		this.independent = independent;
	}

	public Double getl() {
		return l;
	}

	public void setl(Double l) {
		this.l = l;
	}

	public Double getni() {
		return ni;
	}

	public void setni(Double ni) {
		this.ni = ni;
	}

	public Double gets() {
		return s;
	}
	
	public void sets(Double s) {
		this.s = s;
	}
	
}
