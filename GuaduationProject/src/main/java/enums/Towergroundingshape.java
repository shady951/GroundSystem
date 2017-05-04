package enums;

/**
 * 杆塔接地形状系数枚举
 * @author tc
 *
 */
public enum Towergroundingshape {
	one(1, 1.76),two(2, 2.0),three(3, -0.6),
	four(4, 1.0),five(5, 0.89);

	//index:形状序号
	private int indexT;
	//B:形状系数
	private double B;
	
	private Towergroundingshape(int indexT, double B) {
		this.indexT = indexT;
		this.B = B;
	}
	
	public static Towergroundingshape shapeOf(int indexT) {
		for(Towergroundingshape tds : values()) {
			if(tds.getindexT() == indexT) return tds;
		}
		return null;
	}

	private int getindexT() {
		return indexT;
	}

	public double getB() {
		return B;
	}
}
