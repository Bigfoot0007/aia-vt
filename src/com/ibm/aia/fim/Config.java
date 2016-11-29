package com.ibm.aia.fim;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

// Main Internal configure class, define some parameters and something.
public class Config {

	public static String CCWSServiceEP = "http://aiabdwww3.aia.biz/customer/services/VitalityService";
	public static String CCReq = "ccrequest.xml";
	public static String CCRes = "ccresponse.xml";
	public static String EBReq = "ebrequest.xml";
	public static String EBRes = "ebresponse.xml";
	public static String USER_AGENT = "IBM_TFIMBG_6.2.2";

	public static String readTemplatefile(String filename) {
		
		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(filename);
		String line;
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
