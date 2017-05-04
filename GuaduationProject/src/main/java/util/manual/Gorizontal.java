package util.manual;

import enums.Gorizontalshape;

/**
 * 水平接地电阻计算模块
 * @author tc
 *
 */
public class Gorizontal {

	private static final double pi = Math.PI;

	public Gorizontal() {
	}
	
	/**
	 * 计算水平接地电阻
	 * @param p	土壤电阻率
	 * @param d	接地体等效直径
	 * @param l	接地体总长度
	 * @param h	埋深
	 * @param A	接地系数
	 * @return r	水平接地电阻
	 */
	public double gorizontal(Double p, Double d, Double l, Double h,  Integer indexG) {
		Double r = null;
		h += 0.001;
		Double B = Gorizontalshape.shapeOf(indexG).getB();
		Double n = Math.pow(l, 2) / (h * d);
		r = (Math.log(n) + B) * p / (2 * pi * l);
		return r;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new Gorizontal().gorizontal(500d, 0.05, 10d, 0.8d, 1));
//	}
}
