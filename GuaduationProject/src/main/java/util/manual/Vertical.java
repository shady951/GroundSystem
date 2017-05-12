package util.manual;

import java.util.HashMap;


/**
 * 垂直接地电阻计算模块
 * @author tc
 *
 */
public class Vertical {
	
	private static final double pi = Math.PI;
	//正n边形n等分角
	private static Double angle = null;
	//正n边形半径系数
	private static Double radius = null;
	
	public Vertical() {
	}
	
	
	/**
	 * 计算单根垂直接地极(中点电位法)
	 * @param p	土壤电阻率
	 * @param d	接地极等效直径
	 * @param l	接地极长度
	 * @param h	埋深
	 * @return R	垂直接地电阻阻值
	 */
	public double vertical(Double p, Double d, Double l, Double h) {
		Double R = null;
		Double n = 4 * l * (l + 2 * h) / (l + 4 * h);
		R = Math.log(n / (1.36 * d)) * p / (2 * pi * l);
		return R;
	}
	
	/**
	 * 计算水平双层土壤单根垂直接地体的接地电阻
	 * 参考文献：《水力发电厂接地设计技术导则》DL/T5091-1999
	 * 注：本方法对《导则》稍作改动，可计算埋深影响并简化无穷级数的计算
	 * @param p1	上层土壤电阻率
	 * @param p2	下层土壤电阻率
	 * @param H	上层土壤深度
	 * @param h	棒顶埋深
	 * @param l	单根垂直接地棒平均长度(Lr)
	 * @param d	垂直接地体等效直径
	 * @return R	接地电阻阻值
	 */
	public double Doubledeckvertical(Double p1, Double p2, Double H, Double h, Double l, Double d) {
		Double R = null;
		HashMap<String, Double> hm = getl(H, h, l);
		Double l1 = hm.get("l1");
		Double l2 = hm.get("l2");
		Double x = getx(H, l, l1, l2);
		Double pa = getpa(p1, p2, l, l1, l2);
		Double k = getk(p1, p2);
		R = (Math.log(8 * l / d) - 1 + x * Math.log(1 / (1 - k))) * pa / (2 * pi * l);
		return R;
	}
	
	/**
	 * 计算多根垂直接地极等距直线排列的接地电阻
	 * 参考文献：王洪泽,等. 直线排列多根垂直地极接地电阻通用公式. 广西电力,2004(4)
	 * @param p	土壤电阻率
	 * @param p1	下层土壤电阻率
	 * @param H  上层土壤深度
	 * @param d	接地极等效直径
	 * @param l	接地极长度
	 * @param h	埋深
	 * @param s	垂直接地极间距
	 * @param n	垂直接地极数量
	 * @return R	接地电阻阻值
	 */
	public double straightverticals(Double p,Double p1, Double H, Double d, Double l, Double h, Double s, Double n){
		Double R = null;
		Double R0 = null;
		Double D = (n - 1 + 0.001) * s;
		if(H != 0d) {
			R0 = Doubledeckvertical(p, p1, H, h, l, d);
			HashMap<String, Double> hm = getl(H, h, l);
			Double l1 = hm.get("l1");
			Double l2 = hm.get("l2");
			p = getpa(p, p1, l, l1, l2);
		} else {
			R0 = vertical(p, d, l, h);
		}
		R = R0 / n + 1.3 * p * Math.pow(Math.sqrt(n) - 1, 2) / (n * D);
		return R;
	}
	
	/**
	 * 计算多根垂直接地极等距环形排列的接地电阻
	 * 参考文献：马晓红,等. 实用的多根接地电极的接地电阻计算. 电工技术,2004(11)
	 * @param p	土壤电阻率
	 * @param p1	下层土壤电阻率
	 * @param H  上层土壤深度
	 * @param d	接地极等效直径
	 * @param l	接地极长度
	 * @param h	埋深
	 * @param s	垂直接地极间距
	 * @param n	垂直接地极数量
	 * @return R	接地电阻阻值
	 */
	public double linkverticals(Double p,Double p1, Double H, Double d, Double l, Double h,Double s, Double n){
		Vertical.angle = 2 * pi / n;
		Vertical.radius = getradius();
		Double R = null;
		Double R1 =null;
		Double K = null;
		Double k = 1 / l;
		Double w = s / l;
		if(H != 0d) {
			R1 = Doubledeckvertical(p, p1, H, h, 1d, d * k);
			HashMap<String, Double> hm = getl(H, h, l);
			Double l1 = hm.get("l1");
			Double l2 = hm.get("l2");
			p = getpa(p, p1, l, l1, l2);
			K =  getK(p, w, n);
		} else {
			R1 = vertical(p, d * k, 1d, h);
			K = getK(p, w, n);
		}
		R = k * (R1 + K) / n;
		return R;
	}
	
	/**
	 * 计算多根任意形状排列的垂直接地极的接地电阻
	 * 参考文献：王洪泽. 双层土壤变电站多根垂直接地极接地电阻解析公式. 广西电力,2005(3)
	 * @param p	土壤电阻率
	 * @param S	垂直接地极顶端覆盖面积
	 * @param L0	面积S的外缘周长
	 * @param d	接地极等效直径
	 * @param l	接地极长度
	 * @param h	埋深
	 * @param n	垂直接地极数量
	 * @return R	接地电阻阻值
	 */
	public Double verticals(Double p, Double S, Double L0, Double d, Double l, Double h, Double n) {
		Double R = null;
		Double a1 = geta1(L0, S);
		Double B = getB(S, h);
		Double R0 = vertical(p, d, l, h);
		Double Rn = 0.213 * (1 + B) * a1 * p / Math.sqrt(S);
		R = (R0 + Rn * Math.pow(Math.sqrt(n) - 1, 2)) / n;
		return R;
	}
	
	/**
	 * @return HM 判断计算并储存上下层土壤内的棒长l1，l2的值
	 */
	public HashMap<String, Double> getl(Double H, Double h, Double l) {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		Double l1 = null;
		Double l2 = null;
		Double N = null;
		if(H > h) {
			N = h + l;
			if(H > N) {
				l1 = l;
				l2  = 0d;
			} else {
				l1 = H - h;
				l2 = l - l1;
			}
		} else {
			l1 = 0d;
			l2 = l;
		}
		hm.put("l1", l1);
		hm.put("l2", l2);
		return hm;
	}
	
	/**
	 * @return k	土壤反射系数(+0.001：防止用户输入相同电阻率的两层土壤，使k等于0)
	 */
	private Double getk(Double p1, Double p2) {
		Double k = null;
		k = (p2 - p1 + 0.001) / (p2 + p1);
		return k;
	}
	
	/**
	 * @return pa 土壤视电阻率公式
	 */
	private Double getpa(Double p1, Double p2, Double l, Double l1, Double l2) {
		Double pa = null;
		pa = p1 * p2 * l / (p1 * l2 + p2 * l1);
		return pa;
	}
	
	/**
	 * @return x	比值系数
	 */
	private Double getx(Double H, Double l, Double l1, Double l2) {
		Double x = null;
		if(l2 == 0d) {
			x = l / H;
		}	else {
			x = l1 / l;
		}
		return x;
	}
	
	/**
	 * @return K	环形接地极每一根的相对并联电阻阻值
	 */
	private Double getK(Double p, Double w, Double n) {
		Double K = 0d;
		Double a;
		for(a = angle; a <= pi; a += angle) {
			K += getR21(p, w, getb(a));
		}
		if(isoddnumber(n)) {
			a -= angle;
		} else {
			a -= 2 * angle;
		}
		for(; a > 0; a -= angle) {
			K += getR21(p, w, getb(a));
		}
		return K;
	}
	
	/**
	 * @return R21	并联接地电极电阻值
	 */
	private Double getR21(Double p, Double w, Double b){
		Double R21 = null; 
		Double Rm = getRm(w * b);
		R21 = p * Rm;
//		System.out.println("R21:"+R21);
		return R21;
	}
	
	/**
	 * @return b	正n边形的一个顶点到任意顶点的距离系数
	 */
	private Double getb(Double a){
		Double b = null;
		b = Math.sqrt(2 * Math.pow(radius, 2)  * (1 - Math.cos(a)));
//		System.out.println("b:"+ b);
		return b;
	}
	
	/**
	 * @return r 正n边形半径系数
	 */
	private Double getradius() {
		Double r = null;
		r = 0.5 / Math.sin(angle / 2);
		return r;
	}
	
	/**
	 * @return Rm 耦合电阻
	 */
	private Double getRm(double w) {
//		System.out.println("w:"+w);
		Double Rm = null;
		//当s/l大于10时或小于0.1时，忽略耦合电阻的影响
		if(w > 10 || w < 0.1) return 0d;
		if(w >= 1.5) {
			Rm = 1.0 / 17 - w / 170;
		} else {
			Rm = 0.29645 - w * 0.1643;
		}
//		System.out.println("Rm:"+Rm);
		return Rm;
	}
	
	/**
	 * @return n	垂直接地极的根数是否是奇数
	 */
	private boolean isoddnumber(Double n){
		if(n % 2 == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return a1 面积S形状修正系数
	 */
	private double geta1(Double L0, Double S) {
		Double a1 = null;
		Double N = 3 * Math.log(L0 / Math.sqrt(S)) - 0.2;
		a1 = N * Math.sqrt(S) / L0;
		return a1;
	}
	
	/**
	 * @return B 地网面积与埋深的关联系数
	 */
	private double getB(Double S, Double h) {
		Double B = null;
		B = 1 / (1 + 4.6 * h / Math.sqrt(S));
		return B;
	}
	
	
//	public static void main(String[] args) {
//		System.out.println(new Vertical().vertical(100d, 0.02, 10d, 0.5d));//结果：12.0972， 论文数据为12.0972
//		System.out.println(new Vertical().straightverticals(100d, 0.02, 10d, 0d, 14.142, 2d));//结果：6.836， 论文数据为6.837
//		System.out.println(new Vertical().linkverticals(100d, 0.2, 60d, 1d, 10d, 100d));//结果：0.0091，论文数据为0.0090
//	}
	
}
