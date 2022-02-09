FROM openjdk:8
EXPOSE 8080
ADD target/daas-securityxref.jar daas-securityxref.jar
COPY target/matcher-config.json matcher-config.json
ENTRYPOINT ["java","-jar","/daas-securityxref.jar"]