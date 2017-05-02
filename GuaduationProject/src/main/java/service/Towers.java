package service;

import util.convert.Totallengthofmat;
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
		Countresult cs;
		Double R = null;
		Double Sp = getsinglep(p, H, p1);
		double Rk = new Tower().getR(Sp);
		if (p < 300d) {
			R = type == 1? new Groundmat().generalmat(Sp, S) :  0.1 * Sp;
			if (R < Rk) return new Countresult(); // TODO
		}
		if (type == 1d) {
			cs = getR(p, H, p1, S, Rk, 0, city);
		} else {
			if(city) {
				
			} else {
				
			}
		}

		return null; // TODO
	}
}
