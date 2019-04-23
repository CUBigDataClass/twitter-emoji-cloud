FROM ubuntu

# Install required packages

RUN apt-get update && \
    apt-get install -y wget && \
    apt-get install -y build-essential && \
    apt-get install -y software-properties-common && \
    apt-get install -y python

# Install Java.

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME


# Install Storm.

ENV STORM_USER=usama \
    STORM_CONF_DIR=/conf \
    STORM_DATA_DIR=/data \
    STORM_LOG_DIR=/logs

# Add a user and make dirs
RUN set -ex; \
    adduser "$STORM_USER"; \
    mkdir -p "$STORM_CONF_DIR" "$STORM_DATA_DIR" "$STORM_LOG_DIR"; \
    chown -R "$STORM_USER:$STORM_USER" "$STORM_CONF_DIR" "$STORM_DATA_DIR" "$STORM_LOG_DIR"``

ARG DISTRO_NAME=apache-storm-1.2.2

# Download Apache Storm.
RUN set -ex; \
    wget -q "http://www.apache.org/dist/storm/$DISTRO_NAME/$DISTRO_NAME.tar.gz"; \
    export GNUPGHOME="$(mktemp -d)"; \
    tar -xzf "$DISTRO_NAME.tar.gz"; \
    chown -R "$STORM_USER:$STORM_USER" "$DISTRO_NAME"; \
    rm "$DISTRO_NAME.tar.gz";

ENV PATH $PATH:/$DISTRO_NAME/bin

ADD storm.yaml /conf

RUN chown -R "$STORM_USER" "$STORM_CONF_DIR" "$STORM_DATA_DIR" "$STORM_LOG_DIR"

#now mount and run the jar

#setup file structure

RUN mkdir /theproject
WORKDIR /theproject
RUN mkdir target

#set environment variables

ENV BDE_SQL_USERNAME=bde
ENV BDE_SQL_PASSWORD=twitterstorm

#mount the jar and run

ADD target/twitter_storm-1.0-SNAPSHOT.jar target/twitter_storm-1.0-SNAPSHOT.jar

CMD storm jar target/twitter_storm-1.0-SNAPSHOT.jar com.bde.twitter_storm.App EtlxxJXmRs8hfr7TS9Y4PzDHN EBIhCbAmchKcQCNqjKrFIGgIHzaeC7gErclp8oZyZh7ASZKEgW 2707728705-w1cuPeV4GXRMmU6jA1l6egTHoHsZL9ViaFYpfrP MYtc6XblqwrFuUlhWupJYAPvNjnA5e4TugI9J1LxscrBz