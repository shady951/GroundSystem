package util.manual;

import enums.Towergroundingshape;

/**
 * 杆塔接地电阻计算模块
 * @author tc
 *
 */
public class Tower {
	private static final double pi = Math.PI;

	public Tower() {
	}

	/**
	 * 计算杆塔接地电阻
	 * 
	 * @param p			土壤电阻率
	 * @param L1			单条接地体长度
	 * @param L2			单条接地体长度
	 * @param h			埋深
	 * @param d			水平接地体等效直径
	 * @param indexT形状系数的序号
	 * @return R			不同接地装置种类的接地电阻
	 */
	public double gorizontal(Double p, Double L1, Double L2, Double h, Double d, Integer indexT) {
		Double R = null;
		Double B = Towergroundingshape.shapeOf(indexT).getB();
		Double L = getL(L1, L2, indexT);
		Double N = Math.log(Math.pow(L, 2) / (h * d));
		R = p * (N + B) / (2 * pi * L);
		return R;
	}
	
	public double gorizontal(Double p, Double L, Double h, Double d) {
		return gorizontal(p, L/ 2, L/ 2, h, d, 3);
	}

	/**
	 * @return L	水平接地极总长度
	 */
	private Double getL(Double L1, Double L2, Integer index) {
		Double L = null;
		switch (index) {
		case 1:	L = 4 * (L1 + L2);
			break;
		case 2:	L = 4 * L1 + L2;
			break;
		case 3:	L = L1 + L2;
			break;
		case 4:	L = 2 * (L1 + L2);
			break;
		case 5:	L = 2 * (L1 + L2);
			break;
		}
		return L;
	}
	
	/**
	 * @return R 杆塔工频电阻要求值
	 */
	public static Double getR(Double p) {
		if(p <= 100) return 10d;
		if(p >= 2000d) return 30d;
		return 0.01 * p + 10d;
	}

	/**
	 * @return lt 杆塔放射形接地极每根最大长度
	 */
	public static Double Getlt(Double p) {
		if(p <= 1000d) return 40d;
		if(p >= 3000d) return 100d;
		return 0.02 * p + 40d;
	}
	
	
	public static void main(String[] args) {
		System.out.println(new Tower().gorizontal(1000d, 40d, 12d, 0.8, 0.05, 1));
	}
	
}
