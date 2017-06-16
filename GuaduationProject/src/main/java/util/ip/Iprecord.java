package util.ip;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class Iprecord {

	private static final Logger logger = LoggerFactory.getLogger("watch");
	
	/**
	 * 查询记录ip地址
	 * @param request
	 * @param use			true:使用,false:访问
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> iplog(HttpServletRequest request, boolean use) {
		Set<String> set;
		String ip = getip(request);
//		System.out.println("iplog ip:"ip);
		if(!use) {
			set = (Set<String>) request.getServletContext().getAttribute("ipset");
			if(!set.contains(ip)){
//				if(ipcheck(request, ip)){
//					logger.info("ip:"+ip+" 四川电信访问\r\n");
//				} else {
					logger.info("ip:"+IpQuery.queryIP(ip)+" 访问\r\n");
//				}
				set.add(ip);
				return set;
			} else {
				logger.info("ip:"+ip+" 访问\r\n");
			}
		} else {
			logger.info("ip:"+ip+" 使用\r\n");
		}
		return null;
	}

	/**
	 * 检查ip地址是否在四川电信范围
	 * @param request
	 * @param ip
	 * @return
	private static boolean ipcheck(HttpServletRequest request, String ip) {
		@SuppressWarnings("unchecked")
		List<String> list = (ArrayList<String>)request.getServletContext().getAttribute("iplist");
		ArrayList<String> al1 = new ArrayList<String>();
		ArrayList<String> al2 = new ArrayList<String>();
		ArrayList<String> alip = new ArrayList<String>();
		for(int index = 1; index < list.size(); index += 2) {
			if(ip.equals(list.get(index)) || ip.equals(list.get(index+1))) return true;
			for(String S : list.get(index).split("\\."))	al1.add(S);
			for(String S : list.get(index+1).split("\\."))	 al2.add(S);
			for(String S : ip.split("\\.")) alip.add(S);
			for(int j = 0; j < 4; j++) {
				if(al1.get(j).equals(alip.get(j)) && al2.get(j).equals(alip.get(j))) continue;
				int al1num = Integer.parseInt(al1.get(j));
				int al2num = Integer.parseInt(al2.get(j));
				int alipnum = Integer.parseInt(alip.get(j));
				if((al1num <= alipnum && alipnum <= al2num) || (al2num <= alipnum && alipnum <= al1num)){
					return true;
				} else {
					al1.clear();
					al2.clear();
					alip.clear();
					break;
				}
			}
		}
		return false;
	}
	 */

	private static String getip(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
	
}
