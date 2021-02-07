FROM openjdk:11

WORKDIR /usr/src/app

COPY target/Schnorr-*.jar /Schnorr.jar
CMD ["java", "-jar", "Schnorr.jar"]