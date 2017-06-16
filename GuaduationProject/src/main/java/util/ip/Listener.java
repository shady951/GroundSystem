package util.ip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Listener implements ServletContextListener {

	public Listener() {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
//		System.out.println("ipinit---------------");
		String ippath = arg0.getServletContext().getRealPath("/WEB-INF/logs/watch/ipcontainer.txt");
//		String iplist = arg0.getServletContext().getRealPath("/WEB-INF/iptxt/iplist.txt");
//		System.out.println("webrootkey:"+System.getProperty("GP"));
		File file = new File(ippath);
		InputStreamReader reader;
		BufferedReader br;
		Set<String> set = new HashSet<String>();
//		List<String> list = new ArrayList<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write("0:0:0:0:0:0:0:1\r\n127.0.0.1\r\n");
				bw.close();
			}
			reader = new InputStreamReader(new FileInputStream(file));
			br = new BufferedReader(reader);
			//读取访问过的ip
			String line = "";
			for(line = br.readLine(); line != null; line = br.readLine()) {
				set.add(line);
			}
			//读取四川电信ip
//			reader = new InputStreamReader(new FileInputStream(iplist));
//			br = new BufferedReader(reader);
//			for(line = br.readLine(); line != null; line = br.readLine()){
//				list.add(line);
//			}
			br.close();
			reader.close();
//			arg0.getServletContext().setAttribute("iplist", list);
			arg0.getServletContext().setAttribute("ipset", set);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		@SuppressWarnings("unchecked")
		Set<String> set = (HashSet<String>)arg0.getServletContext().getAttribute("ipset");
		Iterator<String> it = set.iterator();
//		System.out.println("Destroyed set:"+set.toString());
		String ippath = arg0.getServletContext().getRealPath("/WEB-INF/logs/watch/ipcontainer.txt");
		File file = new File(ippath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(; it.hasNext(); ) {
				bw.write(it.next().toString()+"\r\n");
			}
			bw.close();
		} catch (Exception e) {
		}

	}

}
