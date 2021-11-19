FROM ubuntu:18.04
RUN apt update && apt install -y openjdk-11-jre-headless
RUN java --version
RUN apt install -y maven
RUN mvn -version 
WORKDIR /app-compiled
ARG ENV
COPY . .
RUN mvn clean install -Dmaven.test.skip=true
CMD java -jar /app-compiled/target/f2x-0.0.1-SNAPSHOT.jar && exec /bin/bash -c "trap : TERM INT; sleep infinity & wait"
EXPOSE 9091