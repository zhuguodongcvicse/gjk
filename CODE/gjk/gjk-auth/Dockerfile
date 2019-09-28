FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-auth

WORKDIR /gjk-auth

EXPOSE 3000

ADD ./pig-auth/target/gjk-auth.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-auth.jar
