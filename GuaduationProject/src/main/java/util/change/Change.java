package util.change;

import util.convert.Totallengthofmat;
import dto.Countresult;
import dto.Data;
import dto.Result;

public class Change {

//	private Countresult cs;
	
//	public Change(Countresult cs) {
//		this.cs = cs;
//	}
	
	public static Result getResult(Countresult cs, Data dt) {
		switch (cs.getstyle()) {
		case 1: return building(cs, dt);
		case 2: return powerstation(cs, dt);
		case 3: return towers(cs, dt);
		}
		return null;
	}

	private static Result building(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		sb.append("根据《防雷规范与标准》§2.5，采用**热镀锌扁钢，围绕建筑外" + 
				cs.getm() +
				"米铺设环形接地体。与建筑地基内钢筋进行可靠焊接，形成网格面积为" +
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) + 
				"X" + 
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) +
				"米的水平地网");
		if(cs.getflag() != null) {
			switch(cs.getflag()) {
			case 1:
				sb.append("，再沿地网四角向外铺设" +
						cs.getlr() +
						"米外延接地体");
				break;
			case 3:
				sb.append("，沿地网四角向外铺设" + 
						cs.getlr() +
						"米外延接地体");
	//			break;
			case 2:
				if(cs.getlv() < 3d) {
					sb.append("，再在地网每个网孔交叉点补加" +
							cs.getlv() +
							"米等效直径大于40mm的热镀锌钢管或角钢，总共" +
							cs.getn() +
							"根。");
				} else {
					sb.append("，再沿地网边线等距补加" +
							cs.getn() +
							"根长度为" +
							cs.getlv() +
							"米，等效直径大于40mm的长热镀锌钢管。");
				}
				break;
			case 4:
				sb.append("。因符合规范条件，需按规范沿地网四角向外铺设" +
						cs.getlr() +
						"米外延接地体");
				break;
			case 5:
				sb.append("。因符合规范条件，需按规范在地网每个网孔交叉点补加" +
						cs.getlr() +
						"米等效直径大于40mm的热镀锌钢管或角钢，总共" +
						cs.getn() +
						"根。");
				break;
			}
		}
		if(cs.getmodulecount() != 0d) {
			sb.append("最后沿建筑地网外缘等距补加" +
					cs.getmodulecount() +
					"个规格为*的接地模块"); 
		}
		return null;// TODO
	}

	private static Result powerstation(Countresult cs, Data dt) {
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
		sb.append("采用**热镀锌扁钢，围绕建筑外" + 
				cs.getm() +
				"米铺设环形接地体。与建筑地基内钢筋进行可靠焊接，形成网格面积为" +
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) + 
				"X" + 
				Math.ceil(Totallengthofmat.singlelength(Math.sqrt(cs.getS()))) +
				"米的水平地网");
		if(cs.getflag() != null) {
			switch(cs.getflag()) {
			case 1:
				sb.append("，再沿地网四角向外铺设4根" +
						cs.getlr() +
						"米的*外延接地体");
				break;
			case 3:
				sb.append("，沿地网四角向外铺设4根" + 
						cs.getlr() +
						"米的*外延接地体");
	//			break;
			case 2:
				if(cs.getlv() < 3d) {
					sb.append("，再在地网每个网孔交叉点补加" +
							cs.getlv() +
							"米等效直径大于40mm的热镀锌钢管或角钢，总共" +
							cs.getn() +
							"根");
				} else {
					sb.append("，再沿地网边线等距补加" +
							cs.getn() +
							"根长度为" +
							cs.getlv() +
							"米，等效直径大于50mm的长热镀锌钢管");
				}
				break;
			case 4:
				sb.append("，因符合规范条件，需按规范沿地网四角向外铺设" +
						cs.getlr() +
						"米外延接地体");
				break;
			case 5:
				sb.append("，因符合规范条件，需按规范在地网每个网孔交叉点补加" +
						cs.getlr() +
						"米等效直径大于40mm的热镀锌钢管或角钢，总共" +
						cs.getn() +
						"根");
				break;
			}
		}
		if(cs.getmodulecount() != 0d) {
			sb.append("。最后沿建筑地网外缘等距补加" +
					cs.getmodulecount() +
					"个规格为*的接地模块"); 
		}
		if(cs.getindependent() != 0) {
				sb.append("。以该规范§2.6为基础，计算独立接地装置冲击接地电阻，接地形式为" +
						(cs.getindependent() == 1? "直线" : "环形") +
						"等距铺设" +
						cs.getl() +
						"米等效直径大于40mm的热镀锌钢管" +
						cs.getni() +
						"根，每根间距" +
						cs.gets() +
						"米，每根顶部以*扁钢焊接");
				if(cs.getmodulecounti() != 0) {
					sb.append("，辅以" +
							cs.getmodulecounti() +
							"个400mmX600mmX60mm的方形接地模块，单价70");
				}
		}
		return null; //TODO
	}
	private static Result towers(Countresult cs, Data dt) {
		StringBuilder sb = new StringBuilder();
		sb.append("根据L/T621-1997《交流电气装置的接地》§6.3，该");
		switch(cs.getkind()) {
		case 1: 
			sb.append(dt.gettype() == 1 ? "铁塔" : "混凝土电杆" +
					"的自然接地体已满足要求，无需增设人工接地装置");
			break;
		case 2:
			sb.append("铁塔需铺设人工接地装置，沿铁塔四角外延4根" +
					cs.getlr() +
					"米长的*热镀锌圆钢");
			break;
		case 3: 
			sb.append("混凝土电杆需铺设人工接地装置，先以电杆为中点铺设一根" +
					cs.getlr() +
					"米水平*接地体，再在该接地体两端向外各延伸出两根" +
					cs.getlr() + 
					"米水平*接地体");
			break;
		case 4:
			sb.append("混凝土杆因地形受限，优先以占地面积小来设计，围绕电杆用*扁钢铺设半径为" +
					cs.getr() +
					"米的环形接地装置" +
					(cs.getindependent() == 2d? "并增设" +
					cs.getni() + 
					"根长" +
					cs.getl() +
					"的*圆钢垂直接地体，等距焊接至环形接地装置外缘。": "。"));
		}
		return null; // TODO
	}



}
