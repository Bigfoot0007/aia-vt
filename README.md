# aia-vt
This is a common jar file.

com.ibm.aia.fim.jar  (The package is : com.ibm.aia.fim)

#. copy WAS lib (/opt/IBM/WebSphere/AppServer/lib). 
#. modify /opt/IBM/FIM/plugins/com.tivoli.am.fim.osgi.connector_6.2.2/META-INF/MANIFEST.MF
   and append ",com.ibm.aia.fim" into.

# then restart WAS and public TIFM plugins.


Then the Javascript Mapping rule can call this class and function.
