package service;

import util.convert.GroundModule;
import util.convert.Impulseconversion;
import util.manual.Vertical;
import dto.Countresult;

public class PowerStation extends Building {

	public PowerStation() {
	}

	/**
	 * 变电站/所接地设计
	 * 
	 * @param p
	 * @param H
	 * @param p1
	 * @param S
	 * @param Rk
	 * @param type	配电电压规模
	 * @param city
	 * @return
	 */
	@Override
	public Countresult design(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city) {
		Countresult cs = getR(p, H, p1, S, Rk, 1, city);
		double R = cs.getR();
		double modulecount = 0;
		double K = 1d;
		// R没达标，补加接地模块
		if (R > Rk) {
			double Rt = R;
			modulecount = GroundModule.getcount(p, Rt, Rk);
			K = GroundModule.getR(p, Rt, modulecount) / Rt;
			cs.setmodulecount(modulecount);
		}
		Double Ri;
		// 判断是否设置独立集中接地装置
		if (isindependent(p, type)) {
			System.out.println("-------------------按DL/T 620-1997. 7.16-7.17需增设集中接地装置-----------------------");
			//变电站/所集中接地装置冲击电阻需小于10欧姆
			// 优先补加直线型集中接地装置，因为占地少
			Ri = straightscheme(p, p1, H, S, cs);
			// 当Ri位于10—12之间，补加接地模块比替换成环形接地装置更优
			if (Ri > 12d)
				Ri = linkscheme(p, p1, H, S, cs);
			if (cs.getindependent() == 1) {
				System.out.println("加装直线型等距垂直接地");
			} else {
				System.out.println("加装环型等距垂直接地");
			}
			System.out.println("垂直接地体长度l:" + cs.getl());
			System.out.println("垂直接地体间距s:" + cs.gets());
			System.out.println("垂直接地体数量ni:" + cs.getni());
			System.out.println("集中接地装置冲击接地电阻Ri:" + cs.getRi());
			System.out.println("////////////////////////////////////////");
			// Ri大于10，为集中接地装置补加接地模块
			if (Ri > 10d) {
				System.out.println("-----------为集中接地装置加装接地模块-------------------:");
				double Rit = Ri;
				double modulecounti = GroundModule.getcount(p, Rit, 10d);
				double Ki = GroundModule.getR(p, Rit, modulecounti) / Rit;
				Ri = Ri * Ki;
				cs.setmodulecounti(modulecounti);
				cs.setRi(Ri);
				System.out.println("接地模块数量modulecounti:" + modulecounti);
				System.out.println("接地模块降阻率:" + Ki);
				System.out.println("集中接地装置冲击接地电阻Ri:" + Ri);
				System.out.println("////////////////////////////////////////");
			}
		} else {
			Ri = getRi(p, H, p1, cs);
			// Ri * K依然大于10，为接地网补加接地模块
			if (Ri * K > 10d) {
				if (R > Rk) {
					double Rit = Ri;
					modulecount = GroundModule.getcount(p, Rit, 10d);
					K = GroundModule.getR(p, Rit, modulecount) / Rit;
					cs.setmodulecount(modulecount);
				}
			}
			Ri = Ri * K;
		}
		R = R * K;
		cs.setR(R);
		cs.setRi(Ri);
		System.out.println("-----------为工频接地加装接地模块-------------------:");
		System.out.println("接地模块数量modulecount:" + modulecount);
		System.out.println("接地模块降阻率:" + K);
		System.out.println("R:" + R);
		System.out.println("Ri:" + Ri);
		System.out.println("////////////////////////////////////////");
		System.out.println("最终R:" + R);
		System.out.println("最终Ri:" + Ri);
		return cs;
	}

	/**
	 * (DL/T 620-1997. 7.16-7.17)
	 * 
	 * @return 是否设置独立防雷接地装置
	 */
	private boolean isindependent(Double p, Integer Rk) {
		if (Rk <= 35d)
			return true;
		if (Rk == 66d && p >= 500d)
			return true;
		if (Rk >= 110d && p >= 1000d)
			return true;
		return false;
	}

	/**
	 * @return 环形接地间距为s，数量为n所构成的圆的半径
	 */
	private double getr(Double s, Double n) {
		return s * 0.5 / Math.sin(pi / n);
	}

	/**
	 * @return 由直线距离与接地体间距得出的最大接地体数量
	 */
	private double getni(Double d, Double s) {
		return Math.ceil(d / s) == d / s ? Math.ceil(d / s) + 1 : Math.ceil(d / s);
	}

	/**
	 * @return 双层土壤视在电阻率下的接地体有效长度
	 */
	private double getle(Double p, Double p1, Double H, Double l) {
		return 2 * Math.sqrt(getpa(p, p1, H, h, l));
	}

	private double straightscheme(Double p, Double p1, Double H, Double S, Countresult cs) {
		// dr:地网等效直径，作为集中接地装置的最大布设距离
		double dr = Math.sqrt(S / pi) * 2;
		// l:接地体长度
		double l = 2.5d;
		// s:接地体间距
		double s = l;
		// d:接地体布设距离
		double d = s;
		// n:接地体数量
		double n = getni(d, s);
		// le:接地体有效长度
		double le = getle(p, p1, H, l);
		double li = l;
		double si = s;
		double di = d;
		double ni = n;
		// i:方案计数
		int i = 0;
		double R = 0;
		double Ra = 0;
		// Ri:冲击接地电阻
		double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l), (d / 2 + l) / le,
				new Vertical().straightverticals(p, p1, H, br, l, h, s, n));
		// l最大不能超过地网等效直径或者60米
		for (; (l = l == 3.5 ? 3d : l) < (le = getle(p, p1, H, l) < 60d ? getle(p, p1, H, l) : 60d); l++) {
			// s的最大值需满足在有效长度之内
			for (s = l; s <= (le - l) * 2; s++) {
				// d最大不能超过地网等效直径或者有效长度
				for (d = s; d <= ((le - l) * 2 < dr ? (le - l) * 2 : dr); d++) {
					i++;
					// 遍历所有可行方案，选择Ri最小且垂直接地体数量小于15个的方案
					if (Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (d / 2 + l) / le,
							R = new Vertical().straightverticals(p, p1, H, br, l, h, s, n = getni(d, s))))) {
						if (n <= 15) {
							Ri = Ra;
							li = l;
							si = s;
							di = d;
							ni = n;
						}
						// System.out.println(Ri+"i:"+i);
					}
				}
			}
		}
		System.out.println("---------------------------------------------");
		System.out.println("直线排列的有效方案一共i:" + i + "个");
		System.out.println("接地体最大排列长度dr:" + dr);
		System.out.println("接地体有效长度le:" + getle(p, p1, H, li));
		System.out.println("接地体实际最大长度:" + di);
		System.out.println("l:" + li);
		System.out.println("s:" + si);
		System.out.println("n:" + ni);
		System.out.println("R:" + R);
		System.out.println("Ri:" + Ri);
		System.out.println("---------------------------------------------");
		cs.setRi(Ri);
		cs.setindependent(1d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return Ri;
	}

	private double linkscheme(Double p, Double p1, Double H, Double S, Countresult cs) {
		// l:接地体长度
		double l = 2.5d;
		// s:接地体间距
		double s = l;
		// n:接地体数量
		double n = 3d;
		// le:接地体有效长度
		double le = getle(p, p1, H, l);
		double li = l;
		double si = s;
		double ni = n;
		double Ra;
		int i = 0;
		double R = 0;
		// Ri:冲击接地电阻
		double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l), (getr(s, n) + l) / le,
				new Vertical().linkverticals(p, p1, H, bc, l, h, s, n));
		//l取2.5及大于2.5的正整数，小于接地体有效长度或60米
		for (; (l = l == 3.5 ? 3d : l) < (getle(p, p1, H, l) < 60d ? getle(p, p1, H, l) : 60d); l++) {
			//s需大于l的两倍，小于当n为3个时，s可达到最大值，s最大值构成的圆半径
			for (s = l * 2; s <= 2 * (le - l) * Math.sin(pi / 3); s++) {
				//为构成最基本的环形，n最小为3，最大值所构成的圆半径需小于建筑面积的等效圆半径和接地等效半径需且数量小于50个
				for (n = 3; getr(s, n) <=  (Math.sqrt(S / pi) < le - l? Math.sqrt(S / pi) : le - l) && n <= 50d; n++) {
					i++;
					if (Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (getr(s, n) + l) / le,
							R = new Vertical().linkverticals(p, p1, H, bc, l, h, s, n)))) {
						//若没有小于10欧姆的，则选择Ri最小的方案
						if (Ri >= 10d) {
							Ri = Ra;
							li = l;
							si = s;
							ni = n;
							// System.out.println("一Ri:"+Ri+"i:"+i);
							//若有小于10欧姆的，则在小于10欧的方案中选择耗材最小的
						} else if (Ra < 10d && si * ni > s * n) {
							Ri = Ra;
							li = l;
							si = s;
							ni = n;
							// System.out.println("二Ri:"+Ri+"i:"+i);
						}
					}
				}
			}
		}
		System.out.println("---------------------------------------------");
		System.out.println("环形排列的有效方案一共i:" + i + "个");
		System.out.println("接地体有效长度le:" + getle(p, p1, H, li));
		System.out.println("接地体实际最大长度:" + getr(si, ni) + li);
		System.out.println("l:" + li);
		System.out.println("s:" + si);
		System.out.println("n:" + ni);
		System.out.println("R:" + R);
		System.out.println("Ri:" + Ri);
		System.out.println("---------------------------------------------");
		cs.setRi(Ri);
		cs.setindependent(2d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return Ri;
	}

	public static void main(String[] args) {
		// new PowerStation().straightscheme(2500d, 1500d, 4d, 6000d);
		System.out.println(new PowerStation().design(2400d, 2d, 1500d, 4000d, 10d, 110, true));
	}

}
