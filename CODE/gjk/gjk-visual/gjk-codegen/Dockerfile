FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-codegen

WORKDIR /gjk-codegen

EXPOSE 5003

ADD ./gjk-visual/gjk-codegen/target/gjk-codegen.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-codegen.jar
