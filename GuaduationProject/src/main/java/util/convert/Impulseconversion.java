package util.convert;

/**
 * 冲击系数换算模块
 * @author tc
 *
 */
public class Impulseconversion {

	public Impulseconversion() {
	}

	/**
	 * 计算冲击系数
	 * @param p	土壤电阻率
	 * @param Lk	接地体实际长度与有效长度的比值
	 * @param R	有效长度下的工频电阻值
	 * @return Ri	冲击接地电阻值
	 */
	public Double convert(Double p, Double Lk, Double R) {
		if(p > 3000) p = 3000d;
		Double Ri = null;
		Double k = getk(p);
		//取(1,1)带入得b值
		Double b = 1 - k * 1;
		Ri = R / (k * Lk + b);
//		Ri = k * Lk + b;
		return Ri;
	}
	
	/**
	 * @return k	不同土壤电阻率下，关于冲击系数为y与接地体实际长度与有效长度
	 * 的比值为x，所做的出的直线的斜率
	 */
	private Double getk(Double p) {
		Double k = null;
		k = -0.001 * p - 0.25;
		return k;
	}
	
	public static void main(String[] args) {
		System.out.println(new Impulseconversion().convert(500d, 0.5, 1d));//1.375
		System.out.println(new Impulseconversion().convert(700d, 0.5, 1d));//1.475
		System.out.println(new Impulseconversion().convert(1000d, 0.5, 1d));//1.625
		System.out.println(new Impulseconversion().convert(1300d, 0.5, 1d));//1.775
		System.out.println(new Impulseconversion().convert(1500d, 0.5, 1d));//1.875
		System.out.println(new Impulseconversion().convert(1700d, 0.5, 1d));//1.975
		System.out.println(new Impulseconversion().convert(2000d, 0.5, 1d));//2.125
		System.out.println(new Impulseconversion().convert(3001d, 0.5, 1d));//2.125
		System.out.println(new Impulseconversion().convert(2500d, 0.38, 85d));
	}
	
	
}
