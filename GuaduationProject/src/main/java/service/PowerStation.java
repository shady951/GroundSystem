package service;

import util.convert.GroundModule;
import util.convert.Impulseconversion;
import util.manual.Vertical;
import dto.Countresult;

public class PowerStation extends Building{

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
	@Override
	public Countresult design(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city) {
		Countresult cs = getR(p, H, p1, S, Rk, 1, city);
		double R = cs.getR();
		double modulecount = 0;
		double K = 1d;
		if (R > Rk) {
			double Rt = R;
			modulecount = GroundModule.getcount(p, Rt, Rk);
			K = GroundModule.getR(p, Rt, modulecount) / Rt;
			cs.setmodulecount(modulecount);
		}
		Double Ri;
		//判断是否设置独立集中接地装置
		if(isindependent(p, type)) {
			System.out.println("-------------------按DL/T 620-1997. 7.16-7.17需增设集中接地装置-----------------------");
			//优先补加直线型集中接地装置，因为占地少
			Ri = straightscheme(p, p1, H, S, cs);
			//当Ri位于10—12之间，补加接地模块比替换成环形接地装置更优
			if(Ri > 12d) Ri = linkscheme(p, p1, H, S, cs);
			//Ri大于10补加接地模块
			if(cs.getindependent() == 1) {
				System.out.println("加装直线型等距垂直接地");
			} else {
				System.out.println("加装环型等距垂直接地");
			}
			System.out.println("垂直接地体长度l:"+cs.getl());
			System.out.println("垂直接地体间距s:"+cs.gets());
			System.out.println("垂直接地体数量ni:"+cs.getni());
			System.out.println("集中接地装置冲击接地电阻Ri:"+cs.getRi());
			System.out.println("////////////////////////////////////////");
			if(Ri > 10d) {
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
			Ri = getRi(p,H, p1, cs);
			if (Ri * K > 10d) {
				if(R > Rk) {
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
		System.out.println("最终R:"+R);
		System.out.println("最终Ri:"+Ri);
		return cs;
	}
	
	/**
	 *	(DL/T 620-1997. 7.16-7.17) 
	 * @return 是否设置独立防雷接地装置
	 */
	private boolean isindependent(Double p, Integer Rk) {
		if(Rk <= 35d) return true;
		if(Rk == 66d && p >= 500d) return true;
		if(Rk >= 110d && p >= 1000d) return true;
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
		return Math.ceil(d / s) == d / s? Math.ceil(d / s) + 1 : Math.ceil(d / s);
	}
	
	/**
	 * @return 双层土壤视在电阻率下的接地体有效长度
	 */
	private double getle(Double p, Double p1, Double H, Double l) {
		return 2 * Math.sqrt(getpa(p, p1, H, h, l));
	}
	
	private double linkscheme(Double p, Double p1, Double H, Double S, Countresult cs) {
			double l = 2.5d;
			double s = l;
			double n = 3d;
			double le = getle(p, p1, H, l);
			double li = l;
			double si = s;
			double ni = n;
			double Ra;
			int i  = 0;
			double R = 0;
			double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l), (getr(s, n)+l) / le, new Vertical().linkverticals(p, p1, H, bc, l, h, s, n));
			for(; (l = l == 3.5? 3d : l) < (getle(p, p1, H, l) < 60d? getle(p, p1, H, l) : 60d); l++) {
				for(s = l * 2; s <= 2 * (le - l) * Math.sin(pi / 3); s++) {
					for(n = 3; Math.pow(getr(s, n), 2) * pi <= S && getr(s, n) <= le - l && n <= 50d; n++) {
						i++;
						if(Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (getr(s, n)+ l ) / le,
								R = new Vertical().linkverticals(p, p1, H, bc, l, h, s, n)))) {
							if(Ri >= 10d) {
								Ri = Ra;
								li = l;
								si = s;
								ni = n;
	//							System.out.println("一Ri:"+Ri+"i:"+i);
							} else if(Ra < 10d && si * ni > s * n) {
								Ri = Ra;
								li = l;
								si = s;
								ni = n;
	//							System.out.println("二Ri:"+Ri+"i:"+i);
							}
						}
					}
				}
			}
			System.out.println("---------------------------------------------");
			System.out.println("环形排列的有效方案一共i:"+i+"个");
			System.out.println("接地体有效长度le:"+ getle(p, p1, H, li));
			System.out.println("接地体实际最大长度:"+ getr(si, ni) + li);
			System.out.println("l:"+li);
			System.out.println("s:"+si) ;
			System.out.println("n:"+ ni);
			System.out.println("R:"+  R);
			System.out.println("Ri:"+Ri);
			System.out.println("---------------------------------------------");
			cs.setRi(Ri);
			cs.setindependent(2d);
			cs.setl(li);
			cs.sets(si);
			cs.setni(ni);
			return Ri; 
		}

	private double straightscheme(Double p, Double p1, Double H, Double S, Countresult cs) {
		double dr = Math.sqrt(S / pi) * 2;
		double l = 2.5d;
		double le = getle(p, p1, H, l);
		double s = l;
		double d = s;
		double li = l;
		double si = s;
		double di = d;
		int i  = 0;
		double R = 0;
		double Ra;
		double ni;
		double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l) , (d / 2 +l) / le, 
				new Vertical().straightverticals(p, p1, H, br, l, h,s,ni = getni(d, s)));
		for(; (l = l == 3.5? 3d : l) < (le = getle(p, p1, H, l) < 60d? getle(p, p1, H, l) : 60d); l++) {
			for(s = l  ; s <= (le - l) * 2; s++) {
				for(d = s; d <= ((le - l) * 2 < dr?  (le - l) * 2 : dr); d++) {
					i++;
					if(Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (d / 2 + l) / le, 
							R = new Vertical().straightverticals(p, p1, H, br, l, h,s,ni = getni(d, s))))) {
						if(ni <= 10) {
							Ri = Ra;
							li = l;
							si = s;
							di = d;
						}
//						System.out.println(Ri+"i:"+i);
					}
				}
			}
		}
		System.out.println("---------------------------------------------");
		System.out.println("直线排列的有效方案一共i:"+i+"个");
		System.out.println("接地体最大排列长度dr:"+dr);
		System.out.println("接地体有效长度le:"+ getle(p, p1, H, li));
		System.out.println("接地体实际最大长度:"+ di);
		System.out.println("l:"+li);
		System.out.println("s:"+si) ;
		System.out.println("n:"+ getni(di, si));
		System.out.println("R:"+ R);
		System.out.println("Ri:"+Ri);
		System.out.println("---------------------------------------------");
		cs.setRi(Ri);
		cs.setindependent(1d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return Ri;
	}

	public static void main(String[] args) {
//		new PowerStation().straightscheme(2500d, 1500d, 4d, 6000d);
		System.out.println(new PowerStation().design(2400d, 2d, 1500d, 4000d, 10d, 110, true));
	}
	
}
