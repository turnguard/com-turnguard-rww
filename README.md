# com-turnguard-rww

Welcome to com-turnguard-rww!

This is the new parent of all com-turnguard-rww (Read Write Web) activity.

- com-turnguard-rww
  - com-turnguard-rww-webid
  
      This section gives an overview of how to setup a tomcat webserver for use with WebID+TLS. This excercise involves three parts
    * Setup APR (the Apache Portable Runtime) and Tomcat Native Support.
       
      This step is necessary to be able to use Self Signed Certificates. We will use the APR Connector, which exposes a couple of settings that a required, mainly SSLVerifyClient="optionalNoCA". 
    * Create Self Signed Server Certificate and Key.
    * Configure Tomcat for SSL.
    * Configure Tomcat to enable WebIDRealm.
    * Setup a Debug Web Application to verify the installation.
  
    Following is the complete list of steps that need to be taken from scratch
    * Download Tomcat. 
    
       `$ cd ${tomcat-parent-directory}`<br/>
       `$ wget http://mirror2.klaus-uwe.me/apache/tomcat/tomcat-7/v7.0.59/bin/apache-tomcat-7.0.59.zip`<br/>
       `$ unzip ${tomcat-parent-directory}/apache-tomcat-7.0.59.zip`<br/>
       `$ cd ${tomcat-parent-directory}/apache-tomcat-7.0.59/bin`<br/>
       `$ rm *.bat`<br/>
       `$ chmod +x *.sh`<br/>
    * Setup APR (the Apache Portable Runtime) and Tomcat Native Support.
    
      * Install libapr and libapr-devel using the linux package manager, on OpenSuse these packages are called libapr1 and libapr1-devel, on ubuntu libapr1 and libapr1-dev respectivly [1][2]. Additionally install OpenSSL development headers and jni development headers. Here [3] is the complete list of requirements.
      * Locate "apr-1-config", which is usually at /usr/bin. It is needed to build Tomcat Native Support
      * Build Tomcat Native Support
       
         `$ cd ${tomcat-parent-directory}/apache-tomcat-7.0.59/bin`<br/>
         `$ tar -zxvf tomcat-native.tar.gz`<br/>
         `$ cd tomcat-native-${version}-src/jni/native`<br/>
         `$ ./configure --with-apr=${/path/to}/apr-1-config`<br/>
         `$ make`<br/>
         `$ sudo make install`<br/>

         Usually libtcnative will be installed to /usr/local/apr/lib. You can now either setup or LD_LIBRARY_PATH
         or simply create a symlink to libtcnative-1.so inside your lib directory (lib or lib64) in case it is not already there.
         
    * Create Self Signed Server Certificate and Key.
    
      `$ cd ${tomcat-parent-directory}/apache-tomcat-7.0.59/conf`<br/>
      `$ mkdir ssl`<br/>
      `$ cd ssl`<br/>
      `$ openssl req -x509 -newkey rsa:2048 -keyout key.pem -out cert.pem -days 365`
      
      Enter a password and the values for the certificate. Note that you should use the host's name as the name (in this example: localhost)
      
    * Configure Tomcat for SSL.
    
      We will now create an APR SSL Connector inside server.xml. I'm using VIM for this. Note that in server.xml there are already a couple of connectors defined. Simply add the xml snippet (see below) right after the standard connector for port 8080 inside the "Service" section. (Also note that there's a clean server.xml without comments in the releases section)
    
      `$ cd ${tomcat-parent-directory}/apache-tomcat-7.0.59/conf`<br/>
      `$ vim server.xml`<br/>
      
      paste the following snippet and save the file replacing the value for SSLPassword with the password that was used in the previous step<br/>
      
      ```xml
        <Connector
          clientAuth="false" port="8443" minSpareThreads="5" maxSpareThreads="75"
          enableLookups="true" disableUploadTimeout="true"
          acceptCount="100" maxThreads="200"
          scheme="https" secure="true" SSLEnabled="true"
          SSLCertificateFile="conf/ssl/cert.pem"
          SSLCertificateKeyFile="conf/ssl/key.pem"
          SSLPassword="${your-pem-password}"
          SSLVerifyClient="optionalNoCA" 
          SSLEngine="on" 
          SSLVerifyDepth="2" 
          sslProtocol="TLS"/>
      ```
    * Configure Tomcat for WebIDRealm.
      * Download tomcat-users.rdf from the releases section of this repository and store it in ${tomcat-parent-directory}/apache-tomcat-7.0.59/conf
      * Edit tomcat-users.rdf to give yourself the "DebugRole", replacing ${YOUR-WEB-ID} by the actual URI. Note that the "DebugRole" is already defined, it will be used later, when verifying the installation.
      ```xml
        <rdf:Description rdf:about="${YOUR-WEB-ID}">
          <rdf:type rdf:resource="#User"/>
          <webid:hasRole rdf:resource="http://data.turnguard.com/webid/2.0/DebugRole"/>
        </rdf:Description>
      ```
      * Download all libraries (*.jar) from the releases section of this repository and store them in ${tomcat-parent-directory}/apache-tomcat-7.0.59/lib
      * Edit ${tomcat-parent-directory}/apache-tomcat-7.0.59/conf/server.xml to include the WebIDDatabase (this is where WebIDRealm will look for users and roles instead of the default tomcat-users.xml). Copy the following xml snippet into the "GlobalNamingResources" section
      ```xml
      <Resource name="WebIDDatabase" auth="Container"
        type="com.turnguard.rww.webid.database.WebIDDatabase"
        description="Userdatabase graph for the WebIDRealm"
        factory="com.turnguard.rww.webid.database.impl.openrdf.MemoryStoreFactory"
        pathname="conf/tomcat-users.rdf" />
      ```
      * Edit ${tomcat-parent-directory}/apache-tomcat-7.0.59/conf/server.xml to include the WebIDRealm in the "Engine" section. Note: You should remove all other "Realm" definitions from the default "Engine" section in server.xml before.
      ```xml
      <Realm className="com.turnguard.rww.webid.tomcat.realm.WebIDRealm" 
        resourceName="WebIDDatabase" 
        validate="false"/>
      ```
    * Install Verifcation WebApp.
      * Download the webid-verification.war file from the releases section and store it in ${tomcat-parent-directory}/apache-tomcat-7.0.59/webapps
      * Start the server by 
      
        `$ cd ${tomcat-parent-directory}`<br/>
        `$ ./bin/startup.sh`<br/>
        
      * Point your browser to https://localhost:8443/webid-verification
        This should first bring up a popup that let's you choose your browser certificate. After you have chosen a certificate, you should either see an error page in case something went wrong, or a success page, displaying a couple of facts from your webID.
        
    * Play.
      * Remove your role triples from ${tomcat-parent-directory}/apache-tomcat-7.0.59/conf/tomcat-users.rdf and restart the server and try to log in again (Note that firefox is capabale of deleting "active logins" in the preferences section otherwise you might need to clear cookies and cache). Or..
      * Open web.xml (${tomcat-parent-directory}/webapps/webid-verification/WEB-INF/web.xml) and checkout the security configuration. By default the url-pattern is "*". You could create a new directory inside the application's directory and protect only this directory by changing the url-pattern. See tomcat docs for details about security-constraints.
    * Next
      * Tutorial on using virtuoso as UserDatabase (instead of the file tomcat-users.rdf)
      * Adaptions for other servlet engines like WebSphere and friends..
      

* [1] http://software.opensuse.org/package/libapr1
* [2] https://launchpad.net/ubuntu/+source/apr
* [3] https://tomcat.apache.org/tomcat-7.0-doc/apr.html#Linux
