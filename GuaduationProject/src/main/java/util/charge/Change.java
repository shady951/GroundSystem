package util.charge;

import util.convert.Totallengthofmat;
import dto.Countresult;
import dto.Data;
import dto.Result;

public class Change {
	
	private static final double pi = Math.PI;
	//100*5热镀锌扁钢价格(元/米)
	private static final double bcmoney = 7.86;
	//Ф40热镀锌圆钢价格(元/米)
	private static final double brmoney = 19.72;
	//400mmX600mmX60mm方形接地模块(元/个)
	private static final double mdmoney = 70d;
	
	public static Result getResult(Countresult cs, Data dt) {
		String plan = null;
		double money;
		switch (cs.getstyle()) {
		case 1: 
			plan = building(cs, dt);
			break;
		case 2: 
			plan = powerstation(cs, dt);
			break;
		case 3: 
			plan =  towers(cs, dt);
			break;
		}
		money = getmoney(cs, dt);
		return new Result(plan, cs.getR(), cs.getRi(), money);
	}

	/**
	 * 水平地网，集中垂直接地体的连接，以扁钢计算，其余以圆钢计算
	 * @return 耗材预算(元)
	 */
	private static double getmoney(Countresult cs, Data dt) {
		double Lbc = 0d;
		double Lbr = 0d;
		//计算扁钢耗材
		if(dt.getstyle() != 3) {
			Lbc = Totallengthofmat.totallenth(cs.getS()) - (Totallengthofmat.totallenth(dt.getS()) - Math.sqrt(dt.getS()) * 4);
		} else if(cs.getkind() == 4){
			Lbc = cs.getr() * 2 * (pi + 2);
		}
		if(cs.getindependent() != 0) Lbc += cs.gets() * (cs.getindependent() == 1 ?cs.getni() - 1 : cs.getni());
		//计算圆钢耗材
		switch(cs.getflag()) {
		case 4 :
		case 1 :
			Lbr = cs.getlr() * 4;
			break;
		case 5 :
		case 2 :
			Lbr = cs.getlv() * cs.getn();
			break;
		case 3 :
			Lbr = cs.getlr() * 4 + cs.getlv() * cs.getn();
			break;
		}
		if(cs.getindependent() != 0) Lbr += cs.getl() * cs.getni();
		return Lbc * bcmoney + Lbr * brmoney + (cs.getmodulecount() + cs.getmodulecounti()) * mdmoney;
	}

	private static String building(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		sb.append("根据《防雷规范与标准》§2.5，采用100*5热镀锌扁钢，围绕建筑外" + 
				cs.getm() +
				"米铺设环形接地体，埋深0.8米。与建筑地基内钢筋进行可靠焊接，形成网格面积为" +
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) + 
				"X" + 
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) +
				"米的水平地网，");
			switch(cs.getflag()) {
			case 1:
				sb.append("再沿地网四角向外铺设4根" +
						cs.getlr() +
						"米Ф40热镀锌圆钢作为水平外延接地体，");
				break;
			case 3:
				sb.append("沿地网四角向外铺设4根" + 
						cs.getlr() +
						"米Ф40热镀锌圆钢作为水平外延接地体，");
	//			break;
			case 2:
				if(cs.getlv() < 3d) {
					sb.append("再在地网每个网孔交叉点补加" +
							cs.getlv() +
							"米Ф40热镀锌圆钢垂直接地体，总共" +
							cs.getn() +
							"根，形成复合地网，");
				} else {
					sb.append("，由于土壤下层电阻率较低，沿地网边线等距补加" +
							cs.getn() +
							"根长度为" +
							cs.getlv() +
							"米的Ф40热镀锌圆钢长垂直接地体，形成复合地网，");
				}
				break;
			case 4:
				sb.append("因符合规范条件，需按规范沿地网四角向外铺设4根" +
						cs.getlr() +
						"米Ф40热镀锌圆钢作为水平外延接地体，");
				break;
			case 5:
				sb.append("因符合规范条件，需按规范在地网每个网孔交叉点补加" +
						cs.getlr() +
						"米的Ф40热镀锌圆钢垂直接地体，总共" +
						cs.getn() +
						"根，形成复合地网，");
				break;
			}
		if(cs.getmodulecount() != 0d) {
			sb.append("最后沿地网外缘均匀设置" +
					cs.getmodulecount() +
					"个规格为400mmX600mmX60mm的方形接地模块，"); 
		}
		sb.append("即可满足要求。");
		return sb.toString();
	}

	private static String powerstation(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		if(cs.getindependent() != 0) {
			if(dt.gettype() == 1) {
				sb.append("根据《防雷规范与标准》§2.5，一类防雷建筑物应设置独立接地装置，" +
						"其冲击接地电阻不宜大于10欧姆。");
			} else {
				sb.append("根据DL/T620-1997《交流电气装置的过电压保护和绝缘配合》§7.1，该变电站应设置独立接地装置，" +
						"其冲击接地电阻不宜大于10欧姆。根据《防雷规范与标准》§2.5，");
			}
		} else {
			sb.append("根据《防雷规范与标准》§2.4，该变电站按二类防雷建筑物设计。根据该规范§2.5，" );
		}
		sb.append("采用100*5热镀锌扁钢，围绕建筑外" + 
				cs.getm() +
				"米铺设环形接地体，埋深0.8米。与建筑地基内钢筋进行可靠焊接，形成网格面积为" +
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) + 
				"X" + 
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) +
				"米的水平地网，");
			switch(cs.getflag()) {
			case 1:
				sb.append("再沿地网四角向外铺设4根" +
						cs.getlr() +
						"米的Ф40热镀锌圆钢作为水平外延接地体，");
				break;
			case 3:
				sb.append("沿地网四角向外铺设4根" + 
						cs.getlr() +
						"米的Ф40热镀锌圆钢作为水平外延接地体，");
//				break;
			case 2:
				if(cs.getlv() < 3d) {
					sb.append("再在地网每个网孔交叉点补加" +
							cs.getlv() +
							"米的Ф40热镀锌圆钢垂直接地体，总共" +
							cs.getn() +
							"根，形成复合地网，");
				} else {
					sb.append("由于土壤下层电阻率较低，沿地网边线等距补加" +
							cs.getn() +
							"根长度为" +
							cs.getlv() +
							"米的Ф40热镀锌圆钢长垂直接地体，形成复合地网，");
				}
				break;
			case 4:
				sb.append("因符合规范条件，需按规范沿地网四角向外铺设4根" +
						cs.getlr() +
						"米的Ф40热镀锌圆钢作为水平外延接地体，");
				break;
			case 5:
				sb.append("因符合规范条件，需按规范在地网每个网孔交叉点补加" +
						cs.getlr() +
						"米的Ф40热镀锌圆钢垂直接地体，总共" +
						cs.getn() +
						"根，形成复合地网，");
				break;
			}
		if(cs.getmodulecount() != 0d) {
			sb.append("最后沿地网外缘均匀设置" +
					cs.getmodulecount() +
					"个规格为400mmX600mmX60mm的方形接地模块，"); 
		}
		if(cs.getindependent() != 0) {
				sb.append("以该规范§2.6为基础，计算独立接地装置冲击接地电阻，接地形式为" +
						(cs.getindependent() == 1? "直线" : "环形") +
						"等距铺设" +
						cs.getl() +
						"米的Ф40热镀锌圆钢" +
						cs.getni() +
						"根，每根间距" +
						cs.gets() +
						"米，以100*5热镀锌扁钢将所有垂直接地体焊接，埋深0.8米，");
				if(cs.getmodulecounti() != 0) {
					sb.append("再等距补加" +
							cs.getmodulecounti() +
							"个规格为400mmX600mmX60mm的方形接地模块，");
				}
		}
		sb.append("即可满足要求。");
		return sb.toString();
	}
	private static String towers(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		sb.append("根据L/T621-1997《交流电气装置的接地》§6.3，该");
		switch(cs.getkind()) {
		case 1: 
			sb.append(dt.gettype() == 1 ? "铁塔" : "混凝土电杆" +
					"的自然接地体已满足要求，无需增设人工接地装置，");
			break;
		case 2:
			sb.append("铁塔需铺设人工接地装置，沿铁塔四角向外铺设4根" +
					cs.getlr() +
					"米长的Ф40热镀锌圆钢作为水平外延接地体，埋深0.8米，");
			break;
		case 3: 
			sb.append("混凝土电杆需铺设人工接地装置，先以电杆为中点铺设一根" +
					cs.getlr() +
					"米的Ф40热镀锌圆钢水平接地体，埋深0.8米，再以该接地体两端向外各延伸出两根" +
					cs.getlr() + 
					"米的Ф40热镀锌圆钢作为水平水平外延接地体，");
			break;
		case 4:
			sb.append("混凝土杆因地形受限，优先以占地面积小来设计，围绕电杆用100*5热镀锌扁钢铺设半径为" +
					cs.getr() +
					"米的环形接地装置，埋深0.8米，" +
					(cs.getindependent() == 2d? "并沿环形外缘等距铺设" +
					cs.getni() + 
					"根长" +
					cs.getl() +
					"的Ф40热镀锌圆钢垂直接地体，": "，"));
		}
		sb.append("即可满足要求。");
		return sb.toString();
	}
	public static void main(String[] args) {
	
	}
}
