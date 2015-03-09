#
# Semester build container
#

FROM ubuntu:trusty
MAINTAINER Takayuki Okazaki (https://github.com/watermint)

RUN apt-get update
RUN apt-get install -y wget curl apt-transport-https software-properties-common

RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections

RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN apt-get install -y oracle-java8-installer oracle-java8-set-default
RUN apt-get install -y scala

WORKDIR /var/tmp
RUN wget https://dl.bintray.com/sbt/debian/sbt-0.13.7.deb
RUN dpkg -i *.deb

RUN useradd -u 1000 -m semester
ADD . /var/semester

USER semester
WORKDIR /var/semester

ENTRYPOINT sbt test
