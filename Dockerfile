FROM java:11-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=./target/modules
COPY ${DEPENDENCY}/jentle-1.0.0-RELEASE.jar /usr/app/boor.jar
WORKDIR /usr/app
RUN sh -c 'touch demo-docker-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","boot.jar"]
##CMD java -XX:+UseConcMarkSweepGC -XX:NativeMemoryTracking=summary -XX:MaxPermSize=256m -Xmx2g -Xms32m -jar boot.jar
