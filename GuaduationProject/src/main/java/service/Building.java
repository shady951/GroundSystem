package service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import dto.Countresult;
import util.convert.GroundModule;
import util.convert.Impulseconversion;
import util.convert.Totallengthofmat;
import util.manual.Doubledeckground;
import util.manual.Groundmat;

@Service
public class Building {

	private final double pi = Math.PI;
	// 埋深
	private final double h = 0.7;
	// 水平接地体等效直径
	private final double bc = 0.05;
	// 垂直接地体等效直径
	private final double br = 0.05;

	public Building() {
	}

	// @Autowired
	// private Groundmat groundmat;

	/**
	 * 普通建筑接地设计
	 * @param p		(上层)土壤电阻率
	 * @param H		上层土壤深度(0为单层)
	 * @param p1		下层土壤电阻率(0为单层)
	 * @param S		 占地面积
	 * @param Rk		工频电阻要求值
	 * @param type  防雷建筑分类
	 * @param city	 土地资源是否受限
	 * @return
	 */
	public Countresult design(Double p, Double H, Double p1, Double S, Double Rk, Integer type, boolean city) {
		System.out.println("city:"+city);
		System.out.println("最初面积S:"+getS(S, 2d));
		System.out.println("最大面积S:"+2 * S);
		Double R = null;
		Double Ri = null;
		double modulecount = 0d;
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
		R = new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a),
				0d, a * 4, S, h, bc, 0d);
		System.out.println("最终面积S:" + S);
		System.out.println("外延距离:" + m/2);
		System.out.println("R最初:" + R);
		Integer flag = null;
		Double lr = null;
		Double lv = null;
		// 防雷规范2.5，是否补加接地体
		map = getl(p, r, type);
		if (!map.isEmpty()) {
			System.out.println("--------------按规范补加接地体:----------------------");
			// 补加水平接地体总长
			lr = map.get("lr");
			// 补加垂直接地体
			lv = map.get("lv");
			double Rv;
			System.out.println("lv:"+lv);
			System.out.println("n:"+Totallengthofmat.amount(a));
			if (H == 0d || H > 3d) {
				System.out.println("按单层土壤计算");
//				System.out.println(new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a), lv, a * 4, S, h, bc, Totallengthofmat.amount(a)));
				Rv = new Groundmat().verticalmat(p, Totallengthofmat.totallenth(a), lv, a * 4, S, h, bc, br, Totallengthofmat.amount(a));
			} else {
				System.out.println("按双层土壤计算");
				Rv = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Totallengthofmat.totallenth(a), a * 4, lv,
						Totallengthofmat.amount(a), bc, br);
			}
			if (!city) {
				System.out.println("未在城市,比较水平接地体与垂直接地体");
				double Rr = new Groundmat().emissivitygorizontal(p, a, lr, new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a),
						0d, a * 4, S, h, bc, 0d), new Groundmat().gorizontal(p, bc, lr / 4, h, 1) / 4);
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
			System.out.println("R:"+R);
			System.out.println("////////////////////////////////////////");
		}
		// 工频电阻是否满足要求
		Double N = null;
		if (R > Rk) {
			System.out.println("--------------地网改进:----------------");
			lv = 2.5;
			Double Rv1 = null;
			Double Rv2 = null;
			if(H == 0d) {
				System.out.println("单层土壤");
				Rv1 = new Groundmat().verticalmat(p, Totallengthofmat.totallenth(a), lv, a * 4, S, h, bc, br,
						Totallengthofmat.amount(a));
			} else {
				System.out.println("双层土壤");
				Rv1 = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Totallengthofmat.totallenth(a), a * 4, lv,
						Totallengthofmat.amount(a), bc, br);
			}
			System.out.println("补加"+lv+"米垂直接地体电阻Rv1:"+Rv1);
			if(H != 0d && H < a / 3 && p >= p1 * 1.25) {
				System.out.println("可补加长垂直接地体");
				double iv = Math.ceil(H) <= 3d? 3d : Math.ceil(H);
				N = 4d;
				Integer U = 0;
				for(;(Rv2 = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Totallengthofmat.totallenth(a), a * 4, iv,
						N = Math.ceil(a * 4 / iv) < 4d? 4d : Math.ceil(a * 4 / iv), bc, br)) > Rk && iv < a && iv < 60d; iv++) {
					if(Rv2 < Rv1 && U == 0) {
						U = iv < 2 * (Math.sqrt(p) - r) ? 1 : 2;
					}
				}
				System.out.println("U :"+ U);
				System.out.println("补加"+iv+"米长垂直接地体电阻Rv2:"+ Rv2);
				System.out.println("长垂直接地体个数N:" + N);
				if(U == 1){
					Rv1 = Rv2;
					lv = iv;
				}
			}
			if (!city) {
				double ir = 1d;
				if (lr != null) ir = Math.ceil(lr);
				Double Rr = null;
				for (; (Rr = new Groundmat().emissivitygorizontal(p, a, ir, new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a),
						0d, a * 4, S, h, bc, 0d), new Groundmat().gorizontal(p, bc, ir / 4, h, 1) / 4)) > Rk && ir < 4 * r; ir++);
				System.out.println("补加"+ir+"米水平接地体电阻Rr:"+Rr);
				if (Rr > Rk && Rr > Rv1) {
					flag = 2;
					R = Rv1;
				} else {
					flag = 1;
					R = Rr;
					lr = ir;
				}
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
		Ri = getRi(p,H, p1, S, a, r, lr, lv, flag, R, n, N);
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
			R = R * K;
			Ri = Ri * K;
			System.out.println("接地模块数量modulecount:" + modulecount);
			System.out.println("接地模块降阻率:" + K);
			System.out.println("R:" + R);
			System.out.println("Ri:" + Ri);
			System.out.println("////////////////////////////////////////");
		}
		System.out.println("最终R:"+R);
		System.out.println("最终Ri:"+Ri);

		return null;// TODO
	}

	/**
	 * @return S 外延m/2米时的建筑占地面积
	 */
	private Double getS(Double S, Double m) {
		return S + 2 * m * Math.sqrt(S) + Math.pow(m, 2);
	}

	/**
	 * (防雷规范2.5)
	 * 
	 * @return map 补加接地体
	 */
	private Map<String, Double> getl(Double p, Double r, Integer type) {
		Map<String, Double> map = new HashMap<String, Double>();
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
	 * 
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
	private Double getRi(Double p,Double H, Double p1, Double S, Double a, Double r, Double lr, Double lv, Integer flag, Double R, Double n, Double N) {
		System.out.println("-----------------计算冲击接地电阻-------------------------");
		Double Ri = null;
		Double le = 2 * Math.sqrt(p);
		// 接地网对角线
		// Double lb = a * Math.sqrt(2);
		Double lb = 2 * r;
		Double l = lb;
		if (flag != null) {
			if (flag == 1) {
				l += lr / 4;
			}
			if (flag == 2) {
				l += lv;
			}
		}
		if (l > le) {
			if (le <= lb) {
				le = le * Math.sqrt(2) / 2;
//				Ri = new Groundmat().generalmat(p, Math.pow(le, 2));
				Ri = new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a),
						0d, a * 4, Math.pow(le, 2), h, bc, 0d);
				System.out.println("等效半径小于等于地网直径");
			} else {
				double lrv = le - lb;
				System.out.println("等效半径在地网直径与地网最大长度之间");
				System.out.println("lrv:"+lrv);
				if (flag == 1) {
					Ri = new Groundmat().emissivitygorizontal(p, a, 4 * lrv, new Groundmat().gorizontalmat(p, Totallengthofmat.totallenth(a),
							0d, a * 4, S, h, bc, 0d), new Groundmat().gorizontal(p, bc, lrv, h, 1) / 4);
				} else {
					if(H == 0d) {
						System.out.println("冲击接地电阻Ri按单层土壤计算");
						Ri = new Groundmat().verticalmat(p, Totallengthofmat.totallenth(a), lrv, a * 4, S, h, bc, br,
								Totallengthofmat.amount(a));
					} else {
						System.out.println("冲击接地电阻Ri按双层土壤计算");
							Ri = new Doubledeckground().gorizontalDoubledeckMat(p, p1, S, H, h, Totallengthofmat.totallenth(a),
										a * 4, lrv,n = lv < 3d ? n : N, bc, br);
//						Ri = new Groundmat().verticalmat(p, Totallengthofmat.totallenth(a), lrv, a * 4, S, h, bc, br,
//								Totallengthofmat.amount(a));
					}
				}
			}
		} else {
			double Lk = l / le;
			Ri = new Impulseconversion().convert(p, Lk, R);
			System.out.println("等效半径大于地网最大长度");
			System.out.println("l:" + l);
			System.out.println("le:" + le);
		}
		System.out.println("最初Ri:"+Ri);
		System.out.println("////////////////////////////////////////");
		return Ri;
	}

	// private Double getRi(Double p, Double a,Double r, Double R) {
	// Double Ri = null;
	// Double le = 2 * Math.sqrt(p);
	// //接地网对角线
	// // Double lb = a * Math.sqrt(2);
	// Double lb = a * r;
	// Double l = lb;
	// if(l > le) {
	// le = le * Math.sqrt(2) / 2;
	// Ri = new Groundmat().generalmat(p, Math.pow(le, 2));
	// } else {
	// double Lk = l / le;
	// Ri = new Impulseconversion().convert(p, Lk, R);
	// }
	// return Ri;
	// }

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
		
		System.out.println("----------1----------------------------------------------");
		System.out.println(new Building().design(2500d, 2d, 1700d, 4000d, 10d, 3, true));
		System.out.println("----------2----------------------------------------------");
		System.out.println(new Building().design(2500d, 2d, 1500d, 4000d, 10d, 2, false));
	}
}
