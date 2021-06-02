FROM jingyuzhao/jdk1.8
COPY target/blog-rocketmq-1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]