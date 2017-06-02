package service.impl;

import org.springframework.stereotype.Service;

import service.Ground;
import util.convert.GroundModule;
import util.convert.Impulseconversion;
import util.manual.Vertical;
import dto.Countresult;

/**
 * 变电站/所接地设计
 * @author tc
 *
 */
@Service
public class PowerStation extends Building implements Ground{

	public PowerStation() {
	}

	/**
	 * 变电站/一类防雷建筑接地设计
	 * @param p		(上层)土壤电阻率
	 * @param H		上层土壤深度(0为单层)
	 * @param p1		下层土壤电阻率(0为单层)
	 * @param S		占地面积
	 * @param Rk		工频电阻要求值
	 * @param type	配电电压规模(500kv;220kv;110kv;66kv;35kv;20kv)(1:一类防雷建筑)
	 * @param city	土地资源是否受限
	 * @return
	 */
	@Override
	public Countresult design(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city) {
		Countresult cs;
		if(type == 1) {
			cs = getR(p, H, p1, S, Rk, 1, city);
		} else {
			cs = getR(p, H, p1, S, Rk, 2, city);
		}
		cs.setstyle(2);
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
			Ri = straightscheme(p, p1, H, S, cs, true);
			// 当Ri位于10—12之间，补加接地模块比替换成环形接地装置更优
			if (Ri > 12d)
				Ri = linkscheme(p, p1, H, S, cs, true);
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
		System.out.println("-----------为地网加装接地模块-------------------:");
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
	 * (DL/T 620-1997 7.16-7.17)
	 * 
	 * @return 是否设置独立防雷接地装置
	 */
	private boolean isindependent(Double p, Integer Rk) {
		if (Rk <= 66d)
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

	protected double straightscheme(Double p, Double p1, Double H, Double S, Countresult cs, boolean needRi) {
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
		new Vertical();
		double R = Vertical.straightverticals(p, p1, H, br, l, h, s, n);
		double Ra = 0;
		new Impulseconversion();
		// Ri:冲击接地电阻
		double Ri = Impulseconversion.convert(getpa(p, p1, H, h, l), (d / 2 + l) / le, R);
		// l最大不能超过地网等效直径或者40米
		for (; (l = l == 3.5 ? 3d : l) < (le = getle(p, p1, H, l) < 40d ? getle(p, p1, H, l) : 40d); l++) {
			// s的最大值需满足在有效长度之内
			for (s = l; s <= (le - l) * 2; s++) {
				// d最大不能超过地网等效直径或者有效长度
				for (d = s; d <= ((le - l) * 2 < dr ? (le - l) * 2 : dr); d++) {
					i++;
					new Impulseconversion();
					new Vertical();
					// 遍历所有可行方案，选择Ri达标且垂直接地体数量小于15个的方案
					if (Ri > (Ra = Impulseconversion.convert(getpa(p, p1, H, h, l), (d / 2 + l) / le,
							R = Vertical.straightverticals(p, p1, H, br, l, h, s, n = getni(d, s))))) {
						if(needRi) {
							if (Ri >= 10 && n <= 15) {
								Ri = Ra;
								li = l;
								si = s;
								di = d;
								ni = n;
								// System.out.println(Ri+"i:"+i);
							} else if(Ri < 10 && n <= ni) {
								Ri = Ra;
								li = l;
								si = s;
								di = d;
								ni = n;
							}
						} else {
							new Vertical();
							if(R > (Ra = Vertical.straightverticals(p, p1, H, br, l, h, s, n = getni(d, s)))) {
								R = Ra;
								li = l;
								si = s;
								di = d;
								ni = n;
							}
						}
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
		if(needRi) System.out.println("Ri:" + Ri);
		System.out.println("---------------------------------------------");
		if(needRi) {
			cs.setRi(Ri);
		} else {
			cs.setR(R);
		}
		cs.setindependent(1d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return needRi? Ri : R;
	}

	protected double linkscheme(Double p, Double p1, Double H, Double S, Countresult cs, boolean needRi) {
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
		new Vertical();
		double R = Vertical.linkverticals(p, p1, H, bc, l, h, s, n);
		new Impulseconversion();
		// Ri:冲击接地电阻
		double Ri = Impulseconversion.convert(getpa(p, p1, H, h, l), (getr(s, n) + l) / le, R);
		//l取2.5及大于2.5的正整数，小于接地体有效长度或40米
		for (; (l = l == 3.5 ? 3d : l) < (getle(p, p1, H, l) < 40d ? getle(p, p1, H, l) : 40d); l++) {
			//s需大于l的两倍，小于当n为3个时，s可达到最大值，s最大值构成的圆半径
			for (s = l * 2; s <= 2 * (le - l) * Math.sin(pi / 3); s++) {
				//为构成最基本的环形，n最小为3，最大值所构成的圆半径需小于建筑面积的等效圆半径和接地有效半径需且数量小于50个
				for (n = 3; getr(s, n) <=  (Math.sqrt(S / pi) < le - l? Math.sqrt(S / pi) : le - l) && n <= 50d; n++) {
					i++;
					if(needRi) {
						new Impulseconversion();
						new Vertical();
						if (Ri > (Ra = Impulseconversion.convert(getpa(p, p1, H, h, l), (getr(s, n) + l) / le,
								R = Vertical.linkverticals(p, p1, H, bc, l, h, s, n)))) {
							//若没有小于10欧姆的，则选择Ri最小的方案
							if (Ri >= 10d) {
								Ri = Ra;
								li = l;
								si = s;
								ni = n;
								// System.out.println("一Ri:"+Ri+"i:"+i);
								//若有小于10欧姆的，则在小于10欧的方案中选择耗材最小的
							} else if (Ra < 10d && li * ni > l * n) {
								Ri = Ra;
								li = l;
								si = s;
								ni = n;
								// System.out.println("二Ri:"+Ri+"i:"+i);
							}
						}
					} else {
						if(R > (Ra = Vertical.linkverticals(p, p1, H, bc, l, h, s, n))) {
							R = Ra;
							li = l;
							si = s;
							ni = n;
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
		if(needRi) System.out.println("Ri:" + Ri);
		System.out.println("---------------------------------------------");
		if(needRi) {
			cs.setRi(Ri);
		} else {
			cs.setR(R);
		}
		cs.setindependent(2d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return needRi? Ri : R;
	}


}
