importPackage(Packages.com.tivoli.am.fim.trustserver.sts);
importPackage(Packages.com.tivoli.am.fim.trustserver.sts.uuser);
importPackage(Packages.java.lang); // for System object
importPackage(Packages.com.ibm.aia.fim); // for Customer Corner to Vitality ID.

/**
 * * Globale settings.
 */

var aliveurl = "https//ssodev.aia.com.hk/keep.jsp";
var logouturl = "https//ssodev.aia.com.hk/pkmglogout";
var vtmemberid = ""; // this id will get by WebService, the default is Empty
var ccws = "http://aiabdwww3.aia.biz/customer/services/VitalityService";
var ebws = "http://10.69.4.53:7042/eCOMPASS/EbService";

// Get the current principal name.
var prinValues = java.lang.reflect.Array.newInstance(java.lang.String, 1);
prinValues[0] = stsuu.getPrincipalName();

var hostedsystemname = stsuu.getAttributeValueByName("tagvalue_hostedsystemname");

// Clean Space and uppercase it.

if(hostedsystemname != null){
	hostedsystemname = hostedsystemname.replace("/(^\s*)|(\s*$)/g", "");
	hostedsystemname = hostedsystemname.toUpperCase();
}

/**
 * * The main Process.
 */

System.out.println("=============== VT SP DEBUG BEGIN ====================");
System.out.println(stsuu); // show the income token message.
System.out.println(">>>>> HOSTEDSYSTEMNAME >>>>>> " + hostedsystemname + ">>>>>> HOSTEDSYSTEMNAME >>>>>");

if (hostedsystemname == null ) {
	System.out.println("HOSTEDSYSTEMNAME is NULL, the following action will be skippted.....");
}
// if the hosted system is Customer Conner, get the member id from CC
if (hostedsystemname == "CC") {
	var ccid = stsuu.getAttributeValueByName("tagvalue_aiaccid")
	try {
		var cc2vt = new CC2VT(ccid);
		System.out.println(cc2vt);
		var vtmemberid = cc2vt.Post(ccws);
	} catch (err) {
		System.out.println(err);
		throw "Customer Corner VT Service error." + err;
	}
}

// if the hosted system is EB, get the member id from EB
if (hostedsystemname == "EB") {

	try {
		var eb2vt = new EB2VT(prinValues);
		System.out.println(eb2vt);
		var vtmemberid = cc2vt.Post(ebws);
	} catch (err) {
		System.out.println(err);
		throw "Employee Benifit VT Service error." + err;
	}
}

if (hostedsystemname!=null && hostedsystemname != "CC" &&  hostedsystemname != "EB") {
	System.out.println("HOSTEDSYSTEMNAME is NULL, the following action will be skippted.....");
	throw "Error HOSTEDSYSTEMNAME, Unsupport, ONLY CC and EB is supported.";
}

/**
 * * Clen the current Universal User. and create a new Token
 */

System.out.println("------------------------ Prepare the NEW TOKEN -----------------------------");
System.out.println(">>>>>>>>>>> " + prinValues + ">>>>>>>>>>>");
stsuu.clear();

var principalAttr = new Attribute("name", "urn:ibm:names:ITFIM:5.1:accessmanager", prinValues);
stsuu.addPrincipalAttribute(principalAttr);
stsuu.addAttribute(new Attribute("member-id", "", vtmemberid));
stsuu.addAttribute(new Attribute("keep-alive-url", "", aliveurl));
stsuu.addAttribute(new Attribute("logout-url", "", logouturl));
System.out.println(stsuu);

System.out.println("================ VT SP  DEBUG ENDING ===================");

