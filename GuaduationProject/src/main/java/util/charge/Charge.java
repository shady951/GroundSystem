package util.charge;

import util.convert.Totallengthofmat;
import dto.Countresult;
import dto.Data;
import dto.Result;

/**
 * 价格计算、字符串拼接模块
 * @author tc
 *
 */
public class Charge {
	
	private static final double pi = Math.PI;
	//100*5热镀锌扁钢价格(元/米)
	private static final double bcmoney = 7.86;
	//Ф40热镀锌圆钢价格(元/米)
	private static final double brmoney = 19.72;
	//400mmX600mmX60mm方形接地模块(元/个)
	private static final double mdmoney = 70d;
	
	public static Result getResult(Countresult cs, Data dt) {
		String plan = null;
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
		return new Result(plan, cs.getR(), cs.getRi(), getmoney(cs, dt));
	}

	/**
	 * 水平地网以扁钢计算，其余以圆钢计算
	 * @return 耗材预算(元)
	 */
	private static String getmoney(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		double Lbc = 0d;
		double Lbr = 0d;
		double count = 0d;
		double money = 0d;
		//计算扁钢耗材
		if(dt.getstyle() != 3) {
			Lbc = Totallengthofmat.totallenth(Math.sqrt(cs.getS())) - (Totallengthofmat.totallenth(Math.sqrt(dt.getS())) - Math.sqrt(dt.getS()) * 4);
		} else if(cs.getkind() == 4){
			Lbc = cs.getr() * 2 * (pi + 2);
		}
		if(Lbc != 0d){
			sb.append("需要扁钢"+(int)Lbc+"米，");
		}
		System.out.println("扁钢耗材："+Lbc);
		System.out.println("扁钢耗费："+Lbc * bcmoney);
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
		if(Lbr != 0d) sb.append("圆钢"+(int)Lbr+"米，");
		System.out.println("圆钢耗材："+Lbr);
		System.out.println("圆钢耗费："+Lbr * brmoney);
		//计算接地模块消耗
		count = cs.getmodulecount() + cs.getmodulecounti();
		if(count != 0d) sb.append("接地模块"+count+"个，");
		//计算总价
		money = (int)Lbc * bcmoney + (int)Lbr * brmoney + count * mdmoney;
		if(money == 0d) {
			sb.append(money+"元");
		} else {
			sb.append("总计："+money+"元");
		}
		return sb.toString();
	}

	private static String building(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		sb.append("根据《建筑物防雷设计规范》，采用100*5热镀锌扁钢，围绕建筑外" + 
				cs.getm() +
				"米铺设环形接地体，埋深0.8米。与建筑地基内钢筋进行可靠焊接，形成网格面积为" +
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) + 
				"X" + 
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) +
				"米，地网面积为"
				+ cs.getS()
				+ "平方米的水平地网，");
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
						cs.getlv() +
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
				sb.append("根据《建筑物防雷设计规范》，一类防雷建筑物应设置独立的集中接地装置，" +
						"其冲击接地电阻不宜大于10欧姆。");
			} else {
				sb.append("根据DL/T620-1997《交流电气装置的过电压保护和绝缘配合》，该变电站应装设独立的集中接地装置，" +
						"其冲击接地电阻不宜大于10欧姆。根据《建筑物防雷设计规范》§2.5，");
			}
		} else {
			sb.append("根据《建筑物防雷设计规范》，" );
		}
		sb.append("采用100*5热镀锌扁钢，围绕建筑外" + 
				cs.getm() +
				"米铺设环形接地体，埋深0.8米。与建筑地基内钢筋进行可靠焊接，形成网格面积为" +
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) + 
				"X" + 
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) +
				"米，地网面积为"
				+ cs.getS()
				+ "平方米的水平地网，");
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
						cs.getlv() +
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
				sb.append("以该规范为基础，计算集中接地装置的冲击接地电阻，接地形式为" +
						(cs.getindependent() == 1? "直线" : "环形") +
						"等距铺设" +
						cs.getl() +
						"米的Ф40热镀锌圆钢" +
						cs.getni() +
						"根，每根间距" +
						cs.gets() +
						"米，埋深0.8米，");
				if(cs.getmodulecounti() != 0) {
					sb.append("再等距补加" +
							cs.getmodulecounti() +
							"个规格为400mmX600mmX60mm的方形接地模块，");
				}
				sb.append("最后使集中接地装置与主接地网的地中距离大于3米，");
		}
		sb.append("即可满足要求。");
		return sb.toString();
	}
	private static String towers(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		sb.append("根据L/T621-1997《交流电气装置的接地》，该");
		switch(cs.getkind()) {
		case 1: 
			sb.append(dt.gettype() == 1 ? "铁塔" : "混凝土电杆");
			sb.append("无需增设人工接地装置，其自身的自然接地体");
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
			sb.append( "混凝土杆采用100*5热镀锌扁钢围绕铺设半径为" +
					cs.getr() +
					"米的环形接地装置，埋深0.8米，"); 
			break;
		case 5:
			sb.append("混凝土杆因地形受限，优先以占地面积小来设计，以电杆为中心，采用100*5热镀锌扁钢铺设面积为"
					+ cs.getS()
					+ "平方米的"
					+ (cs.getflag() == 0 ? "水平" : "复合")
					+ "地网，网格面积为"
					+ Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS())))  
					+ "X"
					+ Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS())))
					+ "米，");
			if(cs.getflag() != 0) {
				sb.append("垂直接地体为"
						+ cs.getlv()
						+ "米的Ф40热镀锌圆钢共"
						+ cs.getn()
						+ "根，");
			}
			break;
		}
		sb.append("即可满足要求。");
		return sb.toString();
	}
	
//	public static void main(String[] args) {
//	}
	
}
