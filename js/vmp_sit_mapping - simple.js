importPackage(Packages.com.tivoli.am.fim.trustserver.sts);
importPackage(Packages.com.tivoli.am.fim.trustserver.sts.uuser);
importPackage(Packages.java.lang); // for System object
importPackage(Packages.com.ibm.aia.fim); // for Customer Corner to Vitality
// ID.

/**
 * * Globale settings.
 */

var aliveurl = "https//ssodev.aia.com.hk/keep.jsp";
var logouturl = "https//ssodev.aia.com.hk/pkmglogout";
var vtmemberid = "042940605"; // this id will get by WebService, the default
// is Empty

// Get the current principal name.
var prinValues = java.lang.reflect.Array.newInstance(java.lang.String, 1);
prinValues[0] = stsuu.getPrincipalName();

/**
 * * The main Process.
 */

System.out.println("=============== VT SP DEBUG BEGIN ====================");
System.out.println(stsuu); // show the income token message.

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
