package com.ibm.aia.fim;

public class mainTest {

	/*
	 * This is the main function. java -jar com.ibm.aia.fim.mainEBTest http://10.72.3.53:7042/eCOMPASS/EbService EB98322334
	 */

	public static void main(String[] args) {

		String vitalityID = "";
		String defaultService = "CC";
		String uid = "defaultuid";
		
		String url = "https://123.57.221.117/aia/ws.do";

		if (args.length > 2) {
			defaultService = args[0];
			url = args[1];
			uid = args[2];
		}else{
			System.out.println("Error. Parameter failed.");
			return;
		}
		System.out.println("defaultService="+defaultService+",url="+url+",uid="+uid);
		/*
		for (int i = 0; i < args.length; i++) {
			System.out.println("args[" + i + "] £º" + args[i]);
		}*/

		if ("CC".equalsIgnoreCase(defaultService)) {

			CC2VT cc2vt = new CC2VT(uid);

			CCResp ccresp;
			try {
				ccresp = cc2vt.PostHTTPClient(url);
				
				if (ccresp.geReturnCode() == 0) {
					vitalityID = ccresp.getVitalityID();
					System.out.println("vitalityID : " + vitalityID);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("EB".equalsIgnoreCase(defaultService)) {

			EB2VT eb2vt = new EB2VT(uid);

			EBResp ccresp;
			try {
				ccresp = eb2vt.PostHTTPClient(url);
				// System.out.println(ccresp);
				if (ccresp.geReturnCode() == 0) {
					vitalityID = ccresp.getVitalityID();
					// System.out.println("vitalityID : " + vitalityID);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Unsuppored Service : " + defaultService + ", Please check, only support CC EB. ");
		}
	}

}
