package util.convert;

/**
 * 接地模块计算
 * @author tc
 *
 */
public class GroundModule {
	//模块调整系数
	private static final double in = 0.75; 
	
	/**
	 * 计算接地模块所需数量
	 * @param p		土壤电阻率
	 * @param Rw	原电阻值
	 * @param Rk		电阻要求值
	 * @return n		数量(个)
	 */
	public static double getcount (double p, double Rw, double Rk) {
		double Rz =1 / ( 1 / Rk - 1 / Rw);
		double Rj = getRj(p);
		return Math.ceil(Rj / (Rz * in));
	}
	
	/**
	 * 加上接地模块后得到的电阻
	 * @param p			土壤电阻率
	 * @param Rw		原电阻值
	 * @param count	接地模块数量
	 * @return R
	 */
	public static double getR(double p, double Rw, double count) {
		double Rnj = getRj(p) / (count * in);
		return 1 / (1 / Rw + 1 / Rnj);
	}
	
	private static double getRj (double p) {
		return  0.068 * (p / (0.4 * 0.6) - 2);
	}
	
}
