FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-libs

WORKDIR /gjk-libs

EXPOSE 4200

ADD ./gjk-libs/gjk-libs-biz/target/gjk-libs-biz.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-libs-biz.jar
