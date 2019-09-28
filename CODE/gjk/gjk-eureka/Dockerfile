FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-eureka

WORKDIR /gjk-eureka

EXPOSE 8761

ADD ./gjk-eureka/target/gjk-eureka.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-eureka.jar
