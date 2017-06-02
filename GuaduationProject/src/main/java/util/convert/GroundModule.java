package util.convert;

/**
 * 接地模块计算
 * @author tc
 *
 */
public class GroundModule {
	//模块调整系数
	private static final double in = 0.75; 
	
	public static double getcount (double p, double Rw, double Rk) {
		double Rz =1 / ( 1 / Rk - 1 / Rw);
		double Rj = getRj(p);
		return Math.ceil(Rj / (Rz * in));
	}
	
	public static double getR(double p, double Rw, double count) {
		double Rnj = getRj(p) / (count * in);
		return 1 / (1 / Rw + 1 / Rnj);
	}
	
	private static double getRj (double p) {
		return  0.068 * (p / (0.4 * 0.6) - 2);
	}
	
}
