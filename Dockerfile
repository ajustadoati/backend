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

WORKDIR     /app

EXPOSE      9000
