package com.ibm.aia.fim;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EBResp {
	public static Logger logger = Logger.getLogger("EBResp");
	private String retxml = "";
	private int returncode = 0; // return code from response xml
	private String returnmsg = "";// return message from response xml
	private String vitalityID = "";// return vitalityid from response xml

	public EBResp(String returnxml) {
		retxml = returnxml;
		initByRegx(returnxml);
		logger.info("<<<<< vitalityID:"+vitalityID);
	}

	// this the main function.
	private void initByRegx(String returnxml) {// extract the return value by
												// Regx format.
		// <code>0</code>

		Pattern p = Pattern.compile("<code>(.*)</code>"); // 正则表达式

		Matcher m = p.matcher(returnxml);
		logger.info("DEBUG:return msg:" + p.toString());

		if (m.find() == true) {
			returncode = Integer.parseInt(m.group(1).trim());
		}

		// <message>Yes</message>

		p = Pattern.compile("<message>(.*)</message>"); // 正则表达式
		logger.info("DEBUG:return msg:" + p.toString());

		m = p.matcher(returnxml);

		if (m.find() == true) {
			this.returnmsg = m.group(1).trim();
		}

		// <entityID>EID111111</entityID>

		p = Pattern.compile("<entityID>(.*)</entityID>"); // 正则表达式
		logger.info("DEBUG:return msg:" + p.toString());

		m = p.matcher(returnxml);

		if (m.find() == true) {
			this.vitalityID = m.group(1).trim();
		}

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
