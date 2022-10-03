# define base docker image
FROM openjdk:11
LABEL maintainer="nikolaj"
ADD target/payments-0.0.1-SNAPSHOT.jar payments.jar
ENTRYPOINT ["java", "-jar", "payments.jar"]