importPackage(Packages.com.tivoli.am.fim.trustserver.sts);
importPackage(Packages.com.tivoli.am.fim.trustserver.sts.uuser);
importPackage(Packages.java.lang); // for System object
importPackage(Packages.java.util); // for System object
importPackage(Packages.com.ibm.aia.fim); // for Customer Corner to Vitality ID.

/**
 * * Globale settings. Version 2016.06.05 updated. VT login failed.
 */

var aliveurl = "https//ssodev.aia.com.hk/keep.jsp";
var logouturl = "https//ssodev.aia.com.hk/pkmglogout";
var vtmemberid = ""; // this id will get by WebService, the default is Empty
var ccws = "http://10.49.105.227/customer/services/VitalityService";
var ebws = "http://10.72.3.53:7042/eCOMPASS/EbService";
var vtid = ""; // VTID default is empty
var ssoid = "";
var nonssoid = "";

var prinValues = java.lang.reflect.Array.newInstance(java.lang.String, 1);
prinValues[0] = stsuu.getPrincipalName();
var ssoid = prinValues[0];

var hostedsystemname = stsuu.getAttributeValueByName("tagvalue_hostedsystemname");

// Clean Space and uppercase it.

if (hostedsystemname != null) {
	hostedsystemname = hostedsystemname.replace("/(^\s*)|(\s*$)/g", "");
	hostedsystemname = hostedsystemname.toUpperCase();
} else {
	System.out.println("Import Messages"); // import into FIM will use this route.
}

/**
 * * The main Process.
 */

System.out.println("=============== VT SP DEBUG BEGIN ====================");
// System.out.println(stsuu); // show the income token message.
System.out.println(">>>>> HOSTEDSYSTEMNAME : " + hostedsystemname);

var iter = stsuu.getAttributes();
while (iter.hasNext()) {
	var ii = iter.next();
	System.out.println(">>>>> [ " + ii.getName() + " ] = " + ii.getValues()[0]);
}
// Get Language

var lang = (stsuu.getAttributeValueByName("tagvalue_currentlang") == "" || stsuu.getAttributeValueByName("tagvalue_currentlang") == null) ? "en" : stsuu.getAttributeValueByName("tagvalue_currentlang");
/**
 * * Clean the current Universal User. and create a new Token
 */

System.out.println("------------------------ Prepare the NEW TOKEN -----------------------------");
System.out.println(">>>>>>>>>>> " + ssoid + " >>>>>>>>>>>");
System.out.println(">>>>>>>>>>> Try to get VTID   >>>>>>>>>>>");
vtid = getVTID(); // Main sub function
System.out.println(">>>>>>>>>>> Try to get VTID Successful, Clean current STUU  >>>>>>>>>>>");
stsuu.clear();
var principalAttr = new Attribute("name", "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified", vtid);
stsuu.addPrincipalAttribute(principalAttr);
stsuu.addAttribute(new Attribute("member-id", "", vtid));
stsuu.addAttribute(new Attribute("locale", "", lang));
stsuu.addAttribute(new Attribute("logout-url", "", ""));
System.out.println(stsuu);

System.out.println("================ VT SP  DEBUG ENDING ===================");

/*
 * Get VTID
 */
function getVTID() {
	var vtid = "";
	if (ssoid == "#novalue#") { // Non-SSOID

		System.out.println("=== Enter Non-SSO Process.====");

		/*
		 * Fixed by Gxy on 2016.06.05 if (!(hostedsystemname == "CC" || hostedsystemname == "EB")) { // only CC and EB supported throw "The hostedsystemname is " + hostedsystemname + ", only CC or EB supported for Non-SSOID."; }
		 */
		nonssoid = stsuu.getAttributeValueByName("tagvalue_currentuid"); // currentid is the nonssoid
		System.out.println("=== CurrentUID : " + nonssoid);
		if (hostedsystemname == "CC") {
			vtid = getVTIDfromCC(nonssoid);
		}
		if (hostedsystemname == "EB") {
			vtid = getVTIDfromEB(nonssoid);
		}

	} else { // SSOID

		// Try CCID first

		ssoid = stsuu.getAttributeValueByName("tagvalue_aiaccid")
		System.out.println("=== Enter SSO Process.====: " + ssoid);
		if (ssoid != "" && ssoid != null) {
			vtid = getVTIDfromCC(ssoid);
		}
		// Then Try EBID .
		if (vtid == "") {
			ssoid = stsuu.getAttributeValueByName("tagvalue_aiaebid")
			if (ssoid != "" && ssoid != null) {
				vtid = getVTIDfromEB(ssoid);
			}
		}
		if (vtid == "") {
			System.out.println("Can't get VTID from CC or EB, please ask the support team's help!");
		}

	}
	return vtid;
}

// Function get VTID form Customer Corner
function getVTIDfromCC(ccid) {
	var returnid = "";
	try {
		var cc2vt = new CC2VT(ccid);
		System.out.println(cc2vt);
		var ccresp = cc2vt.Post(ccws);
		returnid = ccresp.getVitalityID();
		if (returnid == "" || returnid == null) {
			throw "Error: Can't get Vitality ID from CC Service : " + ccws;
		}

	} catch (err) {
		System.out.println(err);
		throw "Customer Corner VT Service error." + ccws + err;
	}
	return returnid;
}

// Function
function getVTIDfromEB(ebid) {
	var returnid = "";
	try {
		var eb2vt = new EB2VT(ebid);
		System.out.println(eb2vt);
		var ebresp = eb2vt.Post(ebws);
		returnid = ebresp.getVitalityID();
		if (returnid == "" || returnid == null) {
			throw "Error: Can't get Vitality ID from EB Service : " + ebws;
		}

	} catch (err) {
		System.out.println(err);
		throw "Employee Benifit VT Service error." + ebws + err;
	}
	return returnid;
}
