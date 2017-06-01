package control;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.impl.Building;
import service.impl.PowerStation;
import service.impl.Towers;
import util.charge.Charge;
import util.ip.Iprecord;
import dto.Countresult;
import dto.Data;
import dto.Result;

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
	public String index(HttpServletRequest request) {
		Set<String> set;
		if((set = Iprecord.iplog(request)) != null) request.getServletContext().setAttribute("ipset", set);;
		return "index";
	}

	@ResponseBody
	@RequestMapping(value = "/design", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	public Result design(@RequestBody Data data) {
		Countresult countresult = null;
//		System.out.println(data);
		switch (data.getstyle()) {
		case 1:
			if (data.gettype() == 1) {
				countresult = powerStation.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(),
						data.iscity());
			} else {
				countresult = building.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(),
						data.iscity());
			}
			break;
		case 2:
			countresult = powerStation.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(),
					data.iscity());
			break;
		case 3:
			countresult = towers.design(data.getp(), data.getH(), data.getp1(), data.getS(), data.getRk(), data.gettype(), data.iscity());
			break;
		}
		return Charge.getResult(countresult, data);
	}
	
}