package control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dto.Countresult;
import dto.Data;
import dto.Result;
import service.impl.Building;
import service.impl.PowerStation;
import service.impl.Towers;
import util.charge.Change;

@Controller
@RequestMapping
public class Control {

	@Autowired
	private Building building;
	@Autowired
	private PowerStation powerStation;
	@Autowired
	private Towers towers;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value="/design", method = RequestMethod.POST, produces={"application/json; charset=utf-8"})
	public Result design(@RequestBody Data data) {
		Countresult countresult = null;
		System.out.println(data);
		switch(data.getstyle()) {
			case 1:
				if(data.gettype() == 1) {
					countresult = powerStation.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(), data.iscity());
				} else {
					countresult = building.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(), data.iscity());
				}
			break;
			case 2:countresult = powerStation.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(), data.iscity());
			break;
			case 3:countresult = towers.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(), data.iscity());
			break;
		}
		return Change.getResult(countresult, data);
	}
	
	@ResponseBody
	@RequestMapping(value="/design1", method = RequestMethod.POST, produces={"application/json; charset=utf-8"})
	public Result work1() {
//		Result result;
//		switch(data.getstyle()) {
//			case 1:
//				if(data.gettype() == 1) {
//					result = powerStation.design(p, H, p1, S, Rk, type, city);
//				} else {
//					result = building.design(p, H, p1, S, Rk, type, city);
//				}
//			break;
//			case 2:result = powerStation.design(p, H, p1, S, Rk, type, city);
//			break;
//			case 3:result = towers.design(p, H, p1, S, Rk, type, city);
//			break;
//		}
		return new Result("this is a plan", 2, 3, 4);
	}
}
