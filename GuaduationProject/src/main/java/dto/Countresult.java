package dto;

/**
 * 数据传输bean类
 * @author tc
 *
 */
public class Countresult {

	private int style;		//建筑类型(1:普通建筑 2:变电站/所 3:杆塔)
	private double R;		//工频接地电阻
	private double Ri;		//冲击接地电阻
	private double S;		//地网面积
	private double m;		//外延长度
	private int flag;	//接地体补加形式(1:只补加水平接地体 2:只补加垂直接地体 3:都补加 4:根据防雷规范补加水平接地体 5:根据根据防雷规范补加垂直接地体)
	private double lr;		//单根水平接地体长度
	private double lv;		//单根垂直接地体长度
	private double n;		//垂直接地体数量
	private double modulecount;	//接地模块数量
	private double modulecounti;	//集中接地装置的接地模块数量
	private double independent;	//集中接地装置的接地形式(1:直线形接地 2:环形接地)
	private double l;		//集中接地装置的单根接地体长度
	private double s;		//集中接地装置接地体之间的间距
	private double ni;		//集中接地装置的接地体数量
	private double r;		//电杆环形接地装置的半径
	private int kind;//杆塔接地形式(1:自然接地 2:铁塔放射形 3:电杆放射形 4:电杆环形)
	
	public Countresult() {
	}
	
	/**
	 * 用于getR()
	 * @param R		工频接地电阻
	 * @param flag	接地体补加形式
	 * @param lr		单根水平接地体长度
	 * @param lv		单根垂直接地体长度
	 * @param S		地网面积
	 * @param m		外延长度
	 * @param n		垂直接地体数量
	 */
	public Countresult(double R, int flag, double lr, double lv, double S, double m, double n) {
		this.R = R;
		this.flag = flag;
		this.lr = lr;
		this.lv = lv;
		this.S = S;
		this.m = m;
		this.n = n;
	}

	/**用于杆塔放射形接地
	 * @param style
	 * @param R
	 * @param Ri
	 * @param flag
	 * @param lr
	 * @param kind
	 */
	public Countresult(int style, double R, double Ri, int flag, double lr, int kind) {
		super();
		this.style = style;
		this.R = R;
		this.Ri = Ri;
		this.flag = flag;
		this.lr = lr;
		this.kind = kind;
	}

	/**用于铁塔放射形接地
	 * @param style
	 * @param R
	 * @param Ri
	 * @param S
	 * @param flag
	 * @param lr
	 * @param kind
	 */
	public Countresult(int style, double R, double Ri, double S, int flag, double lr, int kind) {
		this.style = style;
		this.R = R;
		this.Ri = Ri;
		this.S = S;
		this.flag = flag;
		this.lr = lr;
		this.kind = kind;
	}

	/**
	 * 用于杆塔自然接地
	 * @param R		工频接地电阻
	 * @param Ri		冲击接地电阻
	 * @param kind	杆塔接地形式
	 */
	public Countresult(int style, double R, double Ri, int kind) {
		this.style = style;
		this.R = R;
		this.Ri = Ri;
		this.kind = kind;
	}

	@Override
	public String toString() {
		return "Countresult [style=" + style + ", R=" + R + ", Ri=" + Ri + ", S=" + S + ", m=" + m + ", flag=" + flag + ", lr=" + lr
				+ ", lv=" + lv + ", n=" + n + ", modulecount=" + modulecount + ", modulecounti=" + modulecounti + ", independent="
				+ independent + ", l=" + l + ", s=" + s + ", ni=" + ni + ", r=" + r + ", kind=" + kind + "]";
	}

	public int getstyle() {
		return style;
	}

	public void setstyle(int style) {
		this.style = style;
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

	public int getflag() {
		return flag;
	}

	public void setflag(int flag) {
		this.flag = flag;
	}

	public double getlr() {
		return lr;
	}

	public void setlr(double lr) {
		this.lr = lr;
	}

	public double getlv() {
		return lv;
	}

	public void setlv(double lv) {
		this.lv = lv;
	}

	public double getn() {
		return n;
	}

	public void setn(double n) {
		this.n = n;
	}

	public double getmodulecount() {
		return modulecount;
	}

	public void setmodulecount(double modulecount) {
		this.modulecount = modulecount;
	}

	public double getmodulecounti() {
		return modulecounti;
	}

	public void setmodulecounti(double modulecounti) {
		this.modulecounti = modulecounti;
	}

	public double getindependent() {
		return independent;
	}

	public void setindependent(double independent) {
		this.independent = independent;
	}

	public double getl() {
		return l;
	}

	public void setl(double l) {
		this.l = l;
	}

	public double gets() {
		return s;
	}
	
	public void sets(double s) {
		this.s = s;
	}

	public double getni() {
		return ni;
	}

	public void setni(double ni) {
		this.ni = ni;
	}

	public double getr() {
		return r;
	}

	public void setr(double r) {
		this.r = r;
	}
	
	public int getkind() {
		return kind;
	}

	public void setkind(int kind) {
		this.kind = kind;
	}
	
	
}
