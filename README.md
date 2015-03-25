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
    * Download Tomcat 
    
       `$ cd ${tomcat-parent-directory}`<br/>
       `$ wget http://mirror2.klaus-uwe.me/apache/tomcat/tomcat-7/v7.0.59/bin/apache-tomcat-7.0.59.zip`<br/>
       `$ unzip apache-tomcat-7.0.59`<br/>
       `$ cd apache-tomcat-7.0.59/bin`<br/>
       `$ rm *.bat`<br/>
       `$ chmod +x *.sh`<br/>
    
    
    
      
