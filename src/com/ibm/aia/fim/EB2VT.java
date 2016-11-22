package com.ibm.aia.fim;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/*
 * This class for retrievel the VT Entity ID by ebid.
 */

public class EB2VT {
	public static Logger logger = Logger.getLogger("EB2VT");
	private String ebid = "";
	private String requestMessage = ""; // request XML, load from ccrequest.xml and fill the ebid.

	public EB2VT(String ebid) {
		this.ebid = ebid;
		prepareCCReqestBody(ebid);
		logger.info(">>>> Request EBID: " + ebid);
		logger.info(this.requestMessage);
	}

	@SuppressWarnings("unused")
	private EB2VT() { // hide the default contructure ,must use the
		super();
	}

	// HTTP POST request
	public EBResp Post(String ccWSUrl) throws Exception {
		if(ebid=="" || ebid==null){
			throw new Exception("EBID is NULL or Empty");
		}
		if(ccWSUrl=="" || ccWSUrl==null){
			throw new Exception("EB Service URL is Not Correct.");
		}
		URL obj;
		if (ccWSUrl.isEmpty()) {
			obj = new URL(Config.CCWSServiceEP);
		} else {
			obj = new URL(ccWSUrl);
		}

		HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", Config.USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		conn.setRequestProperty("SOAPAction", "http://ws.vitality.aiab.com/HasVitalityPolicyerer");
		logger.info("POST TO : " + conn.getURL());
		// Send post request
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(requestMessage);
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		logger.info("<< " + conn.getResponseCode());
		logger.info("<< " + conn.getResponseMessage());

		if (responseCode != 200)
			return null; // not nornal return, return Null.

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		conn.disconnect();
		logger.info("<< BODY: " + response.toString());
		return new EBResp(response.toString());

	}

	private String prepareCCReqestBody(String ebid) {
		String requestString = "";
		try {
			requestString = Config.readTemplatefile(Config.EBReq);
			requestMessage = requestString.replaceAll("%EBID%", this.ebid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestMessage;

	}

	public String getebid() {
		return ebid;
	}

	public void setebid(String ebid) {
		this.ebid = ebid;
	}

	@Override
	public String toString() {
		return "CC2VT [ebid=" + ebid + "]\n\n" + requestMessage + "\n\n";
	}

}
