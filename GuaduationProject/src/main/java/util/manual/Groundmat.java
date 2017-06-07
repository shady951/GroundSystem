package util.manual;

import enums.Gorizontalshape;


/**
 * 接地网接地电阻计算模块
 * @author tc
 *
 */
public class Groundmat {

	private static final double pi = Math.PI;
	
	/**
	 * 计算单根接地体水平接地电阻
	 * @param p	土壤电阻率
	 * @param d	接地体等效直径
	 * @param l	接地体总长度
	 * @param h	埋深
	 * @param indexG	接地系数
	 * @return R	水平接地电阻
	 */
	public static double gorizontal(Double p, Double d, Double l, Double h,  Integer indexG) {
		Double R = null;
		h += 0.001;
		Double B = Gorizontalshape.shapeOf(indexG).getB();
		Double n = Math.pow(l, 2) / (h * d);
		R = (Math.log(n) + B) * p / (2 * pi * l);
		return R;
	}
	
	/**
	 * 计算方形闭合地网与放射型水平接地极组合地网接地电阻
	 * @param p	土壤电阻率
	 * @param a	地网等效正方形的边长
	 * @param b	放射型水平接地极总长度
	 * @param R1	地网接地电阻
	 * @param R2	放射型水平接地极接地电阻
	 * @return R	接地电阻
	 */
	public static double emissivitygorizontal(Double p, Double a, Double b, Double R1, Double R2) {
		Double R = null;
		Double R12 = p * Math.log((2 * b + a ) / a) / (2 * pi * b);
		R = (R1 * R2 - Math.pow(R12, 2)) / (R1 + R2 - 2 * R12);
		return R;
	}

	/**
	 * 计算以水平接地体为主的任意形状边缘闭合接地网接地电阻
	 * 参考文献：《交流电气装置的接地》DL/T621-1997
	 * @param p	土壤电阻率
	 * @param Ls	水平接地极总长度
	 * @param Lr	单根垂直接地极长度
	 * @param L0	接地网外缘边线总长度
	 * @param S	接地网面积
	 * @param h	埋深
	 * @param bc	水平接地极等效直径
	 * @param n	垂直接地极数量
	 * @return R	任意形状边缘闭合的接地网的接地电阻
	 */
	public static double gorizontalmat(Double p, Double Ls, Double Lr, Double L0, Double S, Double h, Double bc, Double n){
		Double R = null;
		h += 0.01;
		Double L = Ls + Lr * n;
		Double B = getB(S, h);
		Double a1 = geta1(S, L0);
		Double Re = 0.213 * p / Math.sqrt(S) * (1 + B) + (Math.log(S / (9 * h * bc)) - 5 * B) * S / (2 * pi * L);
		R = a1 * Re;
		return R;
	}

	/**
	 * 计算任意形状边缘闭合的复合接地网接地电阻
	 * 参考文献：王洪泽. 任意形状复合接地网接地电阻的计算. 电力建设,2000(5)
	 * @param p	土壤电阻率
	 * @param Ls	水平接地极总长度
	 * @param Lr	单根垂直接地极长度
	 * @param L0	接地网外缘边线总长度
	 * @param S	接地网面积
	 * @param h	埋深
	 * @param bc	水平接地极等效直径
	 * @param br	垂直接地极等效直径
	 * @param n	垂直接地极数量
	 * @return R	任意形状边缘闭合的接地网的接地电阻
	 */
	public static double verticalmat(Double p, Double Ls, Double Lr, Double L0, Double S, Double h, Double bc, Double br, Double n) {
		Double R = null;
		h += 0.01;
		Double L = Ls + Lr * n;
		Double k = Ls / L;
		Double a1 = geta1(S, L0);
		Double B = getB(S, h);
		Double Rec = 0.213 * p / (Math.sqrt(S) + 0.3 * Lr) * (1 + B) + (Math.log(S / (9 * h * bc)) - 5 * B) * k *p / (2 * pi * L);
		R = a1 * Rec;
		return R;
	}
	
	/**
	 * @return a1	地网修正系数
	 */
	private static double geta1(Double S, Double L0) {
		Double a1 = null;
		Double S1 = Math.sqrt(S);
		a1 = S1 / L0 * (3 * Math.log(L0 / S1) - 0.2);
		return a1;
	}
	
	/**
	 * @return B	地网面积S和埋深h相关系数
	 */
	private static Double getB(Double S, Double h) {
		Double B = null;
		Double S1 = Math.sqrt(S);
		B	=	1 / (1 + 4.6 * h / S1);
		return B;
	}
	
//	/**
//	 * @return	X	长宽比值，返回值-1：表示面积相对周长过大，或周长相对面积过小
//	 */
//	private static double getX(Double S, Double L0) {
//		Double X = null;
//		Double K = Math.pow(L0, 2) / 4 - 4 * S;
//		if(K < 0) return -1d;
//		Double a = (L0 / 2 + Math.sqrt(K)) / 2; 
//		Double b = (L0 / 2 - Math.sqrt(K)) / 2;
//		X = a / b;
//		return X;
//	}
	
}
