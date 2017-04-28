package util.convert;

/**
 * 地网总长度计算模块
 * @author tc
 *
 */
public class Totallengthofmat {

	public Totallengthofmat() {
	}
	
	/**
	 * 计算地网总长度
	 * @param a	地网等效边长
	 * @return Ls
	 */
	public static double totallenth(Double a) {
		Double Ls = null;
		Ls = 4 * a + (Math.ceil(a / 10) - 1) * 2 * a;
		return Ls;
	}
	
	/**
	 * 计算地网铺设间距
	 * @param a	地网等效边长
	 * @return l
	 */
	public static double singlelength(Double a) {
		Double l = null;
		l = a / Math.ceil(a / 10);
		return l;
	}
	
	/**
	 * 计算网点数
	 * @param a	地网等效边长
	 * @return n
	 */
	public static double amount(Double a) {
		Double n = null;
		n = Math.pow(Math.ceil(a / 10) + 1, 2);
		return n;
	}
	
//	public static void main(String[] args) {
//		System.out.println(new Totallengthofmat().amount(25d));
//	}
}
