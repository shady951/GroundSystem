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
		double modulecount;
		double K = 1d;
		if (R > Rk) {
			System.out.println("-----------为工频接地加装接地模块-------------------:");
			double Rt = R;
			modulecount = GroundModule.getcount(p, Rt, Rk);
			K = GroundModule.getR(p, Rt, modulecount) / Rt;
			cs.setmodulecount(modulecount);
		}
		Double Ri;
		//判断是否设置独立集中接地装置
		if(isindependent(p, type)) {
			//优先补加直线型集中接地装置，因为占地少
			Ri = straightscheme(p, p1, H, S, cs);
			//当Ri位于10—12之间，补加接地模块比替换成环形接地装置更优
			if(Ri > 12d) Ri = linkscheme(p, p1, H, S, cs);
			//Ri大于10补加接地模块
			if(Ri > 10d) {
				System.out.println("-----------为集中接地装置加装接地模块-------------------:");
				double Rit = Ri;
				double modulecounti = GroundModule.getcount(p, Rit, 10d);
				double Ki = GroundModule.getR(p, Rit, modulecounti) / Rit;
				Ri = Ri * Ki;
				cs.setmodulecounti(modulecounti);
			}
		} else {
			Ri = getRi(p,H, p1, cs);
			if (Ri * K > 10d) {
				System.out.println("-----------为冲击接地加装接地模块-------------------:");
				if(R > Rk) {
					double Rit = Ri;
					modulecount = GroundModule.getcount(p, Rit, 10d);
					K = GroundModule.getR(p, Rit, modulecount) / Rit;
					cs.setmodulecount(modulecount);
				}
//				System.out.println("接地模块数量modulecount:" + modulecount);
//				System.out.println("接地模块降阻率:" + K);
//				System.out.println("R:" + R);
//				System.out.println("Ri:" + Ri);
//				System.out.println("////////////////////////////////////////");
			}
			Ri = Ri * K;
		}
		R = R * K;
		cs.setR(R);
		cs.setRi(Ri);
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

	private double linkscheme(Double p, Double p1, Double H, Double S, Countresult cs) {
		double l = 2.5d;
		double s = l;
		double n = 3d;
		double le = getle(p, p1, H, l);
		double r = s * 0.5 / Math.sin(pi / n);
		double li = l;
		double si = s;
		double ni = n;
		double Ra;
		int i  = 0;
		double R = 0;
		double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l), (r+l) / le, new Vertical().linkverticals(p, p1, H, bc, l, h, s, n));
		for(; (l = l == 3.5? 3d : l) < (getle(p, p1, H, l) < 60d? getle(p, p1, H, l) : 60d); l++) {
			for(s = l * 2; s <= 2 * (le - l) * Math.sin(pi / 3); s++) {
				for(n = 3; Math.pow(s * 0.5 / Math.sin(pi / n), 2) * pi <= S && s * 0.5 / Math.sin(pi / n) <= le - l && n <= 50d; n++) {
					i++;
					if(Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (s * 0.5 / Math.sin(pi / n)+ l ) / le,
							R = new Vertical().linkverticals(p, p1, H, bc, l, h, s, n)))) {
						if(Ri >= 10d) {
							Ri = Ra;
							li = l;
							si = s;
							ni = n;
							System.out.println("一Ri:"+Ri+"i:"+i);
						} else if(Ra < 10d && si * ni > s * n) {
							Ri = Ra;
							li = l;
							si = s;
							ni = n;
							System.out.println("二Ri:"+Ri+"i:"+i);
						}
//						Ri = Ra;
//						li = l;
//						si = s;
//						ni = n;
//						System.out.println("Ri:"+Ri+"i:"+i);
					}
				}
			}
		}
		System.out.println(i);
		System.out.println("---------------------------------------------");
		System.out.println("le:"+ le);
		System.out.println("li:"+li);
		System.out.println("si:"+si) ;
		System.out.println("n:"+ ni);
//		System.out.println(new Impulseconversion().convert(p, (d+l) / le, R = new Vertical().straightverticals(p, 0.05, l, 0.8,s, Math.ceil(d / s) == d / s? Math.ceil(d / s) + 1 : Math.ceil(d / s)));
		System.out.println("R:"+  R);
		System.out.println("Ri:"+Ri);
//		System.out.println(new Impulseconversion().convert(getpa(p, p1, H, h, l1), (s1 * 0.5 / Math.sin(pi / 31d)+ l ) / le,new Vertical().linkverticals(p, p1, H, bc, l1, h, s1, 31d)));
		cs.setRi(Ri);
		cs.setindependent(2d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return Ri; 
	}
	
	public static void main(String[] args) {
//		new PowerStation().straightscheme(2500d, 1500d, 4d, 6000d);
//		new PowerStation().linkscheme(2500d, 1500d, 4d, 6000d);
	}
	
	
	private double getle(Double p, Double p1, Double H, Double l) {
		return 2 * Math.sqrt(getpa(p, p1, H, h, l));
	}
	
	
	private double straightscheme(Double p, Double p1, Double H, Double S, Countresult cs) {
		double r2 = Math.sqrt(S / pi) * 2;
		double l = 2.5d;
		double le = getle(p, p1, H, l);
		double s = l;
		double d = s;
		double li = l;
		double si = s;
		double d1 = d;
		int i  = 0;
		double R = 0;
		double Ra;
		double ni;
		double Ri = new Impulseconversion().convert(getpa(p, p1, H, h, l) , (d / 2 +l) / le, 
				new Vertical().straightverticals(p, p1, H, br, l, h,s,ni = Math.ceil(d / s) == d / s? Math.ceil(d / s) + 1 : Math.ceil(d / s)));
		for(; (l = l == 3.5? 3d : l) < (le = getle(p, p1, H, l) < 60d? getle(p, p1, H, l) : 60d); l++) {
			for(s = l  ; s <= (le - l) * 2; s++) {
				for(d = s; d <= ((le - l) * 2 < r2?  (le - l) * 2 : r2); d++) {
					i++;
					if(Ri > (Ra = new Impulseconversion().convert(getpa(p, p1, H, h, l), (d / 2 + l) / le, 
							R = new Vertical().straightverticals(p, p1, H, br, l, h,s,ni = Math.ceil(d / s) == d /s? Math.ceil(d / s) + 1 : Math.ceil(d / s))))) {
						if(ni <= 10) {
							Ri = Ra;
							li = l;
							si = s;
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
		System.out.println("le:"+ getle(p, p1, H, li));
		System.out.println("l1:"+li);
		System.out.println("s1:"+si) ;
		System.out.println("d1:"+d1) ;
		System.out.println("n:"+ (Math.ceil(d1 / si) == d1 / si? Math.ceil(d1 / si) + 1 : Math.ceil(d1 / si)));
		System.out.println("R:"+ R);
		System.out.println("Ri:"+Ri);
		cs.setRi(Ri);
		cs.setindependent(1d);
		cs.setl(li);
		cs.sets(si);
		cs.setni(ni);
		return Ri;
	}

	
}
