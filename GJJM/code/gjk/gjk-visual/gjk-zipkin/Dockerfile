FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wangiegie@gmail.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /gjk-zipkin

WORKDIR /gjk-zipkin

EXPOSE 5002

ADD ./gjk-visual/gjk-zipkin/target/gjk-zipkin.jar ./

CMD java -Djava.security.egd=file:/dev/./urandom -jar gjk-zipkin.jar
