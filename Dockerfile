#COMPILE_STAGE
FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app
RUN apt update && apt install -y unzip
ENTRYPOINT ["/bin/bash", "-c", "\
    wget -O solution.zip \"$0\" && unzip solution.zip && \
    wget -O test.zip \"$1\" && unzip test.zip && \
    mv test solution/src/test && \
    cd solution && \
    mvn clean compile \
"]