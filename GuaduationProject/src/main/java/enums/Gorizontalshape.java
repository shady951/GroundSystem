package enums;

/**
 * 水平接地形状系数枚举 
 * @author tc
 *
 */
public enum Gorizontalshape {
	one(1, -0.6),two(2, -0.18),three(3, 0),
	four(4, 0.48),five(5, 0.89),six(6, 1d),
	seven(7, 2.19),eight(8, 3.03),nine(9, 4.71),
	ten(10, 5.65);
	
	//index:形状序号
	private int indexG;
	//B:形状系数
	private double B;
	
	private Gorizontalshape(int indexG, double B) {
		this.indexG = indexG;
		this.B = B;
	}
	
	public static Gorizontalshape shapeOf(int indexG) {
		for(Gorizontalshape gs : values()) {
			if(gs.getindexG() == indexG) return gs;
		}
		return null;
	}

	private int getindexG() {
		return indexG;
	}

	public double getB() {
		return B;
	}
}
