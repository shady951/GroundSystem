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
	 * @param type 1：铁塔型 2：钢筋混凝土杆型
	 * @param city
	 * @return
	 */
	public Countresult design(Double p, Double H, Double p1, Double S, Integer type, boolean city) {
		Countresult cs = new Countresult(); //TODO
		Double R = null;
		Double Sp = getsinglep(p, H, p1);
		double Rk = Tower.getR(Sp);
		//(L/T621-1997 6.3)
		if (p < 300d) {
			R = type == 1? new Groundmat().generalmat(Sp, S) :  0.1 * Sp;
			if (R < Rk) return cs; // TODO
		}
		if (type == 1d) {
			cs = getR(p, H, p1, S, Rk, 0, city);
		} else {
			if(city) {
				//TODO
			} else {
				double let = 2 * Math.sqrt(getsinglep(p, H, p1));
				double lt = Tower.Getlt(Sp);
				double l2 = 1;
				for(; (R = new Tower().gorizontal(Sp, l2, l2, h, bc, 2)) > Rk && l2 < let && l2 < lt; l2++);
				cs = new Countresult();
				if(R < Rk) return cs; //TODO
				//R不满足要求，补加直线型垂直接地体，其最大排列距离为三倍l2
				double R1 = straightscheme(p, p1, H, Math.pow(3 * l2, 2) * pi, cs, false);
				R = R * R1 / (R + R1);
			}
			//TODO
		}

		return null; // TODO
	}
	
	public static void main(String[] args) {
		double p = 1000d;
		double let = 2 * Math.sqrt(p);
		System.out.println(let);
		System.out.println(new Tower().gorizontal(p, 2d, 60d, 0.8,0.05, 2));
	}
}
