package service.impl;

import org.springframework.stereotype.Service;

import service.Ground;
import util.convert.GroundModule;
import util.manual.Tower;
import dto.Countresult;

/**
 * 杆塔接地设计
 * @author tc
 *
 */
@Service
public class Towers extends PowerStation implements Ground {

	public Towers() {
	}

	/**
	 * 杆塔接地设计
	 * @param p		(上层)土壤电阻率
	 * @param H		上层土壤深度(0为单层)
	 * @param p1		下层土壤电阻率(0为单层)
	 * @param S		占地面积
	 * @param Rk		工频电阻要求值
	 * @param type 杆塔类型(1:铁塔型 2:钢筋混凝土杆型)
	 * @param city	土地资源是否受限		
	 * @return
	 */
	@Override
	public Countresult design(Double p, Double H, Double p1, Double S,Double Rk, Integer type, boolean city) {
		Countresult cs = null;
		Double R = null;
		Double Ri = null;
		Double Sp = getsinglep(p, H, p1);
		Rk = Tower.getR(Sp);
		System.out.println("视在土壤电阻率"+Sp+"欧姆下的最大工频电阻允许值Rk:" + Rk);
		double lt = Tower.Getlt(Sp);
		System.out.println("规范要求单根放射形接地体最大长度lt:" + lt);
		//(L/T621-1997 6.3)
		//当Sp小于30时，需考虑自然接地体，若自然接地体满足要求，可不另设人工接地体
		if (p < 300d) {
			double a = 0.4;	//混凝土电杆自然接地体的冲击系数
			System.out.println("土壤视在电阻率小于300，按规范考虑自然接地体");
			R = (type == 1? 0.44 * p / Math.sqrt(S) :  0.1 * Sp);
			System.out.println("自然接地体工频接地电阻R:"+R);
			if (R <= Rk) {
				if(type == 1) {
					Ri = getRi(p, H, p1, S, 0d, 0d, 0, R, 0d);
				} else {
					Ri = R * a;
				}
				System.out.println("自然接地体冲击接地电阻R:"+Ri);
				return new Countresult(3, R, Ri, 1);
			}
			System.out.println("自然接地体不符合要求，增设人工接地体");
		}
		if (type == 1d) {
			double l0 = 1; //水平接地体长度
			for(; (R = Tower.gorizontal(Sp, l0 , Math.sqrt(S), h, bc, 1)) > Rk && l0 <= lt; l0++);
			System.out.println("增设4根水平放射接地体"+l0+"米");
			System.out.println("工频接地电阻R:"+R);
			//长度修正
			l0 = l0 == 1d? 1d : l0 - 1d;
			Ri = getRi(p, H, p1, S, l0 * 4, 0d, 1, R, 0d);
			cs = new Countresult(3, R, Ri, S, 1, l0, 2);
		} else {
			if(city) { //采用环形接地装置或接地网
				cs = new Countresult();
				cs.setkind(4);
				double l1 = 1d;
				new Tower();
				for(; (R = Tower.gorizontal(Sp, 2 * l1 * pi, h, bc)) > Rk && l1 <= 5d; l1++);
				l1 = l1 == 1d? 1d : l1 - 1d;
				if(R <= Rk) Ri = getRi(p, H, p1, Math.pow(l1, 2) * pi, 0d, 0d, 0, R, 0d);
				cs.setr(l1);
				System.out.println("铺设半径为"+l1+"米的环形接地装置");
				System.out.println("工频接地电阻R:"+R);
				cs.setR(R);
				//R不满足要求，设计复合地网
				for(l1 = 3d; cs.getR() > Rk ; l1++) cs = getR(p, H, p1, Math.pow(l1, 2), Rk, 0, true);
				if(Ri == null) {
					cs.setkind(5);
					Ri = getRi(p, H, p1, cs);
				}
				System.out.println("冲击接地电阻Ri:"+Ri);
				cs.setstyle(3);
				cs.setRi(Ri);
				System.out.println("towers:cs"+cs);
			} else { //采用放射形接地装置
				double le = 2 * Math.sqrt(Sp);
				double l2 = 1;
				new Tower();
				for(; (R = Tower.gorizontal(Sp, l2, l2, h, bc, 2)) > Rk && l2 < le && l2 < lt; l2++);
				if(R < Rk) {
					//长度修正
					l2 = l2 == 1d? 1d : l2 - 1d;
					Ri = getRi(p, H, p1, 0d, l2 * 4, 0d, 1, R, 0d);
					System.out.println("铺设4根长度为"+l2+"米的放射形接地装置");
					System.out.println("工频接地电阻R:"+R);
					cs = new Countresult(3, R, Ri, 1, l2, 3);
				}
			}
		}
		//装设接地模块
		if(cs.getRi() > 10d) {
			double mRi = cs.getRi();
			double modulecount = GroundModule.getcount(p, mRi, 10d);
			double K = GroundModule.getR(p, mRi, modulecount) / mRi;
			cs.setmodulecount(modulecount);
			cs.setR(cs.getR() * K);
			cs.setRi(cs.getRi() * K);
		}
		return cs; 
	}
	
}
