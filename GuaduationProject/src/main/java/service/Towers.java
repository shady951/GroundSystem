package service;

import util.manual.Groundmat;
import util.manual.Tower;
import dto.Countresult;

public class Towers extends PowerStation {

	public Towers() {
	}

	/**
	 * 
	 * @param p
	 * @param H
	 * @param p1
	 * @param S
	 * @param Rk
	 * @param type 1：铁塔型 2：钢筋混凝土杆型
	 * @param city
	 * @return
	 */
	@Override
	public Countresult design(Double p, Double H, Double p1, Double S,Double Rk, Integer type, boolean city) {
		Countresult cs = new Countresult(); //TODO
		Double R = null;
		Double Sp = getsinglep(p, H, p1);
		Rk = Tower.getR(Sp);
		System.out.println("视在土壤电阻率"+Sp+"欧姆下的最大工频电阻允许值Rk:" + Rk);
		double lt = Tower.Getlt(Sp);
		System.out.println("规范要求单根放射形接地体最大长度lt:" + lt);
		//(L/T621-1997 6.3)
		//当Sp小于30时，需考虑自然接地体，若自然接地体满足要求，可不另设人工接地体
		if (p < 300d) {
			System.out.println("土壤视在电阻率小于300，按规范考虑自然接地体");
			R = type == 1? new Groundmat().generalmat(Sp, S) :  0.1 * Sp;
			System.out.println("自然接地体工频接地电阻R:"+R);
			if (R <= Rk) return cs; // TODO
			System.out.println("自然接地体不符合要求，增设人工接地体");
		}
		if (type == 1d) {
			double l0 = 1;
				for(; (R = new Tower().gorizontal(Sp, l0 , Math.sqrt(S), h, bc, 1)) > Rk && l0 < lt; l0++);
				System.out.println("增设4根水平放射接地体"+l0+"米");
				System.out.println("工频接地电阻R:"+R);
			/*
			 * 
			if(R != null && R < Rk) return cs;//TODO
			System.out.println("水平接地体不满足要求,按普通建筑工频接地方案设计地网");
			cs = getR(p, H, p1, S, Rk, 0, city);
			System.out.println(cs.getS());
			System.out.println("接地形式，1:水平接地体2:垂直接地体3:水平加垂直接地体"+cs.getflag());
			System.out.println("4根水平接地体，每根长度lr:"+cs.getlr());
			System.out.println("垂直接地体长度lv:"+cs.getlv());
			System.out.println("垂直接地体个数:"+cs.getn());
			System.out.println("工频接地电阻R:"+cs.getR());
			 */
		} else {
			if(city) {
				//采用环形接地装置
				double l1 = 1;
				for(; (R = new Tower().gorizontal(Sp, 2 * l1 * pi, h, bc)) > Rk && l1 < 4; l1++);
				if(R <= Rk) {
					l1 -= 1;
					System.out.println("铺设半径为"+l1+"米的环形接地装置");
					System.out.println("工频接地电阻R:"+R);
					return cs;
				}
				double R1 = linkscheme(p, p1, H, Math.pow(l1, 2) * pi, cs, false);
				R = new Tower().gorizontal(Sp, 2 * l1 * pi, h, bc);
				R = R * R1 / (R + R1);
				for(; R > Rk ; l1++) {
					R1 = linkscheme(p, p1, H, Math.pow(l1, 2) * pi, cs, false);
					R = new Tower().gorizontal(Sp, 2 * l1 * pi, h, bc);
					R = R * R1 / (R + R1);
				}
				System.out.println("视在土壤电阻率"+Sp+"欧姆下的最大工频电阻允许值Rk:" + Rk);
				System.out.println("铺设半径为"+l1+"米的环形接地装置和环形垂直接地体");
				System.out.println("工频接地电阻R:"+R);
				if(R < Rk) return cs; //TODO
				//R不满足要求，补加环形垂直接地体
				System.out.println("R不满足要求，补加环形垂直接地体");
				System.out.println("垂直接地体长度l:"+cs.getl());
				System.out.println("垂直接地体个数ni:"+cs.getni());
				System.out.println("环形垂直接地体接地电阻R1:"+cs.getR());
				System.out.println("工频接地电阻R:"+R);
			} else {
				//采用放射形接地装置
				double let = 2 * Math.sqrt(getsinglep(p, H, p1));
				double l2 = 1;
				for(; (R = new Tower().gorizontal(Sp, l2, l2, h, bc, 2)) > Rk && l2 < let && l2 < lt; l2++);
				System.out.println("铺设4根长度为"+l2+"米的放射形接地装置");
				System.out.println("工频接地电阻R:"+R);
				if(R < Rk) return cs; //TODO
				/*
				 * 实测在4500欧姆电阻率下，不会用到以下方案 
				//R不满足要求，补加直线型垂直接地体，其最大排列距离为三倍l2
				double R2 = straightscheme(p, p1, H, Math.pow(3 * l2, 2) * pi, cs, false);
				R = R * R2 / (R + R2);
				System.out.println("R不满足要求，补加直线形垂直接地体");
				System.out.println("垂直接地体长度l:"+cs.getl());
				System.out.println("垂直接地体个数ni:"+cs.getni());
				System.out.println("直线形垂直接地体接地电阻R2:"+cs.getR());
				System.out.println("工频接地电阻R:"+R);
				 */
			}
			//TODO 接地模块
		}

		return null; // TODO
	}
	
	public static void main(String[] args) {
//		double p = 1000d;
//		double let = 2 * Math.sqrt(p);
//		System.out.println(let);
//		System.out.println(new Tower().gorizontal(p, 2d, 60d, 0.8,0.05, 2));
		System.out.println(new Towers().design(2500d, 0d, 100d,80d, 0d, 2, true));
		System.out.println(new Towers().design(2500d, 0d, 100d,80d, 0d, 1, false));
	}
}
