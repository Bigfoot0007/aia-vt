package com.ibm.aia.fim;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CCResp {
	public static Logger logger = Logger.getLogger("CCResp");
	private String retxml = "";
	private int returncode = -1; // return code from response xml
	private String returnmsg = "";// return message from response xml
	private String vitalityID = "";// return vitalityid from response xml

	public CCResp(String returnxml) {
		retxml = returnxml;
		initByRegx(returnxml);
		if ("".equals(vitalityID)) {
			logger.info("[Failed] vitalityID:" + vitalityID);
		}else{
			logger.info("[Successful] vitalityID:" + vitalityID);
		}
	}

	// this the main function.
	private void initByRegx(String returnxml) {// extract the return value by Regx format.
		// <returnCd xmlns="">0</returnCd>
		logger.info("Begin paser the key values.... ");
		String message = "";
		Pattern p = Pattern.compile("<returnCd xmlns=\"\">(.*)</returnCd>"); // return code
		Matcher m = p.matcher(returnxml);
		
		message = "1). finding the return code: " + p.toString();
		if (m.find() == true) {
			returncode = Integer.parseInt(m.group(1).trim());
			message += ", return code: " + returncode;
		}
		logger.info(message);

		// <returnMsg xmlns="">Yes</returnMsg>

		p = Pattern.compile("<returnMsg xmlns=\"\">(.*)</returnMsg>"); // return message
		m = p.matcher(returnxml);
		message = "2). finding the return message: " + p.toString();
		if (m.find() == true) {
			this.returnmsg = m.group(1).trim();
			message += ", return message: " + returnmsg;
		}
		logger.info(message);
		// <EntityID xmlns="">5500766786</EntityID>

		p = Pattern.compile("<EntityID xmlns=\"\">(.*)</EntityID>"); // return vtid.
		m = p.matcher(returnxml);
		message = "3). finding the return VTID: " + p.toString();
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
		return "=== CCResp [retxml=\n" + retxml + "\n returncode=" + returncode + ", returnmsg=" + returnmsg + ", vitalityID=" + vitalityID + "]";
	}

}
