package control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dto.Data;
import dto.Result;
import service.impl.Building;
import service.impl.PowerStation;
import service.impl.Towers;

@Controller
@RequestMapping
public class Control {

	@Autowired
	private Building building;
	@Autowired
	private PowerStation powerStation;
	@Autowired
	private Towers towers;

	@ResponseBody
	@RequestMapping(value="/control", method = RequestMethod.POST, produces={"application/json; charset=utf-8"})
	public Result work(Data data) {
		
		return null;
	}
	
	
}
