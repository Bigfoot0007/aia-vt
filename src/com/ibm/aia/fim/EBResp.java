package com.ibm.aia.fim;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EBResp {
	public static Logger logger = Logger.getLogger("EBResp");
	private String retxml = "";
	private int returncode = -1; // return code from response xml
	private String returnmsg = "";// return message from response xml
	private String vitalityID = "";// return vitalityid from response xml

	public EBResp(String returnxml) {
		retxml = returnxml;
		initByRegx(returnxml);
		if ("".equals(vitalityID)) {
			logger.info("[Failed] vitalityID:" + vitalityID);
		}else{
			logger.info("[Successful] vitalityID:" + vitalityID);
		}
	}

	// this the main function.
	private void initByRegx(String returnxml) {// extract the return value by
												// Regx format.
		// <code>0</code>
		logger.info("Begin paser the key values.... ");
		String message = "";
		Pattern p = Pattern.compile("<code>(.*)</code>"); // 正则表达式

		Matcher m = p.matcher(returnxml);
		message = "1). finding the return code: " + p.toString();
		
		if (m.find() == true) {
			returncode = Integer.parseInt(m.group(1).trim());
			message += ", return code: " + returncode;
		}
		logger.info(message);
		// <message>Yes</message>

		p = Pattern.compile("<message>(.*)</message>"); // 正则表达式
		m = p.matcher(returnxml);
		message = "2). finding the return message: " + p.toString();
		if (m.find() == true) {
			this.returnmsg = m.group(1).trim();
			message += ", return message: " + returnmsg;
		}
		logger.info(message);
		// <entityID>EID111111</entityID>

		p = Pattern.compile("<entityID>(.*)</entityID>"); // 正则表达式
		message = "3). finding the return VTID: " + p.toString();

		m = p.matcher(returnxml);

		if (m.find() == true) {
			this.vitalityID = m.group(1).trim();
			message += ", return VTID: " + vitalityID;
		}
		logger.info(message);
	}

	public int geReturnCode() {
		return this.returncode;
	}

	public String getVitalityID() {
		return this.vitalityID;
	}

	@Override
	public String toString() {
		return "CCResp [retxml=" + retxml + ", returncode=" + returncode + ", returnmsg=" + returnmsg + ", vitalityID=" + vitalityID + "]";
	}

}
