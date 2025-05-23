# First stage: build the project and create a custom JRE
FROM maven:3-eclipse-temurin-21 AS jre-builder

WORKDIR /usr/src/project
COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests && mv target/*.jar app.jar
RUN jar xf app.jar

RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 21  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    app.jar > deps.info

RUN echo "$(sed -r '/^\s*$/d' deps.info),jdk.crypto.ec" > deps2.info

RUN jlink \
    --add-modules $(cat deps2.info) \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /optimized-jre

# Second stage: extract the JAR file
FROM eclipse-temurin:21-jre AS extractor
WORKDIR /opt/app
COPY --from=jre-builder /usr/src/project/app.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Third stage: use custom JRE
FROM debian:12.8-slim

ENV JAVA_HOME=/opt/java/jdk21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=jre-builder /optimized-jre $JAVA_HOME

# Set the user to run the application
ARG APPLICATION_USER=spring

# Create a user to run the application
RUN addgroup --system $APPLICATION_USER && adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER

# Create a directory for the application
WORKDIR /project
RUN chown -R $APPLICATION_USER /project

COPY --from=extractor /opt/app/dependencies/ ./
COPY --from=extractor /opt/app/spring-boot-loader/ ./
COPY --from=extractor /opt/app/snapshot-dependencies/ ./
COPY --from=extractor /opt/app/application/ ./

USER $APPLICATION_USER

ENV SPRING_OPTS=""

EXPOSE 8080
CMD java -XX:+UseParallelGC -XX:MaxRAMPercentage=75 org.springframework.boot.loader.launch.JarLauncher $SPRING_OPTS
