# aia-vt


These are a common jar file.

- com.ibm.aia.fim.jar  (The package is : com.ibm.aia.fim)
- httpclient-4.5.2.jar
- httpcore-4.4.4.jar
- httpmime-4.5.2.jar


----------


1. copy these jar files into WAS lib (/opt/IBM/WebSphere/AppServer/lib/ext). 
2. modify /opt/IBM/FIM/plugins/com.tivoli.am.fim.osgi.connector_6.2.2/META-INF/MANIFEST.MF, and append `,com.ibm.aia.fim`
3. then restart WAS and public TIFM plugins.


Then the Javascript Mapping rule can call this class and function.


