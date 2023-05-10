FROM azul/zulu-openjdk:17

ENV PARKING_SLOT_DB_CONNECTION_STRING=mongodb://localhost/
ENV USER_DB_CONNECTION_STRING=mongodb://localhost/
ENV HASHING_SECRET=samplesecret

EXPOSE 8080/tcp

ADD . parking-system-backend
WORKDIR parking-system-backend
RUN rm application.properties
RUN touch application.properties
RUN echo "parkingSlotDbConnectionString=${PARKING_SLOT_DB_CONNECTION_STRING}" >> application.properties
RUN echo "userDbConnectionString=${USER_DB_CONNECTION_STRING}" >> application.properties
RUN echo "hashingSecret=${HASHING_SECRET}" >> application.properties

RUN ./gradlew assemble
CMD ./gradlew run
