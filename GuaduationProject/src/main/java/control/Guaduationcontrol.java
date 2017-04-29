package control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dto.Countresult;
import util.convert.Equivalentdiameter;
import util.manual.Doubledeckground;
import util.manual.Gorizontal;
import util.manual.Groundmat;
import util.manual.Tower;
import util.manual.Vertical;

@Controller
@RequestMapping
public class Guaduationcontrol {
	
	@Autowired
	private Groundmat groundmat;
	@Autowired
	private Doubledeckground doubledeckground;
	@Autowired
	private Tower tower;
	@Autowired
	private Vertical vertical;
	@Autowired
	private Gorizontal gorizontal;
//	@Autowired
//	private Equivalentdiameter equivalentdiameter;
	
	/**
	 * 垂直接地极模块
	 * @param p
	 * @param b0
	 * @param b1
	 * @param l
	 * @param h
	 * @param s
	 * @param n
	 * @param index
	 * @param type		1为单根垂直，2为多根直线排列，3为多根环形排列，4为多根任意排列
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/vertical", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
	public Countresult vertical(@RequestParam() String p, @RequestParam() String b0, @RequestParam(required = false) String b1,
			@RequestParam() String l, @RequestParam() String h,  @RequestParam(required = false) String s, @RequestParam(required = false) String n, 
			@RequestParam() String index, @RequestParam() String type) {
		Double R = null;
		Double d = getd(index, b0, b1);
		switch(type) {
		case "1":	R = vertical.vertical(Double.valueOf(p), d, Double.valueOf(l), Double.valueOf(h));
			break;
		case "2":	R = vertical.straightverticals(Double.valueOf(p), d, Double.valueOf(l), Double.valueOf(h), Double.valueOf(s), Double.valueOf(n));
			break;
//		case "3":	R = vertical.linkverticals(Double.valueOf(p), d, Double.valueOf(l), Double.valueOf(h), Double.valueOf(s), Double.valueOf(n));
//			break;
		}
		return new Countresult(R);
	}
	
	/**
	 * 水平接地极模块
	 * @param p
	 * @param b0
	 * @param b1
	 * @param l
	 * @param h
	 * @param index
	 * @param indexG	水平接地体形状系数的序号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gorizontal", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
	public Countresult gorizontal(@RequestParam() String p, @RequestParam() String b0, @RequestParam(required = false) String b1,
			@RequestParam() String l, @RequestParam() String h, @RequestParam() String index, @RequestParam() String indexG) {
		Double d = getd(index, b0, b1);
		Double R = gorizontal.gorizontal(Double.valueOf(p), d, Double.valueOf(l), Double.valueOf(h), Integer.valueOf(indexG));
		return new Countresult(R);
	}
	
	/**
	 * 接地网模块
	 * @param p
	 * @param bc0
	 * @param bc1
	 * @param br0
	 * @param br1
	 * @param L0
	 * @param S
	 * @param h
	 * @param Lc
	 * @param Lr
	 * @param n
	 * @param indexr	垂直接地体形状的序号
	 * @param indexc	水平接地体形状的序号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/counterpoisegrounding", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
	public Countresult counterpoisegrounding(@RequestParam() String p, @RequestParam() String bc0, @RequestParam(required = false) String bc1,
			@RequestParam() String br0, @RequestParam(required = false) String br1, @RequestParam() String L0, @RequestParam() String S,
			@RequestParam() String h, @RequestParam() String Lc, @RequestParam() String Lr, @RequestParam() String n,  
			@RequestParam() String indexr,  @RequestParam() String indexc) {
		Double R = null;
		Double br = getd(indexr, br0, br1);
		Double bc = getd(indexc, bc0, bc1);
		/*
		 * 当S大于10000平方米，以水平接地体为主的接地网来计算，反之以复合接地网来计算
		 */
		if(Double.valueOf(S) > 10000d) {
			groundmat.gorizontalmat(Double.valueOf(p), Double.valueOf(Lc), Double.valueOf(Lr), Double.valueOf(L0), Double.valueOf(S),
					Double.valueOf(h), bc, Double.valueOf(n));
		}	else {
			groundmat.verticalmat(Double.valueOf(p), Double.valueOf(Lc), Double.valueOf(Lr), Double.valueOf(L0), Double.valueOf(S),
					Double.valueOf(h), bc, br, Double.valueOf(n));
		}
		return new Countresult(R);
	}
	
	/**
	 * 杆塔接地模块
	 * @param p
	 * @param b0
	 * @param b1
	 * @param L1
	 * @param L2
	 * @param h
	 * @param index
	 * @param indexT	杆塔接地形状系数的序号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/towergrounding", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
	public Countresult towergrounding(@RequestParam() String p, @RequestParam() String b0, @RequestParam(required = false) String b1,
			@RequestParam() String L1, @RequestParam() String L2, @RequestParam() String h, @RequestParam() String index,
			@RequestParam() String indexT) {
		Double d = getd(index, b0, b1);
		Double R = tower.gorizontal(Double.valueOf(p), Double.valueOf(L1), Double.valueOf(L2), Double.valueOf(h), d,
									Integer.valueOf(indexT));
		return new Countresult(R);
	}
	
	/**
	 * 垂直双层土壤模块
	 * @param p1
	 * @param p2
	 * @param A1
	 * @param S
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verticalDoubledeckMat", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
	public Countresult verticalDoubledeckMat(@RequestParam() String p1, @RequestParam() String p2, @RequestParam() String A1, 
			@RequestParam() String S) {
		Double R = doubledeckground.verticalDoubledeckMat(Double.valueOf(p1), Double.valueOf(p2), Double.valueOf(A1), Double.valueOf(S));
		return new Countresult(R);
	}
	
	/**
	 * 水平双层土壤模块
	 * @param p1		上层
	 * @param p2		下层
	 * @param bc0	水平
	 * @param bc1	不等边角钢
	 * @param br0	垂直
	 * @param br1	不等边角钢
	 * @param L0		水平接地体外缘周长
	 * @param S
	 * @param H
	 * @param h
	 * @param Lc		水平接地体总长度
	 * @param Lr		单根垂直接地体长度
	 * @param n							
	 * @param indexr	垂直接地体形状的序号
	 * @param indexc	水平接地体形状的序号
	 * @param type			1为接地网，2为单根垂直接地极，3为多根垂直接地极
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gorizontalDoubledeckMat", method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
	public Countresult gorizontalDoubledeckMat(@RequestParam() String p1, @RequestParam() String p2, @RequestParam(required = false) String bc0, 
			@RequestParam(required = false) String bc1, @RequestParam() String br0, @RequestParam(required = false) String br1, 
			@RequestParam() String L0, @RequestParam() String S, @RequestParam() String H, @RequestParam() String h, @RequestParam() String Lc, 
			@RequestParam() String Lr, @RequestParam() String n,  @RequestParam() String indexr,  @RequestParam(required = false) String indexc, 
			@RequestParam() String type) {
		Double R = null;
		Double d0 = getd(indexr, br0, br1);
		switch(type) {
		case "1":	Double d = getd(indexc, bc0, bc1);
							R = doubledeckground.gorizontalDoubledeckMat(Double.valueOf(p1), Double.valueOf(p2), Double.valueOf(S), Double.valueOf(H),
				Double.valueOf(h), Double.valueOf(Lc), Double.valueOf(L0), Double.valueOf(Lr), Double.valueOf(n), d, d0);
							break;
		case "2":	R = doubledeckground.gorizontalDoubledeckvertical(Double.valueOf(p1), Double.valueOf(p2), Double.valueOf(H), 
				Double.valueOf(h), Double.valueOf(Lr), d0);
							break;
		case "3":	R = doubledeckground.gorizontalDoubledeckverticals(Double.valueOf(p1), Double.valueOf(p2), Double.valueOf(S), 
				Double.valueOf(H), Double.valueOf(h), Double.valueOf(L0), Double.valueOf(Lr), Double.valueOf(n), d0);
		}
		return new Countresult(R);
	}
	
	/**
	 * @return d 等效直径
	 */
	private Double getd(String index, String b0, String b1) {
		/*
		 * 序号为 
		 * 1：圆钢
		 * 2：钢管
		 * 3：扁钢
		 * 4：等边角钢
		 * 5：不等边角钢
		 */
		Double d = null;
		if(!index.equals("5")) {
			d = Equivalentdiameter.getEequivalentdiameter(Integer.valueOf(index), Double.valueOf(b0));
		} else {
			d = Equivalentdiameter.getEequivalentdiameter(Integer.valueOf(index), Double.valueOf(b0), Double.valueOf(b1));
		}
		return d;
	}
	
}
