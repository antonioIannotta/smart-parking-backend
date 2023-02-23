FROM ubuntu:22.04


EXPOSE 8888/tcp

# Necessario creare il Jar, portarlo sulla macchina virtuale ubuntu e lanciarlo
RUN apt update
RUN apt install -y git
RUN apt install -y snapd
RUN apt install -y default-jre
RUN apt install -y kotlin
RUN git clone https://github.com/antonioIannotta/parking-system-backend.git
WORKDIR parking-system-backend
RUN git checkout master
RUN ./gradlew
RUN kotlinc src/main/kotlin/it/unibo/lss/parking_system/framework/Application.kt -include-runtime -d Application.jar
RUN java -jar Application.jar

