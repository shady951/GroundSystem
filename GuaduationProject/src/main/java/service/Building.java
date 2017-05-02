package service;

import java.util.HashMap;
import java.util.Map;

import dto.Countresult;
import util.convert.GroundModule;
import util.convert.Impulseconversion;
import util.convert.Totallengthofmat;
import util.manual.Doubledeckground;
import util.manual.Groundmat;

public class Building{

	//圆周率
	public final double pi;
	//地网或垂直接地体顶部埋深
	public final double h;
	//接地体水平等效直径
	public final double bc;
	//接地体垂直等效直径
	public final double br;
	
	public Building() {
		this.pi = Math.PI;
		this.h = 0.8;
		this.bc = 0.05;
		this.br = 0.05;
	}

	/**
	 * 普通建筑接地设计
	 * @param p		(上层)土壤电阻率
	 * @param H		上层土壤深度(0为单层)
	 * @param p1		下层土壤电阻率(0为单层)
	 * @param S		占地面积
	 * @param Rk		工频电阻要求值
	 * @param type	防雷建筑分类
	 * @param city	土地资源是否受限
	 * @return
	 */
	public Countresult design(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city) {
		Countresult cs = getR(p, H, p1, S, Rk, type, city);
		double R = cs.getR();
		double Ri = getRi(p,H, p1, cs);
		double modulecount = 0;
		// 考虑是否加装接地模块
		if (R > Rk || Ri > 10d) {
			System.out.println("-----------加装接地模块-------------------:");
			double K = 1d;
			if(R > Rk) {
				double Rt = R;
				modulecount = GroundModule.getcount(p, Rt, Rk);
				K = GroundModule.getR(p, Rt, modulecount) / Rt;
			}
			// 加装模块后的冲击接地电阻是否符合要求
			if (Ri * K > 10d) {
				double Rit = Ri;
				if (type != 3) {
					modulecount = GroundModule.getcount(p, Rit, 10d);
					K = GroundModule.getR(p, Rit, modulecount) / Rit;
				} else if (Ri * K > 30d) {
					modulecount = GroundModule.getcount(p, Rit, 30d);
					K = GroundModule.getR(p, Rit, modulecount) / Rit;
				}
			}
			cs.setmodulecount(modulecount);
			R = R * K;
			Ri = Ri * K;
			System.out.println("接地模块数量modulecount:" + modulecount);
			System.out.println("接地模块降阻率:" + K);
			System.out.println("////////////////////////////////////////");
		}
		System.out.println("最终工频接地电阻R:"+R);
		System.out.println("最终冲击接地电阻Ri:"+Ri);
		cs.setR(R);
		cs.setRi(Ri);
		return cs;// TODO
	}

	protected Countresult getR(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city) {
		System.out.println("建筑面积S:"+getS(S, 2d));
		if(city){
			System.out.println("在城市，无法扩大地网面积");
		} else {
			System.out.println("不在城市，可扩大地网面积");
			System.out.println("最大面积S:"+2 * S);
		}
		Double Sp = getsinglep(p, H, p1);
		Double R = null;
		Map<String, Double> map;
		// 外延环形接地体距离，最小1米
		Double m = 2d;
		// 建筑一米布置接环形接地体
		if (new Groundmat().gorizontalmat(Sp, Totallengthofmat.totallenth(Math.sqrt(getS(S, m))), 0d, Math.sqrt(getS(S, m)) * 4, getS(S, m), h, bc, 0d) > Rk && !city) {
			for (; getS(S, m) < 2 * S && new Groundmat().gorizontalmat(Sp, Totallengthofmat.totallenth(Math.sqrt(getS(S, m))),
					0d, Math.sqrt(getS(S, m)) * 4, getS(S, m), h, bc, 0d) > Rk; m++);
		}
		S = getS(S, m);
		// 等效圆半径
		Double r = Math.sqrt(S / pi);
		System.out.println("等效圆半径r:" + r);
		// 等效方形边长
		Double a = Math.sqrt(S);
		System.out.println("等效方形边长a:" + a);
		Double Ls = Totallengthofmat.totallenth(a);
		Double n = Totallengthofmat.amount(a);
		R = new Groundmat().gorizontalmat(Sp, Ls, 0d, a * 4, S, h, bc, 0d);
		System.out.println("外延距离:" + m/2);
		System.out.println("最终地网面积S:" + S);
		System.out.println("工频接地电阻R:" + R);
		Integer flag = null;
		Double lr = null;
		Double lv = null;
		// 防雷规范2.5，是否补加接地体
		map = getl(Sp, r, type);
		if (!map.isEmpty()) {
			System.out.println("--------------按规范补加接地体:----------------------");
			// 补加水平接地体总长
			lr = map.get("lr");
			// 补加垂直接地体
			lv = map.get("lv");
			double Rv;
			System.out.println("lv:"+lv);
			System.out.println("n:"+n);
			if (H == 0d) {
				System.out.println("按单层土壤计算");
				Rv = new Groundmat().verticalmat(p , Ls, lv, a * 4, S, h, bc, br, n);
			} else {
				System.out.println("按双层土壤计算");
				Rv = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Ls, a * 4, lv, n, bc, br);
			}
			if (!city) {
				System.out.println("未在城市,比较水平接地体与垂直接地体");
				double Rr = new Groundmat().emissivitygorizontal(Sp, a, lr, new Groundmat().gorizontalmat(Sp, Ls,
						0d, a * 4, S, h, bc, 0d), new Groundmat().gorizontal(Sp, bc, lr / 4, h, 1) / 4);
				System.out.println("lr:"+lr);
				System.out.println("Rr:"+Rr+"  Rv:"+Rv);
				if (Rr > Rv) {
					System.out.println("补加"+lv+"米垂直接地体");
					R = Rv;
					flag = 2;
				} else {
					System.out.println("补加"+lr+"米水平接地体");
					R = Rr;
					flag = 1;
				}
			} else {
				System.out.println("在城市,只能补加垂直接地体");
				System.out.println("补加"+lv+"米垂直接地体");
				R = Rv;
				flag = 2;
			}
			System.out.println("flag:"+flag);
			System.out.println("工频接地电阻R:"+R);
			System.out.println("////////////////////////////////////////");
		}
		// 工频电阻是否满足要求
		if (R > Rk) {
			System.out.println("--------------地网改进:----------------");
			lv = 2.5;
			Double Rv1 = null;
			Double Rv2 = null;
			if(H == 0d) {
				System.out.println("单层土壤");
				Rv1 = new Groundmat().verticalmat(p, Ls, lv, a * 4, S, h, bc, br, n);
			} else {
				System.out.println("双层土壤");
				Rv1 = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Ls, a * 4, lv, n, bc, br);
			}
			System.out.println("补加"+lv+"米垂直接地体电阻Rv1:"+Rv1);
			if(H > h && H < a / 3 && p >= p1 * 1.2 && Rv1 > Rk) {
				System.out.println("下层土壤电阻率小于上层土壤电阻率的80%，可补加长垂直接地体");
				double iv = Math.ceil(H) <= 3d? 3d : Math.ceil(H);
				Double N = 4d;
				boolean f = false;
				for(;(Rv2 = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Ls, a * 4, iv,
						N = Math.ceil(a * 4 / (iv * 2 )) < 4d? 4d : Math.ceil(a * 4 / (iv * 2 )), bc, br)) > Rk && iv <= a && iv <= 60d; iv++) {
					if(Rv2 < Rv1 && iv < 2 * Math.sqrt(getpa(p, p1, H + r, h, iv + r)) - r) {
						Rv1 = Rv2;
						lv = iv;
						n = N;
						f =true;
						if(Rv2 < Rk) {
							break;
						}
					}
				}
				if(f) {
				System.out.println("长垂直接地体最大有效长度：" + (2 * Math.sqrt(getpa(p, p1, H + r, h, lv + r)) - r));
				System.out.println("补加"+lv+"米长垂直接地体个数n:" + n+"个");
				System.out.println("补加长垂直接地体后的接地电阻Rv1:"+ Rv1);
				} else {
					System.out.println("经计算补加长垂直接地体不可行");
				}
			}
			if (!city) {
				System.out.println("未在城市，可补加水平接地体");
				double ir = 1d;
				if (lr != null) ir = Math.ceil(lr);
				Double Rr = null;
				for (; (Rr = new Groundmat().emissivitygorizontal(Sp, a, ir, new Groundmat().gorizontalmat(Sp, Ls,
						0d, a * 4, S, h, bc, 0d), new Groundmat().gorizontal(Sp, bc, ir / 4, h, 1) / 4)) > Rk && ir < 4 * r; ir++);
//				System.out.println("可补加"+ir+"米水平接地体电阻Rr:"+Rr);
				System.out.println("可补加4根"+ir/4+"米水平接地体电阻Rr:"+Rr);
				if(Rr > Rk) {
					for (; (Rr = new Groundmat().emissivitygorizontal(Sp, a, ir, Rv1,
							new Groundmat().gorizontal(Sp, bc, ir / 4, h, 1) / 4)) > Rk && ir < 4 * r; ir++);
					flag = 3;
					System.out.println("以水平为主的地网补加水平接地体的电阻未达要求,则以复合地网为基础补加水平接地体");
//					System.out.println("补加"+lv+"米垂直接地体"+n+"个，再补加总长度"+ir+"米水平接地体之后");
					System.out.println("补加"+lv+"米垂直接地体"+n+"个，再补加4根"+ir/4+"米水平接地体之后");
					System.out.println("接地电阻R:"+Rr);
				} else {
					flag = 1;
					System.out.println("补加总长度"+ir+"米水平接地体及可达到要求，接地电阻R:"+Rr);
				}
				R = Rr;
				lr = ir;
			} else {
				flag = 2;
				R = Rv1;
			}
			System.out.println("flag:"+flag);
			System.out.println("lr:"+lr);
			System.out.println("lv:"+lv);
			System.out.println("地网改进后的R:"+R);
			System.out.println("////////////////////////////////////////");
		}
		return new Countresult(R, flag, lr, lv, S, m / 2, n);
	}
	
	/**
	 * @return S 外延m/2米时的建筑占地面积
	 */
	protected Double getS(Double S, Double m) {
		return S + 2 * m * Math.sqrt(S) + Math.pow(m, 2);
	}
	
	/**
	 * @param p 双层土壤下的单层土壤选择
	 * @return
	 */
	protected Double getsinglep(Double p, Double H, Double p1) {
		return H == 0d? p : (H > h? p : p1);
	}
	
	/**
	 * (防雷规范2.5)
	 * 
	 * @return map 补加接地体
	 */
	protected Map<String, Double> getl(Double p, Double r, Integer type) {
		Map<String, Double> map = new HashMap<String, Double>();
		if(type == 0) return map;
		Double lr = null;
		Double lv = null;
		double k1 = (11 * p - 3600) / 380;
		double k2 = (p - 550) / 50;
		boolean level1 = p <= 500d && r < 5d && type == 1;
		boolean level2 = p <= 800d && r < 5d && type == 2;
		boolean level3 = r < 5d && type == 3;
		boolean level4 = p > 500d && r < k1 && type == 1;
		boolean level5 = p > 800d && r < k2 && type == 2;
		if (level1 || level2 || level3 || level4 || level5) {
			lr = 5 - r;
			if (level4) {
				lr = k1 - r;
			}
			if (level5) {
				lr = k2 - r;
			}
			lv = lr / 2;
			if (lv < 2d) {
				lv = 2d;
			} else {
				lv = 2.5d;
			}
			map.put("lr", lr);
			map.put("lv", lv);
		}
		return map;
	}
	
	/**
	 * (防雷规范2.6.5)
	 * 计算冲击接地电阻
	 * @param p
	 * @param S
	 * @param a
	 * @param r
	 * @param lr
	 * @param lv
	 * @param flag
	 * @param R
	 * @return
	 */
	protected Double getRi(Double p,Double H, Double p1, Double S, Double lr, Double lv, Integer flag, Double R, Double n) {
		System.out.println("-----------------计算冲击接地电阻-------------------------");
		Double Sp = getsinglep(p, H, p1);
		Double Ri = null;
		//a:地网的等效方形边长
		Double a = Math.sqrt(S);
		//r:地网的等效圆半径
		Double r = Math.sqrt(S / pi);
		//Ls:地网总长度
		Double Ls = Totallengthofmat.totallenth(a);
		//le:接地体冲击有效长度
		Double le = 2 * Math.sqrt(Sp);
		Double lb = r;
		Double l = lb;
		if (flag != null) {
			if (flag == 1) {
				l += lr / 4;
			}
			if (flag == 2) {
				l += lv;
				if(H > h) {
					le = 2 * Math.sqrt(getpa(p, p1, H + l, h, l));
				}
			}
		}
		System.out.println("地网水平等效半径r:" + r);
		System.out.println("地网最大长度l:" + l);
		System.out.println("接地等效长度le:" + le);
		if (l > le) {
			if (le <= lb) {
				Ri = new Groundmat().gorizontalmat(Sp, Ls, 0d, 2 * pi * le, Math.pow(le, 2) * pi, h, bc, 0d);
				System.out.println("接地等效长度小于等于地网水平等效半径，冲击接地电阻Ri按水平地网计算");
			} else {
				//lrv:补加接地体的冲击有效长度
				double lrv = le - lb;
				System.out.println("接地等效半径在地网水平等效半径与地网最大长度之间，冲击接地电阻Ri按复合地网计算");
				System.out.println("lrv:"+lrv);
				if (flag == 1) {
					Ri = new Groundmat().emissivitygorizontal(Sp, a, 4 * lrv, new Groundmat().gorizontalmat(Sp, Ls,
							0d, a * 4, S, h, bc, 0d), new Groundmat().gorizontal(p, bc, lrv, h, 1) / 4);
				} else {
					if(H == 0d) {
						System.out.println("冲击接地电阻Ri按单层土壤复合地网计算");
						Ri = new Groundmat().verticalmat(p, Ls, lrv, a * 4, S, h, bc, br,Totallengthofmat.amount(a));
					} else {
						System.out.println("冲击接地电阻Ri按双层土壤复合地网计算");
							Ri = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Ls,
										a * 4, lrv,lv < 3d ? Totallengthofmat.amount(a) : n, bc, br);
					}
				}
			}
		} else {
			if(flag != null && flag == 2 && H > h) {
				Ri = new Impulseconversion().convert(getpa(p, p1, H + l, h, l), l / le, R);
			} else {
				Ri = new Impulseconversion().convert(Sp, l / le, R);
			}
			System.out.println("接地等效半径大于地网最大长度");
		}
		System.out.println("冲击接地电阻Ri:"+Ri);
		System.out.println("////////////////////////////////////////");
		return Ri;
	}
	
	protected Double getRi(Double p,Double H, Double p1, Countresult cs) {
		return getRi(p1, H, p1, cs.getS(), cs.getlr(),cs.getlv(), cs.getflag(), cs.getR(), cs.getn());
	}
	
	/**
	 * 双层土壤视在电阻率
	 * @param p
	 * @param p1
	 * @param H
	 * @param h
	 * @param l
	 * @return
	 */
	protected double getpa(Double p, Double p1, Double H, Double h, Double l) {
		// H为0时按单层土壤计算
		if(H == 0d) return p;
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
	

	public static void main(String[] args) {
//		System.out.println("----------1----------------------------------------------");
//		System.out.println(new Architecture().design(1500d, 0d, 0d, 640d, 10d, 2, true));
//		System.out.println("----------2----------------------------------------------");
//		System.out.println(new Architecture().design(1500d, 0d, 0d, 640d, 10d, 2, false));
//		System.out.println("----------1----------------------------------------------");
//		System.out.println(new Architecture().design(2500d, 0d, 0d, 4000d, 10d, 2, true));
//		System.out.println("----------2----------------------------------------------");
//		System.out.println(new Architecture().design(2500d, 0d, 0d, 4000d, 10d, 2, false));
		
//		System.out.println(new Doubledeckground().gorizontalDoubledeckMat(2500d, 2000d, 4257d, 2d, 0.5, Totallengthofmat.totallenth(63.2), 63.2 * 4, 28d,
//				4d, 0.05, 0.05));
//------------------------------------------------------------------p--,--H--,--p1--,---S---,--Rk--,-type-,-city
		System.out.println(new Building().design(3000d, 2d, 2400d, 4000d, 8d, 1, false));
	}
}
