FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-monitor

WORKDIR /gjk-monitor

EXPOSE 5001

ADD ./gjk-visual/gjk-monitor/target/gjk-monitor.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-monitor.jar
