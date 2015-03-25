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
       `$ unzip ${tomcat-parent-directory}/apache-tomcat-7.0.59.zip`<br/>
       `$ cd ${tomcat-parent-directory}/apache-tomcat-7.0.59/bin`<br/>
       `$ rm *.bat`<br/>
       `$ chmod +x *.sh`<br/>
    * Setup APR (the Apache Portable Runtime) and Tomcat Native Support
    
      * Install libapr and libapr-devel using the linux package manager, on OpenSuse these packages are called libapr1 and libapr1-devel, on ubuntu libapr1 and libapr1-dev respectivly [xx][xx]. Additionally install OpenSSL development headers and jni development headers. Here [xx] is the complete list of requirements.
      * Locate "apr-1-config", which is usually at /usr/bin. It is needed to build Tomcat Native Support
      * Build Tomcat Native Support
       
         `$ cd ${tomcat-parent-directory}/apache-tomcat-7.0.59/bin`<br/>
         `$ tar -zxvf tomcat-native.tar.gz`<br/>
         `$ cd tomcat-native-${version}-src/jni/native`<br/>
         `$ ./configure --with-apr=${/path/to}/apr-1-config`<br/>
         `$ make`<br/>
         `$ sudo make install`<br/>




[xx] http://software.opensuse.org/package/libapr1
[xx] https://launchpad.net/ubuntu/+source/apr
[xx] https://tomcat.apache.org/tomcat-7.0-doc/apr.html#Linux
    
    
      
