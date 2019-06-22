FROM        ubuntu:18.04

MAINTAINER  Richard Rojas copy from <adamalex@gmail.com>

ENV         ACTIVATOR_VERSION 1.3.5
ENV         DEBIAN_FRONTEND noninteractive

# INSTALL OS DEPENDENCIES
RUN         apt-get update; apt-get install -y software-properties-common unzip

# INSTALL JAVA 8
RUN         apt-get update && \
            apt-get install -y openjdk-8-jdk wget vim 

# INSTALL TYPESAFE ACTIVATOR
RUN         cd /tmp && \
            wget http://downloads.typesafe.com/typesafe-activator/$ACTIVATOR_VERSION/typesafe-activator-$ACTIVATOR_VERSION.zip && \
            unzip typesafe-activator-$ACTIVATOR_VERSION.zip -d /usr/local && \
            mv /usr/local/activator-dist-$ACTIVATOR_VERSION /usr/local/activator 
#            rm activator-dist-$ACTIVATOR_VERSION.zip

# COMMIT PROJECT FILES
ADD         app /root/app
ADD         lib /root/lib
ADD         conf /root/conf
#ADD         public /root/public
ADD 	    key /root/key
ADD         build.sbt /root/
ADD         project/plugins.sbt /root/project/
ADD         project/build.properties /root/project/

# TEST AND BUILD THE PROJECT -- FAILURE WILL HALT IMAGE CREATION
#RUN         cd /root; /usr/local/activator/activator stage
#RUN         rm /root/target/universal/stage/bin/*.bat

# TESTS PASSED -- CONFIGURE IMAGE
WORKDIR     /root
#ENTRYPOINT  ["target/universal/stage/bin/simple-rest-scala"]
EXPOSE      9000
