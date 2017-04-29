package service;

import java.util.Map;

import util.convert.Totallengthofmat;
import util.manual.Groundmat;
import dto.Countresult;

public class PowerStation {

	private final double pi = Math.PI;
	// 埋深
	private final double h = 0.7;
	// 水平接地体等效直径
	private final double bc = 0.05;
	// 垂直接地体等效直径
	private final double br = 0.05;

	public PowerStation() {
	}

	/**
	 * 变电站/所接地设计
	 * @param p
	 * @param H
	 * @param p1
	 * @param S
	 * @param Rk
	 * @param type		配电电压规模
	 * @param city
	 * @return
	 */
	public Countresult design(Double p, Double H, Double p1, Double S, Integer Rk, Integer type, boolean city) {
		System.out.println("city:" + city);
		System.out.println("最初面积S:" + getS(S, 2d));
		System.out.println("最大面积S:" + 2 * S);
		Double R = null;
		Double Ri = null;
		// 等效圆半径
		Double r = Math.sqrt(S / pi);
		System.out.println("等效圆半径r:" + r);
		// 等效方形边长
		Double a = Math.sqrt(S);
		System.out.println("等效方形边长a:" + a);
		double n = Totallengthofmat.amount(a);
		Map<String, Double> map;
		// 外延环形接地体距离，最小1米
		Double m = 2d;
		// 建筑一米布置接环形接地体
		if (new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a), 0d, a * 4, getS(S, m), h, bc, 0d) > Rk && !city) {
			double i = 1;
			for (; getS(S, m + i) < 2 * S && new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a),
						0d, a * 4, getS(S, m + i), h, bc, 0d) > Rk; i++);
			m += i;
		}
		S = getS(S, m);
		R = new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a), 0d, a * 4, S, h, bc, 0d);
		System.out.println("最终面积S:" + S);
		System.out.println("外延距离:" + m / 2);
		System.out.println("R最初:" + R);
		Integer flag = null;
		Double lr = null;
		Double lv = null;
		return null;// TODO
	}

	private Double getS(Double S, Double m) {
		return S + 2 * m * Math.sqrt(S) + Math.pow(m, 2);
	}

}
