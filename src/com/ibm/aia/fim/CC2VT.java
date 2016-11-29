package com.ibm.aia.fim;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/*
 * This class for retrievel the VT Entity ID by CCid.
 */

public class CC2VT {
	public static Logger logger = Logger.getLogger("CC2VT");
	private String ccid = "";
	private String requestMessage = ""; // request XML, load from ccrequest.xml and fill the ccid.

	public CC2VT(String ccid) {
		this.ccid = ccid;
		logger.info(">>>>> Request CCID: " + ccid);
		prepareCCReqestBody(ccid);

	}

	@SuppressWarnings("unused")
	private CC2VT() { // hide the default contructure ,must use the
		super();
	}

	// HTTP POST request
	public CCResp Post(String ccWSUrl) throws Exception {
		if (this.ccid == "" || this.ccid == null) {
			throw new Exception("CCID is NULL or Empty");
		}
		if (ccWSUrl == "" || ccWSUrl == null) {
			throw new Exception("CC Service URL is Not Correct.");
		}
		URL obj;
		if (ccWSUrl.isEmpty()) {
			obj = new URL(Config.CCWSServiceEP);
		} else {
			obj = new URL(ccWSUrl);
		}

		logger.info(ccWSUrl);

		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

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
		return new CCResp(response.toString());

	}

	private String prepareCCReqestBody(String ccid) {
		String requestString = "";
		try {
			requestString = Config.readTemplatefile(Config.CCReq);
			requestMessage = requestString.replaceAll("%CCID%", this.ccid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(" === Preparing the POST BODY === :\n" + this.requestMessage);
		return requestMessage;

	}

	public String getCcid() {
		return ccid;
	}

	public void setCcid(String ccid) {
		this.ccid = ccid;
	}

	@Override
	public String toString() {
		return "CC2VT [ccid=" + ccid + "]\n\n" + requestMessage + "\n\n";
	}

	public CCResp PostHTTPClient(String url) throws IOException {
		CloseableHttpResponse response = null;
		String ret = "";
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}

			}).build();

			CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

			// HttpGet httpGet = new HttpGet("https://123.57.221.117");
			HttpPost httpost = new HttpPost(url);
			httpost.setHeader("User-Agent", "FIM62.2.2");
			httpost.setHeader("Accept-Language", "en-US,en;q=0.5");
			httpost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			httpost.setHeader("SOAPAction", "http://ws.vitality.aiab.com/HasVitalityPolicyerer");
			httpost.setEntity(new StringEntity(this.requestMessage));
			logger.info("Post to >>>>>  " + url);
			response = httpClient.execute(httpost);
			logger.info(response.toString());
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				ret = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
			}
			response.close();
			logger.info("Server Return: \n" + ret);
			return new CCResp(ret);

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (null != response) {
				response.close();
			}

		}
		return null;
	}

}
