package util.manual;

import util.convert.Totallengthofmat;
import enums.Gorizontalshape;


/**
 * 接地网接地电阻计算模块
 * @author tc
 *
 */
public class Groundmat {

	private static final double pi = Math.PI;
	
	public Groundmat() {
	}
	
	/**
	 * 计算基础接地网电阻(简化计算)
	 * @param p	土壤电阻率
	 * @param S	建筑面积
	 * @return R	接地电阻
	 */
	public double generalmat (Double p, Double S) {
		Double R = null;
		R = 0.44 * p / Math.sqrt(S);
		return R;
	}
	
	/**
	 * 计算水平接地电阻
	 * @param p	土壤电阻率
	 * @param d	接地体等效直径
	 * @param l	接地体总长度
	 * @param h	埋深
	 * @param indexG	接地系数
	 * @return R	水平接地电阻
	 */
	public double gorizontal(Double p, Double d, Double l, Double h,  Integer indexG) {
		Double R = null;
		h += 0.001;
		Double B = Gorizontalshape.shapeOf(indexG).getB();
		Double n = Math.pow(l, 2) / (h * d);
		R = (Math.log(n) + B) * p / (2 * pi * l);
		return R;
	}
	
	/**
	 * 计算闭合地网与放射型水平接地极组合地网接地电阻
	 * @param p	土壤电阻率
	 * @param a	地网等效正方形的边长
	 * @param b	放射型水平接地极总长度
	 * @param R1	地网接地电阻
	 * @param R2	放射型水平接地极接地电阻
	 * @return R	接地电阻
	 */
	public double emissivitygorizontal(Double p, Double a, Double b, Double R1, Double R2) {
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
	public double gorizontalmat(Double p, Double Ls, Double Lr, Double L0, Double S, Double h, Double bc, Double n){
		Double R = null;
		h += 0.01;
		Double L = Ls + Lr * n;
		Double B = getB(S, h);
		Double a1 = geta1(S, L0);
		Double Re = 0.213 * p / Math.sqrt(S) * (1 + B) + (Math.log(S / (9 * h * bc)) - 5 * B) * p / (2 * pi * L);
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
	public double verticalmat(Double p, Double Ls, Double Lr, Double L0, Double S, Double h, Double bc, Double br, Double n) {
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
	private double geta1(Double S, Double L0) {
		Double a1 = null;
		Double S1 = Math.sqrt(S);
		a1 = S1 / L0 * (3 * Math.log(L0 / S1) - 0.2);
		return a1;
	}
	
	/**
	 * @return B	地网面积S和埋深h相关系数
	 */
	private Double getB(Double S, Double h) {
		Double B = null;
		Double S1 = Math.sqrt(S);
		B	=	1 / (1 + 4.6 * h / S1);
		return B;
	}
	
	
	
//	/**
//	 * @return R1 接地网中的水平接地电阻
//	 */
//	private double getR1(Double p, Double Lc, Double bc, Double S, Double h, Double L0) {
//		Double R1 = null;
//		Double K1 = getK1(S, L0);
//		Double K2 = getK2(S, L0);
//		Double a1 = Math.sqrt(bc * h);
//		if(h == 0) a1 = bc / 2;
//		R1 = (Math.log(2 * Lc / a1) + (K1 * Lc / Math.sqrt(S)) - K2) * p / (pi * Lc);
//		return R1;
//	}
//	
//	/**
//	 * @return R2 接地网中的垂直接地电阻
//	 */
//	private double getR2(Double p, Double Lr, Double br, Double n, Double S, Double L0) {
//		Double R2 = null;
//		Double K1 = getK1(S, L0);
//		Double K = Math.pow(Math.sqrt(n) - 1, 2) / Math.sqrt(S);
//		R2 = (Math.log(8 * Lr / br) - 1 + 2 * K1 * Lr * K) * p / (2 * pi * n * Lr);
//		return R2;
//	}
//	
//	/**
//	 * @return Rm 接地网中的互电阻系数
//	 */
//	private double getRm(Double p, Double Lc, Double Lr, Double S, Double L0){
//		Double Rm = null;
//		Double K1 = getK1(S, L0);
//		Double K2 = getK2(S, L0);
//		Rm = (Math.log(2 * Lc / Lr) + K1 * Lc / Math.sqrt(S) - K2 + 1) * p / (pi * Lc);
//		return Rm;
//	}
//	
//	/**
//	 * @return K1	系数
//	 */
//	private double getK1(Double S, Double L0){
//		Double K1 = null;
//		Double X = getX(S, L0);
//		K1 = -0.04 * X + 1.41;
//		return K1;
//	}
//	
//	/**
//	 * @return K2 系数
//	 */
//	private double getK2(Double S, Double L0){
//		Double K2 = null;
//		Double X = getX(S, L0);
//		K2 = 0.15 * X + 5.5;
//		return K2;
//	}
//	
//	/**
//	 * @return	X	长宽比值，返回值-1：表示面积相对周长过大，或周长相对面积过小
//	 */
//	private double getX(Double S, Double L0) {
//		Double X = null;
//		Double K = Math.pow(L0, 2) / 4 - 4 * S;
//		if(K < 0) return -1d;
//		Double a = (L0 / 2 + Math.sqrt(K)) / 2; 
//		Double b = (L0 / 2 - Math.sqrt(K)) / 2;
//		X = a / b;
//		return X;
//	}
	
//		System.out.println(new Counterpoisegrounding().verticalCounterpoise(200d, 1000d, 2d, 400d, 10000d, 0.8, 0.05, 0.05, 4d));//1.098欧
	
	public static void main(String[] args) {
		System.out.println(new Groundmat().verticalmat(250d, 3040d, 50d, 640d, 25600d, 0.6, 0.01, 0.025, 1d));
		System.out.println(new Groundmat().verticalmat(800d, 1500d, 2.5d, 280d, 4800d, 0.8, 0.02, 0.05, 62d));
		System.out.println(new Groundmat().gorizontalmat(1300d,144d, 0d, 72d, 324d, 0.8d, 0.05, 0d));
//		System.out.println(new Groundmat().generalmat(1300d, 324d));
		System.out.println(new Groundmat().gorizontalmat(1300d,600d, 0d, 200d, 2500d, 0.8d, 0.05, 0d));
//		System.out.println(new Groundmat().generalmat(1300d, 2500d));
		System.out.println("//////////////////////////");
//		System.out.println(new Groundmat().generalmat(1000d, 400d));
		System.out.println(new Groundmat().gorizontal(1000d, 0.05, 20d, 0.5, 1));
		System.out.println(new Groundmat().emissivitygorizontal(1000d, 20d, 80d, 22.0, 72.24318203));
		System.out.println(new Groundmat().verticalmat(1000d, 120d, 2d, 80d, 400d, 0.5, 0.05, 0.05, 2d));
		System.out.println(new Groundmat().verticalmat(1000d, 120d, 20d, 80d, 400d, 0.5, 0.05, 0.05, 2d));
		System.out.println(new Groundmat().verticalmat(1000d, 120d, 0d, 80d, 400d, 0.5, 0.05, 0d, 0d));
		System.out.println(new Groundmat().verticalmat(1000d, 120d, 2d, 80d, 400d, 0.5, 0.05, 0.05, 20d));
		System.out.println(new Groundmat().verticalmat(1000d, 120d, 3d, 80d, 400d, 0.5, 0.05, 0.05, 20d));
//		System.out.println(new Groundmat().generalmat(1000d, 750d));
		double a = Math.sqrt(2728d);
		System.out.println(new Groundmat().gorizontalmat(320d, Totallengthofmat.totallenth(a), 0d, 4 * a, 2728d, 0.5, 0.05, 0d));
	}
	
}
