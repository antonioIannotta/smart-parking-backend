FROM azul/zulu-openjdk:17


EXPOSE 8080/tcp

ADD . parking-system-backend
WORKDIR parking-system-backend
RUN ./gradlew assemble
CMD ./gradlew run
