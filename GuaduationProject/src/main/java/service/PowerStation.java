package service;

import java.util.HashMap;
import java.util.Map;

import util.convert.Impulseconversion;
import util.convert.Totallengthofmat;
import util.manual.Groundmat;
import util.manual.Vertical;
import dto.Countresult;

public class PowerStation {

	private final double pi = Math.PI;
	// 埋深
	private final double h = 0.8;
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
	
	private double linkscheme() {
		
		return 0d; //TODO
	}
	
	private double getpa(Double p, Double p1, Double H, Double h, Double l) {
		Double l1 = null;
		Double l2 = null;
		Double N = null;
		if(H > h) {
			N = h + l;
			if(H > N) {
				l1 = l;
				l2  = 0d;
			} else {
				l1 = H - h;
				l2 = l - l1;
			}
		} else {
			l1 = 0d;
			l2 = l;
		}
		return p * p1 * l / (p * l2 + p1 * l1);
	}
	
	private double getle(Double p, Double p1, Double H, Double l) {
		return 2 * Math.sqrt(getpa(p, p1, H, h, l));
	}
	
	private double straightscheme(Double p, Double p1, Double H, Double S) {
		double r2 = Math.sqrt(S / pi) * 2;
		double l = 2d;
		double le = getle(p, p1, H, l);
		double s = 1;
		double d = 1;
		double l1 = 1;
		double s1 = 1;
		double d1 = 1;
		int i  = 0;
		double R = 0;
		double Ra;
		double n;
		double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l) , (d / 2 +l) / le, 
				new Vertical().straightverticals(p, p1, H, br, l, h,s,n = Math.ceil(d / s) == d / s? Math.ceil(d / s) + 1 : Math.ceil(d / s)));
		for(; l < (le = getle(p, p1, H, l) < 60d? getle(p, p1, H, l) : 60d); l++) {
			for(s = l  ; s <= (le - l) * 2; s++) {
				for(d = s; d <= ((le - l) * 2 < r2?  (le - l) * 2 : r2); d++) {
					i++;
					if(Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (d / 2 + l) / le, 
							R = new Vertical().straightverticals(p, p1, H, br, l, h,s,n = Math.ceil(d / s) == d /s? Math.ceil(d / s) + 1 : Math.ceil(d / s))))) {
						if(n < 20) {
							Ri = Ra;
							l1 = l;
							s1 = s;
							d1 = d;
						}
						System.out.println(Ri+"i:"+i);
					}
				}
			}
		}
		System.out.println("i:"+i);
		System.out.println("---------------------------------------------");
		System.out.println("r2:"+r2);
		System.out.println("le:"+ getle(p, p1, H, l1));
		System.out.println("l1:"+l1);
		System.out.println("s1:"+s1) ;
		System.out.println("d1:"+d1) ;
		System.out.println("n:"+ (Math.ceil(d1 / s1) == d1 / s1? Math.ceil(d1 / s1) + 1 : Math.ceil(d1 / s1)));
		System.out.println("R:"+ R);
		System.out.println("Ri:"+Ri);
		return 0d; // TODO
	}

	private Double getS(Double S, Double m) {
		return S + 2 * m * Math.sqrt(S) + Math.pow(m, 2);
	}
	public static void main(String[] args) {
		new PowerStation().straightscheme(800d, 500d, 3d, 2000d);
	}
	
	
}
