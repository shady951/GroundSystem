package util.manual;

import java.util.HashMap;

/**
 * 双层土壤接地电阻计算模块
 * @author tc
 * 
 */
public  class Doubledeckground {
	
	private static final double pi = Math.PI;
	
//	/**
//	 * 计算垂直双层土壤的接地网接地电阻
//	 * 参考文献：曹晓斌,等. 一类垂直双层土壤中地网接地电阻的简易计算公式. 中国电机工程学报,2009(1),29(1)
//	 * @param p1		土壤电阻率
//	 * @param p2		土壤电阻率
//	 * @param A1	p1土壤面积
//	 * @param S		接地网面积		
//	 * @return R		任意形状边缘闭合的接地电阻
//	 */
//	public double verticalDoubledeckMat(Double p1, Double p2, Double A1, Double S) {
//		Double R = null;
//		Double K = 2 * Math.sqrt(S) - Math.sqrt(2 * A1);
//		R = 0.5 * p1 * p2 * Math.sqrt(pi) / (K * p1 + Math.sqrt(2 * A1) * p2);
//		return R;
//	}
	
	/**
	 * 计算水平双层土壤的接地网接地电阻
	 * 参考文献：王洪泽. 双层土壤中复合地网接地电阻的新解析公式. 广东电力,2005(8),18(8)
	 * @param p1	上层土壤电阻率
	 * @param p2	下层土壤电阻率
	 * @param S	接地网水平面积
	 * @param H	上层土壤深度
	 * @param h	接地网或棒顶埋深
	 * @param L	水平接地体总长度(LC)
	 * @param L0	水平接地体外缘周长
	 * @param l	单根垂直接地棒平均长度(Lr)
	 * @param N	垂直接地棒总根数
	 * @param d	水平接地体等效直径
	 * @param d0	垂直接地棒等效直径
	 * @return R	任意形状边缘闭合的接地电阻
	 */
	public static double gorizontalDoubledeckMat(Double p1, Double p2, Double S, Double H, 
			Double h, Double L, Double L0, Double l, Double N, Double d, Double d0) {
		Double R = null;
		HashMap<String, Double> hm = getl(H, h, l);
		Double l1 = hm.get("l1") + 0.001;
 		Double l2 = hm.get("l2") + 0.001;
 		Double k = getk(p1, p2);
		Double Rhor = getRhor(p1, S, k, H, h, L, L0, d);
		Double Rrod = getRrod(p1, p2, S, k, H, h, l, N, d0, l1, l2);
		Double Rm = null;
		if(l2 > 0d && k < 0d) {
			Rm = getRm(p1, p2, S, h, L, L0, l, d, l1, l2);
		}	else {
			Rm = getRm(Rhor, p1, S, L0, L, l, h, d);
		}
		R = (Rhor * Rrod - Math.pow(Rm, 2)) / (Rhor + Rrod - 2 * Rm);
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
	public static double gorizontalDoubledeckvertical(Double p1, Double p2, Double H, Double h, Double l, Double d) {
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

//	/**
//	 * 计算水平双层土壤多根垂直接地体的接地电阻
//	 * 参考文献：王洪泽. 双层土壤变电站多根垂直接地极接地电阻解析公式. 广西电力,2005(3)
//	 * @param p1	上层土壤电阻率
//	 * @param p2	下层土壤电阻率
//	 * @param S	垂直接地极顶端覆盖面积
//	 * @param H	上层土壤深度
//	 * @param h	棒顶埋深
//	 * @param L0	面积S的外缘周长
//	 * @param l	单根垂直接地棒平均长度(Lr)
//	 * @param n	垂直接地体总根数
//	 * @param d	接地体等效直径
//	 * @return
//	 */
//	public double gorizontalDoubledeckverticals(Double p1, Double p2, Double S, Double H, Double h, Double L0,
//			Double l, Double n,  Double d) {
//		Double R = null;
//		Double dn = null;
//		HashMap<String, Double> hm = getl(H, h, l);
//		Double l1 = hm.get("l1");
//		Double l2 = hm.get("l2");
//		Double k = getk(p1, p2);
//		Double B = getB(S, h);
//		Double a1 = geta1(L0, S);
//		Double pa = getpa(p1, p2, l, l1, l2);
//		Double Rb = 0.213 * (1 + B) * a1 * pa / Math.sqrt(S);
//		Double R0 = gorizontalDoubledeckvertical(p1, p2, H, h, l, d);
//		if(k < 0d){
//			dn = 0.75;
//		}	else {
//			dn = 1d;
//		}
//		Double Kb = 1 + dn * Math.log(1 / (1 - k));
//		R = (R0 + Rb * Kb * Math.pow(Math.sqrt(n) - 1, 2)) / n;		
//		return R;
//	}

	/**
	 * @return HM 判断计算并储存上下层土壤内的棒长l1，l2的值
	 */
	private static HashMap<String, Double> getl(Double H, Double h, Double l) {
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
	 * @return Rhor 水平双层土壤水平地网接地电阻
	 */
	private static double getRhor(Double p1, Double S, Double k, Double H, Double h,Double L, Double L0, Double d) {
		Double Rhor = null;
		Double f = 0.016 * k * (L0 / 4);
		Double H0 = 0.9 * ((k - 1) / k) * Math.sqrt(S / (8 * pi)) * Math.log(1 - k);
		Double B = getB(S, h);
		Double a1 = geta1(L0, S);
		Double R1e = getR1e(p1, S, L, h, d, B);
		Double Re = getRe(p1, k, H, H0, f);
		Rhor = a1 * (R1e - Re);
		return Rhor;
	}
	
	/**
	 * @return B 地网面积与埋深的关联系数
	 */
	private static double getB(Double S, Double h) {
		Double B = null;
		B = 1 / (1 + 4.6 * h / Math.sqrt(S));
		return B;
	}
	
	/**
	 * @return a1 面积S形状修正系数
	 */
	private static double geta1(Double L0, Double S) {
		Double a1 = null;
		Double N = 3 * Math.log(L0 / Math.sqrt(S)) - 0.2;
		a1 = N * Math.sqrt(S) / L0;
		return a1;
	}
	
	/**
	 * @return R1e 均匀土壤电阻率为p1时的等值正方形接地网的接地电阻
	 */
	private static double getR1e(Double p1, Double S, Double L, Double h, Double d, Double B) {
		Double R1e = null;
		Double N0 = 0.213 * (1 + B);
		Double N1 = Math.log(S / (9 * h * d)) - 5 * B;
		R1e = N0 * p1 / Math.sqrt(S) + N1 * p1 / (2 * pi * L);
		return R1e; 
	}
	
	/**
	 * @return Re 下层土壤镜像电流对等值正方形地网所引起的附加接地电阻差
	 */
	private static double getRe(Double p1, Double k, Double H, Double H0, Double f) {
		Double Re = null;
		Re = p1 * Math.log(1 - k) / (2 * pi * (H + H0 + f));
		return Re;
	}

	/**
	 * @return Rrod 水平双层土壤中组合棒的接地电阻
	 */
	private static double getRrod(Double p1, Double p2, Double S, Double k, Double H, Double h, Double l,Double N, Double d0, Double l1, Double l2) {
		Double Rrod = null;
		Double K0 = null;
		Double q = null;
		Double g0 = Math.log(2 * (l + 1) / d0);
		if(k < 0) {
			K0 = 0.75;
		}	else {
			K0 = 0.95;
		}
		if(l2 == 0d) {
			q = 1d;
		}	else {
			q = (l + h) / H;
		}
		Double F0 = getF0(S, k, N, l, K0, g0);
		Double E = getE(N, F0, q);
		Double Ra = getRa(p1, k, H, N, l1, E, g0, F0);
		Rrod = Ra / (1 + (2 * pi * N * l2 * Ra / (g0 * F0 * p2)));
		return Rrod;
	}
	
	/**
	 * @return F0 水平双层土壤中组合棒的接地电阻相关系数
	 */
	private static double getF0(Double S, Double k, Double N, Double l, Double K0, Double g0) {
		Double F0 = null;
		Double n = Math.pow(l / (Math.sqrt(S) * (1 - 0.9 * k)), 0.6);
		F0 = 1 + (N - 1 / Math.sqrt(N)) * K0 * n / g0;
		return F0;
	}
	
	/**
	 * @return E 水平双层土壤中组合棒的接地电阻相关系数
	 */
	private static double getE(Double N, Double F0, Double q) {
		Double E = null;
		Double n = Math.pow(N / F0 - 1, 2);
		E = Math.sqrt(n * Math.pow(q, 2) + 1);
		return E;
	}

	/**
	 * @return Ra 水平双层土壤中组合棒的接地电阻相关系数
	 */
	private static double getRa(Double p1, Double k, Double H, Double N, Double l1, Double E, Double g0, Double F0) {
		Double Ra = null;
		Double n1 = p1 * g0 * F0 / (2 * pi * N * l1);
		Double n2 = p1 * Math.log(1 / (1 - k)) / (2 * pi * H * E);
		Ra = n1 + n2;
		return Ra;
	}

	/**
	 * @return Rm 水平双层土壤，当下层土壤内棒长等于0，或，下层土壤内棒长大于0且土壤反射系数大于等于0时的互电阻
	 */
	private static double getRm(Double Rhor, Double p1, Double S, Double L0,  Double L, Double l, Double h, Double d) {
		Double Rm = null;
		Double a1 = geta1(L0, S);
		Rm = Rhor - a1 * p1 * Math.log(l / Math.sqrt(h * d)) / (pi * L);
		return Rm;
	}
	
	/**
	 * @return Rm 水平双层土壤，当下层土壤内棒长大于0且土壤反射系数小于0时的互电阻
	 */
	private static double getRm(Double p1, Double p2, Double S, Double h, Double L, Double L0, Double l, Double d, Double l1, Double l2) {
		Double Rm = null;
		Double a1 = geta1(L0, S);
		Double B = getB(S, h);
		Double pa = getpa(p1, p2, l, l1, l2);
		Double Rae = getR1e(pa, S, L, h, d, B);
		Rm = a1 * (Rae - pa * Math.log(l / (Math.sqrt(h * d))) / (pi * L));
		return Rm;
	}
	/**
	 * @return k	土壤反射系数(+0.001：防止用户输入相同电阻率的两层土壤，使k等于0)
	 */
	private static Double getk(Double p1, Double p2) {
		Double k = null;
		 k = (p2 - p1 + 0.001) / (p2 + p1);
		return k;
	}
	
	/**
	 * @return x	比值系数
	 */
	private static Double getx(Double H, Double l, Double l1, Double l2) {
		Double x = null;
		if(l2 == 0d) {
			x = l / H;
		}	else {
			x = l1 / l;
		}
		return x;
	}

	/**
	 * @return pa 土壤视电阻率公式一
	 */
	private static Double getpa(Double p1, Double p2, Double l, Double l1, Double l2) {
		Double pa = null;
		pa = p1 * p2 * l / (p1 * l2 + p2 * l1);
		return pa;
	}
	
	/**
	 * @return pa 土壤视电阻率公式二
	 */
//	private Double getpa(Double p1, Double p2, Double l, Double l1) {
//		Double pa = null;
//		Double k = getk(p1, p2);
//		pa = (1 + k) * p1 / (1 - k + 2 * k * (l1 / l));
//		return pa;
//	}
	
	/**
	 * @return pa 土壤视电阻率公式三
	 */
//	private Double getpaa(Double p1, Double p2, Double l, Double l1) {
//		Double pa = null;
//		pa = p1 * p2 / (p1 + (p2 - p1) * (l1 / l));
//		return pa;
//	}
	
//	private double C(Double p1, Double p2, Double l, Double l1) {
//		double k = getk(p1, p2);
//		return (l1/l) * Math.log(1/(1-k));
//	}
	
	/*
	public static void main(String[] args) {
		System.out.println(new Doubledeckground().gorizontalDoubledeckMat(100d, 5.26d, 400d, 5d, 0.5d, 120d, 80d, 10d, 9d, 0.02, 0.02));
		//0.1606321783264501, k=-0.9
		System.out.println(new Doubledeckground().gorizontalDoubledeckMat(100d, 33.33d, 400d, 5d, 0.5d, 120d, 80d, 10d, 9d, 0.02, 0.02));
		//0.8291683044406233,k=-0.5
		System.out.println(new Doubledeckground().gorizontalDoubledeckMat(100d, 100d, 400d, 5d, 0.5d, 120d, 80d, 10d, 9d, 0.02, 0.02));
		//1.8184978920959776,k=0
		System.out.println(new Doubledeckground().gorizontalDoubledeckMat(100d, 300d, 400d, 5d, 0.5d, 120d, 80d, 10d, 9d, 0.02, 0.02));
		//3.411582393087603,k=0.5
		System.out.println(new Doubledeckground().gorizontalDoubledeckMat(100d, 1900d, 400d, 5d, 0.5d, 120d, 80d, 10d, 9d, 0.02, 0.02));
		//7.954519587628304,k=0.9
		//结果准确
		System.out.println(new Doubledeckground().verticalsDoubledeck(100d, 5.26d, 400d, 5d, 0.05, 80d, 10d, 9d, 0.02));
		//0.17005240340905908, k=-0.9
		System.out.println(new Doubledeckground().verticalsDoubledeck(100d, 33.33d, 400d, 5d, 0.05, 80d, 10d, 9d, 0.02));
		//0.9465312040595742,k=-0.5
		System.out.println(new Doubledeckground().verticalsDoubledeck(100d, 100d, 400d, 5d, 0.05, 80d, 10d, 9d, 0.02));
		//2.221485801870099,k=0
		System.out.println(new Doubledeckground().verticalsDoubledeck(100d, 300d, 400d, 5d, 0.05, 80d, 10d, 9d, 0.02));
		//4.413924291607431,k=0.5
		System.out.println(new Doubledeckground().verticalsDoubledeck(100d, 1900d, 400d, 5d, 0.05, 80d, 10d, 9d, 0.02));
		//8.758316145453467,k=0.9
		//结果准确
	}
	 */
	
}
