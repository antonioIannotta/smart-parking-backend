FROM alpine/3.14


EXPOSE 8888/tcp

# Necessario creare il Jar, portarlo sulla macchina virtuale ubuntu e lanciarlo
RUN apk update
RUN apk add git
RUN apk add snapd
RUN apk add default-jre
RUN snap install --classic kotlin
RUN git clone https://github.com/antonioIannotta/parking-system-backend.git
RUN cd parking-system-backend
RUN git checkout -b master
RUN kotlinc src/Application.kt -include-runtime -d Application.jar
RUN java -jar Application.jar

