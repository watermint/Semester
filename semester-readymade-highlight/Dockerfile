#
# Builder of Highlight.js packed
#

FROM ubuntu:trusty
MAINTAINER Takayuki Okazaki (https://github.com/watermint)

RUN apt-get update
RUN apt-get install -y git curl build-essential
RUN curl -sL https://deb.nodesource.com/setup | bash -
RUN apt-get install -y nodejs python
RUN node -v

RUN useradd -m highlight
USER highlight

WORKDIR /home/highlight
RUN git clone https://github.com/isagalaev/highlight.js.git
WORKDIR /home/highlight/highlight.js
RUN git checkout -b target 8.4

RUN npm install
ENTRYPOINT node tools/build.js

# docker cp <Container_Id>:/home/highlight/highlight.js/build/highlight.pack.js src/main/resources/

