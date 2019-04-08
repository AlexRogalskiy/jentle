FROM java:11-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=./target/modules
COPY ${DEPENDENCY}/jentle-collections-1.0.0.jar /usr/app/boor.jar
WORKDIR /usr/app
RUN sh -c 'touch demo-docker-0.0.1-SNAPSHOT.jar'
ENTRYPOINT [
"java",
"-Djava.security.egd=file:/dev/./urandom",
"-XX:MaxPermSize=256m",
"-XX:NativeMemoryTracking=summary",
"-XX:+UseConcMarkSweepGC",
"-Xmx2g",
"-Xms32m",
"-jar",
"boot.jar"
]
