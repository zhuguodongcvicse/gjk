FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-gateway

WORKDIR /gjk-gateway

EXPOSE 9999

ADD ./gjk-gateway/target/gjk-gateway.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-gateway.jar
