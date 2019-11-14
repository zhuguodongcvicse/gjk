FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-project

WORKDIR /gjk-project

EXPOSE 4100

ADD ./gjk-project/gjk-project-biz/target/gjk-project-biz.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-project-biz.jar
